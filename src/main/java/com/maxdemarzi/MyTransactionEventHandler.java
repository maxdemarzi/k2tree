package com.maxdemarzi;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;

import java.util.concurrent.ExecutorService;

public class MyTransactionEventHandler implements TransactionEventHandler {

    public static GraphDatabaseService db;
    private static ExecutorService ex;

    public MyTransactionEventHandler(GraphDatabaseService graphDatabaseService, ExecutorService executor) {
        db = graphDatabaseService;
        ex = executor;
    }

    @Override
    public Object beforeCommit(TransactionData transactionData) throws Exception {
        return null;
    }

    @Override
    public void afterCommit(TransactionData td, Object o) {
        //ex.submit(new K2Runnable(td));
        try (Transaction tx = db.beginTx()) {
            for (Relationship relationship : td.createdRelationships()) {
                K2Trees.set(
                        relationship.getType().name(),
                        relationship.getStartNode().getId(),
                        relationship.getEndNode().getId()
                );
                RK2Trees.set(
                        relationship.getType().name(),
                        relationship.getStartNode().getId(),
                        relationship.getEndNode().getId()
                );
                K4Trees.set(
                        relationship.getType().name(),
                        relationship.getStartNode().getId(),
                        relationship.getEndNode().getId()
                );
            }

            for (Relationship relationship : td.deletedRelationships()) {
                K2Trees.unset(
                        relationship.getType().name(),
                        relationship.getStartNode().getId(),
                        relationship.getEndNode().getId());
            }
            tx.success();
        }
    }

    @Override
    public void afterRollback(TransactionData transactionData, Object o) {

    }
}
