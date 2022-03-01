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
public class FusionTournees extends Operateur{
    private Tournee tourneeToFusion;
    
    public FusionTournees(){
        super();
    }
    
    /**
     * Met à jour correctement le cout de l'opérateur
     * @param tournee
     * @param clientToInsert
     * @param position 
     */
    public FusionTournees(Tournee tournee, Tournee tourneeToFusion) {
        super(tournee);
        this.tourneeToFusion = tourneeToFusion;
        this.deltaCout = this.evalDeltaCout();
    }
    

    @Override
    protected int evalDeltaCout() {
        if(tournee == null) return Integer.MAX_VALUE;
        if(tourneeToFusion == null) return Integer.MAX_VALUE;
        
        return this.tournee.deltaCoutFusion(this.tourneeToFusion);
    }

    @Override
    protected boolean doMouvement() {
        if(this.isMouvementAmeliorant())
            return this.tournee.doFusion(this);
        return false;
    }

    @Override
    public String toString() {
        return "FusionTournees{\n" + 
                "tournee=" + tournee +
                "\ntourneeToFusion=" + tourneeToFusion + 
                "\n, coutFusion=" + deltaCout+ '}';
    }

    public Tournee getTourneeToFusion() {
        return tourneeToFusion;
    }

    
}
