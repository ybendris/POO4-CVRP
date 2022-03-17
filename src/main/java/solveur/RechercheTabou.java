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
public class RechercheTabou implements Solveur{
    private Solveur solveurInitial;

    public RechercheTabou(Solveur solveurInitial) {
        this.solveurInitial = solveurInitial;
    }
    
    

    @Override
    public String getNom() {
        return "RechercheTabou";
    }

    /*
    1: S = solution initiale (e.g. après application d’une méthode constructive et d’une recherche locale)
    2: Sbest = S
    3: OPER = {Ointra−dep, Ointra−ech, Ointer−dep, Ointer−ech}
    4: nbIterMax = 10000
    5: nbIterSansAmelioration = 0
    6: vider la liste tabou
    7: while nbIterSansAmelioration < nbIterMax do
    8: best = opérateur local par défaut
    9: for all O ∈ OPER do
    10: op = meilleur opérateur non tabou de type O dans S
    11: if op est meilleur que best then
    12: best = op
    13: end if
    14: end for
    15: if le mouvement lié à best est implémenté dans S then
    16: ajouter best à la liste tabou
    17: end if
    18: if S a un coût inférieur à Sbest then
    19: Sbest = S
    20: nbIterSansAmelioration = 0
    21: else
    22: nbIterSansAmelioration = nbIterSansAmelioration + 1
    23: end if
    24: end while
    25: return Sbest
    */
    
    @Override
    public Solution solve(Instance instance) {
        Solution s = this.solveurInitial.solve(instance);
        
        
        Solution bestSolution = s;
        int nbIterMax = 10000;
        int nbIterSansAmelioration = 0;
        
        ListeTabou liste = ListeTabou.getInstance();
        liste.vider();
        
        while(nbIterSansAmelioration < nbIterMax){ //critère d’arrêt
            OperateurLocal best = s.getMeilleurOperateurLocal(TypeOperateurLocal.INTER_DEPLACEMENT); // par défaut 
            for(TypeOperateurLocal type :TypeOperateurLocal.values()){
                OperateurLocal op = s.getMeilleurOperateurLocal(type); //cherche le meilleur opérateur non tabou dans la solution courante S
                if(op.isMeilleur(best)){
                    best = op;
                }
            }
            if(s.doMouvementRechercheLocale(best)){
                liste.add(best);
            }
            if(s.getCoutTotal() < bestSolution.getCoutTotal()){
                bestSolution = new Solution(s);
                nbIterSansAmelioration = 0;
            }
            else{
                nbIterSansAmelioration++;
            }
        }
        
        TypeOperateurLocal[] Operateur = TypeOperateurLocal.values();
        System.out.println("Operateur"+Operateur);
        
        
        return bestSolution;
    }
 
    public static void main(String[] args) {
        try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();
            
            
            
            RechercheTabou algo = new RechercheTabou(new RechercheLocale());
            
            Solution s = algo.solve(i);
            
            System.out.println(s.toString());
            System.out.println(s.check());
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        }
    }
}
