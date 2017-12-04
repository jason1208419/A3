package cs.group11.models;

import java.util.Date;

import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;

public class Bid implements Validatable {

	private int id;

	private Auction auction;
	private User user;
	private double price;
	private Date creationDate;

	public Bid(int id, Date creationDate, double price, User user, Auction auction) {
		this.id = id;
		this.user = user;
		this.price = price;
		this.auction = auction;
		this.creationDate = creationDate;

		this.validate();

		auction.addBid(this);
	}

	public Bid(double price, User user, Auction auction) {
		this.id = 0;
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

	public int getId() {
		return id;
	}

	@Override
	public void validate() throws InvalidDataException {
		if (Validator.isNull(user)) {
			throw new InvalidDataException("No user specified for this bid.");
		}
		if (Validator.isNull(auction)) {
			throw new InvalidDataException("No auction set for this bid.");
		}
	}
}
