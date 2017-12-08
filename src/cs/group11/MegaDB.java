package cs.group11;

import java.io.File;
import java.io.IOException;
import java.util.*;

import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
import cs.group11.models.User;

public final class MegaDB {

	private static final File DATA_DIR = new File("data");

	public static final File USER_FILE = new File(DATA_DIR, "users.csv");
	public static final File AUCTION_FILE = new File(DATA_DIR, "auctions.csv");
	public static final File ARTWORK_FILE = new File(DATA_DIR, "artworks.csv");
	public static final File BID_FILE = new File(DATA_DIR, "bids.csv");

	private static HashMap<Integer, Auction> auctions = new HashMap<>();
	private static HashMap<Integer, Artwork> artworks = new HashMap<>();
	private static HashMap<Integer, User> users = new HashMap<>();
	private static HashMap<Integer, Bid> bids = new HashMap<>();

	private MegaDB() {
	}// Prevent instantiation of class.

	public static void load() throws IOException {
		if (DATA_DIR.listFiles() == null || DATA_DIR.listFiles().length != 4) {
			// Missing file(s), invoke save to create
			save();
		}
		users = FileHandler.readUsers(USER_FILE);
		artworks = FileHandler.readArtworks(ARTWORK_FILE);
		auctions = FileHandler.readAuction(AUCTION_FILE, users, artworks);
		bids = FileHandler.readBids(BID_FILE, users, auctions);

		FileHandler.loadFavouriteUserAuctions(USER_FILE, users, auctions);
	}

	public static void save() throws IOException {
		FileHandler.writeBids(bids, BID_FILE);
		FileHandler.writeUsers(users, USER_FILE);
		FileHandler.writeArtworks(artworks, ARTWORK_FILE);
		FileHandler.writeAuction(auctions, AUCTION_FILE);
	}

	public static Collection<Auction> getAuctions() {
		return Collections.unmodifiableCollection(auctions.values());
	}

	public static Collection<User> getUsers() {
		return Collections.unmodifiableCollection(users.values());
	}

	public static Collection<Artwork> getArtworks() {
		return Collections.unmodifiableCollection(artworks.values());
	}

	public static void addAuction(Auction toAdd) {
		toAdd.validate();// Only store valid data in the databse
		auctions.put(toAdd.getId(), toAdd);
	}

	public static void addUser(User toAdd) {
		toAdd.validate();
		users.put(toAdd.getId(), toAdd);
	}

	public static void addArtwork(Artwork toAdd) {
		toAdd.validate();
		artworks.put(toAdd.getId(), toAdd);
	}

	public static void addBid(Bid toAdd) {
		toAdd.validate();
		bids.put(toAdd.getId(), toAdd);
	}

	@Deprecated // Added for test reasons only;
	public static void clear() {
		users.clear();
		auctions.clear();
		bids.clear();
		artworks.clear();
	}
	// REMOVED, UNESSESARY!
	/*
	 * public static Collection<User> searchByUser(String input) { Set<User> results
	 * = new HashSet<>(); for (User user : users.values()) { if
	 * (user.getUsername().contains(input) || user.getFirstname().contains(input) ||
	 * user.getLastname().contains(input)) { results.add(user); } } return results;
	 * }
	 * 
	 * public static Collection<Auction> searchByAuction(String input) {
	 * Set<Auction> results = new HashSet<>(); for (Auction auc : auctions.values())
	 * { if (auc.getCreator().getUsername().contains(input) ||
	 * auc.getArtwork().getName().contains(input)) { results.add(auc); } } return
	 * results; }
	 * 
	 * DELETE IF NOT NEEDED
	 * 
	 * public static Collection<Auction> searchFinishedAuction(String input) {
	 * Set<Auction> results = new HashSet<>(); for (Auction auc : auctions) { if
	 * (auc.isCompleted()) { results.add(auc); } } return results; }
	 */

}
