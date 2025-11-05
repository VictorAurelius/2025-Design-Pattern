public class MOVExporterFactory extends ExporterFactory {

	@Override
	public VideoExporter createExporter() {
		System.out.println("[Factory] Creating MOV Exporter...");
		return new MOVExporter();
	}
}
