package z03.pap22z.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import z03.pap22z.Settings;

public class KeyboardWarriorController extends z03.pap22z.controllers.BaseFallingShapeGameController {
    private int squareSize;

    @Override
    protected void initializeStatics() {
        KeyboardWarriorController.GAME_NAME = "KeyboardWarrior";
    }

    @Override
    public void initializeMainBlock() {
        squareSize = (int) (80 * (1 - 0.1 * (Settings.getGameDifficulty() - 2)));
        spawnDelay = 6 - Settings.getGameDifficulty() + 2;
        timeLeft = Settings.getGameLength();

        super.initializeMainBlock();
    }

    @Override
    protected void spawnShapes() {
        shapeTimeline = new Timeline(
            new KeyFrame(Duration.millis(spawnDelay * (squareSize + 10)), event2 -> {
                if (timeLeft > 0) {
                    Shape shape = new Rectangle(squareSize, squareSize);
                    shape.getStyleClass().add("shape");
                    StackPane stack = new StackPane();
                    stack.getChildren().add(shape);
                    Text text = new Text(getLetters().get(random.nextInt(getLetters().size())).toUpperCase());
                    text.setFont(Font.font("Arial", FontWeight.BOLD, squareSize / 2));
                    stack.getChildren().add(text);
                    stack.setLayoutY(-squareSize);
                    stack.setLayoutX(
                            10 + random.nextInt((int) playfield.getWidth() - squareSize - 20));
                    playfield.getChildren().add(stack);

                    movementTimeline = new Timeline(
                            new KeyFrame(Duration.millis(spawnDelay), event3 -> {
                                stack.setLayoutY(stack.getLayoutY() + 1);
                                if (stack.getLayoutY() + squareSize == finishLine.getLayoutY()) {
                                    shape.pseudoClassStateChanged(ON_TARGET, true);
                                    readyShapes.add(stack);
                                }
                                if (stack.getLayoutY() == finishLine.getLayoutY()
                                        + finishLine.getHeight()) {
                                    if (readyShapes.contains(stack)) {
                                        shape.pseudoClassStateChanged(ON_TARGET, false);
                                        readyShapes.remove(stack);
                                        if (this.logic.getIsGameOn()) {
                                            this.logic.registerTargetMiss();
                                        }
                                    }
                                }
                            }));
                    movementTimeline.setCycleCount(Timeline.INDEFINITE);
                    movementTimeline.play();
                }
            }));
        shapeTimeline.setCycleCount(1000 * timeLeft / (spawnDelay * (squareSize + 10)));
        shapeTimeline.play();
    }
}
