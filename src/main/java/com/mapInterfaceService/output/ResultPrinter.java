package com.mapInterfaceService.output;

import com.mapInterfaceService.model.Line;
import com.mapInterfaceService.model.Model;
import com.mapInterfaceService.model.PathResult;
import com.mapInterfaceService.utils.ResultDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultPrinter {

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
            return ResultDTO.fail("ResultDTO.fail", "No path results found.");
        }

        List<Map<String, Object>> dataArray = new ArrayList<>();
        for (PathResult pathResult : pathResults) {
            Map<String, Object> pathJson = formatPathResult(pathResult);
            if (!pathJson.isEmpty()) {
                dataArray.add(pathJson);
            }
        }

        return ResultDTO.success(dataArray);
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
            pathJson.put("gxmc", trimString(line.getLineName()));
            pathJson.put("gxlx", trimString(line.getGasLineType()));
            pathJson.put("leng", (int)line.getLength());
            pathJson.put("sygr", result.getCapacity());
            pathJson.put("flow", result.getFlow());
            pathJson.put("spjg", line.getUnitPrice());
            pathJson.put("cost", result.getCost());
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
