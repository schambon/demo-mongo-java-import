package com.mongodb.demo;

public class Reporter {

    int imported = 0;

    synchronized public void inc() {
        imported++;
    }

    synchronized public void inc(int nb) {
        imported += nb;
    }

    public void done() {
        System.out.println("Done!");
    }

    public void report() {
        System.out.println(String.format("Imported %d entries", imported));
    }
}
