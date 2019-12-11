package com.example.littlecsd.sudoku.solve;

public class DLXNode {

    private DLXNode U;
    private DLXNode D;
    private DLXNode L;
    private DLXNode R;
    private int row;
    private int col;

    public DLXNode(int row, int col) {
        this.row = row;
        this.col = col;
        U = D = L = R = this;
    }

    public DLXNode getU() {
        return U;
    }

    public void setU(DLXNode u) {
        U = u;
    }

    public DLXNode getD() {
        return D;
    }

    public void setD(DLXNode d) {
        D = d;
    }

    public DLXNode getL() {
        return L;
    }

    public void setL(DLXNode l) {
        L = l;
    }

    public DLXNode getR() {
        return R;
    }

    public void setR(DLXNode r) {
        R = r;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void addRight(DLXNode right) {
        R = right;
        right.setL(this);
    }

    public void addLeft(DLXNode left) {
        L = left;
        left.setR(this);
    }

    public void addUp(DLXNode up) {
        U = up;
        up.setD(this);
    }

    public void addDown(DLXNode down) {
        D = down;
        down.setU(this);
    }

    public void removeHorizon() {
        L.setR(R);
        R.setL(L);
    }

    public void removeVertical() {
        U.setD(D);
        D.setU(U);
    }

    public void recoverHorizon() {
        L.setR(this);
        R.setL(this);
    }

    public void recoverVertical() {
        U.setD(this);
        D.setU(this);
    }
}
