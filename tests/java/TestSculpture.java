import cs.group11.models.artworks.Painting;
import cs.group11.models.artworks.Sculpture;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestSculpture {

    @Test
    public void toCsv() {
        String imgPath = "https://pbs.twimg.com/profile_images/603507749717037056/qgzh0UMy.jpg";
        Sculpture sculpture = new Sculpture(1, "dill", null, imgPath, "Vin Diesel", 2007, 300, 10, 4, "plants", new ArrayList<>());
        String expectedResult = "sculpture,1,dill,null,https://pbs.twimg.com/profile_images/603507749717037056/qgzh0UMy.jpg,Vin Diesel,2007,300.0,10.0,4.0,plants,,";
        assertThat(sculpture.toCsv(), is(expectedResult));
    }

}
