package targets;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;




public class Gunpoint extends Group {
    private Shape bulletPoint;

    public final int WIDTH = 5;
    public final int HEGHT = 30;
    public Gunpoint(Point2D position) {
        Rectangle verticalPart = new Rectangle(position.getX(), position.getY(),WIDTH, HEGHT);
        Rectangle horizontalPart = new Rectangle(position.getX(), position.getY(),WIDTH, HEGHT);
        horizontalPart.setRotate(90);
        bulletPoint = Shape.intersect(verticalPart, horizontalPart);
        bulletPoint.setFill(Color.RED);
        this.getChildren().addAll(verticalPart, horizontalPart, bulletPoint);
    }

    public void setPosition(double x, double y) {
        this.setTranslateX(x);
        this.setTranslateY(y);
    }

    public Shape getBulletPoint() {
        return bulletPoint;
    }


}
