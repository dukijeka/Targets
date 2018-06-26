package targets;

import game.Main;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ShootingTarget extends Target {

    private int radius;
    private boolean shrinkingScaleDirection;

    public ShootingTarget(Point2D inititalPostition, Point2D initialSpeed, Point2D initialAcceleration, int radius) {
        super(inititalPostition, initialSpeed, initialAcceleration);
        this.radius = radius;
        createTarget(ThreadLocalRandom.current().nextInt(3, 7 + 1));
    }

    @Override
    public void updatePosition(float timeElapsed, Point2D acceleration, Rectangle border) {

       // this.shrinkingScaleDirection = shrinkingScaleDirection;
        super.rotationAngle += rotationSpeed * timeElapsed;
        super.speed = super.speed.add(super.acceleration.multiply(timeElapsed));
        super.scale += scaleSpeed * timeElapsed;
//        if (shrinkingScaleDirection) {
//            super.scale += scaleSpeed * timeElapsed;
//        } else {
//            super.scale -= scaleSpeed * timeElapsed;
//        }

        super.postition = super.postition.add(super.speed.multiply(timeElapsed));

        // collisions with border

        // left
        if (super.postition.getX() - getRadius() < border.getX()) {
//            super.postition = new Point2D(
//                     (super.postition.getX() + getRadius()) - super.postition.getX(),
//                    super.postition.getY());

            super.speed = new Point2D(-super.speed.getX(), super.speed.getY());
            //shrinkingScaleDirection = !shrinkingScaleDirection;
            scaleSpeed = -scaleSpeed;
        }

        // right
        if (super.postition.getX() + getRadius() > border.getX() + border.getWidth()) {
//            super.postition = new Point2D(
//                     (super.postition.getX() + border.getWidth() + getRadius()) - super.postition.getX(),
//                    super.postition.getY());

            super.speed = new Point2D(-super.speed.getX(), super.speed.getY());
            //shrinkingScaleDirection = !shrinkingScaleDirection;
            scaleSpeed = -scaleSpeed;
        }

        if (super.postition.getY() - getRadius() < border.getY()) {
//            super.postition = new Point2D(
//                    super.postition.getX(),
//                    (super.postition.getY() + getRadius()) - super.postition.getY());

            super.speed = new Point2D(super.speed.getX(), -super.speed.getY());
            //shrinkingScaleDirection = !shrinkingScaleDirection;
            scaleSpeed = -scaleSpeed;
        }

        if (super.postition.getY() + getRadius() > border.getY() + border.getHeight()) {
//            super.postition = new Point2D(
//                    super.postition.getX(),
//                     (super.postition.getY() + border.getHeight() + getRadius()) - super.postition.getY());

            super.speed = new Point2D(super.speed.getX(), -super.speed.getY());
            //shrinkingScaleDirection = !shrinkingScaleDirection;
            scaleSpeed = -scaleSpeed;
        }

        // update properties
        super.setTranslateX(super.postition.getX());
        super.setTranslateY(super.postition.getY());
        super.setRotate(super.rotationAngle);
        super.setScaleX(super.scale);
        super.setScaleY(super.scale);
    }

    public int getRadius() {
        return radius;
    }

    private void createTarget(int numberOfCircles) {
        float radiusIncrement = radius / numberOfCircles;
        for (int i = 0; i < numberOfCircles; i++) {
            Circle circle;
            Text pointsText;
            int points = (i + 1) * 20;
            // for the first circle
            if (i == numberOfCircles - 1) {
                circle =
                        new Circle(radius - radiusIncrement * i, createRandomBrightColor());
                pointsText = new Text("" + points);
                pointsText.setScaleX((radius - radiusIncrement * i) / pointsText.getBoundsInLocal().getHeight() + 0.2);
                pointsText.setScaleY((radius - radiusIncrement * i) / pointsText.getBoundsInLocal().getWidth() + 0.2);
                pointsText.setTranslateX(pointsText.getX() - pointsText.getBoundsInLocal().getWidth() / 2);
                pointsText.setTranslateY(pointsText.getY() + pointsText.getBoundsInLocal().getHeight() / 4);
                super.getChildren().addAll(circle, pointsText);
            } else if (i % 2 == 0) {
                circle =
                        new Circle(radius -  radiusIncrement * i, Color.WHITE);
                pointsText = new Text("" + points);

                pointsText.setScaleX(radiusIncrement / (2 * pointsText.getBoundsInLocal().getHeight()));
                pointsText.setScaleY(radiusIncrement / pointsText.getBoundsInLocal().getWidth());
                pointsText.setTranslateX(pointsText.getX() - pointsText.getBoundsInLocal().getWidth() / 2 - 10 * (7 - numberOfCircles));
                pointsText.setTranslateY(pointsText.getY() + pointsText.getBoundsInLocal().getHeight() / 4);
                pointsText.setTranslateX(circle.getCenterX() + circle.getRadius() - pointsText.getBoundsInLocal().getWidth());
                super.getChildren().addAll(circle, pointsText);
                if (i == 0) {
                    circle.setStroke(Color.BLACK);
                }
            } else {
                circle =
                        new Circle(radius - radiusIncrement * i, Color.BLACK);
                pointsText = new Text("" + points);
                pointsText.setFill(Color.WHITE);
                pointsText.setScaleX(radiusIncrement / (2 * pointsText.getBoundsInLocal().getHeight()));
                pointsText.setScaleY(radiusIncrement / pointsText.getBoundsInLocal().getWidth());
                pointsText.setTranslateX(pointsText.getX() - pointsText.getBoundsInLocal().getWidth() / 2 - 10 * (7 - numberOfCircles));
                pointsText.setTranslateY(pointsText.getY() + pointsText.getBoundsInLocal().getHeight() / 4);
                pointsText.setTranslateX(circle.getCenterX() + circle.getRadius() - pointsText.getBoundsInLocal().getWidth());
                super.getChildren().addAll(circle, pointsText);
                if (i == 0) {
                    circle.setStroke(Color.BLACK);
                }
            }

            //circle.setOnMousePressed(e -> addScoredPoints(points, circle, e));
            circle.setOnMouseReleased(e -> addScoredPoints(points, circle, e));
            circle.setOnMouseDragOver(e -> addScoredPoints(points, circle, e));
            pointsText.setOnMouseReleased(e -> addScoredPoints(points, circle, e));
            pointsText.setOnMouseDragOver(e -> addScoredPoints(points, circle, e));
        } // end for
    }

    private Color createRandomBrightColor() {
        Random random = new Random();
        float MIN_BRIGHTNESS = 0.8f;
        float h = random.nextFloat();
        float s = 1f;
        float b = MIN_BRIGHTNESS + ((1f - MIN_BRIGHTNESS) * random.nextFloat());
        Color c = Color.hsb(h, s, b);
        return c;
    }

    private void addScoredPoints(int pointsScored, Circle circle, MouseEvent e) {
        //System.out.printf("" + super.getScaleX());
        int totalPointsScored = (int) Math.abs((pointsScored * (1 / super.getScaleX())));
        super.removeFromScene();
        Main.updateScore(Main.getScore() + totalPointsScored);
        Text scoredPointsText = new Text(e.getSceneX(), e.getSceneY(), "" + totalPointsScored);
        scoredPointsText.setScaleX(6);
        scoredPointsText.setScaleY(6);
        scoredPointsText.setMouseTransparent(true);
        Main.getRoot().getChildren().addAll(scoredPointsText);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3), scoredPointsText);
        scaleTransition.setToX(0);
        scaleTransition.setToY(0);
        scaleTransition.setInterpolator(Interpolator.LINEAR);
        scaleTransition.setCycleCount(1);


        FillTransition fillTransition = new FillTransition(Duration.seconds(3), scoredPointsText);
        fillTransition.setToValue(Color.WHITE);
        scaleTransition.setInterpolator(Interpolator.LINEAR);
        scaleTransition.setCycleCount(1);

        scaleTransition.play();
        fillTransition.play();
    }
}
