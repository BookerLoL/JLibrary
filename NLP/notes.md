# NLP notes

## Todo
- Byte-Pair encoding

## Vocabaulary
- **Text normalization**
    - Converting text to a convenient and standard form
        - ex: all lowercase, trimmed leading and trailing white spaces, etc
    - General steps
        1) Tokenizing / Segmenting from running text
        2) normalize word format
        3) segmenting sentences in running text
- **Tokenizing**
    - Separating words from running text 
        - ex: "I like dogs" -> ["I", "like", "dogs"]
    - can't always separate using spaces
        - ex: I'm new. -> ["I", "am", "new"]
            - punctations, slang, and etc make it more difficult
        - ex: 我喜欢中文 -> how could I tokenize this?
            - different languages can increase the complexity of the problem
    - Penn Treebank Toeknization

- **Lemmatization** 
    - check if two words have same root, despite how it's spelled
        - Ex: "are", "is", "am", "were", "was"... -> the lemma would be: "be"
        - Ex: "cry", "cried", "crying" -> "cry" -> the lemma woudl be: "cry"
    - **Lemma**
        - set of lexical forms having the same stem
    - **Lemmatizer**
        - A tool that will map the words to their roots
            - Typically use morphological parsing
                - morphemes, smallest meaning units
                    - **stems**
                        - central morpheme, main meaning 
                    - **affixes**
                        - add additional meaning
                            - ex: prefixes and sufixes 

- **Stemming**
    - Similar to Lemmatization, but only removes suffixes at the end of the word
        - ex: "eating" -> "eat"
        - ex: "helping" -> "help"
- **Sentence/Text/Word segmentation**
    - whole text into indivdual sentences
        - paragraph -> sentences
    - book -> sentences

- **Regular Expressions** 
    - lanauge for text search strings
        - ex: Unix, Emacs
- **Corupus**
    - Collection of texts/speech
        - 1 or more documents
        - ex: Brown corpus by Brown University, Shakesspeare, Google N-grams, COCA
        - more examples: https://en.wikipedia.org/wiki/List_of_text_corpora
- **Word**
    - Might count punctations as words for certain applications

- **Wordform** 
    - Full inflected / derived form of the word

- **Word Types**
    - Number of distinct words in corpus
    - Vocabulary size: |V|
- **Word Tokens**
    - Total number of words
    - running words: N

- **Herdan's Law / Heap's Law**
    - | V | = kN*β
        - k is positive constant
        - β is 0 < β < 1
            - depends on corpus size and genre
- Dictionaries
    - provide **boldface forms** / **entries** as a rough upper bound of lemmas
        - ex: Merriam-Webster, Cambride, Oxford English, Collins English, etc 

- **Code Switching**
    - multiple languages in a single communicative act
        - ex: Tagalog often does this
            - Tumatanggáp ba kayó ng credit card?

- **Utterance**
    - spoken sentence
   
- **Disfluencies**
    - **Fragment**
        - broken-off word that isn't complete  
        - ex: st.. start 
    - **Fillers**
        - fill up pauses 
        - uh, um, like, etc

- **Clitic contactions** 
        - they're -> they are
            - 're is clitic, can't stand alone as a word
- Case Folding
    - change everything to a certain case
        - uppcase, lowercase
            - maybe keep exceptions

- Language Models
    - p (w|h)
        - probability of word given history of words
    - Chain rule of probability
        - k=1 -> n, product of P(W<sub>k</sub> \| W<sub>1</sub><sup>k-1</sup>)
    - bigram model
        - P(W<sub>n</sub>|w<sub>1</sub><sup>n-1</sup>) by using P(W<sub>n</sub>|W<sup>n-1</sup>)
    - n-gram model
        - P(W<sub>n<sub>|W<sub>1</sub><sup>n-1</sup>) == P(W<sub>n</sub>|W<sup>n-1</sup>)
            - unknown words
                - ignore or save probability for unknow words
                - Use smoothing methods
                    - add one & variations
                        - laplaces, lidstones, jeffeys-perks law
                    - witten bell 
                    - good-turing estimation
    - Markov assumption
        - probability of word depends on only on previous word
    - Maximum likelhood estimation / MLE
        - get counts from corpus then normalize count so value is between 0 and 1
            - normalized by unigram count
    - Relative frequency
        - observed frequency of particular sentence divided by observed frequency of a prefix
    - Language model probabilities
        - use log probabilities, avoids underflow
        - ex: p1 x p2 x p3 == exp(log(p1) + log(p2) + log(p3))
- Evaluating Language Models
    - Extrinisic evaluation
        - embed into application and measure improvement
        - very expensive if really big application
    - Intrinistic evaluation
        - measure quality independent of application
        - **Test set / Held out corpora**
            - uses unseen data
            - don't use this set to "train" 
        - **training set / training corpus**
            - large as possible
        - development test set / devset
            - initial test set
        - Distribution
            - typical: 80% training, 10% dev, 10% test
    - **Perplexity (PP)**
        - **minimizing perplexity == maximizing probability**
        - **perplexity is the average branching factor**
        - inverse probability of test set, normalized by number of words
            - need to include begin and ending sentence markers
                - include end marker for total count of words, not start
            - "weighted average branching factor" of a language
                - next digit probability (0..9) -> P = 1/10 -> PP = 10
        - constructucted without any knowledge of vocab of test set to compare
        - only use with same vocab 
    - Ensure training is similar genre to task
        - appropriate dialect too (Ex: Afrian American English vs Chinese English vs American English have different vocabulary)
        - Sparsity, 0's for values
    - Unknown words
        - closed vocabarly
            - can only contain certain words
        - out of vocabulary words (oov)
        - open vocabularly
            - accept unknown words: UNK
        1) Train unknown word
            - vocab -> convert oov to unknown (noramlization) -> estimate from it's count
        2) Vocab -> replace unknown words based on frequency -> after n amount, replace by frequency
    - Models
        - discriminative, parsing, caching
    - SMoothing techniques
        - kneser-ney discounting, backoff, good-turing backoff, interpolated kneser-ney, modified kneser-ney, stupid backoff
    - entropy
    - bloom filters
- Spelling 
    - Tasks
        - error detection
        - autocorrect, suggest correction / list
    - Types of errors
        - non-word: graffe -> giraffe
        - typographical: three -> there
        - cognitive: too -> two
    - Solutions to errors
        - non-word
            - not in dictionary is error
                - candidates using shotest weighted edit dist / highest noisy channel probability
        - other
            - candidate set using pronounciation/spelling, include word itself
                - noisy channel / classifier to pick
    - errors mostly within 2 edit distance
    - spelling error test sets
- Text classification
    - assigning cateogires/topics/genres/age/spam/language/sentiment analysis
    - Classification methods
        - hand-coded rules
            - building and maintaing rules are expensive
        - supervised machine learning
            - Naive bayes, logistic regression, support-vector machines, k-Nearest Neighbors
    - Naive Bayes 
        - gamma (document) = class
        - set of words and their counts (no order)
        - argmax P(c|d)  == argmax P (d | c) P(c)
            - d, document
            - c, class
                - could use relative frequencies to compute P(c)
        - bag of words, position doesn't matter
        - conditional independence, feature probabilities are independent of given class
            - p(x1..xn | c) == P(x1 | c) * P(x2 | c) ...
        - MLE
            - P(cj) = doccount(C = cj) / N doc
            - P(wi | cj) = count(wi, cj) / sum count(w, cj)
                - create a mega document
            - use add-1 smoothing
                - count(wi, c) + 1 / sum count(w, cj) + |V|
                    - count + 1 / total tokens + vocab size
            - P(cj) = |docs j | / |total docs|
        - kind of like a unigram model for each class
        - optimal if independence assumption held
        - good dependable baseline for text classification

- 2 by 2 contingency table
    - selected & in class -> true positive (tp)
    - selected & not in class -> false positive (fp)
    - not selected & in class -> false negative (fn)
    - not selected & not in class -> false positive (fp)
    - Accuracy = (tp + tn) / (tp + fp + fn + fp)
        - not always good to use, espcially if uncommon 
    - Precision = (tp) / (tp + fp)
    - Recall = (tp) / (tp + fn)
    - F measure: conservative average
        - 1 / a(1/P) + (1-a)(1/R)
            - balanced f1 = a = 1/2
                - F = 2PR / (P+R)
    - micro-vs macro averaging
        - macro: average of  tp/(tp +fn) + ....
        - micro: all tp + (all tp + all fn)
    - Confusion matrix
## NLP Tasks
- Named Entity Detection
    - detect names, dates, and etc

## Resources
- Speech and Language Processing An Introduction to Natural Language Processing, Computational Linguistics, Speech Recognition
    - Daniel Jurafsky, James H. Martin