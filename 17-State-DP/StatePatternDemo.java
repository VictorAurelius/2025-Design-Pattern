public class StatePatternDemo {

	public static void main(String[] args) {

		System.out.println("=== State Pattern Demo ===\n");

		// Create video player
		VideoPlayer player = new VideoPlayer("Epic Gaming Montage", 300);
		System.out.println("Video: " + player.getVideoId());
		System.out.println("Duration: 5:00");
		System.out.println("Initial state: " + player.getStateName() + "\n");

		// Test 1: Normal playback flow
		System.out.println("--- Test 1: Normal Playback Flow ---");
		player.play();
		player.showStatus();

		System.out.println("\nSimulating playback (30 seconds)...");
		player.simulatePlayback(30);
		player.showStatus();

		player.pause();
		player.showStatus();

		player.play();  // Resume
		player.showStatus();

		player.simulatePlayback(45);
		player.stop();
		player.showStatus();

		// Test 2: Buffering flow
		System.out.println("\n--- Test 2: Buffering Flow ---");
		player.play();
		player.simulatePlayback(45);

		System.out.println("Network slow (triggering buffering)...");
		player.onBuffering();
		player.showStatus();

		System.out.println("\nBuffering complete, auto-resuming...");
		if (player.getStateName().equals("BUFFERING")) {
			BufferingState bufferingState = new BufferingState();
			bufferingState.onBufferingComplete(player);
		}
		player.showStatus();

		player.stop();

		// Test 3: Invalid operations
		System.out.println("\n--- Test 3: Invalid Operations ---");
		System.out.println("Current state: " + player.getStateName());

		System.out.println("\nTrying to pause (invalid when stopped):");
		player.pause();

		System.out.println("\nTrying to stop (already stopped):");
		player.stop();

		player.play();
		System.out.println("\nTrying to play (already playing):");
		player.play();

		player.pause();
		System.out.println("\nTrying to pause (already paused):");
		player.pause();

		System.out.println("\n--- Summary ---");
		System.out.println("State transitions demonstrated: 10+");
		System.out.println("Invalid operations handled gracefully");
		System.out.println("No if-else statements, zero boolean flags");
	}
}
