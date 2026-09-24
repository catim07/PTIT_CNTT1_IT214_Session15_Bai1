package com.rikkei.b1.service;

import com.rikkei.b1.dto.BookingData;
import com.rikkei.b1.dto.VoteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WalletService {
    private static final Logger log = LoggerFactory.getLogger(WalletService.class);

    // In-memory wallet balances
    private final Map<String, Double> userBalances = new ConcurrentHashMap<>();
    private final Map<String, Double> lockedFunds = new ConcurrentHashMap<>();

    public WalletService() {
        // Initialize default user wallet balances
        userBalances.put("user123", 500.0);
        userBalances.put("user_poor", 10.0);
    }

    public VoteResult prepare(BookingData bookingData) {
        log.info("[2PC - Phase 1: Prepare] WalletService received prepare request for Booking: {}, User: {}, Amount: ${}",
                bookingData.getBookingId(), bookingData.getUserId(), bookingData.getTicketPrice());

        double balance = userBalances.getOrDefault(bookingData.getUserId(), 0.0);
        if (balance < bookingData.getTicketPrice()) {
            log.warn("[2PC - Phase 1: Prepare] WalletService VOTE_ABORT: Insufficient balance for User {} (Current: ${}, Required: ${})",
                    bookingData.getUserId(), balance, bookingData.getTicketPrice());
            return VoteResult.VOTE_ABORT;
        }

        // Lock funds temporarily
        userBalances.put(bookingData.getUserId(), balance - bookingData.getTicketPrice());
        lockedFunds.put(bookingData.getBookingId(), bookingData.getTicketPrice());

        log.info("[2PC - Phase 1: Prepare] WalletService VOTE_COMMIT: Locked ${} for User {}. Remaining balance: ${}",
                bookingData.getTicketPrice(), bookingData.getUserId(), userBalances.get(bookingData.getUserId()));
        return VoteResult.VOTE_COMMIT;
    }

    public void commit(BookingData bookingData) {
        log.info("[2PC - Phase 2: Commit] WalletService committing booking {}: Deducting locked funds of ${}.",
                bookingData.getBookingId(), bookingData.getTicketPrice());
        lockedFunds.remove(bookingData.getBookingId());
    }

    public void rollback(BookingData bookingData) {
        Double locked = lockedFunds.remove(bookingData.getBookingId());
        if (locked != null) {
            double current = userBalances.getOrDefault(bookingData.getUserId(), 0.0);
            userBalances.put(bookingData.getUserId(), current + locked);
            log.info("[2PC - Phase 2: Rollback/Abort] WalletService rolling back booking {}: Restored ${} to User {}. Current balance: ${}",
                    bookingData.getBookingId(), locked, bookingData.getUserId(), userBalances.get(bookingData.getUserId()));
        } else {
            log.info("[2PC - Phase 2: Rollback/Abort] WalletService rolling back booking {}: No funds were locked.",
                    bookingData.getBookingId());
        }
    }

    public double getBalance(String userId) {
        return userBalances.getOrDefault(userId, 0.0);
    }
}
