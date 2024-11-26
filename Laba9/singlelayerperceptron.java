import java.util.Random;

class SingleLayerPerceptron {
    private double[] weights;
    private double learningRate;

    public SingleLayerPerceptron(int inputSize, double learningRate) {
        weights = new double[inputSize];
        this.learningRate = learningRate;
        Random rand = new Random();
        for (int i = 0; i < inputSize; i++) {
            weights[i] = rand.nextDouble() - 0.5; // Инициализация случайными значениями
        }
    }

    public double predict(double[] inputs) {
        double sum = 0;
        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i];
        }
        return activationFunction(sum);
    }

    private double activationFunction(double x) {
        return x >= 0 ? 1 : 0; // Простейшая активация
    }

    public void updateWeights(double[] inputs, double error) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] += learningRate * error * inputs[i];
        }
    }

    public double[] getWeights() {
        return weights.clone();
    }

    public void setWeights(double[] newWeights) {
        weights = newWeights;
    }
}
