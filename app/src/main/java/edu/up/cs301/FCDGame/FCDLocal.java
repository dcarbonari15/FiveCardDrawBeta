package edu.up.cs301.FCDGame;

import android.util.Log;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.RoundOverInfo;

/**
 * Created by carbonar19 on 3/16/2016.
 */
public class FCDLocal extends LocalGame implements FCDGame {

    public FCDState state;

    private boolean foldCase;


    public FCDLocal(){
        state = new FCDState();
        state.shuffle();
        state.shuffle();
        this.dealCards();
    }



    @Override
    protected String checkIfGameOver() {
        if(!(state.getGameStage() == 3)){
            return null;
        }else{
            checkIfRoundOver();
            for(Player p:state.getLobby()){
                if(p.getMoney() == 500 * state.getLobby().size()){
                    return "player " + state.getLobby().indexOf(p) + " wins!";
                }
            }
        }
        return null;

    }

    protected String checkIfRoundOver(){
        if(foldCase){
            int winnerIndex = 0;
            for(Player p:state.getLobby()){
                if(!(p.isFold())){
                    winnerIndex = state.getLobby().indexOf(p);
                }
            }
            int winningPlayerMoney = state.getLobby().get(winnerIndex).getMoney();
            state.getLobby().get(winnerIndex).setMoney(winningPlayerMoney + state.getPot());
            state.setPot(0);
            state.advanceGameStage();
            state.remakeDeck();
            state.shuffle();
            state.shuffle();
            dealCards();
        }
        if(!(state.getGameStage() == 3)){
            return null;
        }else{
            int nextPlayerHand;
            int bestHand = 0;
            int bestHandIndex = 0;
            for(Player p: state.getLobby()){
                nextPlayerHand = state.handValue(p.getHand());
                if(nextPlayerHand >= bestHand){
                    bestHand = nextPlayerHand;
                    bestHandIndex = state.getLobby().indexOf(p);
                }
            }
            int winningPlayerMoney = state.getLobby().get(bestHandIndex).getMoney();
            state.getLobby().get(bestHandIndex).setMoney(winningPlayerMoney + state.getPot());
            state.setPot(0);
            state.advanceGameStage();
            state.remakeDeck();
            state.shuffle();
            state.shuffle();
            dealCards();
            return "player "  + bestHandIndex + " wins $" + state.getPot() + "!!!";
        }
    }


    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        if(state == null){
            return;
        }

        FCDState stateForPlayer = new FCDState(state);

        p.sendInfo(stateForPlayer);

        Log.i("sendUpdatedState", "Sent the new state");

    }

    @Override
    protected boolean canMove(int playerIdx) {
        if(playerIdx < 0 || state.getLobby().size() < playerIdx){
            return false;
        }else{
            return state.getActivePlayer() == playerIdx;
        }
    }



    @Override
    protected boolean makeMove(GameAction action) {
        Log.i("action", action.getClass().toString());
        if(!(action instanceof FCDMoveAction)){
            return false;
        }
        FCDMoveAction move = (FCDMoveAction) action;

        int playerIndex = getPlayerIdx(move.getPlayer());
        if(playerIndex < 0){
            return false;
        }

        if(move.isFold()){
            Log.i("Fold", "isFold");
            state.getLobby().get(playerIndex).fold();
            int foldCount = 0;
            for(Player p: state.getLobby()){
                if(p.isFold()){
                    foldCount++;
                }
            }
            if(playerIndex == 0) {
                state.setActivePlayer(1);
            }else{
                state.setActivePlayer(0);
            }
            state.setCount();
            if(state.getMoveCount() % state.getLobby().size() == 0) {
                state.advanceGameStage();
            }
            if(foldCount == state.getLobby().size() -1){
                state.setGameStage(3);
                foldCase = true;
                checkIfRoundOver();
            }
            for(Player p: state.getLobby()){
                sendUpdatedStateTo(p);
            }
            return true;
        }else if(move.isBet()){
            if(state.getGameStage() == 1 ||state.getGameStage() == 3 ) {
                Log.i("Bet", "isBet");
                int playersMoney = state.getLobby().get(playerIndex).getMoney();
                int pot = state.getPot();
                if ((playersMoney) - ((FCDBetAction) move).getbet() < 0) {
                    return false;
                } else {
                    state.getLobby().get(playerIndex).setMoney(playersMoney - ((FCDBetAction) move).getbet());
                    state.setPot(pot + ((FCDBetAction) move).getbet());
                    if (playerIndex == 0) {
                        state.setActivePlayer(1);
                    } else {
                        state.setActivePlayer(0);
                    }
                    state.setCount();
                    if(state.getMoveCount() % state.getLobby().size() == 0) {
                        state.advanceGameStage();
                    }
                    for (Player p : state.getLobby()) {
                        sendUpdatedStateTo(p);
                    }
                    return true;
                }
            }

        }else if(move.isCall()){
            if(state.getGameStage() == 1 ||state.getGameStage() == 3 ) {
                Log.i("Call", "isCall");
                int tableBet = state.getPot();
                int playerMoney = state.getPlayerMoney(playerIndex);
                int pot = state.getPot();
                if (playerMoney <= 0) {
                    return false;
                } else if (playerMoney - tableBet < 0) {
                    state.setPot(state.getPot() + playerMoney);
                    state.setPlayerMoney(0, playerIndex);
                    if (playerIndex == 0) {
                        state.setActivePlayer(1);
                    } else {
                        state.setActivePlayer(0);
                    }
                    state.setCount();
                    if(state.getMoveCount() % state.getLobby().size() == 0) {
                        state.advanceGameStage();
                    }
                    for (Player p : state.getLobby()) {
                        sendUpdatedStateTo(p);
                    }
                    //send state to the player
                    //state.getLobby().get(playerIndex).sendInfo(new FCDState(state));
                    return true;
                } else {
                    Log.i("Call", "player calling with enough money");
                    state.setPot(pot + tableBet);
                    state.setPlayerMoney(state.getPlayerMoney(playerIndex) - tableBet, playerIndex);
                    if (playerIndex == 0) {
                        state.setActivePlayer(1);
                    } else {
                        state.setActivePlayer(0);
                    }
                    state.setCount();
                    if(state.getMoveCount() % state.getLobby().size() == 0) {
                        state.advanceGameStage();
                    }
                    for (Player p : state.getLobby()) {
                        sendUpdatedStateTo(p);
                    }
                    //send state to the player
                    //state.getLobby().get(playerIndex).sendInfo(new FCDState(state));
                    return true;
                }
            }
        }else if(move.isThrow()){
            Log.i("Throw", "isThrow");
            if(state.getGameStage() != 2){
                return false;
            }
            int[] cardsToDiscard = ((FCDThrowAction)move).getIndexOfThrow();
            Card[] cards = new Card[cardsToDiscard.length];
            for(int i = 0; i < cardsToDiscard.length; i++){
                cards[i] = state.getLobby().get(playerIndex).getCard(cardsToDiscard[i]);
            }
            Log.i("PlayerIdx:", Integer.toString(playerIndex));
            Log.i("ThrowingCards:",Integer.toString(cards.length));
            this.playerDiscards(action, playerIndex, cards);
            if(playerIndex == 0) {
                state.setActivePlayer(1);
            }else{
                state.setActivePlayer(0);
            }
            state.setCount();
            if(state.getMoveCount() % state.getLobby().size() == 0) {
                state.advanceGameStage();
            }
            for(Player p: state.getLobby()){
                sendUpdatedStateTo(p);
            }
            return true;
        }else if(move.isCheck()){
            Log.i("Check", "isCheck");
            if(state.getLastBet() > 0){
                return false;
            }else{
                if(playerIndex == 0) {
                    state.setActivePlayer(1);
                }else{
                    state.setActivePlayer(0);
                }
                state.setCount();
                if(state.getMoveCount() % state.getLobby().size() == 0) {
                    state.advanceGameStage();
                }
                for(Player p: state.getLobby()){
                    sendUpdatedStateTo(p);
                }
                return true;
            }
        }else if(move.isRaise()) {
            if(state.getGameStage() == 1 ||state.getGameStage() == 3 ) {
                Log.i("Raise", "isRaise");
                int tableBet = state.getLastBet();
                int playerMoney = state.getPlayerMoney(playerIndex);
                int pot = state.getPot();
                if ((playerMoney + ((FCDRaiseAction) move).getAmountRaised()) - tableBet < 0) {
                    return false;
                } else {
                    state.setPot(pot + tableBet + ((FCDRaiseAction) move).getAmountRaised());
                    state.setPlayerMoney(playerMoney - tableBet - ((FCDRaiseAction) move).getAmountRaised()
                            , playerIndex);
                    if (playerIndex == 0) {
                        state.setActivePlayer(1);
                    } else {
                        state.setActivePlayer(0);
                    }
                    state.setCount();
                    if(state.getMoveCount() % state.getLobby().size() == 0) {
                        state.advanceGameStage();
                    }
                    for (Player p : state.getLobby()) {
                        sendUpdatedStateTo(p);
                    }
                    return true;
                }
            }
        }

        return false;

    }
    /**
     * handles a player discarding 1 or multiple cards (throw)
     *
     * @param sourcePlayer
     *      the player doing the discarding
     * @param card
     *      the array of cards being discarded
     */
    public void playerDiscards(GameAction action, int sourcePlayer, Card[] card){
        if (card.length > 0 && state.getDeck().size() >= card.length) { //if player is able to discard

            FCDMoveAction move = (FCDMoveAction) action;
            Log.i("Discard", "a player is attempting to discard");

            //for each card in discard array
            for (int i = 0; i < card.length; i++) {
                //for each card in player's hand
                for (int j = 0; j < state.getLobby().get(sourcePlayer).getHand().length; j++) {
                    //check if the two cards match
                    if (card[i] == state.getLobby().get(sourcePlayer).getCard(j)) {
                        Log.i("changingCard", "Changing a player's card");
                        Log.i("discardCard:", card[i].toString());
                        Log.i("cardInHand:", state.getLobby().get(sourcePlayer).getCard(j).toString());

                        //set the discarded card to the top card on the deck
                        state.getLobby().get(sourcePlayer).setCard(j, state.getDeck().get(0));
                        Log.i("SourcePlayer:", Integer.toString(sourcePlayer));
                        //remove top card
                        state.getDeck().remove(0);
                    }
                }
            }
            Log.i("SendingState", "sending new state after throwing");
            //send state to the player
            state.getLobby().get(sourcePlayer).sendInfo(new FCDState(state));
            Log.i("SentState", "sent new state after throwing");
        }
    }

    /**
     * deals the cards to all players
     */
    public void dealCards() {
        //for each card in a hand
        for (int i = 0; i < 5; i++){
            //for each player
            for (int j = 0; j < state.getLobby().size(); j++) {
                state.getLobby().get(j).setCard(i, state.getDeck().get(0));
                state.getDealtCards().add(state.getDeck().get(0));
                state.getDeck().remove(0);
                sendUpdatedStateTo(state.getLobby().get(j));
            }
        }
    }
}
