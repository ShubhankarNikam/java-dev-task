package SnakeGamePackage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;


enum Direction {
    UP, DOWN, LEFT, RIGHT
}


class Snake {
    private LinkedList<Point> body;
    private Direction direction;

    public Snake() {
        body = new LinkedList<>();
        body.add(new Point(SnakeGame.getNumCols() / SnakeGame.getNumCols(), SnakeGame.getNumRows() / SnakeGame.getNumRows()));
        direction = Direction.RIGHT;
    }

    public void move() {
        Point head = getHead();
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }

        body.addFirst(newHead);
        body.removeLast();
    }

    public void grow() {
        Point head = getHead();
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }

        body.addFirst(newHead);
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return body.getFirst();
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}



public class SnakeGame extends JPanel implements KeyListener, Runnable {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int CELL_SIZE = 20;
    private static final int NUM_ROWS = HEIGHT / CELL_SIZE;
    private static final int NUM_COLS = WIDTH / CELL_SIZE;

    private int[][] grid;
    private Snake snake;
    private Point food;
    private boolean isGameOver;
    private int score;

    public SnakeGame() {
        grid = new int[getNumRows()][getNumCols()];
        snake = new Snake();
        isGameOver = false;
        score = 0;

        setPreferredSize(new Dimension(WIDTH+20, HEIGHT+20));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        initializeGame();
    }

    private void initializeGame() {
        spawnFood();
        Thread thread = new Thread(this);
        thread.start();
    }

    private void spawnFood() {
        Random random = new Random();
        int foodX = random.nextInt(getNumCols());
        int foodY = random.nextInt(getNumRows());
        food = new Point(foodX, foodY);
    }

    private void move() {
        snake.move();
        checkCollisions();
    }

    private void checkCollisions() {
        Point head = snake.getHead();

        if (head.x < 0 || head.x >= getNumCols() || head.y < 0 || head.y >= getNumRows()) {
            isGameOver = true;
            return;
        }

        for (Point segment : snake.getBody()) {
            if (segment.equals(head) && !segment.equals(snake.getBody().get(0))) {
                isGameOver = true;
                return;
            }
        }

        if (head.equals(food)) {
            snake.grow();
            spawnFood();
            score++;
        }
    }

    private void gameOver() {
        isGameOver = true;
    }

    private void draw(Graphics g) {
        g.setColor(Color.WHITE);
        for (Point segment : snake.getBody()) {
            g.fillRect(segment.x * CELL_SIZE, segment.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE+1, CELL_SIZE);

        g.setColor(Color.RED);
        g.drawString("Score: " + score, 10, 20);

        if (isGameOver) {
            g.drawString("Game Over!", WIDTH / 2 - 40, HEIGHT / 2);
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void run() {
        while (!isGameOver) {
            move();
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameOver();
    }

    public void keyTyped(KeyEvent e) {}


    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP && snake.getDirection() != Direction.DOWN) {
            snake.setDirection(Direction.UP);
        } else if (key == KeyEvent.VK_DOWN && snake.getDirection() != Direction.UP) {
            snake.setDirection(Direction.DOWN);
        } else if (key == KeyEvent.VK_LEFT && snake.getDirection() != Direction.RIGHT) {
            snake.setDirection(Direction.LEFT);
        } else if (key == KeyEvent.VK_RIGHT && snake.getDirection() != Direction.LEFT) {
            snake.setDirection(Direction.RIGHT);
        }
    }

    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

	public static int getNumCols() {
		return NUM_COLS;
	}

	public static int getNumRows() {
		return NUM_ROWS;
	}
}


