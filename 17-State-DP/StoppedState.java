/**
 * StoppedState - Concrete state for stopped player
 *
 * Represents the player in stopped state:
 * - Position is at 0
 * - No playback happening
 * - Ready to start from beginning
 *
 * Valid transitions FROM Stopped:
 * - play() → PLAYING (start from beginning)
 *
 * Invalid operations:
 * - pause() → Can't pause when stopped
 * - stop() → Already stopped
 * - onBuffering() → Can't buffer when not playing
 */
public class StoppedState implements PlayerState {

	@Override
	public void play(VideoPlayer player) {
		System.out.println("[STOPPED → PLAYING] Starting playback from beginning...");
		player.setPosition(0);
		player.setState(new PlayingState());
	}

	@Override
	public void pause(VideoPlayer player) {
		System.out.println("⚠️  Can't pause - player is stopped!");
	}

	@Override
	public void stop(VideoPlayer player) {
		System.out.println("⚠️  Already stopped!");
	}

	@Override
	public void onBuffering(VideoPlayer player) {
		System.out.println("⚠️  Can't buffer - not playing!");
	}

	@Override
	public String getStateName() {
		return "STOPPED";
	}
}
