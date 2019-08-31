package Processing;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Processing in IDEA
 * @author Jayce
 */
public class CelestialMotionSimulation extends PApplet {

    private static final float G = 0.5f;                    //万有引力常数  应该为6.67259*10^(-11) N·m²/kg²  由于这个程序没有单位，所以随便写了
    private CopyOnWriteArrayList<Body> planets = new CopyOnWriteArrayList<>();
    //private Body star;
    private static int starsCount = 500;                            //行星数量
    private PVector planetsMass = new PVector(100,500);      //行星质量区间
    private PVector planetsSpeed = new PVector(-5f,5f);      //行星速度区间
    private PVector planetsVolume = new PVector(10,(int)5e3);    //行星体积区间   10到5000之间
    Body maxStar;                                                   //镜头追踪最大天体
    private boolean running = true;                                 //暂停
    private static SelectGUI selectGUI;                             //参数窗体
    static int method = 0;                                          //方法
    private static boolean conversionLens = false;                  //镜头是否跟随最大天体  默认为不跟随
    private static boolean is_ShowPath = true;                      //是否显示运行轨迹      默认为显示
    private static PVector lastPosition = new PVector();            //记录镜头最后更新的位置

    public static void main(String[] args){
        //PApplet.main("Processing.CelestialMotionSimulation",args);
        selectGUI = new SelectGUI();
        selectGUI.ShowSelect();
    }

    /**
     * 获取参数信息
     */
    private void GetParam(){
        switch (method) {
            case 1:

                break;
            case 2:
                planetsMass = selectGUI.mass;
                planetsSpeed = selectGUI.speed;
                if(selectGUI.body != null) {
                    Body b = selectGUI.body;
                    b.setCelestialMotionSimulation(this);
                    starsCount = selectGUI.starsCount;
                    planets.add(b);
                }
                else
                    System.out.println("未加入中央天体");
                break;
            default:
                noLoop();
                System.out.println("error1");
        }
    }

    /**
     * 运行动画
     */
    static void Run(){
        System.out.println("method = "+method);
        String[] processingArgs = {"CelestialMotionSimulation"};
        CelestialMotionSimulation celestialMotionSimulation = new CelestialMotionSimulation();
        PApplet.runSketch(processingArgs, celestialMotionSimulation);
    }

    /**
     * 基本设置
     */
    public void settings(){
        size(1600,900);       //窗口大小
    }

    /**
     * 鼠标点击功能设置
     */
    public void mousePressed(){
        //background(64);
    }

    /**
     * 图像初始化
     */
    public void setup(){
        frameRate(144);  //FPS
        //字体
        PFont font = createFont("微软雅黑",12);
        textFont(font);
        //初始化位置为屏幕中央
        lastPosition.x = width >> 1;
        lastPosition.y = height >> 1;
        GetParam();
        switch (method){
            case 1:     //自定义方法
                for(Body body:selectGUI.planets){
                    body.setCelestialMotionSimulation(this);
                    planets.add(body);
                }
                break;
            case 2:     //随机方法
                for(int i = 0;i < starsCount; i++){
                    planets.add(new Body(this,
                            random(planetsMass.x,planetsMass.y),       //质量
                            //random(planetsVolume.x,planetsVolume.y),   //体积
                            random(width),                             //x
                            random(height),                            //y
                            random(planetsSpeed.x,planetsSpeed.y),     //vx
                            random(planetsSpeed.x,planetsSpeed.y),     //vy
                            "M"+i));                               //id
                }
                break;
            default:
                noLoop();
                System.out.println("error2");
        }
    }

    /**
     * 显示每一帧的图像
     */
    public void draw() {
        boolean flag = false;
        background(0);          //背景色：黑色
        if(maxStar != null && conversionLens) {
            translate(-maxStar.pos.x + (width >> 1), -maxStar.pos.y + (height >> 1));   //设置坐标系远点（即左上角坐标）  使得最大天体在正中央
            flag = true;
            lastPosition.x = maxStar.pos.x;
            lastPosition.y = maxStar.pos.y;
        }else {
            translate(-lastPosition.x + (width >> 1), -lastPosition.y + (height >> 1));
        }

        //显示运动轨迹
        if(is_ShowPath) {
            strokeWeight(1);
            for (Body b : planets) {
                b.ShowPath();
            }
        }

        //显示天体
        noStroke();
        for (Body b : planets) {
            b.Show();
            b.Update();
            Attract(b);
        }

        if(flag) {
            //设置回原来的坐标系，使得信息文本保持不动
            translate(maxStar.pos.x - (width >> 1), maxStar.pos.y - (height >> 1));
        }else {
            translate(lastPosition.x - (width >> 1), lastPosition.y - (height >> 1));
        }

        //显示帧率
        fill(0,255,0);
        text("FPS: "+(int)frameRate,10,20);

        //显示数量以及最大天体ID
        if(maxStar != null)
            text("剩余行星数量: "+planets.size() + " 最大行星ID: " + maxStar.id,10,35);
        else
            text("剩余行星数量: "+planets.size(),10,35);
        text("=========================",10,50);
        text("调节速度倍数为: "+Body.N,10,65);

        //显示各个天体的信息
        for(int i=0;i<planets.size();i++){
            Body b = planets.get(i);
            fill(b.ColorR,b.ColorG,b.ColorB);
            String str = "ID=" + b.id + "  M="+b.mass;
            text(str,10,(i + 1) * 20 + 60);
        }

        //显示提示信息
        PFont promptFont = createFont("微软雅黑",20);
        textFont(promptFont);
        fill(255);
        text("操作提示",1450,20);
        PFont info = createFont("微软雅黑",16);
        textFont(info);
        fill(180);
        ArrayList<String> promptText = new ArrayList<>();
            promptText.add("暂停/播放: 空格/p/P");
            promptText.add("是否显示轨迹: k/K");
            promptText.add("(天体加速,而非动画加速)");
            promptText.add("加速: w/W");
            promptText.add("减速: s/S");
            promptText.add("镜头跟踪最大天体: d/D");
            promptText.add("取消跟踪最大天体: a/A");
            promptText.add("镜头移动: 上下左右键");
        for(int i = 0;i < promptText.size(); i++){
            String str = promptText.get(i);
            text(str,1420,(i + 2) * 20);
        }
        PFont font = createFont("微软雅黑",12);
        textFont(font);
    }

    /**
     * 计算单个天体对于其他天体的相互作用
     * @param me 当前天体
     */
    private void Attract(Body me) {
        for(int i = 0; i < planets.size(); i++){  //循环处理每一个天体之间的关系
            Body other = planets.get(i);
            if(other == me)
                continue;
            PVector direction = new PVector(me.pos.x-other.pos.x,me.pos.y-other.pos.y);   //两天体间的方向向量
            if(direction.mag() <= me.r + other.r){    //大的天体吞噬小天体  假设为完全非弹性碰撞
                if(me.mass >= other.mass){             //v = (v1m1 + v2m2)/(m1 + m2)
                    me.vel = PVector.div(PVector.add(PVector.mult(me.vel,me.mass), PVector.mult(other.vel,other.mass)),
                            me.mass + other.mass);
                    me.mass = me.mass + other.mass;
                    me.r = me.CalR();
                    planets.remove(i);
                }
                continue;
            }
            float gravitation = CelestialMotionSimulation.G * (me.mass * other.mass) / direction.magSq();   //天体间引力   F = G·m1·m2/r²
            PVector force = direction.normalize().mult(gravitation);                     //引力方向向量

            other.vel.add(force.div(other.mass).mult(1f/ frameRate));                    //△v = △t·F/m
        }
    }

    /**
     * 按键功能设置
     */
    public void keyPressed(){
        switch (key){
            case 32:                 //空格
            case 'p':
            case 'P':
                if (running) {
                    running = false;
                    noLoop();        //暂停
                } else {
                    running = true;
                    loop();          //运行
                }
                break;
            case 'w':
            case 'W':
                Body.N += 0.5f;      //天体速度增加
                break;
            case 's':
            case 'S':
                if(Body.N >= 1)
                    Body.N -= 0.5f;  //天体速度减小
                break;
            case 'a':
            case 'A':
                conversionLens = false;      //取消跟随最大天体
                break;
            case 'd':
            case 'D':
                conversionLens = true;       //镜头跟随最大天体
                break;
            case 'k':
            case 'K':
                is_ShowPath = !is_ShowPath;  //切换是否显示轨迹
                break;
            case CODED:
                KeyFunction();               //设置一些特殊按键的功能
        }
    }

    /**
     * 特殊按键功能设置
     * 镜头移动: 上下左右键
     */
    private void KeyFunction(){
        switch (keyCode){
            case UP:
                conversionLens = false;
                lastPosition.y -= 10;
                break;
            case DOWN:
                conversionLens = false;
                lastPosition.y += 10;
                break;
            case LEFT:
                conversionLens = false;
                lastPosition.x -= 10;
                break;
            case RIGHT:
                conversionLens = false;
                lastPosition.x += 10;
                break;
        }
    }
}
