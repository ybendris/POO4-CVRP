/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solution;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author yanni
 */
public class Tournee {
    private final Depot depot;
    private final int capacite;
    private int demandeTotale;
    private int coutTotal;
    private Map<Integer,Client> clients;
    
    public Tournee(Instance i){
        this.depot = i.getDepot();
        this.capacite = i.getCapacite();
        this.demandeTotale = 0;
        this.coutTotal = 0;
        this.clients = new LinkedHashMap<>();
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

    public Map<Integer, Client> getClients() {
        return clients;
    }

    public void setDemandeTotale(int demandeTotale) {
        this.demandeTotale = demandeTotale;
    }

    public void setCoutTotal(int coutTotal) {
        this.coutTotal = coutTotal;
    }

    public void setClients(Map<Integer, Client> clients) {
        this.clients = clients;
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
