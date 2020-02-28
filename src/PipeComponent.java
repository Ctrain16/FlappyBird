import javax.swing.*;
import java.awt.*;
/**
 * Creates pipe components of game
 * @author Cal Trainor
 * 2019
 */
public class PipeComponent extends JComponent
{
    private static final int PIPE_WIDTH = 30;
    private static final int PIPE_HEIGHT = 400;
    private static final int dx = 2;

    private int [] topPipeX = {5, 35, 35, 40, 40, 0, 0, 5};
    private int [] topPipesY = {0, 0, 380, 380, 400, 400, 380, 380};
    private int [] bottomPipeX = {0, 40, 40, 35, 35, 5, 5, 0};
    private int [] bottomPipesY = {0, 0, 20, 20, 400, 400, 20, 20};

    private Polygon topPipePoly, bottomPipePoly;

    private int pipeX;
    private int topPipeY;
    private int pipeGap;
    private int bottomPipeY;

    /**
     * Constructor
     */
    public PipeComponent()
    {
        pipeX = (int)(Math.random() * 100) + 600;
        pipeGap = (int)(Math.random() * 100) + 105;
        topPipeY = (int)(Math.random() * (pipeGap - 400) - (pipeGap));
        bottomPipeY = topPipeY + 370 + pipeGap;//needs to be less than 370

        topPipePoly = new Polygon(topPipeX, topPipesY, topPipeX.length);
        bottomPipePoly = new Polygon(bottomPipeX, bottomPipesY, bottomPipeX.length);

        topPipePoly.translate(pipeX, topPipeY);
        bottomPipePoly.translate(pipeX, bottomPipeY);
    }

    /**
     * Draws Pipe
     * @param Graphics object
     * @return void
     */
    public void draw(Graphics g)
    {
        //moves pipe everytime it is painted
        topPipePoly.translate(-2, 0);
        bottomPipePoly.translate(-2, 0);

        g.fillPolygon(topPipePoly);
        g.fillPolygon(bottomPipePoly);
    }

    /**
     * Moves pipe
     * @param none
     * @return void
     */
    public void movePipe()
    {
        pipeX = pipeX - dx;
        super.repaint();
    }

    /**
     * Accessor for pipeX
     * @param none
     * @return int containg pipeX
     */
    public int getPipeX()
    {
        return pipeX;
    }

    /**
     * Accessor for pipCapX
     * @param none
     * @return int containing pipeCapX value
     */
    public int getPipeCapX()
    {
        return pipeX - 5;
    }

    /**
     * Accessor for topPipeY
     * @param none
     * @return int containing topPipeY
     */
    public int getTopPipeY()
    {
        return topPipeY;
    }

    /**
     * Accessor for bottomPipeY
     * @param none
     * @return int containing bottomPipeY
     */
    public int getBottomPipeY()
    {
        return bottomPipeY;
    }
}