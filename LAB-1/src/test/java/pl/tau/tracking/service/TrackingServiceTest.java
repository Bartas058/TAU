package pl.tau.tracking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tau.tracking.dto.TrackingDto;
import pl.tau.tracking.entity.Carrier;
import pl.tau.tracking.entity.Status;
import pl.tau.tracking.entity.Tracking;
import pl.tau.tracking.exception.TrackingNotFoundException;
import pl.tau.tracking.repository.TrackingRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackingServiceTest {

    @Mock
    TrackingRepository trackingRepository;

    @InjectMocks
    TrackingService trackingService;

    private Tracking sampleTracking(
            String tracking, Carrier carrier, Status status,
            LocalDateTime shippedAt, LocalDateTime deliveredAt) {

        return Tracking.builder()
                .id(1L)
                .trackingNumber(tracking)
                .carrier(carrier)
                .status(status)
                .shippedAt(shippedAt)
                .deliveredAt(deliveredAt)
                .build();
    }

    @Test
    void getTrackingByNumber_found_returnsDto() {
        Tracking entity = sampleTracking(
                "TRK-2222-BBBB", Carrier.UPS, Status.SHIPPED,
                LocalDateTime.of(2025,10,1,10,15), null);

        when(trackingRepository.findByTrackingNumber("TRK-2222-BBBB"))
                .thenReturn(Optional.of(entity));

        TrackingDto dto = trackingService.getTrackingByNumber("TRK-2222-BBBB");

        assertNotNull(dto);
        assertEquals("TRK-2222-BBBB", dto.getTrackingNumber());
        verify(trackingRepository, times(1)).findByTrackingNumber("TRK-2222-BBBB");
        verifyNoMoreInteractions(trackingRepository);
    }

    @Test
    void getTrackingByNumber_mapsAllFields() {
        LocalDateTime shipped = LocalDateTime.of(2025, 9, 30, 14, 20);
        Tracking entity = sampleTracking("TRK-6666-FFFF", Carrier.DHL, Status.SHIPPED, shipped, null);

        when(trackingRepository.findByTrackingNumber("TRK-6666-FFFF"))
                .thenReturn(Optional.of(entity));

        TrackingDto dto = trackingService.getTrackingByNumber("TRK-6666-FFFF");

        assertEquals(Carrier.DHL, dto.getCarrier());
        assertEquals(Status.SHIPPED, dto.getStatus());
        assertEquals(shipped, dto.getShippedAt());
        assertNull(dto.getDeliveredAt());
    }

    @Test
    void getTrackingByNumber_notFound_throwsExceptionWithMessage() {
        when(trackingRepository.findByTrackingNumber("TRK-0000-XXXX"))
                .thenReturn(Optional.empty());

        try {
            trackingService.getTrackingByNumber("TRK-0000-XXXX");
            fail("TrackingNotFoundException expected");
        } catch (TrackingNotFoundException ex) {
            assertThat(ex.getMessage(), containsString("TRK-0000-XXXX"));
        }
    }

    @Test
    void getTrackingByNumber_passesExactArgumentToRepository() {
        Tracking entity = sampleTracking("TRK-1111-AAAA", Carrier.DHL, Status.PENDING, null, null);

        when(trackingRepository.findByTrackingNumber(anyString()))
                .thenReturn(Optional.of(entity));

        trackingService.getTrackingByNumber("  TRK-1111-AAAA ");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(trackingRepository).findByTrackingNumber(captor.capture());

        String passed = captor.getValue();
        assertEquals("  TRK-1111-AAAA ", passed);
        assertNotEquals("TRK-1111-AAAA", passed);
    }

    @Test
    void getTrackingByNumber_delivered_hasDeliveredAt() {
        LocalDateTime shipped = LocalDateTime.of(2025, 9, 28, 9, 0);
        LocalDateTime delivered = LocalDateTime.of(2025, 10, 2, 15, 30);
        Tracking entity = sampleTracking("TRK-3333-CCCC", Carrier.INPOST, Status.DELIVERED, shipped, delivered);

        when(trackingRepository.findByTrackingNumber("TRK-3333-CCCC"))
                .thenReturn(Optional.of(entity));

        TrackingDto dto = trackingService.getTrackingByNumber("TRK-3333-CCCC");

        assertNotNull(dto.getDeliveredAt());
        assertEquals(delivered, dto.getDeliveredAt());
    }

    @Test
    void getTrackingByNumber_pending_hasNullDates() {
        Tracking entity = sampleTracking("TRK-5555-EEEE", Carrier.FEDEX, Status.PENDING, null, null);

        when(trackingRepository.findByTrackingNumber("TRK-5555-EEEE"))
                .thenReturn(Optional.of(entity));

        TrackingDto dto = trackingService.getTrackingByNumber("TRK-5555-EEEE");

        assertNull(dto.getShippedAt());
        assertNull(dto.getDeliveredAt());
    }

    @Test
    void getTrackingByNumber_returnsDifferentObjectThanEntity() {
        Tracking entity = sampleTracking("TRK-8888-HHHH", Carrier.UPS, Status.DELIVERED,
                LocalDateTime.of(2025, 9, 26, 13, 0),
                LocalDateTime.of(2025, 9, 29, 10, 30));

        when(trackingRepository.findByTrackingNumber("TRK-8888-HHHH"))
                .thenReturn(Optional.of(entity));

        TrackingDto dto = trackingService.getTrackingByNumber("TRK-8888-HHHH");

        assertEquals(entity.getTrackingNumber(), dto.getTrackingNumber());
        assertEquals(entity.getCarrier(), dto.getCarrier());
        assertEquals(entity.getStatus(), dto.getStatus());
    }

    @Test
    void getTrackingByNumber_notFound_messageStartsWith() {
        when(trackingRepository.findByTrackingNumber("TRK-4444-ZZZZ"))
                .thenReturn(Optional.empty());

        try {
            trackingService.getTrackingByNumber("TRK-4444-ZZZZ");
            fail("ShipmentNotFoundException expected");
        } catch (TrackingNotFoundException ex) {
            assertTrue(ex.getMessage().startsWith("Shipment with tracking number"));
        }
    }

    @Test
    void getTrackingByNumber_supportsDifferentCarrierAndStatus() {
        Tracking dpdCancelled = sampleTracking("TRK-4444-DDDD", Carrier.DPD, Status.CANCELLED,
                LocalDateTime.of(2025, 9, 29, 8, 45), null);

        when(trackingRepository.findByTrackingNumber("TRK-4444-DDDD"))
                .thenReturn(Optional.of(dpdCancelled));

        TrackingDto dto = trackingService.getTrackingByNumber("TRK-4444-DDDD");

        assertEquals(Carrier.DPD, dto.getCarrier());
        assertEquals(Status.CANCELLED, dto.getStatus());
    }

    @Test
    void getTrackingByNumber_repositoryInteractionOnceOnly() {
        Tracking entity = sampleTracking("TRK-0000-JJJJ", Carrier.DPD, Status.SHIPPED,
                LocalDateTime.of(2025, 10, 3, 12, 45), null);

        when(trackingRepository.findByTrackingNumber("TRK-0000-JJJJ"))
                .thenReturn(Optional.of(entity));

        trackingService.getTrackingByNumber("TRK-0000-JJJJ");

        verify(trackingRepository, times(1)).findByTrackingNumber("TRK-0000-JJJJ");
        verifyNoMoreInteractions(trackingRepository);
    }
}
