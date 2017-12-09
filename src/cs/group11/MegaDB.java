package cs.group11;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
import cs.group11.models.User;

/**
 * @author Filippos Pantekis
 * The central database of this aplication, as a static only access class. 
 * 
 */
public final class MegaDB {

	/**
	 * The directory in which all data is stored in.
	 */
	public static final File DATA_DIR = new File("data");

	/**
	 * The file in which all user data is written.
	 */
	public static final File USER_FILE = new File(DATA_DIR, "users.csv");
	/**
	 *The file in which all auction data is written.
	 */
	public static final File AUCTION_FILE = new File(DATA_DIR, "auctions.csv");
	/**
	 * The file in which all artwork data is written.
	 */
	public static final File ARTWORK_FILE = new File(DATA_DIR, "artworks.csv");
	/**
	 *The file in which all bid data is written.
	 */
	public static final File BID_FILE = new File(DATA_DIR, "bids.csv");

	private static HashMap<Integer, Auction> auctions = new HashMap<>();
	private static HashMap<Integer, Artwork> artworks = new HashMap<>();
	private static HashMap<Integer, User> users = new HashMap<>();
	private static HashMap<Integer, Bid> bids = new HashMap<>();

	/**
	 * Private constructor to disable instantiation from outside this class (Static access class)
	 */
	private MegaDB() {
	}// Prevent instantiation of class.

	private static User loggedInUser;

	/**
	 * Log a user in the database, and get their User object back from
	 * the username provided.
	 * @param username the username to check against.
	 * @return a User object representing the profile of this user.
	 */
	public static User login(String username) {
		for (User user : getUsers()) {
			if (user.getUsername().equals(username)) {
				loggedInUser = user;
				return user;
			}
		}
		return null;
	}

	/**
	 * Log the current user out of the database.
	 */
	public static void logout() {
		loggedInUser = null;
	}

	/**
	 * Get the current user loged in.
	 * @return a User object if a user is loged on, null otherwise
	 */
	public static User getLoggedInUser() {
		return loggedInUser;
	}

	/**
	 * load data using {@link FileHandler}.
	 * To be run once, on program startup.
	 */
	public static void load() throws IOException {
		if (DATA_DIR.listFiles() == null || DATA_DIR.listFiles().length != 4) {
			// Missing file(s), invoke save to create
			save();
		}
		users = FileHandler.readUsers(USER_FILE);
		artworks = FileHandler.readArtworks(ARTWORK_FILE);
		auctions = FileHandler.readAuction(AUCTION_FILE, users, artworks);
		bids = FileHandler.readBids(BID_FILE, users, auctions);
		// Populate favourite users for the loaded users.
		FileHandler.loadFavouriteUserAuctions(USER_FILE, users, auctions);
	}

	/**
	 * Save all the data from memory to file, without removing them
	 * from memory.
	 */
	public static void save() throws IOException {
		FileHandler.writeBids(bids, BID_FILE);
		FileHandler.writeUsers(users, USER_FILE);
		FileHandler.writeArtworks(artworks, ARTWORK_FILE);
		FileHandler.writeAuction(auctions, AUCTION_FILE);
	}

	/**
	 * Get a list of all auctions.
	 * @return a clone of the data as a modifiable list.
	 */
	public static List<Auction> getAuctions() {
		return new ArrayList<>(auctions.values());
	}

	/**
	 * Get a list of all users.
	 * @return a clone of the data as a modifiable list.
	 */
	public static List<User> getUsers() {
		return new ArrayList<>(users.values());
	}

	/**
	 * Get a list of all auctions.
	 * @return a clone of the data as a modifiable list.
	 */
	public static List<Artwork> getArtworks() {
		return new ArrayList<>(artworks.values());
	}

	/**
	 * Get a list of all bids.
	 *
	 * @return a clone of the data as a modifiable list.
	 */
	public static List<Bid> getBids() {
		return new ArrayList<>(bids.values());
	}

	/**
	 * Add an auction to the database
	 * Warning: Same ids will result in overrides...
	 */
	public static void addAuction(Auction toAdd) {
		auctions.put(toAdd.getId(), toAdd);
	}

	/**
	 * Add a user to the database
	 * Warning: Same ids will result in overrides...
	 */
	public static void addUser(User toAdd) {
		users.put(toAdd.getId(), toAdd);
	}

	/**
	 * Add an artwork to the database
	 * Warning: Same ids will result in overrides...
	 */
	public static void addArtwork(Artwork toAdd) {
		artworks.put(toAdd.getId(), toAdd);
	}

	/**
	 * Add a bid to the database
	 * Warning: Same ids will result in overrides...
	 */
	public static void addBid(Bid toAdd) {
		bids.put(toAdd.getId(), toAdd);
	}

	@Deprecated // Added for test reasons only;
	public static void clear() {
		users.clear();
		auctions.clear();
		bids.clear();
		artworks.clear();
	}
}
