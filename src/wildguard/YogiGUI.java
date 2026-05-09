package wildguard;

import javax.swing.*;
import java.awt.*;


public class YogiGUI
{
    private TopBar topBar;
    private DrawArea drawArea;
    
    public YogiGUI()
    {
        JFrame frame = new JFrame("Yogi Bear");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setSize(1000, 800);
        
        frame.setLayout(new BorderLayout());
        
        topBar = new TopBar();
        drawArea = new DrawArea(topBar);
        
        frame.add(topBar, BorderLayout.NORTH);
        frame.add(drawArea, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> resetGame());
        menu.add(newGameItem);
        
        
        JMenuItem highScoresItem = new JMenuItem("Leaderboard");
        highScoresItem.addActionListener(e -> new HighScoreWindow());
        menu.add(highScoresItem);
        
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        
        
        frame.setVisible(true);
    }
    
    private void resetGame()
    {
        topBar.setScore(0);
        topBar.setLives(3);
        topBar.setTime(0);
        
        drawArea.resetToLevel1();
        
        topBar.repaint();
    }
}
