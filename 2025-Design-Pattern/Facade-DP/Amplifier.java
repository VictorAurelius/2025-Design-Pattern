public class Amplifier {
    public void on() {
        System.out.println("Amplifier: Turning on");
    }

    public void setInput(String input) {
        System.out.println("Amplifier: Setting input to " + input);
    }

    public void setVolume(int level) {
        System.out.println("Amplifier: Setting volume to " + level);
    }

    public void off() {
        System.out.println("Amplifier: Turning off");
    }
}
