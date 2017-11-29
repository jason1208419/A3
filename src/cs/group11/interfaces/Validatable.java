package cs.group11.interfaces;

import cs.group11.helpers.InvalidDataException;

public interface Validatable {
    public void validate() throws InvalidDataException;
}
