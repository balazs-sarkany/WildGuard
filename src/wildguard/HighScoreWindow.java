package wildguard;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class HighScoreWindow extends JFrame
{
    public HighScoreWindow()
    {
        setTitle("Leaderboard");
        setSize(500, 600);
        setLocationRelativeTo(null);
        
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.BOLD, 14));
        
        JScrollPane scroll = new JScrollPane(area);
        add(scroll);
        
        try
        {
            Database db = new Database();
            ArrayList<HighScore> list = db.getHighScores();
            
            for (HighScore h : list)
            {
                area.append(h.getName() + " - Score: " + h.getScore() + " - Time: " + h.getTimeSeconds() + " sec\n");
            }
        }
        catch (SQLException e)
        {
            area.setText("Error while loading Leaderboard");
        }
        setVisible(true);
    }
}