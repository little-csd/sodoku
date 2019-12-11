package com.example.littlecsd.sudoku.process;

public class Helper {

    public static double[][] MatrixMulMatrix(double[][] matrix1, double[][] matrix2) {
        int row = matrix1.length;
        int col = matrix2[0].length;
        int cross = matrix1[0].length;
        if (cross != matrix2.length) {
            throw new RuntimeException("Could not multiply two matrix of (" + row + ","
                    + cross + ") and (" + matrix2.length + "," + col + ")");
        }
        double[][] result = new double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int sum = 0;
                for (int k = 0; k < cross; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    public static double[] MatrixMulVector(double[][] matrix1, double[] vector) {
        int row = matrix1.length;
        int cross = matrix1[0].length;
        if (cross != vector.length) {
            throw new RuntimeException("Could not multiply a matrix of " + "("
                    + row + "," + cross + ") and a vector of length " + vector.length);
        }
        double[] result = new double[row];
        for (int i = 0; i < row; i++) {
            int sum = 0;
            for (int k = 0; k < cross; k++) {
                sum += matrix1[i][k] * vector[k];
            }
            result[i] = sum;
        }
        return result;
    }

    public static void MatrixMulFactor(double[][] matrix, double factor) {
        int row = matrix.length;
        int col = matrix[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] *= factor;
            }
        }
    }

    public static double[][] Transpose(double[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        double[][] result = new double[col][row];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    public static double[] Matrix2Vector(double[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        double[] res = new double[row * col];
        for (int i = 0; i < row; i++) {
            System.arraycopy(matrix[i], 0, res, i * col, col);
        }
        return res;
    }

    public static double[] Matrix2Vector(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        double[] res = new double[row * col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                res[i * col + j] = matrix[i][j] + 1;
            }
        }
        return res;
    }

    public static void Randomize(double[][] matrix, double factor) {
        int row = matrix.length;
        int col = matrix[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = Math.random() * factor;
            }
        }
    }

    public static double[][] Minus(double[][] matrix1, double[][] matrix2) {
        int row = matrix1.length;
        int col = matrix1[0].length;
        double[][] res = new double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                res[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }
        return res;
    }

    public static void Descend(double[][] matrix1, double[][] matrix2, double learningRate) {
        int row = matrix1.length;
        int col = matrix1[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix1[i][j] += matrix2[i][j] * learningRate;
            }
        }
    }
}
