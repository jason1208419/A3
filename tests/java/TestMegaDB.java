import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import cs.group11.MegaDB;
import cs.group11.models.Address;
import cs.group11.models.User;

public class TestMegaDB {

    private HashMap<Integer, User> localUsers;

    public HashMap<Integer, User> getUsers() {
        if (this.localUsers != null) {
            return localUsers;
        }

        localUsers = new HashMap<>();

        User u = new User(0, new Date(), "me", "Some", "One", "123456789", new Address(new String[] {"address"}, "SA28PP"), "/path");
        User u2 = new User(1, new Date(), "you", "Other", "Two", "987654321", new Address(new String[] {"address"}, "SA28PP"), "/path");

        localUsers.put(u.getId(), u);
        localUsers.put(u2.getId(), u2);

        return localUsers;
    }

    private void addTestUsers() {
        for (User user : getUsers().values()) {
            MegaDB.addUser(user);
        }
    }

    private void deleteFiles() {
        if (MegaDB.USER_FILE.exists()) {
            MegaDB.USER_FILE.delete();
        }

        if (MegaDB.AUCTION_FILE.exists()) {
            MegaDB.AUCTION_FILE.delete();
        }

        if (MegaDB.ARTWORK_FILE.exists()) {
            MegaDB.ARTWORK_FILE.delete();
        }

        if (MegaDB.BID_FILE.exists()) {
            MegaDB.BID_FILE.delete();
        }
    }

    @Before
    public void clearDatabase() {
        localUsers = null;
        MegaDB.clear();
    }

    @Test
    public void testLoad() {
        deleteFiles();

        try {
            MegaDB.load();
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertThat(MegaDB.USER_FILE.exists(), is(true));
        assertThat(MegaDB.AUCTION_FILE.exists(), is(true));
        assertThat(MegaDB.ARTWORK_FILE.exists(), is(true));
        assertThat(MegaDB.BID_FILE.exists(), is(true));
    }

    @Test
    public void testAddUser() {
        Collection<User> users = MegaDB.getUsers();
        addTestUsers();

        User u = getUsers().get(0);
        User u2 = getUsers().get(1);

        assertThat(users.size(), is(2));
        assertThat(users, hasItems(u));
        assertThat(users, hasItems(u2));
    }

    @Test
    public void testFavouriteUserLoad() {
        addTestUsers();

        try {
            MegaDB.save();
            MegaDB.clear();
            MegaDB.load();
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertThat(MegaDB.getUsers().size(), is(2));
        for (User user : MegaDB.getUsers()) {
            User expectedUser = getUsers().get(user.getId());
            TestUser.assertUserMatch(user, expectedUser);
        }
    }

    @Test
    public void testSave() {
        try {
            MegaDB.save();
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

}

