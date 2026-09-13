package documentEditor.impl;


import documentEditor.DocumentElement;

public class Image implements DocumentElement {
    private final String path;

    public Image(String path) {
        this.path = path;
    }

    @Override
    public String getContent() {
        return "[Image: " + path + "]";
    }
}
