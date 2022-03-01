/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import operateur.IntraDeplacement;
import operateur.OperateurIntraTournee;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;
import solution.Tournee;

/**
 *
 * @author yanni
 */
public class TestIntraDeplacement {
    public static void main(String[] args){
        int id = 1;
        Depot d = new Depot(id++, 0, 0);
        Instance inst = new Instance("test", 100, d);
        Client c1 = new Client(10, id++, 5, 0);
        Client c2 = new Client(10, id++, 10, 0);
        Client c3 = new Client(10, id++, 40, 0);
        Client c4 = new Client(10, id++, 5, 0);
        Client c5 = new Client(10, id++, 20, 0);
        inst.ajouterClient(c1);
        inst.ajouterClient(c2);
        inst.ajouterClient(c3);
        inst.ajouterClient(c4);
        inst.ajouterClient(c5);
        Tournee t = new Tournee(inst);
        t.ajouterClient(c1);
        t.ajouterClient(c2);
        t.ajouterClient(c3);
        t.ajouterClient(c4);
        t.ajouterClient(c5);
        

        System.out.println(t.deltaCoutSuppression(1));//return 0
        System.out.println(t.deltaCoutSuppression(4));//return -30
        
        System.out.println(t.deltaCoutDeplacement(4, 2));//return -30
        System.out.println(t.deltaCoutDeplacement(1, 3));//return 0
        System.out.println(t.deltaCoutDeplacement(2, 4));//return -20

        IntraDeplacement op = new IntraDeplacement(t,2,4); //-20
        IntraDeplacement op1 = new IntraDeplacement(t,4,2); //-30
        System.out.println(op.toString());//2   4   et -20
        System.out.println(op1.toString());//4 2 et -30
        
        OperateurLocal op2 = OperateurIntraTournee.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT,t,2,4);
        System.out.println(op2.toString());
                
        System.out.println(op1.isMeilleur(op));//true 
        System.out.println(op.isMeilleur(op1));//false 
        
        System.out.println(op1.isMouvementRealisable());//true
        System.out.println(op1.isMouvementAmeliorant());//true
     
        
        System.out.println(t.getMeilleurOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT));
    }
}
