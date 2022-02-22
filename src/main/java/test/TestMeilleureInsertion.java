/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import instance.Instance;
import instance.reseau.Client;
import instance.reseau.Depot;
import operateur.InsertionClient;
import solution.Tournee;

/**
 *
 * @author yanni
 */
public class TestMeilleureInsertion {
    
   
    public static void main(String[] args){
        int id = 1;
        Depot d = new Depot(id++, 0, 0);
        Instance inst = new Instance("test", 100, d);
        Client c1 = new Client(10, id++, 5, 0);
        Client c2 = new Client(10, id++, 10, 0);
        Client c3 = new Client(10, id++, 20, 0);
        Client cIns = new Client(10, id++, 15, 0);
        inst.ajouterClient(c1);
        inst.ajouterClient(c2);
        inst.ajouterClient(c3);
        inst.ajouterClient(cIns);
        Tournee t = new Tournee(inst);

        t.ajouterClient(c1);
        t.ajouterClient(c2);
        t.ajouterClient(c3);
        
        System.out.println(t.check()); //doit return true
        
        System.out.println(t.deltaCoutInsertion(-1, cIns)); //Infini
        System.out.println(t.deltaCoutInsertion(0, cIns)); //20
        System.out.println(t.deltaCoutInsertion(1, cIns)); //10
        System.out.println(t.deltaCoutInsertion(2, cIns)); //0
        System.out.println(t.deltaCoutInsertion(3, cIns)); //0
        System.out.println(t.deltaCoutInsertion(4, cIns)); //Infini
               
        System.out.println("Tournee"+t.toString());
        
        InsertionClient op = new InsertionClient(t,cIns,-1); //Infini
        InsertionClient op0 = new InsertionClient(t,cIns,0); //20
        InsertionClient op1 = new InsertionClient(t,cIns,1); //10
        InsertionClient op2 = new InsertionClient(t,cIns,2); //0
        InsertionClient op3 = new InsertionClient(t,cIns,3); //0
        InsertionClient op4 = new InsertionClient(t,cIns,4); //Infini
        
        System.out.println(op.toString());
        System.out.println(op0.toString());
        System.out.println(op1.toString());
        System.out.println(op2.toString());
        System.out.println(op3.toString());
        System.out.println(op4.toString());
        
        System.out.println("Pas réalisable (false) "+op.isMouvementRealisable()); //Renvoie false
        System.out.println("Réalisable (true) "+op2.isMouvementRealisable()); //Renvoie true
        
        System.out.println(t.getMeilleureInsertion(cIns)); //Renvoie position 2 car deltaCout = 0 (meilleur)

        
        t.doInsertion(op);
        
        System.out.println("Insertion op (false)" + t.doInsertion(op));//false
        System.out.println(t);
        System.out.println("Insertion op1 (true)" + t.doInsertion(op1));//true
        System.out.println(t);

    }
}
