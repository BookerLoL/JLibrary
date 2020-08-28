# Running Java Through Command Line
- Windows
    - set path=path_to_jdk_bin;
    - javac filename.java
    ``` 
    set path=C:\Program Files\Java\jdk-14.0.2\bin; 
    javac HelloWorld.java
    ```
# Setting up JDK package
- Windows
    - environment variables -> System Variables -> New -> Variable name = **JAVA_HOME**, Variable value = **path_to_jdk_bin**
    ```
    Variable name = JAVA_HOME
    Variable value = C:\Program Files\Java\jdk-14.0.2\bin 
    ```
# Setting up JavaFX in Eclipse 
1) Ensure JDK pacakge is set up
2) Follow the guide on downloading the SDK, using Oracle's website, https://www.oracle.com/java/technologies/install-javafx-sdk.html
    - Will need to create an Account
3) follow the guide on setting up javafx in eclipse, https://openjfx.io/openjfx-docs/

# JavaFX Notes
- FontSmoothingType is used to specify the preferred mechanism used to smooth the edges of fonts for on-screen text.
- ContextMenu is for right clicking in a view for a popup menu
- WebView
    - getEngine and load a page
    