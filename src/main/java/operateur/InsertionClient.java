/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import instance.reseau.Client;
import solution.Tournee;

/**
 *
 * @author yanni
 */
public class InsertionClient extends Operateur {
    private Client clientToInsert;
    private int position;

    public InsertionClient() {
        super();
    }

    /**
     * Met à jour correctement le cout de l'opérateur
     * @param tournee
     * @param clientToInsert
     * @param position 
     */
    public InsertionClient(Tournee tournee, Client clientToInsert, int position) {
        super(tournee);
        this.clientToInsert = clientToInsert;
        this.position = position;
        this.deltaCout = this.evalDeltaCout();
    }
    
    public Client getClientToInsert() {
        return clientToInsert;
    }

    public int getPosition() {
        return position;
    }

    @Override
    protected boolean doMouvement() {
        return this.tournee.doInsertion(this);
    }
    
    @Override
    public String toString() {
        return "InsertionClient{" + "clientToInsert=" + clientToInsert + ", position=" + position + '}';
    }
    
    @Override
    protected int evalDeltaCout() {
        if(tournee == null) return Integer.MAX_VALUE;
        return this.tournee.deltaCoutInsertion(this.position, this.clientToInsert);
    }

    public Tournee getTournee() {
        return tournee;
    }

    
}
