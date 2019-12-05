package Application;

import dbUtil.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class UpdateContact extends State {
    private State nextState;

    public UpdateContact(State nextState) {
        this.nextState = nextState;
    }

    @Override
    public State handle() {
        printHeader();
        int id;
        try {
            id = getPersonID();
            updatePerson(id);

            System.out.println("Successfully updated contact!");
            waitForEnterKeyInput(nextState.toString());
        } catch (Exception e) {
            System.out.println("An error occured:");
            e.printStackTrace();
        }

        return nextState;
    }

    private void updatePerson(int id) throws IOException {
        Person person = null;

        try {
            person = new Person(id);

            if (doesUserWantToUpdate("First Name", person.getFirstName())) {
                String newFirstName = getNewValue("First Name");
                person.setFirstName(newFirstName);
            }
            if (doesUserWantToUpdate("Last Name", person.getLastName())) {
                person.setLastName(getNewValue("Last Name"));
            }
            if (doesUserWantToUpdate("Phone Number", person.getPhoneNumber())) {
                person.setPhoneNumber(getNewValue("Phone Number"));
            }
            if (doesUserWantToUpdate("Email", person.getEmail())) {
                person.setEmail(getNewValue("Email"));
            }
            if (doesUserWantToUpdate("Street", person.getStreet())) {
                person.setStreet(getNewValue("Street"));
            }
        } catch (SQLException e) {
            System.out.println("Could not load contact with id = " + Integer.toString(id));
            e.printStackTrace();
        }

        if (person != null) {
            try {
                person.update();
            } catch (SQLException e) {
                System.out.println("Error: Could not update contact!");
                e.printStackTrace();
            }
        }
    }

    private String getNewValue(String type) throws IOException {
        System.out.print("Enter new value for " + type + ": ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String newValue = bufferedReader.readLine().trim();

        return newValue;
    }

    private boolean doesUserWantToUpdate(String type, String firstName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        char input = '\0';

        while (true) {
            System.out.print("Do you want to update " + type + "('" + firstName + "')? (y/n) ");
            input = bufferedReader.readLine().trim().toLowerCase().charAt(0);

            if (input == 'y' || input == 'n') {
                break;
            }

            System.out.printf("'%s' is not a valid choice!", input);
        }

        if (input == 'y') {
            return true;
        }

        return false;
    }

    private int getPersonID() throws IOException, NumberFormatException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String input  = "";
        int index;
        try {
            System.out.print("Person ID: ");
            input = bufferedReader.readLine().trim();
            index = Integer.parseInt(input);

        } catch (NumberFormatException e) {
            System.out.println(input + " is not a valid number!");
            throw e;
        }

        return index;
    }

    private void printHeader() {
        System.out.println("-------------------------------");
        System.out.println("        Update Contact         ");
        System.out.println("-------------------------------");
    }

    @Override
    public String toString() {
        return "Update Contact";
    }
}
