/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import operateur.InterDeplacement;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;
import solution.Tournee;

/**
 *
 * @author lucas
 */
public class TestInterDeplacement {
    public static void main(String[] args) {
        int id = 1;
        Depot d = new Depot(id++, 0, 0);
        Instance inst = new Instance("test", 100, d);
        Client c1 = new Client(10, id++, 5, 0);
        Client c2 = new Client(10, id++, 10, 0);
        Client c3 = new Client(10, id++, 10, 0);
        Client c4 = new Client(10, id++, 15, 0);
        Client c5 = new Client(60, id++, 0, 10);
        Client c6 = new Client(10, id++, 10, 10);
        Client c7 = new Client(10, id++, 15, 10);
        inst.ajouterClient(c1);
        inst.ajouterClient(c2);
        inst.ajouterClient(c3);
        inst.ajouterClient(c4);
        inst.ajouterClient(c5);
        inst.ajouterClient(c6);
        inst.ajouterClient(c7);
        Tournee t = new Tournee(inst);
        t.ajouterClient(c1);
        t.ajouterClient(c2);
        t.ajouterClient(c6);
        t.ajouterClient(c3);
        t.ajouterClient(c4);
        Tournee u = new Tournee(inst);
        Tournee t2 = new Tournee(inst);
        u.ajouterClient(c5);
        u.ajouterClient(c7);
        InterDeplacement intDep1 = new InterDeplacement(t, u, 0, 1);
        InterDeplacement intDep2 = new InterDeplacement(t, u, 5, 0);
        InterDeplacement intDep3 = new InterDeplacement(u,t, 0, 1);
        InterDeplacement intDep4 = new InterDeplacement(t,t2, 0, 1);
        System.out.println(intDep1);
        System.out.println(intDep2);
        System.out.println(intDep1.isMeilleur(intDep2));
        System.out.println(intDep1.isMouvementAmeliorant());//false
        System.out.println(intDep2.isMouvementAmeliorant());//false
        System.out.println(intDep1.isMouvementRealisable());//true
        System.out.println(intDep2.isMouvementRealisable());//false
        
        System.out.println("t: " + t);
        System.out.println("u: " + u);
        OperateurLocal bestOperateur = t.getMeilleurOperateurInter(u, TypeOperateurLocal.INTER_DEPLACEMENT);
        System.out.println(bestOperateur);
        System.out.println(bestOperateur.doMouvementIfRealisable());
        System.out.println("t: " + t);
        System.out.println("u: " + u);
        
        System.out.println("check tabou");
        System.out.println("Tabou ? " + intDep1.isTabou(intDep2) );//False
        System.out.println("Tabou ? " + intDep1.isTabou(intDep1) );//True
        System.out.println(intDep1.isTabou(intDep2)); //false: pas le même client déplacé
        System.out.println(intDep1.isTabou(intDep3)); //false: pas le même client déplacé
        System.out.println(intDep1.isTabou(intDep4)); //true: même client déplacé, pas dans la même tournéee
        
        
    }
}
