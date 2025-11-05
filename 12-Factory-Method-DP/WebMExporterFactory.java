public class WebMExporterFactory extends ExporterFactory {

	@Override
	public VideoExporter createExporter() {
		System.out.println("[Factory] Creating WebM Exporter...");
		return new WebMExporter();
	}
}
