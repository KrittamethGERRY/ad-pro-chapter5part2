package se233.chapter5part22;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se233.chapter5part22.controller.GameLoop;
import se233.chapter5part22.model.Food;
import se233.chapter5part22.model.Snake;
import se233.chapter5part22.view.GameStage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        GameStage gameStage = new GameStage();
        Snake snake = new Snake(new Point2D(gameStage.WIDTH / 2, gameStage.HEIGHT / 2));
        Food food = new Food(1);
        Food specialFood = new Food(new Point2D(1, 0),5);
        GameLoop gameLoop = new GameLoop(gameStage, snake, food, specialFood);
        Scene scene = new Scene(gameStage, gameStage.WIDTH * gameStage.TILE_SIZE, gameStage.HEIGHT * gameStage.TILE_SIZE);
        scene.setOnKeyPressed(e -> gameStage.setKey(e.getCode()));
        scene.setOnKeyReleased(e -> gameStage.setKey(null));
        stage.setTitle("Snake Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        (new Thread(gameLoop)).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
