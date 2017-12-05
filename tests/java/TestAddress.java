import cs.group11.models.Address;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestAddress {

    @Test
    public void testToCsv() {
        Address address = new Address(new String[] {"28 Kilvey Street", "Singleton Park", "Swansea"}, "SA1 4PO");
        String expectedResult = "28 Kilvey Street;Singleton Park;Swansea;,SA1 4PO,";
        assertThat(address.toCsv(), is(expectedResult));
    }

}
