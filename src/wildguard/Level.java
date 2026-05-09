package wildguard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Level
{
    public int yogiStartX;
    public int yogiStartY;

    public ArrayList<Basket> baskets;
    public ArrayList<Obstacle> obstacles;
    public ArrayList<Ranger> rangers;
    
    private final int BASKET_WIDTH = 60;
    private final int BASKET_HEIGHT = 60;
    private final int OBSTACLE_WIDTH = 120;
    private final int OBSTACLE_HEIGHT = 120;
    private final int RANGER_WIDTH = 60;
    private final int RANGER_HEIGHT = 100;


    public Level(String levelPath) throws IOException
    {
        loadLevel(levelPath);
    }
    
    public void loadLevel(String levelPath) throws IOException
    {
        baskets = new ArrayList<>();
        obstacles = new ArrayList<>();
        rangers = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(levelPath));

        String line;
        while ((line = br.readLine()) != null)
        {
            String[] parts = line.split("\\s+");
            String type = parts[0];

            switch (type)
            {
                case "YOGI":
                    yogiStartX = Integer.parseInt(parts[1]);
                    yogiStartY = Integer.parseInt(parts[2]);
                    break;

                case "BASKET":
                    baskets.add(new Basket(
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]),
                            BASKET_WIDTH,
                            BASKET_HEIGHT
                    ));
                    break;

                case "OBSTACLE":
                    obstacles.add(new Obstacle(
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]),
                            OBSTACLE_WIDTH,
                            OBSTACLE_HEIGHT
                    ));
                    break;
                    
                case "RANGER":
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    String dir = parts[3];
                    int speed = Integer.parseInt(parts[4]);

                    boolean vertical = dir.equals("V");

                    rangers.add(new Ranger(x, y, RANGER_WIDTH, RANGER_HEIGHT, speed, vertical));
                    break;

                default:
                    System.out.println("Unknown row in the level file: " + type);
                    break;
            }
        }
        br.close();
    }
}