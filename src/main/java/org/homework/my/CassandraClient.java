package org.homework.my;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.schemabuilder.CreateKeyspace;
import com.datastax.driver.core.schemabuilder.KeyspaceOptions;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.core.schemabuilder.SchemaStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ml636r on 8/12/2017.
 */
public class CassandraClient {


    private  Cluster cluster;
    private Session session;
    private String keySpace="content";
    private String table="pages";

    public CassandraClient(String ip, int port){
        cluster = Cluster.builder()
                .addContactPoints(ip).withPort(port)
                .build();

        System.out.printf("Connected to cluster: %s%n", cluster.getMetadata().getClusterName());
    }

    public void connect() {
        session = cluster.connect();
    }


    public void createSchema() {
        Map<String, Object> replicationOptions = new HashMap<String, Object>();
        replicationOptions.put("class", "SimpleStrategy");
        replicationOptions.put("replication_factor", 1);
        SchemaStatement statement = SchemaBuilder.createKeyspace(this.keySpace)
                .ifNotExists()
                .with()
                .replication(replicationOptions);

        session.execute(statement.getQueryString());

        statement=SchemaBuilder.createTable(table).ifNotExists()
                .addPartitionKey("url", DataType.varchar())
                .addClusteringColumn("slice",DataType.bigint())
                .addColumn("data",DataType.blob());


        session.execute(statement);

    }




}
