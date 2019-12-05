package dbUtil;

import java.sql.*;

public class Person {
    private static final String TABLE_NAME = "Person";

    private static final String COLUMN_PERSON_ID = "Person_ID";
    private static final String COLUMN_LAST_NAME = "lastname";
    private static final String COLUMN_FIRST_NAME = "firstname";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_STREET = "street";

    private int personID;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String street;

    private Connection allRowsInTableConnection;
    private Statement allRowsInTableStatement;
    private ResultSet allRowsInTable;
    private boolean isAllRowsInTableResultSetEmpty = true;

    public Person() {

    }

    public Person(String firstName, String lastName, String phoneNumber, String email, String street) {
        this(-1, firstName, lastName, phoneNumber, email, street);
    }

    public Person(int personID, String firstName, String lastName, String phoneNumber, String email, String street) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.street = street;
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public Person next() throws SQLException {
        if (allRowsInTable == null) {
            allRowsInTableConnection = DBUtil.getConnection();
            allRowsInTableStatement = allRowsInTableConnection.createStatement();

            String sql = String.format("SELECT * FROM %s", TABLE_NAME);
            System.out.println("Load all sql: " + sql);

            allRowsInTable = allRowsInTableStatement.executeQuery(sql);
        }

        isAllRowsInTableResultSetEmpty = true;

        while (allRowsInTable.next()) {
            isAllRowsInTableResultSetEmpty = false;
            int personID = allRowsInTable.getInt(COLUMN_PERSON_ID);
            String firstName = allRowsInTable.getString(COLUMN_FIRST_NAME);
            String lastName = allRowsInTable.getString(COLUMN_LAST_NAME);
            String phoneNumber = allRowsInTable.getString(COLUMN_PHONE_NUMBER);
            String email = allRowsInTable.getString(COLUMN_EMAIL);
            String street = allRowsInTable.getString(COLUMN_STREET);

            return new Person(personID, firstName, lastName, phoneNumber, email, street);
        }

        if (isAllRowsInTableResultSetEmpty) {
            allRowsInTable.close();
            allRowsInTableStatement.close();
            allRowsInTableConnection.close();

            allRowsInTable = null;
            allRowsInTableStatement = null;
            allRowsInTableConnection = null;
        }

        return null;
    }

    public boolean doesDatabaseContainMoreContacts() {
        return isAllRowsInTableResultSetEmpty;
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
                COLUMN_PERSON_ID, COLUMN_LAST_NAME,
                COLUMN_FIRST_NAME, COLUMN_PHONE_NUMBER,
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
