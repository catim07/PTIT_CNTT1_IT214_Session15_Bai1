package com.rikkei.b1.dto;

public class TwoPhaseCommitResponse {
    private String bookingId;
    private String status; // COMMIT_SUCCESS or ABORT_ROLLBACK
    private VoteResult trainServiceVote;
    private VoteResult walletServiceVote;
    private String decision;
    private String message;

    public TwoPhaseCommitResponse() {
    }

    public TwoPhaseCommitResponse(String bookingId, String status, VoteResult trainServiceVote, VoteResult walletServiceVote, String decision, String message) {
        this.bookingId = bookingId;
        this.status = status;
        this.trainServiceVote = trainServiceVote;
        this.walletServiceVote = walletServiceVote;
        this.decision = decision;
        this.message = message;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VoteResult getTrainServiceVote() {
        return trainServiceVote;
    }

    public void setTrainServiceVote(VoteResult trainServiceVote) {
        this.trainServiceVote = trainServiceVote;
    }

    public VoteResult getWalletServiceVote() {
        return walletServiceVote;
    }

    public void setWalletServiceVote(VoteResult walletServiceVote) {
        this.walletServiceVote = walletServiceVote;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
