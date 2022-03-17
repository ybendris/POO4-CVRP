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
public class InterDeplacement extends OperateurInterTournees {
    public InterDeplacement() {
        super();
    }

    public InterDeplacement(Tournee tournee, Tournee autreTournee, int positionI, int positionJ) {
        super(tournee, autreTournee, positionI, positionJ);
    }
    
    @Override
    protected int evalDeltaCoutTournee() {
        if(this.tournee == null ) return Integer.MAX_VALUE;
        return this.tournee.deltaCoutSuppression(this.positionI);
    }

    @Override
    protected int evalDeltaCoutAutreTournee() {
        if(this.autreTournee == null ) return Integer.MAX_VALUE;
        return this.autreTournee.deltaCoutInsertionInter(this.positionJ, this.clientI);
    }

    @Override
    protected boolean doMouvement() {
        return this.tournee.doDeplacement(this);
    }

    @Override
    public String toString() {
        return "InterDeplacement{" +  "positionI=" + positionI +  ", positionJ=" + positionJ + ", coutDeplacementGlobal=" + deltaCout +","
                + "\n coutDeplacementTournee=" + deltaCoutTournee 
                + ",\n coutDeplacementAutre=" + deltaCoutAutreTournee +", clientI="+ clientI +", clientJ="+ clientJ+", Tournee="+tournee+", autreTournee="+autreTournee+ '}';
    }

    @Override
    public boolean isTabou(OperateurLocal operateur) {
        if(operateur == null) return false;
        if(!(operateur instanceof InterDeplacement)) return false;
        if(operateur.tournee == null || operateur.clientI == null) return false;
        
        if(this.clientI.equals(operateur.clientI))//suppression verif clientJ
            return true;
        
        return false;
    }

        
    
    
}
