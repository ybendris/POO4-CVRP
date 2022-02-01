/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instance.reseau;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe abstraite représentant les coordonnée en 
 * @author yannid
 */
public abstract class Point {
    //Private pour le principe d'encapsulation
    //final comme on ne veut pas modifier 
    
    /**
     * On supposera que l'id est unique
     */
    private final int id; 
    private final int abscisse;
    private final int ordonnee;
    /**
     * La clé représente le point d'arrivé de la route
     */
    private Map<Point, Route> routes;

    
    /**
     * Constructeur par donnée, elles proviennent de fichiers d'instance
     * @param id
     * @param abscisse
     * @param ordonnee 
     */
    public Point(int id, int abscisse, int ordonnee) {
        this.id = id;
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.routes = new HashMap<>();
    }
    
    public int getId() {
        return id;
    }
    
    public int getAbscisse() {
        return abscisse;
    }

    public int getOrdonnee() {
        return ordonnee;
    }

    @Override
    public int hashCode() { //Objects.hash(id)
        /*int hash = 3;
        hash = 37 * hash + this.id;
        return hash;*/
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { //Même adresse mémoire
            return true;
        }
        if (obj == null) { //Null
            return false;
        }
        if (getClass() != obj.getClass()) {//Classe différent
            return false;
        }
        final Point other = (Point) obj;
        return this.id == other.id;
    }
    
    /**
     * Ajoutez une route dans l'ensemble des routes vers le point destination
     * @param destination 
     */
    public void ajouterRoute(Point destination){
        Route r = new Route(this,destination);
        this.routes.put(destination,r);
    }
    
    /**
     * 
     * @param destination
     * @return MAX_VALUES si pas de routes, 
     */
    public int getCoutVers(Point destination){
        Route r = this.routes.get(destination);
        if(r!=null){
            return r.getCout();
        }
        else{
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public String toString() {
        return "Point{" + "id=" + id + ", abscisse=" + abscisse + ", ordonnee=" + ordonnee + '}';
    }

    
    public static void main(String[] args) {
        Client c1 = new Client(40,1,5,6);
        Client c2 = new Client(50,2,50,6);
        Client c3 = new Client(60,3,55,6);
        
        c1.ajouterRoute(c2);
        c2.ajouterRoute(c3);
        
        System.out.println(c1.getCoutVers(c1)); //Infinity
        System.out.println(c1.getCoutVers(c2)); //45
        System.out.println(c2.getCoutVers(c3)); //5
    }
        
    
    
}
