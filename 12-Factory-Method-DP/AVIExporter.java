public class AVIExporter implements VideoExporter {

	@Override
	public void export(String videoPath) {
		System.out.println("AVIExporter: Exporting " + videoPath);
		System.out.println("Output: " + videoPath.replace(".mp4", "_exported.avi"));
		System.out.println("Codec: " + getCodec() + ", Bitrate: " + getBitrate() + " kbps");
		System.out.println("AVI export completed");
	}

	@Override
	public String getFormat() {
		return "AVI";
	}

	@Override
	public int getBitrate() {
		return 8000;  // kbps - higher quality for editing
	}

	@Override
	public String getCodec() {
		return "XVID (High Quality)";
	}

	@Override
	public String getFileExtension() {
		return ".avi";
	}
}
