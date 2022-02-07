/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solution;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import instance.reseau.Route;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author yanni
 */
public class Tournee {
    private final Depot depot; //Ne change pas -> final
    private final int capacite; //Ne change pas -> final
    private int demandeTotale; //Change à la modification des clients visités
    private int coutTotal; //Change à la modification des clients visité
    private LinkedList<Client> clients; //Change car on modifie cet ensemble
    
    public Tournee(Instance i){
        this.depot = i.getDepot();
        this.capacite = i.getCapacite();
        this.demandeTotale = 0;
        this.coutTotal = 0;
        this.clients = new LinkedList<>();
    }

    public Depot getDepot() {
        return depot;
    }

    public int getCapacite() {
        return capacite;
    }

    public int getDemandeTotale() {
        return demandeTotale;
    }

    public int getCoutTotal() {
        return coutTotal;
    }

    public List<Client> getClients() {
        return clients;
    }


    public void setDemandeTotale(int demandeTotale) {
        this.demandeTotale = demandeTotale;
    }

    public void setCoutTotal(int coutTotal) {
        this.coutTotal = coutTotal;
    }

    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.depot);
        hash = 59 * hash + Objects.hashCode(this.clients);
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
        final Tournee other = (Tournee) obj;
        if (!Objects.equals(this.depot, other.depot)) {
            return false;
        }
        return Objects.equals(this.clients, other.clients);
    }
    
    
    private boolean ajoutClientPossible(Client clientToAdd){
        int id = clientToAdd.getId();
        
        if(clientToAdd == null){
            return false;
        }
        
        if(this.demandeTotale + clientToAdd.getDemande() > this.capacite){
            return false;
        }
        else{
            return true;
        }
    }
    
    /**
     * Permet d'ajouter un client en vérifiant:
     *      Client non null
     *      Client n'est pas déjà inséré
     *      Capacité du camion pas dépassée
     * @param clientToAdd
     * @return 
     */
    public boolean ajouterClient(Client clientToAdd){   
        int id = clientToAdd.getId();
        if(ajoutClientPossible(clientToAdd)){
            
            this.demandeTotale += clientToAdd.getDemande(); //maj demande totale (addition)
           
            if(this.clients.isEmpty()){//Ajout dans une tournee qui n'a pas de client (Figure 3a)
                this.depot.ajouterRoute(clientToAdd);//Route aller
                clientToAdd.ajouterRoute(this.depot);//Route retour
                int coutAller = this.depot.getCoutVers(clientToAdd);
                int coutRetour = clientToAdd.getCoutVers(this.depot);
                this.coutTotal = coutAller + coutRetour; //Cout est juste un aller-retour (Figure 3b)
            }
            else{ //Ajout dans une tournée qui contient déjà des clients (Figure 3c)
                
                Client lastClient = this.clients.getLast();
                
                lastClient.ajouterRoute(clientToAdd);
                clientToAdd.ajouterRoute(this.depot);
                
                //Calcul du cout (Figure 3d)
                int coutVersNewClient = lastClient.getCoutVers(clientToAdd);
                int coutNewClientVersDepot = clientToAdd.getCoutVers(this.depot);
                int coutARetirer = lastClient.getCoutVers(this.depot);
                
                this.coutTotal += coutVersNewClient + coutNewClientVersDepot - coutARetirer;                              
            }  
            this.clients.addLast(clientToAdd);
            return true;
        }
        else{
            return false;
        }
    }
    
    
    /**
     * Checker de la class Tournee
     * @return 
     */
    public boolean check(){
        return verifCout() && verifCapacite() && verifDemande();
    }
    
    /**
     * Verifie si la demande totale et le coût total sont correctement calculés 
     * @return 
     */
    private boolean verifCout(){
        var coutAverif = this.coutTotal;
        var coutReel = 0;
        
        coutReel += this.depot.getCoutVers(this.clients.getFirst());//Depot vers premier
        for(int i = 0; i < this.clients.size()-1 ; i++){
            Client courant  = this.clients.get(i);
            Client suivant  = this.clients.get(i+1);
            
            coutReel += courant.getCoutVers(suivant);
        }
        coutReel += this.clients.getLast().getCoutVers(this.depot);
        
        System.out.println("Cout à vérifier: "+ coutAverif+" Cout réel: "+coutReel);
        
        return (coutAverif == coutReel);
    }
    
    private boolean verifDemande(){
        var demandeAverif = this.demandeTotale;
        var demandeReel = 0;
        
        for(Client cli: this.getClients()){
            demandeReel += cli.getDemande();
        }
        
        System.out.println("Demande à vérifier: "+demandeAverif+" Demande réelle: "+demandeReel);
        
        
        return (demandeAverif == demandeReel);
    }
    
    /**
     * Verifie si la demande totale est inférieure ou égale à la capacité.
     * @return 
     */
    private boolean verifCapacite(){
        System.out.println("Demande: "+this.demandeTotale+" Capacite: "+this.getCapacite());
        return this.demandeTotale<=this.getCapacite();
    }

    @Override
    public String toString() {
        String s = "Tournee{" + "depot=" + depot + ", capacite=" + capacite + ", demandeTotale=" + demandeTotale + ", coutTotal=" + coutTotal + ", clients=";
        
        for(Client c: clients){
            s += "\n\t"+c.toString();
        }
        
        s += "\n}"; 
        return s;
    }
    
    
    
    public static void main(String[] args) {
        try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();
            
            Client newClient = i.getClients().getFirst();//On teste avec le premier Client
            
            Client newClient2 = i.getClients().getLast();//On teste avec le premier Client
            
            System.out.println(newClient.equals(newClient2));
            
            Tournee t = new Tournee(i);
            System.out.println(t.toString());
            
            //On essaie d'ajoutes tous les clients en une tournees
            for(Client cli : i.getClients()){
                t.ajouterClient(cli);
            }
            
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
        
        
            
    }
    
}
