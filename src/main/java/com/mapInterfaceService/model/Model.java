package com.mapInterfaceService.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Model {
    private List<Node> nodes;
    private List<Line> lines;

    public Node findNodeById(int nodeId) {
        return nodes.stream()
                .filter(node -> node.getNodeId() == nodeId)
                .findFirst()
                .orElse(null);
    }

    public Line findLineByStartEnd(int startNodeId, int endNodeId) {
        return lines.stream()
                .filter(line -> (line.getStartNode() == startNodeId && line.getEndNode() == endNodeId))
                .findFirst()
                .orElse(null);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }


}