package third;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Graph extends Application {

    private static List<Integer> testX = new ArrayList<>();
    private static List<Double> testY = new ArrayList<>();
    private static List<Integer> trainX = new ArrayList<>();
    private static List<Double> trainY = new ArrayList<>();

    @Override public void start(Stage stageTest) {
        stageTest.setTitle("Test result");
        //defining the axes
        final NumberAxis xAxisTest = new NumberAxis();
        final NumberAxis yAxisTest = new NumberAxis();
        xAxisTest.setLabel("Number of features");
        //creating the chart
        final LineChart<Number,Number> lineChartTest =
                new LineChart<Number,Number>(xAxisTest,yAxisTest);

        lineChartTest.setTitle("Test result");
        //defining a series
        XYChart.Series seriesTest = new XYChart.Series();
        seriesTest.setName("Test result");
        //populating the series with data
        for (int i = 0; i < testX.size(); ++i) {
            seriesTest.getData().add(new XYChart.Data(testX.get(i), testY.get(i)));
        }

        Scene sceneTest  = new Scene(lineChartTest,800,600);
        lineChartTest.getData().add(seriesTest);
        lineChartTest.setCreateSymbols(false);

        stageTest.setScene(sceneTest);
        stageTest.show();

        Stage stageTrain = new Stage();
        stageTrain.setTitle("Train result");

        //Set position of second window, related to primary window.
        stageTrain.setX(stageTest.getX() + 250);
        stageTrain.setY(stageTest.getY() + 100);

        final NumberAxis xAxisTrain = new NumberAxis();
        final NumberAxis yAxisTrain = new NumberAxis();
        xAxisTrain.setLabel("Number of features");
        //creating the chart
        final LineChart<Number,Number> lineChartTrain =
                new LineChart<Number,Number>(xAxisTrain,yAxisTrain);

        lineChartTrain.setTitle("Train result");
        lineChartTrain.setCreateSymbols(false);
        //defining a series
        XYChart.Series seriesTrain = new XYChart.Series();
        seriesTrain.setName("Train result");
        //populating the series with data
        for (int i = 0; i < trainX.size(); ++i) {
            seriesTrain.getData().add(new XYChart.Data(trainX.get(i), trainY.get(i)));
        }

        Scene sceneTrain  = new Scene(lineChartTrain,800,600);
        lineChartTrain.getData().add(seriesTrain);

        stageTrain.setScene(sceneTrain);
        stageTrain.show();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader brTest = new BufferedReader(new FileReader("test.txt"));
        String s;
        while ((s = brTest.readLine()) != null) {
            String[] line = s.split(" ");
            testX.add(Integer.parseInt(line[0]));
            testY.add(Double.parseDouble(line[1]));
        }
        BufferedReader brTrain = new BufferedReader(new FileReader("train.txt"));
        while ((s = brTrain.readLine()) != null) {
            String[] line = s.split(" ");
            trainX.add(Integer.parseInt(line[0]));
            trainY.add(Double.parseDouble(line[1]));
        }
        launch(args);
    }
}