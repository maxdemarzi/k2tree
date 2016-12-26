package com.maxdemarzi;

import org.neo4j.graphdb.*;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class K2TreeBenchmark {

    private GraphDatabaseService db;
    private Random rand = new Random();
    RelationshipType LIKES = RelationshipType.withName("LIKES");
    RelationshipType PURCHASED = RelationshipType.withName("PURCHASED");

    @Param({"1000"})
    public int userCount;

    @Param({"100"})
    public int itemCount;

    @Param({"50"})
    public int likesCount;

    @Param({"5"})
    public int purchaseCount;


    @Setup
    public void prepare() throws IOException {
        db = new TestGraphDatabaseFactory().newImpermanentDatabase();
        populateDb(db);
    }

    @TearDown
    public void tearDown() {
        db.shutdown();
    }

    private void populateDb(GraphDatabaseService db) throws IOException {
        ArrayList<Node> users = new ArrayList<>();
        ArrayList<Node> items = new ArrayList<>();

        try (Transaction tx = db.beginTx()) {
            for (int i = 0; i < userCount; i++) {
                users.add(createNode(db, "User", "username", "user" + String.valueOf(i)));
            }
            tx.success();
        }

        try (Transaction tx = db.beginTx()) {
            for (int i = 0; i < (itemCount + 5); i++){
                items.add(createNode(db, "Item", "name", "thing" + String.valueOf(i)));
            }
            tx.success();
        }
        Transaction tx = db.beginTx();
        try  {
            for (int i = 0; i < userCount; i++){
                Node user =  users.get(i);

                for (int j = 0; j < likesCount; j++) {
                    user.createRelationshipTo(items.get(j), LIKES);
                }

                for (int j = 0; j < purchaseCount; j++) {
                    user.createRelationshipTo(items.get(j), PURCHASED);
                }

                if(i % 100 == 0){
                    tx.success();
                    tx.close();
                    tx = db.beginTx();
                }

            }
            tx.success();
        } finally {
            tx.close();
        }
    }

    private Node createNode(GraphDatabaseService db, String label, String property, String value) {
        Node node = db.createNode(Label.label(label));
        node.setProperty(property, value);
        return node;
    }

    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 5)
    @Fork(1)
    @Threads(4)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public boolean measureSingleLike() throws IOException {
        return K2Trees.get("LIKES", rand.nextInt(userCount), userCount + rand.nextInt(itemCount));
    }

    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 5)
    @Fork(1)
    @Threads(4)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public boolean measureSingleLikeK4() throws IOException {
        return K4Trees.get("LIKES", rand.nextInt(userCount), userCount + rand.nextInt(itemCount));
    }

    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 5)
    @Fork(1)
    @Threads(4)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public boolean measureRoaringSingleLike() throws IOException {
        return RK2Trees.get("LIKES", rand.nextInt(userCount), userCount + rand.nextInt(itemCount));
    }

    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 5)
    @Fork(1)
    @Threads(4)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public boolean measureSingleLike2() throws IOException {
        try (Transaction tx = db.beginTx()) {
            Node userNode = db.getNodeById(rand.nextInt(userCount));
            Node itemNode = db.getNodeById(userCount + rand.nextInt(itemCount));

            for (Relationship rel : userNode.getRelationships(Direction.OUTGOING, LIKES)) {
                if (rel.getEndNode().equals(itemNode)) {
                    return true;
                }
            }

            tx.success();
        }
        return false;
    }

    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 5)
    @Fork(1)
    @Threads(4)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureAllUserLikes() throws IOException {
        int count = 0;
        for (int user = 0; user < userCount; user++){
            ArrayList<Integer> itemNodeIds = K2Trees.getByX("LIKES", user);
            count += itemNodeIds.size();
        }
    }

    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 5)
    @Fork(1)
    @Threads(4)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureAllUserLikes2() throws IOException {
        int count = 0;
        try (Transaction tx = db.beginTx()) {
            for (int user = 0; user < userCount; user++) {
                Node userNode = db.getNodeById(user);
                ArrayList<Long> itemNodeIds = new ArrayList<>();
                for (Relationship rel : userNode.getRelationships(Direction.OUTGOING, LIKES)) {
                    itemNodeIds.add(rel.getEndNode().getId());
                }
                count += itemNodeIds.size();
            }
            tx.success();
        }
    }
    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 5)
    @Fork(1)
    @Threads(4)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void measureAllUserLikes4() throws IOException {
        int count = 0;
        for (int user = 0; user < userCount; user++){
            ArrayList<Integer> itemNodeIds = K4Trees.getByX("LIKES", user);
            count += itemNodeIds.size();
        }
    }

}
