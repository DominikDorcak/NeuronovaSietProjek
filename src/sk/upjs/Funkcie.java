
package sk.upjs;
// pomocná trieda obsahujúca používané funkcie

public class Funkcie {
    
    public static double fxy(double x, double y) {
        return ((x + (y * y)) / Math.sqrt(x + 1))/4;
    }
    
    public static double aktivacna(double x){
        return Math.tanh(x);
    }
    
    public static double aktivacnaDerivovana(double x){
        return 1-(Math.tanh(x)*Math.tanh(x));
    }
}
