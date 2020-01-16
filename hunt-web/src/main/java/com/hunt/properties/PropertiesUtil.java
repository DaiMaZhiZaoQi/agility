package com.hunt.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class PropertiesUtil{
	 @Value("#{configProperties['record.recordFilePath']}")   
	private String recordFilePath; 
	 @Value("#{configProperties['csv.csvFilePath']}")
	 private String csvFilePath;

	public String getRecordFilePath() {   
		return recordFilePath;
	}

	public void setRecordFilePath(String recordFilePath) {
		this.recordFilePath = recordFilePath;
	}

	public String getCsvFilePath() {
		return csvFilePath;
	}

	public void setCsvFilePath(String csvFilePath) {
		this.csvFilePath = csvFilePath;
	}
	 
	
	
}
