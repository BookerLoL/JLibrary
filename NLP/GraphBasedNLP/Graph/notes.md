# Notes on Graphs

## Basic Graph Definition
- Graph
    - set of vertices connected by set of edges
    - directed and undirected
        - directed / digraph
            - edges, traverse only one way
        - Undirected
            - edges, traverse both ways
    - Completed
        - every two vertices are adjacent to another
    - Connected
        - two vertices connected by a path
    - Subgraph, subset of another graph components
    - Unweighted by default
- Edges 
    - tail vertex (from)
    - head vertex (to)
    
- Vertices
    - adjacent
        - two vertices connected by an edge
    - degree
        - total number of edges incident to that edge
            - in-degree
                - edges that has vertex as head
            - outdegree
                - edges that has vertex as tail
- Average degree 
    - (1/total vertices) * sum [each vertex] of Degrees(vertex)
- Path
    - sequence of verices using edges to transition
    - simple path:  no repeating vertices
    - cycle:  first and last vertex coincide
    - shortest paths
         Dijkstra / Floyd-Warshall
    -  Longest shortest path
        - diameter
- Connected Component
    - connected subgraph, no vertex can be added
- Clique
    - subgraph complete
    - k-clique, k vertices
- clustering coefficient
- degree distribution
- Graph Types   
    - bipartite: vetices --> disjoint subsets & edges connect verticies in diff subsets
    - random: edges randomly generated
        - Erdos-Renyi graph model, given probability of p
    - regular: all vertices have same degree
    - scale-free: degree distribution using a power law 
    - small-world: avg path between every two verticies is small
- Graph Representation
    - matrix
        - fast computation but grows rapdily
        - good when small, typically has sparseness
    - adjacency lists
        - 

- bibliographic  coupling: every vertex that has edge to vertex1 has edge to vertex2 as well
- co-citation: vertex1 and vertex2 every edge to the same nodes
- Eigenvectors
    - Eigenvalues
## Graph-Based Algorithms
-depth-first, breadth-first
- minimum-spanning-tree
    - minimum weight
    - properties
        1) can have multiple, all have same total weight
        2) edges = total nodes - 1
    - algorithms
        - prims algorithm, undirected and connected
        - kruskals algorithm, undirected and connected
- Directed Acyclic graph sorting
    - kahns algorithm 
- Shortest Path
    - Dijkstras algorithm
    - FLoyd-warshall, algorithm 
- Cut, partition of a grpah into two disjoin subsets
    - minimum-cut and max flow algorithm
        - ford fulkerson, edmonds-karp, capacity scaling, dinics algorithm, psuh relabel
- graph matching
    - isomorphic, NP problem
        - iterative partitioning of vertices (based on degree?)
        - graph edit distance
- Dimensionality reduction
    - Latent semantic analyiss / Latent sementic Indexing
        - A =  USV^T
        - eigen
- Stochastic processes on graphs
    - random-walks methods
    - Diestel (05), Gross and Yellen (05), Cook and Holder (06)

## Networks
- interchangable with graphs
- types
    - lattices
    - random graphs
- Goals
    - predict extrinistic behavior of network 
        - based on measurable properties
- ER model
    - diameters small, no clustering, degree distributions dont match real-life networks
    - total average edges
        - p(k) = pn(n-1)/2
        - large n, p(k) = (lambda^k * e^-lambda) / k!
            - Poisson distribution
- Power law networks
    - scale invariant / no typical degree
    - ex:  p(x) = C x^-a
- Zipf's Law
    - relation between frequency based rank of word and it's frequency
        - inversely correlated
            - most frequent = f
            - second-most freq = f/2
    - uses
        - word freq
        - number of senses per word
        - freq of visits to youtube videos
- Preferential attachment
    - Barabasi-Albert model (temporal)
        - rich get richer
            - start with m0 nodes, links are arbitrary, each node has 1 link
                - growth: add new node to m nodes in network
                    - m <= m0
                - preferntial attachment: depends on degree of node

## Graph-Based Information Retrieval
- 