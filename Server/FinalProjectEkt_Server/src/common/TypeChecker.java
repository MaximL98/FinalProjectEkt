package common;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class: InputValidator
 * Contains a static method that validates, for any list of objects (stored as Object[]), that the elements inside are of some desired types.
 * @author Rotem
 *
 */
public class TypeChecker {
	/**
	 * This static method compares classes between an array[Object] and an arrayList of classes, in a given range of indices.
	 * We get the class for each object and compare it with our expected type (stored, for objects[i], in paramTypes.get(i))
	 * @param objects - the array of objects we want to validate
	 * @param paramTypes - the types for the segment of objects we are interested in (segment inside objects)
	 * @param startIdx - the first index of objects to compare with the 0th index of paramTypes
	 * @return true if all objects in range are of the desired types, else false
	 */
	public static boolean validate(Object[] objects, ArrayList<Class<?>> paramTypes, int startIdx) {
		if(objects.length < (startIdx + paramTypes.size()))
			throw new IllegalArgumentException(
					"Invalid range of parameters for validateInput {"
							+ Arrays.toString(objects) + ", "
							+ Arrays.toString(paramTypes.toArray())+", "+ startIdx + "}");

		for(int j=0;j<paramTypes.size();j++) {
			if(!objects[j + startIdx].getClass().equals(paramTypes.get(j))) {
				//System.out.println(objects[j + startIdx].getClass() + ", " + paramTypes.get(j));
				return false;
			}
		}
		return true;
	}
	
	/*
	 * An example for testing this one (commented out)
	 */
	
	/*
	public static void main(String[] args) {
		System.out.println("Testing my shit");
		Object[] params = new Object[]{new Integer(1), new String("ass"), new Object[] {"ass", "tits"}};
		ArrayList<Class<?>> types = new ArrayList<Class<?>>();
		types.add(String.class);
		types.add(Object[].class);
		System.out.println(Arrays.toString(params));
		System.out.println("Calling validate!");
		boolean b = validate(params, types, 1);
		System.out.println("Got result " + b + " from validate");
	}
	*/
}
