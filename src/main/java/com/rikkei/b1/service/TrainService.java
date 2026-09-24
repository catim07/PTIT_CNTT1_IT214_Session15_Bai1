package com.rikkei.b1.service;

import com.rikkei.b1.dto.BookingData;
import com.rikkei.b1.dto.VoteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TrainService {
    private static final Logger log = LoggerFactory.getLogger(TrainService.class);

    // In-memory simulation of train seat status: trainId -> seatNo -> status
    private final Map<String, String> reservedSeats = new ConcurrentHashMap<>();

    public VoteResult prepare(BookingData bookingData) {
        log.info("[2PC - Phase 1: Prepare] TrainService received prepare request for Booking: {}, Train: {}, Seat: {}",
                bookingData.getBookingId(), bookingData.getTrainId(), bookingData.getSeatNo());

        // Simulated validation: Seat "FULL" or "TAKEN" will cause VOTE_ABORT
        if ("TAKEN".equalsIgnoreCase(bookingData.getSeatNo()) || "FULL".equalsIgnoreCase(bookingData.getTrainId())) {
            log.warn("[2PC - Phase 1: Prepare] TrainService VOTE_ABORT: Seat {} on Train {} is already reserved!",
                    bookingData.getSeatNo(), bookingData.getTrainId());
            return VoteResult.VOTE_ABORT;
        }

        // Temporary hold
        reservedSeats.put(bookingData.getBookingId(), "PREPARED");
        log.info("[2PC - Phase 1: Prepare] TrainService VOTE_COMMIT: Seat {} on Train {} held temporarily.",
                bookingData.getSeatNo(), bookingData.getTrainId());
        return VoteResult.VOTE_COMMIT;
    }

    public void commit(BookingData bookingData) {
        log.info("[2PC - Phase 2: Commit] TrainService committing booking {}: Seat {} on Train {} confirmed.",
                bookingData.getBookingId(), bookingData.getSeatNo(), bookingData.getTrainId());
        reservedSeats.put(bookingData.getBookingId(), "COMMITTED");
    }

    public void rollback(BookingData bookingData) {
        log.info("[2PC - Phase 2: Rollback/Abort] TrainService rolling back booking {}: Releasing seat {}.",
                bookingData.getBookingId(), bookingData.getSeatNo());
        reservedSeats.remove(bookingData.getBookingId());
    }

    public String getSeatStatus(String bookingId) {
        return reservedSeats.getOrDefault(bookingId, "NONE");
    }
}
