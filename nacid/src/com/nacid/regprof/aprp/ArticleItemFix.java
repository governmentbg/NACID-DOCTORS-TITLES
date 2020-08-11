package com.nacid.regprof.aprp;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.regprof.*;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.nomenclatures.regprof.APRPImpl;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.utils.csv.Csv2Record;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleItemFix {
    
    private static final int LINE = 0;
    private static final List<Integer> SKIP_LINES = Arrays.asList(1, 45, 72, 129, 162, 163, 164, 165, 166, 167);
    private static final Logger logger = Logger.getLogger(ArticleItemFix.class);

    public ArticleItemFix() {
        // TODO Auto-generated constructor stub
    }

    private static void fixArticleItems() throws IOException, ParseException {
        Csv2Record<APRPImpl> csv2record = new Csv2Record<APRPImpl>(APRPImpl.class, "D:/projects/NACID/src/com/nacid/regprof/aprp/aprp.properties");
        csv2record.setSeparator(';');
        csv2record.setSkipLines(0);
        List<APRPImpl> impls = csv2record.generateRecords("D:/aprp2.csv");
        DataSource ds = new StandAloneDataSource("jdbc:postgresql://localhost:9000/NACID/", "postgres", "postgres");
        NacidDataProvider nacidDataProvider = new NacidDataProviderImpl(ds);
        RegprofTrainingCourseDataProvider tcDP = nacidDataProvider.getRegprofTrainingCourseDataProvider();
        RegprofApplicationDataProvider dp = nacidDataProvider.getRegprofApplicationDataProvider();
        List<String> message = new LinkedList<String>();
        
        Map<String, Integer> appNumToId = new HashMap<String, Integer>();
        List<? extends RegprofApplicationDetailsForList> applications = dp.getRegprofApplicationDetailsForList(false);
        Calendar calendar = Calendar.getInstance();
        if (applications != null) {
            for (RegprofApplicationDetailsForList a : applications) {
                if (a.getAppDate() != null) {
                    calendar.setTime(a.getAppDate());
                    appNumToId.put(a.getAppNum() + "/" + calendar.get(Calendar.YEAR), a.getId());
                }
            }
        }
        int counter = 0;

        for (APRPImpl application : impls) {
            counter++;
            if (!SKIP_LINES.contains(counter) && counter > LINE) {
                System.out.println("Line: " + counter);
            } else {
                continue;
            }
            String appNum = application.getDocflowNum().trim() + "/" + application.getYear().trim();
            Integer appId = appNumToId.get(appNum);
            if (appId == null) {
                System.out.println("Null appId on line: " + counter);
                continue;
            } else {
                System.out.println("Id " + appId + " on line " + counter);
            }
            Integer trainingCourseId = tcDP.getRegprofTrainingCourse(appId).getDetails().getId();
            if (trainingCourseId == null) {
                System.out.println("Null tc id on line: " + counter);
                continue;
            }
            RegprofTrainingCourse tc = tcDP.getRegprofTrainingCourse(appId);
            if (tc == null) {
                System.out.println("Training course is null");
                continue;
            }
            if (tc.getDetails() == null) {
                System.out.println("Training course details record is null");
                message.add(appId + " has null details training course");
                continue;
            }
            RegprofQualificationExamination record = dp.getRegprofQualificationExaminationForTrainingCourse(tc.getDetails().getId());
            if (record == null) {
                System.out.println("Exam record is null");
                message.add(appId + " has null exam record");
                continue;
            }
            
            String article = application.getArticleItem() != null ? application.getArticleItem() : "";
            Integer articleItemId = null;
            if (!article.isEmpty()) {
                if (article.length() == 1) {
                    if (article.equalsIgnoreCase("а")) {
                        articleItemId = 35;
                    } else if (article.equalsIgnoreCase("б")) {
                        articleItemId = 29;
                    } else if (article.equalsIgnoreCase("в")) {
                        articleItemId = 31;
                    } else if (article.equalsIgnoreCase("г")) {
                        articleItemId = 33;
                    } else if (article.equalsIgnoreCase("д")) {
                        articleItemId = 34;
                    }
                } else {
                    char articleItem = '\0';
                    Pattern pattern = Pattern.compile("„.”");
                    Matcher matcher = pattern.matcher(article);
                    boolean found = false;
                    while (matcher.find()) {
                        articleItem = article.charAt(matcher.start() + 1);
                        found = true;
                    }
                    if (found) {
                        if (article.contains("11")) {
                            if (articleItem == 'а') {
                                articleItemId = 35;
                            } else if (articleItem == 'б') {
                                articleItemId = 29;
                            } else if (articleItem == 'в') {
                                articleItemId = 31;
                            } else if (articleItem == 'г') {
                                articleItemId = 33;
                            } else if (articleItem == 'д') {
                                articleItemId = 34;
                            }
                        } else if (article.contains("17")) {
                            if (articleItem == 'а') {
                                articleItemId = 9;
                            } else if (articleItem == 'б') {
                                articleItemId = 10;
                            } else if (articleItem == 'в') {
                                articleItemId = 11;
                            } else if (articleItem == 'г') {
                                articleItemId = 12;
                            } else if (articleItem == 'д') {
                                articleItemId = 13;
                            }
                        } else if (article.contains("18")) {
                            if (articleItem == 'а') {
                                articleItemId = 16;
                            } else if (articleItem == 'б') {
                                articleItemId = 17;
                            } else if (articleItem == 'в') {
                                articleItemId = 18;
                            } else if (articleItem == 'г') {
                                articleItemId = 19;
                            } else if (articleItem == 'д') {
                                articleItemId = 20;
                            } else if (articleItem == 'е') {
                                articleItemId = 21;
                            }
                        } else if (article.contains("19")) {
                            if (articleItem == 'а') {
                                articleItemId = 23;
                            } else if (articleItem == 'б') {
                                articleItemId = 24;
                            } else if (articleItem == 'в') {
                                articleItemId = 25;
                            } else if (articleItem == 'г') {
                                articleItemId = 26;
                            } 
                        }
                    }
                }
            }
            System.out.println("Article item is: " + articleItemId);
            if (articleItemId != null) {
                record.setArticleItemId(articleItemId);
                dp.saveRegprofQualificationExamination(record);
            } else {
                message.add(appId + " has null article item");
            }
            
        }
        System.out.println(message);
    }
    
    public static void main(String[] args) throws IOException, ParseException {
        ArticleItemFix object = new ArticleItemFix();
        object.fixArticleItems();

    }

}
