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
        if(operateur == null) return false;
        if(!(operateur instanceof InterEchange)) return false;
        if(operateur.tournee == null || operateur.clientI == null || operateur.clientJ == null) return false;
        
        if(this.clientI.equals(operateur.clientI) || this.clientJ.equals(operateur.clientJ))
            return true;
        
        return false;
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
