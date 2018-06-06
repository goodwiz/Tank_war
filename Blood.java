/*
Author:goodwiz
Time:2018/06/06
Github:https://github.com/goodwiz
 */

package com.company;//需要删除这条

import java.awt.*;



//      血块类，画出血块，添加移动轨迹
public class Blood {
    int x , y, w , h;

    TankClient tc;

    int step =0;

//    获得坦克生死
    public boolean isLive() {
        return live;
    }

//    设置坦克血条
    public void setLive(boolean live) {
        this.live = live;
    }

    private boolean live = true;

    private int[][] pos = {{350,300},{360,300},{375,275},{400,200},{360,270},{365,290},{340,280}};


//    初始化血块位置
    public Blood (){
        x = pos[0][0];
        y = pos[0][1];
    }


//    画出血块，执行移动
    public void draw (Graphics g){
        if(!live) return;

        Color c= g.getColor();
        g.setColor(Color.MAGENTA);
        g.fillRect(x,y,w,h);
        g.setColor(c);

        move();
    }

//    血块移动，如果血块位置达到终点则初始化
    private void move(){
        step++;
        if(step == pos.length){
            step = 0;
        }
        x = pos[step][0];
        y = pos[step][1];
        w = h = 15;
    }

//    获得血块方块，用于判断坦克是否接触到血块
    public Rectangle getRect(){

        return new Rectangle(x,y,w,h);

    }


}
