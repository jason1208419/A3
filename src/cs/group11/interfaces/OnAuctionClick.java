package cs.group11.interfaces;

import cs.group11.models.Auction;

/**
 * @author Nasir
 * Handles actions performed when a representation of an auction is clicked in the GUI .
 */
public interface OnAuctionClick {
    void clicked(Auction auction);
}
