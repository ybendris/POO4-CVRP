/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instance.reseau;



/**
 *
 * @author yanni
 */
public class Client extends Point{
    private int demande;

    public Client(int demande, int id, int abscisse, int ordonnee) {
        super(id, abscisse, ordonnee);
        this.demande = demande;
    }

    @Override
    public String toString() {
        return "Client{" + "demande=" + demande + ", " + super.toString() + "}";
    }

    public int getDemande() {
        return demande;
    }
    
    

    public static void main(String[] args) {
        Client c = new Client(200,1,10,20);
        System.out.println(c.toString());
    }
    
}
