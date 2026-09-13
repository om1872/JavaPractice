package documentEditor;

import documentEditor.impl.ConsolePrinter;
import documentEditor.impl.FilePersistence;

public class DocumentBuilderClient {
    public static void main(String[] args) {
        Persistence fileClient = new FilePersistence();
        Printer consolePrinter = new ConsolePrinter();

        Document document = new Document(consolePrinter, fileClient);

        document.addText("Hi, my document!");
        document.print();
        document.save();
        document.addImage("abc.png");
        document.print();
        document.save();
        document.addText("next sentence");
        document.print();
        document.save();

    }
}
