package com.mapInterfaceService.utils;

import java.util.List;
import java.util.Map;

public class RequestDTO {
    private String date;
    private Map<String, Integer> demandNodes;
    private List<Integer> supplyNodes;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Integer> getDemandNodes() {
        return demandNodes;
    }

    public void setDemandNodes(Map<String, Integer> demandNodes) {
        this.demandNodes = demandNodes;
    }

    public List<Integer> getSupplyNodes() {
        return supplyNodes;
    }

    public void setSupplyNodes(List<Integer> supplyNodes) {
        this.supplyNodes = supplyNodes;
    }
}
