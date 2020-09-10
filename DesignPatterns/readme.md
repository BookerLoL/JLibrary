# Design Patterns
- Command
    - Able to store functions as objects,  pass in a context to modify it 
- Flyweight
    - Avoids creating new objects that share the same properties
        - Useful if properties don't change but need to create many instances of them
            - Just store an instance and reuse them rather than create more of the same
- Interpreter 
    - For compilers/evaluating expressions
- State
    - Interface with all the options, the methods should take a context (object) that will be passed around and update it's current state
- Factory 
    - Hide the creation of the objects but return objects
- Abstract Factory
    - Factory classes implement a another factory
        - Allows creation of objects but with different subtypes
- Builder
    - Hides the constructor of a class (use private constructor)
    - Uses a static class within that class
- Iterator
    - Access elements in a collection without knowing underlying representation

# ToDo
- Source: https://gameprogrammingpatterns.com/contents.html
    - Observer, Prototype, Singleton, DoubleBuffer, Update Method, Subclass Sandbox, Object, Component, Event Queue, Service Locator, Data Locality, Dirty Flag, Object Pool, Spatial Partition

# Resources
- https://gameprogrammingpatterns.com/contents.html
