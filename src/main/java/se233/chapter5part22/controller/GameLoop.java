package se233.chapter5part22.controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.chapter5part22.Launcher;
import se233.chapter5part22.model.Direction;
import se233.chapter5part22.model.Food;
import se233.chapter5part22.model.Snake;
import se233.chapter5part22.view.GameStage;

public class GameLoop implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(GameLoop.class);
    private GameStage gameStage;
    private Snake snake;
    private Food food;
    private Food specialFood;
    private float interval = 1000.0f / 10;
    private int score;
    private boolean running;
    private boolean testMode = false;
    public GameLoop(GameStage gameStage, Snake snake, Food food, Food specialFood) {
        this.gameStage = gameStage;
        this.snake = snake;
        this.food = food;
        this.specialFood = specialFood;
        score = 0;
        running = true;
    }
    public void keyProcess() {
        KeyCode curKey = gameStage.getKey();
        Direction curDirection = snake.getDirection();
        if ((curDirection == Direction.UP && curKey == KeyCode.DOWN) || (curDirection == Direction.DOWN && curKey == KeyCode.UP) || (curDirection == Direction.LEFT && curKey == KeyCode.RIGHT)
                || (curDirection == Direction.RIGHT && curKey == KeyCode.LEFT)) {
            System.out.println("CANNOT MOVE TO THE OPPOSITE DIRECTION");
        } else {
            if (curKey == KeyCode.UP) {
                snake.setDirection(Direction.UP);
            } else if (curKey == KeyCode.DOWN) {
                snake.setDirection(Direction.DOWN);
            }  else if (curKey == KeyCode.LEFT) {
                snake.setDirection(Direction.LEFT);
            }  else if (curKey == KeyCode.RIGHT) {
                snake.setDirection(Direction.RIGHT);
            }
        }

        snake.move();
        snake.trace();
    }

    private void checkCollision() {
        if (snake.collided(food)) {
            snake.grow();
            score+= food.getPoint();
            System.out.println("Score = " + score);
            food.respawn();
        }
        if (snake.collided(specialFood)) {
            snake.grow();
            score+= specialFood.getPoint();
            System.out.println("Score = " + score);
            specialFood.respawn();
        }
        if (checkAndHandleDeath()) {
            running = false;
            if (testMode) return;
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText("Game Over");
                alert.setContentText("You're dead");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    try {
                        Stage newStage = new Stage();
                        new Launcher().start(newStage);

                        Stage currentStage = (Stage) gameStage.getScene().getWindow();
                        currentStage.close();

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Platform.exit();
                }
            });
        }
    }

    public boolean checkAndHandleDeath() {
        if (snake.checkDead()) {
            running = false;
            return true;
        }
        return false;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public int getScore() {
        return score;
    }

    public void redraw() {
        gameStage.render(snake, food, specialFood);
    }
    @Override
    public void run() {
        while (running) {
            keyProcess();
            checkCollision();
            redraw();
            try {
                Thread.sleep((long) interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}