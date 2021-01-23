// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Task1
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task1 extends Task {
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
        m = scanner.nextInt();
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
        int F = n + (k - 1) * k / 2 * n + m * k;

        wr.write("p cnf " + V + " " + F + "\n");

        // o familie de mafioti trebuie sa aiba un spion infiltrat
        for (int i = 1; i <= n; i++) {
            int baseVal = (i - 1) * k;
            for (int j = 1; j <= k; j++) {
                wr.write((baseVal + j) + " ");
            }
            wr.write(" 0\n");
        }

        // o familie are asignat un singur spion
        for (int i = 1; i <= n; i++) {
            int baseVal = (i - 1) * k;
            for (int j = 1; j < k; j++) {
                for (int l = j + 1; l <= k; l++) {
                    wr.write(-(baseVal + j) + " " + -(baseVal + l) + " 0\n");
                }
            }
        }

        // doua familii prietene nu pot avea acelasi spion infiltrat
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (rel[i][j] == 1) {
                    int baseVal1 = (i - 1) * k;
                    int baseVal2 = (j - 1) * k;
                    for (int l = 1; l <= k; l++) {
                        wr.write(-(baseVal1 + l) + " " + -(baseVal2 + l) + " 0\n");
                    }
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
        wr.write(answer + "\n");
        if (answer.equals("True")) {
            for (int i = 0; i < sol.size(); i += k) {
                flag = true;
                for (int j = 0; j < k && flag; j++) {
                    if (sol.get(i + j) > 0) {
                        wr.write((j + 1) + " ");
                        flag = false;
                    }
                }
            }
        }
        wr.close();
    }
}
