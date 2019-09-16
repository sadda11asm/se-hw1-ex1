package kz.edu.nu.cs.se.hw;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class Hand {

    private ArrayList<String> hand;
    private String last;


    public Hand() {
        hand = new ArrayList<>();
        last = null;
    }

    public void push(String card, boolean is) {
        hand.add(card);
        if (is)
            last = card;
    }

    public int size() {
        return hand.size();
    }

    public ArrayList<String> get() {
        ArrayList<String> ans = new ArrayList<>();
        ans.addAll(hand);
        return ans;
    }

    public void remove(String... cards) {
        for (int i = 0; i<cards.length; i++) {
            if (!hand.contains(cards[i])) {
                throw new RummyException("INVALID_MELD", 1);
            }
        }
        for (int i = 0; i<cards.length; i++) {
            hand.remove(cards[i]);
        }
    }

    public void remove(String card) {
        if (card.equals(last)) {
            throw new RummyException("NOT_VALID_DISCARD", 13);
        }
        if (hand.contains(card)) {
            hand.remove(card);
        } else {
            throw new RummyException("EXPECTED_CARD", 7);
        }
    }

}
