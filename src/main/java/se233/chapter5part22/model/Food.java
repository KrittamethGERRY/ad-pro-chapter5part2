package se233.chapter5part22.model;

import javafx.geometry.Point2D;
import se233.chapter5part22.view.GameStage;

import java.util.Random;

public class Food {
    private Point2D position;
    private Random rn;
    private int point;

    public Food(Point2D position, int point) {
        this.rn = new Random((long)(Math.random() * 10));
        this.position = position;
        this.point = point;
    }

    public Food(int point) {
        this.rn = new Random();
        this.position = new Point2D(rn.nextInt(GameStage.WIDTH), rn.nextInt(GameStage.HEIGHT));
        this.point = point;
    }
    public void respawn() {
        Point2D prev_position = this.position;
        do {
            this.position = new Point2D(rn.nextInt(GameStage.WIDTH), rn.nextInt(GameStage.HEIGHT));
        } while (prev_position == this.position);
    }
    public Point2D getPosition() {
        return position;
    }

    public int getPoint() {
        return point;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }
}
