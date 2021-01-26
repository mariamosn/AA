// Copyright 2020
// Author: Matei Simtinică

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Task3
 * This being an optimization problem, the solve method's logic has to work differently.
 * You have to search for the minimum number of arrests by successively querying the oracle.
 * Hint: it might be easier to reduce the current task to a previously solved task
 */
public class Task3 extends Task {
    String task2InFilename;
    String task2OutFilename;
    // TODO: define necessary variables and/or data structures
    private int n, m;
    private int[][] rel;
    private int[][] complAdjMatrix;
    private int crtK, complM = 0;
    private String crtAns = new String("False");
    private ArrayList<Integer> antisocialFamilies;

    @Override
    public void solve() throws IOException, InterruptedException {
        task2InFilename = inFilename + "_t2";
        task2OutFilename = outFilename + "_t2";
        Task2 task2Solver = new Task2();
        task2Solver.addFiles(task2InFilename, oracleInFilename, oracleOutFilename, task2OutFilename);
        readProblemData();

        // TODO: implement a way of successively querying the oracle (using Task2) about various arrest numbers until you
        //  find the minimum
        for (int i = n; i >= 1 && crtAns.equals("False"); i--) {
            crtK = i;
            reduceToTask2();
            task2Solver.solve();
            extractAnswerFromTask2();
        }

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
        createComplementaryAdjMatrix();

        int mMax = (n - 1) * n / 2;
        complM = mMax - m;
    }

    public void reduceToTask2() throws IOException {
        // TODO: reduce the current problem to Task2
        Writer wr = new FileWriter(task2InFilename);

        wr.write(n + " ");
        wr.write(complM + " ");
        wr.write(crtK + "\n");

        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (complAdjMatrix[i][j] == 1) {
                    wr.write(i + " " + j + "\n");
                }
            }
        }

        wr.close();
    }

    public void extractAnswerFromTask2() throws FileNotFoundException {
        // TODO: extract the current problem's answer from Task2's answer
        File task2Ans = new File(task2OutFilename);
        Scanner scanner = new Scanner(task2Ans);

        crtAns = scanner.next();
        if (crtAns.equals("True")) {
            antisocialFamilies = new ArrayList<>();
            while (scanner.hasNext()) {
                antisocialFamilies.add(scanner.nextInt());
            }
        }
    }

    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)

        Writer wr = new FileWriter(outFilename);

        for (int i = 1; i <= n; i++) {
            if (!antisocialFamilies.contains(i)) {
                wr.write(i + " ");
            }
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
