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
import operateur.InsertionClient;
import solution.Solution;

/**
 *
 * @author yanni
 */
public class MeilleureInsertion implements Solveur{

    @Override
    public String getNom() {
        return "Meilleure Insertion";
    }

    @Override
    public Solution solve(Instance instance) {
        LinkedList<Client> clients = instance.getClients();
        Solution s = new Solution(instance);
        InsertionClient best = new InsertionClient();
        
        
        while(!clients.isEmpty()){
            best = getMeilleurInsertionClientSolution(s, clients);
            if(s.doInsertion(best)){
                clients.remove(best.getClientToInsert());
            }
            else{
                s.ajouterClientNouvelleTournee(clients.getFirst());
                clients.remove(clients.getFirst());
            }
        }
                
        return s;     
    }
    
    private InsertionClient getMeilleurInsertionClientSolution(Solution s, LinkedList<Client> clients){
        InsertionClient meilleur = new InsertionClient();
        InsertionClient courrant = new InsertionClient();
        for(Client c: clients){
            courrant = s.getMeilleureInsertion(c);
            if(courrant.isMeilleur(meilleur)) meilleur = courrant;
        }
        return meilleur;
    }
    
    public static void main(String[] args) {
         try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();
            
            MeilleureInsertion algo = new MeilleureInsertion();
            
            Solution s = algo.solve(i);
            
            System.out.println(s.toString());
            System.out.println(s.check());
            
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
    }
    
}
