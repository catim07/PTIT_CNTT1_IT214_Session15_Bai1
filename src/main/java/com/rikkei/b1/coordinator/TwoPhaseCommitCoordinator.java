package com.rikkei.b1.coordinator;

import com.rikkei.b1.dto.BookingData;
import com.rikkei.b1.dto.TwoPhaseCommitResponse;
import com.rikkei.b1.dto.VoteResult;
import com.rikkei.b1.service.TrainService;
import com.rikkei.b1.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TwoPhaseCommitCoordinator {
    private static final Logger log = LoggerFactory.getLogger(TwoPhaseCommitCoordinator.class);

    private final TrainService trainService;
    private final WalletService walletService;

    public TwoPhaseCommitCoordinator(TrainService trainService, WalletService walletService) {
        this.trainService = trainService;
        this.walletService = walletService;
    }

    /**
     * Coordinator function executing 2PC Protocol for Train Ticket Booking
     *
     * Phase 1: Prepare Phase (Voting)
     * Phase 2: Commit / Abort Phase (Decision & Execution)
     */
    public TwoPhaseCommitResponse coordinator(BookingData bookingData) {
        log.info("==========================================================================");
        log.info("STARTING 2PC TRANSACTION FOR BOOKING ID: {}", bookingData.getBookingId());
        log.info("==========================================================================");

        // ----------------------------------------------------------------------
        // PHASE 1: PREPARE PHASE (Voting Phase)
        // ----------------------------------------------------------------------
        log.info("[COORDINATOR] Phase 1: Sending PREPARE to TrainService & WalletService...");

        VoteResult trainVote;
        VoteResult walletVote;

        try {
            trainVote = trainService.prepare(bookingData);
        } catch (Exception e) {
            log.error("[COORDINATOR] Exception during TrainService prepare: {}", e.getMessage());
            trainVote = VoteResult.VOTE_ABORT;
        }

        try {
            walletVote = walletService.prepare(bookingData);
        } catch (Exception e) {
            log.error("[COORDINATOR] Exception during WalletService prepare: {}", e.getMessage());
            walletVote = VoteResult.VOTE_ABORT;
        }

        log.info("[COORDINATOR] Phase 1 Complete. Votes Received -> TrainService: {}, WalletService: {}", trainVote, walletVote);

        // ----------------------------------------------------------------------
        // PHASE 2: COMMIT OR ABORT (DECISION PHASE)
        // ----------------------------------------------------------------------
        boolean canCommit = (trainVote == VoteResult.VOTE_COMMIT) && (walletVote == VoteResult.VOTE_COMMIT);

        if (canCommit) {
            log.info("[COORDINATOR] Phase 2 Decision: GLOBAL COMMIT! Sending COMMIT to all participants.");
            try {
                trainService.commit(bookingData);
                walletService.commit(bookingData);

                log.info("[COORDINATOR] 2PC Transaction SUCCESSFUL for Booking ID: {}", bookingData.getBookingId());
                return new TwoPhaseCommitResponse(
                        bookingData.getBookingId(),
                        "COMMIT_SUCCESS",
                        trainVote,
                        walletVote,
                        "GLOBAL_COMMIT",
                        "Train ticket booked successfully! Both 2PC phases completed."
                );
            } catch (Exception e) {
                log.error("[COORDINATOR] Critical Error during Commit Phase: {}. Initiating Emergency Rollback.", e.getMessage());
                trainService.rollback(bookingData);
                walletService.rollback(bookingData);
                return new TwoPhaseCommitResponse(
                        bookingData.getBookingId(),
                        "ABORT_ROLLBACK",
                        trainVote,
                        walletVote,
                        "GLOBAL_ABORT",
                        "Commit failed due to system error: " + e.getMessage()
                );
            }
        } else {
            log.warn("[COORDINATOR] Phase 2 Decision: GLOBAL ABORT! Sending ROLLBACK to all participants.");
            trainService.rollback(bookingData);
            walletService.rollback(bookingData);

            log.warn("[COORDINATOR] 2PC Transaction ABORTED for Booking ID: {}", bookingData.getBookingId());
            return new TwoPhaseCommitResponse(
                    bookingData.getBookingId(),
                    "ABORT_ROLLBACK",
                    trainVote,
                    walletVote,
                    "GLOBAL_ABORT",
                    "Booking aborted because one or more services voted VOTE_ABORT."
            );
        }
    }
}
