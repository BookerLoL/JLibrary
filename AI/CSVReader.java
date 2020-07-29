import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {
	private String[] header;
	private List<Object[]> data;

	public CSVReader(String filename, String delimiter) {
		this(filename, delimiter, false);
	}

	public CSVReader(String filename, boolean hasHeader) {
		this(filename, ",", hasHeader);
	}

	public CSVReader(String filename, String delimiter, boolean hasHeader) {
		data = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
			if (hasHeader) {
				header = br.readLine().split(delimiter);
			}

			String line = null;
			while ((line = br.readLine()) != null) {
				data.add(line.split(delimiter));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean hasHeader() {
		return header != null;
	}

	public Object[] getHeaderArray() {
		return header;
	}

	public String getHeaderString() {
		return Arrays.toString(header);
	}

	public int numberOfRows() {
		return data.size();
	}
	
	public Object getData(int row, int col) {
		return data.get(row)[col];
	}

	public Object[] getDataRow(int row) {
		return data.get(row);
	}

	public Object[][] getDataArray() {
		Object[][] dataAry = new Object[data.size()][];
		for (int i = 0; i < data.size(); i++) {
			dataAry[i] = data.get(i);
		}
		return dataAry;
	}

	public String getDataString() {
		return getDataString(false);
	}

	public String getDataString(boolean includeHeader) {
		StringBuilder sb = new StringBuilder();
		if (includeHeader && hasHeader()) {
			sb.append(Arrays.toString(header) + "\n");
		}
		data.forEach(row -> sb.append(Arrays.toString(row) + "\n"));
		return sb.toString();
	}

	public String toString() {
		return getDataString(true);
	}
}
