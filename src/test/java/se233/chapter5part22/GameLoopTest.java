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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GameLoopTest {
    private GameStage gameStage;
    private Snake snake;
    private Food food;
    private GameLoop gameLoop;

    @BeforeEach
    public void setup() {
        gameStage = new GameStage();
        snake = new Snake(new Point2D(0,0));
        food = new Food(new Point2D(0,1));
        gameLoop = new GameLoop(gameStage, snake, food);
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
        clockTickHelper();
        Boolean running = (Boolean) ReflectionHelper.getField(gameLoop, "running");
        assertFalse(running);
    }
    @Test
    public void redraw_calledThreeTimes_snakeAndFoodShouldRenderThreeTimes() throws Exception {
        GameStage mockGameStage = Mockito.mock(GameStage.class);
        Snake mockSnake = Mockito.mock(Snake.class);
        Food mockFood = Mockito.mock(Food.class);
        GameLoop gameLoop = new GameLoop(mockGameStage, mockSnake, mockFood);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class<?>[0]);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class<?>[0]);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class<?>[0]);
        verify(mockGameStage, times(3)).render(mockSnake, mockFood);
    }
}
