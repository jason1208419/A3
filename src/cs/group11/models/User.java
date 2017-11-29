package cs.group11.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;
import javafx.scene.image.Image;

public class User implements Validatable {

	private int id;
	private Date lastLogin;

	private String username;
	private String firstname;
	private String lastname;
	private String telNo;
	private Address address;
	private Image avatar;

	private List<User> favouriteUsers;
	private List<Auction> favouriteAuctions;

	public User(String username, String firstname, String lastname, String telNo, Address address, Image avatar) {
		this.id = 0;
		this.lastLogin = new Date();

		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.telNo = telNo;
		this.address = address;
		this.avatar = avatar;

		this.validate();

		this.favouriteUsers = new ArrayList<>();
		this.favouriteAuctions = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public Address getAddress() {
		return address;
	}

	public Image getAvatar() {
		return avatar;
	}

	public void setAvatar(Image avatar) {
		this.avatar = avatar;
	}

	public List<Auction> getFavouriteAuctions() {
		return favouriteAuctions;
	}

	public List<User> getFavouriteUsers() {
		return favouriteUsers;
	}

	public void addFavouriteUser(User user) {
		this.favouriteUsers.add(user);
	}

	public void removeFavouriteUser(User user) {
		this.favouriteUsers.remove(user);
	}

	public void addFavouriteAuction(Auction auction) {
		this.favouriteAuctions.add(auction);
	}

	public void removeFavouriteAuction(Auction auction) {
		this.favouriteAuctions.remove(auction);
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
		if (Validator.isNull(avatar)) {
			throw new InvalidDataException("No avatar has been selected!");
		}
		if (Validator.isNull(address)) {
			throw new InvalidDataException("A valid UK address has not been set.");
		}
		address.validate();
	}
}
