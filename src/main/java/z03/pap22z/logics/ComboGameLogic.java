package z03.pap22z.logics;

import javafx.beans.property.SimpleIntegerProperty;

public class ComboGameLogic extends GameLogic {
    protected SimpleIntegerProperty currentCombo = new SimpleIntegerProperty();

    public ComboGameLogic() {
        super();
        this.currentCombo.setValue(0);
    }

    @Override
    public void registerTargetHit() {
        super.registerTargetHit();
        addPoints();
    }

    public void addPoints() {
        addPoints(10 * (currentCombo.get() + 1));
        this.currentCombo.set(this.currentCombo.get() + 1);
    }

    @Override
    public void registerTargetMiss() {
        super.registerTargetMiss();
        this.currentCombo.set(0);
    }
}
