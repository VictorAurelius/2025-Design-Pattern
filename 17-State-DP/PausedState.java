/**
 * PausedState - Concrete state for paused player
 *
 * Represents the player in paused state:
 * - Playback paused at current position
 * - Position preserved
 * - Ready to resume
 *
 * Valid transitions FROM Paused:
 * - play() → PLAYING (resume from current position)
 * - stop() → STOPPED (stop and reset to 0)
 *
 * Invalid operations:
 * - pause() → Already paused
 * - onBuffering() → Can't buffer when paused
 */
public class PausedState implements PlayerState {

	@Override
	public void play(VideoPlayer player) {
		System.out.println("[PAUSED → PLAYING] Resuming playback from position " +
		                   player.getPosition() + "s...");
		player.setState(new PlayingState());
	}

	@Override
	public void pause(VideoPlayer player) {
		System.out.println("⚠️  Already paused!");
	}

	@Override
	public void stop(VideoPlayer player) {
		System.out.println("[PAUSED → STOPPED] Stopping playback...");
		player.setPosition(0);
		player.setState(new StoppedState());
	}

	@Override
	public void onBuffering(VideoPlayer player) {
		System.out.println("⚠️  Can't buffer - not playing!");
	}

	@Override
	public String getStateName() {
		return "PAUSED";
	}
}
