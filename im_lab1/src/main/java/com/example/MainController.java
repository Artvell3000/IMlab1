package com.example;

import javafx.util.Duration;

import com.example.DBHandler.RowDb;
import com.example.MyModel.Cord;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController{
    MyModel model = null;
    final double delay = 50;
    DBHandler db = new DBHandler();

    @FXML
    XYChart.Series<Number, Number> series = null;

    private double getDoubleValue(TextField t){
        return Double.parseDouble(t.getText());
    }
     
    @FXML
    private Button btn;
    @FXML
    private Button btnStop;

    Timeline timeline = new Timeline();

    @FXML
    private TextField edtHeight;
    @FXML
    private TextField edtAngle;
    @FXML
    private TextField edtSize;
    @FXML
    private TextField edtWeight;
    @FXML
    private TextField edtSpeed;
    @FXML
    private TextField edtDt;
    
    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private TableColumn<RowDb, String> idCol;

    @FXML
    private TableColumn<RowDb, String> timeCol;
    @FXML
    private TableColumn<RowDb, String> maxHCol;
    @FXML
    private TableColumn<RowDb, String> speedCol;
    @FXML
    private TableColumn<RowDb, String> distanceCol;

    @FXML
    private TableView<RowDb> table;

    @FXML
    private void clickI(ActionEvent event){
        table.setVisible(!table.isVisible());
        
        var l = db.getAllRow();

        idCol.setCellValueFactory(new PropertyValueFactory<RowDb, String>("id"));
        timeCol.setCellValueFactory(new PropertyValueFactory<RowDb, String>("timeStep"));
        speedCol.setCellValueFactory(new PropertyValueFactory<RowDb, String>("speedAtTheEnd"));
        maxHCol.setCellValueFactory(new PropertyValueFactory<RowDb, String>("maxHeight"));
        distanceCol.setCellValueFactory(new PropertyValueFactory<RowDb, String>("distance"));

        table.setItems(l);
        
    }

    @FXML
    private void clickStart(ActionEvent event) {
        btn.setVisible(false);

        model = new MyModel(
            getDoubleValue(edtWeight),
            getDoubleValue(edtSize), 
            getDoubleValue(edtHeight), 
            getDoubleValue(edtSpeed), 
            getDoubleValue(edtAngle),
            getDoubleValue(edtDt)
            );

        series = new XYChart.Series<>();
        lineChart.getData().add(series);

        timeline.getKeyFrames().add(
            new KeyFrame(Duration.millis(delay), e -> {
                if(!model.isLandOn()) {
                    
                    Cord prevP = model.getNowState();
                    Cord p = model.getNextState();

                    if(p.y < 0 ){
                        p.x = prevP.x + ((0 - prevP.y)  * (p.x - prevP.x))/ (p.y - prevP.y);
                        p.y = 0;
                    }

                    series.getData().add(new XYChart.Data<>(p.x, p.y));
                }else{
                    btnStop.setVisible(false);
                    db.setRow(model.getInfo());
                    timeline.stop();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void clickUpdate(ActionEvent event){



        model = new MyModel(
            getDoubleValue(edtWeight),
            getDoubleValue(edtSize), 
            getDoubleValue(edtHeight), 
            getDoubleValue(edtSpeed), 
            getDoubleValue(edtAngle),
            getDoubleValue(edtDt)
            );

        lineChart.getData().remove(series);

        series = new XYChart.Series<>();
        lineChart.getData().add(series);
        
        timeline.play();
        
        isStop = false;
        btnStop.setText("Stop");
        btnStop.setVisible(true);
    }

    boolean isStop = false;
    @FXML
    private void clickStop(ActionEvent event){
        isStop = !isStop;
        if(isStop){
            timeline.stop();
            btnStop.setText("Continue");
            return;
        }

        timeline.play();
        btnStop.setText("Stop");
    }
}
