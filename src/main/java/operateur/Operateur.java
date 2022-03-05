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
public abstract class Operateur {
    protected Tournee tournee;
    protected int deltaCout;

    public Operateur(){
        this.deltaCout = Integer.MAX_VALUE;
    }
    
    public Operateur(Tournee tournee) {
        this();
        this.tournee = tournee;
    }

    public int getDeltaCout() {
        return deltaCout;
    }
    
    /**
     * N'engendre pas un cout infini, donc on vérifie le cout
     * @return 
     */
    public boolean isMouvementRealisable(){
        if(this.deltaCout >= Integer.MAX_VALUE){
            return false;
        }
        return true;
    }
    
    public boolean isMouvementAmeliorant(){
        if(deltaCout < 0)
            return true;
        return false;
    }
    
    /**
     * Renvoie true si l'Opérateur courant est strictement meilleur que l'opérateur passé en param
     * @param op
     * @return 
     */
    public boolean isMeilleur(Operateur op){
        if(op == null) return false;
        return this.getDeltaCout() < op.getDeltaCout();
    }

    protected abstract int evalDeltaCout();
    protected abstract boolean doMouvement();
    
    public boolean doMouvementIfRealisable(){
        if(!this.isMouvementRealisable()){
            return false;
        }
        this.doMouvement();
        return true;
    }

    public Tournee getTournee() {
        return tournee;
    }
    
    

    @Override
    public String toString() {
        return "Operateur{" + "tournee=" + tournee + ", \ndeltaCout=" + deltaCout + '}';
    }
    
    
}
