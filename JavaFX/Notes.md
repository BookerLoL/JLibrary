# Notes
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