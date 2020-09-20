package main.nlp.stemmers;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Tester {
	private static class TestingGroup {
		String vocabFile;
		String stemFile;
		boolean specialCaseFile;
		Function<String, String>[] functions;
		Stemmer stemmer;

		@SuppressWarnings("unchecked")
		public TestingGroup(String vocabFile, String stemFile, Stemmer stemmer) {
			this(stemFile, vocabFile, stemmer, false);
		}

		public TestingGroup(String stemFile, String vocabFile, Stemmer stemmer, boolean isSpecialCaseFile,
				Function<String, String>... functions) {
			this.vocabFile = vocabFile;
			this.stemFile = stemFile;
			this.stemmer = stemmer;
			specialCaseFile = isSpecialCaseFile;
			this.functions = functions;
		}

		public String getVocabFile() {
			return vocabFile;
		}
		
		public String getStemFile() {
			return stemFile;
		}

		public boolean isSpecialCaseFile() {
			return specialCaseFile;
		}

		public Function<String, String>[] getFunctions() {
			return functions;
		}

		public Stemmer getStemmer() {
			return stemmer;
		}
	}
	
	private static PrintStream out;
	public static void main(String[] args) throws UnsupportedEncodingException {
		out = new PrintStream(System.out, true, "UTF-8"); //ensure this line remains

		TestingGroup test = getLovinsStemmer();
		//testStemming(test.getVocabFile(), test.getStemFile(), test.getStemmer(), true);
		//test("losing", "los", test.getStemmer());
		testAllStemmers();
	}

	private static void testAllStemmers() {
		TestingGroup[] testGroup = { getCatalanSnowballStemmer(), getDutchSnowballStemmer(), getEnglishPorterStemmer(),
				getEnglishSnowballStemmer(), getFrenchSnowballStemmer(), getGermanSnowballStemmer(),
				getItalianSnowballStemmer(), getLovinsStemmer(), getPaiceHuskStemmer(), getPortugueseSnowballStemmer(), getRomanianSnowballStemmer(),
				getSpanishSnowballStemmer(), getSwedishSnowballStemmer() };

		for (TestingGroup test : testGroup) {
			testStemming(test.getVocabFile(), test.getStemFile(), test.getStemmer(), false);
		}
	}

	public static TestingGroup getCatalanSnowballStemmer() {
		return new TestingGroup("snowballCaVocab.txt", "snowballCaStem.txt", new CatalanSnowballStemmer());
	}

	public static TestingGroup getDutchSnowballStemmer() {
		return new TestingGroup("snowballNlVocab.txt", "snowballNlStem.txt",  new DutchSnowballStemmer());
	}

	public static TestingGroup getEnglishPorterStemmer() {
		return new TestingGroup("porter1EnVocab.txt", "porter1EnStem.txt",  new EnglishPorterStemmer());
	}

	public static TestingGroup getEnglishSnowballStemmer() {
		return new TestingGroup("snowballEnVocab.txt", "snowballEnStem.txt",  new EnglishSnowballStemmer());
	}

	public static TestingGroup getFrenchSnowballStemmer() {
		return new TestingGroup("snowballFrVocab.txt", "snowballFrStem.txt",  new FrenchSnowballStemmer());
	}

	public static TestingGroup getGermanSnowballStemmer() {
		return new TestingGroup( "snowballDeVocab.txt", "snowballDeStem.txt", new GermanSnowballStemmer());
	}

	public static TestingGroup getItalianSnowballStemmer() {
		return new TestingGroup( "snowballItVocab.txt", "snowballItStem.txt", new ItalianSnowballStemmer());
	}

	@SuppressWarnings("unchecked")
	public static TestingGroup getLatinSnowballStemmer() {
		LatinSchinkeStemmer lst = new LatinSchinkeStemmer();
		Function<String, String> nounFunc = word -> lst.getNounForm(word);
		Function<String, String> verbFunc = word -> lst.getVerbForm(word);
		return new TestingGroup("latin_word_noun_verb.txt", "", lst, true, nounFunc, verbFunc);
	}
	
	public static TestingGroup getLovinsStemmer() {
		return new TestingGroup("lovinsVocab.txt", "lovinsStem.txt", new EnglishLovinsStemmer());
	}

	public static TestingGroup getPaiceHuskStemmer() {
		return new TestingGroup( "paiceHuskVocab.txt", "paiceHuskStem.txt", new PaiceHuskStemmer());
	}

	public static TestingGroup getPortugueseSnowballStemmer() {
		return new TestingGroup( "snowballPtVocab.txt", "snowballPtStem.txt", new PortugueseSnowballStemmer());
	}

	public static TestingGroup getRomanianSnowballStemmer() {
		return new TestingGroup( "snowballRoVocab.txt", "snowballRoStem.txt", new RomanianSnowballStemmer());
	}

	public static TestingGroup getSpanishSnowballStemmer() {
		return new TestingGroup( "snowballEsVocab.txt", "snowballEsStem.txt", new SpanishSnowballStemmer());
	}

	public static TestingGroup getSwedishSnowballStemmer() {
		return new TestingGroup( "snowballSvVocab.txt", "snowballSvStem.txt", new SwedishSnowballStemmer());
	}
	
	private static void testStemming(TestingGroup test) {
		List<List<String>> testResults = new ArrayList<>(test.getFunctions().length);
		List<List<String>> expectedResults = new ArrayList<>(test.getFunctions().length);
		String line = "";
		
		try (BufferedReader br = new BufferedReader(new FileReader(test.getVocabFile()))) {
			while ((line = br.readLine()) != null) {
				String[] inputs = line.split("\\s+");
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void testStemming(String testFile, String expectedFile, Stemmer stemmer, boolean shouldStem, boolean shouldPrint) {
		List<String> testResults = new LinkedList<>();
		List<String> expectedResults = new LinkedList<>();
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
			while ((line = br.readLine()) != null) {
				if (shouldStem) {
					testResults.add(stemmer.stem(line));
				} else {
					testResults.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (BufferedReader br = new BufferedReader(new FileReader(expectedFile))) {
			while ((line = br.readLine()) != null) {
				expectedResults.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int wrong = 0;
		
		for (int i = 0; i < expectedResults.size(); i++) {
			String testWord = testResults.get(i);
			String expectedWord = expectedResults.get(i);
			if (!testWord.equals(expectedWord)) {
				wrong++;
				if (shouldPrint) {
					out.println("Line " + (i + 1) + " got: " + testWord + " " + testWord.length() + " but expected: "
							+ expectedWord + " " + expectedWord.length());
				}
			}
		}
		System.out.println("There were " + wrong + " incorrect");
	}

	public static void test(String word, String expected, Stemmer stemmer) {
		String stemmedWord = stemmer.stem(word);
		if (stemmedWord.equals(expected)) {
			out.println("Correct stem");
		} else {
			out.println("Got: " + stemmedWord + " but expected: " + expected);
		}
	}

	public static void testStemming(String testFile, String expectedFile, Stemmer stemmer, boolean shouldPrint) {
		testStemming(testFile, expectedFile, stemmer, true, shouldPrint);
	}

	public static void compareFiles(String testFile, String expectedFile) {
		testStemming(testFile, expectedFile, null, false, true);
	}

	public static void writeToFile(String readFile, String outFile, Stemmer stemmer) {
		List<String> testResults = new LinkedList<>();
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(readFile))) {
			while ((line = br.readLine()) != null) {
				testResults.add(stemmer.stem(line) + "\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (BufferedWriter br = new BufferedWriter(new FileWriter(outFile))) {
			for (String word : testResults) {
				br.write(word);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
