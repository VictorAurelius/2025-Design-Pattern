public class Main {
    public static void main(String[] args) {
        // Initialize all devices
        DimLights lights = new DimLights();
        Screen screen = new Screen();
        Projector projector = new Projector();
        Amplifier amplifier = new Amplifier();
        DVDPlayer dvd = new DVDPlayer();

        // Create the Facade
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
                lights, screen, projector, amplifier, dvd);

        // Client uses a simple facade method to watch a movie
        homeTheater.watchMovie("Avatar 2");

        // Then end the movie
        homeTheater.endMovie();
    }
}
