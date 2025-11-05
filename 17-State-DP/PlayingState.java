/**
 * PlayingState - Concrete state for playing video
 *
 * Represents the player actively playing video:
 * - Video is playing
 * - Position advancing
 * - Can be paused or stopped
 *
 * Valid transitions FROM Playing:
 * - pause() → PAUSED (pause at current position)
 * - stop() → STOPPED (stop and reset to 0)
 * - onBuffering() → BUFFERING (network slow)
 *
 * Invalid operations:
 * - play() → Already playing
 */
public class PlayingState implements PlayerState {

	@Override
	public void play(VideoPlayer player) {
		System.out.println("⚠️  Already playing!");
	}

	@Override
	public void pause(VideoPlayer player) {
		System.out.println("[PLAYING → PAUSED] Pausing at position " +
		                   player.getPosition() + "s...");
		player.setState(new PausedState());
	}

	@Override
	public void stop(VideoPlayer player) {
		System.out.println("[PLAYING → STOPPED] Stopping playback...");
		player.setPosition(0);
		player.setState(new StoppedState());
	}

	@Override
	public void onBuffering(VideoPlayer player) {
		System.out.println("[PLAYING → BUFFERING] Network slow - buffering content...");
		player.setState(new BufferingState());
	}

	@Override
	public String getStateName() {
		return "PLAYING";
	}
}
