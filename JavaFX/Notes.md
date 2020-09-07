# Notes
## Setting up JavaFX in Eclipse 
1) Ensure JDK pacakge is set up
2) Follow the guide on downloading the SDK, using Oracle's website, https://www.oracle.com/java/technologies/install-javafx-sdk.html
    - Will need to create an Account
3) follow the guide on setting up javafx in eclipse, https://openjfx.io/openjfx-docs/

##  JavaFX Notes
- FontSmoothingType is used to specify the preferred mechanism used to smooth the edges of fonts for on-screen text.
- ContextMenu is for right clicking in a view for a popup menu
- WebView
    - getEngine and load a page
- Mouse events
    - CLICKED (pressed and released), PRESSED, RELEASED, MOVED (no buttons pressed, within node), DRAGGED, ENTERED, EXITED
- Determining the width or height of a label before displayed 
    - Methods
        1)
        ```Java
        //slow example
        Group root = new Group();
        Label label = new Label("text");
        root.getCHildren().add(label);
        Scene scene = new Scene(root);

        root.applyCss();
        root.layout();
        System.out.println(label.getWidth()); //will work properly now
        ```
        2) Using a Text instead of Label