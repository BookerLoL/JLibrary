# Notes
## Regular Expressions
- Tool for text patterns
- Specify strings to extract/match information such as phone numbers, emails, names, etc
- Good to use to start off tackling an NLP problem, may end up not using it.

## Errors
- False positives (Type 1)
    - Matching Strings that shouldn't have matched
    - **Increase precision/accuracy** to minimize
- False negatives (Type 2) 
    - Not matching what should have matched
    - **Increase recall/coverage** to minimize

## Regulex Expression Notations
- You should probably do your own research as there are a ton of operators that can be used
    - Can test your regex with online regex tester
    - such as lookaheads, and etc

|Operator|Example|Matches|What's happening?|
|---|---|---|---|
| x | a | looks for a lowercase a | Looks to match a sequence of characters (ex: sequence of 1 character)  that is an a |
| xyz | abc | looks for lowercase abc | Looks for a sequence of characters that are: a then b then c |
| xY | aB | looks for lowercase a then uppercase B | Still a sequence of patterns but uppercase and lowercase matters |
| \| | hello\|bye\|ok | looks for  hello or bye or ok | uses alternation '\|' operator to say: I want this or that or that  | 
| ( ) | x(y\|z) | Looks for x then either y or z | capture group '(' ')' operators are useful to group regexes and distinguish what you mean. You can think of it like order of operations, do this sequence first |
| without () | xy\|z | looks for xy or a z | Without the grouping operator, altneration will take the left side as a regex and right side as a regex to look for | 
|[...] |[0123456789]|Any digit| Uses  character class  '[' and ']' operators to specific any digit doing: 1\|2\|3\|4\|5\|6\|7\|8\|9\|0 as the underlying logic|
|[...] |[Hh]ello|Hello, hello| Looks for H\|h then ello sequence|
| [low-high] |[A-Z][a-z][0-9]|Uppercase letter then lowercase letter then a digit| uses range '-' operator to specify A-Z, a-z, 0-9 are just shortcuts for A\|B...Z a\|b...z 0\|1...\|9|
| [^low-high] |^[A-Z]|Not an uppsercase letter| Uses negation/not '^' as first character in the brackets to distinguish not these letters|
| ? | honou?r | honor or honour | Uses  optional '?' operator say that: "you can have it but it's not needed" | 
| * | yo* | y, yo, yoo, yooo, yooo..... | uses kleene closure/star '*' operator, expects 0 or more of the previous regex | 
| * | (yo)* | , yo, yoyo, yoyoyo | 0 or more of the previous regex, since (yo) is a regex, it expects 0 or more yo |
| + | yo+ | yo, yoo, yooo... | Uses kleene plus '+' operator, expects 1 or more of the previous regex | 
| . | m.m | mum, m1m, mWm ... | Uses dot '.' operator, means any character can represent this dot | 
| . | .*er | er, 1er, Buyer ... | combination of regex operators, expects: 0 or more any characters then er sequence | 
| ^ | ^bye | bye | match-beginning-of-line '^' operator, expects the regex to the right to appear at the start of the string | 
| $ |  bye$ | bye | match-end-of-line '$' operator, expects teh regex to the left to appear at the end of the string | 
| {minLimit, maxLiimt} | [a-z]{0, 3} | , a, aa, aaa | Uses quantifier '{' '}' to find certain amount of appearances of the previous regex | 
| \b | \bis\b | is | using word boundary '\b' operator to boundary characters | 


## Typical Regular Expression Precedence Hierachy
- Highest to lowest
    - ()
    - *, ?, +, {} 
    - ^, &, sequences of letters
    - | 

## Terminology and Concepts
- greegy pattern
    - match longest string possible
- non-greedy patterns
    - match shortest string possible