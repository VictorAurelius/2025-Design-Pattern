import java.util.HashMap;
import java.util.Map;

public class IconFactory {

	// Object pool - stores flyweight objects
	private static Map<String, VideoIcon> iconPool = new HashMap<>();
	private static int createdCount = 0;
	private static int reusedCount = 0;

	// Private constructor - utility class
	private IconFactory() {
		throw new AssertionError("Cannot instantiate IconFactory");
	}

	public static synchronized VideoIcon getIcon(String iconType) {
		VideoIcon icon = iconPool.get(iconType);

		if (icon == null) {
			// Flyweight doesn't exist - create new one
			System.out.println("\n🆕 Creating NEW flyweight: " + iconType);

			switch (iconType.toLowerCase()) {
				case "play":
					icon = new PlayIcon();
					break;
				case "pause":
					icon = new PauseIcon();
					break;
				case "like":
					icon = new LikeIcon();
					break;
				case "share":
					icon = new ShareIcon();
					break;
				default:
					throw new IllegalArgumentException("Unknown icon type: " + iconType);
			}

			iconPool.put(iconType, icon);
			createdCount++;

		} else {
			// Flyweight exists - reuse it
			System.out.println("♻️  Reusing EXISTING flyweight: " + iconType);
			reusedCount++;
		}

		return icon;
	}

	public static int getPoolSize() {
		return iconPool.size();
	}

	public static int getCreatedCount() {
		return createdCount;
	}

	public static int getReusedCount() {
		return reusedCount;
	}

	public static void printStatistics() {
		System.out.println("\n╔════════════════════════════════════════════════════════════╗");
		System.out.println("║           FLYWEIGHT FACTORY STATISTICS                     ║");
		System.out.println("╚════════════════════════════════════════════════════════════╝");
		System.out.println("Unique flyweights in pool: " + getPoolSize());
		System.out.println("Total flyweights created: " + getCreatedCount());
		System.out.println("Total flyweights reused: " + getReusedCount());
		System.out.println("Reuse ratio: " + (getReusedCount() + getCreatedCount()) / getPoolSize() + ":1");
	}
}
