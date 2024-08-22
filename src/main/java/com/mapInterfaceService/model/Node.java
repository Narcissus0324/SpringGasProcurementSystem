package com.mapInterfaceService.model;

public class Node {
    private int nodeId;
    private String nodeName;
    private double gasSitePrice;  // 单位：元/立方米
    private int siteType;
    private String siteClass;
    private double gasSupply;  // 单位：万立方米

    private String gasSourceParty;
    private String gasReceiveUnit;
    private double gasExportCapacity;
    private double gasHourFlow;
    private String contacts;
    private String contactNumber;
    private double areaCovered;
    private double expansionCapacity;
    private double importPipeDiameter;
    private double exportPipeDiameter;
    private double importPressure;
    private double exportPressure;


    public boolean isSupplyNode() {
        return this.gasExportCapacity > 0;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public double getGasPrice() {
        return gasSitePrice;
    } //单位：元/m³

    public void setGasPrice(double gasSitePrice) {
        this.gasSitePrice = gasSitePrice;
    }

    public double getGasSupply() {
        return gasSupply;
    }

    public void setGasSupply(double gasSupply) {
        this.gasSupply = gasSupply;
    }

    public int getSiteType() {
        return siteType;
    }

    public void setSiteType(int siteType) {
        this.siteType = siteType;
    }

    public String getSiteClass() {
        return siteClass;
    }

    public void setSiteClass(String siteClass) {
        this.siteClass = siteClass;
    }

    public String getGasSourceParty() {
        return gasSourceParty;
    }

    public void setGasSourceParty(String gasSourceParty) {
        this.gasSourceParty = gasSourceParty;
    }

    public String getGasReceiveUnit() {
        return gasReceiveUnit;
    }

    public void setGasReceiveUnit(String gasReceiveUnit) {
        this.gasReceiveUnit = gasReceiveUnit;
    }

    public double getGasExportCapacity() {
        return gasExportCapacity;
    }

    public void setGasExportCapacity(double gasExportCapacity) {
        this.gasExportCapacity = gasExportCapacity;
    }

    public double getGasHourFlow() {
        return gasHourFlow;
    }

    public void setGasHourFlow(double gasHourFlow) {
        this.gasHourFlow = gasHourFlow;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public double getAreaCovered() {
        return areaCovered;
    }

    public void setAreaCovered(double areaCovered) {
        this.areaCovered = areaCovered;
    }

    public double getExpansionCapacity() {
        return expansionCapacity;
    }

    public void setExpansionCapacity(double expansionCapacity) {
        this.expansionCapacity = expansionCapacity;
    }

    public double getImportPipeDiameter() {
        return importPipeDiameter;
    }

    public void setImportPipeDiameter(double importPipeDiameter) {
        this.importPipeDiameter = importPipeDiameter;
    }

    public double getExportPipeDiameter() {
        return exportPipeDiameter;
    }

    public void setExportPipeDiameter(double exportPipeDiameter) {
        this.exportPipeDiameter = exportPipeDiameter;
    }

    public double getImportPressure() {
        return importPressure;
    }

    public void setImportPressure(double importPressure) {
        this.importPressure = importPressure;
    }

    public double getExportPressure() {
        return exportPressure;
    }

    public void setExportPressure(double exportPressure) {
        this.exportPressure = exportPressure;
    }
}
