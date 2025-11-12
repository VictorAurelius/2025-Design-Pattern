public class FactoryMethodDemo {

	public static void main(String[] args) {

		System.out.println("=== Factory Method Pattern Demo ===\n");

		// Test 1: Using different factory types
		System.out.println("--- Test 1: Different Export Formats ---");
		ExporterFactory mp4Factory = new MP4ExporterFactory();
		VideoExporter mp4Exporter = mp4Factory.createExporter();
		mp4Exporter.export("/videos/tutorial.mp4");

		ExporterFactory aviFactory = new AVIExporterFactory();
		aviFactory.exportVideo("/videos/tutorial.mp4");

		ExporterFactory movFactory = ExporterFactory.getFactory("MOV");
		movFactory.exportVideo("/videos/tutorial.mp4");

		ExporterFactory webmFactory = ExporterFactory.getFactory("WEBM");
		webmFactory.exportVideo("/videos/tutorial.mp4");

		// Test 2: Polymorphism - batch export
		System.out.println("\n--- Test 2: Batch Export (Polymorphism) ---");
		ExporterFactory[] factories = {
			new MP4ExporterFactory(),
			new AVIExporterFactory(),
			new MOVExporterFactory(),
			new WebMExporterFactory()
		};

		System.out.println("Processing batch export for: /videos/demo.mp4");
		for (ExporterFactory factory : factories) {
			VideoExporter exporter = factory.createExporter();
			System.out.println("Format: " + exporter.getFormat() +
			                   " (Codec: " + exporter.getCodec() +
			                   ", Bitrate: " + exporter.getBitrate() + " kbps)");
		}

		// Test 3: Device-specific export
		System.out.println("\n--- Test 3: Device-Specific Export ---");
		String[] devices = {"iPhone", "Android", "Web Browser", "Video Editor"};
		String[] formats = {"MOV", "MP4", "WebM", "AVI"};

		for (int i = 0; i < devices.length; i++) {
			System.out.println("Device: " + devices[i] + " -> Recommended format: " + formats[i]);
			ExporterFactory factory = ExporterFactory.getFactory(formats[i]);
			VideoExporter exporter = factory.createExporter();
			System.out.println("  Using codec: " + exporter.getCodec());
		}
	}
}
