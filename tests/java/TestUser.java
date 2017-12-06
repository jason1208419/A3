import cs.group11.models.Address;
import cs.group11.models.User;
import org.hamcrest.core.Is;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestUser {

    public static void assertUserMatch(User user1, User user2) {
        assertThat(user1.getId(), Is.is(user2.getId()));
        assertThat(user1.getLastLogin().toString(), Is.is(user2.getLastLogin().toString()));
        assertThat(user1.getUsername(), Is.is(user2.getUsername()));
        assertThat(user1.getFirstname(), Is.is(user2.getFirstname()));
        assertThat(user1.getLastname(), Is.is(user2.getLastname()));
        assertThat(user1.getTelNo(), Is.is(user2.getTelNo()));
        assertThat(user1.getAvatarPath(), Is.is(user2.getAvatarPath()));

        Address address1 = user1.getAddress();
        Address address2 = user2.getAddress();

        assertThat(address1.getLines(), Is.is(address2.getLines()));
        assertThat(address1.getPostcode(), Is.is(address2.getPostcode()));
    }

    @Test
    public void testToCsv() {
        Address address = new Address(new String[] {"28 Kilvey Street", "Singleton Park", "Swansea"}, "SA1 4PO");
        User user = new User(1, new Date(1512074890055L), "Admin", "Nasir", "Al Jabbouri", "07461174758", address, "https://i.pinimg.com/736x/ef/41/fa/ef41fa8febc1a1ea7035a33c9f0c9a2e--mango-hummer.jpg");
        String expectedResult = "1,1512074890055,Admin,Nasir,Al Jabbouri,07461174758,28 Kilvey Street;Singleton Park;Swansea;,SA1 4PO,https://i.pinimg.com/736x/ef/41/fa/ef41fa8febc1a1ea7035a33c9f0c9a2e--mango-hummer.jpg,[],[],";
        assertThat(user.toCsv(), is(expectedResult));
    }

}
