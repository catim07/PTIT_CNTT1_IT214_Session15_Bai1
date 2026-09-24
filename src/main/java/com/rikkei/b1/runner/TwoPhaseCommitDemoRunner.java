package com.rikkei.b1.runner;

import com.rikkei.b1.coordinator.TwoPhaseCommitCoordinator;
import com.rikkei.b1.dto.BookingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TwoPhaseCommitDemoRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(TwoPhaseCommitDemoRunner.class);
    private final TwoPhaseCommitCoordinator coordinator;

    public TwoPhaseCommitDemoRunner(TwoPhaseCommitCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("\n==========================================================================");
        log.info("RUNNING 2PC (TWO-PHASE COMMIT) PROTOCOL SIMULATION DEMO");
        log.info("==========================================================================");

        // Demo Scenario 1: Success (Commit Flow)
        log.info("\n>>> DEMO SCENARIO 1: SUCCESSFUL BOOKING (VOTE_COMMIT -> COMMIT)");
        BookingData successBooking = new BookingData("BK-2PC-SUCCESS-001", "user123", "TRAIN-SE1", "SEAT-15", 100.0);
        coordinator.coordinator(successBooking);

        // Demo Scenario 2: Abort due to Insufficient Wallet Balance
        log.info("\n>>> DEMO SCENARIO 2: ABORTED BOOKING - INSUFFICIENT WALLET (VOTE_ABORT -> ROLLBACK)");
        BookingData poorBooking = new BookingData("BK-2PC-ABORT-WALLET-002", "user_poor", "TRAIN-SE1", "SEAT-16", 500.0);
        coordinator.coordinator(poorBooking);

        // Demo Scenario 3: Abort due to Seat Unavailable
        log.info("\n>>> DEMO SCENARIO 3: ABORTED BOOKING - SEAT TAKEN (VOTE_ABORT -> ROLLBACK)");
        BookingData takenBooking = new BookingData("BK-2PC-ABORT-TRAIN-003", "user123", "TRAIN-SE1", "TAKEN", 100.0);
        coordinator.coordinator(takenBooking);

        log.info("\n==========================================================================");
        log.info("2PC DEMO COMPLETE");
        log.info("==========================================================================");
    }
}
