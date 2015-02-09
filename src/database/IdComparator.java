package database;

import java.util.Comparator;

public class IdComparator implements Comparator<Student>{

	@Override
	public int compare(Student student1, Student student2){
		
		return (student1.getID() < student2.getID()) ?-1: (student1.getID() > student2.getID()) ? 1:0 ;
	}
}