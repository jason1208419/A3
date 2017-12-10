package cs.group11.interfaces;

import cs.group11.models.User;

/**
 * @author Nasir
 * Handles actions performed when a representation of a user is clicked in the GUI .
 */
public interface OnUserClick {
    void clicked(User user);
}
