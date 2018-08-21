package com.mongodb.demo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class InsertJob implements Runnable {

    int batchSize = 1000;
    int bucketSize = 20;
    List<Entry> entries;
    MongoCollection<Document> collection;

    Reporter reporter;

    public InsertJob(List<Entry> entries, MongoCollection<Document> collection, Reporter reporter) {
        System.out.println(String.format("New job with %d entries", entries.size()));
        this.entries = entries;
        this.collection = collection;
        this.reporter = reporter;
    }

    @Override
    public void run() {
        List<Document> documents = new ArrayList<>();
        if (entries != null && ! entries.isEmpty()) {
            Document doc = doc();
            List<Document> sub = new ArrayList<>();

            int i = 0;
            for (Entry entry : entries) {
                if (i < bucketSize) {
                    sub.add(toSub(entry));
                    i++;
                    reporter.inc();
                } else {
                    // flush!
                    System.out.println("Flush");
                    doc.append("sub", new ArrayList(sub));
                    documents.add(doc);

                    sub.clear();
                    sub.add(toSub(entry));
                    i++;
                    reporter.inc();
                    
                    doc = doc();
                    i = 0;

                    if (documents.size() % batchSize == 0) {
                        collection.insertMany(documents);
                        documents.clear();
                    }
                }
            }
            System.out.println("Last flush");
            doc.append("sub", sub);
            documents.add(doc);

            collection.insertMany(documents);
        }
    }

    Document doc() {
        return new Document()
                .append("Field1", entries.get(0).field1)
                .append("Field2", entries.get(0).field2);
    }

    Document toSub(Entry entry) {
        return new Document()
                .append("Field3", entry.getField3())
                .append("Field4", entry.getField4());
    }
}
