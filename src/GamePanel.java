import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
/**
 * Creates Pipe dodger game
 * @author Cal Trainor
 * 2019
 */
public class GamePanel extends JPanel
{
    private static final int CHARACTER_X = 50;
    private static final int CHARACTER_STARTING_Y = 200;
    private static final int UPDATE_DELAY = 15;

    private PipeComponent pipe;
    private PipeComponent nextPipe;
    private PrintWriter scoreWriter;
    private File scoreFile = new File("src/ScoreKeeper.txt");

    private int highScore = 0;
    private int score = 0;
    private boolean gameStarted, gameOver, mouseClicked;

    private ArrayList<PipeComponent> pipes = new ArrayList<PipeComponent>();
    private CharacterComponent character;
    private Timer pipeTimer, startGame;

    class MouseClickListener extends MouseAdapter
    {
        /**
         * sets mouseClicked boolean to true
         * @param MouseEvent
         * @return void
         */
        public void mousePressed(MouseEvent event)
        {
            mouseClicked = true;
            if(gameOver)
            {
                mouseClicked = false;
                gameOver = false;
                gameStarted = false;
                score = 0;
                restartGame();
                pipeTimer.stop();
                startGame.start();
            }
        }
    }

    class TimerListener implements ActionListener
    {
        /**
         * Performs essential functions of game
         * @param ActionEvent
         * @return void
         */
        public void actionPerformed(ActionEvent event)
        {
            //updates character position
            if(mouseClicked)
            {
                character.moveCharacter(-2);
            }
            else
            {
                character.moveCharacter(character.getVertSpeed());
            }

            boolean addAnotherPipe = movePipes();

            if(addAnotherPipe)
            {
                addPipe();
            }

            mouseClicked = false;
            updateScore();
            checkForCollisions();

            repaint();
        }
    }

    class StartListener implements ActionListener
    {
        /**
         * Sets game in motion
         * @param ActionEvent
         * @return void
         */
        public void actionPerformed(ActionEvent ae)
        {
            if(mouseClicked)
            {
                startGame.stop();
                gameStarted = true;
                StartGame();
            }
            mouseClicked = false;
        }
    }

    class ReplayListener extends KeyAdapter
    {
        /**
         * Listens for user input
         * @param KeyEvent
         * @return void
         */
        public void keyPressed (KeyEvent event)
        {
            char key = event.getKeyChar();
            if(key == 'q')
            {
                scoreWriter.println(highScore);
                scoreWriter.close();
                System.exit(0);
            }
        }
    }

    /**
     * Constructor
     */
    public GamePanel() throws IOException
    {
        setFocusable(true);

        Scanner scan = new Scanner(scoreFile);
        if(scan.hasNext())
        {
            highScore = Integer.parseInt(scan.next());
        }

        scoreWriter = new PrintWriter(scoreFile);
        character = new CharacterComponent(CHARACTER_X, CHARACTER_STARTING_Y);
        addPipe();

        KeyListener listenForKey = new ReplayListener();
        addKeyListener(listenForKey);
        MouseListener listenToMouse = new MouseClickListener();
        addMouseListener(listenToMouse);

        ActionListener startListen = new StartListener();
        startGame = new Timer(UPDATE_DELAY, startListen);
        startGame.start();

        gameStarted = false;
    }

    /**
     * Starts game
     * @param none
     * @return void
     */
    public void StartGame()
    {
        ActionListener timeListener = new TimerListener();
        pipeTimer = new Timer(UPDATE_DELAY, timeListener);
        pipeTimer.start();
    }

    /**
     * Adds a new pipe to ArrayList
     * @param none
     * @return void
     */
    public void addPipe()
    {
        pipe = new PipeComponent();
        pipes.add(pipe);
    }

    /**
     * Moves all pipes in ArrayList and signifies if a new pipe needs to be generated
     * @param none
     * @return boolean value
     */
    public boolean movePipes()
    {
        boolean addAnotherPipe = false;
        for(int i = 0; i < pipes.size(); i++)
        {
            PipeComponent currentPipe = pipes.get(i);
            currentPipe.movePipe();

            if(i + 1 == pipes.size())
            {
                int currentPipeX = currentPipe.getPipeX();
                if(currentPipeX < 400)
                {
                    addAnotherPipe = true;
                }
            }
        }
        repaint();
        return addAnotherPipe;
    }

    /**
     * Paints game
     * @param Graphics object
     * @return void
     */
    public void paintComponent (Graphics g)
    {
        super.paintComponent (g);

        g.setColor(new Color(51, 204, 255));//light shade of blue
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.GREEN);

        //goes through ArrayList of pipes and draws each one
        for (PipeComponent currentPipe : pipes)
        {
            currentPipe.draw(g);
        }

        character.draw(g);

        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 5, 20);
        g.drawString("HighScore: " + highScore, 500, 20);
        if(!gameStarted)
        {
            g.drawString("Click to Start", 250, 180);
        }
        if(gameOver)
        {
            g.drawString("Game Over", 240, 160);
            g.drawString("Score: " + score, 250, 180);
            g.drawString("High Score: " + highScore,232 , 200);
            g.drawString("Click to play again", 215, 220);
            g.drawString("Press Q to quit", 230, 240);
        }
    }

    /**
     * Updates score and highscore
     * @param none
     * @return void
     */
    public void updateScore()
    {
        nextPipe = pipes.get(score);
        if(CHARACTER_X > nextPipe.getPipeCapX() + 35)
        {
            score++;
        }
        if(score > highScore)
        {
            highScore = score;
        }
    }

    /**
     * Checks for collisions
     * @param none
     * @return void
     */
    public void checkForCollisions()
    {
        //checks if character goes off panel
        if(character.getCharacterY() + 30 >= 380 || character.getCharacterY() <= 0)
        {
            pipeTimer.stop();
            endGameSequence();
        }

        //checks for collisions with pipe
        if(CHARACTER_X + 30 >= nextPipe.getPipeX())
        {
            if(character.getCharacterY() <= nextPipe.getTopPipeY() + 400)
            {
                pipeTimer.stop();
                endGameSequence();
            }
            else if(character.getCharacterY() + 30 >= nextPipe.getBottomPipeY())
            {
                pipeTimer.stop();
                endGameSequence();
            }
        }

        //checks for collisions with pipe cap
        if(CHARACTER_X + 30 >= nextPipe.getPipeCapX())
        {
            if(character.getCharacterY() <= nextPipe.getTopPipeY() + 400 && character.getCharacterY() >= nextPipe.getTopPipeY() + 350)
            {
                pipeTimer.stop();
                endGameSequence();
            }
            if(character.getCharacterY() + 30 >= nextPipe.getBottomPipeY() && character.getCharacterY() <= nextPipe.getBottomPipeY() + 20)
            {
                pipeTimer.stop();
                endGameSequence();
            }
        }
    }

    /**
     * Commences the end game sequence
     * @param none
     * @return void
     */
    public void endGameSequence()
    {
        gameOver = true;
        repaint();
    }

    /**
     * Restarts game
     * @param none
     * @return void
     */
    public void restartGame()
    {
        pipes.removeAll(pipes);
        character = new CharacterComponent(CHARACTER_X, CHARACTER_STARTING_Y);
        addPipe();
        repaint();
    }
}