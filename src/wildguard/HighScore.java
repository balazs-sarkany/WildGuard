package wildguard;

import java.sql.Timestamp;

public class HighScore
{
    private int id;
    private String name;
    private int score;
    private int timeSeconds;
    private Timestamp timestamp;
    
    public HighScore(int id, String name, int score, int timeSeconds, Timestamp timestamp)
    {
        this.id = id;
        this.name = name;
        this.score = score;
        this.timeSeconds = timeSeconds;
        this.timestamp = timestamp;
    }
    
    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getScore()
    {
        return score;
    }

    public int getTimeSeconds()
    {
        return timeSeconds;
    }

    public Timestamp getTimestamp()
    {
        return timestamp;
    }

}
