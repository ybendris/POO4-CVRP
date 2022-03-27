/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import operateur.ListeTabou;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;
import solution.Solution;

/**
 *
 * @author yanni
 */
public class RechercheLocale implements Solveur{
    private Solveur solveurInitial;

    public RechercheLocale(Solveur solveurInitial) {
        this.solveurInitial = solveurInitial;
    }
    
    
    @Override
    public String getNom() {
        return "RechercheLocale("+this.solveurInitial.getNom()+')';
    }

    
    /*
    1: S = solution après application d’une méthode constructive
    2: improve = vrai
    3: while improve est vrai do
    4: improve = faux
    5: best = meilleur opérateur de déplacement intra-tournée dans S
    6: if le mouvement lié à best est améliorant then
    7: implémenter le mouvement lié à best sur la solution S
    8: improve = true
    9: end if
    10: end while
    11: return S
    */
    @Override
    public Solution solve(Instance instance) {
        ListeTabou liste = ListeTabou.getInstance();
        liste.vider();
           
        Solution s = this.solveurInitial.solve(instance);

        boolean improve = true;
        
        while(improve == true){
            improve = false;
            
            for(TypeOperateurLocal type :TypeOperateurLocal.values()){
                OperateurLocal bestOperateur = s.getMeilleurOperateurLocal(type);
                System.out.println(bestOperateur);
                if(bestOperateur.isMouvementAmeliorant()){
                    s.doMouvementRechercheLocale(bestOperateur);
                    improve = true;
                } 
            }
        }
        return s;
    }
    
    /**
     * Test sur la première instance
     * @param args 
     */
    public static void main(String[] args) {
        try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();
            
            Solveur solveurInitial = new InsertionSimple();
            
            RechercheLocale algo = new RechercheLocale(solveurInitial);
            
            Solution s = algo.solve(i);
            
            System.out.println(s.toString());
            System.out.println(s.check());
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
    }
    
}
