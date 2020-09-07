package scanner;

//An example of what a direct coded scanner would do
public class DirectCodedScanner {
	public void nextWord(String word) {
		//Basically just use labels and simulate the states
		
		//ex
		/*
		 * s0: 
		 * 		{
		 * 			nextChar();
		 * 			lexme += char;
		 * 			if state.isAccepting() 
		 * 				clear stack
		 *			if (char == 'r)
		 *				goto s1
		 *			else goto sout
 		 * 		}
 		 * 
 		 * sout:
 		 * 		while(!state.isAccepting() && state != bad) 
 		 * 			...
		 */
	}
}
