/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import instance.Instance;
import instance.reseau.Point;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.logging.Level;
import java.util.logging.Logger;
import solution.Solution;

/**
 *
 * @author yanni
 */
public class SolveurPLNE implements Solveur{
    private IloCplex cplex;
    private IloIntVar[][] x;
    private IloNumVar[] u;
    
    private Instance instance;
    private int nbPoints;
    
    
    
    private void buildModel() throws IloException{
        this.cplex = new IloCplex();
        defineDecisionVarX();
        defineDecisionVarU();
        objective();
        defineContrainte1();
        defineContrainte2();
        defineContrainte3();
        defineContrainte4();
    }
    
    @Override
    public String getNom() {
        return "SolveurPLNE";
    }

    @Override
    public Solution solve(Instance instance) {
        this.instance = instance;
        this.nbPoints = instance.getNbClients()+1;
        try {
            this.buildModel();
            cplex.exportModel("model_"+instance.getNom()+".lp");
            cplex.setParam(IloCplex.DoubleParam.TimeLimit, 60);
            if(cplex.solve()){
                System.out.println("TROUVE");
            }
        } 
        catch (IloException ex) {
            Logger.getLogger(SolveurPLNE.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        return "SolveurPLNE{" + "cplex=" + cplex + '}';
    }
    
    public static void main(String[] args) {
        try{
            InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();

            SolveurPLNE solv = new SolveurPLNE();
            
            solv.buildModel();
            
        }
        catch(ReaderException ex){
            System.out.println(ex.getMessage());
        } catch (IloException ex) {
            Logger.getLogger(SolveurPLNE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void defineDecisionVarX() throws IloException {
        /**
         * 0 = dépot
         * 1 = 1er client ici
         */
        x = new IloIntVar[nbPoints][nbPoints];
        for(int i=0;i<nbPoints; ++i){
            for(int j=0;j<nbPoints; ++j){
                if(i != j){
                    x[i][j] = this.cplex.intVar(0, 1, "x"+i+"_"+j );
                }
            }
        }
    }

    private void defineDecisionVarU() throws IloException {
        u = new IloNumVar[nbPoints];
        
        for(int i=1; i<nbPoints; ++i){
            int demande = instance.getClients().get(i).getDemande();
            int capacite = instance.getCapacite();
            u[i] = this.cplex.numVar(demande,capacite,"u_"+i);
        }
        
    }

    private void objective() throws IloException {
        IloLinearNumExpr expr = cplex.linearNumExpr();

        for(int i=0; i<nbPoints; ++i){
            for(int j=0; j<nbPoints; ++j){
                if(i != j) {
                    Point pi;
                    Point pj;
                    
                    if(i == 0){
                        pi = instance.getDepot();
                    }
                    else{
                        pi = instance.getClients().get(i-1);
                    }
                    if(j == 0){
                        pj = instance.getDepot();
                    }
                    else{
                        pj = instance.getClients().get(j-1);
                    }

                    int cout = pi.getCoutVers(pj);

                    expr.addTerm(x[i][j],cout);
                }
            }
        }
        this.cplex.addMinimize(expr);
    }
    
    
    private void defineContrainte1() throws IloException{
        for(int i=1 ; i< nbPoints;i++){
            IloLinearNumExpr expr = cplex.linearNumExpr();
            for(int j=0; j< nbPoints;j++){
                if(i !=j){
                    expr.addTerm(x[i][j], 1);
                }
            }
            cplex.addEq(expr,1);   
        }
    }
    
    private void defineContrainte2() throws IloException{
        for(int i=1 ; i< nbPoints;i++){
            IloLinearNumExpr expr = cplex.linearNumExpr();
            for(int j=0; j< nbPoints;j++){
                if(i !=j){
                    expr.addTerm(x[j][i], 1);
                }
            }
            cplex.addEq(expr,1);   
        }
    }
    
    private void defineContrainte3() throws IloException{
        IloLinearNumExpr expr = cplex.linearNumExpr();
        for(int i=1 ; i< nbPoints;i++){
            expr.addTerm(x[0][i], 1);
        }
        cplex.addLe(expr,nbPoints-1);   
    }
    
    private void defineContrainte4() throws IloException{
        int capaciteVehicule = instance.getCapacite();
        
        for(int i=1 ; i< nbPoints;i++){
            for(int j=1; j< nbPoints;j++){
                if(i !=j){
                    int demandeClient = instance.getClients().get(j-1).getDemande();
                    IloLinearNumExpr expr = cplex.linearNumExpr();
                    
                    expr.addTerm(u[i], 1);
                    expr.addTerm(u[j], -1);
                    expr.addTerm(x[i][j], capaciteVehicule);
                    cplex.addLe(expr,capaciteVehicule-demandeClient);

                }
            }
        }
    }
}
