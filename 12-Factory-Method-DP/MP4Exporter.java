public class MP4Exporter implements VideoExporter {

	@Override
	public void export(String videoPath) {
		System.out.println("MP4Exporter: Exporting " + videoPath);
		System.out.println("Output: " + videoPath.replace(".mp4", "_exported.mp4"));
		System.out.println("Codec: " + getCodec() + ", Bitrate: " + getBitrate() + " kbps");
		System.out.println("MP4 export completed");
	}

	@Override
	public String getFormat() {
		return "MP4";
	}

	@Override
	public int getBitrate() {
		return 5000;  // kbps - balanced for web streaming
	}

	@Override
	public String getCodec() {
		return "H.264 (High Efficiency)";
	}

	@Override
	public String getFileExtension() {
		return ".mp4";
	}
}
