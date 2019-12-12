package com.example.littlecsd.sudoku.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The class that generate generate sudoku arrays (without collision
 * but may have no solution).
 */
public class Generator {

    private static final int MAXN = 511;
    int[] row = new int[9];
    int[] col = new int[9];
    int[] block = new int[9];

    public Generator() {

    }

    public int[][] generate(int min, int max) {
        if (max <= min) return null;
        int delta = max - min;
        int num = (int) (Math.random() * delta) + min;
        return generate(num);
    }

    public int[][] generate(int num) {
        for (int i = 0; i < 9; i++) {
            row[i] = 0;
            col[i] = 0;
            block[i] = 0;
        }
        int[][] ans = new int[9][9];
        int[] wait = new int[9];
        List<Integer> pos = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            pos.add(i);
        }
        Collections.shuffle(pos);
        int cur = 0;
        Random random = new Random();
        while (num > 0) {
            int p = pos.get(cur);
            cur++;
            int x = p / 9;
            int y = p % 9;
            int k = getBlockPos(x, y);
            int bits = ~(row[x] | col[y] | block[k]) & MAXN;
            if (bits == 0) continue;
            int length = 0;
            while (bits > 0) {
                int xx = bits & -bits;
                wait[length] = xx;
                length++;
                bits = bits ^ xx;
            }
            int opt = wait[random.nextInt(length)];
            row[x] |= opt;
            col[y] |= opt;
            block[k] |= opt;
            ans[x][y] = getNum(opt);
            num--;
        }
        return ans;
    }

    private int getNum(int x) {
        int ans = 0;
        while (x > 0) {
            ans++;
            x >>= 1;
        }
        return ans;
    }

    private int getBlockPos(int x, int y) {
        return x / 3 * 3 + y / 3;
    }
}
