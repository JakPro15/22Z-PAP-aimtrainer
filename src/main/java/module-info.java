module AimTrainer {
    requires java.sql;
    requires javafx.fxml;
    requires javafx.controls;
    requires hibernate.jpa;
    requires lombok;
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    opens z03.pap22z to javafx.fxml;
    exports z03.pap22z;
}