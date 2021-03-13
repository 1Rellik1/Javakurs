
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;

public class VisualInteface extends Application {
    HashMap<String, String> companies;
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
        stage.setWidth(450);
        stage.setHeight(550);
        stage.setResizable(true);
        stage.centerOnScreen();
        return (stage);
    }
    private Scene MakeScene(){
        ObservableList<String> langs = FXCollections.observableArrayList(companies.keySet());
        ComboBox<String> comboBox = new ComboBox(langs);
        comboBox.setValue("Телеграф-п");
        comboBox.setVisibleRowCount(18);
        Label lbl = new Label();
        comboBox.setOnAction(event -> lbl.setText(comboBox.getValue()));
        FlowPane flowPane = new FlowPane(10, 10, comboBox, lbl);
        Scene scene = new Scene(flowPane, Color.BLACK);
        return scene;
    }

//    private ComboBox<String> MakeFlowPane(){
//
//        return (comboBox);
//    }
}
