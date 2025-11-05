public class MOVExporter implements VideoExporter {

	@Override
	public void export(String videoPath) {
		System.out.println("\n[MOVExporter] Starting export...");
		System.out.println("  📁 Input: " + videoPath);
		System.out.println("  📦 Output: " + videoPath.replace(".mp4", "_exported.mov"));
		System.out.println("  🎞️  Codec: " + getCodec());
		System.out.println("  📊 Bitrate: " + getBitrate() + " kbps");
		System.out.println("  📄 Extension: " + getFileExtension());
		System.out.println("  🎯 Use case: Apple devices, professional editing");
		System.out.println("  ⏱️  Processing... [████████████████████] 100%");
		System.out.println("  ✓ MOV export completed!");
	}

	@Override
	public String getFormat() {
		return "MOV";
	}

	@Override
	public int getBitrate() {
		return 12000;  // kbps - professional quality
	}

	@Override
	public String getCodec() {
		return "Apple ProRes (Professional)";
	}

	@Override
	public String getFileExtension() {
		return ".mov";
	}
}
