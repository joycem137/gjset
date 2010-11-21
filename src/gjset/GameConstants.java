package gjset;

/**
 * This class stores a bunch of constants for the game.
 */
public class GameConstants
{
	public static final int GAME_STATE_NOT_STARTED = 0;
	public static final int GAME_STATE_IDLE = 1;
	public static final int GAME_STATE_SET_CALLED = 2;
	public static final int GAME_STATE_SET_FINISHED = 3; // This state represents the condition when a set was finished.
	public static final int GAME_STATE_GAME_OVER = 4;
	
	public static final int GAME_PORT = 15563;
	
	public static final String COMM_VERSION = "2";
	
	public static final int MAX_PLAYERS = 8;
	
	public static final int SET_POINTS = 3;
	public static final int SET_PENALTY = 2;
}
