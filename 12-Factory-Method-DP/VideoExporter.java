public interface VideoExporter {

	// Main export method
	void export(String videoPath);

	// Format information methods
	String getFormat();
	int getBitrate();
	String getCodec();
	String getFileExtension();
}
