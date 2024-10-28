import java.util.Arrays;
import java.util.Random;

public class SimplePerceptron {
    private double[][] weights;     // Веса для 10 выходов (цифры 0-9)
    private double learningRate;

    public SimplePerceptron(int inputCount, int outputCount, double learningRate) {
        this.learningRate = learningRate;
        weights = new double[outputCount][inputCount];
        Random rand = new Random();

        // Инициализация весов случайными значениями
        for (int i = 0; i < outputCount; i++) {
            for (int j = 0; j < inputCount; j++) {
                weights[i][j] = rand.nextDouble(); // из диапазона [0.0, 1.0]
            }
        }
    }

    public int predict(double[] inputs) {
        double[] outputs = new double[weights.length];

        // Вычисляем линейную комбинацию входов и весов
        for (int i = 0; i < weights.length; i++) {
            outputs[i] = 0;
            for (int j = 0; j < inputs.length; j++) {
                outputs[i] += inputs[j] * weights[i][j];
            }
        }

        // Возвращаем индекс выхода с максимальным значением (классификация)
        return maxIndex(outputs);
    }

    public void train(double[][] inputs, int[] labels, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                int label = labels[i];
                double[] predicted = new double[weights.length];
                
                for (int j = 0; j < weights.length; j++) {
                    predicted[j] = 0;
                    for (int k = 0; k < inputs[i].length; k++) {
                        predicted[j] += inputs[i][k] * weights[j][k];
                    }
                }

                // Обновление весов
                for (int j = 0; j < weights.length; j++) {
                    double error = (j == label ? 1 : 0) - sigmoid(predicted[j]);
                    for (int k = 0; k < inputs[i].length; k++) {
                        weights[j][k] += learningRate * error * inputs[i][k];
                    }
                }
            }
        }
    }

    private int maxIndex(double[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public static void main(String[] args) {
        SimplePerceptron perceptron = new SimplePerceptron(100, 10, 0.1);

        //Генерация данных
        double[][] trainingData = new double[1000][100]; //1000 изображений по 100 пикселей
        int[] labels = new int[1000]; // 1000 меток (0-9)

        Random rand = new Random();
        for (int i = 0; i < trainingData.length; i++) {
            for (int j = 0; j < trainingData[i].length; j++) {
                trainingData[i][j] = rand.nextDouble(); // случайные значения (пиксели)
            }
            labels[i] = rand.nextInt(10); // случайная метка от 0 до 9
        }

        // Обучение перцептрона
        perceptron.train(trainingData, labels, 100); 

        // Тестирование
        double[] testInput = new double[100]; 
        for (int i = 0; i < testInput.length; i++) {
            testInput[i] = rand.nextDouble(); 
        }

        int predictedLabel = perceptron.predict(testInput);
        System.out.println("Predicted Label: " + predictedLabel);
    }
}
