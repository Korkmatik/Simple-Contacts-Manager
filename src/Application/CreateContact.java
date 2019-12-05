package Application;

import dbUtil.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class CreateContact extends State {
    State nextState;
    BufferedReader bufferedReader;

    public CreateContact(State nextState) {
        this.nextState = nextState;
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public State handle() {
        printHeader();

        String firstName, lastName, phoneNumber, email, street;

        try {
            firstName = readFirstName();
            lastName = readLastName();
            phoneNumber = readPhoneNumber();
            email = readEmail();
            street = readStreet();

            Person person = new Person(firstName, lastName, phoneNumber, email, street);

            char decision;
            do {
                System.out.println("---------------------------------------------");
                System.out.println(person.toString());
                System.out.print("Do you really want to save this Contact? (y/n) ");
                decision = bufferedReader.readLine().trim().toLowerCase().charAt(0);

                if (decision == 'y' || decision == 'n') {
                    break;
                }
                System.out.printf("'%s' is not valid!\n", decision);
            } while (true);

            if (decision == 'y') {
                try {
                    person.saveToDatabase();
                    System.out.printf("Successfully saved '%s %s'!\n", person.getFirstName(), person.getLastName());
                } catch (SQLException e) {
                    System.out.printf("Could not save '%s %s' due to an error!\n", person.getFirstName(), person.getLastName());
                    e.printStackTrace();
                }

            }
            else {
                System.out.printf("'%s %s' won't be saved!\n", person.getFirstName(), person.getLastName());
            }

        } catch (IOException e) {
            System.out.println("Error while trying to read from console: ");
            e.printStackTrace();
        }

        return nextState;
    }

    private String readStreet() throws IOException {
        return readFromConsole("Street: ");
    }

    private String readEmail() throws IOException {
        return readFromConsole("Email: ");
    }

    private String readPhoneNumber() throws IOException {
        return readFromConsole("Phone Number: ");
    }

    private String readLastName() throws IOException {
        return readFromConsole("Last Name: ");
    }

    private String readFirstName() throws IOException {
        return readFromConsole("First Name: ");
    }

    private String readFromConsole(String message) throws IOException {
        System.out.print(message);

        return bufferedReader.readLine().trim();
    }

    private void printHeader() {
        System.out.println("-------------------------------");
        System.out.println("       Create a contact        ");
        System.out.println("-------------------------------");
    }

    @Override
    public String toString() {
        return "Create Contact";
    }
}
