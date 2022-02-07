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
import solution.Solution;

/**
 *
 * @author yanni
 */
public class InsertionSimple implements Solveur {

    
    
    
    @Override
    public String getNom() {
        return "Intertion Simple";
    }

    @Override
    public Solution solve(Instance i) {
        LinkedList<Client> clients = i.getClients();
        Solution s = new Solution(i);
        boolean affecte;
        
        for(Client cli : clients){
            affecte = false;
            if(s.ajouterClientTourneeExistante(cli)){
                affecte = true;
            }
            if(affecte == false){
                s.ajouterClientNouvelleTournee(cli);
            }
        }
        return s;
    }
    
    
    
    public static void main(String[] args) {
        try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();
          
            InsertionSimple algoSimple = new InsertionSimple();
            
            Solution simple = algoSimple.solve(i);
            
            System.out.println(simple.toString());
            
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
    }
   
         

    
}
