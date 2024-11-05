package com.example.eventmuziki.Models;

public class confirmPaymentModel {
    
    private String paymentId, creatorId, bidderId, amount, status, eventId, docId;
    private boolean creatorConfirmed, bidderConfirmed;

    public confirmPaymentModel() {
    }

    public confirmPaymentModel(String paymentId, String creatorId, String bidderId, String amount, String status, String eventId, String docId,
                               boolean creatorConfirmed, boolean bidderConfirmed) {
        this.paymentId = paymentId;
        this.creatorId = creatorId;
        this.bidderId = bidderId;
        this.amount = amount;
        this.status = status;
        this.eventId = eventId;
        this.docId = docId;
        this.creatorConfirmed = creatorConfirmed;
        this.bidderConfirmed = bidderConfirmed;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCreatorConfirmed() {
        return creatorConfirmed;
    }

    public void setCreatorConfirmed(boolean creatorConfirmed) {
        this.creatorConfirmed = creatorConfirmed;
    }

    public boolean isBidderConfirmed() {
        return bidderConfirmed;
    }

    public void setBidderConfirmed(boolean bidderConfirmed) {
        this.bidderConfirmed = bidderConfirmed;
    }
}
