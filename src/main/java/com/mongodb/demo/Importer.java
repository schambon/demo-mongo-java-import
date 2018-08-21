package com.mongodb.demo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Importer {

    String file, mongoUri, dbname, collname;
    int threads;

    public Importer(String file, String mongoUri, String dbname, String collname, int threads) {
        this.file = file;
        this.mongoUri = mongoUri;
        this.threads = threads;
        this.dbname = dbname;
        this.collname = collname;
    }

    public void run() {
        MongoClient client = new MongoClient(new MongoClientURI(mongoUri));
        MongoCollection<Document> collection = client.getDatabase(dbname).getCollection(collname);

        Reporter reporter = new Reporter();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                reporter.report();
            }
        }, 0, 1000);

        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        List<Entry> buffer = new ArrayList<>();
        int lastField1 = -1;
        int lastField2 = -1;

        FileParser parser = new FileParser(file);
        for (Entry entry : parser.iterate()) {
            if (lastField1 == -1) {
                // 1st entry
                lastField1 = entry.getField1();
                lastField2 = entry.getField2();
            }

            if (lastField1 == entry.getField1() && lastField2 == entry.getField2()) {
                buffer.add(entry);
            } else {
                executorService.submit(new InsertJob(new ArrayList<>(buffer), collection, reporter));
                buffer.clear();
                lastField1 = entry.getField1();
                lastField2 = entry.getField2();
            }
        }
        executorService.submit(new InsertJob(buffer, collection, reporter));

        reporter.done();
//        timer.cancel();
    }
}
