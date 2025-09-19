package se233.chapter5part22;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se233.chapter5part22.controller.GameLoop;
import se233.chapter5part22.model.Direction;
import se233.chapter5part22.model.Food;
import se233.chapter5part22.model.Snake;
import se233.chapter5part22.view.GameStage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameLoopTest {
    private GameStage gameStage;
    private Snake snake;
    private Food food;
    private Food specialFood;
    private GameLoop gameLoop;

    @BeforeEach
    public void setup() {
        gameStage = new GameStage();
        snake = new Snake(new Point2D(0,0));
        food = new Food(new Point2D(0,1), 1);
        specialFood = new Food(new Point2D(1,0), 5);
        gameLoop = new GameLoop(gameStage, snake, food, specialFood);
    }
    private void clockTickHelper() throws Exception {
        ReflectionHelper.invokeMethod(gameLoop, "keyProcess", new Class<?>[0]);
        ReflectionHelper.invokeMethod(gameLoop, "checkCollision", new Class<?>[0]);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class<?>[0]);
    }
    @Test
    public void keyProcess_pressRight_snakeTurnRight() throws Exception {
        ReflectionHelper.setField(gameStage,"key", KeyCode.RIGHT );
        ReflectionHelper.setField(snake, "direction", Direction.DOWN);
        clockTickHelper();
        Direction currentDirection = (Direction) ReflectionHelper.getField(snake, "direction");
        assertEquals(Direction.RIGHT, currentDirection);
    }
    @Test
    public void collided_snakeEatFood_shouldGrow() throws Exception {
        clockTickHelper();
        assertTrue(snake.getLength() > 1);
        clockTickHelper();
        assertNotSame(food.getPosition(), new Point2D(0,1));
    }


    @Test void collided_snakeHitBorder_shouldDie() throws Exception {
        ReflectionHelper.setField(gameStage, "key", KeyCode.LEFT);
        gameLoop.setTestMode(true);
        clockTickHelper();
        Boolean running = (Boolean) ReflectionHelper.getField(gameLoop, "running");
        assertFalse(running);
    }
    @Test
    public void redraw_calledThreeTimes_snakeAndFoodShouldRenderThreeTimes() throws Exception {
        GameStage mockGameStage = Mockito.mock(GameStage.class);
        Snake mockSnake = Mockito.mock(Snake.class);
        Food mockFood = Mockito.mock(Food.class);

        GameLoop gameLoop = new GameLoop(mockGameStage, mockSnake, mockFood, null);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class<?>[0]);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class<?>[0]);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class<?>[0]);
        verify(mockGameStage, times(3)).render(mockSnake, mockFood, null);
    }

    @Test
    public void snakeEatFood_scoreShouldIncreaseByOne() throws Exception {
        clockTickHelper();
        assertTrue(gameLoop.getScore() > 0);
        clockTickHelper();
        assertNotSame(food.getPosition(), new Point2D(0,1));
    }

    @Test
    public void snakeEatSpecialFood_scoreShouldIncreaseByFive() throws Exception {
        ReflectionHelper.setField(gameStage, "key", KeyCode.RIGHT);
        clockTickHelper();
        assertTrue(gameLoop.getScore() > 4, "The point is not increasing by 5");
        clockTickHelper();
        assertNotSame(food.getPosition(), new Point2D(1,0), "The is not food disappearing");
    }

    @Test
    public void snakeHeadingRight_whenPressLeft_snakeStillHeadingRight() throws Exception {
        ReflectionHelper.setField(gameStage, "key", KeyCode.RIGHT);
        gameLoop.keyProcess();
        clockTickHelper();
        ReflectionHelper.setField(gameStage, "key", KeyCode.LEFT);
        gameLoop.keyProcess();
        System.out.println(snake.getDirection());
        assertTrue(snake.getHead().getX() > 1, "Snake moves opposite direction");
    }
}
