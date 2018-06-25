package targets;

import game.Main;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

public abstract class Target extends Group{

    protected Point2D postition;
    protected Point2D speed;
    protected Point2D acceleration;
    protected double rotationSpeed;
    protected double rotationAngle;
    protected double scaleSpeed;
    protected double scale;

    public Target(Point2D inititalPostition, Point2D initialSpeed, Point2D initialAcceleration) {
        this.postition = inititalPostition;
        this.speed = initialSpeed;
        this.acceleration = initialAcceleration;

        // set rotation parameters
        rotationSpeed = (Math.random() + 0.5)*180.0/Math.PI;

        // scale
        scale = Math.random() * 0.9 + 0.3; // initial scale
        scaleSpeed = Math.random() * 0.7;

        // move target to desired position
        this.setTranslateX(inititalPostition.getX());
        this.setTranslateY(inititalPostition.getY());

    }

    public abstract void updatePosition(float timeElapsed, Point2D acceleration,
                                        Rectangle border);

    public void removeFromScene() {
        Main.getRoot().getChildren().remove(this);
        Main.getTargets().remove(this);
    }

}
