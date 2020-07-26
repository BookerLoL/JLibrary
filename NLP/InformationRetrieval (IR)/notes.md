# Notes
- user needs info, uses search statement, translate search statement & get info
    - text, images, audio, videos
- Information Retrieval System: helps user in finding info 
    - 4 major subsystems
        - Ingesting
        - Indexing
        - Searching
        - Displaying
## Problems
- user input vs author language
    - vocabulary issues -> need shared vocabulary
        - user often lacks vocab
- polesemy: word with multiple meanings
- acroynms
- multimedia adds compelxity
- what to display
    - false hits
    - useless info
- redudancy in sources

## Objectives
- minimize time to locate user requested info
- usually precision is better than recalling items
- precision across the first 20-50 hits


# Definitions
- Precision = # relevant retrieved / # total retrieved
    - total precision used comparing search systems
- Recall = # relevant retrieved / # total possible relevant

## Ingesting
- SDI
    - compare new items against stored statements of interests of users & deliver
    - item -> check user profiles -> if satisy, add to mail
    - automatic generated profile better than human profiles
- Alerts
    - alert profiles == search statements + metadata
        - after n alerts, alert notification sent out
- Items and Item index
    - retrospective search: already been processed
    - multimedia item dbs moved to hierarchal storage mgmt system
- Indexing and Mapping
    - citational metadata: source, author, etc
    - semanntic metadata: human generated
    - public and private indexes
    - Using Taxonomy: hierarchical ordering of set controled vocab terms to describe concepts
- Search
    - map user info & items in db
    - relationships
        - boolean, natural lang, proximity, contiguous word phrases, fuzzy searches
    - interpretation
        - term masking, numeric, and date rage, contiguous word phrases, concept/thesaurus expansion
    - Examples
         - boolean logic
                - AND, OR, NOT == set intersection, set union, set difference
            - Proximity
                - distance alloweed within an item between two search terms
                    - ex:
                        - "Word1" ADJ "Word2" -> "Word1 Word2"
                        - "Word1" within X words of "Word2" 
                        - "WOrd1" within X paragraphs of "Word2"
            - Continguous Word Phrase: query term and search operator
                - 2+ words as single unit: "United State of America" -> as 1 unit
            - Fuzzy Searchers: locate spellings of words similar to search term
                - incr recall for decrease precision
                - ex: "computer" -> "computer", "compiter", "conputer", etc
                    - check if valid word with diff meaning, include if with low ranking or don't include it
            - Term masking: expand query by masking portion of term
                - better if no stemming or very simple stemming algoriithm
                - fixed-length not used often
                - variable-length
                    - ex: *computer* -> minicomputer, microcomputer
                    - ex: comput* -> computers, computes, computing
                    - *Word -> suffix search
                    - Word* -> Prefix search (80-90% searches)
                    - *Word* -> Imbedded String Search
            - Numeric and Date Ranges
                - ex: 150-300, 1/2/2000-8/5/2009, >23, <=101
            - Vocab Browse
                - provide list of words and show number of records term was found in 
  