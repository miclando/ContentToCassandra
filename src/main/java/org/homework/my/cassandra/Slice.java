package org.homework.my.cassandra;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.nio.ByteBuffer;

/**
 * Created by ml636r on 8/20/2017.
 */
@Table(keyspace = "content", name = "pages")
public class Slice {

    @PartitionKey
    @Column
    private String url;

    @Column
    private int slice;

    @Column
    private ByteBuffer data;


    public Slice() {

    }

    public Slice(String url,int slice, byte[] data) {
        this.url = url;
        this.slice = slice;
        if (data != null) {
            this.data = ByteBuffer.wrap(data.clone());
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSlice() {
        return slice;
    }

    public void setSlice(int slice) {
        this.slice = slice;
    }

    public ByteBuffer getData() {
        return data;
    }

    public void setData(ByteBuffer data) {
        if (data != null) {
            this.data = data.duplicate();
        }
    }


}
