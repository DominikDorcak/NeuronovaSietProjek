package sk.upjs;

import java.util.*;

public class Backpropagation {

    private List<Double> chyba = new ArrayList<>();


    public List<Double> getChyba() {
        return chyba;
    }

    public void setChyba(List<Double> chyba) {
        this.chyba = chyba;
    }
    private int[] popisSiete = {3, 7, 6, 5, 1};
    private double uciaciPomer = 0.1;

    private double himVystup;
    private List<double[][]> himVystupy;
    private double vystup;
    private List<double[][]> vystupy ;
    private List<double[][]> vahy = new ArrayList<>();
    private double delta;
    private List<double[][]> delty ;

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

    public void trenujVstup(double x, double y, double ocakavanyVysledok) {
        VypocitajVystup(x, y);
        chyba.add(vystup-ocakavanyVysledok);

        //System.out.println("vystup na sieti: " + vystup);
        vypocitajDelty(ocakavanyVysledok);
        nastavVahyCezMatice();

    }

    public void VypocitajVystup(double x, double y) {

        // kod pre spracovanie jedneho vstupu z treningovej vzorky
        himVystupy = new ArrayList<>();
        vystupy = new ArrayList<>();

        double[][] vstup = new double[1][popisSiete[0]];
        vstup[0][0] = x;
        vstup[0][1] = y;
        vstup[0][2] = -1;
        himVystupy.add(vstup);

        //System.out.println("vstup: " + Arrays.toString(vstup[0]));

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
            vystupy.set(j, d);
        }
        vystup = Funkcie.aktivacna(himVystup);
    }

    public void vypocitajDelty(double ocakavanyVysledok) {
        delty = new ArrayList<>();
        delta = Funkcie.aktivacnaDerivovana(himVystup) * (ocakavanyVysledok - vystup);

        double[][] aktualne = new double[1][popisSiete[popisSiete.length - 2]-1];
        double[] him = himVystupy.get(himVystupy.size() - 1)[0];
        double sumar = 0;
        double[][] wm = vahy.get(vahy.size() - 1);
        double[] deltaMPlusJeden;
    // z vystupnej na poslednu skrytu vrstvu
        for (int j=0 ; j< aktualne.length; j++) {
        for (int i = 0; i < wm.length; i++) {
            sumar = wm[i][0] * delta;
        }
            aktualne[0][j] = Funkcie.aktivacnaDerivovana(him[j]) * (sumar);
        }
        delty.add(aktualne);

       //ostatne vrstvy
        for (int k = popisSiete.length - 3; k >= 0; k--) {
            aktualne = new double[1][popisSiete[k]-1];
            him = himVystupy.get(k)[0];
            wm = vahy.get(k);
            deltaMPlusJeden = delty.get(delty.size() - 1)[0];

            for (int i = 0; i < aktualne[0].length; i++) {
                sumar = 0;

                for (int j = 0; j < wm[0].length; j++) {
                    sumar = sumar + (deltaMPlusJeden[j] * wm[i][j]);
                }
                aktualne[0][i] = Funkcie.aktivacnaDerivovana(him[i]) * sumar;
            }
            delty.add(aktualne);
        }

        Collections.reverse(delty);
    }

    

    public void nastavVahyCezMatice() {
        double[][] aktualne = vahy.get(vahy.size() - 1);
        double[][] vystupy = this.vystupy.get(this.vystupy.size() - 1);
        double[][] delty;
        double[][] noveVahy;

        for (int i = 0; i < aktualne.length; i++) {
            aktualne[i][0] = aktualne[i][0] + (uciaciPomer * delta * vystupy[0][i]);
        }
        vahy.set(vahy.size() - 1, aktualne);

        for (int k = popisSiete.length - 3; k >= 0; k--) {
            aktualne = vahy.get(k);
            vystupy = Matica.transpose(this.vystupy.get(k));
            delty = this.delty.get(k + 1);
            double [][] a = Matica.multiply(vystupy,delty);
            noveVahy = Matica.multiplyByConstant(a, uciaciPomer);

            aktualne = Matica.add(aktualne, noveVahy);

            vahy.set(k, aktualne);

        }
    }
}
