package Application;

import dbUtil.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class ListContacts extends State {
    Person personen;

    private State nextState;

    public ListContacts(State nextState) {
        this.nextState = nextState;

        personen = new Person();
    }

    @Override
    public State handle() {
        printHeader();
        printContacts();
        waitForEnterKeyInput(nextState.toString());

        return nextState;
    }

    private void printContacts() {
        for (int i = 0; i < 100; i++) {
            try {
                Person person = personen.next();

                if (person == null) {
                    return;
                }

                System.out.println(person.toString() + "\n");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (personen.doesDatabaseContainMoreContacts()) {
            System.out.print("Do you want to print more contacts? (y/n) ");


            char decision;
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                do {
                    decision = bufferedReader.readLine().trim().toLowerCase().charAt(0);

                    if (decision == 'y' || decision == 'n') {
                        break;
                    }

                    System.out.printf("'%c' is not a valid input!\n", decision);

                } while (true);

                bufferedReader.close();

                if (decision == 'y') {
                    printContacts();
                }
            } catch (IOException e) {
                System.out.println("Error while trying to read: ");
                e.printStackTrace();
            }
        }
    }

    private void printHeader() {
        System.out.println("-------------------------------");
        System.out.println("         List contacts         ");
        System.out.println("-------------------------------");
    }

    @Override
    public String toString() {
        return "List Contacts";
    }
}
