package cs.group11;

import cs.group11.models.*;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;

import java.io.*;
import java.util.*;

public final class FileHandler {

    public static HashMap<Integer, User> readUsers(File file) throws IOException {
        HashMap<Integer, User> users = new HashMap<>();
        List<String> lines = readLines(file);

        for (String line : lines) {
            User user = parseUser(line);
            users.put(user.getId(), user);
        }

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String line = lines.get(i);

            String[] csvLine = line.split(",");
            String[] favouriteUsersId = csvLine[9].split(";");
            List<User> favouriteUsers = parseFavouriteUsers(users, favouriteUsersId);

            user.addAllFavouriteUsers(favouriteUsers);
        }

        // TODO: Add favourite auctions

        return users;
    }

    public static List<User> parseFavouriteUsers(HashMap<Integer, User> users, int[] favouriteUsersId) {
        List<User> favouriteUsers = new ArrayList<>();

        for(int id : favouriteUsersId ) {
            User favouriteUser = users.get(id);

            if (favouriteUser != null) {
                favouriteUsers.add(favouriteUser);
            } else {
                throw new RuntimeException("Could not find a user with ID:" + id);
            }
        }

        return favouriteUsers;
    }

    public static List<User> parseFavouriteUsers(HashMap<Integer, User> users, String[] favouriteUsersId) {
        int[] ids = Arrays.stream(favouriteUsersId).mapToInt(Integer::parseInt).toArray();

        return parseFavouriteUsers(users, ids);
    }

    public static User parseUser(String line) {
        String[] csvLine = line.split(",");

        int userId = Integer.parseInt(csvLine[0]);
        Date lastLogin = new Date(Long.parseLong(csvLine[1]));

        String username = csvLine[2];
        String firstname = csvLine[3];
        String lastname = csvLine[4];
        String telNo = csvLine[5];

        Address address = new Address(csvLine[6].split(";"), csvLine[7]);
        String avatarPath = csvLine[8];

        return new User(userId, lastLogin, username, firstname, lastname, telNo, address, avatarPath);
    }


//    public static List<Auction> parseFavouriteAuctions(HashMap<Integer, Auction>, int[] favouriteAuctionsId) {
//
//    }

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

//
//    public static List<Artwork> readArtworks(File file) {
//
//    }
//
//    public static HashMap<Integer, Auction> readAuction(File file) {
//
//    }

//    public static void writeUsers(HashMap<Integer, User> users) throws IOException {
//
//    }
//
//    public static void writeAuction (Auction auction) {
//
//    }
//
//    public static void writeFile (File file) {
//
//    }
//
//    public static void writeArtwork (Artwork artwork) {
//
//    }

//    private static Bid parseBid (String line) {
//
//    }
//
//    private static Painting parsePainting (String line) {
//
//    }
//
//    private static Sculpture parseSculpture (String line) {
//
//    }
}
