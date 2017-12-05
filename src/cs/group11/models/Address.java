package cs.group11.models;

import java.util.regex.Pattern;

import cs.group11.helpers.InvalidDataException;
import cs.group11.helpers.Validator;
import cs.group11.interfaces.Validatable;

public class Address implements Validatable {

	private static final int MINIMUM_ADDRESS_LINES = 1;
	private static final Pattern UK_ADDRESS_REGEX = Pattern.compile("([A-Z0-9]{2,4} ?){2}", Pattern.CASE_INSENSITIVE);
	
	private String[] lines;
	private String postcode;

	public Address(String[] lines, String postcode) {
		this.lines = lines;
		this.postcode = postcode.toUpperCase();

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

	public void setLines(String[] lines) {
		this.lines = lines;
	}

	public String getLine(int number) {
		return this.lines[number - 1];
	}

	public void setLine(int number, String line) {
		this.lines[number - 1] = line;
	}

	public String toCsv() {
		StringBuilder builder = new StringBuilder();

		for (String addressLine : this.lines) {
			builder.append(addressLine).append(";");
		}
		builder.append(",");

		builder.append(this.postcode).append(",");

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
		if (lines == null || lines.length < MINIMUM_ADDRESS_LINES) {
			throw new InvalidDataException("Too few address lines!");
		}

	}
}
