package cs.group11.models;

import cs.group11.helpers.InvalidDataException;
import cs.group11.interfaces.Validatable;

public class Address implements Validatable {

    private String[] lines;
    private String postcode;

    public Address(String[] lines, String postcode) {
        this.lines = lines;
        this.postcode = postcode;

        this.validate();
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String[] getLines() {
        return lines;
    }

    public String getLine(int number) {
        return this.lines[number - 1];
    }

    public void setLine(int number, String line) {
        this.lines[number - 1] = line;
    }

    @Override
    public void validate() throws InvalidDataException {

    }
}
