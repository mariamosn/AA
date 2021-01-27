// Copyright 2021
// Autor schelet: Matei Simtinică
// Student: Maria Moșneag 323CA

import java.io.*;
import java.util.ArrayList;

public class Task3 extends Task {
    String task2InFilename;
    String task2OutFilename;

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
     * matricea de adiacență complementară
     * rel[i][j] == 1 <=> Familiile i și j nu se înțeleg.
     */
    private int[][] complAdjMatrix;
    /**
     * numărul "relațiilor de neînțelegere"
     */
    private int complM = 0;
    /**
     * dimensiunea curentă a clicii căutate
     */
    private int crtK;
    /**
     * răspunsul ultimei interogări a oracolului
     */
    private String crtAns = "False";
    /**
     * lista cu familiile care nu vor fi arestate
     */
    private final ArrayList<Integer> antisocialFamilies = new ArrayList<>();

    /**
     * lista cu familiile care vor fi arestate
     */
    private final ArrayList<Integer> sol = new ArrayList<>();

    @Override
    public void solve() throws IOException, InterruptedException {
        task2InFilename = inFilename + "_t2";
        task2OutFilename = outFilename + "_t2";
        Task2 task2Solver = new Task2();
        task2Solver.addFiles(task2InFilename, oracleInFilename, oracleOutFilename, task2OutFilename);
        readProblemData();

        for (int i = n; i >= 1 && crtAns.equals("False"); i--) {
            crtK = i;
            reduceToTask2();
            task2Solver.solve();
            extractAnswerFromTask2();
        }

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

        rel = new int[n + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            String[] nums2 = reader.readLine().trim().split("\\s+");
            int u = Integer.parseInt(nums2[0]);
            int v = Integer.parseInt(nums2[1]);
            rel[u][v] = 1;
            rel[v][u] = 1;
        }

        createComplementaryAdjMatrix();

        reader.close();
    }

    /**
     * reducerea problemei curente la task-ul 2 și crearea input-ului pentru acesta
     */
    public void reduceToTask2() throws IOException {
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

    /**
     * decodificarea răspunsului primit de la task2
     */
    public void extractAnswerFromTask2() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(task2OutFilename));

        crtAns = reader.readLine();
        if (crtAns.equals("True")) {
            // obținerea listei de familii care nu vor fi arestate
            String[] nums = reader.readLine().trim().split("\\s+");
            for (String s:
                 nums) {
                antisocialFamilies.add(Integer.parseInt(s));
            }

            // crearea listei de familii ce urmează să fie arestate
            for (int i = 1; i <= n; i++) {
                if (!antisocialFamilies.contains(i)) {
                    sol.add(i);
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

        for (Integer i :
                sol) {
            wr.write(i + " ");
        }
        wr.write("\n");

        wr.close();
    }

    /**
     * construirea matricii complementare de adiacență
     */
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

        int mMax = (n - 1) * n / 2;
        complM = mMax - m;
    }
}
