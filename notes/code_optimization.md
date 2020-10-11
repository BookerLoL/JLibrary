# Code Optimization
- **Java optimization is a bit different from other languages since the JVM does optimize a lot of things for us behind the scenes.** 
- If you are just learning to code, I highly advise you to not worry about optimizing until you are truly comfortable with the langauge. 
- Some optimization techniques may improve performance but increases memory consumption greatly, so be weary of your constraints that you are allowed to work in.

## What to think about when wanting to optimize
1) Write clean, concise, and short code that is understandable
    - Unless performance is an issue, this should be the number 1 priority
2) Use profilers to check the performance of your code to find bottlenecks
    - Don't optimize code unless you need to
3) Look at the bottleneck areas (Always look at the largest bottlenecks first)
    - Often times an **algorithm is too slow**
        - Algorithms are most likely the number 1 reason as to why your code is slow.
   - **Improper data structures** being used 
        - Certain data structures will simply perform better for the task or have better practical performance.
        - You should understand the theoretical performance and the pros and cons of using a specific data structure. 
    - **Code is too abstracted**
        - You don't have to make everything an object then access the contents through methods. 
        - That's why design patterns and simplifying design can improve the performance.
    - **Poorly selected/use of functions**
        - Some functions are very flexible but at the cost of increased performance or they do extra work that you don't need. 
    - **Redoing/doing uncessary work**
        - These 2 rules are very good to follow
            - never do uncessary work
            - don't redo work that you've already done.
    - Other things
        - avoid using sychonized/lock methods
        - avoid exceptions for control flow
        - reuse objects
            - creating objects is expensive
        - Language related stuff
            - StringBuilder vs Concatenating (when to use which one)
            - garbage collector
        - other stuff
4) Look at the data types you are working with, data types have faster operations than others
5) Check possible compilation settings
6) Try to fix up the issues
7) Try different approaches that are within reason and see which works better in practice. 

## Optimization Technqiues
- **String Concatenation**
    - When using a for loop concatenating, use a StringBuilder 
        - If you know the max size, allocate enough memory right away to avoid dynamic reallocation
    - Non-loop concatenating, it's fine to use the + operator.
        - JVM should convert it to stringbuilder implementation
        - Ex: 

            ```java
                return  "Hello " + " World";
            ```
        - This approach has benchmarks where it's faster
- **Use primitives**
    - When possible, use primitives over the wrapper classes to avoid autoboxing and reduces memory
    - Avoid using BigInteger and BigDecimal unless you have to use them.
        - Slow calculations and more memory.
- **Caching**
    - Cache expensive operations if the benefit outweighs the overhead of caching
        - Ex: valueOf, DB connections, memoization, 
- **Iterative over Recursive**
    - Iterative is always faster than recursive but can add a lot of complexity to the code.
- **Packages**
    - Don't use wildcard, import the classes directly
- **Local Variables**
    - Use local variables over global variables
- **Switch Case**
    - More optimized than if statements
    - Java 8, switch handles string
- **Accessing Variables**
    - Directly access the variable rather than get/set accessors
- **Nulling Variables**
    - Nulling variables when don't need, helps with GC
- **Code Motion**
    - Move invariant values inside a loop outside the loop.
- **Unrolling Loops**
    - Decrease iterations in loops.
    - perform multiple actions in one iteration
    - Ex:
        ```java
        //arrow is gauranteed to be even
        for (int i = 0; i < ary.length; i += 2) {
            ary[i] = someValue;
            ary[i + 1] = someValue;
        }
        ```

## Examples of The Techniques
- You can find examples of these techniques in the /src/main/optimization directory.
- Though you can't see the performance right away, if you profile it over and over you will see there is an improvement in performance.

## Resources
These are some resources that can help you understand how to optimize Java code better.
- https://docs.oracle.com/cd/E13150_01/jrockit_jvm/jrockit/geninfo/diagnos/underst_jit.html
- https://www.infoworld.com/article/2077647/make-java-fast--optimize-.html
- https://docs.oracle.com/javame/8.1/me-dev-guide/optimization.htm
- https://stackify.com/java-performance-tuning/
- https://docs.oracle.com/javame/8.1/me-dev-guide/optimization.htm  
- https://docs.oracle.com/cd/E13150_01/jrockit_jvm/jrockit/geninfo/diagnos/underst_jit.html
- https://docs.oracle.com/javase/9/jrockit-hotspot/compilation-optimization.htm#JRHMG119