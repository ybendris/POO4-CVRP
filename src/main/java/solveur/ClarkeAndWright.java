/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import instance.Instance;
import instance.reseau.Client;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.LinkedList;
import operateur.FusionTournees;
import operateur.InsertionClient;
import solution.Solution;

/**
 *
 * @author yanni
 */
public class ClarkeAndWright implements Solveur {

    @Override
    public String getNom() {
        return "ClarkeAndWright";
    }

    /*
    1: N = liste de clients
    2: S = solution vide (sans tournées)
    3: for all client k ∈ N do
    4: ajouter k dans une nouvelle tournée de S
    5: end for
    6: fusion = vrai
    7: while fusion est vrai do
    8: best = meilleur opérateur de fusion de deux tournées de S
    9: fusion = le mouvement lié à best a pu être implémenté dans S
    10: end while
    11: return S
    */
    
    
    @Override
    public Solution solve(Instance instance) {
        LinkedList<Client> clients = instance.getClients();
        Solution s = new Solution(instance);
        
        
        for(Client c: clients){
            s.ajouterClientNouvelleTournee(c);
        }
        boolean fusion = true;
        
        while(fusion){
            FusionTournees best = s.getMeilleurFusion();
            
            fusion = s.doFusion(best);
        }
         
     
        
        
        return s;     
    }
    
    public static void main(String[] args) {
        try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();
          
            ClarkeAndWright algo = new ClarkeAndWright();
            
            Solution s = algo.solve(i);
            
            System.out.println(s.toString());
            
            
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
    }
            
    
}
