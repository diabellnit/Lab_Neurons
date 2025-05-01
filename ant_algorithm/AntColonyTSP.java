//Реализовать алгоритм муравья для решения задачи коммивояжера на java

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AntColonyTSP {
    private static final int NUM_ANTS = 10;
    private static final int NUM_CITIES = 5;
    private static final double ALPHA = 1.0; // pheromone importance
    private static final double BETA = 2.0; // distance priority
    private static final double EVAPORATION_RATE = 0.5;
    private static final double INITIAL_PHEROMONE = 1.0;

    private double[][] distances;
    private double[][] pheromones;

    public AntColonyTSP(double[][] distances) {
        this.distances = distances;
        this.pheromones = new double[NUM_CITIES][NUM_CITIES];
        for (int i = 0; i < NUM_CITIES; i++) {
            for (int j = 0; j < NUM_CITIES; j++) {
                pheromones[i][j] = INITIAL_PHEROMONE;
            }
        }
    }

    public void solve() {
        for (int iteration = 0; iteration < 100; iteration++) {
            List<List<Integer>> allPaths = new ArrayList<>();
            for (int ant = 0; ant < NUM_ANTS; ant++) {
                List<Integer> path = constructPath();
                allPaths.add(path);
                updatePheromones(path);
            }
            evaporatePheromones();
        }
        // Output the best path found
        System.out.println("Best path found: " + bestPath(allPaths));
    }

    private List<Integer> constructPath() {
        List<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[NUM_CITIES];
        int currentCity = new Random().nextInt(NUM_CITIES);
        path.add(currentCity);
        visited[currentCity] = true;

        for (int i = 1; i < NUM_CITIES; i++) {
            currentCity = selectNextCity(currentCity, visited);
            path.add(currentCity);
            visited[currentCity] = true;
        }
        return path;