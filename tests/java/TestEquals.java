import cs.group11.models.Address;
import cs.group11.models.Auction;
import cs.group11.models.User;
import cs.group11.models.artworks.Painting;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;


import java.util.Date;




public class TestEquals {
    @Test
    public void testEqualsAuction() {
        Address creatorAddress = new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL");
        User creator = new User(0, new Date(), "admin", "Nasir", "Al Jabbouri", "07481173742", creatorAddress, "res/avatars/creeper.jpg");
        String description = "The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June 1889, " +
                "it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition " +
                "of an idealized village";

        String artworkImagePath = "https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg";
        Painting painting = new Painting("Starry Night", description, artworkImagePath, "Vincent Van Gogh", 1889, 200, 300);
        Auction a = new Auction(creator, 5, 5.2, painting);

        Auction b = new Auction(creator, 5, 5.2, painting);

        assertThat(a.equals(b),is(true));
    }

    @Test
    public void testEqualsAuction2(){

            Address creatorAddress = new Address(new String[]{"29 Flintstones Avenue", "Ding Dong Street", "UK"}, "PDT 0KL");
            User creator = new User(0, new Date(), "admin", "Nasir", "Al Jabbouri", "07481173742", creatorAddress, "res/avatars/creeper.jpg");
            String description = "The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June 1889, " +
                    "it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition " +
                    "of an idealized village";

            String artworkImagePath = "https://www.moma.org/wp/moma_learning/wp-content/uploads/2012/07/Van-Gogh.-Starry-Night-469x376.jpg";
            Painting painting = new Painting("Starry Night", description, artworkImagePath, "Vincent Van Gogh", 1889, 200, 300);
            Auction a = new Auction(creator, 5, 5.2, painting);

            Auction b = new Auction(creator, 6, 5.2, painting);

            assertThat(a.equals(b),is(false));
    }

}
