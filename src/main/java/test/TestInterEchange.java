/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import operateur.InterEchange;
import operateur.OperateurInterTournees;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;
import solution.Tournee;

/**
 *
 * @author yanni
 */
public class TestInterEchange {
    public static void main(String[] args) {
        int id = 1;
        Depot d = new Depot(id++, 0, 0);
        Instance inst = new Instance("test", 100, d);
        Client c1 = new Client(10, id++, 10, 0);
        Client c2 = new Client(40, id++, 20, 0);
        Client c3 = new Client(10, id++, 30, 0);
        
        Client c4 = new Client(60, id++, -10, 0);
        Client c5 = new Client(10, id++, -20, 0);
        Client c6 = new Client(10, id++, -30, 0);
        inst.ajouterClient(c1);
        inst.ajouterClient(c2);
        inst.ajouterClient(c3);
        inst.ajouterClient(c4);
        inst.ajouterClient(c5);
        inst.ajouterClient(c6);
        
        Tournee t = new Tournee(inst);
        t.ajouterClient(c1);
        t.ajouterClient(c2);
        t.ajouterClient(c6);
        
        Tournee u = new Tournee(inst);
        u.ajouterClient(c4);
        u.ajouterClient(c5);
        u.ajouterClient(c3);
        
        System.out.println("DeltaCout -40: "+ t.deltaCoutRemplacementInter(2, c3));//-40
        System.out.println("DeltaCout -40: "+ u.deltaCoutRemplacementInter(2, c6));//-40
        
        System.out.println("DeltaCout Infinity: "+ t.deltaCoutRemplacementInter(3, c3));//Infinity
        System.out.println("DeltaCout Infinity: "+ u.deltaCoutRemplacementInter(3, c6));//Infinity
        
        System.out.println("DeltaCout -40: "+ t.deltaCoutRemplacementInter(2, c3)); //-40
        System.out.println("DeltaCout Infinity: "+ u.deltaCoutRemplacementInter(2, c2)); //Infinity: Dépassement capacité
        
        System.out.println("--------------------");
        
        
        InterEchange op = new InterEchange(t,u,2,2); //-40
        InterEchange op1 = new InterEchange(u,t,2,2); //-40
        OperateurLocal op2 = OperateurInterTournees.getOperateurInter(TypeOperateurLocal.INTER_ECHANGE, t, u, 2, 2);
        InterEchange op3 = new InterEchange(u,t,2,1); //Infinity: Dépassement capacité
        InterEchange op4 = new InterEchange(t,u,0,1);  //20
        InterEchange op5 = new InterEchange(t,u,0,2);  //-80

        
        System.out.println(op);
        System.out.println(op1);
        System.out.println(op2);
        System.out.println(op3);
        System.out.println(op4);
        System.out.println(op5);
        
        System.out.println(op.isMouvementRealisable()); //true
        System.out.println(op.isMouvementAmeliorant()); //true
        
        System.out.println(op2.isMouvementRealisable()); //true
        System.out.println(op2.isMouvementAmeliorant()); //true
        
        System.out.println(op3.isMouvementRealisable()); //false
        System.out.println(op3.isMouvementAmeliorant()); //false car deltaCout infini / Dépassement capacité;
        
        OperateurLocal bestOperateur = t.getMeilleurOperateurInter(u, TypeOperateurLocal.INTER_ECHANGE);
        System.out.println(bestOperateur);
        
        System.out.println(bestOperateur.isMouvementRealisable()); //true
        System.out.println(bestOperateur.isMouvementAmeliorant()); //true
        
        System.out.println("AVANT");
        System.out.println(t);
        System.out.println(u);
        System.out.println(bestOperateur.doMouvementIfRealisable());//true
        System.out.println("APRES");
        System.out.println(t);
        System.out.println(u);
        
        System.out.println(op3.doMouvementIfRealisable());//false
        
        System.out.println("check tabou");
        System.out.println(op.isTabou(op)); //true: même opérateur
        System.out.println(op4.isTabou(op4)); //true: même opérateur
        System.out.println(op2.isTabou(op2)); //true: même opérateur
        
        System.out.println(op4.isTabou(op5)); //true: un seul client en commun
        System.out.println(op.isTabou(op5)); //true: un seul client en commun
        System.out.println(op.isTabou(op1)); //false pas de clientI ou J en commun
       
    }
}
