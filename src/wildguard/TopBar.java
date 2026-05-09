package wildguard;

import javax.swing.*;
import java.awt.*;

public class TopBar extends JPanel
{
    private int score = 0;
    private int lives = 3;
    private int time = 0;
    private int level = 1;
    
    private Timer timer;
    
    public TopBar()
    {
        setPreferredSize(new Dimension(800, 40));
        setBackground(new Color(230, 230, 230));
        
        
        timer = new Timer(1000, e -> {
            time++;
            repaint();
        });
        timer.start();
    }
    
    public void pauseTimer()
    {
        timer.stop();
    }
    
    public void resumeTimer()
    {
        timer.start();
    }
    
    public void resetTimer()
    {
        time = 0;
    }
    
    public void setScore(int score)
    {
        this.score = score;
    }
    
    public int getScore()
    {
        return this.score;
    }
    
    public void setLives(int lives)
    {
        this.lives = lives;
    }
    
    public int getLives()
    {
        return this.lives;
    }
    
    public void setTime(int time)
    {
        this.time = time;
    }
    
    public int getTime()
    {
        return this.time;
    }
    
    public void setLevel(int level)
    {
        this.level = level;
    }
    
    public int getLevel()
    {
        return this.level;
    }
    
    @Override
    protected void paintComponent(Graphics grphcs)
    {
        super.paintComponent(grphcs);
        
        grphcs.setColor(Color.BLACK);
        grphcs.setFont(new Font("Arial", Font.BOLD, 24));
        
        grphcs.drawString("Score: " + score, 20, 30);
        grphcs.drawString("Lives: " + lives , 200, 30);
        
        grphcs.drawString("Time: " + time, 360, 30);
        
        grphcs.drawString("Level: " + level, 500, 30);
    }
}
