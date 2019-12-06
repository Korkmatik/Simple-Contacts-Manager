package dbUtil;

import Application.State;
import com.sun.source.util.SimpleTreeVisitor;

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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getStreet() {
        return street;
    }

    private String phoneNumber;
    private String email;
    private String street;

    private Connection allRowsInTableConnection;
    private Statement allRowsInTableStatement;
    private ResultSet allRowsInTable;
    private boolean isAllRowsInTableResultSetEmpty = true;

    public Person() {

    }

    public Person(int id) throws SQLException {
        Person person = loadPersonByID(id);

        this.personID = person.personID;
        this.firstName = person.firstName;
        this.lastName = person.lastName;
        this.phoneNumber = person.phoneNumber;
        this.email = person.email;
        this.street = person.street;
    }

    public static Person loadPersonByID(int id) throws SQLException {
        Connection connection = DBUtil.getConnection();

        String sql = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_PERSON_ID + " = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        id = resultSet.getInt(COLUMN_PERSON_ID);
        String firstName = resultSet.getString(COLUMN_FIRST_NAME);
        String lastName = resultSet.getString(COLUMN_LAST_NAME);
        String phoneNumber = resultSet.getString(COLUMN_PHONE_NUMBER);
        String email = resultSet.getString(COLUMN_EMAIL);
        String street = resultSet.getString(COLUMN_STREET);

        Person person = new Person(id, firstName, lastName, phoneNumber, email, street);

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return person;
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

    public int getPersonID() { return personID; }

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

    public static Person LoadFromDatabaseByID(int personID) throws SQLException {
        Connection connection = DBUtil.getConnection();

        String sql = String.format(
                "SELECT * FROM %s WHERE %s = ?",
                TABLE_NAME, COLUMN_PERSON_ID
        );

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        Person person = null;

        if (resultSet.next()) {
            int id = resultSet.getInt(COLUMN_PERSON_ID);
            String lastName = resultSet.getString(COLUMN_LAST_NAME);
            String firstName = resultSet.getString(COLUMN_FIRST_NAME);
            String phoneNumber = resultSet.getString(COLUMN_PHONE_NUMBER);
            String email = resultSet.getString(COLUMN_EMAIL);
            String street = resultSet.getString(COLUMN_STREET);

            person = new Person(id, firstName, lastName, phoneNumber, email, street);
        }

        return person;
    }

    private static int loadPersonID(ResultSet resultSet) throws SQLException {
        return resultSet.getInt(COLUMN_PERSON_ID);
    }

    public void update() throws SQLException {
        if (personID == -1)
            return;

        Connection connection = DBUtil.getConnection();

        String sql = "UPDATE " + TABLE_NAME +
                " SET " +
                COLUMN_FIRST_NAME + " = ?, " +
                COLUMN_LAST_NAME + " = ?, " +
                COLUMN_PHONE_NUMBER + " = ?, " +
                COLUMN_EMAIL + " = ?, " +
                COLUMN_STREET + " = ? " +
                "WHERE " + COLUMN_PERSON_ID + " = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        preparedStatement.setString(3, phoneNumber);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5, street);
        preparedStatement.setInt(6, personID);

        preparedStatement.execute();

        preparedStatement.close();
        connection.close();
    }

    public static void deleteByID(int id) throws SQLException {
        String sql = String.format("DELETE FROM %s WHERE %s = ?", TABLE_NAME, COLUMN_PERSON_ID);

        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, id);
        preparedStatement.execute();

        preparedStatement.close();
        connection.close();
    }

    @Override
    public String toString() {
        String toString = "";

        if (personID != -1) {
            toString += "ID: " + Integer.toString(personID) + "\n";
        }

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
