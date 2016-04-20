package edu.up.cs301.FCDGame;

import edu.up.cs301.card.Card;
import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;

/**
 * The epic computerized game  player. This the class that will contain the "epic" AI
 * that will be hard to beat without cheating or good luck.
 *
 * @author David Carbonari
 * @author Ryan Dehart
 * @author Gabe Hall
 * @version March 2016
 */


public  class SmartAI extends GameComputerPlayer{
    private double pause;
    private GameAction lastTriedMove;

    private FCDState savedState;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public SmartAI(String name) {


        this(name, 2.0);
    }

    public SmartAI(String name, Double pause){
        super(name);

        this.pause = pause * 1000;
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        if (!(info instanceof FCDState)) {
            if (info instanceof IllegalMoveInfo) {
                if (lastTriedMove instanceof FCDCallAction) {
                    game.sendAction(new FCDCheckAction(this));
                } else if (lastTriedMove instanceof FCDCheckAction) {
                    game.sendAction(new FCDCallAction(this));
                }
            } else {
                return;
            }
        }

        savedState = (FCDState) info;


        Card[] hand = savedState.getPlayer1Hand(savedState.getActivePlayer());
        int handVal = savedState.getLobby().get(savedState.getActivePlayer()).getHandValue();

        //If Ultron has a royal flush:
        if (handVal == 9 && savedState.getPlayerMoney(1) > savedState.getPlayerMoney(0) && savedState.getGameStage() == 1) {


            game.sendAction((new FCDCallAction(this)));
            lastTriedMove = new FCDCallAction(this);
        }

        if (handVal == 9 && savedState.getPlayerMoney(1) > savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getPlayerMoney(0))));
            lastTriedMove = new FCDRaiseAction(this, savedState.getPlayerMoney(0));
        }


        if (handVal == 9 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 1) {


            game.sendAction((new FCDCallAction(this)));
            lastTriedMove = new FCDCallAction(this);
        }

        if (handVal == 9 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getPlayerMoney(1))));
            lastTriedMove = new FCDRaiseAction(this, savedState.getPlayerMoney(1));
        }

        //If Ultron has a straight flush:

        if (handVal == 8 && savedState.getPlayerMoney(1) > savedState.getPlayerMoney(0) && savedState.getGameStage() == 1) {


            game.sendAction((new FCDCallAction(this)));
            lastTriedMove = new FCDCallAction(this);
        }

        if (handVal == 8 && savedState.getPlayerMoney(1) > savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getPlayerMoney(0))));
            lastTriedMove = new FCDRaiseAction(this, savedState.getPlayerMoney(0));
        }


        if (handVal == 8 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 1) {


            game.sendAction((new FCDCallAction(this)));
            lastTriedMove = new FCDCallAction(this);
        }

        if (handVal == 8 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getPlayerMoney(1))));
            lastTriedMove = new FCDRaiseAction(this, savedState.getPlayerMoney(1));
        }

        //If Ultron has quads

        if (handVal == 7 && savedState.getPlayerMoney(1) > savedState.getPlayerMoney(0) && savedState.getGameStage() == 1) {


            game.sendAction((new FCDCallAction(this)));
            lastTriedMove = new FCDCallAction(this);
        }

        if (handVal == 7 && savedState.getPlayerMoney(1) > savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getPlayerMoney(0))));
            lastTriedMove = new FCDRaiseAction(this, savedState.getPlayerMoney(0));
        }


        if (handVal == 7 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 1) {

            game.sendAction((new FCDCallAction(this)));
            lastTriedMove = new FCDCallAction(this);
        }

        if (handVal == 7 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getPlayerMoney(1))));
            lastTriedMove = new FCDRaiseAction(this, savedState.getPlayerMoney(1));
        }

        //If Ultron has a crowded condo

        if (handVal == 6 && savedState.getPlayerMoney(1) > savedState.getPlayerMoney(0) && savedState.getGameStage() == 1) {
            game.sendAction((new FCDRaiseAction(this, savedState.getLastBet() + 30)));
            lastTriedMove = new FCDRaiseAction(this, savedState.getLastBet() + 30);

        }

        if (handVal == 6 && savedState.getPlayerMoney(1) > savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getLastBet() + 30)));
            lastTriedMove = new FCDRaiseAction(this, savedState.getLastBet() + 30);
        }


        if (handVal == 6 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 1) {
            if (savedState.getLastBet() == 0) {
                game.sendAction((new FCDRaiseAction(this, 50)));
                lastTriedMove = new FCDRaiseAction(this, 50);
            } else {

                game.sendAction((new FCDCallAction(this)));
                lastTriedMove = new FCDCallAction(this);
            }
        }

        if (handVal == 6 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getPlayerMoney(1))));
            lastTriedMove = new FCDRaiseAction(this, savedState.getPlayerMoney(1));
        }

        //if Ultron has a flush
        if (handVal == 5 && savedState.getPlayerMoney(1) > savedState.getPlayerMoney(0) && savedState.getGameStage() == 1) {
            if (savedState.getLastBet() < 51) {
                game.sendAction((new FCDRaiseAction(this, savedState.getLastBet() + savedState.getPlayerMoney(1) / 3)));
                lastTriedMove = new FCDRaiseAction(this, +savedState.getPlayerMoney(1) / 3);
            } else {
                game.sendAction((new FCDCallAction(this)));

            }
        }

        if (handVal == 5 && savedState.getPlayerMoney(1) > savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getLastBet() + 30)));
            lastTriedMove = new FCDRaiseAction(this, savedState.getLastBet() + 30);
        }


        if (handVal == 5 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 1) {
            if (savedState.getLastBet() == 0) {
                game.sendAction((new FCDRaiseAction(this, 50)));
                lastTriedMove = new FCDRaiseAction(this, 50);
            } else {

                game.sendAction((new FCDCallAction(this)));
                lastTriedMove = new FCDCallAction(this);
            }
        }

        if (handVal == 5 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getPlayerMoney(1))));
            lastTriedMove = new FCDRaiseAction(this, savedState.getPlayerMoney(1));
        }

        //if Ultron has a straight
        if (handVal == 4 && savedState.getPlayerMoney(1) > savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getLastBet() + 30)));
            lastTriedMove = new FCDRaiseAction(this, savedState.getLastBet() + 30);
        }


        if (handVal == 4 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 1) {
            if (savedState.getLastBet() == 0) {
                game.sendAction((new FCDRaiseAction(this, 50)));
                lastTriedMove = new FCDRaiseAction(this, 50);
            } else {

                game.sendAction((new FCDCallAction(this)));
                lastTriedMove = new FCDCallAction(this);
            }
        }

        if (handVal == 4 && savedState.getPlayerMoney(1) < savedState.getPlayerMoney(0) && savedState.getGameStage() == 3) {
            game.sendAction((new FCDRaiseAction(this, savedState.getPlayerMoney(1))));
            lastTriedMove = new FCDRaiseAction(this, savedState.getPlayerMoney(1));
        }
        //if Ultron has trips
        if (handVal == 3 && savedState.getGameStage() == 1) {
            game.sendAction((new FCDCallAction(this)));
            lastTriedMove = new FCDCallAction(this);
        }
        if (handVal == 3 && savedState.getGameStage() == 3) {
            if (savedState.getLastBet() < 31) {
                game.sendAction((new FCDCallAction(this)));
                lastTriedMove = new FCDCallAction(this);
            } else {
                game.sendAction((new FCDFoldAction(this)));
                lastTriedMove = new FCDFoldAction(this);

            }
        }

        // if Ultron has two pair
        if (handVal == 2 && savedState.getGameStage() == 1) {
            game.sendAction((new FCDThrowAction(this, new int[]{0, 1, 2, 3, 4})));
            lastTriedMove = new FCDThrowAction(this, new int[]{0, 1, 2, 3, 4});
        }
        if (handVal == 2 && savedState.getGameStage() == 3) {
            if (savedState.getLastBet() < -1) {
                game.sendAction((new FCDFoldAction(this)));
                lastTriedMove = new FCDFoldAction(this);
            } else {
                game.sendAction((new FCDCheckAction(this)));
                lastTriedMove = new FCDCheckAction(this);
            }
        }

        // if Ultron has one pair
        if (handVal == 1 && savedState.getGameStage() == 1) {
            game.sendAction((new FCDThrowAction(this, new int[]{0, 1, 2, 3, 4})));
            lastTriedMove = new FCDThrowAction(this, new int[]{0, 1, 2, 3, 4});
        }
        if (handVal == 1 && savedState.getGameStage() == 3) {
            if (savedState.getLastBet() < -1) {
                game.sendAction((new FCDFoldAction(this)));
                lastTriedMove = new FCDFoldAction(this);
            } else {
                game.sendAction((new FCDCheckAction(this)));
                lastTriedMove = new FCDCheckAction(this);
            }
        }
        // if Ultron has jack crap
        if (handVal == 0 && savedState.getGameStage() == 1) {
            game.sendAction((new FCDThrowAction(this, new int[]{0, 1, 2, 3, 4})));
            lastTriedMove = new FCDThrowAction(this, new int[]{0, 1, 2, 3, 4});
        }
        if (handVal == 0 && savedState.getGameStage() == 3) {
            if (savedState.getLastBet() < -1) {
                game.sendAction((new FCDFoldAction(this)));
                lastTriedMove = new FCDFoldAction(this);
            } else {
                game.sendAction((new FCDCheckAction(this)));
                lastTriedMove = new FCDCheckAction(this);
            }
        }


    }
}

