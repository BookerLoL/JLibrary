# Collection of Fully Functional Stemmers

# What are Stemmers?

Stemmers will take a word as input and reduce it's stem/root word form. <br>
In otherwords, the word will be transformed into a smaller form of the word.
<br>It's important to note that stemmers are based on rules and they are not perfect.

# Setup

- **Important** ensure that the project handles UTF-8 BEFORE USING THE FILES otherwise encodings will get messed up due to changing character encodings.
  - **Eclipse**
    - Click on Project -> Properties -> Resource -> "Text File Encoding" to Other (UTF-8) -> Apply and Close
  - **VSCode**
    - Ctrl + , -> Type "autoguessencoding" -> Select on autoguessencoding
      - can also change in the settings.json file -> "files.autoGuessEncoding":true
    - Ctrol + , -> Type "encoding" -> Files:encoding -> select "utf8"

# Currently (fully functional)

- **Porter Stemmer** (English/En-us and old English)
- **Porter2 Stemmer** / **English Stemmer** (English/En-us, old English)
- **Lovins Stemmer** (English/En-us)
- **Paice Husk Stemmer** / **Lancaster Stemmer** (English/En-us)
- **UEA Stemmer** (English/En-us)
- **S Stemmer** / **Harman Stemmer** (English/En-us)
- **French Stemmer** (French/Fr)
- **Spanish Stemmer** (Spanish/Es)
- **Schinke Stemmer** (Latin)
- **Swedish Stemmer** (Swedish/Sv)
- **German Stemmer** (German/De)
- **CISTEM Stemmer** (German/De)
- **Catalan Stemmer** (Catalan/Ca)
- **Portuguese Stemmer** (Portuguese/Pt)
- **Italian Stemmer** (Italian/La)
- **Romanian Stemmer** (Romanian/Ro)
- **Dutch Stemmer** (Dutch/Nl)

# How to use a stemmer

```java
Stemmer stemmer = new PorterStemmer();
System.out.println(stemmer.stem("temptations")); //temptat
```

# Special Case Stemmers

- Schinke Stemmer (Latin)

```java
  LatinSchinkeStemmer stemmer = new LatinSchinkeStemmer();
  String toStemWord = "portis";
  stemmer.getNounForm(toStemWord); //port
  stemmer.getVerbForm(toStemWord); //por
```

- CISTEM Stemmer (German/De)

```java
//Check specifications for explained details
GermanCISTEMStemmer stemmer1 = new GermanCISTEMStemmer(); //case insensitive = false
GermanCISTEMStemmer stemmer2 = new GermanCISTEMStemmer(true); //case insensitive = true
```

# Notes

- All stemmers will lowercase the word and will expect the word to have already been trimmed and non-null.
- The stemmers can easily be modified to add stopword list/lexical look ups/exceptions
- Language names will use ISO 639-1 codes
  - https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes

# Todo

- ## Currently

  - **The tester file will be moved to the tests later**
    - Will be modified later

- ## Add more languages later

- ## Add more different stemmers later
  - Context sensitive, corpus-based, YASS, HMM, N-gram, Dawsons, Xerox, Krovetz

# How can you help?

- If you can find the algorithm specifications for **Krovetz**, **Xerox**, **Dawsons** stemmers that would be most appreciated as I can't seem to find them.

# Copyright

The source code was written by me, however the algorithmic idea was produced by other people and you can find that the source link is provded in the file header documentation.
