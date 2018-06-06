/*
Author:goodwiz
Time:2018/06/06
Github:https://github.com/goodwiz
 */

package com.company;//需要删除这条

import java.awt.*;


//       爆炸类，实现爆炸的绘制
public class Explode {
    int x , y;
    private boolean live = true;


    private  TankClient tc;

//    存储爆炸圆，大小变化
    int [] diameter = {4,7,12,18,26,32,49,30,14,6};
    int step = 0;


    public Explode(int x, int y,TankClient tc){
        this.x =x;
        this.y =y;
        this.tc =tc;
    }

//    爆炸绘制，重新赋值
    public void draw(Graphics g){
        if(!live) {
            tc.explodes.remove(this);
            return;

        }

        if(step == diameter.length){
            live = false;
            step = 0;
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.ORANGE);
        g.fillOval(x,y,diameter[step],diameter[step]);
        step ++;
    }
}
