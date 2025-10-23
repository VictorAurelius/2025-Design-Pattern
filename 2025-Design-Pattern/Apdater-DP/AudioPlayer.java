public class AudioPlayer implements MediaPlayer {
    private MediaAdapter mediaAdapter;
    
    @Override
    public void play(String audioType, String filename) {
        // Phát MP3 trực tiếp
        if (audioType.equalsIgnoreCase("mp3")) {
            System.out.println("Playing MP3 file: " + filename);
        }
        // Sử dụng Adapter để phát MP4 và VLC
        else if (audioType.equalsIgnoreCase("mp4") || 
                 audioType.equalsIgnoreCase("vlc")) {
            mediaAdapter = new MediaAdapter();
            mediaAdapter.play(audioType, filename);
        } else {
            System.out.println("Invalid format: " + audioType);
        }
    }
}
