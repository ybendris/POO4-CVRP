/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instance.reseau;

import java.util.Objects;


/**
 *
 * @author yanni
 */
public class Route {
    private int cout;
    private Point depart;
    private Point destination;

    public Route(Point depart, Point destination) {
        this.depart = depart;
        this.destination = destination;
        this.cout = calculCout(depart,destination);
    }
    
               
    private int calculCout(Point depart, Point destination){       
        int dx = depart.getAbscisse() - destination.getAbscisse();
        int dy = depart.getOrdonnee() - destination.getOrdonnee();
        double distance = Math.sqrt(dx*dx + dy*dy);
        
        return (int)Math.round(distance);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.depart);
        hash = 29 * hash + Objects.hashCode(this.destination);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Route other = (Route) obj;
        if (!Objects.equals(this.depart, other.depart)) {
            return false;
        }
        return Objects.equals(this.destination, other.destination);
    }

    @Override
    public String toString() {
        return "Route{" + "cout=" + cout + ", depart=" + depart + ", destination=" + destination + '}';
    }
    
    
    
    public static void main(String[] args) {
        
        Client c1 = new Client(200,1,1,3);
        Client c2 = new Client(300,2,9,15);
        
        Depot d1 = new Depot(4,80,6);
        
        Route r1 = new Route(d1,c2);
        Route r2 = new Route(c2,d1);
        Route r3 = new Route(c1,c2);
        
        System.out.println(r1.toString());
        System.out.println(r2.toString());
        System.out.println(r3.toString());
        
   
    }

    int getCout() {
       return this.cout;
    }
    
    
}
