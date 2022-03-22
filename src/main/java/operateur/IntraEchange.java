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
    
    
    
    public boolean isTabou(OperateurLocal operateur) {
        if(operateur == null) return false;
        if(!(operateur instanceof IntraEchange)) return false;
        if(operateur.tournee == null || operateur.clientI == null || operateur.clientJ == null) return false;
        if(!this.tournee.equals(operateur.tournee)) return false;
        if(this.clientI.equals(operateur.clientI) && this.clientJ.equals(operateur.clientJ))
            return true;
        if(this.clientI.equals(operateur.clientJ) && this.clientJ.equals(operateur.clientI))
            return true;
        
        return false;
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
