package com.example;
import java.sql.*;

import com.example.MyModel.ModelInfo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;

public class DBHandler {
    private final String url = "jdbc:postgresql://localhost:5432/im_project";
    private final String user = "art";
    private final String password = "2978";

    DBHandler(){
        try (Connection con = DriverManager.getConnection(url, user, password);) {
            System.out.println("connected");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    public class RowDb{
        private SimpleStringProperty id;
        private SimpleStringProperty timeStep;
        private SimpleStringProperty distance;
        private SimpleStringProperty maxHeight;
        private SimpleStringProperty speedAtTheEnd;

        RowDb(String id, String time, String dist, String maxH, String speed){
            this.id = new SimpleStringProperty(id);
            this.timeStep = new SimpleStringProperty(time);
            this.distance = new SimpleStringProperty(dist);
            this.maxHeight = new SimpleStringProperty(maxH);
            this.speedAtTheEnd = new SimpleStringProperty(speed);
        }

        public String getId(){ return id.get();}
        public void setId(String value){ id.set(value);}

        public String getTimeStep(){ return timeStep.get();}
        public void setTimeStep(String value){ timeStep.set(value);}

        public String getDistance(){ return distance.get();}
        public void setDistance(String value){ distance.set(value);}

        public String getMaxHeight(){ return maxHeight.get();}
        public void setMaxHeight(String value){ maxHeight.set(value);}

        public String getSpeedAtTheEnd(){ return speedAtTheEnd.get();}
        public void setSpeedAtTheEnd(String value){ speedAtTheEnd.set(value);}
    }

    public ObservableList<RowDb> getAllRow(){
        try (Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement pst = con.prepareStatement("SELECT * FROM model");
        ResultSet rs = pst.executeQuery();) {
            ObservableList<RowDb> list = FXCollections.observableArrayList();
            while (rs.next()) {
                list.add(new RowDb(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)
                ));
            }
            System.out.println("connected");
            return list;
            
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            return null;
        }        
    }

    public void setRow(ModelInfo i){

        BigDecimal numericDt = new BigDecimal(String.valueOf(i.dt));
        BigDecimal numericDist = new BigDecimal(String.valueOf(i.distance));
        BigDecimal numericMaxH = new BigDecimal(String.valueOf(i.maxH));
        BigDecimal numericSpeed = new BigDecimal(String.valueOf(i.speedAtTheEnd));

        String query = "INSERT INTO model(time_step, distance, max_height, speed_at_the_end_point) VALUES(?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement pst = con.prepareStatement(query)) {
            pst.setBigDecimal(1, numericDt);
            pst.setBigDecimal(2, numericDist);
            pst.setBigDecimal(3, numericMaxH);
            pst.setBigDecimal(4, numericSpeed);
            pst.executeUpdate();
            System.out.println("connected");
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }


    public static void main(String[] args) throws SQLException {
        DBHandler d = new DBHandler();
        d.getAllRow();
    }
}
