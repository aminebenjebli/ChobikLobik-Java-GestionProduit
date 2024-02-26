package utils;

import services.SubscriptionService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerUtility {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void startSubscriptionUpdater() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                SubscriptionService subscriptionService = new SubscriptionService();
                subscriptionService.updateExpiredSubscriptions();
            } catch (Exception e) {
                e.printStackTrace(); // Handle the exception as appropriate for your application
            }
        }, 0, 1, TimeUnit.DAYS);
    }
}
