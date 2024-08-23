package com.mapInterfaceService.algorithm;

import com.google.ortools.Loader;
import com.google.ortools.graph.MaxFlow;
import com.google.ortools.graph.MinCostFlow;
import com.google.ortools.graph.MinCostFlowBase;
import com.mapInterfaceService.model.Model;
import com.mapInterfaceService.model.Node;
import com.mapInterfaceService.model.Line;
import com.mapInterfaceService.model.PathResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Logger;

public class PathFinder {
    private final Logger logger;
    private final Model model;
    private final int month;
    private final Map<Integer, Integer> demandNodes;
    private final Set<Integer> specifiedSupplyNodes;
    private final List<String> libPaths;

    public PathFinder(Model model, int month, Map<Integer, Integer> demandNodes, Set<Integer> specifiedSupplyNodes, Logger logger, List<String> libPaths) {
        this.model = model;
        this.month = month;
        this.demandNodes = demandNodes;
        this.specifiedSupplyNodes = specifiedSupplyNodes;
        this.logger = logger;
        this.libPaths = libPaths;
        loadLibraries();
    }

    private void loadLibraries() {
        try {
            for (String libPath : libPaths) {
                loadLibrary(libPath);
            }
        } catch (Exception e) {
            Logger.getLogger(PathFinder.class.getName()).severe("加载 ORTools 本地库失败: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void loadLibrary(String libPath) throws Exception {
        String fileName = Paths.get(libPath).getFileName().toString();

        InputStream inputStream = PathFinder.class.getResourceAsStream(libPath);
        File tempDll = File.createTempFile(fileName, null);
        tempDll.deleteOnExit();
        assert inputStream != null;
        Files.copy(inputStream, tempDll.toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.load(tempDll.getAbsolutePath());
    }

    // 求解最大流
    private List<PathResult> findMaxFlowSolution() {
        MaxFlow maxFlow = new MaxFlow();
        Map<Integer, Integer> nodeSupplyMap = new HashMap<>(); // 记录节点的供应量
        List<String> arcInfoList = new ArrayList<>(); // 记录弧线信息 (起点，终点，容量)

        // 找到现有节点ID的最大值
        int maxNodeId = model.getNodes().stream()
                .mapToInt(Node::getNodeId)
                .max()
                .orElse(0);

        int superSourceId = maxNodeId + 1; // 超级源点ID
        int superSinkId = superSourceId + 1; // 超级汇点ID

        // 连接指定的供应节点到超级源点
        if (specifiedSupplyNodes == null) {
            model.getNodes().stream()
                    .filter(node -> !demandNodes.containsKey(node.getNodeId())) // 过滤掉指定的需求节点
                    .forEach(node -> {
                        int gasSupply = (node.getGasSupply() == 0) ? 1000 : (int) node.getGasSupply();
                        maxFlow.addArcWithCapacity(superSourceId, node.getNodeId(), gasSupply);

                        nodeSupplyMap.put(node.getNodeId(), gasSupply); // 记录节点供应量
                        arcInfoList.add("起点: " + superSourceId + " -> 终点: " + node.getNodeId() + "，容量: " + gasSupply); // 记录弧线信息
                    });
        } else {
            specifiedSupplyNodes.forEach(supplyNodeId -> {
                Node node = model.findNodeById(supplyNodeId);
                if (node != null) {
                    int gasSupply = (node.getGasSupply() == 0) ? 1000 : (int) node.getGasSupply();
                    maxFlow.addArcWithCapacity(superSourceId, supplyNodeId, gasSupply);

                    nodeSupplyMap.put(supplyNodeId, gasSupply); // 记录节点供应量
                    arcInfoList.add("起点: " + superSourceId + " -> 终点: " + supplyNodeId + "，容量: " + gasSupply); // 记录弧线信息
                }
            });
        }

        // 连接需求节点到超级汇点
        demandNodes.forEach((nodeId, demand) -> {
            Node node = model.findNodeById(nodeId);
            if (node != null) {
                maxFlow.addArcWithCapacity(nodeId, superSinkId, demand);

                nodeSupplyMap.put(nodeId, -demand); // 记录节点需求量（负值表示需求）
                arcInfoList.add("起点: " + nodeId + " -> 终点: " + superSinkId + "，容量: " + demand); // 记录弧线信息
            }
        });

        // 添加模型中所有的线路到最大流计算中
        for (Line line : model.getLines()) {
            int capacity = (line.getCapacityForMonth(month) == 0) ? 5000 : line.getCapacityForMonth(month);
            maxFlow.addArcWithCapacity(line.getStartNode(), line.getEndNode(), capacity);
            maxFlow.addArcWithCapacity(line.getEndNode(), line.getStartNode(), capacity);

            arcInfoList.add("起点: " + line.getStartNode() + " -> 终点: " + line.getEndNode() + "，容量: " + capacity); // 记录弧线信息
        }

        // 输出已记录的网络信息
/*
        logger.info("网络创建完成，输出所有节点和弧线信息：");
        nodeSupplyMap.forEach((nodeId, supply) -> logger.info("节点ID: " + nodeId + "，节点容量: " + supply)); // 输出节点的信息
        for (String arcInfo : arcInfoList) {
            logger.info(arcInfo);
        }
*/

        MaxFlow.Status status = maxFlow.solve(superSourceId, superSinkId); // 求解最大流

        List<PathResult> maxFlowResults = new ArrayList<>();
        if (status == MaxFlow.Status.OPTIMAL) {
            logger.info("最大流求解成功，最大流量为: " + maxFlow.getOptimalFlow());
            for (int i = 0; i < maxFlow.getNumArcs(); ++i) {
                long flow = maxFlow.getFlow(i);
                long capacity = maxFlow.getCapacity(i);
                if (flow > 0) {
                    int startNode = maxFlow.getTail(i);
                    int endNode = maxFlow.getHead(i);
                    if (startNode < superSourceId && endNode < superSourceId) {
                        logger.info("线路 " + startNode + " -> " + endNode + " 的流量: " + flow + " ,容量: " + capacity);
                        PathResult result = new PathResult(0, startNode, endNode, flow, 0, capacity);
                        maxFlowResults.add(result);
                    }
                }
            }
        } else {
            logger.warning("求解网络最大流失败，状态: " + status);
        }
        return maxFlowResults;
    }

    // 求解模型
    public List<PathResult> solve() {
        MinCostFlow minCostFlow = new MinCostFlow();
        List<PathResult> pathResults = new ArrayList<>();
        Map<Integer, Integer> nodeSupplyMap = new HashMap<>();  // 用于记录节点的供应量 (点，供应量）
        List<String> arcInfoList = new ArrayList<>();   // 用于记录弧线信息 (起点，终点，容量，单位成本)

        // 找到现有节点ID的最大值，只执行一次
        int maxNodeId = model.getNodes().stream()
                .mapToInt(Node::getNodeId)
                .max()
                .orElse(0);

        int superSinkNodeId = maxNodeId + 1;  // 超级汇点的ID

        // 总供给量和总需求量
        int totalSupply = 0;
        int totalDemand = demandNodes.values().stream().mapToInt(Integer::intValue).sum();
        logger.info("总需求量: " + totalDemand);

        for (Node node : model.getNodes()) {
            if ((specifiedSupplyNodes == null || specifiedSupplyNodes.contains(node.getNodeId()))&& !demandNodes.containsKey(node.getNodeId())) {
                int gasSupply = (node.getGasSupply() == 0) ? 1000 : (int) node.getGasSupply();
                totalSupply += gasSupply;
            }
        }

        // 判断供给是否小于需求
        if (totalSupply < totalDemand) {
            logger.info("总供给小于总需求，将计算最大流以确定最大可能满足的需求量。");
            return findMaxFlowSolution();
        }

        boolean needSuperSink = totalSupply > totalDemand;

        if (needSuperSink) {
            minCostFlow.setNodeSupply(superSinkNodeId, totalDemand - totalSupply);
            nodeSupplyMap.put(superSinkNodeId, totalDemand - totalSupply);
        }

        final int[] extraNodeId = {maxNodeId + 2};    // 初始化 extraNodeId

        // 设置额外节点
        model.getNodes().stream()
                .filter(node ->
                        (specifiedSupplyNodes == null || specifiedSupplyNodes.isEmpty() || specifiedSupplyNodes.contains(node.getNodeId()))
                                && !demandNodes.containsKey(node.getNodeId())) // 过滤指定需求节点
                .forEach(node -> {
                    int currentExtraNodeId = extraNodeId[0]++;  // 使用当前的 extraNodeId，并递增
                    int gasSupply = (node.getGasSupply() == 0) ? 1000 : (int) node.getGasSupply();
                    double gasPrice = 10000 * ((node.getGasPrice() == 0.0) ? 2.5 : node.getGasPrice()); // 单位：元/万m³
                    minCostFlow.addArcWithCapacityAndUnitCost(currentExtraNodeId, node.getNodeId(), gasSupply, (int) gasPrice);
                    minCostFlow.setNodeSupply(currentExtraNodeId, gasSupply);
                    nodeSupplyMap.put(currentExtraNodeId, gasSupply);
                    arcInfoList.add("起点: " + currentExtraNodeId + " -> 终点: " + node.getNodeId() + "，容量: " + gasSupply + "，单位成本: " + (int)gasPrice);

                    if (needSuperSink) {
                        minCostFlow.addArcWithCapacityAndUnitCost(currentExtraNodeId, superSinkNodeId, gasSupply, 0);  // 从额外节点连接到超级汇点
                        arcInfoList.add("起点: " + currentExtraNodeId + " -> 终点: " + superSinkNodeId + "，容量: " + gasSupply + "，单位成本: 0");
                    }
                    minCostFlow.setNodeSupply(node.getNodeId(), 0); // 设置原始供应节点的供应量为0
                    nodeSupplyMap.put(node.getNodeId(), 0);
                });

        // 设置边
        for (Line line : model.getLines()) {
            int capacity = (line.getCapacityForMonth(month) == 0) ? 5000 : line.getCapacityForMonth(month);
            minCostFlow.addArcWithCapacityAndUnitCost(line.getStartNode(), line.getEndNode(), capacity, (int) (line.getUnitCost()));
            minCostFlow.addArcWithCapacityAndUnitCost(line.getEndNode(), line.getStartNode(), capacity, (int) (line.getUnitCost()));

            // 记录弧线信息
            String arcInfo = "起点: " + line.getStartNode() + " -> 终点: " + line.getEndNode() +
                    "，容量: " + capacity + "，单位成本: " + (int)line.getUnitCost();
            arcInfoList.add(arcInfo);
        }

        demandNodes.forEach((nodeId, demand) -> {
            minCostFlow.setNodeSupply(nodeId, -demand);
            nodeSupplyMap.put(nodeId, -demand);
            logger.info("设置需求节点 " + nodeId + " 的需求量为: " + demand);
        });

        // 输出所有节点的信息
/*
        logger.info("网络创建完成，输出所有节点信息：");
        nodeSupplyMap.forEach((nodeId, supply) -> logger.info("节点ID: " + nodeId + "，节点容量: " + supply));

        // 输出所有线路的信息
        logger.info("网络创建完成，输出所有线路信息：");
        for (String arcInfo : arcInfoList) {
            logger.info(arcInfo);
        }
*/

        MinCostFlowBase.Status status = minCostFlow.solve();
        if (status == MinCostFlow.Status.OPTIMAL) {
            long OptimalCost = minCostFlow.getOptimalCost();
            logger.info("最小成本流求解成功,成本为: " + OptimalCost);
            for (int i = 0; i < minCostFlow.getNumArcs(); ++i) {
                long flow = minCostFlow.getFlow(i);
                if (flow > 0) {
                    int startNode = minCostFlow.getTail(i);
                    int endNode = minCostFlow.getHead(i);
                    long cost = minCostFlow.getFlow(i) * minCostFlow.getUnitCost(i);
                    long capacity = minCostFlow.getCapacity(i);
                    if (startNode < superSinkNodeId && endNode < superSinkNodeId) {
                        logger.info("线路 " + startNode + " -> " + endNode + " 的流量: " + flow + "，成本: " + cost + "，容量: " + capacity);
                        PathResult result = new PathResult(OptimalCost, startNode, endNode, flow, cost, capacity);
                        pathResults.add(result);
                    }
                }
            }
            return pathResults;
        } else {
            logger.warning("求解最小成本流问题失败，转向计算最大流。");
            logger.warning("求解器状态: " + status);
            return findMaxFlowSolution();  // 失败时求解最大流
        }
    }
}
