package com.mongodb.demo;

public class Main {

    public static void main(String[] args) {
        CLIHelper helper = new CLIHelper();

        if (helper.parseArguments(args)) {

            Importer importer = new Importer(helper.getFile(), helper.getMongoUri(), helper.getDb(), helper.getColl(), helper.getThreads());
            importer.run();


        }  else {
            System.err.println("Cannot parse command line, abort.");
        }


    }
}
