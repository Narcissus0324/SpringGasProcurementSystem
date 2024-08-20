package com.mapInterfaceService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapInterfaceService.service.GasProcurementService;
import com.mapInterfaceService.utils.RequestDTO;
import com.mapInterfaceService.utils.ResultDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/map/data")
public class GasProcurementController {

    private final GasProcurementService service;
    private final ObjectMapper objectMapper;

    private static final Logger logger = Logger.getLogger(GasProcurementController.class.getName());

    public GasProcurementController(GasProcurementService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/routing")
    public ResponseEntity<ResultDTO<List<Map<String, Object>>>> calculate(@RequestBody RequestDTO requestDTO) {
        try {
            String requestJson = objectMapper.writeValueAsString(requestDTO);
            logger.info("Received request: " + requestJson);

            String date = requestDTO.getDate();

            Map<String, Integer> rawDemandNodes = requestDTO.getDemandNodes();
            Map<Integer, Integer> demandNodes = new HashMap<>();
            for (Map.Entry<String, Integer> entry : rawDemandNodes.entrySet()) {
                demandNodes.put(Integer.parseInt(entry.getKey()), entry.getValue());
            }

            List<Integer> supplyNodesList = requestDTO.getSupplyNodes();
            Set<Integer> supplyNodes = supplyNodesList != null ? new HashSet<>(supplyNodesList) : null;

            ResultDTO<List<Map<String, Object>>> response = service.calculatePath(date, demandNodes, supplyNodes, logger);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (JsonProcessingException e) {
            logger.severe("Error processing JSON request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultDTO.fail("B0001", "JSON processing error: " + e.getMessage()));
        } catch (Exception e) {
            logger.severe("Error occurred during routing calculation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultDTO.fail("B0001", "error: " + e.getMessage()));
        }
    }
}
