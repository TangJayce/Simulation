package Processing;

import processing.core.PApplet;

public class Ball {
    private int x;
    private int y;
    private int r;
    private PApplet pApplet;
    private int xSpeed;
    private int ySpeed;

    public Ball() {}

    public Ball(PApplet pApplet,int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.pApplet = pApplet;
        this.xSpeed = (int)pApplet.random(-10,10);
        this.ySpeed = (int)pApplet.random(-10,10);
    }

    public Ball(PApplet pApplet, int x, int y, int r, int xSpeed, int ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.pApplet = pApplet;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void step(){
        x += xSpeed;
        if(x < 0 || x > pApplet.width){
            xSpeed *= -1;
        }

        y += ySpeed;
        if(y < 0 || y > pApplet.height){
            ySpeed *= -1;
        }
    }

    public void render(){
        pApplet.circle(x, y, r);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }
}
