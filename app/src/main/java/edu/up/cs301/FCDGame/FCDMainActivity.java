package edu.up.cs301.FCDGame;

        import java.util.ArrayList;

        import edu.up.cs301.game.GameMainActivity;
        import edu.up.cs301.game.GamePlayer;
        import edu.up.cs301.game.LocalGame;
        import edu.up.cs301.game.config.GameConfig;
        import edu.up.cs301.game.config.GamePlayerType;


public class FCDMainActivity extends GameMainActivity {

    // the port number that this game will use when playing over the network
    private static final int PORT_NUMBER = 2278;

    /**
     * Create the default configuration for this game:
     * - one human player vs. one computer player
     * - minimum of 1 player, maximum of 2
     *
     * @return
     * the new configuration object, representing the default configuration
     */
    @Override
    public GameConfig createDefaultConfig() {

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // FCD has two player types:  human and computer
        playerTypes.add(new GamePlayerType("Local Human Player") {
            public GamePlayer createPlayer(String name) {
                return new FCDHumanPlayer(name);
            }});
        playerTypes.add(new GamePlayerType("Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new DumbAI(name);
            }});



        // Create a game configuration class for FCD:
        GameConfig defaultConfig = new GameConfig(playerTypes, 1, 2, "FCD", PORT_NUMBER);
        defaultConfig.addPlayer("Human", 0); // player 1: a human player
        defaultConfig.addPlayer("Computer", 1); // player 2: a computer player
        defaultConfig.setRemoteData("Remote Human Player", "", 0);

        return defaultConfig;
    }//createDefaultConfig

    /**
     * create a local game
     *
     * @return
     * the local game, a FCD game
     */
    @Override
    public LocalGame createLocalGame() {
        return new FCDLocal();
    }

}