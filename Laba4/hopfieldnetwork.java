import java.util.Arrays;

public class HopfieldNetwork {
    private final int size; // Количество нейронов
    private final double[][] weights; // Веса сети

    public HopfieldNetwork(int size) {
        this.size = size;
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

    public static void main(String[] args) {
        int size = 100; // 10x10 изображения
        HopfieldNetwork hopfield = new HopfieldNetwork(size);

        // Примеры образцов цифр (например, 0, 1, 2 ...)
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
            // Добавьте другие цифры по аналогии...
        };

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

        // Восстановление изображения
        double[] restored = hopfield.restore(corrupted);

        // Вывод восстановленного изображения
        System.out.println("Restored Image:");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print((restored[i * 10 + j] > 0 ? "1 " : "0 "));
            }
            System.out.println();
        }
    }
}
