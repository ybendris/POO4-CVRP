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
public abstract class OperateurInterTournees extends OperateurLocal {
    protected Tournee autreTournee;
    protected int deltaCoutTournee;
    protected int deltaCoutAutreTournee;

    /**
     * Je redéfinie les deltaCout a voir
     */
    public OperateurInterTournees() {
        super();
        this.deltaCoutTournee = Integer.MAX_VALUE;
        this.deltaCoutAutreTournee = Integer.MAX_VALUE;
    }

       
    public OperateurInterTournees(Tournee tournee, Tournee autreTournee, int positionI, int positionJ) {
        super(positionI, positionJ, tournee);
        this.autreTournee = autreTournee;
        this.clientJ = autreTournee.getClientByPosition(positionJ);
        this.deltaCout = this.evalDeltaCout();
    }
    
    protected abstract int evalDeltaCoutTournee();
    protected abstract int evalDeltaCoutAutreTournee();

    public Tournee getAutreTournee() {
        return autreTournee;
    }

    public Tournee getTournee() {
        return tournee;
    }
    
    @Override
    protected int evalDeltaCout() {
        if(this.evalDeltaCoutTournee() == Integer.MAX_VALUE || this.evalDeltaCoutAutreTournee() == Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return this.evalDeltaCoutTournee()+ this.evalDeltaCoutAutreTournee();
    }
    
}
