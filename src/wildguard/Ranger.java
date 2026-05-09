package wildguard;

public class Ranger
{
    public int x;
    public int y;
    public int width;
    public int height;
    public int speed;
    public boolean vertical;
    public boolean movingPositiveDirection = true;
    
    public Ranger(int x, int y, int width, int height, int speed, boolean vertical)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.vertical = vertical;
    }
}
