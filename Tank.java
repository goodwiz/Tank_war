/*
Author:goodwiz
Time:2018/06/06
Github:https://github.com/goodwiz
 */

package com.company;//需要删除这条

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;


//    坦克类，实现与坦克相关的所有操作
public class Tank {

//    初始化坦克速度，大小
    public  static  final int XSPEED = 5;
    public  static  final int YSPEED = 5;

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

//    获得坦克生命值
    public int getLife() {
        return life;
    }

//    设置坦克生命值
    public void setLife(int life) {
        this.life = life;
    }

//    初始化坦克生命值和血条
    private int life = 100;
    private BloodBar bb = new BloodBar();

//    设置坦克生死
    public void setLive(boolean live) {
        this.live = live;
    }
//    获得坦克生死
    public boolean isLive() {
        return live;
    }

//    初始化坦克为生
    private boolean live = true;

//    创建坦克/游戏对象
    TankClient tc;

//    获得坦克好坏
    public boolean isGood() {
        return good;
    }

    private boolean good;

    private int x, y;

//    初始化坦克前一刻位置
    private int oldX, oldY;

//    随机数生成
    private static Random r = new Random();

//    初始化方向
    private boolean bL = false, bU = false,bR = false,bD = false;

//    方向数组
    enum Direction{L,LU,U,RU,R,RD,D,LD,STOP};


    private Direction dir = Direction.STOP;

    private Direction ptDir = Direction.D;

    private int step = r.nextInt(12) + 3;

//    存放坦克位置
    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY =y;
        this.good = good;
    }


//    存放坦克位置方向
    public Tank(int x, int y, boolean good, Direction dir, TankClient tc){
        this(x,y, good);
        this.dir = dir;
        this.tc= tc;
    }

//    画出坦克
    public void draw(Graphics g){

        if(!live) {
            if(!good){
                tc.tanks.remove(this);
            }
            return;
        }

        Color c = g.getColor(); //取得g(以后称为画笔)的颜色
        if (good) g.setColor(Color.RED);
        else g.setColor(Color.BLUE);
        g.fillOval(x, y, WIDTH, HEIGHT); //"画圆",利用填充一个四边形(四边形的内切圆)，参数分别代表：四边形左上点的坐标X，Y，宽度，高度
        g.setColor(c); //用完画笔后把画笔默认的颜色(黑色)设置回去

//        if(good){
//            bb.draw(g);
//        }

        bb.draw(g);
//        方向判断，重画坦克
        switch (ptDir){
            case L:
                g.drawLine(x +Tank.WIDTH/2,y + Tank.HEIGHT/2,x,y+Tank.HEIGHT/2);
                break;
            case LU:
                g.drawLine(x +Tank.WIDTH/2,y + Tank.HEIGHT/2,x,y);
                break;
            case U:
                g.drawLine(x +Tank.WIDTH/2,y + Tank.HEIGHT/2,x + Tank.WIDTH/2,y);
                break;
            case RU:
                g.drawLine(x +Tank.WIDTH/2,y + Tank.HEIGHT/2,x+ Tank.WIDTH,y);
                break;
            case R:
                g.drawLine(x +Tank.WIDTH/2,y + Tank.HEIGHT/2,x+Tank.WIDTH,y+Tank.HEIGHT/2);
                break;
            case RD:
                g.drawLine(x +Tank.WIDTH/2,y + Tank.HEIGHT/2,x+Tank.WIDTH,y+Tank.HEIGHT);
                break;
            case D:
                g.drawLine(x +Tank.WIDTH/2,y + Tank.HEIGHT/2,x + Tank.WIDTH/2 ,y+Tank.HEIGHT);
                break;
            case LD:
                g.drawLine(x +Tank.WIDTH/2,y + Tank.HEIGHT/2,x,y+Tank.HEIGHT);
                break;

        }


        move();
    }

//    坦克移动函数
    void move(){

        this.oldX = x;
        this.oldY = y;


        switch (dir){
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case STOP:
                break;
        }

        if(this.dir != Direction.STOP){
            this.ptDir = this.dir;
        }

        if (x < 0) x = 0;
        if( y < 30) y = 30;
        if ( x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
        if ( y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;

        if(!good){
            Direction[] dirs = Direction.values();

            if(step == 0){
                step = r.nextInt(12)+3;
                int rn = r.nextInt(dirs.length);
                dir = dirs[rn];
            }

            step --;

            if(r.nextInt(40) > 38) this.fire();

        }

    }

    private void stay(){
        x = oldX;
        y = oldY;
    }

//    按键按压控制
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode(); //得到按键的虚拟码，再和下面的KeyEvent.VK_LEFT等虚拟码比较看是否是某按键

        switch (key) {
            case KeyEvent.VK_R:
                if(!this.live){
                    this.live = true;
                    this.life = 100;
                }
            case KeyEvent.VK_CONTROL:
//                tc.m = fire();
                fire();
                break;

            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;

        }
        locateDirection();

    }

//    方向判断
    void locateDirection(){
        if(bL && !bU && !bR && !bD) dir = Direction.L;
        else if(bL && bU && !bR && !bD) dir = Direction.LU;
        else if(!bL && bU && !bR && !bD) dir = Direction.U;
        else if(!bL && bU && bR && !bD) dir = Direction.RU;
        else if(!bL && !bU && bR && !bD) dir = Direction.R;
        else if(!bL && !bU && bR && bD) dir = Direction.RD;
        else if(!bL && !bU && !bR && bD) dir = Direction.D;
        else if(bL && !bU && !bR && bD) dir = Direction.LD;
        else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
    }

//    按键抬起控制
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode(); //得到按键的虚拟码，再和下面的KeyEvent.VK_LEFT等虚拟码比较看是否是某按键

        switch (key) {

            case KeyEvent.VK_CONTROL:
//              tc.m = fire();
                fire();
                break;

            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_A:
                superFire();
                break;

        }
        locateDirection();

    }

//    子弹发射，根据炮筒方向
    public Missile fire(){
        if (!live) return null;
        int x = this.x + Tank.WIDTH/2- Missile.WIDTH/2;
        int y = this.y + Tank.HEIGHT/2- Missile.HEIGHT/2;

        Missile m = new Missile(x, y,good,ptDir, this.tc);
        tc.missiles.add(m);
        return m;

    }


//    子弹发射，任意方向
    public Missile fire(Direction dir){

        if (!live) return null;
        int x = this.x + Tank.WIDTH/2- Missile.WIDTH/2;
        int y = this.y + Tank.HEIGHT/2- Missile.HEIGHT/2;

        Missile m = new Missile(x, y,good,dir, this.tc);
        tc.missiles.add(m);
        return m;
    }

//    获得坦克方块
    public Rectangle getRect(){
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

//    处理坦克撞墙
    public boolean collidesWithWall(Wall w){
        if(this.live && this.getRect().intersects(w.getRect())){
            this.stay();
            return true;
        }
        return false;
    }

//    处理坦克撞坦克
    public boolean collidesWithTanks (List <Tank> tanks){

        for(int i = 0; i < tanks.size();i++){
            Tank t = tanks.get(i);
            if(this != t){
                if(this.live && t.isLive() &&  this.getRect().intersects(t.getRect())){
                    this.stay();
                    t.stay();
                    return true;
            }
        }
        }
        return false;

    }

//    超级炮弹
    private void superFire(){
        Direction[] dirs =Direction.values();
        for(int i = 0; i< 8; i++){
            fire(dirs[i]);
        }
    }

//    血条画出
    private class BloodBar{
        public void draw (Graphics g){
            Color c = g.getColor();
            g.setColor(Color.RED);
            g.drawRect(x,y-10, WIDTH,10);
            int w = WIDTH * life/100;
            g.fillRect(x,y-10,w,10);
            g.setColor(c);
        }
    }

//    坦克吃血块
    public boolean eat(Blood b) {
        if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
            this.life = 100;
            b.setLive(false);
            return true;
        }
        return false;

    }
}
