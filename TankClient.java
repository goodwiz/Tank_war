/*
Author:goodwiz
Time:2018/06/06
Github:https://github.com/goodwiz
 */

package com.company;//需要删除这条


import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;


//  主函数类，实现画布构造，调用各个类
public class TankClient extends Frame {

//    设置画布大小，固定值不可调
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

//    创建我方坦克，规定位置、转态
    Tank myTank = new Tank(50,50,true,Tank.Direction.STOP,this);
//    Missile m = null;

//    创建不可穿透墙，位置大小
    Wall w1 =new Wall(100, 200, 20, 150,this), w2 = new Wall(300,100,300,20,this);

//    创建坦克的血条
    Blood b = new Blood();


//    创建数组用于存放需要不断再生的数据

//    创建数组Explode存放爆炸
    List<Explode> explodes = new ArrayList<Explode>();

//    创建数组Missile存放子弹
    List<Missile> missiles = new ArrayList<Missile>();

//    创建Tank存放坦克
    List<Tank> tanks = new ArrayList<Tank>();


//    Missile m = new Missile(50,50,Tank.Direction.R);

//    定义一个屏幕后的虚拟图片，用于画出坦克
    Image offScreenImage = null;



//    绘画函数，实现视觉呈现
    @Override
    public void paint(Graphics g) {

//        画布上呈现实时数据
        g.drawString("missiles count:" + missiles.size(),10,50);
        g.drawString("explodes count:" + explodes.size(),10,70);
        g.drawString("tanks count:" + tanks.size(),10,90);
        g.drawString("tanks life:" + myTank.getLife(),10,110);

//        添加坦克，如果敌方坦克数为零，重新添加坦克
        if(tanks.size() <= 0){
            for (int i = 0; i<5; i++){
                tanks.add(new Tank(50 + 40 *(i +1), 50,false,Tank.Direction.D,this));
            }
        }

//        添加子弹，实现子弹打击坦克/墙操作
        for (int i = 0; i < missiles.size(); i++){
            Missile m = missiles.get(i);

            m.hitTanks(tanks);
            m.hitTank(myTank);
            m.hitWall(w1);
            m.hitWall(w2);
            m.draw(g);
//            if (!m.isLive()) missiles.remove(m);
//            else m.draw(g);
        }
//        if (m!= null) m.draw(g);

//        添加爆炸，爆炸消失重新生成
        for (int i = 0 ; i < explodes.size(); i++){
            Explode e = explodes.get(i);
            e.draw(g);
        }

//        坦克撞击操作，处理坦克撞击墙/坦克
        for (int i = 0; i < tanks.size(); i++){
            Tank t = tanks.get(i);
            t.collidesWithWall(w1);
            t.collidesWithWall(w2);
            t.collidesWithTanks(tanks);
            t.draw(g);
        }


//        重画，画出我方坦克，血条补给，墙和血条
        myTank.draw(g);
        myTank.eat(b);
        w1.draw(g);
        w2.draw(g);
        b.draw(g);


    }

    //闪动处理，利用双缓冲消除圆圈移动时屏幕的闪动
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT); //判断是为了避免每次重画时都给offScreenImage赋值
        }
        Graphics gOffScreen = offScreenImage.getGraphics(); //定义虚拟图片上的画笔gOffScreen
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.GREEN);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT); //重画背景，如果没有这句则在屏幕上会保留圆圈的移动路径
        gOffScreen.setColor(c);
        paint(gOffScreen); //把圆圈画到虚拟图片上
        g.drawImage(offScreenImage, 0, 0, null); //再一次性把虚拟图片画到真实屏幕上，在真实屏幕上画则要用真实屏幕的画笔g
    }



//    画布设置
    public void launchFrame() {

        for (int i = 0; i<10; i++){
            tanks.add(new Tank(50 + 40 *(i +1), 50,false,Tank.Direction.D,this));
        }


        this.setLocation(400, 300);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setTitle("坦克大战");
        this.setResizable(false); //不允许改变窗口大小
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        }); //添加关闭功能，此处使用匿名类比较合适
        this.setBackground(Color.GREEN);

        this.addKeyListener(new KeyMonitor());

        setVisible(true);

        new Thread(new PaintThread()).start(); //启动线程，实例化线程对象时不要忘了new Thread(Runnable对象);
    }

//    主函数
    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }

    //画布优化，PaintThread只为TankClient服务，所以写成内部类好些
    public class PaintThread implements Runnable {

        public void run() {
            while (true) {
                repaint(); //repaint()是TankClient或者他的父类的方法，内部类可以访问外部包装类的成员，这也是内部类的好处
                try {
                    Thread.sleep(100); //每隔50毫秒重画一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    按键处理，处理子弹坦克的移动和按压操作
    public class KeyMonitor extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            myTank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }

    }

}
