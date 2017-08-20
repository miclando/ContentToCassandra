package org.homework.my.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.core.schemabuilder.SchemaStatement;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ml636r on 8/12/2017.
 */
public class CassandraClient {


    private  Cluster cluster;
    private Session session;
    private MappingManager manager;
    private Mapper<Slice> mapper;

    /**
     * constructor
     * @param ip ip if the cassandra instance we want to use
     * @param port the port of the cassandra instance we want to use
     */
    public CassandraClient(String ip, int port){
        cluster = Cluster.builder()
                .addContactPoints(ip).withPort(port)
                .build();
        System.out.printf("Connected to cluster: %s%n", cluster.getMetadata().getClusterName());
    }

    /**
     * the method connects to the C*
     */
    public void connect() {
        session = cluster.connect();
    }

    /**
     * the method connects to the C* and created the mapper objects
     */
    public void createMapping() {
        manager = new MappingManager(session);
        mapper=manager.mapper(Slice.class);
    }


    /**
     * the method creates the key space and the table needed to the cassandra to function
     * the method checks if the tables need to be created or not.
     */
    public void createSchema() {
        Map<String, Object> replicationOptions = new HashMap<String, Object>();
        replicationOptions.put("class", "SimpleStrategy");
        replicationOptions.put("replication_factor", 1);
        SchemaStatement statement = SchemaBuilder.createKeyspace(Constants.KEYSPACE.getName())
                .ifNotExists()
                .with()
                .replication(replicationOptions);

        session.execute(statement.getQueryString());
        statement=SchemaBuilder.createTable(Constants.KEYSPACE.getName(),Constants.TABLE.getName()).ifNotExists()
                .addPartitionKey("url", DataType.varchar())
                .addClusteringColumn("slice",DataType.cint())
                .addColumn("data",DataType.blob());
        session.execute(statement);
    }

    /**
     * the method closes the client conection to the c*
     */
    public void close(){
        if(cluster!=null){
            cluster.close();
        }
    }

    /**
     * the method drops the key space.
     */
    public void clearAll(){
        SchemaStatement statement = SchemaBuilder.dropKeyspace(Constants.KEYSPACE.getName());
        session.execute(statement.getQueryString());
    }


    /**
     * the method stores the slice object in the DB
     * @param slice the object representing part of the page.
     */
    public void save(Slice slice){
        mapper.save(slice);
    }

    /**
     * the method creates the accessor object used to accses the c* table
     * @return ContentAccessor
     */
    public ContentAccessor getContentAccessor(){
        ContentAccessor res=null;
        if(manager!=null){
            res=manager.createAccessor(ContentAccessor.class);
        }
        return res;
    }
}
