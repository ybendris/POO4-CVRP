/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import operateur.ListeTabou;
import operateur.TypeOperateurLocal;
import solution.Solution;

/**
 *
 * @author yanni
 */
public class RechercheTabou implements Solveur{
    private InsertionSimple solveurInitial;

    public RechercheTabou(InsertionSimple solveurInitial) {
        this.solveurInitial = solveurInitial;
    }
    
    

    @Override
    public String getNom() {
        return "RechercheTabou";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution s = this.solveurInitial.solve(instance);
        Solution bestSolution = s;
        
        
        TypeOperateurLocal[] Operateur = TypeOperateurLocal.values();
        System.out.println("Operateur"+Operateur);
        
        
        return bestSolution;
    }
 
    public static void main(String[] args) {
        try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();
            
            
            
            RechercheTabou algo = new RechercheTabou(new InsertionSimple());
            
            Solution s = algo.solve(i);
            
            System.out.println(s.toString());
            System.out.println(s.check());
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
    }
}
