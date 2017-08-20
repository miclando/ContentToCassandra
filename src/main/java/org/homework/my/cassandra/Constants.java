package org.homework.my.cassandra;

/**
 * Created by ml636r on 8/20/2017.
 */
public enum Constants {
    KEYSPACE("content"),
    TABLE("pages");

    String name;

    Constants(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
