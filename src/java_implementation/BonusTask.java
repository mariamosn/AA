// Copyright 2021
// Autor schelet: Matei Simtinică
// Student: Maria Moșneag 323CA

import java.io.*;
import java.util.ArrayList;

public class BonusTask extends Task {
    /**
     * numărul de familii
     */
    private int n;
    /**
     * numărul de relații (de prietenie) între familii
     */
    private int m;
    /**
     * matricea de adiacență ce reprezintă graful familiilor
     * rel[i][j] == 1 <=> Familia i și familia j se înțeleg.
     */
    private int[][] rel;

    /**
     * lista cu familiile care vor fi arestate
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

    @Override
    public void readProblemData() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inFilename));

        String[] nums = reader.readLine().trim().split("\\s+");
        n = Integer.parseInt(nums[0]);
        m = Integer.parseInt(nums[1]);

        rel = new int[n + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            String[] nums2 = reader.readLine().trim().split("\\s+");
            int u = Integer.parseInt(nums2[0]);
            int v = Integer.parseInt(nums2[1]);
            rel[u][v] = 1;
            rel[v][u] = 1;
        }
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        Writer wr = new FileWriter(oracleInFilename);

        // numărul de variabile din cadrul formulei în format cnf
        int V = n;
        // numărul de clauze
        int F = m + n;
        // suma ponderilor clauzelor soft
        int S = n;

        wr.write("p wcnf " + V + " " + F + " " + (S + 1) + "\n");

        // dacă două familii se înțeleg, cel puțin una trebuie să fie arestată (clauze hard)
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (rel[i][j] == 1) {
                    wr.write((S + 1) + " " + i + " " + j + " 0\n");
                }
            }
        }

        // dorim să arestăm cât mai puține familii (clauze soft)
        for (int i = 1; i <= n; i++) {
            wr.write(1 + " " + -i + " 0\n");
        }

        wr.close();
    }

    /**
     * decodificarea răspunsului primit de la oracol
     */
    @Override
    public void decipherOracleAnswer() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(oracleOutFilename));

        String[] nums = reader.readLine().trim().split("\\s+");
        int var = Integer.parseInt(nums[0]);

        String[] nums2 = reader.readLine().trim().split("\\s+");
        for (int i = 1; i <= var; i++) {
            int crtFam = Integer.parseInt(nums2[i - 1]);
            if (crtFam > 0) {
                sol.add(crtFam);
            }
        }
    }

    /**
     * scrierea răspunsului final în fișierul de output
     */
    @Override
    public void writeAnswer() throws IOException {
        Writer wr = new FileWriter(outFilename);

        for (Integer i :
                sol) {
            wr.write(i + " ");
        }
        wr.write("\n");

        wr.close();
    }
}
