package org.garion.games.jeopardy.util;

/**
 * Describes the interface that a game UI should provide to show who is
 * currently ringing in. Also provides the ability to reset the UI to the
 * default state where no one is ringing in.
 * 
 * You *must* call everything in this interface on the Swing EDT.
 * 
 * @author martin@mbs3.org
 * 
 */
public interface ExternalPlayerController {

	/**
	 * Reset the UI to indicate that no player has rung in. 
	 */
	public void resetPlayer();

	/**
	 * Change the UI to indicate that player 'name' has rung in.
	 * @param name
	 */
	public void signalPlayer(String name);

}
