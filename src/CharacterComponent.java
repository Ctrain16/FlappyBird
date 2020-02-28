import javax.swing.*;
import java.awt.*;
/**
 * Creates Character component of game
 * @author Cal Trainor
 * 2019
 */
public class CharacterComponent extends JComponent
{
    private final int CHARACTER_X;
    private final double FALLING_RATE = 0.05;
    private double characterY;

    private double vertSpeed;
    private int delta = 1;
    private ImageIcon image;

    /**
     * Constructor
     * @param int
     * @param int
     */
    public CharacterComponent(int x, int y)
    {
        CHARACTER_X = x;
        characterY = y;

        //imports image and scales to wanted size
        image = new ImageIcon("src/FlappyBird.png");
        Image scaleImage = image.getImage();
        scaleImage = scaleImage.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        image = new ImageIcon(scaleImage);
    }

    /**
     * Draws character
     * @param Graphics object
     * @return void
     */
    public void draw(Graphics g)
    {
        image.paintIcon(this, g, CHARACTER_X, (int) characterY);
    }

    /**
     * Moves character
     * @param double containg vertSpeed
     */
    public void moveCharacter(double p_vertSpeed)
    {
        characterY += p_vertSpeed * delta;
        p_vertSpeed += FALLING_RATE * delta;

        vertSpeed = p_vertSpeed;

        super.repaint();
    }

    /**
     * Accessor for vertSpeed
     * @param none
     * @return double value containing vertSpeed
     */
    public double getVertSpeed()
    {
        return vertSpeed;
    }

    /**
     * Accessor for characterY
     * @param none
     * @return int value containing characterY
     */
    public int getCharacterY()
    {
        return (int) characterY;
    }
}