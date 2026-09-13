package documentEditor.impl;

import documentEditor.DocumentElement;

public class Text implements DocumentElement {
    private final String content;

    public Text(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
