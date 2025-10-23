public class HomeTheaterFacade {
    private DimLights lights;
    private Screen screen;
    private Projector projector;
    private Amplifier amplifier;
    private DVDPlayer dvd;

    public HomeTheaterFacade(DimLights lights, Screen screen,
            Projector projector, Amplifier amplifier,
            DVDPlayer dvd) {
        this.lights = lights;
        this.screen = screen;
        this.projector = projector;
        this.amplifier = amplifier;
        this.dvd = dvd;
    }

    public void watchMovie(String movie) {
        System.out.println("\n=== Getting ready to watch movie... ===\n");
        lights.dim();
        screen.down();
        projector.on();
        projector.setInput("DVD");
        amplifier.on();
        amplifier.setInput("DVD");
        amplifier.setVolume(5);
        dvd.on();
        dvd.play(movie);
        System.out.println("\n=== Enjoy your movie! ===\n");
    }

    public void endMovie() {
        System.out.println("\n=== Shutting down home theater... ===\n");
        dvd.stop();
        dvd.off();
        amplifier.off();
        projector.off();
        screen.up();
        lights.brightOn();
        System.out.println("\n=== Home theater is off ===\n");
    }
}
