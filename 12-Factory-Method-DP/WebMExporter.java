public class WebMExporter implements VideoExporter {

	@Override
	public void export(String videoPath) {
		System.out.println("WebMExporter: Exporting " + videoPath);
		System.out.println("Output: " + videoPath.replace(".mp4", "_exported.webm"));
		System.out.println("Codec: " + getCodec() + ", Bitrate: " + getBitrate() + " kbps");
		System.out.println("WebM export completed");
	}

	@Override
	public String getFormat() {
		return "WebM";
	}

	@Override
	public int getBitrate() {
		return 3000;  // kbps - optimized for web
	}

	@Override
	public String getCodec() {
		return "VP9 (Web Optimized)";
	}

	@Override
	public String getFileExtension() {
		return ".webm";
	}
}
