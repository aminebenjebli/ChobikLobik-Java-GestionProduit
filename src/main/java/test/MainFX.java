package test;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainFX extends Application {

    private TimerUtility timerUtility;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Showroom.fxml"));
            primaryStage.setTitle("ChoubikLoubik");
            primaryStage.setScene(new Scene(root, 600, 500)); // Set initial size if necessary
            primaryStage.show();

            primaryStage.centerOnScreen();

            timerUtility = new TimerUtility();
            timerUtility.startSubscriptionUpdater();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (timerUtility != null) {
            timerUtility.shutdownNow();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class TimerUtility {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startSubscriptionUpdater() {
        scheduler.scheduleAtFixedRate(() -> {
            SubscriptionService subscriptionService = new SubscriptionService();
            subscriptionService.updateExpiredSubscriptions();
        }, 0, 1, TimeUnit.DAYS);
    }

    public void shutdownNow() {
        scheduler.shutdownNow();
    }
}

class SubscriptionService {
    public void updateExpiredSubscriptions() {
        // Logic to update expired subscriptions
    }
}
