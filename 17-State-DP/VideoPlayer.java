/**
 * VideoPlayer - Context class for State pattern
 *
 * Manages video playback and delegates behavior to current state.
 * Instead of using boolean flags (isPlaying, isPaused, etc.),
 * uses a single state object that encapsulates behavior.
 *
 * BEFORE STATE PATTERN (IF-ELSE HELL):
 * - 4 boolean flags: isPlaying, isPaused, isStopped, isBuffering
 * - Giant if-else in every method (20+ conditional branches)
 * - Manual flag management (easy to create invalid states)
 * - Cyclomatic complexity: 16 (VERY HIGH)
 *
 * AFTER STATE PATTERN (CLEAN):
 * - Single state object
 * - Delegate to state.play(), state.pause(), etc.
 * - No if-else statements
 * - Cyclomatic complexity: 2 (EXCELLENT)
 */
public class VideoPlayer {

	private PlayerState state;
	private String videoId;
	private int position;  // Current playback position in seconds
	private int duration;  // Total video duration in seconds

	/**
	 * Create new video player
	 *
	 * @param videoId Video identifier
	 * @param duration Video duration in seconds
	 */
	public VideoPlayer(String videoId, int duration) {
		this.videoId = videoId;
		this.duration = duration;
		this.position = 0;
		this.state = new StoppedState();  // Initial state
	}

	// ========================================
	// PLAYER CONTROLS (Delegate to current state)
	// ========================================

	/**
	 * Play button pressed
	 * Delegates to current state - no if-else needed!
	 */
	public void play() {
		state.play(this);
	}

	/**
	 * Pause button pressed
	 * Delegates to current state - no if-else needed!
	 */
	public void pause() {
		state.pause(this);
	}

	/**
	 * Stop button pressed
	 * Delegates to current state - no if-else needed!
	 */
	public void stop() {
		state.stop(this);
	}

	/**
	 * Network buffering triggered (internal event)
	 * Delegates to current state
	 */
	public void onBuffering() {
		state.onBuffering(this);
	}

	// ========================================
	// STATE MANAGEMENT
	// ========================================

	/**
	 * Set current state
	 * Called by state objects to transition to new state
	 *
	 * @param state New state to transition to
	 */
	public void setState(PlayerState state) {
		this.state = state;
	}

	/**
	 * Get current state name
	 *
	 * @return State name (STOPPED, PLAYING, PAUSED, BUFFERING)
	 */
	public String getStateName() {
		return state.getStateName();
	}

	// ========================================
	// PLAYER CONTEXT (Accessed by states)
	// ========================================

	/**
	 * Get video ID
	 *
	 * @return Video identifier
	 */
	public String getVideoId() {
		return videoId;
	}

	/**
	 * Get current playback position
	 *
	 * @return Position in seconds
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Set playback position
	 * Called by states to update position
	 *
	 * @param position New position in seconds
	 */
	public void setPosition(int position) {
		this.position = Math.max(0, Math.min(position, duration));
	}

	/**
	 * Get video duration
	 *
	 * @return Duration in seconds
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Simulate playback (advance position)
	 * Used in demo to show position changes
	 *
	 * @param seconds Seconds to advance
	 */
	public void simulatePlayback(int seconds) {
		position = Math.min(position + seconds, duration);
	}

	/**
	 * Display current player status
	 */
	public void showStatus() {
		System.out.println("Current State: " + getStateName() +
		                   " | Position: " + position + "s");
	}
}
