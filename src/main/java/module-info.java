module AimTrainer {
    requires javafx.fxml;
    requires javafx.controls;
    requires hibernate.jpa;
    requires lombok;
    opens z03.pap22z to javafx.fxml;
    exports z03.pap22z;
}