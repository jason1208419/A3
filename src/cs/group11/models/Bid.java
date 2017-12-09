package cs.group11.models;

import java.util.Date;
import java.util.List;

import cs.group11.MegaDB;
import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;

public class Bid implements Validatable {

	private int id;

	private Auction auction;
	private User user;
	private double price;
	private Date creationDate;

	public Bid(double price, User user, Auction auction) {
		this.id = getNextId();

		this.user = user;
		this.price = price;
		this.auction = auction;
		this.creationDate = new Date();

		this.validate();

		auction.addBid(this);
		user.addBid(this);
		MegaDB.addBid(this);
	}


	public Bid(int id, Date creationDate, double price, User user, Auction auction) {
		this.id = id;
		this.user = user;
		this.price = price;
		this.auction = auction;
		this.creationDate = creationDate;

		this.validate();

		auction.addBid(this);
		user.addBid(this);
		MegaDB.addBid(this);
	}

	private static int getNextId() {
		List<Bid> bids = MegaDB.getBids();

		if (bids.size() == 0) {
			return 0;
		}

		Bid lastBid = bids.get(bids.size() - 1);
		return lastBid.getId() + 1;
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

	public int getId() {
		return id;
	}

	public String toCsv() {
		StringBuilder builder = new StringBuilder();

		builder.append(this.getId()).append(",");
		builder.append(this.creationDate.getTime()).append(",");
		builder.append(this.user.getId()).append(",");
		builder.append(this.price).append(",");
		builder.append(this.auction.getId()).append(",");

		return builder.toString();
	}

	@Override
	public void validate() throws InvalidDataException {
		if (Validator.isNull(user)) {
			throw new InvalidDataException("No user specified for this bid.");
		}
		if (Validator.isNull(auction)) {
			throw new InvalidDataException("No auction set for this bid.");
		}

		Bid lastBid = this.auction.getLastBid();

		if (lastBid != null && lastBid.getId() == this.user.getId()) {
            throw new InvalidDataException("You're already the highest bidder.");
        }

        if (lastBid != null && this.price <= lastBid.getPrice()) {
            throw new InvalidDataException("Bid must be greater than the previous bid.");
        }

        if (lastBid != null && this.price <= this.auction.getReservePrice()) {
            throw new InvalidDataException("Bid must be greater than the reserve price.");
        }

        if (this.auction.isCompleted()) {
            throw new InvalidDataException("Auction is finished. No more bids accepted.");
        }

	}
}
