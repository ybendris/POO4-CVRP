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
public class InsertionPlusProcheVoisin implements Solveur {

    @Override
    public String getNom() {
        return "InsertionPlusProcheVoisin";
    }

    @Override
    public Solution solve(Instance inst) {
        LinkedList<Client> clients = inst.getClients();
        Solution s = new Solution(inst);
        Client i = clients.getFirst();
        Client j;
        boolean affecte;
        
        while(!clients.isEmpty()){
            affecte = false;
            if(s.ajouterClientDerniereTournee(i)){
                affecte = true;
            }
            if(affecte == false){
                s.ajouterClientNouvelleTournee(i);
            }
            clients.remove(i);
            j = getClientLePlusProche(i,clients);
            
            i = j;
        }
        
        return s;
    }
    
    private Client getClientLePlusProche(Client courant,LinkedList<Client> ensemble){
        Client proche=null;
        int distance;
        int distanceMin = Integer.MAX_VALUE;
        
        for(Client c :ensemble){
            courant.ajouterRoute(c);
            distance = courant.getCoutVers(c);
            if(distance < distanceMin){
                distanceMin = distance;
                proche = c;
            }
        }
        System.out.println("->"+proche);
        return proche;
    }
    
    
    public static void main(String[] args) {
        try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();
            
            InsertionPlusProcheVoisin algoProche = new InsertionPlusProcheVoisin();
            
            Solution procheVoisin = algoProche.solve(i);
            
            System.out.println(procheVoisin.toString());
            System.out.println(procheVoisin.check());
            
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
    }
            
}
