/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import operateur.FusionTournees;
import solution.Solution;
import solution.Tournee;

/**
 *
 * @author yanni
 */
public class TestFusionTournees {
    public static void main(String[] args) {
        int id = 1;
        Depot d = new Depot(id++, 0, 0);
        Instance inst = new Instance("test", 100, d);
        Client c1 = new Client(10, id++, 10, 0);
        Client c2 = new Client(10, id++, 20, 0);
        Client c3 = new Client(10, id++, 25, 0);
        
        inst.ajouterClient(c1);
        inst.ajouterClient(c2);
        inst.ajouterClient(c3);
        
        Tournee t1 = new Tournee(inst);
        Tournee t2 = new Tournee(inst);
        Tournee t3 = new Tournee(inst);

        t1.ajouterClient(c1);
        t2.ajouterClient(c2);
        t3.ajouterClient(c3);
        
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
        
        System.out.println(t1.check()); //doit return true
        System.out.println(t2.check()); //doit return true
        System.out.println(t3.check()); //doit return true
        
        System.out.println(t1.deltaCoutFusion(t2)); // doit faire -20
        System.out.println(t2.deltaCoutFusion(t3)); // doit faire -40
        
        
        
        FusionTournees opFusion = new FusionTournees(t1,t2);
        FusionTournees opFusion2 = new FusionTournees(t2,t3);
        
        System.out.println(opFusion.toString());
        System.out.println(opFusion2.toString());
        
        System.out.println("Bbbbb");
        System.out.println(opFusion2.isMeilleur(opFusion)); // true
        
        
        
        
        Solution s = new Solution(inst);
        System.out.println(s.toString());
        
        s.ajouterClientNouvelleTournee(c1);
        s.ajouterClientNouvelleTournee(c2);
        s.ajouterClientNouvelleTournee(c3);
        
        System.out.println(s.toString());

        FusionTournees best = s.getMeilleurFusion();
        System.out.println(best.toString());
        
        
        System.out.println("Meilleur:"+best.isMeilleur(best));
        
        System.out.println("Avant:"+s.check()+"--"+s.toString());
        
        s.doFusion(best);
        FusionTournees best2 = s.getMeilleurFusion();
        s.doFusion(best2);

        System.out.println("Après:"+s.toString());

    }
}
