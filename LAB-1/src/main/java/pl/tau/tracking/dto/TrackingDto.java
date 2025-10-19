package pl.tau.tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.tau.tracking.entity.Carrier;
import pl.tau.tracking.entity.Status;
import pl.tau.tracking.entity.Tracking;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingDto {

    private String trackingNumber;

    private Carrier carrier;

    private Status status;

    private LocalDateTime shippedAt;

    private LocalDateTime deliveredAt;

    public static TrackingDto from(Tracking tracking) {
        return builder()
                .trackingNumber(tracking.getTrackingNumber())
                .carrier(tracking.getCarrier())
                .status(tracking.getStatus())
                .shippedAt(tracking.getShippedAt())
                .deliveredAt(tracking.getDeliveredAt())
                .build();
    }
}
