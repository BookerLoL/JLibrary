# Scanner Concepts
- Regex that can be handled: grouping ( (...) ) ,  kleene closure ( * ), kleene plus ( + ), optional ( ?) , alternation ( | ), concatenation ( ab )
- NFA, DFA, Minimized DFA using dynamic graph
    - Minimized DFA: Hopcroft’s Algorithm, Brzozowski’s Algorithm
- Shows examples of how using Minimized DFA as a recognizer, direct coded scanner

# Todo list
- 9/7/2020, Want to add An Incremental Algorithm (W) for creating a Minimized DFA, want to add [] regex option
    - https://www.cs.swan.ac.uk/cie08/cie2008-local.pdf#page=13 
    - http://www.numdam.org/article/ITA_2014__48_2_173_0.pdf

# Updates
- 9/7/2020, refactored for readability, added a factories, utility, and tester class; updated package name to **scanner**
