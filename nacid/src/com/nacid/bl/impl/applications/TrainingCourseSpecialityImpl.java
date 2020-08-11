package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.TrainingCourseSpeciality;

/**
 * Created by georgi.georgiev on 04.05.2015.
 */
public class TrainingCourseSpecialityImpl implements TrainingCourseSpeciality{
    private int trainingCourseId;
    private int specialityId;
    private Integer originalSpecialityId;

    public TrainingCourseSpecialityImpl(int trainingCourseId, int specialityId, Integer originalSpecialityId) {
        this.trainingCourseId = trainingCourseId;
        this.specialityId = specialityId;
        this.originalSpecialityId = originalSpecialityId;
    }

    public int getTrainingCourseId() {
        return trainingCourseId;
    }

    public int getSpecialityId() {
        return specialityId;
    }

    public Integer getOriginalSpecialityId() {
        return originalSpecialityId;
    }
}
