/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instance.reseau;


/**
 *
 * @author yanni
 */
public class Depot extends Point {

    public Depot(int id, int abscisse, int ordonnee) {
        super(id, abscisse, ordonnee);
    }
    
    @Override
    public String toString() {
        return "Depot{" + super.toString() + '}';
    }
    
    public static void main(String[] args) {
        Depot D = new Depot(1,15,25);
        
    }
    

    
}
