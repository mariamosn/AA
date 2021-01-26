// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Bonus Task
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class BonusTask extends Task {
    // TODO: define necessary variables and/or data structures
    private int n, m;
    private int[][] rel;
    private ArrayList<Integer> sol;
    private int[][] complAdjMatrix;

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
        // TODO: read the problem input (inFilename) and store the data in the object's attributes
        File input = new File(inFilename);
        Scanner scanner = new Scanner(input);
        n = scanner.nextInt();
        m = scanner.nextInt();
        rel = new int[n + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            rel[u][v] = 1;
            rel[v][u] = 1;
        }
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        // TODO: transform the current problem into a SAT problem and write it (oracleInFilename) in a format
        //  understood by the oracle

        Writer wr = new FileWriter(oracleInFilename);

        int V = n * n;
        int F = 0;
        int S = 0;

        for (int k = n; k >= 1; k--) {
            F += n * (n - 1) / 2 * k + m * (k - 1) * k / 2 + n * (k - 1) * k / 2 + k + n * k;
            S += k * F + k * F - 1;
        }

        wr.write("p wcnf " + V + " " + F + " " + (S + 1) + "\n");

        for (int k = 1; k <= n; k++) {
            // doua familii nu pot fi pe aceeasi pozitie din clica
            int offset = n * (k - 1) * k / 2;
            for (int i = 1; i < n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    for (int l = 1; l <= k; l++) {
                        int var1 = (i - 1) * k + l + offset;
                        int var2 = (j - 1) * k + l + offset;
                        wr.write(k + " " + -var1 + " " + -var2+ " 0\n");
                    }
                }
            }

            // doua familii care nu se inteleg nu pot face parte din aceeasi familie extinsa
            for (int i = 1; i < n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    if (complAdjMatrix[i][j] != 1) {
                        for (int a = 1; a <= k; a++) {
                            for (int b = 1; b <= k; b++) {
                                int var1 = (i - 1) * k + a + offset;
                                int var2 = (j - 1) * k + b + offset;
                                wr.write(k + " " + -var1 + " " + -var2 + " 0\n");
                            }
                        }
                    }
                }
            }

            // o familie nu poate sa apara pe mai multe pozitii ale clicii
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j < k; j++) {
                    for (int l = j + 1; l <= k; l++) {
                        int var1 = (i - 1) * k + j + offset;
                        int var2 = (i - 1) * k + l + offset;
                        wr.write(k + " " + -var1 + " " + -var2 + " 0\n");
                    }
                }
            }

            // pe o pozitie a clicii trebuie sa fie cel putin o familie
            for (int i = 1; i <= k; i++) {
                wr.write(k + " ");
                for (int j = 1; j <= n; j++) {
                    int var = (j - 1) * k + i + offset;
                    wr.write(var + " ");
                }
                wr.write(" 0\n");
            }

            // clauza suplimentara care sa faca toate variabilele 0 in cazul in care nu exista o clica
            int pond = 0;
            for (int i = 1; i <= k; i++) {
                for (int j = 1; j <= n; j++) {
                    int var = (j - 1) * k + i + offset;
                    wr.write(pond + " " + -var + "0\n");
                }
            }
        }

        wr.close();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        // TODO: extract the current problem's answer from the answer given by the oracle (oracleOutFilename)

        File input = new File(oracleOutFilename);
        Scanner scanner = new Scanner(input);

        String answer = scanner.next();
        if (answer.equals("True")) {
            int V = scanner.nextInt();
            sol = new ArrayList<>();
            for (int i = 1; i <= V; i++) {
                sol.add(scanner.nextInt());
            }
        }
    }

    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)

        Writer wr = new FileWriter(outFilename);

        for (Integer i :
                sol) {
            wr.write(i + " ");
        }
        wr.write("\n");

        wr.close();
    }

    private void createComplementaryAdjMatrix() {
        complAdjMatrix = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    complAdjMatrix[i][j] = 0;
                } else {
                    complAdjMatrix[i][j] = Math.abs(rel[i][j] - 1);
                }
            }
        }
    }
}
