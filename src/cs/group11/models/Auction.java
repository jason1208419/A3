package cs.group11.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs.group11.helpers.InvalidDataException;
import cs.group11.interfaces.Validatable;

public class Auction implements Validatable {

    private User creator;
    private int maxBids;
    private Artwork artwork;
    private double reservePrice;

    private List<Bid> bids;
    private Date creationDate;


    public Auction(User creator, int maxBids, double reservePrice, Artwork artwork) {
        this.creator = creator;
        this.maxBids = maxBids;
        this.reservePrice = reservePrice;
        this.artwork = artwork;

        this.validate();

        this.bids = new ArrayList<>();
        this.creationDate = new Date();
    }

    public User getCreator() {
        return creator;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public int getMaxBids() {
        return maxBids;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {
        if (!this.bids.contains(bid)) {
            this.bids.add(bid);
        }
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public double getReservePrice() {
        return reservePrice;
    }

    public boolean isCompleted() {
        return this.bids.size() == this.maxBids;
    }

    @Override
    public void validate() throws InvalidDataException {
        // TODO: validate maxBids, reservePrice
    }
}
