/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import solution.Tournee;

/**
 *
 * @author yanni
 */
public class InterEchange extends OperateurInterTournees {

    public InterEchange() {
        super();
    }

    public InterEchange(Tournee tournee, Tournee autreTournee, int positionI, int positionJ) {
        super(tournee, autreTournee, positionI, positionJ);
    }
    
    @Override
    protected int evalDeltaCoutTournee() {
        if(this.tournee == null ) return Integer.MAX_VALUE;
        return this.tournee.deltaCoutRemplacementInter(positionI, clientJ);
    }

    @Override
    protected int evalDeltaCoutAutreTournee() {
        if(this.autreTournee == null ) return Integer.MAX_VALUE;
        return this.autreTournee.deltaCoutRemplacementInter(positionJ, clientI);
    }

    @Override
    public boolean isTabou(OperateurLocal operateur) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected boolean doMouvement() {
        return this.tournee.doEchange(this);
    }

    @Override
    public String toString() {
        return "InterEchange{" +  "positionI=" + positionI +  ", positionJ=" + positionJ + ", coutDeplacementGlobal=" + deltaCout +","
                + "\n coutDeplacementTournee=" + deltaCoutTournee 
                + ",\n coutDeplacementAutre=" + deltaCoutAutreTournee +", \nclientI="+ clientI +", clientJ="+ clientJ+", Tournee="+tournee+", autreTournee="+autreTournee+ '}';
    }
    
    
    
}
