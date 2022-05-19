package ec.com.jasr.geosatisws.modules.offenders.model.dto;

import ec.com.jasr.geosatisws.core.application.AppSpringCtx;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class TrackedOffender {

    private @Getter @Setter OffenderDTO offender;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> result;

    public TrackedOffender(OffenderDTO offender) {
        this.offender = offender;
    }

    public void beginTracking() {
        result = executor.scheduleAtFixedRate(() -> {

            offender.setLatitude(getRandomValue(offender.getLatitude()));
            offender.setLongitude(getRandomValue(offender.getLongitude()));

            AppSpringCtx.getBean(SimpMessagingTemplate.class).convertAndSend("/ws-resp/offenders-position", offender);
        }, 0, 1, TimeUnit.SECONDS);

    }

    private BigDecimal getRandomValue(BigDecimal value) {
        Random random = new Random();
        return new BigDecimal(random.doubles(
                value.subtract(new BigDecimal(0.0005)).doubleValue(),
                value.add(new BigDecimal(0.0005)).doubleValue()).findAny().getAsDouble())
                .setScale(5, RoundingMode.HALF_EVEN);
    }

    public void stopTracking() {
        try {
            result.cancel(false);
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isShutdown() {
        return executor.isShutdown();
    }
}
