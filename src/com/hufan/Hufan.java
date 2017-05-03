package com.hufan;

import java.awt.*;  
import java.awt.event.*;  
import java.awt.image.BufferedImage;  
import java.io.*;  
import java.util.*;  
import java.util.Timer;  
import javax.imageio.ImageIO;  
import javax.swing.*;  
  
public class Hufan extends JFrame{  
    HuPanel hp;  
    public static void main(String[] args) {  
        new Hufan();  
    }  
    public Hufan(){  
        hp=new HuPanel();  
        this.addMouseMotionListener(hp);  
        this.add(hp);  
        this.setTitle("微信打飞机");  
        this.setSize(400, 600);  
        this.setLocation(500, 100);  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        this.setVisible(true);  
    }  
}  
class HuPanel extends JPanel implements Runnable,MouseMotionListener{  
    Player player;
    static Vector<Enemy> enemys;
    static Vector<Bullet> bullets;  
    static Vector<Bomb> bombs; 
    BufferedImage bground,logo,shoot,bullet,small;  
    BufferedImage smallBomb1,smallBomb2,smallBomb3;
    //SoundPlayer fire;  
    public HuPanel(){  
        player=new Player();  
        enemys=new Vector<Enemy>();  
        bullets=new Vector<Bullet>();  
        bombs=new Vector<Bomb>();  
        new Enemy();
        try {  
            bground=ImageIO.read(new File("images/shoot_background.png"));  
            logo=ImageIO.read(new File("images/logo.png"));  
            //shoot=ImageIO.read(new File("images/shoot.png"));  
            //bullet=shoot.getSubimage(Bullet.imgX, Bullet.imgY, Bullet.imgW, Bullet.imgH);
            bullet=ImageIO.read(new File("images/bullet.png"));
            //small=shoot.getSubimage(Enemy.imgX, Enemy.imgY, Enemy.imgW, Enemy.imgH);  
            small=ImageIO.read(new File("images/enemy1.png"));
            smallBomb1=ImageIO.read(new File("images/enemy1_down1.png"));
            smallBomb2=ImageIO.read(new File("images/enemy1_down2.png"));
            smallBomb3=ImageIO.read(new File("images/enemy1_down3.png")); 
              
//          fire=new SoundPlayer(Bullet.sound);  
//          fire.loop();  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
          
        new Thread(this).start();  
    }  
    public void paint(Graphics g){  
        super.paint(g);  

        g.drawImage(bground, 0, -75,this);  
        g.drawImage(logo, player.x, player.y,60,70,this);  

        for(int i=0;i<bullets.size();i++){  
            Bullet b=bullets.get(i);  
            g.drawImage(bullet, b.x, b.y,this);  
        }  

        for(int i=0;i<enemys.size();i++){  
            Enemy e=enemys.get(i);  
            g.drawImage(small, e.x, e.y,this);  
        }  

        for(int i=0;i<bombs.size();i++){  
            Bomb b=bombs.get(i);  
            if(b.life>12)  
            {  
                g.drawImage(smallBomb1, b.x, b.y, this);  
            }else if(b.life>5)  
            {  
                g.drawImage(smallBomb2, b.x, b.y, this);  
            }else{  
                g.drawImage(smallBomb3, b.x, b.y, this);  
            }  
              

            b.lifeDown();  

            if(b.life==0)  
            {  
                bombs.remove(b);  
            }  
        }  
    }  
    public void run() {  
        while(true){  
            try {  
                Thread.sleep(10);  
                this.repaint();  
            } catch (InterruptedException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    }  
    public void mouseDragged(MouseEvent e) {}  
    public void mouseMoved(MouseEvent e) {  

    	player.x=e.getX()-30;  
        player.y=e.getY()-50;  
    }  
      
}  

class Plane{  
    int x;  
    int y;  
    int speed;  
    public Plane() {}  
    public Plane(int x, int y,int speed) {  
        this.x = x;  
        this.y = y;  
        this.speed=speed;  
    }  
      
}  

class Player extends Plane{  
    int x=100;  
    int y=450;  
    Timer timer;  
    Bullet bullet;  
    public Player(){  
        timer=new Timer();  
        timer.schedule(new PlayerTask(), 10);  
    }  
    class PlayerTask extends TimerTask{  
  
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            while(true){  
                bullet=new Bullet(x+25,y);  
                HuPanel.bullets.add(bullet);  
                new Thread(bullet).start();  
                try {  
                    Thread.sleep(100);  
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
        }  
          
    }  
}  

class Enemy extends Plane implements Runnable{  
    static int imgX=539;  
    static int imgY=617;  
    static int imgW=50;  
    static int imgH=34;  
    boolean isLife=true;  
    Timer timer;  
    Random r;  
    Enemy enemy;  
    public Enemy() {  
        r=new Random();  
        timer=new Timer(); 
        timer.schedule(new EnemyTask(), 100);  
    }  
    public Enemy(int x,int y,int speed) {  
        super(x,y,speed);  
    }  
    class EnemyTask extends TimerTask{  
          
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            while(true){  
                int result = 4 + (int)(Math.random() * ((8 - 4) + 1));//�ٶȵ�ֵ  
                enemy=new Enemy(r.nextInt(350),0,result);  
                HuPanel.enemys.add(enemy);  
                new Thread(enemy).start();  
                try {  
                    Thread.sleep(500);  
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
        }  
          
    }  
    public void run() {  
        // TODO Auto-generated method stub  
        while(true){  
            y+=speed;  
            if(y>600){  
                HuPanel.enemys.remove(this);  
                break;  
            }  
            try {  
                Thread.sleep(50);  
            } catch (Exception e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    }  
      
}  
class Bullet implements Runnable{  
    int x;  
    int y;  
    static int imgX=71;  
    static int imgY=78;  
    static int imgW=7;  
    static int imgH=21;  
    int speed=8;  
    //static String sound="sound/fire_bullet.wav";  
    public Bullet(int x, int y) {  
        super();  
        this.x = x;  
        this.y = y;  
    }  
  
    public void run() {  
        // TODO Auto-generated method stub  
        while(true){  
            y-=speed;  

            for(int i=0;i<HuPanel.enemys.size();i++){  
                Enemy e=HuPanel.enemys.get(i);  
                if(x>=e.x&&x<=e.x+e.imgW&&y>=e.y&&y<=e.y+e.imgH){  
                    HuPanel.enemys.remove(e);  
                    Bomb bomb=new Bomb(e.x, e.y);  
                    HuPanel.bombs.add(bomb);  
                    try {  
                        //new SoundPlayer(bomb.sound).play();  
                    } catch (Exception e1) {  
                        e1.printStackTrace();  
                    }  
                }  
            }  
            if(y<0){  
                HuPanel.bullets.remove(this);  
                break;  
            }  
            try {  
                Thread.sleep(20);  
            } catch (InterruptedException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    }  
}  
class Bomb{  
    static int small1X = 272;  
    static int small1Y = 356;  
    static int small2X = 878;  
    static int small2Y = 704;  
    static int small3X = 935;  
    static int small3Y = 706;  
      
    static int smallW = 50;  
    static int smallH = 40;  
    int x;  
    int y;  
    int life=18;  
    //String sound="sound/small_plane_killed.wav";  
    public Bomb(int x, int y) {  
        this.x = x;  
        this.y = y;  
    }  

    public void lifeDown()  
    {  
        if(life>0)  
        {  
            life--;  
        }  
    }  
}  