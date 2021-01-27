// Copyright 2021
// Autor schelet: Matei Simtinică
// Student: Maria Moșneag 323CA

import java.io.*;
import java.util.ArrayList;

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
        int F = n * (n - 1) / 2 * k + k + m * k * k + n * (k - 1) * k / 2;
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
        BufferedReader reader = new BufferedReader(new FileReader(oracleOutFilename));
        int crt = 0;
        boolean flag;

        answer = reader.readLine();
        if (answer.equals("True")) {
            int V = Integer.parseInt(reader.readLine());

            String[] nums = reader.readLine().trim().split("\\s+");
            for (int i = 1; i <= V; i += k) {
                crt++;
                flag = true;
                for (int j = 1; j <= k && flag; j++) {
                    int crtVar = Integer.parseInt(nums[(i - 1) + (j - 1)]);
                    if (crtVar > 0) {
                        sol.add(crt);
                        flag = false;
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
