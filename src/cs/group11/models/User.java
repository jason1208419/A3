package cs.group11.models;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs.group11.FileHandler;
import cs.group11.MegaDB;
import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;

/**
 * A user of the system
 * @author Nasir
 */
public class User implements Validatable, Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private Date lastLogin;

	private String username;
	private String firstname;
	private String lastname;
	private String telNo;
	private Address address;
	private String avatarPath;

	private List<User> favouriteUsers;
	private List<Auction> favouriteAuctions;
	private List<Bid> bids;
	private List<Auction> createdAuctions;

	/**
	 * Constructs a user.
	 * @param username The username of the user.
	 * @param firstname The first name of the user.
	 * @param lastname The last name of the user.
	 * @param telNo The telephone number of the user.
	 * @param address The address of the user.
	 * @param avatarPath The filepath of the user's avatar
	 */
	public User(String username, String firstname, String lastname, String telNo, Address address, String avatarPath) {
		this.id = getNextId();

		this.lastLogin = new Date();
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.telNo = telNo;
		this.address = address;
		this.avatarPath = avatarPath;

		this.validate();

		this.favouriteUsers = new ArrayList<>();
		this.favouriteAuctions = new ArrayList<>();
		this.bids = new ArrayList<>();
		this.createdAuctions = new ArrayList<>();
		MegaDB.addUser(this);
	}

	/**
	 * Assigns the user a unique id.
	 * @return The user's id to be asssigned.
	 */
	private static int getNextId() {
		List<User> users = MegaDB.getUsers();

		if (users.size() == 0) {
			return 0;
		}

		User lastUser = users.get(users.size() - 1);
		return lastUser.getId() + 1;
	}

	/**
	 * Constructs a user with an id.
	 * @param id The unique id of the user
	 * @param lastLogin The date the user last logged in.
	 * @param username The username of the user.
	 * @param firstname The first name of the user.
	 * @param lastname The last name of the user.
	 * @param telNo The telephone number of the user.
	 * @param address The address of the user.
	 * @param avatarPath The avatar of the user.
	 */
	public User(int id, Date lastLogin, String username, String firstname, String lastname, String telNo,
			Address address, String avatarPath) {
		this.id = id;
		this.lastLogin = lastLogin;

		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.telNo = telNo;
		this.address = address;
		this.avatarPath = avatarPath;

		this.validate();

		this.favouriteUsers = new ArrayList<>();
		this.favouriteAuctions = new ArrayList<>();
		this.bids = new ArrayList<>();
		this.createdAuctions = new ArrayList<>();
		MegaDB.addUser(this);
	}

	/**
	 * @return Returns the id of the user.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return Returns the last time the user logged in.
	 */
	public Date getLastLogin() {
		return lastLogin;
	}

	/**
	 * Sets the date the user last logged in.
	 * @param lastLogin the date the user last logged in.
	 */
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @return Returns the username of the user.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username of the user.
	 * @param username The username of the user.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return Returns the first name of the user.
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * Sets the first name of the user.
	 * @param firstname The first name of the user.
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return Returns the last name of the user.
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * Sets the last name of the user.
	 * @param lastname The last name of the user.
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return Returns the user's telephone number
	 */
	public String getTelNo() {
		return telNo;
	}

	/**
	 * Sets the telephone number of the user.
	 * @param telNo The user's telephone number.
	 */
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	/**
	 * @return Returns the user's address.
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @return Returns the path of the user's avatar.
	 */
	public String getAvatarPath() {
		return avatarPath;
	}

	/**
	 * Sets the path of the user's avatar.
	 * @param avatarPath The avatar path of the user.
	 */
	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	/**
	 * @return Returns the user's favourite auctions.
	 */
	public List<Auction> getFavouriteAuctions() {
		return favouriteAuctions;
	}

	/**
	 * @return Returns the logged in user's favourite users.
	 */
	public List<User> getFavouriteUsers() {
		return favouriteUsers;
	}

	/**
	 * Adds a group of users to the user's favourite users.
	 * @param users The users to be added.
	 */
	public void addAllFavouriteUsers(List<User> users) {
		this.favouriteUsers.addAll(users);
	}

	/**
	 * Adds a group of auctions to the user's favourite auctions.
	 * @param auctions The auctions to be added.
	 */
	public void addAllFavouriteAuctions(List<Auction> auctions) {
		this.favouriteAuctions.addAll(auctions);
	}

	/**
	 * Adds a favourite user to the logged in user's favourite users.
	 * @param user The user to be added.
	 */
	public void addFavouriteUser(User user) {
		this.favouriteUsers.add(user);
	}

	/**
	 * Removes a favourite user from the logged in user's favourite users.
	 * @param user The user to be removed.
	 */
	public void removeFavouriteUser(User user) {
		this.favouriteUsers.remove(user);
	}

	/**
	 * Adds a favourite auction to the logged in user's favourite auctions.
	 * @param auction The auction to be added.
	 */
	public void addFavouriteAuction(Auction auction) {
		this.favouriteAuctions.add(auction);
	}

	/**
	 * Removes a favourite auction from the logged in user's favourite auctions.
	 * @param auction The auction to be removed.
	 */
	public void removeFavouriteAuction(Auction auction) {
		this.favouriteAuctions.remove(auction);
	}

	/**
	 * Stores a new bid placed by the user.
	 * @param bid The bid to be stored.
	 */
	public void addBid(Bid bid) {
		if (!this.bids.contains(bid)) {
			this.bids.add(bid);
		}
	}

	/**
	 * Stores a new auction created by the user..
	 * @param auction The auction to be stored.
	 */
	public void addCreatedAuction(Auction auction) {
		if (!this.createdAuctions.contains(auction)) {
			this.createdAuctions.add(auction);
		}
	}

	/**
	 * @return Returns all the bids that won the user an auction.
	 */
	public List<Bid> getWonBids() {
		List<Bid> wonBids = new ArrayList<>();

		for (Bid bid : this.bids) {
			Auction auction = bid.getAuction();

			if (!wonBids.contains(bid) && auction.isCompleted() && auction.getLastBid().getUser().equals(this)) {
				wonBids.add(auction.getLastBid());
			}
		}

		return wonBids;
	}

	/**
	 * @return Return the list of bids people have placed on a user's auction.
	 */
	public List<Bid> getReceivedBids() {
		List<Bid> received = new ArrayList<>();

		for (Auction createdAuction : createdAuctions) {
			List<Bid> auctionBids = createdAuction.getBids();
			received.addAll(auctionBids);
		}
		return received;
	}

	/**
	 * @return Returns the list of bids the user has placed.
	 */
	public List<Bid> getBids() {
		return bids;
	}

	/**
	 * @return Returns the list of auctions the user has created.
	 */
	public List<Auction> getCreatedAuctions() {
		return createdAuctions;
	}

	/**
	 * @return Returns a user's information formatted into a Csv storable format.
	 */
	public String toCsv() {
		StringBuilder builder = new StringBuilder();

		builder.append(this.id).append(",");
		builder.append(this.lastLogin.getTime()).append(",");
		builder.append(this.username).append(",");
		builder.append(FileHandler.escape(this.firstname)).append(",");
		builder.append(FileHandler.escape(this.lastname)).append(",");
		builder.append(this.telNo).append(",");
		builder.append(this.address.toCsv());
		builder.append(FileHandler.escape(this.avatarPath)).append(",");

		if (this.favouriteUsers.size() == 0) {
			builder.append("[]");
		} else {
			for (User user : this.favouriteUsers) {
				builder.append(user.getId()).append(";");
			}
		}
		builder.append(",");

		if (this.favouriteAuctions.size() == 0) {
			builder.append("[]");
		} else {
			for (Auction auction : this.favouriteAuctions) {
				builder.append(auction.getId()).append(";");
			}
		}
		builder.append(",");

		String str = builder.toString();
		return str;
	}

	/**
	 * Writes information stored in the system to the relevant files.
	 * @throws IOException Thrown if there are any errors writing to the file.
	 */
	public void save() throws IOException {
		MegaDB.save();
	}

	@Override
	public void validate() throws InvalidDataException {
		if (Validator.isStringEmpty(username)) {
			throw new InvalidDataException("No username specified!");
		}
		if (Validator.isStringEmpty(firstname)) {
			throw new InvalidDataException("No firstname provided!");
		}
		if (Validator.isStringEmpty(lastname)) {
			throw new InvalidDataException("No lastname specified!");
		}
		if (Validator.isStringEmpty(telNo)) {
			throw new InvalidDataException("No telephone number provided!");
		}
		if (Validator.isStringEmpty(avatarPath)) {
			throw new InvalidDataException("No avatar has been selected!");
		}
		if (Validator.isNull(address)) {
			throw new InvalidDataException("A valid UK address has not been set.");
		}
		address.validate();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof User) {
				return ((User) obj).getUsername().equals(getUsername());
			}
		}
		return false;
	}

}