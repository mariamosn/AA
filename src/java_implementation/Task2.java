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
 * Task2
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task2 extends Task {
    /**
     * numărul de familii
     */
    private int n;
    /**
     * numărul de relații (de prietenie) între familii
     */
    private int m;
    /**
     * numărul de familii dintr-o familie extinsă căutată
     */
    private int k;

    /**
     * matricea de adiacență ce reprezintă graful familiilor
     * rel[i][j] == 1 <=> Familia i și familia j se înțeleg.
     */
    private int[][] rel;

    /**
     * răspunsul oracolului (True sau False)
     */
    private String answer;
    /**
     * răspunsul final al programului,
     * reprezentat de lista de familii din familia extinsă găsită
     */
    private ArrayList<Integer> sol = new ArrayList<>();

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    /**
     * citirea datelor problemei de la input
     */
    @Override
    public void readProblemData() throws IOException {
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

    /**
     * crearea inputului pentru oracol,
     * prin reducerea problemei curente la SAT
     */
    @Override
    public void formulateOracleQuestion() throws IOException {
        Writer wr = new FileWriter(oracleInFilename);

        // numărul de variabile din cadrul formulei în format cnf
        int V = n * k;
        // numărul de clauze
        int F = n * (n - 1) / 2 * k + m * (k - 1) * k / 2 + n * (k - 1) * k / 2 + k;

        wr.write("p cnf " + V + " " + F + "\n");

        // pe o poziție a clicii poate să fie cel mult o familie
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                for (int l = 1; l <= k; l++) {
                    int var1 = (i - 1) * k + l;
                    int var2 = (j - 1) * k + l;
                    wr.write(-var1 + " " + -var2 + " 0\n");
                }
            }
        }

        // pe o poziție a clicii trebuie să fie cel puțin o familie
        for (int i = 1; i <= k; i++) {
            for (int j = 1; j <= n; j++) {
                int var = (j - 1) * k + i;
                wr.write(var + " ");
            }
            wr.write(" 0\n");
        }

        // două familii care nu se înteleg nu pot face parte din aceeași familie extinsă
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

        // o familie nu poate să apară pe mai multe poziții ale clicii
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j < k; j++) {
                for (int l = j + 1; l <= k; l++) {
                    int var1 = (i - 1) * k + j;
                    int var2 = (i - 1) * k + l;
                    wr.write(-var1 + " " + -var2 + " 0\n");
                }
            }
        }

        wr.close();
    }

    /**
     * decodificarea răspunsului primit de la oracol
     */
    @Override
    public void decipherOracleAnswer() throws IOException {
        File input = new File(oracleOutFilename);
        Scanner scanner = new Scanner(input);
        boolean flag = true;
        int crt = 0;

        answer = scanner.next();
        if (answer.equals("True")) {
            int V = scanner.nextInt();
            for (int i = 1; i <= V; i += k) {
                crt++;
                for (int j = 1; j <= k; j++) {
                    int crtVar = scanner.nextInt();
                    if (crtVar > 0) {
                        sol.add(crt);
                    }
                }
            }
        }

    }

    /**
     * scrierea răspunsului final în fișierul de output
     */
    @Override
    public void writeAnswer() throws IOException {
        Writer wr = new FileWriter(outFilename);
        wr.write(answer + "\n");
        if (answer.equals("True")) {
            for (Integer i :
                    sol) {
                wr.write(i + " ");
            }
            wr.write("\n");
        }
        wr.close();
    }
}
