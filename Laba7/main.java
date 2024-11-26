import java.util.Arrays;
import java.util.Random;

class Neuron {
    double[][] weights; // Веса нейронов
    int inputSize; // Размерность входа

    public Neuron(int inputSize) {
        this.inputSize = inputSize;
        this.weights = new double[3][inputSize]; // Три класса
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] = rand.nextDouble(); // Инициализация весов случайными значениями
            }
        }
    }

    public int classify(double[] input) {
        double[] outputs = new double[3];
        for (int i = 0; i < 3; i++) {
            outputs[i] = 0;
            for (int j = 0; j < input.length; j++) {
                outputs[i] += weights[i][j] * input[j];
            }
        }
        return argMax(outputs);
    }

    private int argMax(double[] outputs) {
        int maxIndex = 0;
        for (int i = 1; i < outputs.length; i++) {
            if (outputs[i] > outputs[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex; // Возвращаем индекс класса с максимальным значением
    }

    public void train(double[] input, int targetClass, double learningRate) {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < input.length; j++) {
                // Обновление весов
                if (i == targetClass) {
                    weights[i][j] += learningRate * input[j]; // Увеличиваем веса для правильного класса
                } else {
                    weights[i][j] -= learningRate * input[j] * 0.1; // Уменьшаем веса для остальных классов
                }
            }
        }
    }
}

class Cognitron {
    private Neuron[] neurons; // Массив нейронов

    public Cognitron(int inputSize) {
        neurons = new Neuron[3]; // 3 класса
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new Neuron(inputSize); // Создаем нейрон для каждого класса
        }
    }

    public void train(double[][] inputs, int[] targets, double learningRate, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                neurons[targets[i]].train(inputs[i], targets[i], learningRate);
            }
        }
    }

    public int classify(double[] input) {
        return neurons[0].classify(input); // Классифицируем входные данные
    }
}

public class Main {
    public static void main(String[] args) {
        // Генерация образцов данных (произвольные входные данные)
        double[][] inputs = {
            {1, 0, 0},  // Класс 0
            {0, 1, 0},  // Класс 1
            {0, 0, 1},  // Класс 2
            {1, 1, 0},  // Класс 0
            {0, 1, 1},  // Класс 1
            {1, 0, 1}   // Класс 2
        };
        int[] targets = {0, 1, 2, 0, 1, 2}; // Целевые классы для обучения

        Cognitron cogniton = new Cognitron(3); // Создаем когнитрон
        cogniton.train(inputs, targets, 0.1, 100); // Обучаем когнитрон

        // Тестирование
        double[] testInput = {1, 0, 1}; // Пример входа для классификации
        int predictedClass = cogniton.classify(testInput);
        System.out.println("Predicted class: " + predictedClass);
    }
}
