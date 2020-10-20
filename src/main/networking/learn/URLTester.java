package main.networking.learn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

//https://docs.oracle.com/javase/tutorial/networking/urls/_setProxy.html
public class URLTester {
	private static class BasicURl {
		/*
		 * Example of how URL's can be constructed.
		 */
		public static void test() throws MalformedURLException {
			URL url = new URL("https://www.cnn.com/");
			URL relativeURL = new URL("https://store.google.com/category/");
			URL laptopTablesURL = new URL(relativeURL, "latops_tables");
			URL fullURL = new URL("http", "google.com", 80, "category/laptops_tables.html"); // http://google.com:80/category/laptops_tables.html
		}

		/*
		 * If URL has special characters, either transform the characters to their valid
		 * form or use URI.toURL() method
		 */
		public static void handleSpecialSpaces() throws URISyntaxException, MalformedURLException {
			URI uri = new URI("http", "www.google.com", "/hello wolrd/", "");
			URL handleSpecialCharacters = uri.toURL();
		}
	}

	private static class URLReadingAndWriting {
		public static void readDirectlyFrom(URL url) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
				String input;
				while ((input = br.readLine()) != null) {
					System.out.println(input);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public static URLConnection connect(URL url) throws IOException {
			return url.openConnection();
		}

		public static void close(URLConnection urlConnection) {
			// If url, can convert to HTTPConnection and disconnect()
			// else, simply close the stream
		}

		public static void read(URLConnection connection) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String input;
				while ((input = br.readLine()) != null) {
					System.out.println(input);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public static void write(URLConnection connection, String prefix, String input) throws IOException {
			connection.setDoOutput(true); // allow to write
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.write(prefix + input);
			out.close();

			read(connection); // read response
		}
	}

	public static void main(String[] args) throws IOException {
		URL url = new URL("https://www.cnn.com/");
		URLReadingAndWriting.read(URLReadingAndWriting.connect(url));
	}

}
