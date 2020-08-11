package com.nacid.db.numgenerator;

import com.nacid.data.common.StringValue;
import com.nacid.data.numgenerator.EntryNumSeriesRecord;
import com.nacid.db.utils.DatabaseService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by georgi.georgiev on 07.04.2015.
 */
public class NumgeneratorDB extends DatabaseService {
    public static final String GET_NUMBER = "select numgenerator.get_num_without_date(?, ?) as value";

    public NumgeneratorDB(DataSource ds) {
        super(ds);
    }

    public String getNumber(int seriesId, int submissionType) throws SQLException {
        return selectRecords(GET_NUMBER, StringValue.class, seriesId, submissionType).get(0).getValue();
    }

    public List<EntryNumSeriesRecord> getEntryNumSeries() throws SQLException {
        return selectRecords(EntryNumSeriesRecord.class, null);
    }

}
