package com.mapInterfaceService.model;

public class PathResult {
    private final long OptimalCost;
    private final int startNodeId;
    private final int endNodeId;
    private final long flow;
    private final long cost;
    private final long capacity;

    public PathResult(long OptimalCost, int startNodeId, int endNodeId, long flow, long cost, long capacity) {
        this.OptimalCost = OptimalCost;
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.flow = flow;
        this.cost = cost;
        this.capacity = capacity;
    }

    public int getStartNodeId() {
        return startNodeId;
    }

    public int getEndNodeId() {
        return endNodeId;
    }

    public long getFlow() {
        return flow;
    }

    public long getCost() {
        return cost;
    }

    public long getCapacity() {
        return capacity;
    }

    public long getOptimalCost() {
        return OptimalCost;
    }
}
