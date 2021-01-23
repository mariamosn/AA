// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.IOException;
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

    @Override
    public void solve() throws IOException, InterruptedException {
        task2InFilename = inFilename + "_t2";
        task2OutFilename = outFilename + "_t2";
        Task2 task2Solver = new Task2();
        task2Solver.addFiles(task2InFilename, oracleInFilename, oracleOutFilename, task2OutFilename);
        readProblemData();

        // TODO: implement a way of successively querying the oracle (using Task2) about various arrest numbers until you
        //  find the minimum

        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
        // TODO: read the problem input (inFilename) and store the data in the object's attributes
        File input = new File(inFilename);
        Scanner scanner = new Scanner(input);
        n = scanner.nextInt();
        m = scanner.nextInt();;
        rel = new int[n + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            rel[u][v] = 1;
            rel[v][u] = 1;
        }
    }

    public void reduceToTask2() {
        // TODO: reduce the current problem to Task2
    }

    public void extractAnswerFromTask2() {
        // TODO: extract the current problem's answer from Task2's answer
    }

    @Override
    public void writeAnswer() throws IOException {
        // TODO: write the answer to the current problem (outFilename)
    }
}
