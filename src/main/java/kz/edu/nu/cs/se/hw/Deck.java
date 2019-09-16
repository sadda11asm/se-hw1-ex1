package kz.edu.nu.cs.se.hw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {

    private ArrayList<String> deck;

    public Deck() {
        deck = new ArrayList<>();
        final String[] suits = new String[] { "C", "D", "H", "S", "M" };
        final String[] ranks = new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(rank + suit);
            }
        }
    }

    public int size() {
        return deck.size();
    }
    public void push(String card) {
        deck.add(card);
    }

    public void shuffle(Long l) {
        Collections.shuffle(deck, new Random(l));
    }

    public String pop() {
        return deck.remove(deck.size()-1);
    }


    public void remove(String card) {
        deck.remove(card);
    }

    public ArrayList<String> get() {
        return deck;
    }
}
