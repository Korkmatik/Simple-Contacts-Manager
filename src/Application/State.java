package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class State {
    public abstract State handle();

    public abstract String toString();

    protected void waitForEnterKeyInput(String stateName) {
        System.out.println("Press [ENTER] to return to " + stateName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
