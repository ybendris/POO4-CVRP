/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instance;

import instance.reseau.Client;
import instance.reseau.Depot;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import io.InstanceReader;
import io.exception.ReaderException;


/**
 *
 * @author yanni
 */
public class Instance {
    /**
     * Le nom doit être unique
     */
    private final String nom; 
    /**
     * Capacité de tout les véhicules
     */
    private final int capacite;
    private final Depot depot;
    /**
     * La clé correspond à l'id du client (supposé Unique)
     */
    private final Map<Integer,Client> clients;

    public Instance(String nom, int capacite, Depot depart) {
        this.nom = nom;
        this.capacite = capacite;
        this.depot = depart;
        this.clients = new LinkedHashMap<>();
    }

    public String getNom() {
        return nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public Depot getDepot() {
        return depot;
    }
    
    
    public int getNbClients(){
        return this.clients.size();
    }
    
    /**
     * Recherche en complexite O(1)
     */
    public Client getClientById(int id){
        return this.clients.get(id);
    }
    
    
    /**
     * @return  une copie de la liste des clients
     */ 
    public LinkedList<Client> getClients(){
        return new LinkedList<>(this.clients.values());
    }

    public boolean ajouterClient(Client clientToAdd){
        int id = clientToAdd.getId();
        
        if(clientToAdd == null){
            return false;
        }
        if(this.clients.containsKey(id)){
            return false;
        }
        
        creerRouteNouveauClient(clientToAdd);
        
        this.clients.put(id, clientToAdd);
        
        return true;
    }

    /**
     * Créer toute les routes possibles dans les deux sens
     * @param clientToAdd 
     */
    public void creerRouteNouveauClient(Client clientToAdd) {
        /**
         * Ajout la route du depot de l'instance vers le nouveau client
         */
        this.depot.ajouterRoute(clientToAdd);
        
        /**
         * Ajout la route retour du nouveau client vers le depot
         */
        clientToAdd.ajouterRoute(this.depot);
        
        for(Client c : this.clients.values()){
            c.ajouterRoute(clientToAdd);
            clientToAdd.ajouterRoute(c);
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.nom);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Instance other = (Instance) obj;
        return Objects.equals(this.nom, other.nom);
    }

    @Override
    public String toString() {
        String s = "Instance{" + "nom=" + nom +  ", capacite=" + capacite + ", depart=" + depot + ", clients=" ;
        for(Client c: clients.values()){
            s += "\n\t"+c.toString();
        }
        
        s += "\n}"; 
        return s;
    }
    
    
    public static void main(String[] args) {
        try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            
            Instance i = read.readInstance();
            
            System.out.println(i.toString());
            System.out.println(i.getNbClients());
           
            int somme=0;
            for(Client c : i.getClients()){
                somme += c.getDemande();
            }
            System.out.println(somme);
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
        
        
        
    }
    
    
}
