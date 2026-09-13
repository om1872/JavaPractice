package documentEditor.impl;

import documentEditor.Printer;

public class ConsolePrinter implements Printer {
    @Override
    public void print(String s) {
        System.out.print(s);
    }
}
