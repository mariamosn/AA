// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Task2
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task2 extends Task {
    // TODO: define necessary variables and/or data structures
    private int n, m, k;
    private int[][] rel;
    private String answer;
    private ArrayList<Integer> sol = null;

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
        m = scanner.nextInt();;
        k = scanner.nextInt();
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

        int V = n * k;
        int F = n * (n - 1) / 2 * k + m * (k - 1) * k / 2 + n * (k - 1) * k / 2 + k;

        wr.write("p cnf " + V + " " + F + "\n");

        // doua familii nu pot fi pe aceeasi pozitie din clica
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                for (int l = 1; l <= k; l++) {
                    int var1 = (i - 1) * k + l;
                    int var2 = (j - 1) * k + l;
                    wr.write(-var1 + " " + -var2 + " 0\n");
                }
            }
        }

        // doua familii care nu se inteleg nu pot face parte din aceeasi familie extinsa
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (rel[i][j] != 1) {
                    for (int a = 1; a <= k; a++) {
                        for (int b = 1; b <= k; b++) {
                            int var1 = (i - 1) * k + a;
                            int var2 = (j - 1) * k + b;
                            wr.write(-var1 + " " + -var2 + " 0\n");
                        }
                    }
                }
            }
        }

        // o familie nu poate sa apara pe mai multe pozitii ale clicii
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j < k; j++) {
                for (int l = j + 1; l <= k; l++) {
                    int var1 = (i - 1) * k + j;
                    int var2 = (i - 1) * k + l;
                    wr.write(-var1 + " " + -var2 + " 0\n");
                }
            }
        }

        // pe o pozitie a clicii trebuie sa fie cel putin o familie
        for (int i = 1; i <= k; i++) {
            for (int j = 1; j <= n; j++) {
                int var = (j - 1) * k + i;
                wr.write(var + " ");
            }
            wr.write(" 0\n");
        }

        wr.close();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        // TODO: extract the current problem's answer from the answer given by the oracle (oracleOutFilename)

        File input = new File(oracleOutFilename);
        Scanner scanner = new Scanner(input);

        answer = scanner.next();
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
        boolean flag = true;
        int crt = 0;
        wr.write(answer + "\n");
        if (answer.equals("True")) {
            for (int i = 0; i < sol.size(); i += k) {
                crt++;
                flag = true;
                for (int j = 0; j < k && flag; j++) {
                    if (sol.get(i + j) > 0) {
                        wr.write(crt + " ");
                        flag = false;
                    }
                }
            }
        }
        wr.close();
    }
}
