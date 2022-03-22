/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author yanni
 */
public class ListeTabou {
    private static ListeTabou instance;
    private LinkedBlockingQueue<OperateurLocal> listeTabou;
    private int deltaAspiration;
    
    private ListeTabou(){
        this.listeTabou = new LinkedBlockingQueue<OperateurLocal>(80);
        this.deltaAspiration = 0; //initialiser à 0?
    }
  
    /**
     * Point d'accès pour l'instance unique du singleton, d'ou le private du constructeur
     */
    public static ListeTabou getInstance(){           
        if (instance == null){   
            instance = new ListeTabou(); 
        }
        return instance;
    }

    public LinkedBlockingQueue<OperateurLocal> getListeTabou() {
        return listeTabou;
    }
    
    public void add(OperateurLocal operateur){
        if(this.listeTabou.size() >= 80){
            this.listeTabou.poll();
        }
        this.listeTabou.offer(operateur);
    }
    
    public boolean isTabou(OperateurLocal operateur){
        if(operateur.getDeltaCout() < this.deltaAspiration)
            return false;
        for(OperateurLocal op : this.listeTabou){
            if(op.isTabou(operateur)){
                return true;
            }
        }
        return false;
    }
    
    
    public void vider(){
        this.listeTabou.clear();
    }

    public void setDeltaAspiration(int deltaAspiration) {
        this.deltaAspiration = deltaAspiration;
    }
    
    
    
}
