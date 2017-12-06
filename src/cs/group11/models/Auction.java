package cs.group11.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs.group11.MegaDB;
import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;

public class Auction implements Validatable {

	private int id;

	private User creator;
	private int maxBids;
	private Artwork artwork;
	private double reservePrice;

	private List<Bid> bids;
	private Date creationDate;

	public Auction(User creator, int maxBids, double reservePrice, Artwork artwork) {
		this.id = 0;
		this.creationDate = new Date();

		this.creator = creator;
		this.maxBids = maxBids;
		this.reservePrice = reservePrice;
		this.artwork = artwork;

		this.validate();

		this.bids = new ArrayList<>();

		MegaDB.addAuction(this);
	}

	public Auction(int id, Date creationDate, User creator, int maxBids, double reservePrice, Artwork artwork) {
		this.id = id;
		this.creationDate = creationDate;

		this.creator = creator;
		this.maxBids = maxBids;
		this.reservePrice = reservePrice;
		this.artwork = artwork;

		this.validate();

		this.bids = new ArrayList<>();

		MegaDB.addAuction(this);
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

	public Bid getLastBid() {
		if (bids.size() == 0)
			return null;
		return bids.get(0);//top element = last bid.
	}

	public void addBid(Bid bid) {
		if (!this.bids.contains(bid)) {
			this.bids.add(bid);
		}
	}

	public void addAllBids(List<Bid> bids) {
		for (Bid bid : bids) {
			this.addBid(bid);
		}
	}

	public int getId() {
		return id;
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

	public String toCsv() {
		StringBuilder builder = new StringBuilder();

		builder.append(this.id).append(",");
		builder.append(this.creationDate.getTime()).append(",");
		builder.append(this.creator.getId()).append(",");
		builder.append(this.maxBids).append(",");
		builder.append(Double.toString(this.reservePrice)).append(",");
		builder.append(this.artwork.getId()).append(",");

		for (Bid bid : this.bids) {
			builder.append(bid.getId()).append(";");
		}
		builder.append(",");

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
}