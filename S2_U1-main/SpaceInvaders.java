
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SpaceInvaders  implements ActionListener, KeyListener
{
        JFrame f1;
        JPanel main, sub;
        SpaceGraphics g1;
        JButton b1,b2;
        int xdir, ydir, index, i;
        int shotDelay, fastShotDelay, deadMissile, deadFastMissile, totalShots, totalFastShots;
        boolean endgame, shotTaken, startGame, fastShotTaken; 
        Alien a1[]; 
        SpaceShip player;
        Missile[] missile, fastMissile;
    
        public SpaceInvaders() 
    { 
        endgame = false;
        startGame = false;
        shotTaken = false;
        fastShotTaken = false;
        
        missile = new Missile[50];
        fastMissile = new Missile[100];
        
        shotDelay = 100;
        fastShotDelay = 1000;
        deadMissile = 0;
        deadFastMissile = 0;
        totalShots = 0;
        totalFastShots = 0;
        
        a1 = new Alien[20];
        
        for(index = 0; index < a1.length; index++)
        {
            if (index < 10) {
                a1[index] = new Alien(index * 60 + 75, 10);
            } else {
                a1[index] = new Alien((index-10) * 60 + 45, 60);
            }
        }
        
        player = new SpaceShip(300,600);
                
        xdir = 0;
        ydir = 0;
        
        f1 = new JFrame("The Goverment Needs You Nerd");
          f1.setSize(700,700);
          f1.setResizable(false);
          f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
          
        Container c1 = f1.getContentPane();
          
        g1 = new SpaceGraphics(player,missile, fastMissile,shotTaken, fastShotTaken,a1);
        
        g1.addKeyListener(this);
          
        b1 =  new JButton("Start");
           b1.addActionListener(this);
        
         b2 =  new JButton("End");
           b2.addActionListener(this);
         
        sub = new JPanel(); 
          sub.add(b1);
          sub.add(b2);
                     
       main = new JPanel();
          main.setLayout(new BorderLayout());          
          main.setSize(600,600);
          main.add(g1,BorderLayout.CENTER);
          main.add(sub,BorderLayout.SOUTH);
        
        c1.add(main);
        f1.show(); 
        runGame();
    }
    
    public void runGame()
    {
        Thread runner = new Thread();
         
      while(endgame == false)
      {    
          
          
        /** Need this section of code to slow computer down.  and  Line 55*/   
         try 
          { 
            runner.sleep(5); 
           }
          catch(InterruptedException e) {}    
       /** Need this section of code to slow computer down. */
       
       if (startGame == true) {
           
            if (shotTaken == true) {
                for (index = 0; index < a1.length; index++) {
                    for (i = 0; i < totalShots; i++) {
                        if(missile[i].getX() >= a1[index].getX() - 10 && missile[i].getX() <= a1[index].getX() + 20 && missile[i].getY() <= a1[index].getY() + 20 && a1[index].isDestroyed() == false)
                        { 
                            a1[index].destroyAlien();  
                            player.addScore(10);
                        }
                    }
                }
            
                for (index = 0; index < totalShots; index++) {
                    missile[index].moveMissile(0,-1);
                }
                g1.updateMissileLocation(missile, fastMissile, totalShots, totalFastShots, shotTaken, fastShotTaken);
            }
                      
            if (fastShotTaken == true) {
                for (index = 0; index < a1.length; index++) {
                    for (i = 0; i < totalFastShots; i++) {
                        if(fastMissile[i].getX() >= a1[index].getX() - 10 && fastMissile[i].getX() <= a1[index].getX() + 20 && fastMissile[i].getY() <= a1[index].getY() + 20 && a1[index].isDestroyed() == false)
                        { 
                            a1[index].destroyAlien();  
                            player.addScore(10);
                        }
                    }
                }
        
                for (index = 0; index < totalFastShots; index++) {
                    fastMissile[index].moveMissile(0,-3);
                }
                g1.updateMissileLocation(missile, fastMissile, totalShots, totalFastShots, shotTaken, fastShotTaken);
            }
        
            shotDelay++;
            fastShotDelay++;
           
            if (player.getX() < 0) {  
                xdir = 0; 
                player.setShip(0, player.getY());
            }
           
            if (player.getX() > 665) {  
                xdir = 0; 
                player.setShip(665, player.getY());   
            }
            player.moveShip(xdir, ydir);
            
            g1.updateAlien(a1);        
            g1.updatePlayerLocation(player, endgame);
            g1.repaint();
       }
      }         //end of while
     }          //end of runGame() method
    
    public void actionPerformed (ActionEvent event)
    {
        if (event.getSource() == b1)
        {
           g1.requestFocus();
           startGame = true;
         }
         
        if (event.getSource() == b2)
        {
           endgame = true;
         }
         
         
    }
    
    public void keyPressed(KeyEvent evt)
    {
       
           if(evt.getKeyCode() == 37)       //left arrow
           {
              xdir = -1;
              ydir = 0;
           }
           if(evt.getKeyCode() == 39)      //right arrow
           {
              xdir = 1;
              ydir = 0;
           }
            
           if(evt.getKeyCode() == 32 && shotDelay > 100)      // spacebar
           {
            if (deadMissile == totalShots) deadMissile = 0;
            try {
                if (missile[deadMissile].getY() < 0) {
                    missile[deadMissile].setMissile(player.getX()+5, player.getY()-5);
                    deadMissile++;
                } else {
                    missile[totalShots] = new Missile(player.getX()+5, player.getY()-5);
                    totalShots++; 
                }
            } catch (NullPointerException e) {
                missile[totalShots] = new Missile(player.getX()+5, player.getY()-5);
                totalShots++; 
            }
            shotDelay = 0;
            shotTaken = true;
            }
           
            if(evt.getKeyCode() == 90 && fastShotDelay > 150)      // spacebar
           {
               if (deadFastMissile == totalFastShots) deadFastMissile = 0;
               try {
                   if (fastMissile[deadFastMissile].getY() < 0) {
                       fastMissile[deadMissile].setMissile(player.getX()+5, player.getY()-5);
                       deadFastMissile++;
                    } else {
                        fastMissile[totalFastShots] = new Missile(player.getX()+5, player.getY()-5);
                        totalFastShots++;
                    }
               } catch (NullPointerException e) {
                    fastMissile[totalFastShots] = new Missile(player.getX()+5, player.getY()-5);
                    totalFastShots++;
               }
               fastShotDelay = 0; 
               fastShotTaken = true;
           } 
              
        }
    public void keyReleased(KeyEvent evt)
    {}
    public void keyTyped(KeyEvent evt)
    {}
}