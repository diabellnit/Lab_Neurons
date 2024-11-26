import java.util.Arrays;
import java.util.Random;

class Neuron {
    double[] weights;
    double output;
    double delta;

    public Neuron(int numInputs) {
        weights = new double[numInputs + 1];  // +1 для bias
        Random rand = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = rand.nextDouble() * 2 - 1; // Инициализация весов случайно
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

class MLP {
    private Neuron[] inputLayer;
    private Neuron[] hiddenLayer;
    private Neuron[] outputLayer;

    public MLP(int inputSize, int hiddenSize, int outputSize) {
        inputLayer = new Neuron[inputSize];
        hiddenLayer = new Neuron[hiddenSize];
        outputLayer = new Neuron[outputSize];

        for (int i = 0; i < hiddenSize; i++) {
            hiddenLayer[i] = new Neuron(inputSize);
        }

        for (int i = 0; i < outputSize; i++) {
            outputLayer[i] = new Neuron(hiddenSize);
        }
    }

    public double[] forward(double[] inputs) {
        double[] hiddenOutputs = new double[hiddenLayer.length];
        for (int i = 0; i < hiddenLayer.length; i++) {
            hiddenOutputs[i] = hiddenLayer[i].activate(inputs);
        }

        double[] outputs = new double[outputLayer.length];
        for (int i = 0; i < outputLayer.length; i++) {
            outputs[i] = outputLayer[i].activate(hiddenOutputs);
        }
        return outputs;
    }

    public void train(double[][] inputPatterns, double[][] targets, double learningRate, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputPatterns.length; i++) {
                double[] inputs = inputPatterns[i];
                double[] target = targets[i];

                // Прямое распространение
                double[] outputs = forward(inputs);

                // Обратное распространение
                for (int j = 0; j < outputLayer.length; j++) {
                    outputLayer[j].calculateDelta(target[j]);
                }

                for (int j = 0; j < hiddenLayer.length; j++) {
                    double sum = 0;
                    for (int k = 0; k < outputLayer.length; k++) {
                        sum += outputLayer[k].weights[j + 1] * outputLayer[k].delta;
                    }
                    hiddenLayer[j].delta = hiddenLayer[j].output * (1 - hiddenLayer[j].output) * sum;
                }

                // Обновление весов
                for (int j = 0; j < outputLayer.length; j++) {
                    outputLayer[j].updateWeights(new double[hiddenLayer.length], learningRate);
                }
                for (int j = 0; j < hiddenLayer.length; j++) {
                    hiddenLayer[j].updateWeights(inputs, learningRate);
                }
            }
        }
    }

    public int predict(double[] inputs) {
        double[] outputs = forward(inputs);
        return argmax(outputs);
    }

    private int argmax(double[] outputs) {
        int index = 0;
        double max = outputs[0];
        for (int i = 1; i < outputs.length; i++) {
            if (outputs[i] > max) {
                max = outputs[i];
                index = i;
            }
        }
        return index;
    }
}

class SingleLayerPerceptron {
    private final Neuron[] neurons;

    public SingleLayerPerceptron(int numInputs, int numOutputs) {
        neurons = new Neuron[numOutputs];
        for (int i = 0; i < numOutputs; i++) {
            neurons[i] = new Neuron(numInputs);
        }
    }

    public void train(double[][] inputPatterns, double[][] targets, double learningRate, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputPatterns.length; i++) {
                double[] inputs = inputPatterns[i];
                double[] target = targets[i];

                // Прямое распространение и обновление весов
                for (int j = 0; j < neurons.length; j++) {
                    double output = neurons[j].activate(inputs);
                    neurons[j].calculateDelta(target[j]);
                    neurons[j].updateWeights(inputs, learningRate);
                }
            }
        }
    }

    public int predict(double[] inputs) {
        double[] outputs = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            outputs[i] = neurons[i].activate(inputs);
        }
        return argmax(outputs);
    }

    private int argmax(double[] outputs) {
        int index = 0;
        double max = outputs[0];
        for (int i = 1; i < outputs.length; i++) {
            if (outputs[i] > max) {
                max = outputs[i];
                index = i;
            }
        }
        return index;
    }
}

public class DigitRecognition {
    public static void main(String[] args) {
        // Загрузка данных
        // Вместо этого вы можете использовать ваш способ загрузки данных. Здесь показан пример с рандомными данными.
        int numSamples = 1000; // Общее количество образцов
        int inputSize = 784; // 28x28 изображение
        int outputSize = 10; // Цифры от 0 до 9

        double[][] inputPatterns = new double[numSamples][inputSize];
        double[][] targets = new double[numSamples][outputSize];
        
        // Здесь должны быть ваши данные для обучения
        // Для простоты используются случайные данные
        Random rand = new Random();
        for (int i = 0; i < numSamples; i++) {
            for (int j = 0; j < inputSize; j++) {
                inputPatterns[i][j] = rand.nextDouble(); // Заполнение случайными числовыми значениями
            }
            targets[i][rand.nextInt(outputSize)] = 1.0; // Случайный целевой класс
        }

        // Обучение многослойного перцептрона
        MLP mlp = new MLP(inputSize, 128, outputSize); // 128 скрытых нейронов
        mlp.train(inputPatterns, targets, 0.01, 50); // Обучение MLP

        // Обучение однослойного перцептрона
        SingleLayerPerceptron slp = new SingleLayerPerceptron(inputSize, outputSize);
        slp.train(inputPatterns, targets, 0.01, 50); // Обучение SLP

        // Оценка точности (на реальных данных замените на тестовые)
        int correctMLP = 0;
        int correctSLP = 0;

        for (int i = 0; i < numSamples; i++) {
            int predictionMLP = mlp.predict(inputPatterns[i]);
            int predictionSLP = slp.predict(inputPatterns[i]);

            if (Arrays.stream(targets[i]).max().getAsDouble() == targets[i][predictionMLP]) {
                correctMLP++;
            }
            if (Arrays.stream(targets[i]).max().getAsDouble() == targets[i][predictionSLP]) {
                correctSLP++;
            }
        }

        System.out.printf("MLP Accuracy: %.2f%%\n", (correctMLP / (double) numSamples) * 100);
        System.out.printf("Single Layer Perceptron Accuracy: %.2f%%\n", (correctSLP / (double) numSamples) * 100);
    }
}

