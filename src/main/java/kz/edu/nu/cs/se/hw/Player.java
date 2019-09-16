package kz.edu.nu.cs.se.hw;

public class Player {
    private boolean finished;
    private Hand hand;
    private String name;

    public Player(String name, int index) {
        hand = new Hand();
        this.name = name;
        this.finished = false;
    }

    public String getName() {
        return this.name;
    }


    public Hand getHand() {
        return hand;
    }

    public void addCard(String card, boolean is) {
        hand.push(card, is);
    }

    public void remove(String... cards) {
        hand.remove(cards);
    }

    public void remove(String card) {
        hand.remove(card);
    }

    public void setFinished(boolean b) {
        this.finished = b;
    }

    public boolean isFinished() {
        return finished;
    }
}
