package com.maxdemarzi;

import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.event.TransactionData;

public class K2Runnable implements Runnable {

    private static TransactionData td;


    public K2Runnable (TransactionData transactionData) {
        td = transactionData;
    }

    @Override
    public void run() {
        for (Relationship relationship : td.createdRelationships()) {
            relationship.getStartNode();
            relationship.getEndNode();
            relationship.getType();
        }
    }

}
