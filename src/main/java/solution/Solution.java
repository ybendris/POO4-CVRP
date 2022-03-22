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
import operateur.FusionTournees;
import operateur.InsertionClient;
import operateur.OperateurInterTournees;
import operateur.OperateurIntraTournee;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;

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
    private LinkedList<Tournee> tournees;
    
    
    
    
    public Solution(Instance i){
        this.coutTotal = 0;
        this.instance = i;
        this.tournees = new LinkedList<>();
    }
    
    public Solution(Solution s){
        this.coutTotal = s.coutTotal;
        this.instance = s.instance;
        this.tournees = new LinkedList<Tournee>();
        for(Tournee tourneeToAdd : s.getTournees()){
            this.tournees.add(new Tournee(tourneeToAdd));
        }
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
            deltaCout = t.getCoutTotal();
            if(t.ajouterClient(clientToAdd)){
                this.coutTotal += t.getCoutTotal() - deltaCout;
                return true;
            }
        }
        return false;
    }
    
    public boolean ajouterClientDerniereTournee(Client clientToAdd){
        int deltaCout = 0;
        
        if(this.tournees.size() == 0){
            return false;
        }
        
        Tournee derniereTournee = this.tournees.getLast();
        
        deltaCout = derniereTournee.getCoutTotal();
        if(derniereTournee.ajouterClient(clientToAdd)){
            this.coutTotal += derniereTournee.getCoutTotal() - deltaCout;
            return true;
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
        return verifTournees() && verifCoutTotal() && verifClients();
    }
    
    private boolean verifTournees(){
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
        
        //System.out.println("Cout à vérifier: "+ coutAverif+" Cout réel: "+coutReel);
        return coutAverif == coutReel;
    }
    
    public InsertionClient getMeilleureInsertion(Client clientToInsert){
        InsertionClient meilleur = new InsertionClient();
        
        for(Tournee t : this.tournees){
            InsertionClient courrant = t.getMeilleureInsertion(clientToInsert);
            if(courrant.isMeilleur(meilleur)) meilleur = courrant;
        }
        
        return meilleur;
    }
    
    private OperateurLocal getMeilleurOperateurIntra(TypeOperateurLocal type){
        OperateurLocal best = OperateurLocal.getOperateur(type);
        for(Tournee t : this.tournees){
            OperateurLocal op = t.getMeilleurOperateurIntra(type);
            if(op.isMeilleur(best)) {
                best = op;
            }
        }
        return best;
    }
    
    private OperateurLocal getMeilleurOperateurInter(TypeOperateurLocal type){
        OperateurLocal best = OperateurLocal.getOperateur(type);
        for(Tournee t1 : this.tournees){
            for(Tournee t2 : this.tournees){
                OperateurLocal op = t1.getMeilleurOperateurInter(t2,type);
                if(op.isMeilleur(best)) {
                    best = op;
                }
            }
        }
        return best;
    }

    
    public OperateurLocal getMeilleurOperateurLocal(TypeOperateurLocal type) {
        if(OperateurLocal.getOperateur(type) instanceof OperateurIntraTournee){
            return this.getMeilleurOperateurIntra(type);
        }
        else if(OperateurLocal.getOperateur(type) instanceof OperateurInterTournees){
            return this.getMeilleurOperateurInter(type);
        }
        else{
            return null;
        }
    }
    
    
    
    public boolean doInsertion(InsertionClient infos){
        if(infos == null) return false;
        if(!this.tournees.contains(infos.getTournee())) return false;
        if(!infos.doMouvementIfRealisable())return false;
        
        this.coutTotal += infos.getDeltaCout();
        
        return true;
    }
    
    public boolean doFusion(FusionTournees infos){
        if(infos == null) return false;
        if(!infos.doMouvementIfRealisable())return false;
        this.coutTotal += infos.getDeltaCout();
        
        /**
         * Suppression de la solution, la tournée qui a été fusionnée
         */
        this.tournees.remove(infos.getTourneeToFusion());
             
        
        return true;
    }
    
    public boolean doMouvementRechercheLocale(OperateurLocal infos){
        if(infos == null) return false;
        if(!infos.doMouvementIfRealisable())return false;
        this.coutTotal += infos.getDeltaCout();
        
        
        if (!this.check()){
            System.out.println("Mauvais mouvement recherche locale, "+this.toString());
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }
        
        return true;
    }
    
    public FusionTournees getMeilleurFusion(){
        FusionTournees meilleure = new FusionTournees();
        for(Tournee t1 : this.tournees){
            FusionTournees courrante = t1.getMeilleureFusion(this.getTournees());
            if(courrante.isMeilleur(meilleure)) meilleure = courrante;
        }
        
        
        return meilleure;
    }
    
    
    
    private boolean verifClients(){
        List<Client> clientsAverif = this.instance.getClients();
        
        for(Tournee t : this.tournees){
            for(Client cli : t.getClients()){
                if(!clientsAverif.remove(cli)){
                    System.out.println("Erreur on livre client plusieur fois");
                    return false;
                }
            }
        }
        if(!clientsAverif.isEmpty()){
            System.out.println("Un client n'a pas été livré");
            return false;
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

    public int getCoutTotal() {
        return coutTotal;
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

    public LinkedList<Tournee> getTournees() {
        return tournees;
    }
    
    
    
    public static void main(String[] args) {
        try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();
          
            Solution s = new Solution(i);
            Solution s2 = new Solution(s);
            
            
            for(Client cli : i.getClients()){
                if(s.ajouterClientTourneeExistante(cli)){
                }
                else{
                    s.ajouterClientNouvelleTournee(cli);
                }
            }
            
            System.out.println("Solution intiale: "+s.toString());
            System.out.println("Solution copiée: "+s2.toString());
            
            
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
    }
    
}
