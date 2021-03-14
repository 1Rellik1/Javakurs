
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class VisualInteface extends Application {
    private TextField cash;
    private TextField asserts;
    private Label profit;
    private String company =null;
    private FlowPane flowPane;
    private HashMap<String, String> companies;
    private void GetInformation() throws IOException, InterruptedException {
        String companies_url = HTTP.Get("https://iss.moex.com/iss/engines/stock/markets/shares/boardgroups/57/" +
                "securities.jsonp?iss.meta=off&security_collection=3&sort_column=SHORTNAME&sort_order=asc&lang=ru" +
                "&_=1615584079650");
        companies = HTTP.JparseCompanies(companies_url);
    }
    public void thisStart() {
        Application.launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        GetInformation();
        stage=MakeStage();
        stage.show();
    }


    public Stage MakeStage() throws Exception {
        Stage stage = new Stage();
        stage.setScene(MakeScene());
        stage.setTitle("Проверка стратегий");
        stage.setWidth(650);
        stage.setHeight(550);
        stage.setResizable(true);
        stage.centerOnScreen();
        return (stage);
    }
    private Scene MakeScene(){
        MakeFlowPane();
        ScrollPane scrollPane = new ScrollPane(flowPane);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, Color.BLACK);
        return scene;
    }

    private ComboBox MakeComboBoxCompanies(){
        ObservableList<String> langs = FXCollections.observableArrayList(companies.keySet());
        ComboBox<String> comboBox = new ComboBox(langs);
        comboBox.setValue("Телеграф-п");
        comboBox.setVisibleRowCount(18);
        comboBox.setOnAction(event -> GetKey(comboBox));
        return comboBox;
    }
    private Label MakeProfitLabel(){
        profit= new Label("Profit");
        return profit;
    }
    private TextField MakeTextfield(String starting_text){
        TextField textField = new TextField();
        textField.setPrefColumnCount(11);
        textField.setText(starting_text);
        return textField;
    }
    private Button MakeButton(TextField cash, TextField assets){
        Button btn = new Button("Посчитать прибыль");
        btn.setPrefWidth(80);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String OurProfit;
                MovingAverageStrategy movingAverageStrategy=new MovingAverageStrategy(Double.valueOf(cash.getText()),
                        Double.valueOf(assets.getText()));
                try {
                    if (company!=null) {
                        List<Double>prices=(HTTP.JparseArray(HTTP.Get(HTTP.MakeUrl(companies.get(company)))));
                        List<Double>ma=movingAverageStrategy.getMa(HTTP.JparseArray(HTTP.
                                Get(HTTP.MakeUrl(companies.get(company)))));
                        OurProfit=String.valueOf((movingAverageStrategy.
                                signals(prices)-Double.valueOf(cash.getText())));
                        prices = prices.subList(prices.size() - ma.size(), prices.size());
                        flowPane.getChildren().setAll(MakeComboBoxCompanies(),cash,asserts,MakeButton(cash,asserts),
                                MakeProfitLabel(),MakeLineChart(prices,ma),MakeProfitLineChart
                                        (movingAverageStrategy.getPortfoliovalue()));
                        profit.setText(OurProfit);
                        movingAverageStrategy = null;
                        System.gc();
                    }
                    else profit.setText("Компания не выбрана, выберите компанию");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return btn;
    }
    private void MakeFlowPane(){
        cash=MakeTextfield("Cash");
        asserts =MakeTextfield("asserts");
        flowPane=new FlowPane(10, 10, MakeComboBoxCompanies(),cash,asserts,MakeButton(cash,asserts),
                MakeProfitLabel());
    }
    private LineChart MakeLineChart(List<Double>prices,List<Double>ma){
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        LineChart<Number, Number> numberLineChart = new LineChart<Number, Number>(x,y);
        numberLineChart.setTitle("Strategy");
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        series1.setName("Prices");
        series2.setName("Moving Average");

        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();
        for(int i=0; i<prices.size(); i++){
            datas.add(new XYChart.Data(i,prices.get(i)));
            datas2.add(new XYChart.Data(i,ma.get(i)));        }
        series1.setData(datas);
        series2.setData(datas2);
        numberLineChart.getData().add(series1);
        numberLineChart.getData().add(series2);
        numberLineChart.setCreateSymbols(false);
        return numberLineChart;
    }
    private LineChart MakeProfitLineChart(List<Double>profit){
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        LineChart<Number, Number> numberLineChart = new LineChart<Number, Number>(x,y);
        numberLineChart.setTitle("Profit");
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Profit");
        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        for(int i=0; i<profit.size(); i++) {
            datas.add(new XYChart.Data(i, profit.get(i)));
        }
        series1.setData(datas);
        numberLineChart.getData().add(series1);
        numberLineChart.setCreateSymbols(false);
        return numberLineChart;
    }

    private void GetKey(ComboBox<String> comboBox){
        this.company = comboBox.getValue();
    }
}
