package Processing;

import processing.core.PVector;

import java.util.ArrayList;

/**
 * @author Jayce
 */
public class Body {
    String id;                      //名称
    float mass;                     //质量
    float volume;                   //体积
    float r;                        //半径
    PVector pos;                    //位置向量
    PVector vel;                    //运动方向向量
    private CelestialMotionSimulation celestialMotionSimulation = new CelestialMotionSimulation();
    //RGB颜色值
    float ColorR;
    float ColorG;
    float ColorB;
    //最大路径长度(单位：帧，而非像素点)
    private static final int maxPath = 400;
    //加速
    static float N = 1;

    private ArrayList<PVector> path = new ArrayList<>();

    Body(){
        ColorR = celestialMotionSimulation.random(0,255);
        ColorG = celestialMotionSimulation.random(0,255);
        ColorB = celestialMotionSimulation.random(0,255);
    }

    public Body(CelestialMotionSimulation celestialMotionSimulation) {
        this.celestialMotionSimulation = celestialMotionSimulation;
        ColorR = celestialMotionSimulation.random(0,255);
        ColorG = celestialMotionSimulation.random(0,255);
        ColorB = celestialMotionSimulation.random(0,255);
    }

    Body(CelestialMotionSimulation celestialMotionSimulation, float mass, float x, float y, float vx, float vy, String id) {
        this.id = id;
        this.mass = mass;
        this.pos = new PVector(x,y);
        this.vel = new PVector(vx,vy);
        this.celestialMotionSimulation = celestialMotionSimulation;
        this.r = CalR();
        ColorR = celestialMotionSimulation.random(0,255);
        ColorG = celestialMotionSimulation.random(0,255);
        ColorB = celestialMotionSimulation.random(0,255);
        //System.out.println("r="+ColorG+" g="+ColorG+" b="+ColorB);
    }

    Body(CelestialMotionSimulation celestialMotionSimulation, float mass, float volume, float x, float y, float vx, float vy, String id) {
        this.id = id;
        this.mass = mass;
        this.volume = volume;
        this.pos = new PVector(x,y);
        this.vel = new PVector(vx,vy);
        this.celestialMotionSimulation = celestialMotionSimulation;
        this.r = CalR();
        ColorR = celestialMotionSimulation.random(0,255);
        ColorG = celestialMotionSimulation.random(0,255);
        ColorB = celestialMotionSimulation.random(0,255);
        //System.out.println("r="+ColorG+" g="+ColorG+" b="+ColorB);
    }

    /**
     * 由公式  V = 4πR³/3  推导  假设密度都为 1
     * @author Jayce
     * @return r  (半径)
     */
    float CalR(){
        if (celestialMotionSimulation.maxStar == null) {
            celestialMotionSimulation.maxStar = this;
        } else if (celestialMotionSimulation.maxStar.mass < mass) {
            celestialMotionSimulation.maxStar = this;
        }
        //return (float) Math.sqrt(mass/Math.PI);                 //二维
        return (float) Math.pow(mass/Math.PI*(3f/4f),1f/3f);      //三维
    }

    void Show(){
        celestialMotionSimulation.fill(ColorR,ColorG,ColorB);
        celestialMotionSimulation.circle(pos.x,pos.y,r*2);
        celestialMotionSimulation.fill(255);
        celestialMotionSimulation.text(id,pos.x,pos.y);
    }

    void ShowPath(){
        celestialMotionSimulation.stroke(ColorR,ColorG,ColorB);
        for(int i=0;i<path.size();i++){
            PVector a = path.get(i);
            PVector b;
            if(i+1 == path.size()){
                b=pos;
            }else{
                b = path.get(i+1);
                celestialMotionSimulation.line(a.x,a.y,b.x,b.y);
            }
        }
    }

    void Update(){
        pos.add(PVector.mult(vel,N));
        path.add(new PVector(pos.x,pos.y));
        if(path.size() > maxPath){
            path.remove(0);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
        this.r = CalR();
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getR() {
        return r;
    }

    public PVector getPos() {
        return pos;
    }

    public void setPos(PVector pos) {
        this.pos = pos;
    }

    public PVector getVel() {
        return vel;
    }

    public void setVel(PVector vel) {
        this.vel = vel;
    }

    public static int getMaxPath() {
        return maxPath;
    }

    public CelestialMotionSimulation getCelestialMotionSimulation() {
        return celestialMotionSimulation;
    }

    public void setCelestialMotionSimulation(CelestialMotionSimulation celestialMotionSimulation) {
        Body temp = this.celestialMotionSimulation.maxStar;
        this.celestialMotionSimulation = celestialMotionSimulation;
        this.celestialMotionSimulation.maxStar = temp;
    }
}
