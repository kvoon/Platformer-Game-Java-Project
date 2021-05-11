package game.view;

import game.model.Block;
import game.model.Entity;
import game.model.GameEngine;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

class Runner {

    private GameEngine model;
    private Pane pane;
    private List<EntityView> entityViews;
    private BackgroundDrawer backgroundDrawer;
    private ImageView[] health = new ImageView[3];
    private Text lives;
    private Timeline timeline;
    private double xViewportOffset = 0.0;
    private double width;
    private double height;
    private Text score;
    private Text overallScore;
    private Text targetTime;


    Runner(GameEngine model, Pane pane, double width, double height) {
        this.model = model;
        this.pane = pane;
        this.width = width;
        this.height = height;
        this.entityViews = new ArrayList<>();
        this.health[0] = new ImageView(new Image("heart.png"));
        this.health[1] = new ImageView(new Image("heart.png"));
        this.health[2] = new ImageView(new Image("heart.png"));
        for (int i = health.length - 1; i >= 0; i--) {
            this.health[i].setFitHeight(30);
            this.health[i].setFitWidth(30);
            this.health[i].setY(10);
            this.health[i].setX(width - 40 - i * 40);
        }
        this.score = new Text(20, 20, "Current Score : " + (int)model.getScore());
        this.score.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
        this.score.setFill(Paint.valueOf("BLACK"));
        this.overallScore = new Text(20, 50, "Overall Score : " + (int)model.getTotalScore());
        this.overallScore.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
        this.overallScore.setFill(Paint.valueOf("BLACK"));
        this.targetTime = new Text(20, 80, "Target Time Remaining: " + model.getTargetTime());
        this.targetTime.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
        this.targetTime.setFill(Paint.valueOf("BLACK"));
        this.lives = new Text(10, 20, "lives remaining: " + model.getLives());
        this.lives.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 20));
        this.lives.setFill(Paint.valueOf("BLACK"));
        this.lives.setX(width - this.lives.getLayoutBounds().getWidth() - 150);
        this.lives.setY(30);
        this.pane.getChildren().add(lives);
        this.pane.getChildren().add(score);
        this.pane.getChildren().add(overallScore);
        this.pane.getChildren().add(targetTime);
        this.backgroundDrawer = new ParallaxBackground();
        this.backgroundDrawer.draw(model, pane);
        addHealth();
    }

    void run() {
        timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void draw() {
        model.tick();

        if (model.isFinished()) {
            drawScreen("Congratulations!\nYou Won with " + (int)model.getTotalScore()+ " points!\n"
            + "With " + model.getLives() + " lives remaining!");
            return;
        } else if (model.gameOver()) {
            drawScreen("You lose!");
            return;
        }

        drawLives();
        drawTargetTime();
        drawCurrentScore();
        drawOverallScore();

        if (model.getCurrentLevel().getHeroHealth() == 2) {
            pane.getChildren().remove(health[2]);
        } else if (model.getCurrentLevel().getHeroHealth() == 1) {
            pane.getChildren().remove(health[1]);
        } else if (model.getCurrentLevel().getHeroHealth() == 0) {
            pane.getChildren().remove(health[0]);
            addHealth();
            model.resetCurrentLevel();
        }

        List<Entity> entities = model.getCurrentLevel().getEntities();

        for (EntityView entityView : entityViews) {
            entityView.markForDelete();
        }

        double heroXPos = model.getCurrentLevel().getHeroX();
        heroXPos -= xViewportOffset;

        if (heroXPos < GameWindow.getViewportMargin()) {
            if (xViewportOffset >= 0) { // Don't go further left than the start of the level
                xViewportOffset -= GameWindow.getViewportMargin() - heroXPos;
                if (xViewportOffset < 0) {
                    xViewportOffset = 0;
                }
            }
        } else if (heroXPos > width - GameWindow.getViewportMargin()) {
            xViewportOffset += heroXPos - (width - GameWindow.getViewportMargin());
        }

        backgroundDrawer.update(xViewportOffset);

        for (Entity entity : entities) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }
        entityViews.removeIf(EntityView::isMarkedForDelete);
    }

    /**
     * Draws the finish screen with the specified message in the center
     *
     * @param message The message to be displayed in the center
     */
    private void drawScreen(String message) {
        for (EntityView entityView : entityViews) {
            pane.getChildren().remove(entityView.getNode());
        }
        for (ImageView life : this.health) {
            pane.getChildren().remove(life);
        }
        pane.getChildren().remove(lives);
        Text t = new Text(10, 20, message);
        t.setFont(Font.font("Chalkboard SE", FontPosture.ITALIC, 30));
        t.setFill(Paint.valueOf("BLACK"));
        t.setLayoutX((width - t.getLayoutBounds().getWidth()) / 2.0);
        t.setLayoutY((height - t.getLayoutBounds().getHeight()) / 2.0);
        t.setTextAlignment(TextAlignment.CENTER);
        pane.getChildren().add(t);
        timeline.stop();
    }

    /**
     * Adds the hero's health to the view
     */
    private void addHealth() {
        for (ImageView life : health) {
            pane.getChildren().add(life);
        }
    }

    /**
     * Adds the number of lives the hero has to the view
     */
    private void drawLives() {
        this.lives.setText("lives remaining: " + model.getLives());
    }

    private void drawTargetTime() {
        this.targetTime.setText("Target Time Remaining: " + model.getTargetTime());
    }

    private void drawCurrentScore() {
        this.score.setText("Current Score : " + (int)model.getScore());
    }
    private void drawOverallScore() {
       this.overallScore.setText("Overall Score : " + (int)model.getTotalScore());
    }
}