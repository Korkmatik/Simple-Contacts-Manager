package Application;

public class ExitApplication extends State {
    @Override
    public State handle() {
        System.exit(0);

        return null;
    }

    @Override
    public String toString() {
        return "Exit";
    }
}
