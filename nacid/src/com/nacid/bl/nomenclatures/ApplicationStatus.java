package com.nacid.bl.nomenclatures;

import com.nacid.bl.numgenerator.NumgeneratorDataProvider;

import java.util.*;


public interface ApplicationStatus extends FlatNomenclature{
    /**
     * migrirano ot starata baza danni
     */
	public static final int APPLICATION_STATUS_MIGRATED = -1;
    public static final int APPLICATION_PODADENO_STATUS_CODE = 1;
    public static final int APPLICATION_IZCHAKVANE_STATUS_CODE = 2;
    public static final int APPLICATION_FOR_EXAMINATION_STATUS_CODE = 3;
    public static final int APPLICATION_OTTEGLENO_STATUS_CODE = 4;
    public static final int APPLICATION_LEGITIMNO_PO_SEDALISHTE_STATUS_CODE = 5;
    public static final int APPLICATION_LEGITIMNO_PO_MIASTO_STATUS_CODE = 6;
    public static final int APPLICATION_LEGITIMNA_PROGRAMA_STATUS_CODE = 43;

    public static final int APPLICATION_NELEGITIMNO_STATUS_CODE = 7;
    public static final int APPLICATION_AUTHENTIC_STATUS_CODE = 8;
    public static final int APPLICATION_FAKE_DIPLOMA_STATUS_CODE = 9;
    public static final int APPLICATION_PRIZNATO_STATUS_CODE = 10;
    
   
    /**
     * otkazano
     */
    public static final int APPLICATION_REFUSED_STATUS_CODE = 11;
    /**
     * otlojeno
     */
    public static final int APPLICATION_POSTPONED_STATUS_CODE = 12;
    /**
     * prekrateno  (za prekratqvane)
     */
    public static final int APPLICATION_TERMINATED_STATUS_CODE = 13;

    /**
     * dobrovolno prekrateno
     */
    public static final int APPLICATION_VOLUNTARILY_TERMINATED_CODE = 40;
    

    public static final int APPLICATION_OBEZSILENO_STATUS_CODE = 17;
    
    public static final int APPLICATION_POSTPONED_SUBMITTED_DOCS = 18;
    
    public static final int APPLICATION_NEDOPUSKANE_DO_RAZGLEJDANE_STATUS_CODE = 19;


    public static final int APPLICATION_DESTROYED_STATUS_CODE = 32;


    public static final int APPLICATION_LEGITIMNO_STATUS_CODE = 33;

    /**
     * udostoveren staj
     */
    public static final int APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE = 34;


    /**
     * udostoverena kvalifikaciq
     */
    public static final int APPLICATION_RECOGNIZED_QUALIFICATION_STATUS_CODE = 35;


    /**
     * udostoverena kvalifikaciq sys staj
     */
    public static final int APPLICATION_RECOGNIZED_EXPERIENCE_AND_QUALIFICATION_STATUS_CODE = 39;

    /**
     * neudostoverena kvalifikaciq
     */
    public static final int APPLICATION_NOT_RECOGNIZED_QUALIFICATION_STATUS_CODE = 37;
    /**
     * neudostoveren staj
     */
    public static final int APPLICATION_NOT_RECOGNIZED_EXPERIENCE_STATUS_CODE = 38;

    /**
     * neavtentichna
     */
    public static final int APPLICATION_NOT_AUTHENTIC_STATUS_CODE = 36;


    public static final int APPLICATION_SPIRANE_NA_SROKA = 41;












    /**
     * spisyk ot statusi, koito ne trqbva da se vijdat v zaqvleniq v kalendara na komisiqta, kogato e izbran filtyra samo aktivnite
     */
    public static final List<Integer> RUDI_COMMISSION_APPLICATION_ONLY_ACTIVE_STATUSES = Arrays.asList(APPLICATION_REFUSED_STATUS_CODE, APPLICATION_PRIZNATO_STATUS_CODE, APPLICATION_TERMINATED_STATUS_CODE);
    
    /**
     * pri zapis na taba obuchenie v zaqvlenie, ako statusa na zaqvlenieto e edin ot izbroenite to toi stava za prou4vane!
     */
    public final static List<Integer> RUDI_APPLCATION_STATUSES_FOR_CHANGE_TO_FOR_EXAMINATION = Arrays.asList(APPLICATION_PODADENO_STATUS_CODE, APPLICATION_IZCHAKVANE_STATUS_CODE);
    /**
     * statusi koito se davat ot komisiqta
     */
    public static final List<Integer> RUDI_APPLICATION_STATUSES_FROM_COMMISSION = Arrays.asList(ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE, ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE, ApplicationStatus.APPLICATION_TERMINATED_STATUS_CODE, ApplicationStatus.APPLICATION_POSTPONED_STATUS_CODE, ApplicationStatus.APPLICATION_NEDOPUSKANE_DO_RAZGLEJDANE_STATUS_CODE);




    /**
     * statusi pri koito moje da se generira udostoverenie
     */
    public static final List<Integer> REGPROF_POSITIVE_STATUS_CODES = Arrays.asList(APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE, APPLICATION_RECOGNIZED_QUALIFICATION_STATUS_CODE, APPLICATION_RECOGNIZED_EXPERIENCE_AND_QUALIFICATION_STATUS_CODE);

    /**
     * statusi za koito moje da se generira predlojenie za otkaz
     */
    public static final List<Integer> REGPROF_NEGATIVE_STATUS_CODES = Arrays.asList(APPLICATION_NOT_AUTHENTIC_STATUS_CODE, APPLICATION_NELEGITIMNO_STATUS_CODE, APPLICATION_REFUSED_STATUS_CODE, APPLICATION_NOT_RECOGNIZED_QUALIFICATION_STATUS_CODE, APPLICATION_NOT_RECOGNIZED_EXPERIENCE_STATUS_CODE);


    //public static final List<Integer> REGPROF_FINALIZATION_STATUS_CODES = Arrays.asList(APPLICATION_TERMINATED_STATUS_CODE, APPLICATION_OBEZSILENO_STATUS_CODE, APPLICATION_VOLUNTARILY_TERMINATED_CODE, APPLICATION_REFUSED_STATUS_CODE, APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE, APPLICATION_RECOGNIZED_QUALIFICATION_STATUS_CODE, APPLICATION_RECOGNIZED_EXPERIENCE_AND_QUALIFICATION_STATUS_CODE, APPLICATION_NOT_AUTHENTIC_STATUS_CODE, APPLICATION_NOT_RECOGNIZED_EXPERIENCE_STATUS_CODE, APPLICATION_NOT_RECOGNIZED_QUALIFICATION_STATUS_CODE, APPLICATION_NELEGITIMNO_STATUS_CODE);

    /**
     * opredelq v regprof pri koi statusi nqma da se pokazva taba "prikliucvane"
     */
    public static final List<Integer> REGPROF_NON_FINALIZATION_STATUS_CODES = Arrays.asList(APPLICATION_SPIRANE_NA_SROKA, APPLICATION_STATUS_MIGRATED, APPLICATION_PODADENO_STATUS_CODE, APPLICATION_FOR_EXAMINATION_STATUS_CODE, APPLICATION_LEGITIMNO_STATUS_CODE, APPLICATION_AUTHENTIC_STATUS_CODE);


    /**
     * map, koito sydyrja koi kodove ot STATUTE/AUTHENTICITY/RECOMMENDATION sa positive i koi negative
     * key   -> entryNumSeriesId -> STATUTE_SERIES_ID/AUTHENTICITY_SERIES_ID/RECOMMENDATION_SERIES_ID
     * value -> map -> key   -> applicationStatusId
     *                 value -> true - polojitelen; false - otricatelen
     */
    public static final Map<Integer, Map<Integer, Boolean>> STATUTE_AUTHENTICITY_RECOMMENDATION_POSITIVE_NEGATIVE_STATUS_CODES = new HashMap<Integer, Map<Integer, Boolean>>() {
        {
            Map<Integer, Boolean> codes = new HashMap<>();
            codes.put(APPLICATION_LEGITIMNO_PO_MIASTO_STATUS_CODE, true);
            codes.put(APPLICATION_NELEGITIMNO_STATUS_CODE, false);
            codes.put(APPLICATION_NEDOPUSKANE_DO_RAZGLEJDANE_STATUS_CODE, false);
            put(NumgeneratorDataProvider.STATUTE_SERIES_ID, codes);



            codes = new HashMap<>();
            codes.put(APPLICATION_AUTHENTIC_STATUS_CODE, true);
            codes.put(APPLICATION_FAKE_DIPLOMA_STATUS_CODE, false);
            put(NumgeneratorDataProvider.AUTHENTICITY_SERIES_ID, codes);

            codes = new HashMap<>();
            codes.put(APPLICATION_PRIZNATO_STATUS_CODE, true);
            codes.put(APPLICATION_REFUSED_STATUS_CODE, true);
            codes.put(APPLICATION_NEDOPUSKANE_DO_RAZGLEJDANE_STATUS_CODE, true);
            put(NumgeneratorDataProvider.RECOMMENDATION_SERIES_ID, codes);
        }
    };


    public boolean isLegal();
}
