package com.nacid.bl.impl.numgenerator;

import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.numgenerator.EntryNumSeriesRecord;
import com.nacid.db.numgenerator.NumgeneratorDB;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by georgi.georgiev on 07.04.2015.
 */
public class NumgeneratorDataProviderImpl implements NumgeneratorDataProvider {



    private NacidDataProviderImpl nacidDataProvider;
    private NumgeneratorDB db;
    private Map<Integer, EntryNumSeriesRecord> entryNumSeriesMap = new HashMap<Integer, EntryNumSeriesRecord>();


    public NumgeneratorDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new NumgeneratorDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
        clearCache();

    }

    public void clearCache() {
        try {
            List<EntryNumSeriesRecord> series = db.getEntryNumSeries();
            for (EntryNumSeriesRecord s : series) {
                entryNumSeriesMap.put(s.getId(), s);
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }


    public String getNextNumber(int seriesId, int submissionType) {
        try {
            return db.getNumber(seriesId, submissionType);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public String getEntryNumSeriesNameById(int entryNumSeriesId) {
        return entryNumSeriesMap.get(entryNumSeriesId).getSeriesName();
    }

    public Date getWorkingDate() {
        Calendar cal = Calendar.getInstance();
        Utils.clearTimeFields(cal);
        return cal.getTime();
    }
}
