package database;

import java.util.HashSet;
import java.util.Set;

/**
 * Explore different collections (Hash Set, HashMap, LinkedList)
 * 
 * @author lsong
 *
 */
//equals and hashcode in Object. these need to be symmetric. if it equals in "equal" then it equals in hashcode. but not opposite e.g. longs repeat hashcode
//if two objects equal and have same hashcode, then won't add to Set.
//Need to define equals and hashcode to add student to hashset or hashmap key
//99% arraylist, hashmap, hashset (rarely linkedlist). concurrent hashmap.
//don't use vector (concurrent version of array list. slow)
//Hashtable and hashmap same thing. don't use hashtable.
public class PlayWithCollections {

	public static void main(String[] args) {
		new PlayWithCollections().doMain();
	}

	private void doMain() {

		// Pre-create students to use in collections
		Student s1 = new Student("One", "Student", 5);
		Student s2 = new Student("Two", "Duplicate", 6);
		Student s3 = new Student("Two", "Duplicate", 7);
		Student s4 = new Student("Four", "SameLast", 10);
		Student s5 = new Student("Five", "SameLast", 3);
		Student s6 = new Student("Six", "Hello", 12);
		Student s7 = new Student("Seven", "Great", 12);
		Student s8 = new Student("Eight", "Bye", 9);
		Student s9 = new Student("Nine", "Java", 6);

		Student elements[] = { s1, s2, s3, s4, s5, s6, s7, s8, s9 };
		
		findDuplicateLastNames(elements);
		
	}
	
	/**
	 * Finds duplicate last names from an array of students
	 */
	private void findDuplicateLastNames(Student elements[]){
		
		Set<String> duplicateLastSet = new HashSet<String>();
		
		for (Student s : elements) {
			
			//Try to add student last name
			boolean added = addLastNameToSet(duplicateLastSet, s);
			
			//Check if student last name was added
			if (added == false) {
				System.out
						.println("Error: "
								+ s.toString()
								+ " was unsuccessfully added due to duplicate last name.");
			}
		}
	}

	/**
	 * Add Student last names into a HashSet<String>
	 */
	private boolean addLastNameToSet(Set<String> set, Student s) {
		return set.add(s.getLastName());
	}

}
