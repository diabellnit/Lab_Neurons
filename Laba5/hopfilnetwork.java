import java.util.Arrays;

public class HopfieldNetwork {
    private final int size; // Количество нейронов
    private final double[][] weights; // Веса сети
    private final double[][] trainedPatterns; // Образцы для распознавания

    public HopfieldNetwork(int size, double[][] trainedPatterns) {
        this.size = size;
        this.trainedPatterns = trainedPatterns;
        this.weights = new double[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(weights[i], 0);
        }
    }

    // Обучение сети на входном паттерне
    public void train(double[] pattern) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    weights[i][j] += pattern[i] * pattern[j]; // Сумма по всем маскам
                }
            }
        }
    }

    // Восстановление изображения
    public double[] restore(double[] input) {
        double[] output = Arrays.copyOf(input, size);
        boolean changed;

        do {
            changed = false;
            for (int i = 0; i < size; i++) {
                double netInput = 0;
                for (int j = 0; j < size; j++) {
                    netInput += weights[i][j] * output[j];
                }
                double newValue = netInput > 0 ? 1 : -1; // Применяем активацию
                if (newValue != output[i]) {
                    output[i] = newValue;
                    changed = true; // Если изменилось, флаг
                }
            }
        } while (changed); // Повторяем, пока есть изменения

        return output;
    }

    // Распознавание образца
    public int recognize(double[] pattern) {
        double[] restored = restore(pattern);
        
        int bestMatchIndex = -1;
        double bestMatchScore = Double.NEGATIVE_INFINITY;

        // Сравниваем восстановленный образец с известными образцами
        for (int i = 0; i < trainedPatterns.length; i++) {
            double score = 0;
            for (int j = 0; j < size; j++) {
                // Сравниваем восстановленный и тренировочный паттерн
                if (restored[j] == trainedPatterns[i][j]) {
                    score++;
                }
            }
            if (score > bestMatchScore) {
                bestMatchScore = score;
                bestMatchIndex = i;
            }
        }

        return bestMatchIndex; // Возвращаем индекс лучшего совпадения
    }

    public static void main(String[] args) {
        int size = 100; // 10x10 изображения

        // Образцы для обучения (например, 0, 1, ...)
        double[][] samples = {
            // Пример цифры "0"
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 1,
             1, 0, 0, 0, 0, 1, 0, 0, 0, 1,
             1, 0, 0, 0, 0, 1, 0, 0, 0, 1,
             1, 0, 0, 0, 0, 1, 0, 0, 0, 1,
             1, 1, 1, 1, 1, 0, 1, 1, 1, 1,
             0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
             0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
             0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
             0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
             1, 1, 1, 1, 1, 0, 1, 1, 1, 1},

            // Пример цифры "1"
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
             1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
             1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
             1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
             1, 1, 1, -1, -1, -1, -1, -1, -1, -1,
             1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
             1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
             1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
             -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
             -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
            // Вы можете добавить другие цифры по аналогии...
        };

        // Создаем сеть Хопфилда
        HopfieldNetwork hopfield = new HopfieldNetwork(size, samples);

        // Обучение сети на образцах
        for (double[] sample : samples) {
            hopfield.train(sample);
        }

        // Ввод поврежденного изображения (например, изображение "0" с некоторыми испорченными значениями)
        double[] corrupted = {
            1, 1, 1, 1, 1, 0, 1, 1, 1, 1,
            1, 0, 0, 0, -1, 1, 0, -1, 0, 1,
            1, 0, -1, 0, 0, 1, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 0, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 0, 1, 1, 1, 1
        };

        // Распознаем восстановленный образ
        int recognizedDigit = hopfield.recognize(corrupted);

        // Вывод результатов
        System.out.println("Recognized Digit Index: " + recognizedDigit);
        for (double value : samples[recognizedDigit]) {
            System.out.print((value > 0 ? "1 " : "0 "));
        }
    }
}
