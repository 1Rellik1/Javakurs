
import Indicators.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class VisualInteface extends Application {
    private TextField cash;
    private TextField asserts;
    private Label profit;
    private String company =null;
    private FlowPane flowPaneTop;
    private HashMap<String, String> companies;
    private FlowPane flowPaneCenter;
    private FlowPane flowPaneBottom;
    private Group checkboxes;


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
        MakeFlowPaneTop();
        MakeFlowPaneCenter();
        MakeFlowPaneBottom();
        ScrollPane scrollPane = new ScrollPane(flowPaneCenter);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(MakeborderPane(flowPaneTop,scrollPane,flowPaneBottom), Color.BLACK);
        return scene;
    }
    private Group MakeCheckBoxes(){
        checkboxes = new Group();
        CheckBox ma = new CheckBox("Moving Average");
        CheckBox macd = new CheckBox("MACD");
        macd.setLayoutX(ma.getLayoutX()+115);
        CheckBox ao = new CheckBox("Awesome Oscillator");
        ao.setLayoutX(macd.getLayoutX()+60);
        CheckBox mf = new CheckBox("Money flow index");
        mf.setLayoutX(ao.getLayoutX()+130);
        CheckBox rs = new CheckBox("Relative strength Index");
        rs.setLayoutX(mf.getLayoutX()+120);
        checkboxes.getChildren().addAll(ma,macd,ao,mf,rs);
        return checkboxes;
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
        profit= new Label();
        return profit;
    }
    private TextField MakeTextfield(String starting_text){
        TextField textField = new TextField();
        textField.setPrefColumnCount(11);
        textField.setPromptText(starting_text);
        return textField;
    }
    private Button MakeClearButton(){
        Button btn = new Button("Очистить все экраны");
        btn.setPrefWidth(150);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cash.setText("");
                asserts.setText("");
                flowPaneTop.getChildren().setAll(MakeComboBoxCompanies(),cash,asserts,MakeCheckBoxes(),
                        MakeButton(cash,asserts),MakeClearButton());
                profit.setText("");
                flowPaneCenter.getChildren().setAll();
            }
        });
        return btn;
    }
    private Button MakeButton(TextField cash, TextField assets){
        Button btn = new Button("Посчитать прибыль");
        btn.setPrefWidth(150);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String OurProfit;
                try {
                    if (company!=null) {
                        if (profit.getText().length()!=0) {
                            if ((profit.getText().substring(0, 1).equals("К")) | (profit.getText().substring(0, 1).equals("О"))) {
                                profit.setText("");
                            }
                        }
                        List<Double> prices = (HTTP.JparseArray(HTTP.Get(HTTP.MakeUrl(companies.get(company)))));
                        if (((CheckBox) checkboxes.getChildren().get(0)).isSelected()) {
                            MovingAverageStrategy movingAverageStrategy=new MovingAverageStrategy(Double.parseDouble(cash.getText()),
                                    Double.parseDouble(assets.getText()));
                            prices = (HTTP.JparseArray(HTTP.Get(HTTP.MakeUrl(companies.get(company)))));
                            List<Double> ma = movingAverageStrategy.getMa(HTTP.JparseArray(HTTP.
                                    Get(HTTP.MakeUrl(companies.get(company)))));
                            OurProfit = String.valueOf((movingAverageStrategy.
                                    signals(prices) - Double.valueOf(cash.getText())));
                            prices = prices.subList(prices.size() - ma.size(), prices.size());
                            flowPaneCenter.getChildren().addAll(MakeLineChart
                                    (prices, ma, "Moving Average strategy"), MakeProfitLineChart
                                    (movingAverageStrategy.getPortfoliovalue(), "Moving Average strategy"));
                            profit.setText(profit.getText()+"Moving Average Strategy profit: "+OurProfit+'\n');
                            movingAverageStrategy = null;
                            System.gc();
                        }
                        if (((CheckBox) checkboxes.getChildren().get(1)).isSelected()) {
                            MovingAverageConvergenceDivergence movingAverageConvergenceDivergence =
                                    new MovingAverageConvergenceDivergence(Double.parseDouble(cash.getText()),
                                            Double.parseDouble(assets.getText()));
                            OurProfit = String.valueOf((movingAverageConvergenceDivergence.
                                    signals(prices) - Double.valueOf(cash.getText())));
                            List<List<Double>> macd = movingAverageConvergenceDivergence.getdataforchart();
                            flowPaneCenter.getChildren().addAll(MakeLineChart(macd.get(0), macd.get(1),"moving Average Convergence/Divergence strategy"), MakeProfitLineChart
                                    (movingAverageConvergenceDivergence.getPortfoliovalue(),"moving Average Convergence/Divergence strategy"));
                            profit.setText(profit.getText()+"moving Average Convergence/Divergence profit: "+OurProfit+'\n');
                            movingAverageConvergenceDivergence=null;
                            System.gc();
                        }
                        if (((CheckBox) checkboxes.getChildren().get(2)).isSelected()) {
                            AwesomeOscillator awesomeOscillator = new AwesomeOscillator(Double.parseDouble
                                    (cash.getText()), Double.parseDouble(assets.getText()));
                            OurProfit = String.valueOf((awesomeOscillator.
                                    signals(prices) - Double.valueOf(cash.getText())));
                            List<List<Double>> ao = awesomeOscillator.getdataforchart();
                            flowPaneCenter.getChildren().addAll(MakeLineChart(ao.get(0), ao.get(1),"Awesome Oscillator"), MakeProfitLineChart
                                    (awesomeOscillator.getPortfoliovalue(),"Awesome Oscillator"));
                            profit.setText(profit.getText()+"Awesome Oscillator profit: "+OurProfit+'\n');
                            awesomeOscillator=null;
                            System.gc();
                        }
                        if (((CheckBox) checkboxes.getChildren().get(3)).isSelected()) {
                            MoneyFlowIndex moneyFlowIndex = new MoneyFlowIndex(Double.parseDouble
                                    (cash.getText()), Double.parseDouble(assets.getText()));
                            OurProfit = String.valueOf((moneyFlowIndex.
                                    signals(prices,HTTP.GetVolumes((HTTP.Get(HTTP.MakeUrl(companies.get(company))))))
                                    - Double.valueOf(cash.getText())));

                            List<Double> mf = moneyFlowIndex.getdataforchart();
                            flowPaneCenter.getChildren().addAll(MakeLineChart(mf,"Money Flow Index"), MakeProfitLineChart
                                    (moneyFlowIndex.getPortfoliovalue(),"Money Flow Index"));
                            profit.setText(profit.getText()+"Money Flow Index profit: "+OurProfit+'\n');
                            moneyFlowIndex=null;
                            System.gc();
                        }
                        if (((CheckBox) checkboxes.getChildren().get(4)).isSelected()) {
                            RelativeStrengthindex relativeStrengthindex = new RelativeStrengthindex(Double.parseDouble
                                    (cash.getText()), Double.parseDouble(assets.getText()));
                            OurProfit = String.valueOf((relativeStrengthindex.
                                    signals(prices) - Double.valueOf(cash.getText())));

                            List<Double> rs = relativeStrengthindex.getdataforchart();
                            flowPaneCenter.getChildren().addAll(MakeLineChart(rs,"Relative Strength index"), MakeProfitLineChart
                                    (relativeStrengthindex.getPortfoliovalue(),"Relative Strength index"));
                            profit.setText(profit.getText()+"Relative Strength index profit: "+OurProfit+'\n');
                            relativeStrengthindex=null;
                            System.gc();
                        }
                    }
                    else profit.setText("Компания не выбрана, выберите компанию");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e){
                    profit.setText("Ошибка ввода");
                }

            }
        });
        return btn;
    }
    private BorderPane MakeborderPane(FlowPane flowtop, ScrollPane scroll, FlowPane flowbottom){
        BorderPane border = new BorderPane();
        border.setTop(flowtop);
        border.setCenter(scroll);
        border.setBottom(flowbottom);
        return border;
    }

    private void MakeFlowPaneCenter(){
        flowPaneBottom =new FlowPane(10, 10,MakeProfitLabel());

    }

    private void MakeFlowPaneBottom(){
        flowPaneCenter = new FlowPane(10, 10);
    }

    private void MakeFlowPaneTop(){
        cash=MakeTextfield("Cash");
        asserts =MakeTextfield("asserts");
        flowPaneTop =new FlowPane(10, 10, MakeComboBoxCompanies(),cash,asserts,MakeCheckBoxes(),
                MakeButton(cash,asserts),MakeClearButton());
    }
    private LineChart MakeLineChart(List<Double>prices,List<Double>dataforchart,String strategy_name){
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        y.setAutoRanging(false);
        if (Collections.min(prices) < Collections.min(dataforchart))
            y.setLowerBound(Collections.min(prices));
        else
            y.setLowerBound(Collections.min(dataforchart));
        if (Collections.max(prices) >Collections.max(dataforchart))
            y.setUpperBound(Collections.max(prices));
        else
            y.setUpperBound(Collections.max(dataforchart));
        LineChart<Number, Number> numberLineChart = new LineChart<Number, Number>(x,y);
        numberLineChart.setTitle(strategy_name);
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        if (strategy_name=="Moving Average strategy") {
            series1.setName("Prices");
            series2.setName("Signal line");
        }
        else{
            series1.setName("Signal line 1");
            series2.setName("Signal line 2");
        }
        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();
        for(int i=0; i<prices.size(); i++){
            datas.add(new XYChart.Data(i,prices.get(i)));
            datas2.add(new XYChart.Data(i,dataforchart.get(i)));        }
        series1.setData(datas);
        series2.setData(datas2);
        numberLineChart.getData().add(series1);
        numberLineChart.getData().add(series2);
        numberLineChart.setCreateSymbols(false);

        return numberLineChart;
    }
    private LineChart MakeLineChart(List<Double>dataforchart, String strategy_name){
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        LineChart<Number, Number> numberLineChart = new LineChart<Number, Number>(x,y);
        numberLineChart.setTitle(strategy_name);
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();
        series1.setName("Signal line");
        series2.setName("top line");
        series3.setName("bottom line");

        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas3 = FXCollections.observableArrayList();
        for(int i=0; i<dataforchart.size(); i++) {
            datas.add(new XYChart.Data(i, dataforchart.get(i)));
        }
        for(int i=0; i<dataforchart.size(); i++) {
            datas2.add(new XYChart.Data(i, 80));
        }
        for(int i=0; i<dataforchart.size(); i++) {
            datas3.add(new XYChart.Data(i, 20));
        }

        series1.setData(datas);
        series2.setData(datas2);
        series3.setData(datas3);
        numberLineChart.getData().add(series1);
        numberLineChart.getData().add(series2);
        numberLineChart.getData().add(series3);
        numberLineChart.setCreateSymbols(false);
        return numberLineChart;
    }
    private LineChart MakeProfitLineChart(List<Double>profit,String strategy_name){
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        LineChart<Number, Number> numberLineChart = new LineChart(x,y);
        numberLineChart.setTitle(strategy_name);
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
