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

import javax.jws.soap.SOAPBinding;

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

    private static int getNextId() {
        List<User> users = MegaDB.getUsers();
        User lastUser = users.get(users.size() - 1);
        if (lastUser == null) {
            return 0;
        } else {
            return lastUser.getId() + 1;
        }
    }

    public User(int id, Date lastLogin, String username, String firstname, String lastname, String telNo, Address address, String avatarPath) {
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

    public static User login(String username) {
        // set users lastLogin to "new Date()"
        return null;
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

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public List<Auction> getFavouriteAuctions() {
        return favouriteAuctions;
    }

    public List<User> getFavouriteUsers() {
        return favouriteUsers;
    }

    public void addAllFavouriteUsers(List<User> users) {
        this.favouriteUsers.addAll(users);
    }

    public void addAllFavouriteAuctions(List<Auction> auctions) {
        this.favouriteAuctions.addAll(auctions);
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

    public void addBid(Bid bid) {
        if (!this.bids.contains(bid)) {
            this.bids.add(bid);
        }
    }

    public void addCreatedAuction(Auction auction) {
        if (!this.createdAuctions.contains(auction)) {
            this.createdAuctions.add(auction);
        }
    }

    public List<Bid> getWonBids() {
        List<Bid> wonBids = new ArrayList<>();

        for (Bid bid : this.bids) {
            Auction auction = bid.getAuction();

            if (auction.isCompleted() && auction.getLastBid().getUser().equals(this)) {
                wonBids.add(bid);
            }
        }

        return wonBids;
    }

    public List<Bid> getReceivedBids() {
        List<Bid> received = new ArrayList<>();

        for (Auction createdAuction : createdAuctions) {
            List<Bid> auctionBids = createdAuction.getBids();
            received.addAll(auctionBids);
        }
        return received;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public List<Auction> getCreatedAuctions() {
        return createdAuctions;
    }

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
}