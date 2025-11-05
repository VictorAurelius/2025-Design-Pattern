public class MP4ExporterFactory extends ExporterFactory {

	@Override
	public VideoExporter createExporter() {
		System.out.println("[Factory] Creating MP4 Exporter...");
		return new MP4Exporter();
	}
}
