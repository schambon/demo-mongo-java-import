package com.mongodb.demo;

import org.apache.commons.cli.*;

public class CLIHelper {


    String file = "data.txt";

    String mongoUri = "mongodb://localhost:27017";
    String db = "demo";
    String coll = "entries";

    int threads = 4;

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMongoUri() {
        return mongoUri;
    }

    public void setMongoUri(String mongoUri) {
        this.mongoUri = mongoUri;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getColl() {
        return coll;
    }

    public void setColl(String coll) {
        this.coll = coll;
    }

    public boolean parseArguments(String[] args) {

        CommandLineParser parser = new DefaultParser();
        Options opts = new Options();
        opts.addOption("f", "file", true, "Input file");
        opts.addOption("u", "mongo-uri", true, "MongoDB URI");
        opts.addOption("d", "database", true, "Database");
        opts.addOption("c", "collection", true, "Collection");
        opts.addOption("t", "threads", true, "Concurrent threads");

        try {
            CommandLine line = parser.parse(opts, args);
            if (line.hasOption("file")) {
                this.setFile(line.getOptionValue("file"));
            }
            if (line.hasOption("mongo-uri")) {
                this.setMongoUri(line.getOptionValue("mongo-uri"));
            }
            if (line.hasOption("threads")) {
                this.setThreads(Integer.parseInt(line.getOptionValue("threads")));
            }
            if (line.hasOption("database")) {
                this.setDb(line.getOptionValue("database"));
            }
            if (line.hasOption("collection")) {
                this.setColl(line.getOptionValue("collection"));
            }
            return true;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }



}
