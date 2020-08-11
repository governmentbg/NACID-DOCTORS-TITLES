package com.nacid.bl.impl.ras.objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * User: ggeorgiev
 * Date: 22.10.2019 г.
 * Time: 18:53
 */
public class Dissertation {
    private String title;//tema na BG
    private String titleAlt; //tema na drug ezik
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Europe/Sofia")
    private Date dateOfAcquire;//data na zashtita
    private Integer numberOfBibliography;//bibliografiq
    private Integer languageId;//ezik
    private Integer numberOfPages;
    private String annotation;//anotaciq
    private String annotationAlt;//anotaciq na anglijski
    private boolean dissertationIsNotDeposited; //Дисертацията не подлежи на депозиране в НАЦИД


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleAlt() {
        return titleAlt;
    }

    public void setTitleAlt(String titleAlt) {
        this.titleAlt = titleAlt;
    }

    public Date getDateOfAcquire() {
        return dateOfAcquire;
    }

    public void setDateOfAcquire(Date dateOfAcquire) {
        this.dateOfAcquire = dateOfAcquire;
    }

    public Integer getNumberOfBibliography() {
        return numberOfBibliography;
    }

    public void setNumberOfBibliography(Integer numberOfBibliography) {
        this.numberOfBibliography = numberOfBibliography;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getAnnotationAlt() {
        return annotationAlt;
    }

    public void setAnnotationAlt(String annotationAlt) {
        this.annotationAlt = annotationAlt;
    }

    public boolean isDissertationIsNotDeposited() {
        return dissertationIsNotDeposited;
    }

    public void setDissertationIsNotDeposited(boolean dissertationIsNotDeposited) {
        this.dissertationIsNotDeposited = dissertationIsNotDeposited;
    }
}
