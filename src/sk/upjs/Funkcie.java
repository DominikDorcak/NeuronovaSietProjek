/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.upjs;

/**
 *
 * @author domin
 */
public class Funkcie {
    
    public static double fxy(double x, double y) {
        return (x + (y * y)) / Math.sqrt(x + 1);
    }
    
    public static double aktivacna(double x){
        return Math.tanh(x);
    }
    
    public static double aktivacnaDerivovana(double x){
        return 1-(Math.tanh(x)*Math.tanh(x));
    }
}
