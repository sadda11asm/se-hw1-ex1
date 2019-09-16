package kz.edu.nu.cs.se.hw;

import java.util.*;

public class Meld {

    private ArrayList<String> cards;
    private int property; // 1 if rank equals, 2 if suit equals

    public Meld(String... cards) {
        this.cards = new ArrayList<>();
        if (cards.length < 3) {
            throw new RummyException("Not enough cards", 1);
        }
        char suit = cards[0].charAt(cards[0].length() - 1);
        String rank = cards[0].substring(0, cards[0].length() - 1);
        boolean okR = true;
        boolean okS = true;
        for (int i = 1; i < cards.length; i++) {
            char s = cards[i].charAt(cards[i].length() - 1);
            String r = cards[i].substring(0, cards[i].length() - 1);
            if (okR && !r.equals(rank)) {
                okR = false;
            }
            if (okS && s != suit) {
                okS = false;
            }
        }
        if (!okS && !okR) {
            throw new RummyException("Expected card", 7);
        }
        List<String> ranks =  new ArrayList<>(Arrays.asList("A","2","3","4","5","6","7","8","9","10","J","Q","K"));
        if (okS) {
            int last = -1;
            for (int i = 0; i <cards.length; i++) {
                if (i==0) last = ranks.indexOf(cards[i].substring(0, cards[i].length() - 1));
                if (i!=0 && ranks.indexOf(cards[i].substring(0, cards[i].length() - 1)) != last+1) {
                    throw new RummyException("Not valid meld", 1);
                } else {
                    last = ranks.indexOf(cards[i].substring(0, cards[i].length() - 1));
                }
            }
            property = 2;
        } else {
            property = 1;
        }
        Collections.addAll(this.cards, cards);
    }

    public String[] get() {
        return convertToArray(cards);
    }

    public void add(String... cards) {
        char suit = this.cards.get(0).charAt(this.cards.get(0).length() - 1);
        String rank = this.cards.get(0).substring(0, this.cards.get(0).length() - 1);
        for (int i = 0; i < cards.length; i++) {
            char s = cards[i].charAt(cards[i].length() - 1);
            String r = cards[i].substring(0, cards[i].length() - 1);
            if (property==1) {
                if (!r.equals(rank))
                    throw new RummyException("Not valid meld", 1);
            } else {
                if (s!=suit) {
                    throw new RummyException("Not valid meld", 1);
                }
            }
        }
        System.out.println(cards.length);
        this.cards.addAll(Arrays.asList(cards));
    }


    private String[] convertToArray(ArrayList<String> list) {
        String[] ans = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ans[i] = list.get(i);
        }
        return ans;
    }
}
