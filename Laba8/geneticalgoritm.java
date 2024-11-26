import java.util.*;

public class GeneticAlgorithm {
    static final int POPULATION_SIZE = 100;
    static final int GENERATIONS = 100;
    static final double MUTATION_RATE = 0.05;

    static class Individual {
        double x, y;
        double fitness;

        Individual(double x, double y) {
            this.x = x;
            this.y = y;
            this.fitness = evaluateFitness();
        }

        double evaluateFitness() {
            return 1.0 / (1 + x * x + y * y);
        }
    }

    public static void main(String[] args) {
        runGeneticAlgorithm("elite");
        runGeneticAlgorithm("roulette");
    }

    static void runGeneticAlgorithm(String selectionMethod) {
        List<Individual> population = initializePopulation();
        
        for (int generation = 0; generation < GENERATIONS; generation++) {
            Collections.sort(population, Comparator.comparingDouble(ind -> -ind.fitness));
            List<Individual> newPopulation = new ArrayList<>();

            // Отбор
            if (selectionMethod.equals("elite")) {
                // Сохраняем лучших 10%
                int eliteCount = POPULATION_SIZE / 10;
                for (int i = 0; i < eliteCount; i++) {
                    newPopulation.add(population.get(i));
                }
            } else if (selectionMethod.equals("roulette")) {
                // Метод рулетки
                double totalFitness = population.stream().mapToDouble(ind -> ind.fitness).sum();
                for (int i = 0; i < POPULATION_SIZE; i++) {
                    double randomValue = Math.random() * totalFitness;
                    double cumulativeFitness = 0;
                    for (Individual ind : population) {
                        cumulativeFitness += ind.fitness;
                        if (cumulativeFitness >= randomValue) {
                            newPopulation.add(ind);
                            break;
                        }
                    }
                }
            }

            // Скрещивание и запрос на создание новых особей
            while (newPopulation.size() < POPULATION_SIZE) {
                Individual parent1 = newPopulation.get((int) (Math.random() * newPopulation.size()));
                Individual parent2 = newPopulation.get((int) (Math.random() * newPopulation.size()));
                newPopulation.add(crossover(parent1, parent2));
            }

            // Мутация
            mutatePopulation(newPopulation);
            population = newPopulation;
        }

        // Вывод результата
        Individual bestIndividual = population.get(0);
        System.out.printf("Selection Method: %s, Best X: %.4f, Best Y: %.4f, Fitness: %.4f\n", selectionMethod, bestIndividual.x, bestIndividual.y, bestIndividual.fitness);
    }

    static List<Individual> initializePopulation() {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new Individual(Math.random() * 10 - 5, Math.random() * 10 - 5));
        }
        return population;
    }

    static Individual crossover(Individual parent1, Individual parent2) {
        double childX = (parent1.x + parent2.x) / 2;
        double childY = (parent1.y + parent2.y) / 2;
        return new Individual(childX, childY);
    }

    static void mutatePopulation(List<Individual> population) {
        for (Individual ind : population) {
            if (Math.random() < MUTATION_RATE) {
                ind.x += (Math.random() * 2 - 1); // Мутация x
                ind.y += (Math.random() * 2 - 1); // Мутация y
                ind.fitness = ind.evaluateFitness(); // Переоценка
            }
        }
    }
}
