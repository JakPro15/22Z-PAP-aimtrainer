package z03.pap22z;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameLogic
{

    protected IntegerProperty pointsProperty = new SimpleIntegerProperty();
    protected FloatProperty accuracyProperty = new SimpleFloatProperty();
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

    public void registerTargetHit()
    {
        this.targetsNr += 1;
        this.targetsHit += 1;
        this.pointsProperty.setValue(this.pointsProperty.getValue() + (currentCombo + 1) * 10);
        this.accuracyProperty.setValue((this.targetsHit / this.targetsNr) * 100);
        this.currentCombo += 1;
    }

    public void registerTargetMiss()
    {
        this.targetsNr += 1;
        this.accuracyProperty.setValue((this.targetsHit / this.targetsNr) * 100);
        this.currentCombo = 0;
    }

    public float getAccuracy()
    {
        return this.accuracyProperty.getValue();
    }

    public int getPoints()
    {
        return this.pointsProperty.getValue();
    }

    public IntegerProperty pointsProperty()
    {
        return this.pointsProperty;
    }

    public FloatProperty accuracyProperty()
    {
        return this.accuracyProperty;
    }
}
