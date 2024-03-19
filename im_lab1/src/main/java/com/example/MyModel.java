package com.example;

public class MyModel {
    final float g = (float) 9.81;
    final float C = (float) 0.15;
    final float rho = (float) 1.29;
    final double dt;
    private boolean LandOn = false;
    private double maxH;
    private double speedAtTheEnd;
    private double distance;

    private double x, y, cosa, sina, k, vx, vy;

    public class ModelInfo {
        public double maxH;
        public double speedAtTheEnd;
        public double distance;
        public double dt;

        ModelInfo(double dt, double dis, double m, double s){
            maxH = m;
            speedAtTheEnd = s;
            distance = dis;
            this.dt = dt;
        }
    }

    public class Cord{
        public double x;
        public double y;

        Cord(double x, double y){
            this.y = y;
            this.x = x;
        }
    }

    public boolean isLandOn(){
        return LandOn;
    }

    public ModelInfo getInfo(){
        return new ModelInfo(dt, distance, maxH, speedAtTheEnd);
    }

    public Cord getNowState(){
        return new Cord(x,y);
    }

    public Cord getNextState(){

        if(LandOn) return new Cord(x,y);

        double v = Math.sqrt((vx * vx + vy * vy));
        vx = vx - k * vx * v * dt;
        vy = vy - (g+ k * vy * v) * dt;
        x = x + vx * dt;
        y = y + vy * dt;

        maxH = Math.max(maxH, y);

        if (y <= 0) {
            LandOn = true;
            distance = x;
            speedAtTheEnd = v;
        }
        return new Cord(x,y);
    }

    MyModel(double m, double S, double y, double v0, double angle, double dt){
        this.maxH = y;
        this.y = y;
        this.dt = dt;
        x = 0;
        double a = angle* Math.PI / 180;
        cosa = Math.cos(a);
        sina = Math.sin(a);
        k = 0.5 * C * rho * S / m;
        vx = v0 * cosa;
        vy = v0 * sina;
    }
}
