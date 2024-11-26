import java.util.Random;

class Neuron {
    double[] weights;
    double output;
    double delta;

    public Neuron(int inputs) {
        weights = new double[inputs + 1]; // +1 для bias
        Random rand = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = rand.nextDouble() * 2 - 1; // Инициализация случайными значениями
        }
    }

    public double activate(double[] inputs) {
        double sum = weights[0]; // bias
        for (int i = 0; i < inputs.length; i++) {
            sum += weights[i + 1] * inputs[i];
        }
        return sigmoid(sum);
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public void calculateDelta(double target) {
        delta = output * (1 - output) * (target - output);
    }

    public void updateWeights(double[] inputs, double learningRate) {
        weights[0] += learningRate * delta; // Обновление bias
        for (int i = 0; i < inputs.length; i++) {
            weights[i + 1] += learningRate * delta * inputs[i];
        }
    }
}

class NeuralNetwork {
    private Neuron[] layer1;
    private Neuron outputNeuron;

    public NeuralNetwork(int inputSize, int hiddenSize) {
        layer1 = new Neuron[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            layer1[i] = new Neuron(inputSize);
        }
        outputNeuron = new Neuron(hiddenSize);
    }

    public double forward(double[] inputs) {
        double[] hiddenOutputs = new double[layer1.length];
        for (int i = 0; i < layer1.length; i++) {
            hiddenOutputs[i] = layer1[i].activate(inputs);
        }
        outputNeuron.output = outputNeuron.activate(hiddenOutputs);
        return outputNeuron.output;
    }

    public void train(double[][] inputPatterns, double[] targets, double learningRate, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputPatterns.length; i++) {
                double[] inputs = inputPatterns[i];
                double target = targets[i];

                // Прямое распространение
                forward(inputs);

                // Обратное распространение
                outputNeuron.calculateDelta(target);
                for (Neuron neuron : layer1) {
                    neuron.calculateDelta(neuron.output);
                }

                // Обновление весов
                outputNeuron.updateWeights(new double[layer1.length], learningRate);
                for (int j = 0; j < layer1.length; j++) {
                    layer1[j].updateWeights(inputs, learningRate);
                }
            }
        }
    }
}

public class XORNeuralNetwork {
    public static void main(String[] args) {
        double[][] inputPatterns = {
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1}
        };
        double[] targets = {0, 1, 1, 0}; // XOR

        NeuralNetwork nn = new NeuralNetwork(2, 2); // 2 входа, 2 скрытых нейрона
        nn.train(inputPatterns, targets, 0.1, 10000); // Обучение

        // Тестирование
        for (double[] pattern : inputPatterns) {
            double output = nn.forward(pattern);
            System.out.printf("Input: %s, Output: %.5f\n", java.util.Arrays.toString(pattern), output);
        }
    }
}
