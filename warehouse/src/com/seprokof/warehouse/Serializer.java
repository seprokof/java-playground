package com.seprokof.warehouse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Performs write and read operations with the serialized files.
 * 
 * @author seprokof
 *
 */
public final class Serializer {

	/**
	 * Assemblies the object back from the serialized representation.
	 * 
	 * @param fullPathFile
	 *            fully qualified path to serialized file
	 * @param nestedType
	 *            the type of the objects which will be restores
	 * @return the collection of restored objects
	 * @throws IOException
	 *             will be thrown if any errors occurred during file access
	 */
	public static <T> List<T> deserialize(String fullPathFile, Class<T> nestedType) throws IOException {
		try (ObjectInputStream ois = new ObjectInputStream(
				Files.newInputStream(Paths.get(fullPathFile), StandardOpenOption.READ))) {
			return (List<T>) (List<?>) ois.readObject();
		} catch (ClassNotFoundException e) {
			// should not happens
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Writes object to the given file.
	 * 
	 * @param fullPathFile
	 *            fully qualified path to serialized file
	 * @param toSerialize
	 *            the list of objects to serialize
	 * @throws IOException
	 *             will be thrown if any errors occurred during file access
	 */
	public static void serialize(String fullPathFile, List<?> toSerialize) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fullPathFile),
				StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
			oos.writeObject(toSerialize);
		}
	}

}
