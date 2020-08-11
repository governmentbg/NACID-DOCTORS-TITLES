package com.nacid.bl.numgenerator;

import com.nacid.bl.nomenclatures.ApplicationType;

import java.util.*;

/**
 * Created by georgi.georgiev on 07.04.2015.
 */
public interface NumgeneratorDataProvider {
    public static final int NACID_SERIES_ID = 4;
    public static final int REGPROF_SERIES_ID = 5;
    public static final int STATUTE_SERIES_ID = 16;
    public static final int AUTHENTICITY_SERIES_ID = 17;
    public static final int RECOMMENDATION_SERIES_ID = 18;
    public static final int DOCTORATE_SERIES_ID = 21;

    public static final int SUBMISSION_TYPE_ELECTRONICAL = 1;
    public static final int SUBMISSION_TYPE_DESK = 2;

    public static final Map<Integer, List<Integer>> APPLICATION_TYPE_TO_ENTRYNUM_SERIES = new HashMap<Integer, List<Integer>>(){{
        put(ApplicationType.RUDI_APPLICATION_TYPE, Arrays.asList(NACID_SERIES_ID));
        put(ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE, Arrays.asList(STATUTE_SERIES_ID, AUTHENTICITY_SERIES_ID, RECOMMENDATION_SERIES_ID));
        put(ApplicationType.DOCTORATE_APPLICATION_TYPE, Arrays.asList(DOCTORATE_SERIES_ID));
    }};
    public static final Map<Integer, Integer> ENTRY_NUM_SERIES_TO_APPLICATION_TYPE = new HashMap<Integer, Integer>(){{
        put(NACID_SERIES_ID, ApplicationType.RUDI_APPLICATION_TYPE);
        put(STATUTE_SERIES_ID, ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE);
        put(AUTHENTICITY_SERIES_ID, ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE);
        put(RECOMMENDATION_SERIES_ID, ApplicationType.STATUTE_AUTHENTICITY_RECOMMENDATION_APPLICATION_TYPE);
        put(DOCTORATE_SERIES_ID, ApplicationType.DOCTORATE_APPLICATION_TYPE);
    }};


    public String getNextNumber(int seriesId, int submissionType);

    public Date getWorkingDate();

    public String getEntryNumSeriesNameById(int entryNumSeriesId);


}
