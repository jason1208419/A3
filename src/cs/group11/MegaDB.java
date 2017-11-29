package cs.group11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cs.group11.models.Artwork;
import cs.group11.models.Auction;
import cs.group11.models.User;

public final class MegaDB {

	private static List<Auction> auctions = new LinkedList<>();
	private static List<Artwork> artworks = new ArrayList<>();
	private static Set<User> users = new HashSet<>();

	private MegaDB() {
	}// Prevent instantiation of class.

	public static void load() {
		// TODO implement once FileReader is in place.
	}

	public static void save() {
		// TODO implement once FileReader is in place.
	}

	public static Collection<Auction> getAuctions() {
		return auctions;
	}

	public static Collection<User> getUsers() {
		return users;
	}

	public static Collection<Artwork> getArtworks() {
		return artworks;
	}

	public static boolean addAuction(Auction toAdd) {
		toAdd.validate();
		return auctions.add(toAdd);
	}

	public static boolean addUser(User toAdd) {
		toAdd.validate();
		return users.add(toAdd);
	}

	public static boolean addArtwork(Artwork toAdd) {
		toAdd.validate();
		return artworks.add(toAdd);
	}

	/*
	 * TODO:
	 * 	Implement search function(s) to searc based on a specific value through the 
	 * database (Users/Artworks/Auctions)
	 */
	
}
