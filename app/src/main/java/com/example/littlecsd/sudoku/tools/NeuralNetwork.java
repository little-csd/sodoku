package com.example.littlecsd.sudoku.tools;

import java.util.Arrays;

public class NeuralNetwork {

    private static final int NODE_INPUT = 81;
    private static final int NODE_HIDE = 32;
    private static final int NODE_OUTPUT = 1;

    private double learningRate;

    private double[][] w1;
    private double[][] w2;

    private double[][] hide;
    private double[][] output;

    public NeuralNetwork(double learningRate) {
        this.learningRate = learningRate;
        w1 = new double[NODE_INPUT][NODE_HIDE];
        Helper.Randomize(w1, 1000);
        w2 = new double[NODE_HIDE][NODE_OUTPUT];
        Helper.Randomize(w2, 1000);
    }

    // data: N by 81
    // result: N by 1
    public void train(double[][] data, double[][] result, int times) {
        if (data.length != result.length) {
            throw new RuntimeException("Data error!");
        }
        int N = result.length;

        double[][] dataT = Helper.Transpose(data);
        while (times > 0) {
            hide = Helper.MatrixMulMatrix(data, w1);
            output = Helper.MatrixMulMatrix(hide, w2);

            double[][] dOutput = Helper.Minus(result, output);
            double[][] dW2 = Helper.MatrixMulMatrix(Helper.Transpose(hide), dOutput);
            double[][] dHide = Helper.MatrixMulMatrix(dOutput, Helper.Transpose(w2));
            double[][] dW1 = Helper.MatrixMulMatrix(dataT, dHide);

            Helper.Descend(w1, dW1, learningRate);
            Helper.Descend(w2, dW2, learningRate);
            times--;
        }
    }

    public double[][] predict(double[][] data) {
        print(w1);
        print(w2);
        hide = Helper.MatrixMulMatrix(data, w1);
        output = Helper.MatrixMulMatrix(hide, w2);
        Helper.MatrixMulFactor(output, 0.00001);
        return output;
    }

    private static void print(double[][] ans) {
        if (ans == null) {
            System.out.println("null");
            return;
        }
        for (int i = 0; i < 9; i++) {
            System.out.println(Arrays.toString(ans[i]));
        }
    }
}
