import cs.group11.models.artworks.Painting;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestPainting {

    @Test
    public void toCsv() {
        Painting painting = new Painting(0, "flowers", null, "http://spotdeco.com/wp-content/uploads/2016/08/interior-design-styles-boho-flowers.png", "Van Gogh", 1993, 100, 200);
        String expectedResult = "painting,0,flowers,null,http://spotdeco.com/wp-content/uploads/2016/08/interior-design-styles-boho-flowers.png,Van Gogh,1993,100.0,200.0,";
        assertThat(painting.toCsv(), is(expectedResult));
    }

}
