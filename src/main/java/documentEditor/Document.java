package documentEditor;

import documentEditor.impl.Image;
import documentEditor.impl.Text;
import java.util.ArrayList;
import java.util.List;

public class Document {
    private String document = "";
    private final List<DocumentElement> elements;
    private final Printer printer;
    private final Persistence persistence;

    public Document(Printer printer, Persistence persistence) {
        this.printer = printer;
        this.persistence = persistence;
        this.elements = new ArrayList<>();
    }

    public void addText(String text) {
        DocumentElement docElement = new Text(text);
        addElement(docElement);
    }

    public void addImage(String path) {
        DocumentElement docElement = new Image(path);
        addElement(docElement);
    }

    public void print() {
        buildDocument();
        printer.print(document);
    }

    public void save() {
        buildDocument();
        persistence.save(document);
    }

    private void buildDocument() {
        StringBuilder sb = new StringBuilder();
        for(DocumentElement docElem : elements) {
            sb.append(docElem.getContent());
        }
        document = sb.toString();
    }

    private void addElement(DocumentElement docElem) {
        elements.add(docElem);
    }
}
