package com.maxdemarzi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.*;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class K2TreesTest {
    private GraphDatabaseService db;
    private Random rand = new Random();
    RelationshipType LIKES = RelationshipType.withName("LIKES");
    RelationshipType PURCHASED = RelationshipType.withName("PURCHASED");
    public int userCount = 10;
    public int itemCount = 5;
    public int likesCount = 3;
    public int purchaseCount = 1;

    @Before
    public void setup() throws IOException {
        db = new TestGraphDatabaseFactory().newImpermanentDatabase();
        populateDb(db);
    }

    @Test
    public void shouldWork() {
        int count = 0;
        try (Transaction tx = db.beginTx()) {
            for (int user = 0; user < userCount; user++) {
                for (int item = 0; item < itemCount; item++) {
                    long userId =  db.findNode(Label.label("User"), "username", "user" + String.valueOf(user)).getId();
                    long itemId =  db.findNode(Label.label("Item"), "name", "thing" + String.valueOf(item)).getId();
                    if (K2Trees.get("LIKES", userId, itemId)) {
                        count++;
                    }
                }
            }
            tx.success();
        }
        int expected = userCount * likesCount;
        Assert.assertEquals(expected, count);
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
            for (int i = 0; i < (itemCount); i++){
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
}
