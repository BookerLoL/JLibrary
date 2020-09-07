package scanner;

public class Tester {
	public static void main(String[] args) {
		String[] regexOption = { "a(b|c)*|who|what|where|abs|fee|fie", "t(b(c|d))*|a|c*", "ab|(ab)*c",
				"r(0|1|2|3|4|5|6|7|8|9)+", "abc|bc|ad", "a(b|c)*", "a(b|c)", "fee|fie", "(a)c?", "a?", "a*", "ab(c|d)+", "ab",
				"a" };
		System.out.println("Total options: " + regexOption.length);
		
		int optionIndex = 0;
		System.out.println("Using index: " + optionIndex + "\toption: " + regexOption[optionIndex]);
		NFAConverter nfaConv = new NFAConverter();
		State nfa = nfaConv.convert(regexOption[optionIndex]);
		DFAConverter dfaConv = new DFAConverter();
		State dfa = dfaConv.convert(nfa);
		MinDFAConverter1 minDfaConv1 = new MinDFAConverter1();
		State minDfa1 = minDfaConv1.convert(dfa);
		//System.out.println("\n\n\n NEXT PRINT \n\n\n");
		MinDFAConverter2 minDfaConv2 = new MinDFAConverter2();
		State minDfa2 = minDfaConv2.convert(nfa);
		Converter.print(minDfa2);
	}
}
