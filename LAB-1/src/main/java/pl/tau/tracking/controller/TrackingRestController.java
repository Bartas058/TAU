package pl.tau.tracking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.tau.tracking.dto.TrackingDto;
import pl.tau.tracking.service.TrackingService;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
public class TrackingRestController {

    private final TrackingService trackingService;

    @GetMapping("/{trackingNumber}")
    public ResponseEntity<TrackingDto> getTrackingByNumber(
            @PathVariable("trackingNumber") String trackingNumber) {
        return ResponseEntity.ok(trackingService.getTrackingByNumber(trackingNumber));
    }
}
