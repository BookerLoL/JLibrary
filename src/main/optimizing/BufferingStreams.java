package main.optimizing;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * This class is designed to show you an example of how to optimize code in
 * general by buffering streams.
 * 
 * <p>
 * It's not always clear when to use a buffered streams but when writing out bytes it's generally 
 * better to buffer the byte streams.
 * 
 * 
 * @author Ethan
 * @version 1.0
 */
public class BufferingStreams {
	
	public static void slowStreams() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.close();
	}

	public static void fastStreams() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(baos);
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.close();
	}
}
