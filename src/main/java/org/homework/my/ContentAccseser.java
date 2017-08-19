package org.homework.my;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

import java.sql.ResultSet;

/**
 * Created by ml636r on 8/12/2017.
 */
@Accessor
public interface ContentAccseser {

    @Query("SELECT * FROM content.pages WHERE URL = :url")
    public ResultSet getAllByUrl( String url);

    @Query("SELECT * FROM content.pages WHERE URL = :url And part = :partNumber")
    public ResultSet getPartByUrlAndPartNumber( String url,int partNumber);
}
