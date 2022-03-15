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
public abstract class OperateurIntraTournee extends OperateurLocal{

    public OperateurIntraTournee() {
        super();
    }

    public OperateurIntraTournee(int positionI, int positionJ, Tournee tournee) {
        super(tournee,positionI, positionJ);
        this.deltaCout = this.evalDeltaCout();
    }

    
    
}
