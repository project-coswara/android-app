package com.project.coswara.model;

import java.io.Serializable;
import java.util.HashMap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HealthData implements Serializable {
    private String currentStatus;
    private final HashMap<String, Boolean> healthMap = new HashMap<>();

    public void updateMap(String key, Boolean value){
        this.healthMap.put(key, value);
    }

    public boolean isComplete(){
        return currentStatus != null && !currentStatus.isEmpty();
    }
}
