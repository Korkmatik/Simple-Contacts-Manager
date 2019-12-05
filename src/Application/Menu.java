package Application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Menu extends Application.State {

    private ArrayList<State> nextStates;

    private String userInput;

    public Menu() {
        nextStates = new ArrayList<>();

        nextStates.add(new CreateContact(this));
        nextStates.add(new ExitApplication());
    }

    @Override
    public State handle() {
        State nextState = null;

        do {
            printUI();
            getUserInput();
            nextState = handleUserInput();
        } while (nextState == null);

        return nextState;
    }

    private void getUserInput() {
        BufferedReader bufferedReader = null;

        System.out.print("Your choice: ");

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            userInput = bufferedReader.readLine().trim();
        } catch (Exception e) {
            System.out.println("Error:");
            e.printStackTrace();
            userInput = "";
        }
    }

    private State handleUserInput() {
        int index = 0;

        try {
            index = Integer.parseInt(userInput);
        } catch (Exception e) {
            System.out.println(userInput + " is not a valid menu entry!");
            return null;
        }


        if (index >= nextStates.size()) {
            return null;
        }

        return nextStates.get(index);
    }

    private void printUI() {
        for (int i = 0; i < nextStates.size(); i++) {
            System.out.printf("%d - %s\n", i, nextStates.get(i).toString());
        }
    }

    @Override
    public String toString() {
        return "Menu";
    }
}
