import java.util.*;

class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private int numGenerations;
    private Random random;

    public GeneticAlgorithm(int populationSize, double mutationRate, int numGenerations) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.numGenerations = numGenerations;
        this.random = new Random();
    }

    public SingleLayerPerceptron evolve(double[][] inputs, double[] outputs, int inputSize) {
        List<SingleLayerPerceptron> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new SingleLayerPerceptron(inputSize, 0.1));
        }

        for (int generation = 0; generation < numGenerations; generation++) {
            population.sort(Comparator.comparingDouble(perceptron -> evaluate(perceptron, inputs, outputs)));
            List<SingleLayerPerceptron> newPopulation = new ArrayList<>();

            // Селекция и кроссовер
            for (int i = 0; i < populationSize; i++) {
                SingleLayerPerceptron parent1 = population.get(random.nextInt(populationSize / 2)); // Берём лучших
                SingleLayerPerceptron parent2 = population.get(random.nextInt(populationSize / 2));
                SingleLayerPerceptron child = crossover(parent1, parent2);
                mutate(child);
                newPopulation.add(child);
            }

            population = newPopulation;
        }

        return population.get(0); // Возвращаем лучшего
    }

    private double evaluate(SingleLayerPerceptron perceptron, double[][] inputs, double[] outputs) {
        double errorSum = 0;
        for (int i = 0; i < inputs.length; i++) {
            double prediction = perceptron.predict(inputs[i]);
            double error = outputs[i] - prediction;
            errorSum += error * error; // Квадрат ошибки
        }
        return errorSum / inputs.length; // Средняя ошибка
    }

    private SingleLayerPerceptron crossover(SingleLayerPerceptron parent1, SingleLayerPerceptron parent2) {
        SingleLayerPerceptron child = new SingleLayerPerceptron(parent1.getWeights().length, 0.1);
        double[] childWeights = new double[child.getWeights().length];
        double[] p1Weights = parent1.getWeights();
        double[] p2Weights = parent2.getWeights();

        for (int i = 0; i < childWeights.length; i++) {
            childWeights[i] = random.nextBoolean() ? p1Weights[i] : p2Weights[i];
        }
        child.setWeights(childWeights);
        return child;
    }

    private void mutate(SingleLayerPerceptron perceptron) {
        double[] weights = perceptron.getWeights();
        for (int i = 0; i < weights.length; i++) {
            if (random.nextDouble() < mutationRate) {
                weights[i] += random.nextGaussian() * 0.1; // Небольшое случайное изменение
            }
        }
        perceptron.setWeights(weights);
    }
}
