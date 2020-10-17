package main.util;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;

/**
 * A helper class to speed up serialziation process.
 * 
 * @author Ethan
 *
 */
public class Serializer {
	public static <T extends Serializable> void saveItem(T data, String path) throws Exception {
		try (ObjectOutputStream ois = new ObjectOutputStream(Files.newOutputStream(Paths.get(path)))) {
			ois.writeObject(data);
		}
	}

	public static <T extends Serializable> void saveItem(List<T> data, String path) throws Exception {
		try (ObjectOutputStream ois = new ObjectOutputStream(Files.newOutputStream(Paths.get(path)))) {
			ois.writeObject(data);
		}
	}

	/**
	 * Loads the item that you saved. Make sure that they are the same type.
	 * 
	 * @param <T>
	 * @param path
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T loadItem(String path) throws Exception {
		try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(path)))) {
			return (T) ois.readObject();
		}
	}
}
