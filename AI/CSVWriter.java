import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
	private String filename;
	private String delimiter;
	private boolean overwrite;

	public CSVWriter(String filename) {
		this(filename, ",", true);
	}

	public CSVWriter(String filename, String delimiter) {
		this(filename, delimiter, true);
	}

	public CSVWriter(String filename, String delimiter, boolean overwrite) {
		this.delimiter = delimiter;
		this.filename = filename;
		this.overwrite = overwrite;
	}

	public void writeRows(Object[][] rows) {
		try (BufferedWriter bf = new BufferedWriter(new FileWriter(new File(filename), overwrite))) {
			for (Object[] row : rows) {
				bf.write(rowLine(row));
				bf.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeRows(List<Object[]> rows) {
		try (BufferedWriter bf = new BufferedWriter(new FileWriter(new File(filename), overwrite))) {
			for (Object[] row : rows) {
				bf.write(rowLine(row));
				bf.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeRow(Object... row) {
		try (BufferedWriter bf = new BufferedWriter(new FileWriter(new File(filename), true))) {
			bf.write(rowLine(row));
			bf.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String rowLine(Object[] row) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < row.length - 1; i++) {
			sb.append(row[i].toString());
			sb.append(delimiter);
		}
		sb.append(row[row.length - 1].toString());
		return sb.toString();
	}

	// Use this if you are going to overwrite an existing file for the first line
	public void writeNewLine() {
		try (BufferedWriter bf = new BufferedWriter(new FileWriter(new File(filename), overwrite))) {
			bf.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setDelimiter(String newDelim) {
		delimiter = newDelim;
	}

	public void setFile(String filename) {
		this.filename = filename;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	public static void main(String[] args) {
		CSVWriter csv = new CSVWriter("test2.txt");
		csv.writeRow("name", "age", "height");
		csv.writeRow("ethan", "21", "174");
	}
}
