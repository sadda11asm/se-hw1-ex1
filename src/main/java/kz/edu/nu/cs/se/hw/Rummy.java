package kz.edu.nu.cs.se.hw;



import java.util.*;

/**
 * Starter code for a class that implements the <code>PlayableRummy</code>
 * interface. A constructor signature has been added, and method stubs have been
 * generated automatically in eclipse.
 *
 * Before coding you should verify that you are able to run the accompanying
 * JUnit test suite <code>TestRummyCode</code>. Most of the unit tests will fail
 * initially.
 *
 * @see PlayableRummy
 * @see TestRummyCode
 *
 */
public class Rummy implements PlayableRummy {

    private ArrayList<Player> players;
    private Deck deck;
    private ArrayList<String> discarded;
    private ArrayList<Meld> melds;
    private int currentPlayer;
    private Steps step;

    private String[] convertToArray(ArrayList<String> list) {
        String[] ans = new String[list.size()];
        for (int i = 0; i< list.size(); i++) {
            ans[i] = list.get(i);
        }
        return ans;
    }

    public Rummy(String... players) {
        if (players.length<2) {
            throw new RummyException("NOT_ENOUGH_PLAYERS", 2);
        }
        if (players.length>6) {
            throw new RummyException("Expected fewer players", 8);
        }
        this.players = new ArrayList<>();
        this.deck = new Deck();
        this.discarded = new ArrayList<>();
        melds = new ArrayList<>();
        this.currentPlayer = -1;
        for (int i = 0; i< players.length; i++) {
            this.players.add(new Player(players[i], i));
        }
        step = Steps.WAITING;
    }
    @Override
    public String[] getPlayers() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i< players.size(); i++) {
            names.add(players.get(i).getName());
        }
        return convertToArray(names);
    }

    @Override
    public int getNumPlayers() {
        return players.size();
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public int getNumCardsInDeck() {
        return deck.size();
    }

    @Override
    public int getNumCardsInDiscardPile() {
        return discarded.size();
    }

    @Override
    public String getTopCardOfDiscardPile() {
        if (discarded.size() == 0) {
            throw new RummyException("Not valid discard, pile is empty", 13);
        }
        return discarded.get(discarded.size()-1);
    }

    @Override
    public String[] getHandOfPlayer(int player) {
        if (player<0 || player>=players.size()) {
            throw new RummyException("Not valid index of player", 10);
        }
        return convertToArray(players.get(player).getHand().get());
    }

    @Override
    public int getNumMelds() {
        return melds.size();
    }

    @Override
    public String[] getMeld(int i) {
        if (i<0 || i>=melds.size()) {
            throw new RummyException("Not valid index of meld", 11);
        }
        ArrayList<String> temp = new ArrayList<>();
        return melds.get(i).get();
    }

    @Override
    public void rearrange(String card) {
        if (step == Steps.WAITING) {
            deck.remove(card);
            deck.push(card);
        }
        else {
            throw new RummyException("Expected step waiting", 3);
        }
    }

    @Override
    public void shuffle(Long l) {
        if (step != Steps.WAITING) {
            throw new RummyException("Expected step waiting", 3);
        }
        deck.shuffle(l);
    }

    @Override
    public Steps getCurrentStep() {
        return step;
    }

    @Override
    public int isFinished() {
        if (step!=Steps.FINISHED)
            return -1;
        for (int i = 0; i<players.size();i++) {
            if (players.get(i).getHand().get().size()==0 || (players.get(i).getHand().get().size()==1 && players.get(i).isFinished())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void initialDeal() {
        if (step!=Steps.WAITING) {
            throw new RummyException("Expected step waiting", 3);
        } else {
            //System.out.println(deck.size());
            int num;
            if (players.size()<=1) {
                throw new RummyException("Expected at least 2 players", 2);
            } else if (players.size() == 2) {
                num = 10;
            } else if (players.size()<=4) {
                num = 7;
            } else if (players.size() <= 6){
                num = 6;
            } else {
                throw new RummyException("Expected fewer number of players", 8);
            }
            for (int i = 0; i<num;i++) {
                for (int j = 0; j<players.size();j++) {
                    players.get(j).addCard(deck.pop(), false);
                }
            }
            discarded.add(deck.pop());
            step = Steps.DRAW;
            currentPlayer = 0;
            //System.out.println(deck.size());
        }
    }

    @Override
    public void drawFromDiscard() {
        if (step != Steps.DRAW) {
            throw new RummyException("Expected DRAW state", 4);
        }
        players.get(currentPlayer).addCard(discarded.remove(discarded.size()-1), true);
        step = Steps.MELD;
    }

    @Override
    public void drawFromDeck() {
        if (step != Steps.DRAW) {
            throw new RummyException("Expected DRAW state", 4);
        }
        if (deck.size()==0) {
            while (!discarded.isEmpty()) {
                deck.push(discarded.remove(discarded.size()-1));
            }
            discarded.add(deck.pop());
        }
        players.get(currentPlayer).addCard(deck.pop(), false);
        step = Steps.MELD;
    }

    @Override
    public void meld(String... cards) {
        if (step!=Steps.MELD && step!=Steps.RUMMY) {
            throw new RummyException("Expected meld or rummy step", 15);
        }
        if (players.get(currentPlayer).getHand().size()==0) {
            throw new RummyException("Expected cards", 7);
        }
        melds.add(new Meld(cards));
        players.get(currentPlayer).remove(cards);

        if (players.get(currentPlayer).getHand().size() == 0) {
            step = Steps.FINISHED;
            players.get(currentPlayer).setFinished(true);
        }
        if (step == Steps.RUMMY && players.get(currentPlayer).getHand().size() == 1) {
            step = Steps.FINISHED;
            players.get(currentPlayer).setFinished(true);
        }
    }

    @Override
    public void addToMeld(int meldIndex, String... cards) {
        if (step!=Steps.MELD && step!=Steps.RUMMY) {
            throw new RummyException("Expected meld or rummy step", 15);
        }
        if (step==Steps.MELD) {
            if (meldIndex < 0 || meldIndex >= melds.size()) {
                throw new RummyException("Not valid meld index", 11);
            }
            melds.get(meldIndex).add(cards);
            players.get(currentPlayer).remove(cards);
            if (players.get(currentPlayer).getHand().size() == 0) {
                step = Steps.FINISHED;
                players.get(currentPlayer).setFinished(true);
            }
        } else {
            if (meldIndex < 0 || meldIndex >= melds.size()) {
                throw new RummyException("Not valid meld index", 11);
            }
            melds.get(meldIndex).add(cards);
            players.get(currentPlayer).remove(cards);
            if (players.get(currentPlayer).getHand().size() == 0 || players.get(currentPlayer).getHand().size() == 1) {
                step = Steps.FINISHED;
                players.get(currentPlayer).setFinished(true);
            }
        }
    }

    @Override
    public void declareRummy() {
        if (step!= Steps.MELD) {
            throw new RummyException("", 5);
        }

        step = Steps.RUMMY;

    }

    @Override
    public void finishMeld() {
        if (step!=Steps.MELD && step!=Steps.RUMMY) {
            throw new RummyException("", RummyException.EXPECTED_MELD_STEP_OR_RUMMY_STEP);
        }
        if (step == Steps.RUMMY && players.get(currentPlayer).getHand().size()>1) {
            step = Steps.DISCARD;
            throw new RummyException("", RummyException.RUMMY_NOT_DEMONSTRATED);
        }
        step = Steps.DISCARD;

    }

    @Override
    public void discard(String card) {
        if (step != Steps.DISCARD)
            throw new RummyException("not discard state", 6);
        players.get(currentPlayer).remove(card);
        discarded.add(card);
        if (players.get(currentPlayer).getHand().size()==0) {
            step = Steps.FINISHED;
        } else {
            currentPlayer = (currentPlayer+1)%players.size();
            step = Steps.DRAW;
        }


    }

    @Override
    public ArrayList<String> getDiscard() {
        return discarded;
    }

    @Override
    public ArrayList<String> getDeck() {
        return deck.get();
    }


}
