public abstract class ExporterFactory {

	// Factory Method (abstract) - subclasses override this
	// This is the core of the Factory Method pattern
	public abstract VideoExporter createExporter();

	// Template Method - defines the export workflow
	// Uses the factory method to get the specific exporter
	public void exportVideo(String videoPath) {
		// Step 1: Get the appropriate exporter using factory method
		VideoExporter exporter = createExporter();

		// Step 2: Use the exporter polymorphically
		exporter.export(videoPath);
	}

	// Convenience static method for factory selection
	// This is optional but makes client code cleaner
	public static ExporterFactory getFactory(String format) {
		switch (format.toUpperCase()) {
			case "MP4":
				return new MP4ExporterFactory();
			case "AVI":
				return new AVIExporterFactory();
			case "MOV":
				return new MOVExporterFactory();
			case "WEBM":
				return new WebMExporterFactory();
			default:
				throw new IllegalArgumentException("Unknown format: " + format);
		}
	}
}
