package com.project.coswara.model;

import java.io.Serializable;
import java.util.HashMap;

public class HealthData implements Serializable {
    private String currentStatus;
    private final HashMap<String, Boolean> healthMap = new HashMap<>();

    public HealthData() {
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public HashMap<String, Boolean> getHealthMap() {
        return healthMap;
    }

    public void updateMap(String key, Boolean value){
        this.healthMap.put(key, value);
    }

    public boolean isComplete(){
        return currentStatus != null && !currentStatus.isEmpty();
    }
}
