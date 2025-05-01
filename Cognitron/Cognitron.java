import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.RecordReaderDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Cognitron {

    public static void main(String[] args) {
        // Параметры сети
        int numClasses = 10; // Количество классов (например, цифры от 0 до 9)
        int inputSize = 784; // Размер входного вектора (28x28 пикселей)
        int hiddenLayerSize = 100;
        int batchSize = 128;
        int epochs = 10; // Увеличьте для лучшего обучения

        // Генерация фиктивных данных (замените на ваши данные)
        List<double[]> features = generateRandomFeatures(1000, inputSize);
        int[] labels = generateRandomLabels(1000, numClasses);

        // Преобразование данных в INDArray
        INDArray featuresArray = Nd4j.create(features.toArray(new double[0][]));
        INDArray labelsArray = Nd4j.create(labels);
        labelsArray = labelsArray.reshape(labelsArray.length(),1);


        // Создание итератора для данных
        RecordReaderDataSetIterator iterator = new RecordReaderDataSetIterator(new SimpleRecordReader(featuresArray, labelsArray), batchSize,1,numClasses);

        // Нормализация данных - важно для улучшения обучения
        NormalizerStandardize normalizer = new NormalizerStandardize();
        normalizer.fit(iterator);
        iterator.setPreProcessor(normalizer);


        // Конфигурация сети
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(123)
                .weightInit(WeightInit.XAVIER)
                .updater(org.deeplearning4j.nn.conf.Updater.ADAM)
                .list()
                .layer(new DenseLayer.Builder().nIn(inputSize).nOut(hiddenLayerSize)
                        .activation(Activation.RELU).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(hiddenLayerSize).nOut(numClasses)
                        .activation(Activation.SOFTMAX).build())
                .build();

        // Создание сети
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(10));

        // Обучение сети
        for(int i=0; i<epochs; i++){
            System.out.println("Epoch " + (i+1));
            model.fit(iterator);
            iterator.reset(); 
        }


        // Проверка 
        double[] testFeature = features.get(0);
        INDArray testFeatureArray = Nd4j.create(testFeature);
        // Нормализуем тестовый пример
        normalizer.transform(testFeatureArray);
        INDArray prediction = model.output(testFeatureArray);
        int predictedLabel = Nd4j.argMax(prediction, 1).getInt(0, 0);
        System.out.println("Predicted label: " + predictedLabel);
    }


    // Метод для генерации фиктивных данных 
    private static List<double[]> generateRandomFeatures(int numExamples, int inputSize){
        Random random = new Random();
        double[][] features = new double[numExamples][inputSize];
        for(int i = 0; i<numExamples; i++){
            for(int j=0; j<inputSize; j++){
                features[i][j] = random.nextDouble();
            }
        }
        return