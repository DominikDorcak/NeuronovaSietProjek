package sk.upjs;
/* Trieda implementuje viacvrstvovú doprednú neurónovú sieť vo forme zoznamu váh prepojení
   a topológie siete pre aproximáciu funkcie s dvojrozmerným vstupom a jednorozmerným výtupom
 */

import java.util.*;

public class Backpropagation {
    //odchylky od ocakavanych vystupov vkladam do Listu
    private List<Double> chyba = new ArrayList<>();

    public List<Double> getChyba() {
        return chyba;
    }

    public void setChyba(List<Double> chyba) {
        this.chyba = chyba;
    }

    private int[] popisSiete = {3, 17, 11, 5, 1};//topologia siete
    private double uciaciPomer = 0.05;

    private double himVystup;// vysledok funkcie him na vystupnom neurone
    private List<double[][]> himVystupy;// ostatne vysledky him
    private double vystup;//vystup na sieti
    private List<double[][]> vystupy;// vystupy ostatnych neuronov
    private List<double[][]> vahy = new ArrayList<>();//zoznam vsetkych vah
    private double delta;//delta na poslednom neurone
    private List<double[][]> delty;//delty ostatnych neuronov


    public void inicializuj() {
        //inicializacia vah
        double[][] W;
        for (int i = 0; i < popisSiete.length - 2; i++) {
            W = new double[popisSiete[i]][popisSiete[i + 1] - 1];
            for (int j = 0; j < W.length; j++) {
                for (int k = 0; k < W[j].length; k++) {
                    W[j][k] =0.02*j*i +0.01*k;
                }

            }
            vahy.add(Arrays.copyOf(W, W.length));

        }
        //prechod z poslednej vnutornej na vonkajsiu vrstvu
        W = new double[popisSiete[popisSiete.length - 2]][popisSiete[popisSiete.length - 1]];
        for (int j = 0; j < W.length; j++) {
            for (int k = 0; k < W[j].length; k++) {
                W[j][k] = 0.025*j + 0.03*k;
            }
        }
        vahy.add(Arrays.copyOf(W, W.length));
    }
    //Trenovanie jedneho vstupu
    public void trenujVstup(double x, double y, double ocakavanyVysledok) {
        VypocitajVystup(x, y);
        chyba.add(vystup - ocakavanyVysledok);
        vypocitajDelty(ocakavanyVysledok);
        nastavVahyCezMatice();

    }

    //Vypocet vystupu siete pre dany vstup
    public double VypocitajVystup(double x, double y) {
        himVystupy = new ArrayList<>();
        vystupy = new ArrayList<>();
        //najprv pocitam hodnoty funkcie him pre vsetky neurony(okrem vstupnej vrstvy)
        //na vstupnu vrstvu nahodim vstup
        double[][] vstup = new double[1][3];
        vstup[0][0] = x;
        vstup[0][1] = y;
        vstup[0][2] = -1;
        himVystupy.add(vstup);

        // ostatne vrstvy riesim pomocou nasobenia maticou vah
        for (int i = 0; i < popisSiete.length - 2; i++) {
            double[][] pole = new double[1][popisSiete[i + 1] - 1];
            pole = Matica.multiply(himVystupy.get(i), vahy.get(i));
            double[][] pole2 = new double[1][popisSiete[i + 1]];
            double[] pole3 = Arrays.copyOf(pole[0], pole2[0].length);
            pole3[pole3.length - 1] = -1;
            pole2[0] = pole3;
            himVystupy.add(Arrays.copyOf(pole2, pole2.length));
        }
        //a na koniec vystupna vrstva
        himVystup = Matica.multiply(himVystupy.get(popisSiete.length - 2), vahy.get(popisSiete.length - 2))[0][0];
        
        //vystupy him funkcie prezeniem aktivacnou funkciou pre zisaknie vystupov(vstupna vrstva zostava)
        vystupy = himVystupy.subList(0, himVystupy.size());
        for (int j = 1; j < vystupy.size(); j++) {
            double[][] d = vystupy.get(j);
            for (int i = 0; i < d[0].length - 1; i++) {
                d[0][i] = Funkcie.aktivacna(himVystupy.get(j)[0][i]);

            }
            vystupy.set(j, d);
        }
        vystup = Funkcie.aktivacna(himVystup);
        return vystup;
    }
    
    //Spatna propagacia chyby
    public void vypocitajDelty(double ocakavanyVysledok) {
        delty = new ArrayList<>();//inicializujem delty
        //vypocet delty na vystupe
        delta = Funkcie.aktivacnaDerivovana(himVystup) * (ocakavanyVysledok - vystup);

        double[][] aktualne = new double[1][popisSiete[popisSiete.length - 2] - 1];
        double[] him = himVystupy.get(himVystupy.size() - 1)[0];
        double sumar = 0;
        double[][] wm = vahy.get(vahy.size() - 1);
        double[] deltaMPlusJeden;
        // z vystupnej na poslednu skrytu vrstvu
        for (int j = 0; j < aktualne.length; j++) {
            for (int i = 0; i < wm.length; i++) {
                sumar = wm[i][0] * delta;
            }
            aktualne[0][j] = Funkcie.aktivacnaDerivovana(him[j]) * (sumar);
        }
        delty.add(aktualne);

        //ostatne vrstvy
        for (int k = popisSiete.length - 3; k >= 0; k--) {
            aktualne = new double[1][popisSiete[k] - 1];
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
        //delty ukladam od zadu preto na koniec otocim poradie
        Collections.reverse(delty);
    }

    //Nastavenie novych vah pomocou operacii s maticami
    public void nastavVahyCezMatice() {
        double[][] stareVahy = vahy.get(vahy.size() - 1);
        double[][] vystupy = this.vystupy.get(this.vystupy.size() - 1);
        double[][] delty;
        double[][] noveVahy;

        for (int i = 0; i < stareVahy.length; i++) {
            stareVahy[i][0] = stareVahy[i][0] + (uciaciPomer * delta * vystupy[0][i]);
        }
        vahy.set(vahy.size() - 1, stareVahy);

        for (int k = popisSiete.length - 3; k >= 0; k--) {
            stareVahy = vahy.get(k);
            vystupy = Matica.transpose(this.vystupy.get(k));
            delty = this.delty.get(k + 1);
            double[][] a = Matica.multiply(vystupy, delty);
            noveVahy = Matica.multiplyByConstant(a, uciaciPomer);

            stareVahy = Matica.add(stareVahy, noveVahy);

            vahy.set(k, stareVahy);

        }
    }


}
