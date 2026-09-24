package com.rikkei.b1;

import com.rikkei.b1.coordinator.TwoPhaseCommitCoordinator;
import com.rikkei.b1.dto.BookingData;
import com.rikkei.b1.dto.TwoPhaseCommitResponse;
import com.rikkei.b1.dto.VoteResult;
import com.rikkei.b1.service.TrainService;
import com.rikkei.b1.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TwoPhaseCommitTest {

    @Autowired
    private TwoPhaseCommitCoordinator coordinator;

    @Autowired
    private TrainService trainService;

    @Autowired
    private WalletService walletService;

    @Test
    public void testTwoPhaseCommitSuccess() {
        BookingData booking = new BookingData("TEST-BK-SUCCESS", "user123", "TRAIN-SE2", "SEAT-10", 50.0);
        TwoPhaseCommitResponse response = coordinator.coordinator(booking);

        assertEquals("COMMIT_SUCCESS", response.getStatus());
        assertEquals("GLOBAL_COMMIT", response.getDecision());
        assertEquals(VoteResult.VOTE_COMMIT, response.getTrainServiceVote());
        assertEquals(VoteResult.VOTE_COMMIT, response.getWalletServiceVote());
        assertEquals("COMMITTED", trainService.getSeatStatus("TEST-BK-SUCCESS"));
    }

    @Test
    public void testTwoPhaseCommitAbortInsufficientWallet() {
        BookingData booking = new BookingData("TEST-BK-ABORT-WALLET", "user_poor", "TRAIN-SE2", "SEAT-11", 5000.0);
        TwoPhaseCommitResponse response = coordinator.coordinator(booking);

        assertEquals("ABORT_ROLLBACK", response.getStatus());
        assertEquals("GLOBAL_ABORT", response.getDecision());
        assertEquals(VoteResult.VOTE_ABORT, response.getWalletServiceVote());
        assertEquals("NONE", trainService.getSeatStatus("TEST-BK-ABORT-WALLET"));
    }

    @Test
    public void testTwoPhaseCommitAbortTrainSeatTaken() {
        BookingData booking = new BookingData("TEST-BK-ABORT-TRAIN", "user123", "TRAIN-SE2", "TAKEN", 50.0);
        TwoPhaseCommitResponse response = coordinator.coordinator(booking);

        assertEquals("ABORT_ROLLBACK", response.getStatus());
        assertEquals("GLOBAL_ABORT", response.getDecision());
        assertEquals(VoteResult.VOTE_ABORT, response.getTrainServiceVote());
    }
}
