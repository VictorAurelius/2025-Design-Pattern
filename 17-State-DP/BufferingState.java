/**
 * BufferingState - Concrete state for buffering player
 *
 * Represents the player buffering content:
 * - Network slow, loading content
 * - Playback temporarily paused
 * - Position preserved
 * - Will auto-resume when buffering completes
 *
 * Valid transitions FROM Buffering:
 * - onBufferingComplete() → PLAYING (resume playback)
 * - stop() → STOPPED (cancel buffering)
 *
 * Invalid operations:
 * - play() → Wait for buffering to complete
 * - pause() → Can't pause while buffering
 * - onBuffering() → Already buffering
 */
public class BufferingState implements PlayerState {

	@Override
	public void play(VideoPlayer player) {
		System.out.println("⚠️  Please wait - buffering in progress...");
	}

	@Override
	public void pause(VideoPlayer player) {
		System.out.println("⚠️  Can't pause while buffering!");
	}

	@Override
	public void stop(VideoPlayer player) {
		System.out.println("[BUFFERING → STOPPED] Canceling buffering and stopping...");
		player.setPosition(0);
		player.setState(new StoppedState());
	}

	@Override
	public void onBuffering(VideoPlayer player) {
		System.out.println("⚠️  Already buffering!");
	}

	/**
	 * Handle buffering complete event
	 * Special method for BufferingState (not in interface)
	 *
	 * @param player VideoPlayer context for state transition
	 */
	public void onBufferingComplete(VideoPlayer player) {
		System.out.println("[BUFFERING → PLAYING] Buffering complete - resuming playback...");
		player.setState(new PlayingState());
	}

	@Override
	public String getStateName() {
		return "BUFFERING";
	}
}
