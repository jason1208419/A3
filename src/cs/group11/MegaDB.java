package cs.group11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
		return Collections.unmodifiableCollection(auctions);
	}

	public static Collection<User> getUsers() {
		return Collections.unmodifiableCollection(users);
	}

	public static Collection<Artwork> getArtworks() {
		return Collections.unmodifiableCollection(artworks);
	}

	public static boolean addAuction(Auction toAdd) {
		toAdd.validate();// Only store valid data in the databse
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

	public static Collection<Artwork> searchArtwork(String input) {
		Set<Artwork> results = new HashSet<>();
		for (Artwork a : artworks) {
			if (a.getArtist().contains(input) 
					|| a.getDescription().contains(input) 
					|| a.getName().contains(input)
					|| String.valueOf(a.getCreationYear()).contains(input)) {
				results.add(a);//Add to results if one of the attributes in Artwork contains 
							   //the input string.
			}
		}
		return results;
	}

	/*
	 * TODO: Implement search function(s) to searc based on a specific value through
	 * the database (Users/Artworks/Auctions)
	 */


    public static Collection<User> searchByUser(String input) {
        Set<User> results = new HashSet<>();
        for (User user : users) {
            if (user.getUsername().contains(input)
                    || user.getFirstname().contains(input)
                    || user.getLastname().contains(input)) {
                results.add(user);
            }
        }
        return results;
    }

    public static Collection<Auction> searchByAuction(String input) {
        Set<Auction> results = new HashSet<>();
        for (Auction auc : auctions) {
            if (auc.getCreator().getUsername().contains(input)
                    || auc.getArtwork().getName().contains(input)) {
                results.add(auc);
            }
        }
        return results;
    }

	/*
    DELETE IF NOT NEEDED

	public static Collection<Auction> searchFinishedAuction(String input) {
		Set<Auction> results = new HashSet<>();
		for (Auction auc : auctions) {
			if (auc.isCompleted()) {
				results.add(auc);
			}
		}
		return results;
	}
	*/

}
