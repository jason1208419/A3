import cs.group11.models.Address;
import cs.group11.models.Auction;
import cs.group11.models.Bid;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestAuction {

    @Test
    public void testToCsv() {
        Address address = new Address(new String[] {"28 Kilvey Street", "Singleton Park", "Swansea"}, "SA1 4PO");
        User user = new User(2, new Date(1512074890055L), "Admin", "Nasir", "Al Jabbouri", "07461174758", address, "https://i.pinimg.com/736x/ef/41/fa/ef41fa8febc1a1ea7035a33c9f0c9a2e--mango-hummer.jpg");

        String imagePath = "http://spotdeco.com/wp-content/uploads/2016/08/interior-design-styles-boho-flowers.png";
        Painting painting = new Painting(2, "Flowers", null, imagePath, "Van Gogh", 1993, 100, 200);
        Auction auction = new Auction(0, new Date(1512171171729L), user, 6, 10.00, painting);

        Bid bid2 = new Bid(1, new Date(1512337327600L), 16.29, user, auction);
        Bid bid1 = new Bid(0, new Date(1512337288025L), 11.20, user, auction);

        String expectedResult = "";
        assertThat(auction.toCsv(), is("0,1512171171729,2,6,10.0,2,1;0;,"));
    }

}
