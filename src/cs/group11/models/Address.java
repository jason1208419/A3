package cs.group11.models;

import java.util.Arrays;
import java.util.regex.Pattern;

import cs.group11.FileHandler;
import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;

/**
 * This class represent an address, storing any necessary information of a user's address
 *
 * @Author
 */
public class Address implements Validatable {

    private static final int MINIMUM_ADDRESS_LINES = 1;
    private static final Pattern UK_ADDRESS_REGEX = Pattern.compile("([A-Z0-9]{2,4} ?){2}", Pattern.CASE_INSENSITIVE);

    private String[] lines;
    private String postcode;

    /**
     * To initialize an address
     * @param lines an array of lines of address
     * @param postcode the postcode
     */
    public Address(String[] lines, String postcode) {
        this.lines = lines;
        this.postcode = postcode.toUpperCase();

        this.validate();
    }

    /**
     * Get the postcode of this address
     * @return the postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Set the postcode of this address
     * @param postcode the postcode
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * Get the array of address lines
     * @return the array
     */
    public String[] getLines() {
        return lines;
    }

    /**
     * Replace with the array of address lines
     * @param lines the array to be added
     */
    public void setLines(String[] lines) {
        this.lines = lines;
    }

    /**
     * Get a line of the address
     * @param number the index wanted
     * @return the specific line of address
     */
    public String getLine(int number) {
        return this.lines[number - 1];
    }

    /**
     * Set the specific line of the address
     * @param number the index of array you wnt to replace the line
     * @param line a line of the address
     */
    public void setLine(int number, String line) {
        this.lines[number - 1] = line;
    }

    /**
     * Convert auction into a storable format
     * @return the converted format
     */
    public String toCsv() {
        StringBuilder builder = new StringBuilder();

        for (String addressLine : this.lines) {
            builder.append(FileHandler.escape(addressLine)).append(";");
        }
        builder.append(",");

        builder.append(FileHandler.escape(this.postcode)).append(",");

        return builder.toString();
    }

    @Override
    public void validate() throws InvalidDataException {
        if (Validator.isStringEmpty(postcode)) {
            throw new InvalidDataException("No postcode set!");
        }
        if (!UK_ADDRESS_REGEX.matcher(postcode.trim()).matches()) {
            throw new InvalidDataException("Invalid postcode format! The postcode must be a valid UK postcode.");
        }

        for (String s : lines) {
            if (Validator.isStringEmpty(s)) {
                throw new InvalidDataException("Null values are not accepted as address line");
            }
        }

        if (lines == null || lines.length < MINIMUM_ADDRESS_LINES) {
            throw new InvalidDataException("Too few address lines!");
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Address)) {
            return false;
        }
        Address other = (Address) obj;
        if (!Arrays.equals(lines, other.lines)) {
            return false;
        }
        if (!postcode.equals(other.postcode)) {
            return false;
        }
        return true;
    }

}
