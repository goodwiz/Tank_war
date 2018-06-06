/*
Author:goodwiz
Time:2018/06/06
Github:https://github.com/goodwiz
 */

package com.company;//需要删除这条

import java.awt.*;
//    墙类
public class Wall {
    int x, y ,w , h;
    TankClient tc;


//    存储墙数据
    public Wall(int x, int y, int w, int h, TankClient tc) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tc = tc;
    }

//    绘制墙
    public void draw (Graphics g){
        g.fillRect(x,y,w,h);
    }

    public Rectangle getRect(){
        return new Rectangle(x, y, w, h);
    }

}
