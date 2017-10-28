package sk.upjs;

import java.util.*;

public class Backpropagation {
    
    private int[] popisSiete = {3, 7, 6, 5, 1};
    private double himVystup;
    private List<double[][]> himVystupy = new ArrayList<>();
    private double vystup;
    private List<double[][]> vystupy = new ArrayList<>();
    private List<double[][]> vahy = new ArrayList<>();
    private double delta;
    private List<double[][]> delty = new ArrayList<>();
    
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
        // kod pre spracovanie jedneho vstupu z treningovej vzorky
        
        double[][] vstup = new double[1][popisSiete[0]];
        vstup[0][0] = x;
        vstup[0][1] = y;
        vstup[0][2] = -1;
        himVystupy.add(vstup);
        
        System.out.println("vstup: " + Arrays.toString(vstup[0]));
        
        for (int i = 0; i < popisSiete.length - 2; i++) {
            double[][] pole = new double[1][popisSiete[i + 1] - 1];
            pole = Matica.multiply(himVystupy.get(i), vahy.get(i));
            double[][] pole2 = new double[1][popisSiete[i + 1]];
            double[] pole3 = Arrays.copyOf(pole[0], pole2[0].length);
            pole3[pole3.length - 1] = -1;
            pole2[0] = pole3;            
            himVystupy.add(Arrays.copyOf(pole2, pole2.length));
        }
        himVystup = Matica.multiply(himVystupy.get(popisSiete.length - 2), vahy.get(popisSiete.length - 2))[0][0];
        
        vystupy = himVystupy.subList(0, himVystupy.size());
        for (int j = 0; j < vystupy.size(); j++) {
            double[][] d = vystupy.get(j);
            for (int i = 0; i < d[0].length - 1; i++) {
                d[0][i] = Funkcie.aktivacna(himVystupy.get(j)[0][i]);
                
            }
        }
        vystup = Funkcie.aktivacna(himVystup);
        
        System.out.println("vystup na sieti: " + himVystup);
        
    }
    
    public void vypocitajDelty(double ocakavanyVysledok) {
        
        delta = Funkcie.aktivacnaDerivovana(himVystup) * (ocakavanyVysledok - vystup);
        
        double[][] aktualne = new double[1][popisSiete[popisSiete.length - 2]];
        double[] him = himVystupy.get(himVystupy.size() - 1)[0];
        double sumar = 0;
        double[][] wm = vahy.get(vahy.size() - 1);
        double[] deltaMPlusJeden;
        
        for (int i = 0; i < aktualne.length; i++) {
            sumar = wm[i][0] * delta;
            aktualne[0][i] = Funkcie.aktivacnaDerivovana(him[i]) * (sumar);
        }
        delty.add(aktualne);
        for (int k = popisSiete.length - 3; k >= 0; k++) {
            aktualne = new double[1][popisSiete[k]];
            him = himVystupy.get(k)[0];
            
            wm = vahy.get(k);
            deltaMPlusJeden = delty.get(delty.size() - 1)[0];
            
            for (int i = 0; i < aktualne.length; i++) {
                sumar = 0;                
                
                for (int j = 0; j < wm.length; j++) {
                    sumar = sumar + deltaMPlusJeden[j] * wm[j][i];
                }
                aktualne[0][i] = Funkcie.aktivacnaDerivovana(him[i]) * sumar;
            }
            delty.add(aktualne);
        }
        
        Collections.reverse(delty);
    }
    
}
