public class AVIExporterFactory extends ExporterFactory {

	@Override
	public VideoExporter createExporter() {
		System.out.println("[Factory] Creating AVI Exporter...");
		return new AVIExporter();
	}
}
