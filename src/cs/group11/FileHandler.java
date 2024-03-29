package cs.group11;

import cs.group11.models.*;
import cs.group11.models.artworks.*;

import java.io.*;
import java.util.*;

public final class FileHandler {

    private static final String COMMA_ESCAPE_CHAR = "\\u002C";
    private static final String SEMICOLON_ESCAPE_CHAR = "\\u003B";
    //private static final String COMMA_ESCAPE_CHAR = "%";
    //private static final String SEMICOLON_ESCAPE_CHAR = "&";

    public static String escape(String line) {
        String escaped = line
                .replace(",", COMMA_ESCAPE_CHAR)
                .replace(";", SEMICOLON_ESCAPE_CHAR);

        return escaped;
    }

    public static String unescape(String line) {
        String unescaped = line
                .replace(COMMA_ESCAPE_CHAR, ",")
                .replace(SEMICOLON_ESCAPE_CHAR, ";");

        return unescaped;

    }

	public static List<String> readLines(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<String> lines = new ArrayList<>();

		String line = "";
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		br.close();

		return lines;
	}

	public static HashMap<Integer, User> readUsers(File file) throws IOException {
		HashMap<Integer, User> users = new HashMap<>();
		List<String> lines = readLines(file);

		// Add users
		for (String line : lines) {
			User user = parseUser(line);
			users.put(user.getId(), user);
		}

		// Add favourite users
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			String line = lines.get(i);

			String[] csvLine = line.split(",");

			String[] favouriteUsersId = csvLine[9].equals("[]")
					? new String[] {}
					: csvLine[9].split(";");

			List<User> favouriteUsers = parseFavouriteUsers(favouriteUsersId, users);
			user.addAllFavouriteUsers(favouriteUsers);
		}

		return users;
	}

	public static HashMap<Integer, Auction> readAuction(File file, HashMap<Integer, User> users,
			HashMap<Integer, Artwork> artworks) throws IOException {
		HashMap<Integer, Auction> auctions = new HashMap<>();
		List<String> lines = readLines(file);

		// Add auctions
		for (String line : lines) {
			Auction auction = parseAuction(line, users, artworks);
			auctions.put(auction.getId(), auction);
		}

		return auctions;
	}

	public static HashMap<Integer, Bid> readBids(File file, HashMap<Integer, User> users,
			HashMap<Integer, Auction> auctions) throws IOException {
		HashMap<Integer, Bid> bids = new HashMap<>();
		List<String> lines = readLines(file);

		// Add bids
		for (String line : lines) {
			Bid bid = parseBid(line, users, auctions);
			bids.put(bid.getId(), bid);
		}

		return bids;
	}

	public static HashMap<Integer, Artwork> readArtworks(File file) throws IOException {
		HashMap<Integer, Artwork> artworks = new HashMap<>();
		List<String> lines = readLines(file);

		// Add Artworks
		for (String line : lines) {
			Artwork artwork = parseArtwork(line);
			artworks.put(artwork.getId(), artwork);
		}

		return artworks;
	}

	public static User parseUser(String line) {
		String[] csvLine = line.split(",");

		int userId = Integer.parseInt(csvLine[0]);
		Date lastLogin = new Date(Long.parseLong(csvLine[1]));

		String username = FileHandler.unescape(csvLine[2]);
		String firstname = FileHandler.unescape(csvLine[3]);
		String lastname = FileHandler.unescape(csvLine[4]);
		String telNo = FileHandler.unescape(csvLine[5]);

		Address address = new Address(FileHandler.unescape(csvLine[6]).split(";"), FileHandler.unescape(csvLine[7]));
		String avatarPath = FileHandler.unescape(csvLine[8]);

		return new User(userId, lastLogin, username, firstname, lastname, telNo, address, avatarPath);
	}

	public static List<User> parseFavouriteUsers(int[] favouriteUsersId, HashMap<Integer, User> users) {
		List<User> favouriteUsers = new ArrayList<>();

		for (int id : favouriteUsersId) {
			User favouriteUser = users.get(id);

			if (favouriteUser != null) {
				favouriteUsers.add(favouriteUser);
			} else {
				throw new RuntimeException("Could not find a user with ID:" + id);
			}
		}

		return favouriteUsers;
	}

	public static List<User> parseFavouriteUsers(String[] favouriteUsersId, HashMap<Integer, User> users) {
		int[] ids = Arrays.stream(favouriteUsersId).mapToInt(Integer::parseInt).toArray();
		return parseFavouriteUsers(ids, users);
	}

	public static List<Auction> parseFavouriteAuctions(int[] favouriteAuctionsId, HashMap<Integer, Auction> auctions) {
		List<Auction> favouriteAuctions = new ArrayList<>();

		for (int id : favouriteAuctionsId) {
			Auction favouriteAuction = auctions.get(id);

			if (favouriteAuction != null) {
				favouriteAuctions.add(favouriteAuction);
			} else {
				throw new RuntimeException("Could not find a auction with ID:" + id);
			}
		}

		return favouriteAuctions;
	}

	public static List<Auction> parseFavouriteAuctions(String[] favouriteAuctionsId,
			HashMap<Integer, Auction> auctions) {
		int[] ids = Arrays.stream(favouriteAuctionsId).mapToInt(Integer::parseInt).toArray();
		return parseFavouriteAuctions(ids, auctions);
	}

	public static Auction parseAuction(String line, HashMap<Integer, User> users, HashMap<Integer, Artwork> artworks) {
		String[] csvLine = line.split(",");

		int auctionId = Integer.parseInt(csvLine[0]);
		Date creationDate = new Date(Long.parseLong(csvLine[1]));

		int creatorId = Integer.parseInt(csvLine[2]);
		User creator = users.get(creatorId);

		int maxBids = Integer.parseInt(csvLine[3]);
		double reservePrice = Double.parseDouble(csvLine[4]);

		int artworkId = Integer.parseInt(csvLine[5]);
		Artwork artwork = artworks.get(artworkId);

		return new Auction(auctionId, creationDate, creator, maxBids, reservePrice, artwork);
	}

	public static List<Bid> parseAuctionBids(int[] bidsIds, HashMap<Integer, Bid> bids) {
		List<Bid> auctionBids = new ArrayList<>();

		for (int id : bidsIds) {
			Bid bid = bids.get(id);

			if (bid != null) {
				auctionBids.add(bid);
			} else {
				throw new RuntimeException("Could not find a bid with ID:" + id);
			}
		}

		return auctionBids;
	}

	public static List<Bid> parseAuctionBids(String[] bidsIds, HashMap<Integer, Bid> bids) {
		int[] ids = Arrays.stream(bidsIds).mapToInt(Integer::parseInt).toArray();
		return parseAuctionBids(ids, bids);
	}

	// Called after the readUsers() function to add the users auctions
	public static HashMap<Integer, User> loadFavouriteUserAuctions(File file, HashMap<Integer, User> users,
			HashMap<Integer, Auction> auctions) throws IOException {
		List<String> lines = readLines(file);

		// Add favourite users
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			String line = lines.get(i);
			String[] csvLine = line.split(",");

			String[] favouriteAuctionsId = csvLine[10].equals("[]")
					? new String[] {}
					: csvLine[10].split(";");

			List<Auction> favouriteAuctions = parseFavouriteAuctions(favouriteAuctionsId, auctions);
			user.addAllFavouriteAuctions(favouriteAuctions);
		}

		return users;
	}

	public static Bid parseBid(String line, HashMap<Integer, User> users, HashMap<Integer, Auction> auctions) {
		String[] csvLine = line.split(",");

		int bidId = Integer.parseInt(csvLine[0]);

		Date creationDate = new Date(Long.parseLong(csvLine[1]));

		int userId = Integer.parseInt(csvLine[2]);
		User user = users.get(userId);

		double price = Double.parseDouble(csvLine[3]);

		int auctionId = Integer.parseInt(csvLine[4]);
		Auction auction = auctions.get(auctionId);

		return new Bid(bidId, creationDate, price, user, auction);
	}

	public static Artwork parseArtwork(String line) {
		String[] csvLine = line.split(",");

		String type = csvLine[0];

		if (type.equals("painting")) {
			return parsePainting(line);
		} else if (type.equals("sculpture")) {
			return parseSculpture(line);
		} else {
			throw new RuntimeException("Invalid Artwork type:" + type);
		}
	}

	public static Painting parsePainting(String line) {
		String[] csvLine = line.split(",");

		int id = Integer.parseInt(csvLine[1]);
		String title = FileHandler.unescape(csvLine[2]);
		String description = csvLine[3].equals("null") ? null : FileHandler.unescape(csvLine[3]);
		String imagePath = FileHandler.unescape(csvLine[4]);
		String artist = FileHandler.unescape(csvLine[5]);
		int creationYear = Integer.parseInt(csvLine[6]);
		double width = Double.parseDouble(csvLine[7]);
		double height = Double.parseDouble(csvLine[8]);

		return new Painting(id, title, description, imagePath, artist, creationYear, width, height);
	}

	public static Sculpture parseSculpture(String line) {
		String[] csvLine = line.split(",");

		int id = Integer.parseInt(csvLine[1]);
		String title = FileHandler.unescape(csvLine[2]);
		String description = csvLine[3].equals("null") ? null : FileHandler.unescape(csvLine[3]);
		String imagePath = FileHandler.unescape(csvLine[4]);
		String artist = FileHandler.unescape(csvLine[5]);
		int creationYear = Integer.parseInt(csvLine[6]);
		double width = Double.parseDouble(csvLine[7]);
		double height = Double.parseDouble(csvLine[8]);
		double depth = Double.parseDouble(csvLine[9]);
		String material = FileHandler.unescape(csvLine[10]);

		List<String> photos = csvLine[11].equals("[]")
			? new ArrayList<>()
				: Arrays.asList(FileHandler.unescape(csvLine[11]).split(";"));

		return new Sculpture(id, title, description, imagePath, artist, creationYear, width, height, depth, material,
				photos);
	}

	public static void writeUsers(HashMap<Integer, User> users, File file) throws IOException {
		List<String> lines = new ArrayList<>();

		for (User user : users.values()) {
			lines.add(user.toCsv());
		}

		writeLines(lines, file);
	}

	public static void writeAuction(HashMap<Integer, Auction> auctions, File file) throws IOException {
		List<String> lines = new ArrayList<>();

		for (Auction auction : auctions.values()) {
			lines.add(auction.toCsv());
		}

		writeLines(lines, file);
	}

	public static void writeArtworks(HashMap<Integer, Artwork> artworks, File file) throws IOException {
		List<String> lines = new ArrayList<>();

		for (Artwork artwork : artworks.values()) {
			lines.add(artwork.toCsv());
		}

		writeLines(lines, file);
	}

	public static void writeBids(HashMap<Integer, Bid> bids, File file) throws IOException {
		List<String> lines = new ArrayList<>();

		for (Bid bid : bids.values()) {
			lines.add(bid.toCsv());
		}

		writeLines(lines, file);
	}

	public static void writeLines(List<String> lines, File file) throws IOException {
		if (!file.exists()) {
			File parentDir = file.getParentFile();
			if (!parentDir.exists())
				parentDir.mkdirs();
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));

		for (String line : lines) {
			bw.write(line);
			bw.newLine();
		}

		bw.close();
	}
}
