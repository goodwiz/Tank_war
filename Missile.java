/*
Author:goodwiz
Time:2018/06/06
Github:https://github.com/goodwiz
 */

package com.company;//需要删除这条

import java.awt.*;
import java.util.List;

//    子弹类，完成所有与子弹相关的操作
public class Missile {

//    初始化子弹速度和大小
    public static final int XSPEED = 10;
    public static final int YSPEED = 10;

    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    int x, y;
    Tank.Direction dir;

    public boolean isLive() {
        return live;
    }

    private boolean good;
    private boolean live = true;



//    子弹函数，存储子弹位置
    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    private TankClient tc;

    public Missile (int x, int y, boolean good,Tank.Direction dir, TankClient tc){
        this(x,y,dir);
        this.good =good;
        this.tc = tc;

    }

//    画出子弹
    public void draw(Graphics g){

        if(!live) {
            tc.missiles.remove(this);
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);

        move();
    }

//    子弹移动函数
    private void move(){


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

        if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT){
            live = false;
//            tc.missiles.remove(this);
        }
    }

    public Rectangle getRect(){
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

//    子弹击中坦克函数
    public boolean hitTank(Tank t){
        if(this.live && this.getRect() .intersects(t.getRect())  && t.isLive() && this.good != t.isGood() ){
            if(t.isGood()){

                t.setLife((t.getLife()-20));
                if(t.getLife() <= 0)t.setLive(false);

            }else {t.setLive(false);}


            this.live = false;

            Explode e = new Explode(x,y,tc);
            tc.explodes.add(e);

            return true;
        }
        return false;
    }

//    子弹击中多个坦克
    public boolean hitTanks (List<Tank> tanks){
        for (int i =0; i < tanks.size(); i++){
            if(hitTank(tanks.get(i))){
                return true;
            }
        }
        return false;
    }

//    子弹击中墙
    public boolean hitWall (Wall w){
        if(this.live && this.getRect().intersects(w.getRect())){
            this.live = false;
            return true;
        }
        return false;

    }
}
