/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solution;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import instance.reseau.Point;
import instance.reseau.Route;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import operateur.FusionTournees;
import operateur.InsertionClient;

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

    public LinkedList<Client> getClients() {
        return new LinkedList<Client>(clients);
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
        if(ajoutClientPossible(clientToAdd)){
            this.demandeTotale += clientToAdd.getDemande(); //maj demande totale (addition)
            this.coutTotal += deltaCoutInsertionFin(clientToAdd);
            this.clients.addLast(clientToAdd);
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Doit être compris entre 0 et le nombre de clients
     * @param position
     * @return 
     */
    private Point getPrec(int position){
        if(position == 0) return this.depot;
        return this.clients.get(position-1);
    }
    
    private Point getCurrent(int position){
        if(position == this.getNbClients()) return this.depot;
        return this.clients.get(position);
    }
    
    private int getNbClients(){
        return this.clients.size();
    }

    private boolean isPositionInsertionValide(int position){
        if(0 <= position && position <= this.getNbClients()){
            return true;
        }
        return false;
    }
    
    /**
     * Donne le coût si on veut ajouter un client à une position donnée
     * @param position
     * @param clientToAdd
     * @return 
     */
    public int deltaCoutInsertion(int position, Client clientToAdd){
        if(!isPositionInsertionValide(position))
            return Integer.MAX_VALUE;
        if(clientToAdd == null)
            return Integer.MAX_VALUE;

        int deltaCout = 0;
        
        if(this.clients.isEmpty()){ //Ajout dans une tournee qui n'a pas de client
            deltaCout = this.depot.getCoutVers(clientToAdd);
            deltaCout += clientToAdd.getCoutVers(this.depot);
            
            //deltaCout est juste un aller-retour (Figure 3b)
        }
        else{ //Ajout dans une tournée qui contient déjà des clients à une certaine position
            Point avant = getPrec(position);
            Point apres = getCurrent(position);
            deltaCout += avant.getCoutVers(clientToAdd);
            deltaCout += clientToAdd.getCoutVers(apres);
            deltaCout -= avant.getCoutVers(apres);
        }
        return deltaCout;                              
    }
    
    public int deltaCoutInsertionFin(Client clientToAdd){
        return deltaCoutInsertion(this.getNbClients(),clientToAdd);
    }
    
    /**
     * Un cout négatif renvoyé par cette méthode est un gain positif, on améliore la solution
     * @param aFusionner
     * @return 
     */
    public int deltaCoutFusion(Tournee aFusionner){
        if(aFusionner == null)
            return Integer.MAX_VALUE;

        int deltaCout = 0;
        
        Point dernierClientTournee1 = this.clients.getLast();
        Point premierClientTournee2 = aFusionner.clients.getFirst();
        
        deltaCout += dernierClientTournee1.getCoutVers(premierClientTournee2);
        deltaCout -= premierClientTournee2.getCoutVers(this.depot);
        deltaCout -= this.depot.getCoutVers(dernierClientTournee1);
        
        return deltaCout;                              
    }
    
    
    public InsertionClient getMeilleureInsertion(Client clientToInsert){
        InsertionClient meilleur = new InsertionClient();
        if(!this.ajoutClientPossible(clientToInsert)) return meilleur;//return d'une valeur par défaut
        
        for(int pos = 0; pos<this.clients.size()+1; pos++){
            InsertionClient courrant = new InsertionClient(this, clientToInsert, pos);
            if(courrant.isMeilleur(meilleur))
                meilleur = courrant;
        }
        
        return meilleur;
    }
    
    public boolean doInsertion(InsertionClient infos){
        if(infos == null) return false;
        if(!infos.isMouvementRealisable()) return false;
        
        Client clientToAdd = infos.getClientToInsert();
        /**
         * Ajoute le client à la position indiqué par l'opérateur d'insertion
         */
        this.clients.add(infos.getPosition(), clientToAdd);
        this.coutTotal += infos.getDeltaCout(); //MAJ cout total
        this.demandeTotale += clientToAdd.getDemande(); //TODO demande totale
        
        if (!this.check()){
            System.out.println("Mauvaise insertion du client, "+clientToAdd);
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }
        
        return true;
    }
    
    
    public boolean fusionTourneesPossible(Tournee tourneeToFusion){
        if(tourneeToFusion == null) return false;
        /**
         * Capacité après fusion dépassée
         */
        if(this.demandeTotale + tourneeToFusion.demandeTotale > this.capacite) return false;
        /**
         * Deux tournees identiques
         */
        if(this.equals(tourneeToFusion)) return false;
        
        /**
         * Tournee courrante vide
         */
        if(this.getNbClients() == 0) return false;
        
        /**
         * Tournee à fusionner vide
         */
        if(tourneeToFusion.getNbClients() == 0) return false;
        
        return true;
    }
    
    
    
    public FusionTournees getMeilleureFusion(LinkedList<Tournee> autresTournees){
        FusionTournees meilleure = new FusionTournees();
               
        for(Tournee t : autresTournees){
            if(this.fusionTourneesPossible(t)){
                FusionTournees courrante = new FusionTournees(this,t);
                if(courrante.isMeilleur(meilleure))
                    meilleure = courrante;   
            }
        }
        
        return meilleure;
    }
              
    public boolean doFusion(FusionTournees infos){
        if(infos == null) return false;
        if(!infos.isMouvementRealisable()) return false;
        //if(!infos.isMouvementAmeliorant()) return false;
        
        Tournee tourneeToFusion = infos.getTourneeToFusion();
        /**
         * Ajoute tous les clients de la tournee a fusionner à la fin
         */
        this.clients.addAll(tourneeToFusion.getClients());
        /**
         * Il faut AJOUTER le delta cout
         */
        this.coutTotal += tourneeToFusion.getCoutTotal() + infos.getDeltaCout();
        this.demandeTotale += tourneeToFusion.getDemandeTotale();
        
        if (!this.check()){
            System.out.println("Mauvaise fusion des tournees, "+this.toString());
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }
        
        
        return true;
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
        for(int i = 0; i < this.getNbClients()-1 ; i++){
            Client courant  = this.clients.get(i);
            Client suivant  = this.clients.get(i+1);
            
            coutReel += courant.getCoutVers(suivant);
        }
        coutReel += this.clients.getLast().getCoutVers(this.depot);
        
        return (coutAverif == coutReel);
    }
    
    
    private boolean verifDemande(){
        var demandeAverif = this.demandeTotale;
        var demandeReel = 0;
        
        for(Client cli: this.getClients()){
            demandeReel += cli.getDemande();
        }
        
        if(demandeAverif == demandeReel){
            return true;
        }
        System.out.println("Erreur: la demande de la Tournee n'est pas égale à la somme des demandes des clients");
        return false;
    }
    
    /**
     * Verifie si la demande totale est inférieure ou égale à la capacité.
     * @return 
     */
    private boolean verifCapacite(){
        if(this.demandeTotale<=this.getCapacite()){
            return true;
        }
        System.out.println("Erreur: la demande totale est supérieure à la capacité de la Tournee");
        return false;
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
