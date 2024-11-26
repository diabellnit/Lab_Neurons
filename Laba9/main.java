public class Main {
    public static void main(String[] args) {
        double[][] trainingInputs = {
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1}
        };

        double[] trainingOutputs = {0, 0, 0, 1}; // Пример: AND функция

        // Обучение однослойного перцептрона
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.01, 1000);
        SingleLayerPerceptron bestPerceptron = ga.evolve(trainingInputs, trainingOutputs, 2);

        System.out.println("Best Single Layer Perceptron Weights:");
        System.out.println(Arrays.toString(bestPerceptron.getWeights()));

        // Обучение многослойного перцептрона
        double[][] trainingOutputsMLP = {
            {0},
            {0},
            {0},
            {1}
        };
        int[] layers = {2, 2, 1};

        GeneticAlgorithmMLP gaMLP = new GeneticAlgorithmMLP(100, 0.01, 1000);
        MultiLayerPerceptron bestMLP = gaMLP.evolve(trainingInputs, trainingOutputsMLP, layers);

        System.out.println("Best Multi Layer Perceptron Weights:");
        for (double[][] layer : bestMLP.getWeights()) {
            System.out.println(Arrays.deepToString(layer));
        }
    }
}
