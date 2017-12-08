import cs.group11.FileHandler;
import cs.group11.models.*;
import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import org.junit.Test;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

public class TestFileHandler {
    private HashMap<Integer, User> users;
    private HashMap<Integer, Auction> auctions;
    private HashMap<Integer, Artwork> artworks;
    private HashMap<Integer, Bid> bids;

    private int[][] favouriteUsersIds = new int[][]{ {4, 2, 3}, {0, 2}, {0}, {4}, {3, 1} };
    private int[][] favouriteAuctionsIds = new int[][] { {0}, {0}, {0}, {0}, {0} };
    private int[][] auctionBidsIds = new int[][] { {1, 0} };

    private HashMap<Integer, User> getUsersList() {
        if (this.users != null) {
            return this.users;
        }

        this.users = new HashMap<>();

        String[] usernames = new String[] {"Admin", "Bob", "Person123", "Killer3", "Airbender"};
        String[] firstnames = new String[] {"Carlos", "Cal", "Kieran", "Olly", "Zaid"};
        String[] lastnames = new String[] {"Mindon", "Cole", "Tier", "James", "Zinger"};
        String[] postcodes = new String[] {"5DE 238", "571 0DA", "K19 DEL", "DTS K9l", "SW3 PL0"};
        String[] avatars = new String[] {
                "https://images.freecreatives.com/wp-content/uploads/2017/01/Flying-Teddy-Bear-Drawing.jpg",
                "https://i.pinimg.com/736x/be/e9/9c/bee99c0a37957bf3c76823f38cb63baa--eagle-scout-beautiful-pictures.jpg",
                "http://compilation11.com/wp-content/uploads/2017/02/Importance-of-Phoenix.jpg?x75317",
                "https://i.pinimg.com/736x/7c/89/71/7c8971dcbff505c3a96386cf5a3e72b2--phoenix-rising-a-phoenix.jpg",
                "https://cdn.pixabay.com/photo/2017/03/29/12/52/bear-2185131_1280.png",
        };

        for (int i = 0; i < usernames.length; i++) {
            String username = usernames[i];
            String firstname = firstnames[i];
            String lastname = lastnames[i];
            String postcode = postcodes[i];

            String[] addressLines = new String[] {"18 Killer Avenue", "London"};
            Address address = new Address(addressLines, postcode);
            User user = new User(i, new Date(1512074890055L), username, firstname, lastname, "074929383531", address, avatars[i]);

            this.users.put(user.getId(), user);
        }

        for (int i = 0; i < this.users.size(); i++) {
            User user = this.users.get(i);
            List<User> favouriteUsers = FileHandler.parseFavouriteUsers(favouriteUsersIds[i], this.users);

            user.addAllFavouriteUsers(favouriteUsers);
        }

        return this.users;
    }
    private HashMap<Integer, Artwork> getArtworksList() {
        if (this.artworks != null) {
            return this.artworks;
        }

        this.artworks = new HashMap<>();

        String imagePath = "http://spotdeco.com/wp-content/uploads/2016/08/interior-design-styles-boho-flowers.png";
        Painting painting = new Painting(2, "Flowers", null, imagePath, "Van Gogh", 1993, 100, 200);

        String imagePath2 = "https://pbs.twimg.com/profile_images/603507749717037056/qgzh0UMy.jpg";
        Sculpture sculpture = new Sculpture(1, "dill", null, imagePath2, "Vin Diesel", 2007, 300, 10, 4, "plants", new ArrayList<>());

        this.artworks.put(2, painting);
        this.artworks.put(1, sculpture);

        return this.artworks;
    }
    private HashMap<Integer, Auction> getAuctionsList() {
        if (this.auctions != null) {
            return this.auctions;
        }

        this.auctions = new HashMap<>();

        HashMap<Integer, User> users = this.getUsersList();
        User user = users.get(2);

        HashMap<Integer, Artwork> artworks = this.getArtworksList();;
        Artwork artwork = artworks.get(2);

        Auction auction = new Auction(0, new Date(1512171171729L), user, 6, 10.00, artwork);
        this.auctions.put(0, auction);

        return this.auctions;
    }
    private HashMap<Integer, Bid> getBidsList() {
        if (this.bids != null) {
            return this.bids;
        }

        this.bids = new HashMap<>();

        HashMap<Integer, Auction> auctions = this.getAuctionsList();
        HashMap<Integer, User> users = this.getUsersList();

        Auction auction = auctions.get(0);

        Bid bid1 = new Bid(0, new Date(1512337288025L), 11.20, users.get(0), auction);
        Bid bid2 = new Bid(1, new Date(1512337327600L), 16.29, users.get(1), auction);

        bids.put(0, bid1);
        bids.put(1, bid2);

        return bids;
    }

    @Test
    public void testReadLines() {
        File file = new File(getClass().getResource("readLines.csv").getFile());
        List<String> lines = null;

        try {
            lines = FileHandler.readLines(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> expectedLines = new ArrayList<>();

        expectedLines.add("0,Alfreds Futterkiste,Maria Anders,Obere Str. 57,Berlin,12209,Germany");
        expectedLines.add("1,Ana Trujillo Emparedados y helados,Ana Trujillo,Avda. de la Constitución 2222,México D.F.,05021,Mexico");
        expectedLines.add("2,Antonio Moreno Taquería,Antonio Moreno,Mataderos 2312,México D.F.,05023,Mexico");
        expectedLines.add("3,Around the Horn,Thomas Hardy,120 Hanover Sq.,London,WA1 1DP,UK");
        expectedLines.add("4,Berglunds snabbköp,Christina Berglund,Berguvsvägen 8,Luleå,S-958 22,Sweden");

        assertThat(lines.size(), is(5));

        for(int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String expectedLine = expectedLines.get(i);
            assertThat(line, is(expectedLine));
        }
    }

    @Test
    public void testWriteArtworks() {
        HashMap<Integer, Artwork> artworks = getArtworksList();

        String resourceDir = getClass().getResource("/").getFile();
        File file = new File(resourceDir + "writeArtworks.csv");

        try {
            FileHandler.writeArtworks(artworks, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(file.exists(), is(true));

        List<String> actualLines = null;
        try {
            actualLines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(actualLines.size(), is(artworks.size()));
    }

    @Test
    public void testWriteAuctions() {
        HashMap<Integer, Auction> auctions = getAuctionsList();

        String resourceDir = getClass().getResource("/").getFile();
        File file = new File(resourceDir + "writeAuctions.csv");

        try {
            FileHandler.writeAuction(auctions, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(file.exists(), is(true));

        List<String> actualLines = null;
        try {
            actualLines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(actualLines.size(), is(auctions.size()));
    }

    @Test
    public void testWriteUsers() {
        HashMap<Integer, User> users = getUsersList();

        String resourceDir = getClass().getResource("/").getFile();
        File file = new File(resourceDir + "writeUsers.csv");

        try {
            FileHandler.writeUsers(users, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(file.exists(), is(true));

        List<String> actualLines = null;
        try {
            actualLines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(actualLines.size(), is(users.size()));
    }

    @Test
    public void testWriteBids() {
        HashMap<Integer, Bid> bids = getBidsList();

        String resourceDir = getClass().getResource("/").getFile();
        File file = new File(resourceDir + "writeBids.csv");

        try {
            FileHandler.writeBids(bids, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(file.exists(), is(true));

        List<String> actualLines = null;
        try {
            actualLines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(actualLines.size(), is(bids.size()));
    }

    @Test
    public void testWriteLines() {
        ArrayList<String> expectedLines = new ArrayList<>();
        expectedLines.add("line 1");
        expectedLines.add("line 2");
        expectedLines.add("line 3");
        expectedLines.add("line 10");
        expectedLines.add("line 4");

        String resourceDir = getClass().getResource("/").getFile();
        File file = new File(resourceDir + "writeFile.txt");

        try {
            FileHandler.writeLines(expectedLines, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(file.exists(), is(true));

        List<String> lines = null;
        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < expectedLines.size(); i++) {
            String expectedLine = expectedLines.get(i);
            String actualLine = lines.get(i);

            assertThat(actualLine, is(expectedLine));
        }
    }

    @Test
    public void testParseFavouriteUsers() {
        HashMap<Integer, User> users = getUsersList();

        for (int i = 0; i < users.size(); i++) {
            int[] expectedFavUsers = favouriteUsersIds[i];

            List<User> favUsers = FileHandler.parseFavouriteUsers(expectedFavUsers, users);
            assertThat(favUsers.size(), is(expectedFavUsers.length));

            for (int i2 = 0; i2 < expectedFavUsers.length; i2++) {
                User favUser = favUsers.get(i2);
                int expectedFavUserId = expectedFavUsers[i2];
                assertThat(favUser.getId(), is(expectedFavUserId));
            }
        }
    }

    @Test
    public void testReadUsers() {
        HashMap<Integer, User> users = getUsersList();

        File file = new File(getClass().getResource("users.csv").getFile());
        HashMap<Integer, User> loadedUsers = null;

        try {
            loadedUsers = FileHandler.readUsers(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (User loadedUser : loadedUsers.values()) {
            User expectedUser = users.get(loadedUser.getId());
            TestUser.assertUserMatch(loadedUser, expectedUser);

            List<User> loadedFavUsers = loadedUser.getFavouriteUsers();
            List<User> expectedFavUsers = expectedUser.getFavouriteUsers();

            int expectedFavUsersCount = favouriteUsersIds[loadedUser.getId()].length;
            assertThat(loadedFavUsers.size(), is(expectedFavUsersCount));

            for (int i = 0; i < loadedFavUsers.size(); i++) {
                User loadedFavUser = loadedFavUsers.get(i);
                User expectedFavUser = expectedFavUsers.get(i);

                TestUser.assertUserMatch(loadedFavUser, expectedFavUser);
            }
        }
    }

    @Test
    public void testLoadUsersAuctions() {
        HashMap<Integer, User> users = getUsersList();

        File userFile = new File(getClass().getResource("users.csv").getFile());
        HashMap<Integer, User> loadedUsers = null;
        HashMap<Integer, Auction> auctions = getAuctionsList();

        try {
            loadedUsers = FileHandler.readUsers(userFile);
            loadedUsers = FileHandler.loadFavouriteUserAuctions(userFile, users, auctions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (User loadedUser : loadedUsers.values()) {
            User expectedUser = users.get(loadedUser.getId());
            TestUser.assertUserMatch(loadedUser, expectedUser);

            List<Auction> loadedFavAuctions = loadedUser.getFavouriteAuctions();
            List<Auction> expectedFavAuctions = expectedUser.getFavouriteAuctions();

            int expectedFavAuctionsCount = favouriteAuctionsIds[loadedUser.getId()].length;
            assertThat(loadedFavAuctions.size(), is(expectedFavAuctionsCount));

            for (int i = 0; i < loadedFavAuctions.size(); i++) {
                Auction loadedFavAuction = loadedFavAuctions.get(i);
                Auction expectedFavAuction = expectedFavAuctions.get(i);

                assertThat(loadedFavAuction.getId(), is(expectedFavAuction.getId()));
                assertThat(loadedFavAuction.getCreationDate().getTime(), is(expectedFavAuction.getCreationDate().getTime()));
                assertThat(loadedFavAuction.getReservePrice(), is(expectedFavAuction.getReservePrice()));
            }
        }
    }

    @Test
    public void testParseUser() {
        String csvLine = "1,1512074890055,Admin,Nasir,Al Jabbouri,07461174758,28 Kilvey Street;Singleton Park;Swansea;,SA1 4PO,https://i.pinimg.com/736x/ef/41/fa/ef41fa8febc1a1ea7035a33c9f0c9a2e--mango-hummer.jpg,";

        User user = FileHandler.parseUser(csvLine);
        assertThat(user.getId(), is(1));
        assertThat(user.getLastLogin(), is(new Date(1512074890055L)));
        assertThat(user.getUsername(), is("Admin"));
        assertThat(user.getFirstname(), is("Nasir"));
        assertThat(user.getLastname(), is("Al Jabbouri"));
        assertThat(user.getTelNo(), is("07461174758"));
        assertThat(user.getAvatarPath(), is("https://i.pinimg.com/736x/ef/41/fa/ef41fa8febc1a1ea7035a33c9f0c9a2e--mango-hummer.jpg"));

        assertThat(user.getAddress().getLines(), is(new String[] {"28 Kilvey Street", "Singleton Park", "Swansea"}));
        assertThat(user.getAddress().getPostcode(), is("SA1 4PO"));
    }

    @Test
    public void testParseAuction() {
        String csvLine = "0,1512171171729,2,6,10.0,2,1;0;,";

        Auction auction = FileHandler.parseAuction(csvLine, this.getUsersList(), this.getArtworksList());
        assertThat(auction, notNullValue());
        assertThat(auction.getCreationDate().getTime(), is(1512171171729L));
        assertThat(auction.getMaxBids(), is(6));
        assertThat(auction.getReservePrice(), is(10.00));

        Painting expectedArtwork = (Painting) this.getArtworksList().get(2);
        Painting actualArtwork = (Painting) auction.getArtwork();
        assertThat(actualArtwork.getId(), is(2));
        assertThat(actualArtwork.getName(), is(expectedArtwork.getName()));
        assertThat(actualArtwork.getWidth(), is(expectedArtwork.getWidth()));
        assertThat(actualArtwork.getHeight(), is(expectedArtwork.getHeight()));
        assertThat(actualArtwork.getArtist(), is(expectedArtwork.getArtist()));
        assertThat(actualArtwork.getCreationYear(), is(expectedArtwork.getCreationYear()));

        User expectedUser = this.getUsersList().get(2);
        User actualUser = auction.getCreator();
        assertThat(actualArtwork.getId(), is(2));
        assertThat(actualUser.getUsername(), is(expectedUser.getUsername()));
        assertThat(actualUser.getFirstname(), is(expectedUser.getFirstname()));
    }

    @Test
    public void testParseFavouriteAuctions() {
        HashMap<Integer, Auction> auctions = getAuctionsList();

        for (int i = 0; i < auctions.size(); i++) {
            int[] expectedFavAuctions = favouriteAuctionsIds[i];

            List<Auction> favAuctions = FileHandler.parseFavouriteAuctions(expectedFavAuctions, auctions);
            assertThat(favAuctions.size(), is(expectedFavAuctions.length));

            for (int i2 = 0; i2 < expectedFavAuctions.length; i2++) {
                Auction favAuction = favAuctions.get(i2);
                int expectedFavAuctionId = expectedFavAuctions[i2];
                assertThat(favAuction.getId(), is(expectedFavAuctionId));
            }
        }
    }

    @Test
    public void testParseAuctionBids() {
        HashMap<Integer, Bid> bids = getBidsList();
        HashMap<Integer, Auction> auctions = getAuctionsList();

        for (int i = 0; i < auctions.size(); i++) {
            int[] expectedBidsIds = auctionBidsIds[i];

            List<Bid> auctionBids = FileHandler.parseAuctionBids(expectedBidsIds, bids);
            assertThat(auctionBids.size(), is(expectedBidsIds.length));

            for (int i2 = 0; i2 < expectedBidsIds.length; i2++) {
                Bid bid = auctionBids.get(i2);
                int expectedBidId = expectedBidsIds[i2];
                assertThat(bid.getId(), is(expectedBidId));
            }
        }
    }

    @Test
    public void testParseBid() {
        HashMap<Integer, User> users = getUsersList();
        HashMap<Integer, Auction> auctions = getAuctionsList();

        String csvLine = "0,1512337288025,0,11.20,0";

        Bid bid = FileHandler.parseBid(csvLine, users, auctions);

        assertThat(bid.getId(), is(0));
        assertThat(bid.getCreationDate().getTime(), is(1512337288025L));
        assertThat(bid.getUser().getId(), is(0));
        assertThat(bid.getUser().getUsername(), is("Admin"));
        assertThat(bid.getPrice(), is(11.20));
        assertThat(bid.getAuction().getId(), is(0));
        assertThat(bid.getAuction().getReservePrice(), is(10.00));
    }

    @Test
    public void testParseArtwork() {
        String csvLine1 = "painting,0,flowers,,http://spotdeco.com/wp-content/uploads/2016/08/interior-design-styles-boho-flowers.png,Van Gogh,1993,100,200";
        String csvLine2 = "sculpture,1,dill,,https://pbs.twimg.com/profile_images/603507749717037056/qgzh0UMy.jpg,Vin Diesel,2007,300,10,4,plants,;";

        Artwork artwork1 = FileHandler.parseArtwork(csvLine1);
        assertThat(artwork1, is(instanceOf(Painting.class)));

        Artwork artwork2 = FileHandler.parseArtwork(csvLine2);
        assertThat(artwork2, is(instanceOf(Sculpture.class)));
    }

    @Test
    public void testParsePainting() {
        String csvLine = "painting,0,flowers,null,http://spotdeco.com/wp-content/uploads/2016/08/interior-design-styles-boho-flowers.png,Van Gogh,1993,100.0,200.0,";

        Painting painting = (Painting) FileHandler.parsePainting(csvLine);

        assertThat(painting.getId(), is(0));
        assertThat(painting.getName(), is("flowers"));
        assertThat(painting.getDescription(), is(nullValue()));
        assertThat(painting.getImagePath(), is("http://spotdeco.com/wp-content/uploads/2016/08/interior-design-styles-boho-flowers.png"));
        assertThat(painting.getArtist(), is("Van Gogh"));
        assertThat(painting.getCreationYear(), is(1993));
        assertThat(painting.getWidth(), is(100.0));
        assertThat(painting.getHeight(), is(200.0));
    }

    @Test
    public void testParseSculpture() {
        String csvLine = "sculpture,1,dill,null,https://pbs.twimg.com/profile_images/603507749717037056/qgzh0UMy.jpg,Vin Diesel,2007,300,10,4,plants,[],";

        Sculpture sculpture = (Sculpture) FileHandler.parseSculpture(csvLine);

        assertThat(sculpture.getId(), is(1));
        assertThat(sculpture.getName(), is("dill"));
        assertThat(sculpture.getDescription(), is(nullValue()));
        assertThat(sculpture.getImagePath(), is("https://pbs.twimg.com/profile_images/603507749717037056/qgzh0UMy.jpg"));
        assertThat(sculpture.getArtist(), is("Vin Diesel"));
        assertThat(sculpture.getCreationYear(), is(2007));
        assertThat(sculpture.getWidth(), is(300.0));
        assertThat(sculpture.getHeight(), is(10.0));
        assertThat(sculpture.getDepth(), is(4.0));
        assertThat(sculpture.getMaterial(), is("plants"));
        assertThat(sculpture.getPhotos().size(), is(0));
    }

    @Test
    public void testReadAuction() {
        HashMap<Integer, User> users = getUsersList();
        HashMap<Integer, Artwork> artworks = getArtworksList();
        HashMap<Integer, Bid> bids = getBidsList();
        HashMap<Integer, Auction> expectedAuctions = getAuctionsList();

        File file = new File(getClass().getResource("auctions.csv").getFile());
        HashMap<Integer, Auction> loadedAuctions = null;

        try {
            loadedAuctions = FileHandler.readAuction(file, users, artworks);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Auction loadedAuction: loadedAuctions.values()) {
            Auction expectedAuction = expectedAuctions.get(loadedAuction.getId());
            assertThat(loadedAuction.getId(), is(expectedAuction.getId()));
            assertThat(loadedAuction.getCreationDate().getTime(), is(expectedAuction.getCreationDate().getTime()));
            assertThat(loadedAuction.getCreator().getId(), is(expectedAuction.getCreator().getId()));
            assertThat(loadedAuction.getCreator().getUsername(), is(expectedAuction.getCreator().getUsername()));
            assertThat(loadedAuction.getReservePrice(), is(expectedAuction.getReservePrice()));
            assertThat(loadedAuction.getArtwork().getArtist(), is(expectedAuction.getArtwork().getArtist()));
        }
    }

    @Test
    public void testReadBids() {
        HashMap<Integer, User> users = getUsersList();
        HashMap<Integer, Auction> auctions = getAuctionsList();
        HashMap<Integer, Bid> expectedBids = getBidsList();

        File file = new File(getClass().getResource("bids.csv").getFile());
        HashMap<Integer, Bid> loadedBids = null;

        try {
            loadedBids = FileHandler.readBids(file, users, auctions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Bid loadedBid : loadedBids.values()) {
            Bid expectedBid = expectedBids.get(loadedBid.getId());
            assertThat(loadedBid.getId(), is(expectedBid.getId()));
            assertThat(loadedBid.getPrice(), is(expectedBid.getPrice()));
            assertThat(loadedBid.getAuction().getId(), is(expectedBid.getAuction().getId()));

            long actualCreationDate = loadedBid.getAuction().getCreationDate().getTime();
            long expectedCreationDate = expectedBid.getAuction().getCreationDate().getTime();
            assertThat(actualCreationDate, is(expectedCreationDate));

            assertThat(loadedBid.getCreationDate().getTime(), is(expectedBid.getCreationDate().getTime()));
            assertThat(loadedBid.getUser().getId(), is(expectedBid.getUser().getId()));
            assertThat(loadedBid.getUser().getUsername(), is(expectedBid.getUser().getUsername()));
        }
    }

    @Test
    public void testReadArtworks() {
        HashMap<Integer, Artwork> expectedArtworks = getArtworksList();

        File file = new File(getClass().getResource("artworks.csv").getFile());
        HashMap<Integer, Artwork> loadedArtworks = null;

        try {
            loadedArtworks = FileHandler.readArtworks(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Artwork loadedArtwork : loadedArtworks.values()) {
            Artwork expectedArtwork = expectedArtworks.get(loadedArtwork.getId());
            assertThat(loadedArtwork, is(instanceOf(expectedArtwork.getClass())));
            assertThat(loadedArtwork.getArtist(), is(expectedArtwork.getArtist()));
            assertThat(loadedArtwork.getId(), is(expectedArtwork.getId()));
            assertThat(loadedArtwork.getImagePath(), is(expectedArtwork.getImagePath()));

            if (loadedArtwork instanceof Sculpture) {
                Sculpture loadedSculpture = (Sculpture) loadedArtwork;
                Sculpture expectedSculpture = (Sculpture) expectedArtwork;

                assertThat(loadedSculpture.getMaterial(), is(expectedSculpture.getMaterial()));
                assertThat(loadedSculpture.getDepth(), is(expectedSculpture.getDepth()));
                assertThat(loadedSculpture.getPhotos().size(), is(expectedSculpture.getPhotos().size()));
            } else if (loadedArtwork instanceof Painting) {
                Painting loadedPainting = (Painting) loadedArtwork;
                Painting expectedPainting = (Painting) expectedArtwork;

                assertThat(loadedPainting.getHeight(), is(expectedPainting.getHeight()));
                assertThat(loadedPainting.getWidth(), is(expectedPainting.getWidth()));
            }
        }
    }

    @Test
    public void testCommaEscape() {


        String expectedString = "\\u002Cunicodetext.";
        String result = FileHandler.escape(",unicodetext.");
        assertThat(result, is(expectedString));
    }

    @Test
    public void testCommaUnescape() {
        String expectedString = ",unicodetext.";
        String result = FileHandler.unescape("\\u002Cunicodetext.");
        assertThat(result, is(expectedString));
    }

    @Test
    public void testSemiColonEscape() {
        String expectedString = "\\u003Bunicodetext.";
        String result = FileHandler.escape(";unicodetext.");
        assertThat(result, is(expectedString));
    }

    @Test
    public void testSemiColonUnescape() {
        String expectedString = ";unicodetext.";
        String result = FileHandler.unescape("\\u003Bunicodetext.");
        assertThat(result, is(expectedString));
    }

    @Test
    public void testEscapeDescription() {
        String originalString = "The Starry Night is an oil on canvas by the Dutch post-impressionist painter " +
                "Vincent van Gogh. Painted in June 1889, it depicts the view from the east-facing window of his " +
                "asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition of an idealized village";

        String expectedString = "The Starry Night is an oil on canvas by the Dutch post-impressionist painter " +
                "Vincent van Gogh. Painted in June 1889\\u002C it depicts the view from the east-facing window of his " +
                "asylum room at Saint-Rémy-de-Provence\\u002C just before sunrise\\u002C with the addition of an " +
                "idealized village";

        String result = FileHandler.escape(originalString);
        assertThat(result, is(expectedString));
    }

    @Test
    public void testParser() {


        String originalString = "painting,0,Starry Night,The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June 1889\\\\u002C it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence\\\\u002C just before sunrise\\\\u002C with the addition of an idealized village,https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg,Vincent Van Gogh,1889,200.0,300.0,";

        Artwork p = FileHandler.parseArtwork(originalString);
        String newString = p.toCsv();
        assertThat(newString, is(originalString));


    }


}
