class GeneticAlgorithmMLP {
    private int populationSize;
    private double mutationRate;
    private int numGenerations;
    private Random random;

    public GeneticAlgorithmMLP(int populationSize, double mutationRate, int numGenerations) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.numGenerations = numGenerations;
        this.random = new Random();
    }

    public MultiLayerPerceptron evolve(double[][] inputs, double[][] outputs, int[] layerSizes) {
        List<MultiLayerPerceptron> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new MultiLayerPerceptron(layerSizes, 0.1));
        }

        for (int generation = 0; generation < numGenerations; generation++) {
            population.sort(Comparator.comparingDouble(mlp -> evaluate(mlp, inputs, outputs)));
            List<MultiLayerPerceptron> newPopulation = new ArrayList<>();

            for (int i = 0; i < populationSize; i++) {
                MultiLayerPerceptron parent1 = population.get(random.nextInt(populationSize / 2));
                MultiLayerPerceptron parent2 = population.get(random.nextInt(populationSize / 2));
                MultiLayerPerceptron child = crossover(parent1, parent2);
                mutate(child);
                newPopulation.add(child);
            }

            population = newPopulation;
        }

        return population.get(0); // Возвращаем лучшего
    }

    private double evaluate(MultiLayerPerceptron mlp, double[][] inputs, double[][] outputs) {
        double errorSum = 0;
        for (int i = 0; i < inputs.length; i++) {
            double[] prediction = mlp.predict(inputs[i]);
            for (int j = 0; j < outputs[i].length; j++) {
                double error = outputs[i][j] - prediction[j];
                errorSum += error * error; // Квадрат ошибки
            }
        }
        return errorSum / inputs.length; // Средняя ошибка
    }

    private MultiLayerPerceptron crossover(MultiLayerPerceptron parent1, MultiLayerPerceptron parent2) {
        MultiLayerPerceptron child = new MultiLayerPerceptron(parent1.getWeights().length, 0.1);
        double[][][] childWeights = child.getWeights();

        for (int layer = 0; layer < childWeights.length; layer++) {
            for (int i = 0; i < childWeights[layer].length; i++) {
                for (int j = 0; j < childWeights[layer][i].length; j++) {
                    childWeights[layer][i][j] = random.nextBoolean() ? parent1.getWeights()[layer][i][j] : parent2.getWeights()[layer][i][j];
                }
            }
        }
        child.setWeights(childWeights);
        return child;
    }

    private void mutate(MultiLayerPerceptron mlp) {
        double[][][] weights = mlp.getWeights();
        for (int layer = 0; layer < weights.length; layer++) {
            for (int i = 0; i < weights[layer].length; i++) {
                for (int j = 0; j < weights[layer][i].length; j++) {
                    if (random.nextDouble() < mutationRate) {
                        weights[layer][i][j] += random.nextGaussian() * 0.1;
                    }
                }
            }
        }
        mlp.setWeights(weights);
    }
}
