import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

// Основной класс для моделирования экосистемы
class Ecosystem {
    ArrayList<Plant> plants;
    ArrayList<Herbivore> herbivores;
    ArrayList<Predator> predators;

    public Ecosystem() {
        plants = new ArrayList<>();
        herbivores = new ArrayList<>();
        predators = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < 20; i++) plants.add(new Plant());
        for (int i = 0; i < 10; i++) herbivores.add(new Herbivore());
        for (int i = 0; i < 5; i++) predators.add(new Predator());
    }

    public void update() {
        for (Plant plant : plants) plant.grow();
        for (Herbivore herbivore : herbivores) herbivore.act(plants);
        for (Predator predator : predators) predator.act(herbivores);
    }

    public ArrayList<Plant> getPlants() { return plants; }
    public ArrayList<Herbivore> getHerbivores() { return herbivores; }
    public ArrayList<Predator> getPredators() { return predators; }
}

// Класс растения
class Plant {
    int health;

    public Plant() {
        this.health = 10; // Начальное здоровье
    }

    public void grow() {
        health++;
    }

    public int getHealth() { return health; }
}

// Класс травоядного
class Herbivore {
    int energy;
    SimplePerceptron controller;

    public Herbivore() {
        this.energy = 20; // Начальная энергия
        this.controller = new SimplePerceptron(2); // 2 входа: энергия, количество растений
    }

    public void act(ArrayList<Plant> plants) {
        if (energy <= 0) return;

        // Собираем данные для контроля
        double[] inputs = new double[]{energy, plants.size()};
        int action = controller.predict(inputs); // 0: двигаться, 1: есть, 2: размножаться

        if (action == 1 && !plants.isEmpty()) { // Если "есть" и есть растения
            Plant plant = plants.remove(0);
            energy += plant.getHealth(); // Потребление здоровья растения
        } else if (action == 0) { // Если "двигаться"
            energy--; // Трата энергии на движение
        }

        if (energy < 0) energy = 0; // Не позволяем отрицательной энергии
    }

    public int getEnergy() { return energy; }
}

// Класс хищника
class Predator {
    int energy;
    SimplePerceptron controller;

    public Predator() {
        this.energy = 30; // Начальная энергия
        this.controller = new SimplePerceptron(2); // 2 входа: энергия, количество травоядных
    }

    public void act(ArrayList<Herbivore> herbivores) {
        if (energy <= 0) return;

        double[] inputs = new double[]{energy, herbivores.size()};
        int action = controller.predict(inputs); // 0: двигаться, 1: есть

        if (action == 1 && !herbivores.isEmpty()) {
            Herbivore herbivore = herbivores.remove(0);
            energy += herbivore.getEnergy(); // Потребление энергии травоядного
        } else if (action == 0) {
            energy--; // Трата энергии на движение
        }

        if (energy < 0) energy = 0; // Не позволяйте отрицательной энергии
    }

    public int getEnergy() { return energy; }
}

// Однослойный перцептрон
class SimplePerceptron {
    double[] weights;

    public SimplePerceptron(int inputSize) {
        weights = new double[inputSize];
        Random rand = new Random();
        for (int i = 0; i < inputSize; i++) {
            weights[i] = rand.nextDouble(); // Инициализация случайными значениями
        }
    }

    public int predict(double[] inputs) {
        double sum = 0;
        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i];
        }
        return sum >= 0.5 ? 1 : 0; // Простая активация
    }
}

// Графический интерфейс на Java Swing
public class EcoSystemSimulation extends JFrame {
    Ecosystem ecosystem;
    JTextArea textArea;

    public EcoSystemSimulation() {
        ecosystem = new Ecosystem();
        setTitle("EcoSystem Simulation");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ecosystem.update();
                displayStatus();
            }
        });
        add(updateButton, BorderLayout.SOUTH);
    }

    public void displayStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plants: ").append(ecosystem.getPlants().size()).append("\n");
        sb.append("Herbivores: ").append(ecosystem.getHerbivores().size()).append("\n");
        sb.append("Predators: ").append(ecosystem.getPredators().size()).append("\n");
        sb.append("\n");
        for (int i = 0; i < ecosystem.getHerbivores().size(); i++) {
            sb.append("Herbivore ").append(i + 1).append(" Energy: ")
              .append(ecosystem.getHerbivores().get(i).getEnergy()).append("\n");
        }
        for (int i = 0; i < ecosystem.getPredators().size(); i++) {
            sb.append("Predator ").append(i + 1).append(" Energy: ")
              .append(ecosystem.getPredators().get(i).getEnergy()).append("\n");
        }
        textArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EcoSystemSimulation sim = new EcoSystemSimulation();
            sim.setVisible(true);
        });
    }
}
