import cs.group11.FileHandler;
import cs.group11.models.Address;
import cs.group11.models.User;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;

public class TestFileHandler {
    private HashMap<Integer, User> users;

    private int[][] favouriteUsersIds = new int[][]{ {4, 2, 3}, {0, 2}, {0}, {4}, {3, 1} };

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
            List<User> favouriteUsers = FileHandler.parseFavouriteUsers(this.users, favouriteUsersIds[i]);

            user.addAllFavouriteUsers(favouriteUsers);
        }

        return this.users;
    }

    private void assertUserMatch(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getLastLogin().toString(), is(user2.getLastLogin().toString()));
        assertThat(user1.getUsername(), is(user2.getUsername()));
        assertThat(user1.getFirstname(), is(user2.getFirstname()));
        assertThat(user1.getLastname(), is(user2.getLastname()));
        assertThat(user1.getTelNo(), is(user2.getTelNo()));
        assertThat(user1.getAvatarPath(), is(user2.getAvatarPath()));

        Address address1 = user1.getAddress();
        Address address2 = user2.getAddress();

        assertThat(address1.getLines(), is(address2.getLines()));
        assertThat(address1.getPostcode(), is(address2.getPostcode()));
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
    public void testWriteUsers() {
        HashMap<Integer, User> users = getUsersList();

    }

    @Test
    public void testParseFavouriteUsers() {
        HashMap<Integer, User> users = getUsersList();

        for (int i = 0; i < users.size(); i++) {
            int[] expectedFavUsers = favouriteUsersIds[i];

            List<User> favUsers = FileHandler.parseFavouriteUsers(users, expectedFavUsers);
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
            assertUserMatch(loadedUser, expectedUser);

            List<User> loadedFavUsers = loadedUser.getFavouriteUsers();
            List<User> expectedFavUsers = expectedUser.getFavouriteUsers();

            int expectedFavUsersCount = favouriteUsersIds[loadedUser.getId()].length;
            assertThat(loadedFavUsers.size(), is(expectedFavUsersCount));

            for (int i = 0; i < loadedFavUsers.size(); i++) {
                User loadedFavUser = loadedFavUsers.get(i);
                User expectedFavUser = expectedFavUsers.get(i);

                assertUserMatch(loadedFavUser, expectedFavUser);
            }
        }
    }

    @Test
    public void testParseUser() {
        String csvLine = "1,1512074890055,Admin,Nasir,Al Jabbouri,07461174758,28 Kilvey Street;Singleton Park;Swansea,SA1 4PO,https://i.pinimg.com/736x/ef/41/fa/ef41fa8febc1a1ea7035a33c9f0c9a2e--mango-hummer.jpg";

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

}
