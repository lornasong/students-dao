package database;

import java.util.Comparator;

public class IdComparator implements Comparator<Student>{

	@Override
	public int compare(Student student1, Student student2){
		
		return (student1.getId() < student2.getId()) ?-1: (student1.getId() > student2.getId()) ? 1:0 ;
	}
}