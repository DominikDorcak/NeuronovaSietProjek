package sk.upjs;
/*
Spustiteľná trieda s generovaním tréningovej zložky a požiadavkami na trénovanie
 */
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;


public class NeuronovaSiet {
   

    //vypis  treningovej vzorky do suboru
    public static void vypis(double[][] pole, String nazovSuboru) {
        PrintWriter pw = null;

        try {
            pw = new PrintWriter(nazovSuboru);
            for (int i = 0; i < 121; i++) {
                pw.append(pole[0][i] + "\t " + pole[1][i] + "\t " + pole[2][i] + "\n");

            }

        } catch (FileNotFoundException e) {

        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }



    public static void main(String[] args) {
        //generovanie cvicnej vzorky
        double[][] cvicnaVzorka = new double[3][121];

        int pocitadlo = 0;
        while (pocitadlo < 11) {
            for (int i = 0; i < 11; i++) {
                cvicnaVzorka[0][i + 11 * pocitadlo] = (0.2 * pocitadlo);
                cvicnaVzorka[1][i + 11 * pocitadlo] = (0.2 * i);
                cvicnaVzorka[2][i + 11 * pocitadlo] = (Funkcie.fxy(0.2 * pocitadlo, 0.2 * i));
            }
            pocitadlo++;

        }
        vypis(cvicnaVzorka, "CvicnaVzorka.csv");
        //trenovanie siete
        Backpropagation bp = new Backpropagation();
        bp.inicializuj();
        double[] chybovaFunkcia = new double[332];
        for (int k = 0; k < 332; k++) {

            for (int i = 0; i < cvicnaVzorka[0].length; i++) {
                bp.trenujVstup(cvicnaVzorka[0][i], cvicnaVzorka[1][i], cvicnaVzorka[2][i]);

            }

            //vypocet chybovej funkcie
            double chyba = 0;
            for (Double d : bp.getChyba()) {
                chyba = chyba + (d * d * 0.5);

            }
            chybovaFunkcia[k] = chyba;
            System.out.println("iteracia: " + k + ", chyba: " + chyba);
            // po natrenovani siete na celej cvicnej vzorke list s chybami vyprazdnim
            bp.setChyba(new ArrayList<>());



            }

        double [][] vysledky = cvicnaVzorka.clone();

        int pocitadlo2 = 0;
        while (pocitadlo2 < 11) {
            for (int i = 0; i < 11; i++){
                vysledky[2][i + 11 * pocitadlo2] = bp.VypocitajVystup(vysledky[0][i + 11 * pocitadlo2],vysledky[1][i + 11 * pocitadlo2]);
        }
        pocitadlo2++;


        }
        vypis(vysledky, "Vysledky.csv");

        PrintWriter pw = null;

        try {
            pw = new PrintWriter("Chyba.csv");
            for (int i = 0; i < chybovaFunkcia.length; i++) {
                pw.append(chybovaFunkcia[i] + "\n");

            }

        } catch (FileNotFoundException e) {

        } finally {
            if (pw != null) {
                pw.close();
            }
        }

       double[][] ocakavaneVysledky = new double[3][121];
        for(int h = 0; h <vysledky[0].length;h++){
            vysledky[0][h] = Math.random()*2d;
            ocakavaneVysledky[0][h] = vysledky[0][h];
            vysledky[1][h] = Math.random()*2d;
            ocakavaneVysledky[1][h] = vysledky[1][h];
            vysledky[2][h] = bp.VypocitajVystup(vysledky[0][h],vysledky[1][h]);
            ocakavaneVysledky[2][h] = Funkcie.fxy(ocakavaneVysledky[0][h],ocakavaneVysledky[1][h]);
        }
        vypis(vysledky, "NaVstupochMimoCvicnejVzorky.csv");
        vypis(ocakavaneVysledky,"Ocakavane.csv");
    }

}
