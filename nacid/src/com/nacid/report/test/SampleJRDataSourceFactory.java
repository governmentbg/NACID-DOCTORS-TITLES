package com.nacid.report.test;

import java.util.*;
import com.nacid.report.test.bean.*;


public class SampleJRDataSourceFactory {

 /**
  * Put here collection with test beans
  * @return
  */
    public static Collection createBeanCollection() {

        ArrayList coll = new ArrayList();
        Application test1 = new Application();
        test1.setApplicantName("Пешо Пешев Пешев");
        test1.setDate("14.06.2010");
        test1.setApplicationNumber("1/14.06.2010");
        test1.setCivilId("ЕГН 1010101010");
        test1.setSpecialityName("Медицина");
        test1.setCommissionSessionDate("20.08.2010");
        test1.setApplicantLastName("Пешев");
        test1.setAddressDetailsBulgaria("пк. 1000,  ул. Стефан Тошев 1");
        test1.setCityDetailsBulgaria("София");
        test1.setTrainingCourseLocation("Лондон, Англия");
        test1.setNacidDirectorName("Ваня Грашки");
        test1.setAcknowledgedEducationLevel("магистър");
        test1.setAcknowledgedSpecialityName("Автоматика");
        test1.setBirthPlace("Тутракан");
        test1.setCitizenship("Силистра");
        test1.setTrainingCourseQualification(" бакалавър ");
        test1.setDiplomaEducationLevel("магистър");
        test1.setDiplomaSpecialityNames("Автоматика");
        test1.setPreviousEducation("Средно образование:ПМГ Иван Вазов, ДобричСредно образование:ПМГ Иван Вазов, ДобричСредно образование:ПМГ Иван Вазов, ДобричСредно образование:ПМГ Иван Вазов, ДобричСредно образование:ПМГ Иван Вазов, ДобричСредно образование:ПМГ Иван Вазов, ДобричСредно образование:ПМГ Иван Вазов, ДобричСредно образование:ПМГ Иван Вазов, ДобричСредно образование:ПМГ Иван Вазов, ДобричСредно образование:ПМГ Иван Вазов, ДобричСредно образование:ПМГ Иван Вазов, ДобричСредно образование:ПМГ Иван Вазов, Добрич");


        //TEST for collection
        Application test2 = new Application();
        test2.setApplicantName("Ivan iavanov");
        test2.setDate("14.06.2010");
        test2.setApplicationNumber("1/14.06.2010");
        test2.setCivilId("ЕГН 1010101010");
        test2.setSpecialityName("Медицина");
        test2.setCommissionSessionDate("20.08.2010");
        test2.setApplicantLastName("Пешев");
        test2.setAddressDetailsBulgaria("пк. 1000,  ул. Стефан Тошев 1");
        test2.setCityDetailsBulgaria("София");
        test2.setTrainingCourseLocation("Лондон, Англия");

        ArrayList beanArrays = new ArrayList();
        for (int i=0; i<1; i++){
            beanArrays.add(test2);
        }
        test1.setCollTest(beanArrays);

        for (int i=0; i<1; i++) {
            coll.add(test1);
        }



         return coll;
    }

}
