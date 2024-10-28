public class Perceptron {
    private double weight;
    private double bias;
    private double learningRate;

    public Perceptron(double learningRate) {
        this.learningRate = learningRate;
        this.weight = 0.0; 
        this.bias = 0.0;   // Инициализация смещения
    }

    public int predict(int input) {
        double activation = input * weight + bias;
        return activation >= 0 ? 1 : 0; // Возвращает 1, если >= 0, иначе 0
    }

    public void train(int[] inputs, int[] labels) {
        for (int i = 0; i < inputs.length; i++) {
            int prediction = predict(inputs[i]);
            double error = labels[i] - prediction;

            weight += learningRate * error * inputs[i]; 
            bias += learningRate * error; // Обновление смещения
        }
    }

    public static void main(String[] args) {
        // Данные для логической функции "И"
        Perceptron andPerceptron = new Perceptron(0.1);
        int[] andInputs = {0, 0, 1, 1};
        int[] andLabels = {0, 0, 0, 1};
        for (int i = 0; i < 10; i++) { // Обучаем 10 эпох
            andPerceptron.train(andInputs, andLabels);
        }

        System.out.println("Logical AND:");
        System.out.println("0 AND 0 = " + andPerceptron.predict(0));
        System.out.println("0 AND 1 = " + andPerceptron.predict(1));
        System.out.println("1 AND 0 = " + andPerceptron.predict(1));
        System.out.println("1 AND 1 = " + andPerceptron.predict(1));

        //Данные для логической функции "ИЛИ"
        Perceptron orPerceptron = new Perceptron(0.1);
        int[] orInputs = {0, 0, 1, 1};
        int[] orLabels = {0, 1, 1, 1};
        for (int i = 0; i < 10; i++) {
            orPerceptron.train(orInputs, orLabels);
        }

        System.out.println("Logical OR:");
        System.out.println("0 OR 0 = " + orPerceptron.predict(0));
        System.out.println("0 OR 1 = " + orPerceptron.predict(1));
        System.out.println("1 OR 0 = " + orPerceptron.predict(1));
        System.out.println("1 OR 1 = " + orPerceptron.predict(1));

        // Пример для логической функции "НЕ"
        Perceptron notPerceptron = new Perceptron(0.1);
        int[] notInputs = {0, 1};
        int[] notLabels = {1, 0}; //0 - это не 1, а 1 - это не 0
        for (int i = 0; i < 10; i++) {
            notPerceptron.train(notInputs, notLabels); 
        }

        System.out.println("Logical NOT:");
        System.out.println("NOT 0 = " + notPerceptron.predict(0));
        System.out.println("NOT 1 = " + notPerceptron.predict(1));
    }
}
