package org.homework.my;

import com.datastax.driver.mapping.Result;
import org.homework.my.cassandra.CassandraClient;
import org.homework.my.cassandra.ContentAccessor;
import org.homework.my.cassandra.Slice;

import java.util.List;

/**
 * Created by ml636r on 8/12/2017.
 */
public class ContentReader {

    private ContentAccessor contentAccessor;
    private CassandraClient client;

    /**
     * constructor
     * @param client C* client
     */
    public ContentReader(CassandraClient client) {
        this.client=client;
        this.contentAccessor = this.client.getContentAccessor();
    }

    /**
     * the method retrieves the page according to the provided URL
     * @param url the url used to search in the DB
     * @return string representing the stored web page.
     */
    public String getPage(String url){
        Result<Slice> res = contentAccessor.getAllByUrl(url);
        StringBuffer sb = new StringBuffer();

        List<Slice> resultList = res.all();
        for(Slice slice: resultList){
            sb.append(new String(slice.getData().array()));
        }
        return sb.toString();
    }

    /**
     * the method retrieves the page according to the provided URL and slice number
     * @param url the url used to search in the DB
     * @param slice the number of the slice
     * @return a String representation of a byte array representing the slice
     */
    public byte[] getPageSlice(String url,int slice){
        Result<Slice> res = contentAccessor.getPartByUrlAndPartNumber(url,slice);
        List<Slice> resultList=res.all();
        if(resultList.size()>1){
            return null;
        }
        return resultList.get(0).getData().array();
    }
}
