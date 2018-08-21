package com.mongodb.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FileParser {

    String file;
    String nextLine;

    public FileParser(String file) {
        this.file = file;
    }

    public Iterable<Entry> iterate() {

        InputStream stream = FileParser.class.getClassLoader().getResourceAsStream(file);
        BufferedReader bred = new BufferedReader(new InputStreamReader(stream));

        try {
            this.nextLine = bred.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            this.nextLine = null;
        }

        return () -> new Iterator<Entry>() {
            @Override
            public boolean hasNext() {
                return FileParser.this.nextLine != null;
            }

            @Override
            public Entry next() {
                String nextLine = FileParser.this.nextLine;

                while(nextLine != null && nextLine.startsWith("#")) {
                    try {
                        FileParser.this.nextLine = bred.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        FileParser.this.nextLine = null;
                    }
                    nextLine = FileParser.this.nextLine;
                }

                if (nextLine == null) {
                    System.out.println("End of file");
                    throw new NoSuchElementException("File is over");
                } else {
                    try {
                        FileParser.this.nextLine = bred.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        FileParser.this.nextLine = null;
                    }
                    return FileParser.this.parseLine(nextLine);
                }
            }
        };
    }

    private Entry parseLine(String nextLine) {
        String[] strings = nextLine.split("¦");
        return new Entry(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]),
                Integer.parseInt(strings[2]), strings[3]);
    }
}
