package sk.upjs;

import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class NeuronovaSiet {

    public static double fxy(double x, double y) {
        return (x + (y * y)) / Math.sqrt(x + 1);
    }

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

        double[][] cvicnaVzorka = new double[3][121];

        int pocitadlo = 0;
        while (pocitadlo < 11) {
            for (int i = 0; i < 11; i++) {
                cvicnaVzorka[0][i + 11 * pocitadlo] = 0.2 * pocitadlo;
                cvicnaVzorka[1][i + 11 * pocitadlo] = 0.2 * i;
                cvicnaVzorka[2][i + 11 * pocitadlo] = fxy(0.2 * pocitadlo, 0.2 * i);
            }
            pocitadlo++;

        }
        vypis(cvicnaVzorka);
    }

}
