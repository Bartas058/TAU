package pl.tau.tracking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tau.tracking.dto.TrackingDto;
import pl.tau.tracking.exception.TrackingNotFoundException;
import pl.tau.tracking.repository.TrackingRepository;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingRepository trackingRepository;

    public TrackingDto getTrackingByNumber(String trackingNumber) {
        return trackingRepository.findByTrackingNumber(trackingNumber)
                .map(TrackingDto::from)
                .orElseThrow(() -> new TrackingNotFoundException("Shipment with tracking number " + trackingNumber + " not found"));
    }
}
