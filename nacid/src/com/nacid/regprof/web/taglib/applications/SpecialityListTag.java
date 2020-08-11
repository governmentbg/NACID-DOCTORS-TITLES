package com.nacid.regprof.web.taglib.applications;

import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.Speciality;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.List;

//RayaWritten------------------------------------------
public class SpecialityListTag extends SimpleTagSupport{
    private List<Speciality> specialities;
    private List<FlatNomenclature> originalSpecialities;

    public void setSpecialities(List<Speciality> specialities) {
        this.specialities = specialities;
    }

    public void setOriginalSpecialities(List<FlatNomenclature> originalSpecialities) {
        this.originalSpecialities = originalSpecialities;
    }

    @Override
    public void doTag()throws JspException, IOException {

        try{
            if (specialities != null) {
                for (int i = 0; i < specialities.size(); i++) {
                    Speciality s = specialities.get(i);
                    FlatNomenclature originalSpeciality = originalSpecialities == null ? null : originalSpecialities.get(i);
                    getJspContext().setAttribute("specialityName", s.getName() + (originalSpeciality == null ? "" : " / "+ originalSpeciality.getName()));
                    getJspContext().setAttribute("specialityId", s.getId());
                    getJspContext().setAttribute("originalSpecialityId", originalSpeciality == null ? "" : originalSpeciality.getId());
                    getJspContext().setAttribute("cnt", i+1);
                    getJspBody().invoke(null);
                }
            }

            for(Speciality s: specialities){

        }    
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
//-----------------------------------------------------
