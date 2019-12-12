package com.example.littlecsd.sudoku.solve;

/**
 * The original solver of sudoku
 * Use state compression dfs to solve
 * It seems that sometimes this function may not work well
 */
public class Solver {

    private static final int MAXN = 511;
    int[] row = new int[9];
    int[] col = new int[9];
    int[] block = new int[9];
    int[][] ans = new int[9][9];

    public int[][] solve(int[][] map) {
        for (int i = 0; i < 9; i++) {
            row[i] = 0;
            col[i] = 0;
            block[i] = 0;
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                ans[i][j] = map[i][j];
                if (map[i][j] == 0) continue;
                int k = 1 << (map[i][j] - 1);
                row[i] |= k;
                col[j] |= k;
                int pos = i / 3 * 3 + j / 3;
                block[pos] |= k;
            }
        }
        if (realSolve(0, 0)) {
            return ans;
        } else {
            return null;
        }
    }

    private boolean realSolve(int x, int y) {
        if (x == 9) {
            return true;
        }
        int nextX;
        int nextY;
        if (y == 8) {
            nextX = x + 1;
            nextY = 0;
        } else {
            nextX = x;
            nextY = y + 1;
        }
        if (ans[x][y] != 0) {
            return realSolve(nextX, nextY);
        }
        int pos = x / 3 * 3 + y / 3;
        int bits = ~(row[x] | col[y] | block[pos]) & MAXN;
        if (bits == 0) return false;
        while (bits > 0) {
            int t = bits & -bits;
            row[x] |= t;
            col[y] |= t;
            block[pos] |= t;

            if (realSolve(nextX, nextY)) {
                ans[x][y] = getNum(t);
                return true;
            }

            row[x] ^= t;
            col[y] ^= t;
            block[pos] ^= t;
            bits ^= t;
        }
        return false;
    }

    private int getNum(int x) {
        int ans = 0;
        while (x > 0) {
            ans++;
            x >>= 1;
        }
        return ans;
    }
}
