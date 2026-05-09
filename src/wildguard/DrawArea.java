package wildguard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

public class DrawArea extends JPanel
{
    
    private TopBar topBar;
    private boolean gamePaused = false;
    
    // yogi
    private Image yogiImage;
    private int yogiX;
    private int yogiY;
    private int yogiWidth = 60;
    private int yogiHeight = 120;
    
    // drawarea
    private int drawAreaWidth = 985;
    private int drawAreaHeight = 700;
    
    // basket
    private Image basketImage;
    private List<Basket> baskets = new ArrayList<>();
    
    // ranger
    private Image rangerImage;
    private List<Ranger> rangers = new ArrayList<>();
    
    // obstacle
    private Image obstacleImage;
    private List<Obstacle> obstacles = new ArrayList<>();
    
    private int score = 0;
    
    private Level currentLevel;
    private int currentLevelIndex = 1;
    private final int maxLevels = 10;
    
    public DrawArea(TopBar topBar)
    {
        this.topBar = topBar;
        
        setPreferredSize(new Dimension(drawAreaWidth, drawAreaHeight));
        setBackground(Color.LIGHT_GRAY);
        
        yogiImage = new ImageIcon(getClass().getResource("/wildguard/images/yogi.jpg")).getImage();
        basketImage = new ImageIcon(getClass().getResource("/wildguard/images/basket.jpg")).getImage();
        rangerImage = new ImageIcon(getClass().getResource("/wildguard/images/ranger.jpg")).getImage();
        obstacleImage = new ImageIcon(getClass().getResource("/wildguard/images/obstacle.jpg")).getImage();


        getInputMap().put(KeyStroke.getKeyStroke("W"), "moveUp");
        getActionMap().put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveYogi(0, -10);
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke("S"), "moveDown");
        getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveYogi(0, +10);
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke("A"), "moveLeft");
        getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveYogi(-10, 0);
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke("D"), "moveRight");
        getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveYogi(+10, 0);
            }
        });
        
        
        
        new Timer(25, e -> updateRangers()).start();
        loadLevel(currentLevelIndex);
    }
    
    
    private void loadLevel(int index)
    {
        try
        {
            currentLevel = new Level("data/levels/level" + index + ".txt");
            startLevel(currentLevel);
        }
        catch (IOException ex)
        {
            System.out.println("Couldn't load level: " + ex.getMessage());
        }
    }
    
    private void startLevel(Level lvl)
    {
        yogiX = lvl.yogiStartX;
        yogiY = lvl.yogiStartY;
        
        baskets = lvl.baskets;
        rangers = lvl.rangers;
        obstacles = lvl.obstacles;
        
        topBar.setLevel(currentLevelIndex);
        topBar.repaint();
        
        repaint();
    }
    
    
    private void moveYogi(int dx, int dy)
    {
        if (gamePaused) return;
        
        int oldX = yogiX;
        int oldY = yogiY;

        yogiX += dx;
        yogiY += dy;

        
        if (yogiX < 0) yogiX = 0;
        if (yogiY < 0) yogiY = 0;
        if (yogiX > drawAreaWidth - yogiWidth) yogiX = drawAreaWidth - yogiWidth;
        if (yogiY > drawAreaHeight - yogiHeight) yogiY = drawAreaHeight - yogiHeight;

        
        if (collidesWithObstacle())
        {
            yogiX = oldX;
            yogiY = oldY;
        }
        
        checkBaskets();
        
        yogiSpotted();

        repaint();
    }
    
    private void checkBaskets()
    {
        boolean allCollectedBefore = allBasketsCollected();
        
        for (Basket b : baskets)
        {
            boolean hitX = yogiX < b.x + b.width && yogiX + yogiWidth > b.x;
            boolean hitY = yogiY < b.y + b.height && yogiY + yogiHeight > b.y;

            if (!b.collected && hitX && hitY)
            {
                b.collected = true;
                score++;
                topBar.setScore(score);
                topBar.repaint();
            }
        }
        if (!allCollectedBefore && allBasketsCollected())
        {
            levelCompleted();
        }
    }
    
    
    private boolean allBasketsCollected()
    {
        for (Basket b : baskets)
        {
            if (!b.collected) return false;
        }
        return true;
    }
    
    
    private void levelCompleted()
    {
        currentLevelIndex++;
        
        if (currentLevelIndex > maxLevels)
        {
            gamePaused = true;
            topBar.pauseTimer();
            
            JOptionPane.showMessageDialog(this, "Game Completed!");
            
            askNameAndShowHighscores();
            
            resetToLevel1();
            gamePaused = false;
            topBar.resumeTimer();
            return;
        }
        topBar.setLevel(currentLevelIndex);
        loadLevel(currentLevelIndex);
    }
    
    
    private void askNameAndShowHighscores()
    {
        String name = JOptionPane.showInputDialog(this, "Enter name: ");
        
        if (name == null)
        {
            new HighScoreWindow();
            return;
        }
        
        if (name.trim().isEmpty())
        {
            name = "Anonymous";
        }
        
        try
        {
            Database db = new Database();
            db.saveScore(name.trim(), score, topBar.getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        new HighScoreWindow();
    }
    
    
    private boolean collidesWithObstacle()
    {
        for (Obstacle o : obstacles)
        {
            boolean hitX = yogiX < o.x + o.width && yogiX + yogiWidth > o.x;
            boolean hitY = yogiY < o.y + o.height && yogiY + yogiHeight > o.y;

            if (hitX && hitY)
            {
                return true;
            }
        }
        return false;
    }
    
    
    
    private void updateRangers()
    {
        if (gamePaused) return;
        for (Ranger r : rangers)
        {
            if (r.vertical)
            {
                if (r.movingPositiveDirection) r.y += r.speed;
                else r.y -= r.speed;
                
                if (r.y <= 0) r.movingPositiveDirection = true;
                if (r.y >= drawAreaHeight - r.height) r.movingPositiveDirection = false;
            }
            else
            {
                if (r.movingPositiveDirection) r.x += r.speed;
                else r.x -= r.speed;
                
                if (r.x <= 0) r.movingPositiveDirection = true;
                if (r.x >= drawAreaWidth - r.width) r.movingPositiveDirection = false;
            }
        }
        yogiSpotted(); 
        repaint();
    }
    
    
    private void yogiSpotted()
    {
        if (gamePaused) return;
        
        int detectionRange = yogiWidth + currentLevelIndex * 10;
        
        for (Ranger r : rangers)
        {
            
            boolean hitX = yogiX < r.x + r.width && yogiX + yogiWidth > r.x;
            boolean hitY = yogiY < r.y + r.height && yogiY + yogiHeight > r.y;
          
            if (hitX && hitY)
            {
                damage();
                return;
            }
            
            
            if (r.vertical)
            {
                if (r.movingPositiveDirection)
                {
                    boolean spotted = yogiY > r.y + r.height && yogiY < r.y + r.height + detectionRange;
                    boolean inFront = yogiX < r.x + r.width && yogiX + yogiWidth > r.x;
                    
                    if (spotted && inFront)
                    {
                        damage();
                        return;
                    }
                }
                
                else
                {
                    boolean spotted = yogiY + yogiHeight < r.y && yogiY + yogiHeight > r.y - detectionRange;
                    boolean inFront = yogiX < r.x + r.width && yogiX + yogiWidth > r.x;
                    
                    if (spotted && inFront)
                    {
                        damage();
                        return;
                    }
                }
            }
            else
            {
                if (r.movingPositiveDirection)
                {
                    boolean spotted = yogiX > r.x + r.width && yogiX < r.x + r.width + detectionRange;
                    boolean inFront = yogiY < r.y + r.height && yogiY + yogiHeight > r.y;
                    
                    if (spotted && inFront)
                    {
                        damage();
                        return;
                    }
                }
                else
                {
                    boolean spotted = yogiX + yogiWidth < r.x && yogiX + yogiWidth > r.x - detectionRange;
                    boolean inFront = yogiY < r.y + r.height && yogiY + yogiHeight > r.y;
                    
                    if (spotted && inFront)
                    {
                        damage();
                        return;
                    }
                }
            }
        }
    }
    
    
    private void damage()
    {
        if (gamePaused) return;
        
        int lives = topBar.getLives();
        lives--;
        topBar.setLives(lives);
        topBar.repaint();
        
        yogiX = currentLevel.yogiStartX;
        yogiY = currentLevel.yogiStartY;
        
        if (lives <= 0)
        {
            gamePaused = true;
            topBar.pauseTimer();
            
            JOptionPane.showMessageDialog(null, "Game Over!");
            
            askNameAndShowHighscores();
            
            resetToLevel1();
            gamePaused = false;
            topBar.resumeTimer();
        }
    }
    
    
    
    public void resetToLevel1()
    {
        currentLevelIndex = 1;
        score = 0;
        
        topBar.setLives(3);
        topBar.setTime(0);
        topBar.setScore(0);
        topBar.setLevel(1);
        topBar.repaint();
        
        loadLevel(currentLevelIndex);
        
        yogiX = currentLevel.yogiStartX;
        yogiY = currentLevel.yogiStartY;
        
        repaint();
    }
    
    
    private void drawDetectionRange(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(255, 255, 0, 60));

        int range = yogiWidth + currentLevelIndex * 10;

        for (Ranger r : rangers)
        {
            if (r.vertical)
            {
                g2.fillRect(r.x, r.y + (r.movingPositiveDirection ? r.height : -range), r.width, range);
            }
            else
            {
                g2.fillRect(r.x + (r.movingPositiveDirection ? r.width : -range), r.y, range, r.height);
            }
        }
    }
    

    
    
    @Override
    protected void paintComponent(Graphics grphcs)
    {
        super.paintComponent(grphcs);
        
        grphcs.drawImage(yogiImage, yogiX, yogiY, yogiWidth, yogiHeight, null);
        
        for (Basket b : baskets)
        {
            if (!b.collected)
            {
                grphcs.drawImage(basketImage, b.x, b.y, b.width, b.height, null);
            }
        }
        
        for (Ranger r : rangers)
        {
            grphcs.drawImage(rangerImage, r.x, r.y, r.width, r.height, null);
        }
        
        for (Obstacle o : obstacles)
        {
            grphcs.drawImage(obstacleImage, o.x, o.y, o.width, o.height, null);
        }
        drawDetectionRange(grphcs);
    }
}