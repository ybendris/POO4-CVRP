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
public class IntraEchange extends OperateurIntraTournee {

    public IntraEchange() {
        super();
    }

    public IntraEchange(Tournee tournee, int positionI, int positionJ) {
        super(positionI, positionJ, tournee);
    }


    @Override
    public String toString() {
        return "IntraEchange{" +  "positionI=" + positionI +  ", positionJ=" + positionJ + ", coutDeplacement=" + deltaCout +", clientI="+ clientI +", clientJ="+ clientJ+",Tournee="+tournee+ '}';
    }
    
    
    
    @Override
    public boolean isTabou(OperateurLocal operateur) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected int evalDeltaCout() {
        if(tournee == null) return Integer.MAX_VALUE;
        return this.tournee.deltaCoutEchange(this.positionI, this.positionJ);
    }

    @Override
    protected boolean doMouvement() {
        return this.tournee.doEchange(this);
    }
    
}
