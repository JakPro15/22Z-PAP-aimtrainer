package z03.pap22z;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameLogic
{

    protected IntegerProperty pointsProperty = new SimpleIntegerProperty();
    protected DoubleProperty accuracyProperty = new SimpleDoubleProperty();
    protected int targetsNr;
    protected int targetsHit;
    protected int currentCombo;

    public GameLogic()
    {
        this.pointsProperty.setValue(0);
        this.accuracyProperty.setValue(0);
        this.targetsNr = 0;
        this.targetsHit = 0;
        this.currentCombo = 0;
    }

    /**
     * Updates all logic values as if a target were hit
     */
    public void registerTargetHit()
    {
        this.targetsNr += 1;
        this.targetsHit += 1;
        this.pointsProperty.setValue(this.pointsProperty.getValue() + (currentCombo + 1) * 10);
        this.accuracyProperty.setValue(((double)this.targetsHit / (double)this.targetsNr) * (double)100);
        this.currentCombo += 1;
    }

    /**
     * Updates all logic values as if a target expired while not having been hit
     */
    public void registerTargetMiss()
    {
        this.targetsNr += 1;
        this.accuracyProperty.setValue(((double)this.targetsHit / (double)this.targetsNr) * (double)100);
        this.currentCombo = 0;
    }

    /**
     * Returns the current ratio of targets hit to all targets
     * @return ratio of number of targets hit to total number off all targets
     */
    public double getAccuracy()
    {
        return this.accuracyProperty.getValue();
    }

    /**
     * Returns current score
     * @return number of points gained throughout the game
     */
    public int getPoints()
    {
        return this.pointsProperty.getValue();
    }

    /**
     * Returns the IntegerProperty of current score to be used in updating labels
     * @return IntegerProperty of the number of points gained throughout the game
     */
    public IntegerProperty pointsProperty()
    {
        return this.pointsProperty;
    }

    /**
     * Returns the DoubleProperty of the current ratio of targets hit to all targets
     * @return DoubleProperty of the ratio of number of targets hit to total number off all targets
     */
    public DoubleProperty accuracyProperty()
    {
        return this.accuracyProperty;
    }
}
