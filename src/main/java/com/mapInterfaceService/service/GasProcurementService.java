package com.mapInterfaceService.service;

import com.mapInterfaceService.algorithm.PathFinder;
import com.mapInterfaceService.data.DataLoader;
import com.mapInterfaceService.model.Model;
import com.mapInterfaceService.model.PathResult;
import com.mapInterfaceService.output.ResultPrinter;
import com.mapInterfaceService.utils.ResultDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class GasProcurementService {

    private final Model model;

    public GasProcurementService(DataLoader dataLoader) {
        this.model = dataLoader.loadData();
    }

    public ResultDTO<List<Map<String, Object>>> calculatePath(String dateStr, Map<Integer, Integer> demandNodes, Set<Integer> specifiedSupplyNodes, Logger logger) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int month = date.getMonthValue();

        PathFinder pathFinder = new PathFinder(model, month, demandNodes, specifiedSupplyNodes, logger);
        List<PathResult> pathResults = pathFinder.solve();

        ResultPrinter resultPrinter = new ResultPrinter(model);
        return resultPrinter.printResults(pathResults);
    }
}
