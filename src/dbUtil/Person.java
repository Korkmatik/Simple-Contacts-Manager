package dbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Person {
    private static final String TABLE_NAME = "Person";

    private static final String COLUMN_PERSON_ID = "Person_ID";
    private static final String COLUMN_LASTNAME = "lastname";
    private static final String COLUMN_FIRSTNAME = "firstname";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_STREET = "street";

    private int personID;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String street;

    public Person(String firstName, String lastName, String phoneNumber, String email, String street) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.street = street;
    }

    public void saveToDatabase() throws SQLException {
        createTableIfNotExists();
        save();
    }

    private void save() throws SQLException {
        Connection connection = DBUtil.getConnection();

        String sql = String.format(
                "INSERT INTO %s " +
                "VALUES (NULL, ?, ?, ?, ?, ?)",
                TABLE_NAME
        );

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, lastName);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, phoneNumber);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5, street);

        preparedStatement.execute();

        if (preparedStatement != null) {
            preparedStatement.close();
        }

        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public String toString() {
        String toString = "";
        toString += "Firstname: " + firstName + "\n";
        toString += "Lastname: " + lastName + "\n";
        toString += "Phone Number: " + phoneNumber + "\n";
        toString += "Email: " + email + "\n";
        toString += "Street: " + street;

        return toString;
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                "%s INTEGER PRIMARY KEY, " +
                "%s TEXT NOT NULL, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT)",
                TABLE_NAME,
                COLUMN_PERSON_ID, COLUMN_LASTNAME,
                COLUMN_FIRSTNAME, COLUMN_PHONE_NUMBER,
                COLUMN_EMAIL, COLUMN_STREET
                );

        Connection connection = null;
        Statement statement = null;
        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();

            statement.execute(sql);
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
