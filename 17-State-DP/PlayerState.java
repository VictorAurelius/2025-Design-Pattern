/**
 * PlayerState - State interface for video player
 *
 * Defines the contract that all concrete player states must implement.
 * Each state handles player controls (play, pause, stop) differently.
 *
 * STATE PATTERN KEY CONCEPTS:
 * - Encapsulate state-specific behavior in separate classes
 * - Context (VideoPlayer) delegates to current state
 * - States manage their own transitions
 *
 * Why pass VideoPlayer parameter?
 * - States need access to player context (position, videoId)
 * - States need to trigger state transitions: player.setState()
 * - Enables states to modify player state
 */
public interface PlayerState {

	/**
	 * Handle play button press
	 * Behavior varies by state:
	 * - Stopped: Start from beginning
	 * - Paused: Resume from current position
	 * - Playing: Already playing (no-op)
	 * - Buffering: Wait for buffer to complete
	 *
	 * @param player VideoPlayer context for state transitions
	 */
	void play(VideoPlayer player);

	/**
	 * Handle pause button press
	 * Behavior varies by state:
	 * - Playing: Pause at current position
	 * - Paused: Already paused (no-op)
	 * - Stopped: Invalid operation
	 * - Buffering: Invalid operation
	 *
	 * @param player VideoPlayer context for state transitions
	 */
	void pause(VideoPlayer player);

	/**
	 * Handle stop button press
	 * Behavior varies by state:
	 * - Playing/Paused: Stop and reset to position 0
	 * - Stopped: Already stopped (no-op)
	 * - Buffering: Cancel buffering and stop
	 *
	 * @param player VideoPlayer context for state transitions
	 */
	void stop(VideoPlayer player);

	/**
	 * Handle network buffering event
	 * Behavior varies by state:
	 * - Playing: Transition to buffering
	 * - Other states: Invalid operation
	 *
	 * @param player VideoPlayer context for state transitions
	 */
	void onBuffering(VideoPlayer player);

	/**
	 * Get state name for display
	 *
	 * @return State name (STOPPED, PLAYING, PAUSED, BUFFERING)
	 */
	String getStateName();
}
