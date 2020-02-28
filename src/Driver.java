import javax.swing.*;
import java.awt.*;
import java.io.*;
/**
 * Viewer for Game
 * @author Cal Trainor
 * 2019
 */
public class Driver
{
    public static void main(String[] args) throws IOException
    {
        JFrame frame = new JFrame();
        frame.setTitle("Ctrain Studios Presents : Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(600,400);

        frame.add(new GamePanel());
        frame.setVisible(true);
    }
}
