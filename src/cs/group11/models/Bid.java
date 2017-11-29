package cs.group11.models;

import java.util.Date;

import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;

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
	public void validate() throws InvalidDataException {
		if (Validator.isNull(user)) {
			throw new InvalidDataException("No user specified for this bid.");
		}
		if (Validator.isNull(auction)) {
			throw new InvalidDataException("No auction set for this bid.");
		}
		if (auction.getBids().size() > 0) {
			Bid lastBid = auction.getLastBid();
			if (price <= lastBid.getPrice()) {
				throw new InvalidDataException("Bid price cannot be below the last Bid price");
			}
		} else if (auction.isCompleted()) {
			throw new InvalidDataException("Cannot add a Bid to a completed Auction");
		}
	}
}
