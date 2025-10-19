package pl.tau.tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tau.tracking.entity.Tracking;

import java.util.Optional;

public interface TrackingRepository extends JpaRepository<Tracking, Long> {

    Optional<Tracking> findByTrackingNumber(String trackingNumber);
}
