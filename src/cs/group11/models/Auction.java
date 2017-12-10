package cs.group11.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs.group11.MegaDB;
import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;

/**
 * This class represent an auction storing any necessary information of an auction
 *
 * @Author Nasir
 */
public class Auction implements Validatable {

    private int id;

    private User creator;
    private int maxBids;
    private Artwork artwork;
    private double reservePrice;

    //list of bids made by users of this auction
    private List<Bid> bids;
    private Date creationDate;

    /**
     * To initialize an auction
     * @param creator the creator of the auction
     * @param maxBids the maximum amount of bids that can be made by users of the auction
     * @param reservePrice The staring price of the auction
     * @param artwork The artwork for bidding of the auction
     */
    public Auction(User creator, int maxBids, double reservePrice, Artwork artwork) {
        this.id = getNextId();

        this.creationDate = new Date();

        this.creator = creator;
        this.maxBids = maxBids;
        this.reservePrice = reservePrice;
        this.artwork = artwork;

        this.validate();

        this.bids = new ArrayList<>();
        creator.addCreatedAuction(this);

        MegaDB.addAuction(this);
    }

    /**
     * To initialize an auction
     * @param id the unique id of the auction
     * @param creationDate the date and time of creation of the auction
     * @param creator the creator of the auction
     * @param maxBids the maximum amount of bids that can be made by users of the auction
     * @param reservePrice The staring price of the auction
     * @param artwork The artwork for bidding of the auction
     */
    public Auction(int id, Date creationDate, User creator, int maxBids, double reservePrice, Artwork artwork) {
        this.id = id;
        this.creationDate = creationDate;

        this.creator = creator;
        this.maxBids = maxBids;
        this.reservePrice = reservePrice;
        this.artwork = artwork;

        this.validate();

        this.bids = new ArrayList<>();
        creator.addCreatedAuction(this);

        MegaDB.addAuction(this);
    }

    /**
     * Create an unique id for the auction
     *
     * @return the unique id
     */
    private static int getNextId() {
        List<Auction> auctions = MegaDB.getAuctions();

        //if there is no auction, assign id 0 to the auction
        if (auctions.size() == 0) {
            return 0;
        }

        Auction lastAuction = auctions.get(auctions.size() - 1);
        return lastAuction.getId() + 1;
    }

    /**
     * Get the creator of this auction
     * @return the creator of this auction
     */
    public User getCreator() {
        return creator;
    }

    /**
     * Get the artwork of this auction
     * @return the artwork of this auction
     */
    public Artwork getArtwork() {
        return artwork;
    }

    /**
     * Get the maximum amount of bid that can be made by users of this auction
     * @return the maximum amount of bid
     */
    public int getMaxBids() {
        return maxBids;
    }

    /**
     * Get a list of bids that have been made by users of this auction
     * @return a list of bids
     */
    public List<Bid> getBids() {
        return bids;
    }

    /**
     * Get the last bid made of this auction
     * @return the last bid made
     */
    public Bid getLastBid() {
        if (bids.size() == 0)
            return null;
        return bids.get(bids.size() - 1);// top element = last bid.
    }

    /**
     * Add a bid to the bid list
     * @param bid the bid to be added
     */
    public void addBid(Bid bid) {
        if (!this.bids.contains(bid)) {
            this.bids.add(bid);
        }
    }

    /**
     * replace with a list of bids to this auction
     * @param bids the list of bids to be added
     */
    public void addAllBids(List<Bid> bids) {
        for (Bid bid : bids) {
            this.addBid(bid);
        }
    }

    /**
     * Get the auction id
     * @return the id of this auction
     */
    public int getId() {
        return id;
    }

    /**
     * Get the creation date of this auction
     * @return the creation date of this auction
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Get the staring price of this auction
     * @return the starting price of this auction
     */
    public double getReservePrice() {
        return reservePrice;
    }

    /**
     * Check if this auction completed
     * @return true if completed; false otherwise
     */
    public boolean isCompleted() {
        return this.bids.size() == this.maxBids;
    }

    /**
     * Convert auction into a storable format
     * @return the converted format
     */
    public String toCsv() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.id).append(",");
        builder.append(this.creationDate.getTime()).append(",");
        builder.append(this.creator.getId()).append(",");
        builder.append(this.maxBids).append(",");
        builder.append(Double.toString(this.reservePrice)).append(",");
        builder.append(this.artwork.getId()).append(",");

        return builder.toString();
    }

    @Override
    public void validate() throws InvalidDataException {
        if (Validator.isNull(artwork)) {
            throw new InvalidDataException("No artwork specified for auction!");
        }
        artwork.validate();
        if (Validator.isNull(creator)) {
            throw new InvalidDataException("No creator specified for auction!");
        }
        if (!Validator.isIntValid(maxBids, Integer.MAX_VALUE, 1)) {
            throw new InvalidDataException("Invalid number of maximum bids! Value must be >= 1");
        }
        if (Validator.isNegative(reservePrice)) {
            throw new InvalidDataException("Negative reserve price not permited. Reserve price must be >= 0");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Auction)) {
            return false;
        }
        Auction other = (Auction) obj;
        if (!artwork.equals(other.artwork)) {
            return false;
        }
        if (!creationDate.equals(other.creationDate)) {
            return false;
        }
        if (!creator.equals(other.creator)) {
            return false;
        }
        if (maxBids != other.maxBids) {
            return false;
        }
        if ((int) reservePrice != (int) other.reservePrice) {
            return false;
        }
        return true;
    }

}