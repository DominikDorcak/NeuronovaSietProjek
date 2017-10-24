package sk.upjs;

import java.util.*;

public class Backpropagation {

    private double vystup;
    private int[] popisSiete = {3, 7, 6, 5, 1};
    private List<Double> vystupy = new ArrayList<>();
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

    public void spracujVstup(double x, double y) {
        //TODO kod pre spracovanie jedneho vstupu z treningovej vzorky
        
    }

}
