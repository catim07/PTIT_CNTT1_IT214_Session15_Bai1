package com.rikkei.b1.controller;

import com.rikkei.b1.coordinator.TwoPhaseCommitCoordinator;
import com.rikkei.b1.dto.BookingData;
import com.rikkei.b1.dto.TwoPhaseCommitResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/2pc")
public class TwoPhaseCommitController {

    private final TwoPhaseCommitCoordinator coordinator;

    public TwoPhaseCommitController(TwoPhaseCommitCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @PostMapping("/book")
    public ResponseEntity<TwoPhaseCommitResponse> bookTrainTicket(@RequestBody BookingData bookingData) {
        if (bookingData.getBookingId() == null || bookingData.getBookingId().isBlank()) {
            bookingData.setBookingId(UUID.randomUUID().toString());
        }
        TwoPhaseCommitResponse response = coordinator.coordinator(bookingData);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/demo/success")
    public ResponseEntity<TwoPhaseCommitResponse> demoSuccess() {
        BookingData booking = new BookingData(
                "BK-" + UUID.randomUUID().toString().substring(0, 8),
                "user123",
                "TRAIN-SE1",
                "SEAT-A12",
                150.0
        );
        TwoPhaseCommitResponse response = coordinator.coordinator(booking);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/demo/abort-wallet")
    public ResponseEntity<TwoPhaseCommitResponse> demoAbortWallet() {
        BookingData booking = new BookingData(
                "BK-" + UUID.randomUUID().toString().substring(0, 8),
                "user_poor",
                "TRAIN-SE1",
                "SEAT-B05",
                200.0
        );
        TwoPhaseCommitResponse response = coordinator.coordinator(booking);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/demo/abort-train")
    public ResponseEntity<TwoPhaseCommitResponse> demoAbortTrain() {
        BookingData booking = new BookingData(
                "BK-" + UUID.randomUUID().toString().substring(0, 8),
                "user123",
                "TRAIN-SE1",
                "TAKEN",
                150.0
        );
        TwoPhaseCommitResponse response = coordinator.coordinator(booking);
        return ResponseEntity.ok(response);
    }
}
