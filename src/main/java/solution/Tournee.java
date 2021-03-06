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
import operateur.InterDeplacement;
import operateur.InterEchange;
import operateur.IntraDeplacement;
import operateur.IntraEchange;
import operateur.ListeTabou;
import operateur.OperateurInterTournees;
import operateur.OperateurIntraTournee;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;

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

    public Tournee(Tournee t) {
        this.depot = t.depot;
        this.capacite = t.capacite;
        this.demandeTotale = t.demandeTotale;
        this.coutTotal = t.getCoutTotal();
        this.clients = t.getClients(); //ici on retourne bien une COPIE (avec new ans getClient)
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
    
    private boolean isPositionClient(int position){
        if(0 <= position && position < this.getNbClients()){
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
        if(!this.isPositionInsertionValide(position) || clientToAdd == null){
            return Integer.MAX_VALUE;
        }
        
        int deltaCout = 0;
        
        if(this.clients.isEmpty()){
            deltaCout = this.depot.getCoutVers(clientToAdd);
            deltaCout += clientToAdd.getCoutVers(this.depot);
        }
        else{
            Point cPrec = this.getPrec(position);
            Point cCour = this.getCurrent(position);
            
            deltaCout -= cPrec.getCoutVers(cCour);
            deltaCout += cPrec.getCoutVers(clientToAdd);
            deltaCout += clientToAdd.getCoutVers(cCour);
        }
        
        return deltaCout;
    }
    
    public int deltaCoutInsertionFin(Client clientToAdd){
        return deltaCoutInsertion(this.getNbClients(),clientToAdd);
    }
    
    public int deltaCoutInsertionInter(int position, Client clientToAdd){
        if(this.ajoutClientPossible(clientToAdd)){
            return this.deltaCoutInsertion(position, clientToAdd);
        }
        return Integer.MAX_VALUE;
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
    
    
    public boolean doEchange(IntraEchange infos){
        if(infos == null) return false;
        if(!infos.isMouvementRealisable()) return false; 
        
        int positionI = infos.getPositionI();
        int positionJ = infos.getPositionJ();
        
        Client ClientI = infos.getClientI();
        Client ClientJ = infos.getClientJ();
        
        this.clients.set(positionI, ClientJ);
        this.clients.set(positionJ, ClientI);
        
        this.coutTotal += infos.getDeltaCout(); //MAJ cout total
        
        if (!this.check()){
            System.out.println("Mauvais échange des clients");
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }
        
        return true;
    }
    
    public boolean doEchange(InterEchange infos){
        if(infos == null) return false;
        if(!infos.isMouvementRealisable()) return false; 
        
        int positionI = infos.getPositionI();
        int positionJ = infos.getPositionJ();
        
        Client clientI = infos.getClientI();
        Client clientJ = infos.getClientJ();
        
        Tournee autreTournee = infos.getAutreTournee();
        
        this.clients.set(positionI, clientJ);
        autreTournee.clients.set(positionJ, clientI);
        
        
        //maj cout
        this.coutTotal += infos.getDeltaCoutTournee();
        autreTournee.coutTotal += infos.getDeltaCoutAutreTournee();
        
        //maj demande
        this.demandeTotale = this.demandeTotale - clientI.getDemande() + clientJ.getDemande();
        autreTournee.demandeTotale = autreTournee.demandeTotale - clientJ.getDemande() + clientI.getDemande();
        
        
        
        
       if (!this.check()){
            System.out.println("Mauvais échange inter-tournee, (courante)"+this.toString()+"\n"+autreTournee.toString());
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }
        
        if (!infos.getAutreTournee().check()){
            System.out.println("Mauvais échange inter-tournee, (autre)"+autreTournee.toString());
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
    
    
    public int deltaCoutDeplacement(int positionI, int positionJ){
        //méthode avec une visibilité private
        if(!isPositionValide(positionI)){
            return Integer.MAX_VALUE;
        }
        
        if(!isPositionInsertionValide(positionJ))
            return Integer.MAX_VALUE;
        
        if(positionI == positionJ)
            return Integer.MAX_VALUE;

        if(Math.abs(positionI - positionJ) == 1) 
            return Integer.MAX_VALUE;
        
        int deltaCout = 0;
        
        Client cli = this.getClientByPosition(positionI);
        
        deltaCout = this.deltaCoutSuppression(positionI) + this.deltaCoutInsertion(positionJ, cli);
        
        return deltaCout;
    }
    
    public int deltaCoutEchange(int positionI, int positionJ) {
        if(!isPositionClient(positionI)){
            //System.out.println("posI Invalid");
            return Integer.MAX_VALUE;
        }
        if(!isPositionClient(positionJ)){
            //System.out.println("posJ Invalid");
            return Integer.MAX_VALUE;
        }
        if(positionI == positionJ){
            //System.out.println("posI = posJ");
            return Integer.MAX_VALUE;
        }
        if(!(positionI<positionJ)){
            //System.out.println("!(positionI<positionJ)");
            return Integer.MAX_VALUE;
        }
        
        
        if(positionJ-positionI == 1){
            //System.out.println("Consécutif");
            return deltaCoutEchangeConsecutif(positionI);
        }
        //System.out.println("Pas consécutif");
        return deltaCoutRemplacement(positionI,this.getClientByPosition(positionJ))+deltaCoutRemplacement(positionJ,this.getClientByPosition(positionI));
    }
    
    private int deltaCoutEchangeConsecutif(int positionI){
        int deltaCout = 0;
        
        Client clientI = this.getClientByPosition(positionI);
        Client clientJ = this.getClientByPosition(positionI+1);
        Point avantI = this.getPrec(positionI);
        Point apresJ = this.getNext(positionI+1);
        
        
        deltaCout -= avantI.getCoutVers(clientI);
        deltaCout -= clientI.getCoutVers(clientJ);
        deltaCout -= clientJ.getCoutVers(apresJ);
        
        deltaCout += avantI.getCoutVers(clientJ);
        deltaCout += clientJ.getCoutVers(clientI);
        deltaCout += clientI.getCoutVers(apresJ);
        
        
        return deltaCout;
    }
    
    private int deltaCoutRemplacement(int position, Client clientJ){
        int deltaCout = 0;

        Client clientI = this.getClientByPosition(position);
        
        Point avantI = this.getPrec(position);
        Point apresI = this.getNext(position);
        
        deltaCout-= avantI.getCoutVers(clientI);
        deltaCout-= clientI.getCoutVers(apresI);
        
        deltaCout+= avantI.getCoutVers(clientJ);
        deltaCout+= clientJ.getCoutVers(apresI);
        
        return deltaCout;
    }
    
    public int deltaCoutRemplacementInter(int position, Client clientJ){
        Client clientI = this.getClientByPosition(position);
        if(clientI == null){
            return Integer.MAX_VALUE;
        }
        if(clientJ == null){
            return Integer.MAX_VALUE;
        }
        if(this.demandeTotale - clientI.getDemande() + clientJ.getDemande() > this.capacite){
            return Integer.MAX_VALUE;
        }
        
        return deltaCoutRemplacement(position,clientJ);
    }
    
    
    public boolean doDeplacement(IntraDeplacement infos){
        if(infos == null) return false;
        if(!infos.isMouvementRealisable()) return false;
        
        int posI = infos.getPositionI();
        int posJ = infos.getPositionJ();
        
        Client clientI = infos.getClientI();
        
        
        //System.out.println("Avant: "+this);
        
        /**
         * En voulant l'insérer à la positionJ, cela va décaler le reste de la list (incrément de 1 de le leur index)
         */
        this.clients.add(posJ, clientI);
        
        if(posI < posJ){
            this.clients.remove(posI);
        }
        else{
            this.clients.remove(posI+1);
        }
        
        this.coutTotal += infos.getDeltaCout();
        
        //System.out.println("Apres: "+this);
        
        if (!this.check()){
            System.out.println("Mauvais déplacement intra-tournee, "+this.toString());
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }
        
        
        return true;
    }
    
    
    public boolean doDeplacement(InterDeplacement infos){
        if(infos == null) return false;
        if(!infos.isMouvementRealisable()) return false;
        
        
        int posI = infos.getPositionI();
        int posJ = infos.getPositionJ();
        
        Client clientI = infos.getClientI();
       
        Tournee autreTournee = infos.getAutreTournee();
        
        autreTournee.clients.add(posJ, clientI);
        this.clients.remove(posI);

        
        //maj cout
        this.coutTotal += infos.getDeltaCoutTournee();
        autreTournee.coutTotal += infos.getDeltaCoutAutreTournee();
        
        //maj demande
        this.demandeTotale -= clientI.getDemande();
        autreTournee.demandeTotale += clientI.getDemande();
        
        /*
        TODO:
        Modifier la liste des clients de la tournée courante 
        Modifier la liste des clients de l’autre tournée 
        Modifier coût total et demande totale des deux tournées.
        Appeler check pour les deux tournees
        */
        if (!this.check()){
            System.out.println("Mauvais déplacement inter-tournee, (courante)"+this.toString()+"\n"+autreTournee.toString());
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }
        
        if (!infos.getAutreTournee().check()){
            System.out.println("Mauvais déplacement inter-tournee, (autre)"+autreTournee.toString());
            System.out.println(infos);
            System.exit(-1); //Termine le programme
        }
        
        
        return true;
    } 
    
    
    /**
     * Renvoie null quand la position n'est pas correcte
     * @param pos
     * @return 
     */
    public Client getClientByPosition(int pos){
        if(pos < 0 || pos >= this.getNbClients()){
            return null;
        }
        return this.clients.get(pos);
    }
    
    
    private Point getNext(int position){
        if(position >= this.getNbClients()-1) return this.depot;
        return this.clients.get(position+1);
    }
    
    private boolean isPositionValide(int position){
        if(position <0 || position > this.getNbClients()+1){
            return false;
        }
        return true;
    }
    
    public int deltaCoutSuppression(int position){
        Point cPrec = this.getPrec(position);
        Point cCour = this.getCurrent(position);
        Point cNext = this.getNext(position);
        int deltaCoutSuppression = 0;
        
        if(!this.isPositionInsertionValide(position)){
            return Integer.MAX_VALUE;
        }
        
        if(this.clients.size()==1){
            deltaCoutSuppression -= cCour.getCoutVers(this.depot);
            deltaCoutSuppression -= this.depot.getCoutVers(cCour);
        }
        else{
            deltaCoutSuppression += cPrec.getCoutVers(cNext);
            deltaCoutSuppression -= cPrec.getCoutVers(cCour);
            deltaCoutSuppression -= cCour.getCoutVers(cNext);
        }
        return deltaCoutSuppression;
    }
    
    public OperateurLocal getMeilleurOperateurIntra(TypeOperateurLocal type) {
        OperateurLocal best = OperateurLocal.getOperateur(type);
        ListeTabou liste = ListeTabou.getInstance();
        for(int i=0; i<clients.size(); i++) {
            for(int j=0; j<clients.size()+1; j++) {
                OperateurIntraTournee op = OperateurLocal.getOperateurIntra(type, this, i, j);
                if(op.isMeilleur(best) && !liste.isTabou(op)) {
                    best = op;
                }
            }
        }
        return best;
    }

    
    public OperateurLocal getMeilleurOperateurInter(Tournee autreTournee, TypeOperateurLocal type){
        OperateurLocal best = OperateurLocal.getOperateur(type);
        ListeTabou liste = ListeTabou.getInstance(); 
        if(!this.equals(autreTournee)) {
            for(int i=0; i<clients.size(); i++) {
                for(int j=0; j<autreTournee.getNbClients()+1; j++) {
                    OperateurInterTournees op = OperateurLocal.getOperateurInter(type, this,autreTournee, i, j);
                    if(op.isMeilleur(best) && !liste.isTabou(op)) {
                        best = op;
                    } 
                }
            }
        }
        
        
        return best;
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
        
        if(!this.clients.isEmpty()){
            coutReel += this.depot.getCoutVers(this.clients.getFirst());//Depot vers premier
            /**
             * Le dernier client n'a pas de suivant, on s'arrette avant
             */
            for(int i = 0; i < this.getNbClients()-1 ; i++){
                Client courant  = this.clients.get(i);
                Client suivant  = this.clients.get(i+1);

                coutReel += courant.getCoutVers(suivant);
            }
            coutReel += this.clients.getLast().getCoutVers(this.depot);
        }
        if(coutAverif == coutReel){
            return true;
        }
        System.out.println("Erreur: le cout de la tournee n'est pas bon, on veut:"+coutAverif+", on a:"+ coutReel);
        return false;
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
