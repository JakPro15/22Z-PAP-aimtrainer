package z03.pap22z;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AimSniperController extends z03.pap22z.SceneController
{
    private AimSniperLogic logic = new AimSniperLogic();

    private Timer timer = new Timer();

    private int timeLeft = 1;

    @FXML
    private Label scoreValueLabel;

    @FXML
    private Label accuracyValueLabel;

    @FXML
    private Label timeLeftValueLabel;

    public void initialize()
    {
        this.logic.pointsProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue)
            {
                scoreValueLabel.textProperty().setValue(String.format("%d", logic.getPoints()));
            }
        });

        this.logic.accuracyProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue)
            {
                accuracyValueLabel.textProperty().setValue(String.format("%.2f%%", logic.getAccuracy()));
            }
        
        
        });

        this.timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        timeLeftValueLabel.setText(String.format("%d seconds", timeLeft));
                        timeLeft -= 1;
                        if(timeLeft == -1)
                        {
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    @FXML
    protected void handleExit(ActionEvent event)
    {
        try
        {
            switchToMenu(event);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}

