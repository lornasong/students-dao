package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;

/**
 * Summary of what this does and the purpose User-interface that interacts with
 * StudentDatabase
 * 
 * @author lsong
 */

public class Controller {

	public static void main(String[] args) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		// User enter school name for database
		StudentDatabase database = createSchoolDatabase(reader);

		printUserOptions();

		// Continuously looping to get user's option until user exits
		while (true) {

			// Reads user selection and branches to appropriate method
			branchOptionsToMethods(getUserOption(reader), database, reader);
			System.out
					.println("-----------------------------------------------------------");
			System.out.println("Please make another selection");

		}
	}

	private static StudentDatabase createSchoolDatabase(BufferedReader reader) {

		System.out
				.println("Please enter the name of your school to create database:");

		try {
			String schoolName = reader.readLine();
			StudentDatabase database = new StudentDatabase(schoolName);
			return database;

		} catch (IOException e) {
			System.out.println("Error occurred");
			return null;
		}

	}

	private static void printUserOptions() {

		System.out
				.println("Please select an option below by typing the option number and enter:");
		System.out.println("1. Add new student");
		System.out.println("2. Remove a student");
		System.out.println("3. View list of enrolled students");
		System.out.println("4. Search for a student");
		System.out.println("5. Edit student name");
		System.out.println("6. Sort students by age");
		System.out.println("7. Sort students by last name");
		System.out.println("8. Save database as CSV");
		System.out.println("9. Quit");
	}

	/**
	 * Gets and checks if user option is valid
	 */
	private static int getUserOption(BufferedReader reader) {

		while (true) {

			try {
				int userOption = Integer.parseInt(reader.readLine());

				// Check that user option is between 1 and 9
				if (0 < userOption && userOption < 10) {
					return userOption;
				} else {
					System.out
							.println("That option is invalid. Please select again");
				}

			} catch (NumberFormatException nfe) {
				System.out.println("Error: user input is not of type integer");
			} catch (IOException e) {
				System.out.println("Error occurred");

			}
		}
	}

	/**
	 * Takes user input of 1 - 9 and branches to appropriate method
	 */
	private static void branchOptionsToMethods(int userOption,
			StudentDatabase database, BufferedReader reader) {

		switch (userOption) {
		case 1:
			addNewStudent(database, reader);
			break;
		case 2:
			removeStudent(database, reader);
			break;
		case 3:
			printStudentList(database);
			break;
		case 4:
			searchForStudent(database, reader);
			break;
		case 5:
			editStudentName(database, reader);
			break;
		case 6:
			sortStudentByAge(database);
			break;
		case 7:
			sortStudentByLastName(database);
			break;
		case 8:
			saveDataAsCsv(database);
			break;
		case 9:
			quitMain();
			break;
		}

	}

	/**
	 * User Option 1: adds new student with first and last name and age. Checks
	 * that first name and last name are not both like-integer. Checks that age
	 * is reasonable.
	 */
	private static void addNewStudent(StudentDatabase database,
			BufferedReader reader) {

		try {

			System.out
					.println("Please enter the FIRST name of the student to add: ");
			String firstName = reader.readLine();

			System.out
					.println("Please enter the LAST name of the student to add: ");
			String lastName = reader.readLine();

			String concatenateName = firstName + lastName;
			concatenateName = concatenateName.trim();

			// Check if first + last name is like an integer or empty
			if (isInputNumeric(concatenateName) || concatenateName.isEmpty()) {

				System.out.println("Error: Invalid Student Name");

			} else {

				System.out.println("Please enter age of the student");
				int age = Integer.parseInt(reader.readLine());

				// Check if age is reasonable
				if (0 < age && age < 100) {

					database.addStudent(firstName, lastName, age);
					System.out.println("You have successfully added: "
							+ firstName + " " + lastName);
				} else {
					System.out.println("Error: invalid age");
				}

			}

		} catch (IOException e) {
			System.out.println("Error occurred");
		}
	}

	/**
	 * User Option 2: remove a student
	 */
	private static void removeStudent(StudentDatabase database,
			BufferedReader reader) {

		try {

			System.out
					.println("Please enter the FULL name of the student to remove: ");
			String fullName = reader.readLine();

			// Check if user entered a first and last name
			if (isInputFullName(fullName)) {
				database.removeStudent(fullName);
			} else {
				System.out.println("Error: full name not entered");
			}

		} catch (IOException e) {
			System.out.println("Error occurred");
		}
	}

	/**
	 * Checks that user input is a full name which means it contains a " " that
	 * is not leading or trailing
	 */
	private static boolean isInputFullName(String userInput) {
		if (userInput.trim().contains(" ")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * User Option 3: print a list of students enrolled
	 */
	private static void printStudentList(StudentDatabase database) {

		database.printStudents();
	}

	/**
	 * User Option 4: search for student either by id or full name depending on
	 * user input.
	 * 
	 * If name entered, check if it is a full name
	 */
	private static void searchForStudent(StudentDatabase database,
			BufferedReader reader) {

		try {

			System.out
					.println("Please enter the full name or ID of the student you wish to search");
			String userInput = reader.readLine();

			// Given ID, search for name
			if (isInputNumeric(userInput)) {

				try {
					System.out.println("Student name: "
							+ database.searchByID(Integer.parseInt(userInput)));
				} catch (NullPointerException e) {
					System.out
							.println("Error: student ID does not exist in database");
				}

				// Given full name, search for ID
			} else if (isInputFullName(userInput)) {

				try {
					int id = database.searchByFullName(userInput);

					System.out.println("Student ID: " + String.valueOf(id));
				} catch (NullPointerException e) {
					System.out
							.println("Error: student name does not exist in database");
				}

				// Not given full name (or ID)
			} else {

				System.out.println("Error: user input is not full name");
			}
		} catch (IOException e) {
			System.out.println("Error occurred");
		}
	}

	/**
	 * Checks whether string input is like-integer or not
	 */
	private static boolean isInputNumeric(String userInput) {

		try {
			Integer.parseInt(userInput);
			// User input is a number
			return true;
		} catch (NumberFormatException nfe) {
			// User input is not a number
			return false;
		}
	}

	/**
	 * User Option 5: edit student name. Take in full name. Enter in new first
	 * and last name
	 */
	private static void editStudentName(StudentDatabase database,
			BufferedReader reader) {

		try {
			System.out
					.println("Please enter the full name of the student you wish to edit");
			String oldFullName = reader.readLine();

			// Check that full name is given
			if (isInputFullName(oldFullName)) {

				try {

					// Try to find student based on first name
					Student student = database
							.getStudentByFullName(oldFullName);

					System.out
							.println("Please enter new FIRST name of the student: ");
					String newFirstName = reader.readLine();

					System.out
							.println("Please enter new LAST name of the student: ");
					String newLastName = reader.readLine();

					String concatenateName = newFirstName + newLastName;
					concatenateName = concatenateName.trim();

					// Check if first + last name is like an integer or empty
					if (isInputNumeric(concatenateName)
							|| concatenateName.isEmpty()) {

						System.out.println("Error: Invalid Student Name");

					} else {

						database.editStudentName(student, newFirstName,
								newLastName);
						System.out
								.println("You have successfully changed the student's name");
					}

				} catch (NullPointerException e) {
					System.out
							.println("Error: student name does not exist in database");
				}
			} else {
				System.out.println("Error: full name not entered");
			}
		} catch (IOException e) {
			System.out.println("Error occurred");
		}
	}

	/**
	 * User Option 6: sort student list by age
	 */
	private static void sortStudentByAge(StudentDatabase database) {
		Collections.sort(database.getStudentList(), new AgeComparator());
		printStudentList(database);
	}

	/**
	 * User Option 7: sort student list by ID
	 */
	private static void sortStudentByLastName(StudentDatabase database) {
		Collections.sort(database.getStudentList(), new LastNameComparator());
		printStudentList(database);
	}

	/**
	 * User Option 8: saves user's database as a csv file named by the school on
	 * desktop
	 */
	private static void saveDataAsCsv(StudentDatabase database) {
		String filename = "C:\\Users\\lsong\\Desktop\\"
				+ database.getSchoolName() + ".csv";

		File file = new File(filename);

		try {

			FileWriter fw = new FileWriter(file);
			PrintWriter out = new PrintWriter(fw);
			// very expensive. synchronized. try buffered writer wrapped in

			out.println("ID,Student Name,Age");

			for (Student s : database.getStudentList()) {
				out.println(s.getID() + "," + s.getFullName() + ", "
						+ s.getAge());
			}

			out.close(); // close will already flush
			fw.close();

			System.out.println("Your csv has been saved on your desktop.");

		} catch (IOException e) {
			System.out.println("Error saving file");
			e.printStackTrace();
		}

	}

	/**
	 * User Option 9: quit
	 */
	private static void quitMain() {
		System.out.println("Exiting database. Thank you.");
		System.exit(0);
	}
}
