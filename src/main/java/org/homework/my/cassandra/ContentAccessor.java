package org.homework.my.cassandra;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

import java.sql.ResultSet;

/**
 * Created by ml636r on 8/12/2017.
 */
@Accessor
public interface ContentAccessor {

    /**
     * the method retrieves all the parts of the page by url,
     * the returned parts are ordered by the slice number.
     * @param url the url used to search in the DB
     * @return a set holding the found record's.
     */
    @Query("SELECT * FROM content.pages WHERE URL = :url ORDER BY slice")
    public Result<Slice> getAllByUrl(@Param("url") String url);

    /**
     * the method returns the slice matching the provided parameters.
     * @param url the url used to search in the DB
     * @param slice the id of the slice we want to return
     * @return a set holding the found record
     */
    @Query("SELECT * FROM content.pages WHERE URL = :url And slice = :slice")
    public Result<Slice> getPartByUrlAndPartNumber(@Param("url") String url,@Param("slice") int slice);
}
