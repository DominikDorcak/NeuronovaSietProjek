package sk.upjs;
/*
Spustiteľná trieda s generovaním tréningovej zložky a požiadavkami na trénovanie
 */
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class NeuronovaSiet {
   

    //vypis  treningovej vzorky do suboru
    public static void vypis(double[][] cvicnaVzorka) {
        PrintWriter pw = null;

        try {
            pw = new PrintWriter("CvicnaVzorka.csv");
            for (int i = 0; i < 121; i++) {
                pw.append(cvicnaVzorka[0][i] + "\t " + cvicnaVzorka[1][i] + "\t " + cvicnaVzorka[2][i] + "\n");

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
        vypis(cvicnaVzorka);
        //trenovanie siete
        Backpropagation bp = new Backpropagation();
        bp.inicializuj();
        for (int k = 0; k < 10; k++) {

            for (int i = 0; i < cvicnaVzorka[0].length; i++) {
                bp.trenujVstup(cvicnaVzorka[0][i], cvicnaVzorka[1][i], cvicnaVzorka[2][i]);

            }

            //vypocet chybovej funkcie
            double chyba = 0;
            for (Double d : bp.getChyba()) {
                chyba = chyba + (d * d * 0.5);

            }
            System.out.println("iteracia: " + k + ", chyba: " + chyba/cvicnaVzorka[0].length);
            // po natrenovani siete na celej cvicnej vzorke list s chybami vyprazdnim
            bp.setChyba(new ArrayList<>());


        }


    }

}
