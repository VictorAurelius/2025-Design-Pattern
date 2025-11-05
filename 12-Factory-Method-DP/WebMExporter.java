public class WebMExporter implements VideoExporter {

	@Override
	public void export(String videoPath) {
		System.out.println("\n[WebMExporter] Starting export...");
		System.out.println("  📁 Input: " + videoPath);
		System.out.println("  📦 Output: " + videoPath.replace(".mp4", "_exported.webm"));
		System.out.println("  🎞️  Codec: " + getCodec());
		System.out.println("  📊 Bitrate: " + getBitrate() + " kbps");
		System.out.println("  📄 Extension: " + getFileExtension());
		System.out.println("  🎯 Use case: Web optimization, smaller file size");
		System.out.println("  ⏱️  Processing... [████████████████████] 100%");
		System.out.println("  ✓ WebM export completed!");
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
