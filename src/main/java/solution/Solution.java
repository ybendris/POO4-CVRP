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
    

    public boolean ajouterClientNouvelleTournee(Client clientToAdd){
        Tournee nouvelleTournee = new Tournee(this.instance);
        if(nouvelleTournee.ajouterClient(clientToAdd)){
            this.coutTotal += nouvelleTournee.getCoutTotal();
            this.tournees.add(nouvelleTournee);
            return true;
        }
        return false;
    }
    
    public boolean ajouterClientTourneeExistante(Client clientToAdd){
        
        int deltaCout = 0;
        //Parcours des tournees existantes
        for(Tournee t : this.tournees){
            deltaCout = t.getCoutTotal();//15
            if(t.ajouterClient(clientToAdd)){
                this.coutTotal += t.getCoutTotal() - deltaCout;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checker de la class Solution
     * @return 
     * 
     * Une solution est réalisable si :
     *   • ses tournées sont toutes réalisables ;
     *   • le coût total de la solution est correctement calculé ;
     *   • tous les clients sont présents dans exactement une seule des tournées de la solution
     */
    public boolean check(){
        return verifTournee() && verifCoutTotal() && verifClients();
    }
    
    private boolean verifTournee(){
       for(Tournee t : this.tournees){ //Ses tournées sont toutes réalisables
            if(!t.check())
                return false;
       }
       return true;
    }
    
    private boolean verifCoutTotal(){
        var coutAverif = this.coutTotal;
        var coutReel = 0;
        for(Tournee t : this.tournees){ //Ses tournées sont toutes réalisables
            coutReel+= t.getCoutTotal();
        }
        
        System.out.println("Cout à vérifier: "+ coutAverif+" Cout réel: "+coutReel);
        return coutAverif == coutReel;
    }
    
    private boolean verifClients(){
        LinkedList<Client> clientsAverif = this.instance.getClients();
        
        for(Client cli : clientsAverif){
            for(Tournee t : this.tournees){
                if(t.getClients().contains(cli)){
                   //clientsAverif.remove(cli);
                   
                }
            }
           
           
        }
        
        return true;
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
          
            Solution s = new Solution(i);
            
            for(Client cli : i.getClients()){
                if(s.ajouterClientTourneeExistante(cli)){
                }
                else{
                    s.ajouterClientNouvelleTournee(cli);
                }
            }
            System.out.println(s.toString());
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
    }
            
    
    
    
    
    
    
}
