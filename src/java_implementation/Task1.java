// Copyright 2021
// Autor schelet: Matei Simtinică
// Student: Maria Moșneag 323CA

import java.io.*;
import java.util.ArrayList;

public class Task1 extends Task {
    /**
     * numărul de familii
      */
    private int n;
    /**
     * numărul de relații (de prietenie) între familii
     */
    private int m;
    /**
     * numărul de spioni disponibili
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
     * reprezentat de lista de spioni asignați fiecărei familii
     */
    private final ArrayList<Integer> sol = new ArrayList<>();

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
        BufferedReader reader = new BufferedReader(new FileReader(inFilename));
        String[] nums = reader.readLine().trim().split("\\s+");
        n = Integer.parseInt(nums[0]);
        m = Integer.parseInt(nums[1]);
        k = Integer.parseInt(nums[2]);

        rel = new int[n + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            String[] nums2 = reader.readLine().trim().split("\\s+");
            int u = Integer.parseInt(nums2[0]);
            int v = Integer.parseInt(nums2[1]);
            rel[u][v] = 1;
            rel[v][u] = 1;
        }

        reader.close();
    }

    /**
     * crearea inputului pentru oracol, prin reducerea problemei curente la SAT
     */
    @Override
    public void formulateOracleQuestion() throws IOException {
        Writer wr = new FileWriter(oracleInFilename);

        // numărul de variabile din cadrul formulei în format cnf
        int V = n * k;
        // numărul de clauze
        int F = n + (k - 1) * k / 2 * n + m * k;

        wr.write("p cnf " + V + " " + F + "\n");

        // o familie de mafioti trebuie să aibă cel puțin un spion infiltrat
        for (int i = 1; i <= n; i++) {
            int baseVal = (i - 1) * k;
            for (int j = 1; j <= k; j++) {
                wr.write((baseVal + j) + " ");
            }
            wr.write(" 0\n");
        }

        // o familie poate avea cel mult un spion infiltrat
        for (int i = 1; i <= n; i++) {
            int baseVal = (i - 1) * k;
            for (int j = 1; j < k; j++) {
                for (int l = j + 1; l <= k; l++) {
                    wr.write(-(baseVal + j) + " " + -(baseVal + l) + " 0\n");
                }
            }
        }

        // două familii care se înțeleg nu pot avea asignat același spion
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

    /**
     * decodificarea răspunsului primit de la oracol
     */
    @Override
    public void decipherOracleAnswer() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(oracleOutFilename));
        boolean flag;
        answer = reader.readLine();
        if (answer.equals("True")) {
            int V = Integer.parseInt(reader.readLine());

            String[] nums = reader.readLine().trim().split("\\s+");
            for (int i = 1; i <= V; i += k) {
                flag = true;
                for (int j = 1; j <= k && flag; j++) {
                    int crt = Integer.parseInt(nums[(i - 1) + (j - 1)]);
                    if (crt > 0) {
                        sol.add(j);
                        flag = false;
                    }
                }
            }
        }
        reader.close();
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
        }

        wr.close();
    }
}
