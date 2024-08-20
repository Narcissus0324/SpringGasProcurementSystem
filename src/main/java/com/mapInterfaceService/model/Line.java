package com.mapInterfaceService.model;

import java.util.List;

public class Line {
    private int lineId;
    private int startNode;
    private int endNode;
    private double length;  // 单位：KM
    private String lineName;
    private String lineDetailName;
    private double unitPrice;  // 单位：元/千立方米·公里

    private double diameter;
    private double designPressure;
    private String pressureLevel;
    private String mainGasSource;
    private double designCapacity;
    private String commissioningDate;
    private String gasLineType;
    private String gasLineStatus;
    private List<MonthlySurplusCapacity> monthlyCapacities; // 单位：万立方米

    public double getUnitCost() {
        return unitPrice * length * 10; // 单位：元/万立方米
    }

    public int getCapacityForMonth(int month) {
        for (MonthlySurplusCapacity capacity : monthlyCapacities) {
            if (capacity.getMonth() == month) {
                return (int) capacity.getSurplusCapacity();
            }
        }
        return 0;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getStartNode() {
        return startNode;
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }

    public int getEndNode() {
        return endNode;
    }

    public void setEndNode(int endNode) {
        this.endNode = endNode;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getLineDetailName() {
        return lineDetailName;
    }

    public void setLineDetailName(String lineDetailName) {
        this.lineDetailName = lineDetailName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public double getDesignPressure() {
        return designPressure;
    }

    public void setDesignPressure(double designPressure) {
        this.designPressure = designPressure;
    }

    public String getPressureLevel() {
        return pressureLevel;
    }

    public void setPressureLevel(String pressureLevel) {
        this.pressureLevel = pressureLevel;
    }

    public String getMainGasSource() {
        return mainGasSource;
    }

    public void setMainGasSource(String mainGasSource) {
        this.mainGasSource = mainGasSource;
    }

    public double getDesignCapacity() {
        return designCapacity;
    }

    public void setDesignCapacity(double designCapacity) {
        this.designCapacity = designCapacity;
    }

    public String getCommissioningDate() {
        return commissioningDate;
    }

    public void setCommissioningDate(String commissioningDate) {
        this.commissioningDate = commissioningDate;
    }

    public String getGasLineType() {
        return gasLineType;
    }

    public void setGasLineType(String gasLineType) {
        this.gasLineType = gasLineType;
    }

    public String getGasLineStatus() {
        return gasLineStatus;
    }

    public void setGasLineStatus(String gasLineStatus) {
        this.gasLineStatus = gasLineStatus;
    }

    public List<MonthlySurplusCapacity> getMonthlyCapacities() {
        return monthlyCapacities;
    }

    public void setMonthlyCapacities(List<MonthlySurplusCapacity> monthlyCapacities) {
        this.monthlyCapacities = monthlyCapacities;
    }
}


