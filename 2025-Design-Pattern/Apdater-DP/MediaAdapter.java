public class MediaAdapter implements MediaPlayer {
    private AdvancedMediaPlayer advancedPlayer;
    
    public MediaAdapter() {
        this.advancedPlayer = new AdvancedMediaPlayer();
    }
    
    @Override
    public void play(String audioType, String filename) {
        if (audioType.equalsIgnoreCase("mp4")) {
            advancedPlayer.playMp4(filename);
        } else if (audioType.equalsIgnoreCase("vlc")) {
            advancedPlayer.playVlc(filename);
        }
    }
}
