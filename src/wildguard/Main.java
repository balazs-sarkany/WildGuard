package wildguard;

import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            Level lvl = new Level("data/levels/level1.txt");
            System.out.println("Yogi start position: x=" + lvl.yogiStartX + ", y=" + lvl.yogiStartY);
            System.out.println("Number of baskets: " + lvl.baskets.size());
            System.out.println("Number of Rangers: " + lvl.rangers.size());
            System.out.println("Number of Obstacles: " + lvl.obstacles.size());
            System.out.println();
        }
        catch (Exception e)
        {
            System.out.println("Error while loading the level");
        }
        
        
        // resetToLevel1()
        TopBar topBar = new TopBar();
        DrawArea drawArea = new DrawArea(topBar);
        
        topBar.setScore(5);
        topBar.setLives(2);
        topBar.setTime(4);
        topBar.setLevel(3);
        
        
        System.out.println("Points: " + topBar.getScore() + " expected: 5");
        System.out.println("Lives: " + topBar.getLives() + " expected: 2");
        System.out.println("Time: " + topBar.getTime() + " expected: 4");
        System.out.println("Level: " + topBar.getLevel() + " expected: 3");
        System.out.println();
        
        drawArea.resetToLevel1();
        System.out.println("resetToLevel1();");
        
        System.out.println("Points: " + topBar.getScore() + " expected: 0");
        System.out.println("Lives: " + topBar.getLives() + " expected: 3");
        System.out.println("Time: " + topBar.getTime() + " expected: 0");
        System.out.println("Level: " + topBar.getLevel() + " expected: 1");
        System.out.println();
        
        // HighScore test
        HighScore h = new HighScore(1, "Teszt", 5, 32, new java.sql.Timestamp(System.currentTimeMillis()));
        System.out.println("Name: " + h.getName() + ", Score: " + h.getScore() + ", Time: " + h.getTimeSeconds());
        System.out.println();
        
        // Collision test
        Obstacle obs = new Obstacle(100, 100, 50, 50);

        int yogiX = 110;
        int yogiY = 110;
        int yogiW = 60;
        int yogiH = 120;
        
        
        boolean hit = (yogiX < obs.x + obs.width && yogiX + yogiW > obs.x && yogiY < obs.y + obs.height && yogiY + yogiH > obs.y);

        System.out.println("Collision: " + hit + " expected: true");
        System.out.println();
        
        
        // allBasketsCollected test
        ArrayList<Basket> baskets = new ArrayList<>();
        baskets.add(new Basket(0,0,40,40));
        baskets.add(new Basket(50,0,40,40));

        baskets.get(0).collected = true;
        baskets.get(1).collected = false;

        boolean all = true;
        for (Basket b : baskets)
        {
            if (!b.collected) all = false;
        }

        System.out.println("Collected all baskets: " + all + " expected: false");

        baskets.get(1).collected = true;

        all = true;
        for (Basket b : baskets)
        {
            if (!b.collected) all = false;
        }

        System.out.println("Collected all baskets: " + all + " expected: true");
        
        YogiGUI yogiGUI = new YogiGUI();
    }
}