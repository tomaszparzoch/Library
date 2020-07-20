package pl.javastart.library.app;

public class LibraryApp {
    private static final String APP_NAME = "Biblioteka v1.6";

    public static void main(String[] args) {

        System.out.println(APP_NAME);
        LibraryControl libcontrol = new LibraryControl();
        libcontrol.controlLoop();
    }
}
