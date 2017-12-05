import cs.group11.models.Address;
import cs.group11.models.User;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestUser {

    @Test
    public void testToCsv() {
        Address address = new Address(new String[] {"28 Kilvey Street", "Singleton Park", "Swansea"}, "SA1 4PO");
        User user = new User(1, new Date(1512074890055L), "Admin", "Nasir", "Al Jabbouri", "07461174758", address, "https://i.pinimg.com/736x/ef/41/fa/ef41fa8febc1a1ea7035a33c9f0c9a2e--mango-hummer.jpg");
        String expectedResult = "1,1512074890055,Admin,Nasir,Al Jabbouri,07461174758,28 Kilvey Street;Singleton Park;Swansea;,SA1 4PO,https://i.pinimg.com/736x/ef/41/fa/ef41fa8febc1a1ea7035a33c9f0c9a2e--mango-hummer.jpg,";
        assertThat(user.toCsv(), is(expectedResult));
    }

}
