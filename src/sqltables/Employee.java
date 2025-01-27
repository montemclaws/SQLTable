package sqltables;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

/**
 * The employee class allows the user to create a table containing employees
 * once instantiated. It requires a Statement to be sent to it from the derby
 * database.
 * 
 * @authors Monte, Edwin, James, Kevin
 */
public class Employee {
	// Fields
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;

	/**
	 * Constructor for the employee class. Creating an instance of this class will
	 * allow the user to manipulate the Employee SQL Table
	 * 
	 * @param statement
	 * @author Kevin
	 */
	public Employee(Statement statement) {
		this.statement = statement;
		try {
			resultSet = statement.executeQuery(Employee.selectAll);
			metaData = resultSet.getMetaData();
		} catch (SQLException e) {
			System.out.println("Something went wrong accessing SQL");
		}

	}

	// These are the SQL methods... We can remove/change these later when we make
	// our own methods
	public static final String createTable = "CREATE TABLE Employee (" + "Id  int not null primary key "
			+ "GENERATED ALWAYS AS IDENTITY " + "(START WITH 100, INCREMENT BY 1)," + "FirstName varchar(255),"
			+ "LastName varchar(255)," + "JobTitle varchar(255)," + "DOB varchar(255)," + "StoreID int" + ")";

	public static final String insertData = "INSERT INTO Employee (FirstName, LastName, JobTitle, DOB, StoreID) VALUES "
			+ "('Tom','Ballinger', 'Clerk', '03/16/03', 3)," + "('Jessie','Romero', 'Manager', '07/21/97', 1),"
			+ "('Barry','Binkerhoff', 'Stocker', '01/05/02', 1)";

	public static final String selectAll = "SELECT * FROM Employee";

	public static final String dropTable = "DROP TABLE Employee";

	/**
	 * Prints the table out as a string (mostly for testing right now)
	 * 
	 * @param resultSet
	 * @throws SQLException
	 */
	public void printTableData() throws SQLException {
		resultSet = statement.executeQuery(selectAll);
		// print header
		int dashCount = 0;
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			System.out.print(metaData.getColumnLabel(i) + " ");
			dashCount += metaData.getColumnLabel(i).length() + 1;
		}
		System.out.println();
		System.out.println("-".repeat(--dashCount));

		// print data
		while (resultSet.next()) {
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				System.out.printf("%-" + (metaData.getColumnLabel(i).toString().length() + 1) + "s",
						resultSet.getObject(i) + " ");
			}
			System.out.println();
		}
	}

	/**
	 * overloaded method to print a row from the table based on the ResultSet
	 * object.
	 * 
	 * @ author Edwin
	 * 
	 * @param resultset - should be an identified row fron the table
	 */
	public void printTableData(ResultSet resultset) {
		// print header
		int dashCount = 0;
		try {
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				System.out.print(metaData.getColumnLabel(i) + " ");
				dashCount += metaData.getColumnLabel(i).length() + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("-".repeat(--dashCount));

		// print data
		try {
			while (resultSet.next()) {
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					System.out.printf("%-" + (metaData.getColumnLabel(i).toString().length() + 1) + "s",
							resultSet.getObject(i) + " ");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the amount of columns in the Employee database.
	 * 
	 * @return
	 * @author Kevin
	 */
	public int getColumnCount() {
		try {
			return metaData.getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Java method for executing the dropTable SQL method.
	 * 
	 * @author Kevin
	 */
	public void dropTable() {
		try {
			statement.execute(dropTable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Java method for executing the createTable SQL method.
	 * 
	 * @author Kevin
	 */
	public void createTable() {
		try {
			statement.execute(createTable);
			resultSet = statement.executeQuery(Employee.selectAll);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Java method for executing the insertData SQL method.
	 * 
	 * @author Kevin
	 */
	public void insertData() {
		try {
			statement.execute(insertData);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param query
	 * @return Returns a DefaultTableModel for use with JTable
	 * @author Monte
	 */
	public DefaultTableModel getTableModel(String query) {
		try {
			DefaultTableModel model = new DefaultTableModel();

			ResultSet resultSet = statement.executeQuery(query);

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				model.addColumn(metaData.getColumnName(i));
			}

			while (resultSet.next()) {
				Object[] rowData = new Object[columnCount];
				for (int i = 1; i <= columnCount; i++) {
					rowData[i - 1] = resultSet.getObject(i);
				}
				model.addRow(rowData);
			}

			return model;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Adds a new employee to the Employee Database
	 * 
	 * @param fName   - First Name
	 * @param LName   - Last Name
	 * @param title   - Job title/Position
	 * @param dob     - Date of Birth
	 * @param storeID - Store ID of the new employee
	 * 
	 * @author Edwin Casady
	 */
	public void addEmployee(String fName, String lName, String title, String dob, int storeID) {
		String s = String.format("INSERT INTO Employee (FirstName, LastName, JobTitle, DOB, StoreID) VALUES "
				+ "('%s','%s', '%s', '%s', %d)", fName, lName, title, dob, storeID);
		try {
			statement.execute(s);
		} catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
		}
	}

	public String[] getEmployeeInfo(int idNum) {
		String row = String.format("SELECT * FROM Employee WHERE Id=%d", idNum);
		String[] infos = new String[6];
		try {
			resultSet = statement.executeQuery(row);

			if (resultSet.next()) {
				for (int i = 0; i < infos.length; i++) {
					infos[i] = resultSet.getString(i + 1);
				}
			}
			return infos;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Removes an employee from the Employee SQL Table
	 * 
	 * @param id
	 * @author James
	 */
	public void removeEmployee(int id) {
		String s = String.format("DELETE FROM Employee WHERE Id = %d", id);
		try {
			statement.execute(s);
		} catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
		}
	}

	/**
	 * Updates an employee from the Employee SQL Table
	 * 
	 * @param id
	 * @author James
	 */
	public void updateEmployee(String fName, String lName, String title, String dob, int storeID, int id) {
		String s = String.format("UPDATE Employee SET FirstName = '%s', LastName = '%s', JobTitle = '%s',"
				+ " DOB = '%s', StoreID = %d WHERE Id = %d", fName, lName, title, dob, storeID, id);
		try {
			statement.execute(s);
		} catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sorts the Employee SQL Table depending on the selection made by the button.
	 * 
	 * @param selection
	 * @return resultSet
	 * @author James
	 */
	public String sortEmployee(String selection) {

		String s = String.format("SELECT * FROM Employee "

		+ "ORDER BY %s", selection);

		return s;

		}
	
	/**
	 * Filters the Employee SQL Table depending on the given columns name and the contents inside.
	 * 
	 * @param name
	 * @param filter
	 * @return resultSet
	 * @author James
	 */
	public String filterEmployee(String name, String filter) {

		String n;

		if(name == "Id" || name == "StoreID")

		n = filter;

		else

		n = "'" + filter + "'";

		String s = String.format("SELECT * FROM Employee WHERE %s = %s", name, n);

		return s;

		}
	/**
	 * Method to get the names of the columns as an array of strings
	 * @return Returns the names of the columns of the table as an array of strings 
	 * @author Kevin Dang
	 */
	public String[] getColumnNames()
	{
		try {
		String[] columnNames = new String[metaData.getColumnCount()];
		for(int i = 1; i <= metaData.getColumnCount(); i++)
		{
			columnNames[i-1] = metaData.getColumnLabel(i);
		}
		return columnNames;
		}catch(SQLException e)
		{
			System.out.println("Something went wrong with SQL");
			e.printStackTrace();
		}
		return null;
	}

}