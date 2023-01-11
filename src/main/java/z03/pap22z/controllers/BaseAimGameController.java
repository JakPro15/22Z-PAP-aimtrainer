package z03.pap22z.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import z03.pap22z.MusicManager;
import z03.pap22z.Settings;

public abstract class BaseAimGameController extends z03.pap22z.controllers.BaseGameController {
    // Radius of the target when the difficulty is set to "normal".
    protected static double NORMAL_RADIUS;
    // Offset value used when calculating target radius in other difficulties.
    protected static double RADIUS_OFFSET;

    // Target circle object.
    @FXML
    protected Circle circle;

    // These properties are used as a means of having constantly updated game
    // dimensions.
    protected SimpleDoubleProperty playWidth = new SimpleDoubleProperty();
    protected SimpleDoubleProperty playHeight = new SimpleDoubleProperty();
    protected SimpleDoubleProperty circleRadius = new SimpleDoubleProperty();

    @Override
    public void initializeMainBlock() {
        circle.setRadius(calculateCircleRadius());

        circleRadius.bind(this.circle.radiusProperty());
        playWidth.bind(this.playfield.widthProperty());
        playHeight.bind(this.playfield.heightProperty());

        super.initializeMainBlock();
    }

    /**
     * Teleports the circle object to random coordinates within the playfield
     * object.
     */
    protected void teleportCircle() {
        double newX = generateCircleCoord(this.playWidth.getValue());
        double newY = generateCircleCoord(this.playHeight.getValue());

        circle.setCenterX(newX);
        circle.setCenterY(newY);
    }

    /**
     * Generates a random valid circle center coordinate given span - length of
     * a single dimension of a field. This method accounts for the radius of
     * the circle to make sure the circle doesn't clip the edges of the field.
     *
     * @param span - the length of a given dimension in a field.
     * @return double - resulting coordinate value.
     */
    protected double generateCircleCoord(double span) {
        double radius = circleRadius.getValue();
        double allowedSpan = span - 2 * radius;
        return random.nextDouble() * allowedSpan + radius;
    }

    /**
     * Returns the circle radius length calculated based on current game
     * difficulty setting (the harder the game, the smaller the radius).
     *
     * @return the calculated radius
     */
    protected static double calculateCircleRadius() {
        int gameDiff = Settings.getGameDifficulty();
        double newRadius = NORMAL_RADIUS - (gameDiff - 2) * RADIUS_OFFSET;
        return newRadius;
    }

    /**
     * Main FXML handler for clicks inside the playfield object.
     * 
     * @param event
     */
    @FXML
    protected void handlePlayfieldClick(MouseEvent event) {
        if (logic.getIsGameOn()) {
            MusicManager.playHitMarkerSound();
            if (isInCircle(event)) {
                teleportCircle();
                this.logic.registerTargetHit();
            } else {
                this.logic.registerTargetMiss();
            }
        }
    }

    /**
     * @param event mouse click event
     * @return the distance of a click from the center of the circle.
     */
    protected double clickToCenterDistance(MouseEvent event) {
        double posX = event.getX();
        double posY = event.getY();
        double circleX = this.circle.getCenterX();
        double circleY = this.circle.getCenterY();
        return calculateDistance(posX, posY, circleX, circleY);
    }

    /**
     * Returns true if the given mouse event occured within a circle.
     * 
     * @param event mouse event
     * @return whether the event occured within the circle.
     */
    protected boolean isInCircle(MouseEvent event) {
        double centerDistance = clickToCenterDistance(event);
        return centerDistance <= this.circle.getRadius();
    }

    /**
     * Calculates distance between two points.
     *
     * @param x1 - first point's x coordinate.
     * @param y1 - first point's y coordinate.
     * @param x2 - second point's x coordinate.
     * @param y2 - second point's y coordinate.
     * @return the distance between points (x1, y1) and (x2, y2)
     */
    protected static double calculateDistance(double x1, double y1, double x2, double y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    }

}
