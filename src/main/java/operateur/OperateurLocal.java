/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operateur;

import instance.reseau.Client;
import solution.Tournee;

/**
 *
 * @author lucas
 */
public abstract class OperateurLocal extends Operateur{
    
    protected int positionI;
    protected int positionJ;
    protected Client clientI;
    protected Client clientJ;

    public OperateurLocal() {
        this.positionI = -1;
        this.positionJ = -1;      
    }
    
    public OperateurLocal(Tournee tournee, int positionI, int positionJ) {
        super(tournee);
        this.positionI = positionI;
        this.positionJ = positionJ;
        this.clientI = this.tournee.getClientByPosition(positionI);
        this.clientJ = this.tournee.getClientByPosition(positionJ);
    }
    
    public int getPositionI() {
        return positionI;
    }

    public int getPositionJ() {
        return positionJ;
    }

    public Client getClientI() {
        return clientI;
    }

    public Client getClientJ() {
        return clientJ;
    }
     
    
    public static OperateurLocal getOperateur(TypeOperateurLocal type){
        switch(type){
            case INTER_DEPLACEMENT:
                return new InterDeplacement();
            case INTER_ECHANGE:
                //return new InterEchange();
            case INTRA_DEPLACEMENT:
                return new IntraDeplacement();
            case INTRA_ECHANGE:
                return new IntraEchange();
            default:
                return null;
        }
    }
    
    public static OperateurIntraTournee getOperateurIntra(TypeOperateurLocal type, Tournee tournee, int positionI, int positionJ) {
        switch(type) {
            case INTRA_DEPLACEMENT:
                return new IntraDeplacement(tournee, positionI, positionJ);
            case INTRA_ECHANGE:
                return new IntraEchange(tournee, positionI, positionJ);
            default:
                return null;
        }
    }
    
     public static OperateurInterTournees getOperateurInter(TypeOperateurLocal type, Tournee tournee, Tournee autreTournee, int positionI, int positionJ) {
        switch(type) {
            case INTER_DEPLACEMENT:
                return new InterDeplacement(tournee, autreTournee ,positionI, positionJ);
            case INTER_ECHANGE:
                //return new InterEchange(tournee, positionI, positionJ);
            default:
                return null;
        }
    }

    public abstract boolean isTabou(OperateurLocal operateur);
     
     
    @Override
    public String toString() {
        return "OperateurLocal{" + "tournee=" + tournee + "positionI=" + positionI + ", positionJ=" + positionJ + ", clientI=" + clientI + ", clientJ=" + clientJ + "deltaCout=" + deltaCout + '}';
    }
}
