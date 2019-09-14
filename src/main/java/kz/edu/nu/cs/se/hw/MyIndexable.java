package kz.edu.nu.cs.se.hw;

public class MyIndexable implements Indexable, Comparable{
    String entry;
    int lineNumber;
    int numInLine;

    MyIndexable (String word, int line, int num) {
        this.entry = word;
        this.lineNumber = line;
        this.numInLine = num;
    }

    @Override
    public String getEntry() {
        return entry;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    public int getNumInLine() {
        return numInLine;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof MyIndexable) {
            MyIndexable obj = (MyIndexable) o;
            if (this.entry == obj.entry) {
                return this.lineNumber - obj.lineNumber;
            } else {
                return this.entry.compareTo(obj.entry);
            }
        } else {
            return 1;
        }
    }
}
