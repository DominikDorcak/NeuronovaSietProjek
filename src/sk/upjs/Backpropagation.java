package sk.upjs;

import java.util.*;

public class Backpropagation {

    private double vystup;
    private int[] popisSiete = {3, 7, 6, 5, 1};
    private List<double[][]> vystupy = new ArrayList<>();
    private List<double[][]> vahy = new ArrayList<>();

    public void inicializuj() {
        //inicializacia vah
        double[][] W;
        for (int i = 0; i < popisSiete.length - 2; i++) {
            W = new double[popisSiete[i]][popisSiete[i + 1] - 1];
            for (int j = 0; j < W.length; j++) {
                for (int k = 0; k < W[j].length; k++) {
                    W[j][k] = Math.random() * 0.5;
                }

            }
            vahy.add(Arrays.copyOf(W, W.length));

        }
        //prechod z poslednej vnutornej na vonkajsiu vrstvu
        W = new double[popisSiete[popisSiete.length - 2]][popisSiete[popisSiete.length - 1]];
        for (int j = 0; j < W.length; j++) {
            for (int k = 0; k < W[j].length; k++) {
                W[j][k] = Math.random() * 0.5;
            }
        }
        vahy.add(Arrays.copyOf(W, W.length));
    }

    public void trenujVstup(double x, double y) {
        //TODO kod pre spracovanie jedneho vstupu z treningovej vzorky
        /*for (int j = 0; j < popisSiete.length; j++) {
            double[][] pole = new double[1][popisSiete[j]-1];
            vystupy.add(Arrays.copyOf(pole, pole.length));
        }*/
       
        double[][] vstup = new double[1][popisSiete[0]];
        vstup[0][0] = x;
        vstup[0][1] = y;
        vstup[0][2] = -1;
        vystupy.add(vstup);
        
        System.out.println("vstup: " + Arrays.toString(vstup[0]));
        
        for (int i =0 ; i < popisSiete.length-2; i++) {
            double[][] pole = new double[1][popisSiete[i+1]-1];
            pole = Matica.multiply(vystupy.get(i), vahy.get(i));
            double[][] pole2 = new double[1][popisSiete[i+1]];
            double[] pole3 = Arrays.copyOf(pole[0], pole2[0].length);
            pole3[pole3.length-1] = -1;
            pole2[0] = pole3; 
            vystupy.add(Arrays.copyOf(pole2, pole2.length));
        }
        vystup = Matica.multiply(vystupy.get(popisSiete.length-2), vahy.get(popisSiete.length-2))[0][0];
        System.out.println("vystup na sieti: " + vystup);
       
    }
    
        
        
    
}
