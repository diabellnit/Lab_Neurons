class MultiLayerPerceptron {
    private double[][] weights;
    private int[] layerSizes;
    private double learningRate;

    public MultiLayerPerceptron(int[] layerSizes, double learningRate) {
        this.layerSizes = layerSizes;
        this.learningRate = learningRate;
        weights = new double[layerSizes.length - 1][][];

        Random rand = new Random();
        for (int i = 0; i < layerSizes.length - 1; i++) {
            weights[i] = new double[layerSizes[i]][layerSizes[i + 1]];
            for (int j = 0; j < layerSizes[i]; j++) {
                for (int k = 0; k < layerSizes[i + 1]; k++) {
                    weights[i][j][k] = rand.nextDouble() - 0.5; // Инициализация случайными значениями
                }
            }
        }
    }

    public double[] predict(double[] inputs) {
        double[] outputs = inputs.clone();
        for (int i = 0; i < layerSizes.length - 1; i++) {
            outputs = forward(outputs, i);
        }
        return outputs;
    }

    private double[] forward(double[] inputs, int layerIndex) {
        double[] outputs = new double[layerSizes[layerIndex + 1]];
        for (int j = 0; j < layerSizes[layerIndex + 1]; j++) {
            outputs[j] = 0;
            for (int k = 0; k < layerSizes[layerIndex]; k++) {
                outputs[j] += inputs[k] * weights[layerIndex][k][j];
            }
            outputs[j] = activationFunction(outputs[j]);
        }
        return outputs;
    }

    private double activationFunction(double x) {
        return 1 / (1 + Math.exp(-x)); // Сигмоида
    }

    public double[][][] getWeights() {
        return weights;
    }

    public void setWeights(double[][][] newWeights) {
        this.weights = newWeights;
    }
}
