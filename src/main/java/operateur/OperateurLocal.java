/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import instance.reseau.Client;
import solution.Tournee;

/**
 *
 * @author yanni
 */
public abstract class OperateurLocal extends Operateur {
    protected final int positionI;
    protected final int positionJ;
    protected Client clientI;
    protected Client clientJ;

    
    public OperateurLocal() {
        super();
        this.positionI = -1;
        this.positionJ = -1;
    }

    public OperateurLocal(int positionI, int positionJ, Tournee tournee) {
        super(tournee);
        this.positionI = positionI;
        this.positionJ = positionJ;
        this.clientI = tournee.getClientByPosition(positionI);
        this.clientJ = tournee.getClientByPosition(positionJ);
    }

    

    @Override
    public String toString() {
        return "OperateurLocal{" + "positionI=" + positionI + ", positionJ=" + positionJ + ", clientI=" + clientI + ", clientJ=" + clientJ + '}';
    }

    
    public static OperateurLocal getOperateur(TypeOperateurLocal type) {
        switch(type) {
            case INTRA_DEPLACEMENT:
                return new IntraDeplacement();
            case INTRA_ECHANGE:
                return new IntraEchange();
            case INTER_DEPLACEMENT:
                return new InterDeplacement();
            case INTER_ECHANGE:
                return new InterEchange();
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
                return new InterDeplacement(tournee, autreTournee, positionI, positionJ);
            case INTER_ECHANGE:
                return new InterEchange(tournee, autreTournee, positionI, positionJ);
            default:
                return null;
        }
    }
    
    
    
}
