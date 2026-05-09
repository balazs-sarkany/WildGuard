package wildguard;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class Database
{
    private Connection connection;
    private PreparedStatement insertStatement;
    
    public Database() throws SQLException
    {
        Properties props = new Properties();
        props.put("user", "yogi");
        props.put("password", "yogi123");
        props.put("serverTimezone", "UTC");
        
        String url = "jdbc:mysql://localhost:3306/yogi_bear";
        
        connection = DriverManager.getConnection(url, props);
        
        String insertQuery = "INSERT INTO highscores (player_name, score, time_seconds) VALUES (?, ?, ?)";
        
        insertStatement = connection.prepareStatement(insertQuery);
    }
    
    public void saveScore(String name, int score, int timeSeconds) throws SQLException
    {
        insertStatement.setString(1, name);
        insertStatement.setInt(2, score);
        insertStatement.setInt(3, timeSeconds);
        
        insertStatement.executeUpdate();
    }
    
    public ArrayList<HighScore> getHighScores() throws SQLException
    {
        String query = "SELECT * FROM highscores " + "ORDER BY score DESC, time_seconds ASC, timestamp ASC " + "LIMIT 10";
        
        ArrayList<HighScore> highScores = new ArrayList<>();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next())
        {
            int id = rs.getInt("id");
            String name = rs.getString("player_name");
            int score = rs.getInt("score");
            int time = rs.getInt("time_seconds");
            Timestamp ts = rs.getTimestamp("timestamp");

            highScores.add(new HighScore(id, name, score, time, ts));
        }
        return highScores;
    }   
}