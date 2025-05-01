import java.util.Random;

public class SimulatedAnnealing {

    // Функция, которую нужно минимизировать
    public static double objectiveFunction(double x) {
        return x  x; // Исправлено: добавлено умножение
    }

    // Функция имитированного отжига
    public static double[] simulatedAnnealing(double initialTemp, double coolingRate, int maxIterations) {
        // Начальное случайное решение
        Random random = new Random();
        double currentSolution = random.nextDouble()  20 - 10; // Случайное число от -10 до 10. Исправлено: добавлено умножение
        double currentEnergy = objectiveFunction(currentSolution);
        double bestSolution = currentSolution; // Лучшее решение на данный момент
        double bestEnergy = currentEnergy;     // Энергия лучшего решения на данный момент

        double temperature = initialTemp; // Начальная температура

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            // Генерация нового решения путем небольшого изменения текущего
            double newSolution = currentSolution + random.nextDouble()  2 - 1; // Случайное изменение от -1 до 1. Исправлено: добавлено умножение
            double newEnergy = objectiveFunction(newSolution); // Энергия нового решения

            // Разница в энергии между новым и текущим решениями
            double energyDiff = newEnergy - currentEnergy;

            // Принимаем ли новое решение?
            // Если разница энергии отрицательная (лучше), или с некоторой вероятностью, зависящей от температуры
            if (energyDiff < 0 || random.nextDouble() < Math.exp(-energyDiff / temperature)) {
                currentSolution = newSolution; // Принимаем новое решение
                currentEnergy = newEnergy;     // Обновляем текущую энергию
            }

            // Обновляем лучшее решение, если найдено лучшее
            if (currentEnergy < bestEnergy) {
                bestSolution = currentSolution; // Обновляем лучшее решение
                bestEnergy = currentEnergy;     // Обновляем энергию лучшего решения
            }

            // Понижаем температуру
            temperature = coolingRate; // Исправлено: умножение, а не присваивание
        }

        return new double[]{bestSolution, bestEnergy}; // Возвращаем лучшее решение и его энергию
    }

    public static void main(String[] args) {
        // Параметры алгоритма
        double initialTemp = 1000;       // Начальная температура
        double coolingRate = 0.95;      // Скорость охлаждения
        int maxIterations = 1000;       // Максимальное число итераций

        // Запуск алгоритма имитированного отжига
        double[] result = simulatedAnnealing(initialTemp, coolingRate, maxIterations);
        System.out.printf("Лучшее решение: %.4f, Энергия лучшего решения: %.4f%n", result[0], result[1]);
    }
}