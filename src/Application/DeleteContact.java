package Application;

import dbUtil.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class DeleteContact extends State {
    private State nextState;

    public DeleteContact(State nextState) {
        this.nextState = nextState;
    }

    @Override
    public State handle() {
        printHeader();

        int id = 0;

        try {
            id = readContactID();
            Person.deleteByID(id);
        } catch (IOException e) {
            System.out.println("Could not read from console:");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.printf("Could not delete contact with ID: %d!\n", id);
        }

        return nextState;
    }

    private int readContactID() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int id = 0;
        while (true) {
            System.out.print("Contact ID: ");
            String input = bufferedReader.readLine().trim();

            try {
                id = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.printf("'%s' is not a number!\n", input);
            }
        }

        return id;
    }

    private void printHeader() {
        System.out.println("-------------------------------");
        System.out.println("        Delete Contact         ");
        System.out.println("-------------------------------");
    }

    @Override
    public String toString() {
        return "Delete Contact";
    }
}
