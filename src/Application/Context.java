package Application;

public class Context {
    State startingState;
    State currentState;

    public Context() {
        startingState = new Menu();

        currentState = startingState;
    }

    public void run() {
        while (true) {
            currentState.handle();
        }
    }
}
