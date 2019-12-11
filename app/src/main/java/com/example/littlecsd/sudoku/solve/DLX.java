package com.example.littlecsd.sudoku.solve;

import java.util.ArrayList;
import java.util.List;

public class DLX {

    private DLXNode head = new DLXNode(-1, -1);
    private List<DLXNode> headList = new ArrayList<>();
    private int[] counts = new int[324];
    private int[] ans = new int[81];
    private int top = 0;

    public int[][] solve(int[][] map, boolean needWrite) {
        reset();
        init(map);
        boolean canSolve = dance();
        if (canSolve) {
            if (needWrite) {
                for (int i = 0; i < top; i++) {
                    int x = ans[i];
                    int num = x / 9;
                    int k = x % 9;
                    int row = num / 9;
                    int col = num % 9;
                    map[row][col] = k + 1;
                }
            }
            return map;
        } else {
            return null;
        }
    }

    private void init(int[][] map) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int num = map[i][j];
                if (num > 0) {
                    addLine(i, j, num - 1);
                } else {
                    for (int k = 0; k < 9; k++) {
                        addLine(i, j, k);
                    }
                }
            }
        }
    }

    private boolean dance() {
        DLXNode cur = head.getR();
        if (cur == head) return true;

        int minCount = counts[cur.getCol()];
        for (DLXNode t = head.getR(); t != head; t = t.getR()) {
            int tCount = counts[t.getCol()];
            if (tCount < minCount) {
                minCount = tCount;
                cur = t;
            }
        }

        removeCol(cur.getCol());

        DLXNode first = cur.getD();
        while (first != cur) {
            ans[top++] = first.getRow();
            DLXNode second = first.getR();

            while (second != first) {
                removeCol(second.getCol());
                second = second.getR();
            }

            if (dance()) return true;

            second = first.getL();
            while (second != first) {
                recoverCol(second.getCol());
                second = second.getL();
            }

            top--;
            first = first.getD();
        }

        recoverCol(cur.getCol());
        return false;
    }

    private void removeCol(int col) {
        DLXNode colNode = headList.get(col);
        colNode.removeHorizon();
        DLXNode first = colNode.getD();
        while (first != colNode) {
            DLXNode second = first.getR();
            while (second != first) {
                second.removeVertical();
                counts[second.getCol()]--;
                second = second.getR();
            }
            first = first.getD();
        }
    }

    private void recoverCol(int col) {
        DLXNode colNode = headList.get(col);
        colNode.recoverHorizon();
        DLXNode first = colNode.getU();
        while (first != colNode) {
            DLXNode second = first.getR();
            while (second != first) {
                second.recoverVertical();
                counts[second.getCol()]++;
                second = second.getR();
            }
            first = first.getU();
        }
    }

    private void reset() {
        headList.clear();
        top = 0;
        DLXNode lastNode = head;
        DLXNode node = null;
        for (int i = 0; i < 324; i++) {
            node = new DLXNode(-1, i);
            lastNode.addRight(node);
            lastNode = node;
            headList.add(node);
            counts[i] = 0;
        }
        node.addRight(head);
    }

    private void addLine(int x, int y, int k) {
        int num = x * 9 + y;
        int row = num * 9 + k;

        // add pos
        int col1 = num;
        DLXNode posNode = new DLXNode(row, col1);
        DLXNode headPos = headList.get(col1);
        DLXNode headPosU = headPos.getU();
        headPos.addUp(posNode);
        headPosU.addDown(posNode);
        counts[col1]++;

        // add row
        int col2 = 81 + x * 9 + k;
        DLXNode rowNode = new DLXNode(row, col2);
        DLXNode headRow = headList.get(col2);
        DLXNode headRowU = headRow.getU();
        headRow.addUp(rowNode);
        headRowU.addDown(rowNode);
        counts[col2]++;

        // add col
        int col3 = 162 + y * 9 + k;
        DLXNode colNode = new DLXNode(row, col3);
        DLXNode headCol = headList.get(col3);
        DLXNode headColU = headCol.getU();
        headCol.addUp(colNode);
        headColU.addDown(colNode);
        counts[col3]++;

        // add block
        int block = x / 3 * 3 + y / 3;
        int col4 = 243 + block * 9 + k;
        DLXNode blockNode = new DLXNode(row, col4);
        DLXNode headBlock = headList.get(col4);
        DLXNode headBlockU = headBlock.getU();
        headBlock.addUp(blockNode);
        headBlockU.addDown(blockNode);
        counts[col4]++;

        posNode.addRight(rowNode);
        rowNode.addRight(colNode);
        colNode.addRight(blockNode);
        blockNode.addRight(posNode);
    }
}
