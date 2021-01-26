// Copyright 2021
// Autor schelet: Matei Simtinică
// Student: Maria Moșneag 323CA

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
    private ArrayList<Integer> sol = new ArrayList<>();

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

        int V = n;
        int F = m + n;
        int S = n;

        wr.write("p wcnf " + V + " " + F + " " + (S + 1) + "\n");

        // dacă două familii se înțeleg, cel puțin una trebuie să fie arestată
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (rel[i][j] == 1) {
                    wr.write((S + 1) + " " + i + " " + j + " 0\n");
                }
            }
        }

        // dorim să arestăm cât mai puține familii
        for (int i = 1; i <= n; i++) {
            wr.write(1 + " " + -i + " 0\n");
        }

        wr.close();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        // TODO: extract the current problem's answer from the answer given by the oracle (oracleOutFilename)

        File input = new File(oracleOutFilename);
        Scanner scanner = new Scanner(input);

        int var = scanner.nextInt();
        int vals = scanner.nextInt();
        for (int i = 1; i <= var; i++) {
            int crtFam = scanner.nextInt();
            if (crtFam > 0) {
                sol.add(crtFam);
            }
        }
    }

    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)

        Writer wr = new FileWriter(outFilename);
        if (sol != null) {
            for (Integer i :
                    sol) {
                wr.write(i + " ");
            }
        }
        wr.write("\n");
        wr.close();
    }
}
