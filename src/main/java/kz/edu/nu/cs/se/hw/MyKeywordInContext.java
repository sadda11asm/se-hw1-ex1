package kz.edu.nu.cs.se.hw;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyKeywordInContext implements KeywordInContext {

    private String name;
    private String path;
    private ArrayList<String> lines;
    private ArrayList<MyIndexable> indexedWords;


    public static void main (String[] args) {
        MyKeywordInContext kwic = new MyKeywordInContext("newtonToEinstein", "newtonToEinstein.txt");
        kwic.txt2html();
        kwic.indexLines();
        kwic.writeIndexToFile();
    }

    public MyKeywordInContext(String name, String pathstring) {
        // TODO Auto-generated constructor stub
        this.name = name;
        this.path = pathstring;
        lines = new ArrayList<>();
        indexedWords = new ArrayList<>();
    }

    @Override
    public int find(String word) {
        word = word.toLowerCase();
        for (int i = 0; i<indexedWords.size();i++) {
            if (word.equals(indexedWords.get(i).entry)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Indexable get(int i) {
        return indexedWords.get(i);
    }

    @Override
    public void txt2html() {
        try {
            BufferedReader input = new BufferedReader(new FileReader(path));
            OutputStream file = new FileOutputStream(new File(name + ".html"));
            PrintStream output = new PrintStream(file);

            String start = "<html><body>";
            String end = "</body></html>";

            String inputText;
            while ((inputText = input.readLine())!=null) {
                lines.add(inputText);
            }

            for (int i = 0; i< lines.size(); i++) {
                if (i==0) {
                    output.println(start+lines.get(0) + "<span id = line_" + (i+1) + "> [" + (i + 1) + "] </span>");
                } else if (i+1==lines.size()) {
                    output.println(lines.get(i) + "<span id = line_" + (i+1) + "> [" + (i + 1) + "] </span>" + end);
                } else {
                    output.println(lines.get(i) + "<span id = line_" + (i+1) + "> [" + (i + 1) + "] </span>");
                }
                output.println("<br>");
            }
            output.close();
            input.close();
            file.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private HashSet<String> getStopWords() {
        HashSet<String> words = new HashSet<>();
        try {
            BufferedReader input = new BufferedReader(new FileReader("stopwords.txt"));
            String inputString;
            while ((inputString = input.readLine())!=null) {
                words.add(inputString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }

    @Override
    public void indexLines() {
        HashSet<String> stopWords = getStopWords();
        // TODO Auto-generated method stub
        for (int i = 0; i<lines.size(); i++) {
            Pattern p = Pattern.compile("([a-zA-Z0-9]+)");
            Matcher m1 = p.matcher(lines.get(i));
            ArrayList<String> words = new ArrayList<>();
            while (m1.find()) {
                if (m1.group().length()>1) {
                    words.add(m1.group());
                }
            }
            HashMap<String, Integer> num = new HashMap<>();
            for (int j = 0; j < words.size(); j++) {
                String word = words.get(j);
                int count = 0;
                if (num.containsKey(word.toLowerCase())) count = num.get(word.toLowerCase());
                count++;
                num.put(word.toLowerCase(), count);
                if (!stopWords.contains(word)) {
                    MyIndexable pair = new MyIndexable(word.toLowerCase(), i+1, count);
                    indexedWords.add(pair);
                }

            }
        }
        Collections.sort(indexedWords);
    }

    @Override
    public void writeIndexToFile() {
        // TODO Auto-generated method stub
        try {
            OutputStream file = new FileOutputStream(new File(  name + "-kwic.html"));
            PrintStream output = new PrintStream(file);

            String start = "<html><head><meta charset=\"UTF-8\"></head><body><div style=\"text-align:center;line-height:1.6\">";
            String end = "</div></body></html>";

            output.println(start);

            for (int i = 0; i<indexedWords.size();i++) {
                MyIndexable pair = indexedWords.get(i);
                String word = pair.getEntry();
                int num = pair.getNumInLine();
                String line = lines.get(pair.getLineNumber()-1);

                int count = 0;
                int j;
                for (j = 0; j<=line.length()-word.length();j++) {
                    if (line.substring(j, j+word.length()).toLowerCase().equals(word)) {
                        count++;
                        if (count == num) {
                            output.print("<a href=\"" + name  + ".html#line_" + pair.getLineNumber() +"\">" + word.toUpperCase() +"</a>");
                            j+=word.length()-1;
                        } else {
                            output.print(line.charAt(j));
                        }
                    } else {
                        output.print(line.charAt(j));
                    }
                }
                for (;j<line.length();j++) {
                    output.print(line.charAt(j));
                }
                output.println("<br>");
            }
            output.println(end);
            output.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
