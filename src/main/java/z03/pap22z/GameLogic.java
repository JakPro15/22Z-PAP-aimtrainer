package z03.pap22z;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameLogic {

    protected SimpleIntegerProperty pointsProperty = new SimpleIntegerProperty();
    protected SimpleDoubleProperty accuracyProperty = new SimpleDoubleProperty();
    protected SimpleIntegerProperty totalClicks = new SimpleIntegerProperty();
    protected SimpleIntegerProperty clicksOnTarget = new SimpleIntegerProperty();
    protected SimpleIntegerProperty currentCombo = new SimpleIntegerProperty();
    protected Boolean isGameOn;

    public GameLogic() {
        this.pointsProperty.setValue(0);
        this.accuracyProperty.setValue(0);
        this.totalClicks.setValue(0);
        this.clicksOnTarget.setValue(0);
        this.currentCombo.setValue(0);
        this.accuracyProperty.bind(Bindings.createDoubleBinding(() -> {
            double hits = this.clicksOnTarget.getValue();
            return hits == 0 ? 0d : ((double) this.clicksOnTarget.get() / this.totalClicks.get() * 100);
        }, this.clicksOnTarget, this.totalClicks));
        this.isGameOn = false;
    }

    /**
     * Updates all logic values as if a target was hit
     */
    public void registerTargetHit() {
        this.totalClicks.set(this.totalClicks.get() + 1);
        this.clicksOnTarget.set(this.clicksOnTarget.get() + 1);
        this.pointsProperty.set(this.pointsProperty.get() + 10 * (currentCombo.get() + 1));
        this.currentCombo.set(this.currentCombo.get() + 1);
    }

    /**
     * Updates all logic values as if a target expired while not having been hit
     */
    public void registerTargetMiss() {
        this.totalClicks.set(this.totalClicks.get() + 1);
        this.currentCombo.set(0);
    }

    /**
     * Returns the current ratio of targets hit to all targets
     * @return ratio of number of targets hit to total number off all targets
     */
    public double getAccuracy() {
        return this.accuracyProperty.getValue();
    }

    /**
     * Returns current score
     * @return number of points gained throughout the game
     */
    public int getPoints() {
        return this.pointsProperty.getValue();
    }

    /**
     * Return current game state (whether or not it is on)
     * @return current game state
     * 
     */
    public Boolean getIsGameOn() {
        return this.isGameOn;
    }

    /**
     * Returns the IntegerProperty of current score to be used in updating labels
     * @return IntegerProperty of the number of points gained throughout the game
     */
    public IntegerProperty pointsProperty() {
        return this.pointsProperty;
    }

    /**
     * Returns the DoubleProperty of the current ratio of targets hit to all targets
     * @return DoubleProperty of the ratio of number of targets hit to total number
     *         off all targets
     */
    public DoubleProperty accuracyProperty() {
        return this.accuracyProperty;
    }

    /**
     * Changes the value of isGameOn variable to the opposite of what it was before.
     */
    public void switchGameState() {
        this.isGameOn = !this.isGameOn;
    }
}
