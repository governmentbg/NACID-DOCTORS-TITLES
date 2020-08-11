package com.nacid.bl.impl.academicrecognition;

public class BGAcademicRecognitionExtendedImpl extends BGAcademicRecognitionInfoImpl {
    private String recognizedUniversityName;
    private String recognitionStatusName;

    public String getRecognizedUniversityName() {
        return recognizedUniversityName;
    }

    public void setRecognizedUniversityName(String recognizedUniversityName) {
        this.recognizedUniversityName = recognizedUniversityName;
    }
    
    

    public String getRecognitionStatusName() {
		return recognitionStatusName;
	}

	public void setRecognitionStatusName(String recognitionStatusName) {
		this.recognitionStatusName = recognitionStatusName;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BGAcademicRecognitionExtendedImpl [recognizedUniversityName=").append(recognizedUniversityName).append(", recognitionStatusName=")
        .append(recognitionStatusName)
        .append(", toString()=")
                .append(super.toString()).append("]");
        return builder.toString();
    }

    
    
}
