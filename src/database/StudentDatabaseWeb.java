package database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Student Database Class that uses Student. This version is built off of Class
 * StudentDatabase but adapted to work with a webpage rather than a console.
 * Includes methods that are in Class Controller.
 * 
 * @author lsong
 */
public class StudentDatabaseWeb {

	private final String schoolName;
	private final List<Student> studentList = new ArrayList<Student>();
		
	public StudentDatabaseWeb(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	/**
	 * Creates an instance of Student and adds object to arrayList of database
	 */
	public String addStudent(String firstName, String lastName, int age) {
		Student student = new Student(firstName, lastName, age);
		studentList.add(student);
		return student.toString() + " has been sucessfully added";
	}

	/**
	 * Updated: Combination of getStudentbyID and getStudentByFullName and its
	 * search methods. Needs to be redone for more complex search queries. Get
	 * ID as a string to consider cases when ID is not inputed e.g.
	 * 
	 */
	public String searchStudentByParameters(String fullName, int id) {

		// If ID field is provided, check only ID since ID is unique.
		if (id > 0) {
			for (Student student : studentList) {
				if (student.getId() == id) {

					return "<br/>" + student.toString();
				}
			}
		}

		// Find all students who have the full name with matches. May not be
		// unique
		String matches = "";
		if (fullName != null && !fullName.trim().isEmpty()) {
			for (Student student : studentList) {

				if (student.getFullName().equalsIgnoreCase(fullName)) {
					matches = matches + "<br/>" + student.toString();
				}
			}

			if (matches.equals("")) {
				return "No matches";
			} else {
				return matches;
			}
		}

		return "No matches";
	}

	/**
	 * Look for student's full name in array list. Using for-each loop.
	 */
	public Student getStudentByFullName(String fullName) {

		for (Student student : studentList) {

			// Student is in student array list
			if (student.getFullName().equals(fullName)) {
				return student;
			}
		}
		// Student is not in array list
		return null;
	}

	/**
	 * Look for student id in array list. Using while loop iterator Notes: use
	 * iterator when removing things from that array
	 */
	public Student getStudentByID(int studentID) {

		Iterator<Student> it = studentList.iterator();

		// Loop through student array list to remove
		while (it.hasNext()) {
			Student student = it.next();

			// Student is in student array list
			if (student.getId() == studentID) {
				return student;
			}
		}
		// Student is not in array
		return null;
	}

	/**
	 * Returns list of students that breaks after each student using html. Based
	 * off of StudentDatabase.printStudents(). Intended use is for web page.
	 */
	public String getStudentListString() {

		// Case that no student is enrolled
		if (studentList.isEmpty()) {
			return "Error: No students are enrolled";
		}
		// Case that students are enrolled
		else {

			StringBuilder sb = new StringBuilder();

			for (Student student : studentList) {
				sb.append(student); // This is the student.toString()
				sb.append("<br/>");
			}
			return sb.toString();
		}
	}

	/**
	 * Sorts studentList by ID then returns list in format for html view
	 * webpage.
	 */
	public String getStudentListById() {
		//Collections.sort(getStudentList(), new IdComparator());
		return getStudentListString();
	}

	/**
	 * Sorts studentList by last name then returns list in format for html view
	 * webpage.
	 */
	public String getStudentListByLastName() {
		//Collections.sort(getStudentList(), new LastNameComparator());
		return getStudentListString();
	}

	/**
	 * Sorts studentList by age then returns list in format for html view
	 * webpage.
	 */
	public String getStudentListByAge() {
		//Collections.sort(getStudentList(), new AgeComparator());
		return getStudentListString();
	}

	/**
	 * Allows user to update first name, last name, and/or age for a particular
	 * student.
	 */
	public String editStudentName(Student student, String newFirstName,
			String newLastName, int newAge) {
		if (newFirstName.trim().isEmpty()) {
			return "Error: Please enter a first name";
		} else {
			if (newLastName.trim().isEmpty()) {
				return "Error: Please enter a last name";
			} else {
				if (newAge < 0) {
					return "Error: Please enter a valid age";
				} else {
					student.setFirstName(newFirstName);
					student.setLastName(newLastName);
					student.setAge(newAge);
					return student.toString()
							+ " has been successfully modified.";
				}
			}
		}

	}
	
	/**
	 * Assumes ID has already been searched and existed. Does not have error
	 * handling.
	 */
	public String removeStudentByID(int id) {
		Student student = getStudentByID(id);
		studentList.remove(student);
		return "Student ID:" + id + " has been successfully disenrolled";
	}

	/**
	 * Saves user's database as a csv file named by the school on desktop
	 */
	public String saveDataAsCsv() {
		String filename = "/Users/lornasong/Desktop/" + getSchoolName()
				+ ".csv";

		File file = new File(filename);

		try {

			FileWriter fw = new FileWriter(file);
			PrintWriter out = new PrintWriter(fw);
			// very expensive. synchronized. try buffered writer wrapped in

			out.println("ID,Student Name,Age");

			for (Student s : studentList) {
				out.println(s.getId() + "," + s.getFullName() + ", "
						+ s.getAge());
			}

			out.close(); // close will already flush
			fw.close();

			return "Your csv has been saved on your desktop.";

		} catch (IOException e) {
			return "Error saving file";
		}

	}

	/**
	 * DEPRECATED: NOT USED FOR WEB PAGE Returns the student ID for a query of a
	 * student name
	 */
	public int searchByFullName(String userInput) {
		Student student = getStudentByFullName(userInput);
		return student.getId();
	}

	/**
	 * DEPRECATED: NOT USED FOR WEB PAGE Returns the student's full name for a
	 * query of a student ID
	 */
	public String searchByID(int studentId) {
		Student student = getStudentByID(studentId);
		return student.getFullName();
	}

	/**
	 * DEPRECATED: NOT USED FOR WEB PAGE Calls findStudentByFullName(fullName)
	 * If is not found, print error message Else student full name is found,
	 * remove object and print message
	 */
	public void removeStudent(String fullName) {

		Student student = getStudentByFullName(fullName);

		// Student is not in array list
		if (student == null) {
			System.out.println("Error: Student is not enrolled at "
					+ getSchoolName());
		}
		// Student is in array list
		else {
			studentList.remove(student);
			System.out
					.println(fullName + " has been removed from the database");
		}

	}

}
