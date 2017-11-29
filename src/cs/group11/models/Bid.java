package cs.group11.models;

import cs.group11.interfaces.Validatable;

import java.util.Date;

public class Bid implements Validatable {

    private Auction auction;
    private User user;
    private double price;
    private Date creationDate;

    public Bid(double price, User user, Auction auction) {
        this.user = user;
        this.price = price;
        this.auction = auction;
        this.creationDate = new Date();

        this.validate();

        auction.addBid(this);
    }

    public User getUser() {
        return user;
    }

    public double getPrice() {
        return price;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Auction getAuction() {
        return auction;
    }

    @Override
    public void validate() throws IllegalArgumentException {
        if (this.price <= this.auction.getReservePrice()) {
            throw new IllegalArgumentException("Bid price cannot be below the Reserve price");
        } else if (this.auction.getBids().size() > 0) {
            Bid lastBid = this.auction.getBids().get(auction.getBids().size() - 1);

            if (this.price <= lastBid.getPrice()) {
                throw new IllegalArgumentException("Bid price cannot be below the last Bid price");
            }
        } else if (this.auction.isCompleted()) {
            throw new IllegalArgumentException("Cannot add a Bid to a completed Auction");
        }
    }
}
