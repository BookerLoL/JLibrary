package main.nlp.phonetics;

public abstract class Phonetizer {
	public abstract String encode(String name);
	
	public static void main(String[] args) {
		Phonetizer p = new ModifiedNysiis();
		String[] names = "SADFKJSALCKJSCSCSHSCHSWJOQPEQOIREUPQR".split(", ");
		//String[] names = {"Dougal"};
		for (String name : names) {
			System.out.println(p.encode(name));
		}
	}
}
