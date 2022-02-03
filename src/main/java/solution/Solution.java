/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solution;

import instance.Instance;
import instance.reseau.Client;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author yanni
 */
public class Solution {
    /**
     * Somme des coûts des tournées
     */
    private int coutTotal;
    private Instance instance;
    private List<Tournee> tournees;
    
    
    public Solution(Instance i){
        this.coutTotal = 0;
        this.instance = i;
        this.tournees = new LinkedList<>();
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Solution other = (Solution) obj;
        if (this.coutTotal != other.coutTotal) {
            return false;
        }
        if (!Objects.equals(this.instance, other.instance)) {
            return false;
        }
        return Objects.equals(this.tournees, other.tournees);
    }

    @Override
    public String toString() {
        String s = "Solution{" + "coutTotal=" + coutTotal + ", instance=" + instance + ", tournees=";
        
        for (int i = 0; i < this.tournees.size(); i++) {
            s += "\n"+this.tournees.get(i).toString();
        }
        s += "\n}"; 
        return s;
    }
    
    
    public static void main(String[] args) {
        try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();
            
            Client newClient = i.getClients().getFirst();//On teste avec le premier Client
            Client newClient2 = i.getClients().getLast();//On teste avec le dernier Client
            
            
            Tournee t = new Tournee(i);
            t.ajouterClient(newClient);
            t.ajouterClient(newClient2);
            
           
            
            
            
            
            Tournee t2 = new Tournee(i);
            for(Client cli : i.getClients()){
                if(t2.ajouterClient(cli)){
                    //System.out.println(t.toString());
                }
                else{
                    //System.out.println("On ne peut pas ajouter le client: "+cli.toString());
                }
            }
            
            System.out.println(t.toString());
            System.out.println(t2.toString());
            
            
            
            Solution s = new Solution(i);
            
            
            s.tournees.add(t);
            s.tournees.add(t2);
            
            System.out.println(s.toString());
            
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
    }
            
    
    
    
    
    
    
}
