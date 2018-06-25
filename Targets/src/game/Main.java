package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import targets.Gunpoint;
import targets.ShootingTarget;
import targets.Target;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Main extends Application {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private final int TARGET_DIAMETER = 100;

    private final int NUMBER_OF_TARGETS = 7;
    private final int GUNPOINT_MOVE_DISTANCE = 5;

    private final MyTimer timer = new MyTimer();
    private static int score = 0;
    private static Text scoreText = new Text(50, 50, "Score: " + Main.score);

    private int ammo = NUMBER_OF_TARGETS + 3;
    private Text ammoText = new Text(150, 50, "Ammo left: " + ammo);
    private Gunpoint gunpoint = new Gunpoint(new Point2D(WIDTH / 2, HEIGHT / 2));

    private static Scene scene;
    private static Group root;

    private Rectangle border = new Rectangle(0, 0, WIDTH, HEIGHT);
    private static List<Target> targets = new LinkedList<>();

    private class MyTimer extends AnimationTimer {
        private long before = 0;
        private Double totalTimeElapsed = 0.0;
        @Override
        public void handle(long now)
        {

            if(before == 0)
                before = now;
            float timeElapsed = (now- before)/1e9f; // converted to seconds
            totalTimeElapsed += timeElapsed;
            for(Target target : targets) {

                target.updatePosition(timeElapsed,
                        new Point2D(timeElapsed * Math.random(), timeElapsed * Math.random()),
                        border
                );
            }

            before = now;

            if (ammo <= 0 || targets.size() == 0) {


                Text endGameText = new Text(scene.getWidth() / 3, scene.getHeight() / 3,
                        "Total Score: " + score + "\n" + "Time: " + totalTimeElapsed.shortValue() + " seconds");
                endGameText.setScaleX(2);
                endGameText.setScaleY(2);
                root.getChildren().setAll(endGameText);
                timer.stop();
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        root = new Group();
        primaryStage.setTitle("Targets");
        scene = new Scene(root, WIDTH, HEIGHT);
        scene.widthProperty().addListener(e-> {onWidthChanged(scene);});
        scene.heightProperty().addListener(e-> {onWidthChanged(scene);});
        //scene.onMouseClickedProperty().addListener(e-> {onWidthChanged(scene);});
        addTargets(root, scene, NUMBER_OF_TARGETS);
        root.getChildren().addAll(scoreText, gunpoint, ammoText);
        scene.setOnMouseMoved(
                e-> {
                    gunpoint.setPosition(e.getSceneX() - WIDTH / 2 - gunpoint.WIDTH / 2,
                            e.getSceneY() - HEIGHT / 2 - gunpoint.HEGHT / 2);
                }
        );

        scene.setOnMouseClicked(e -> updateAmmo());
        scene.setOnMouseDragOver(e -> updateAmmo());
        scene.setOnKeyPressed(e -> onKeyPressed(e));
        gunpoint.setMouseTransparent(true);
        scene.setCursor(Cursor.NONE);

        primaryStage.setScene(scene);
        primaryStage.show();
        timer.start();
    }

    private void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case UP:
                //System.out.printf("up");

                gunpoint.setTranslateY(gunpoint.getTranslateY() - GUNPOINT_MOVE_DISTANCE);

                break;
            case DOWN:

                gunpoint.setTranslateY(gunpoint.getTranslateY() + GUNPOINT_MOVE_DISTANCE);

                break;
            case LEFT:

                gunpoint.setTranslateX(gunpoint.getTranslateX() - GUNPOINT_MOVE_DISTANCE);

                break;
            case RIGHT:

                gunpoint.setTranslateX(gunpoint.getTranslateX() + GUNPOINT_MOVE_DISTANCE);
                break;
            case SPACE:
                for (Target target : targets) {
                    if (gunpoint.getBoundsInParent().intersects(target.getBoundsInParent())) {
                        target.getOnMousePressed().handle(
                                new MouseEvent(MouseEvent.MOUSE_PRESSED, gunpoint.getTranslateX(),
                                        gunpoint.getTranslateY(), 0, 0, MouseButton.PRIMARY,
                                        1, false, false, false, false, true, false, false, true,
                                        false, true, null)
                                );
                        break;
                    }
                }
                break;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void addTargets(Group root, Scene  scene, int numberOfTargets) {
        for (int i = 0; i < numberOfTargets; i++) {
            int xPosition =  TARGET_DIAMETER + (int) (Math.random() * (scene.getWidth() - TARGET_DIAMETER));
            int yPosition = TARGET_DIAMETER + (int) (Math.random() * (scene.getHeight()- TARGET_DIAMETER));
            Target target = new ShootingTarget(new Point2D(xPosition, yPosition),
                    new Point2D(Math.random()*240-120, Math.random()*80-40),
                    new Point2D(Math.random()*240-120, Math.random()*80-40),
                    TARGET_DIAMETER / 2
            );

            root.getChildren().addAll(target);
            targets.add(target);
        }
    }

    private void onWidthChanged(Scene scene) {
        border = new Rectangle(0, 0, scene.getWidth(), scene.getHeight());
    }

    private void updateAmmo() {
        ammo--;
        ammoText.setText("Ammo left: " + ammo);
    }

    public static int getScore() {
        return score;
    }

    public static void updateScore (int newScore) {
        score = newScore;
        scoreText.setText("Score: " + score);
    }

    public static Scene getScene() {
        return scene;
    }

    public static Group getRoot() {
        return root;
    }

    public static List<Target> getTargets() {
        return targets;
    }
}
