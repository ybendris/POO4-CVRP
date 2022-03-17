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
public class IntraDeplacement extends OperateurIntraTournee {

    public IntraDeplacement() {
        super();
    }

    public IntraDeplacement(Tournee tournee, int positionI, int positionJ) {
        super(positionI, positionJ, tournee);
    }

    
    
    
    @Override
    protected int evalDeltaCout() {
        if(tournee == null) return Integer.MAX_VALUE;
        return this.tournee.deltaCoutDeplacement(this.positionI, this.positionJ);
    }

    @Override
    protected boolean doMouvement() {
        return this.tournee.doDeplacement(this);
    }

    @Override
    public String toString() {
        return "IntraDeplacement{" +  "positionI=" + positionI +  ", positionJ=" + positionJ + ", coutDeplacement=" + deltaCout +", clientI="+ clientI +", clientJ="+ clientJ+",Tournee="+tournee+ '}';
    }

    @Override
    public boolean isTabou(OperateurLocal operateur) {
        if(operateur == null) return false;
        if(!(operateur instanceof IntraDeplacement)) return false;
        if(operateur.tournee == null || operateur.clientI == null) return false;
        if(!this.tournee.equals(operateur.tournee)) return false;
        if(this.clientI.equals(operateur.clientI))//suppression verif clientJ
            return true;
        
        return false;
    }
    
    
    
}
