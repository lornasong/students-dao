package database;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class StudentDao extends BaseDaoImpl<Student, Integer> {
	
	public StudentDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Student.class);
	}



	/**
	 * ADDS A TUPLE into mySQL database for a student using first name, last
	 * name, and age.
	 */
	public void addStudentToDatabase(String firstName, String lastName, int age) {

		try {
			create(new Student(firstName, lastName, age));
		} catch (SQLException e) {
			System.out.println("Error: adding student");
			e.printStackTrace();
		}
	}

	/**
	 * RETURNS STUDENT OBJECT given a particular primary key. Always returns one
	 * student to be used for further methods.
	 */
	public Student getStudentByPKey(int pKey) {

		QueryBuilder<Student, Integer> statementBuilder = queryBuilder();
		try {
			statementBuilder.where().like(Student.PRIMARY_KEY, pKey);
			List<Student> studentList = query(statementBuilder.prepare());
			return studentList.get(0);// Will always return one student in index
										// 0

		} catch (SQLException e) {
			System.out.println("Error: querying for student using primary key");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RETURNS LIST sorted by variable 'type.' Type options: 'age', 'lastName',
	 * 'firstName' or default no sorting (sorted by primary key).
	 * 
	 * Use in with method listToString(QueryBuilder) to render on webpage. E.G.
	 * listToString(getStudentListSorted)
	 */
	public List<Student> getStudentListSorted(String type) {

		QueryBuilder<Student, Integer> statementBuilder = queryBuilder();

		// Sort as necessary
		if (type.equals("age")) {
			statementBuilder.orderBy(Student.AGE, true);

		} else if (type.equals("lastName")) {
			statementBuilder.orderBy(Student.LAST_NAME, true);
		} else if (type.equals("firstName")) {
			statementBuilder.orderBy(Student.FIRST_NAME, true);
		} else {
			// Default: no sorting.
		}

		try {
			return statementBuilder.query();
		} catch (SQLException e) {
			System.out.println("Error in creating student list for views sorted: " + type);
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * SEARCHES FOR FIRST NAME and returns QueryBuilder of all matches that
	 * include user's input. That is: %input%. If input is null, returns all.
	 * 
	 * Use in with method listToString(QueryBuilder) to render on webpage. E.G.
	 * listToString(queryByFirstName)
	 */
	public List<Student> queryByFirstName(String firstName) {
		QueryBuilder<Student, Integer> statementBuilder = queryBuilder();
		try {

			statementBuilder.where().like(Student.FIRST_NAME,
					"%" + firstName + "%");
			return statementBuilder.query();

		} catch (SQLException e) {
			System.out.println("Error: querying first name");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * SEARCHES FOR LAST NAME and returns QueryBuilder of all matches that
	 * include user's input. That is: %input%. If input is null, returns all.
	 * 
	 * Use in with method listToString(QueryBuilder) to render on webpage. E.G.
	 * listToString(queryByLastName)
	 */
	public List<Student> queryByLastName(String lastName) {
		QueryBuilder<Student, Integer> statementBuilder = queryBuilder();
		try {

			statementBuilder.where().like(Student.LAST_NAME,
					"%" + lastName + "%");
			return statementBuilder.query();

		} catch (SQLException e) {
			System.out.println("Error: querying last name");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * SEARCHES FOR AGE and returns a QueryBuilder of all exact matches to
	 * user's input. If input is null, returns all.
	 * 
	 * Use in with method listToString(QueryBuilder) to render on webpage. E.G.
	 * listToString(queryByAge)
	 */
	public List<Student> queryByAge(String ageString) {

		QueryBuilder<Student, Integer> statementBuilder = queryBuilder();

		try {

			// No user input, return all
			if (ageString == null || ageString.trim().isEmpty()) {
				statementBuilder.where().isNotNull(Student.AGE);

			} else {
				// User inputed something, return matches if input like number

				int age = Integer.parseInt(ageString);
				statementBuilder.where().like(Student.AGE, age);
			}
			return statementBuilder.query();

		} catch (SQLException e) {
			System.out.println("Error: querying age");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error: age string cannot be parsed to int");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * SEARCHES FOR AGE, FIRSTNAME, LASTNAME and returns QueryBuilder of matches
	 * to user input. If input is null, returns all.
	 * 
	 * Use in with method listToString(QueryBuilder) to render on webpage. E.G.
	 * listToString(queryByMultipleFields)
	 * 
	 * SELECT * FROM student WHERE (firstName IS NULL OR FIRST_NAME = firstName)
	 * AND (lastName IS NULL OR LAST_NAME = lastName) AND (age IS NULL OR AGE =
	 * age)
	 */
	public List<Student> queryByMultipleFields(
			String firstName, String lastName, Integer age) {

		QueryBuilder<Student, Integer> allBuilder = queryBuilder();

		try {

			// No user input, return all
			if (age == null) {
				allBuilder.where()
						.like(Student.LAST_NAME, "%" + lastName + "%").and()
						.like(Student.FIRST_NAME, "%" + firstName + "%");

				// user inputed something, return matches if input like number
			} else {

				//int age = Integer.parseInt(ageString);//put outside. conversions

				allBuilder.where()
						.like(Student.LAST_NAME, "%" + lastName + "%").and()
						.like(Student.FIRST_NAME, "%" + firstName + "%").and()
						.like(Student.AGE, age);
			}

			return allBuilder.query();

		} catch (SQLException e) {
			System.out.println("Error: querying all fields");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Error: age string cannot be parsed to int");
			e.printStackTrace();
		}
		return null;
	}
}
