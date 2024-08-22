package com.mapInterfaceService.output;

import com.mapInterfaceService.model.Line;
import com.mapInterfaceService.model.Model;
import com.mapInterfaceService.model.PathResult;
import com.mapInterfaceService.utils.GeometryUtils;
import com.mapInterfaceService.utils.ResultDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultPrinter {

    private static final Logger logger = LoggerFactory.getLogger(ResultPrinter.class);
    private final Model model;



    public ResultPrinter(Model model) {
        this.model = model;
    }

    /**
     * 将给定的路径结果列表格式化为ResultDTO结构。
     * @param pathResults 路径结果的列表。
     * @return 格式化的ResultDTO结构。
     */
    public ResultDTO<List<Map<String, Object>>> printResults(List<PathResult> pathResults) {
        if (pathResults == null || pathResults.isEmpty()) {
            return ResultDTO.fail("B0001", "No path results found.");
        }

        List<Map<String, Object>> dataArray = new ArrayList<>();
        for (PathResult pathResult : pathResults) {
            Map<String, Object> pathJson = formatPathResult(pathResult);
            if (!pathJson.isEmpty()) {
                dataArray.add(pathJson);
            }
        }
        ResultDTO<List<Map<String, Object>>> resultDTO = ResultDTO.success(dataArray);

        return resultDTO;
    }

    /**
     * 将单个路径结果格式化为JSON对象。
     * @param result 单个路径结果对象。
     * @return 格式化的JSON对象。
     */
    private Map<String, Object> formatPathResult(PathResult result) {
        Map<String, Object> pathJson = new HashMap<>();

        Line line = model.findLineByStartEnd(result.getStartNodeId(), result.getEndNodeId());
        if (line == null) {
            line = model.findLineByStartEnd(result.getEndNodeId(), result.getStartNodeId());
        }
        if (line != null) {
            String coordinatePairs = GeometryUtils.convertGeometryToCoordinatePairs(line.getGeometry());
//            String wkt = GeometryUtils.convertGeometryToWKT(line.getGeometry());

            pathJson.put("path_id", 1);
            pathJson.put("gxbm", trimString(line.getLineCode())); // 管线编码
            pathJson.put("geom", coordinatePairs); // 几何信息
            pathJson.put("gxmc", trimString(line.getLineName())); // 管线名称
            pathJson.put("gxlx", trimString(line.getGasLineType())); // 管线类型
            pathJson.put("leng", line.getLength() * 0.001); // 管线长度（KM）
            pathJson.put("sygr", result.getCapacity()); // 剩余管容（管线的天然气运输上限）（万m³）
            pathJson.put("flow", result.getFlow()); // 流量（本次计算中，流过这条管线的天然气数量）（万m³）
            pathJson.put("spjg", line.getUnitPrice()); // 运输成本（元/万m³）
            pathJson.put("cost", result.getCost()); // 运输费用（本次计算中，流过该管线需要的费用）（元）
            pathJson.put("optimalCost", result.getOptimalCost()); // 总成本（购买 + 运输）
        }

        return pathJson;
    }

    /**
     * 辅助方法，用于去除字符串前后的空格。
     * @param value 需要去除空格的字符串。
     * @return 去除空格后的字符串，或null（如果输入为null）。
     */
    private String trimString(String value) {
        return value != null ? value.trim() : null;
    }
}
