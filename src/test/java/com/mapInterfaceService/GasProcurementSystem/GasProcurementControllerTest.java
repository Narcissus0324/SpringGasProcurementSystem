package com.mapInterfaceService.GasProcurementSystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapInterfaceService.utils.RequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GasProcurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCalculatePath() throws Exception {
        // 创建请求DTO
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setDate("2024-03-01");

        Map<String, Integer> demandNodes = new HashMap<>();
        demandNodes.put("50", 50);
        demandNodes.put("61", 50);
        requestDTO.setDemandNodes(demandNodes);

//        List<Integer> supplyNodes = List.of(545, 1123);
//        requestDTO.setSupplyNodes(supplyNodes);

        // 将DTO转换为JSON字符串
        String requestJson = objectMapper.writeValueAsString(requestDTO);

        MvcResult result = mockMvc.perform(post("/map/data/routing")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        // 将响应保存到文件中
        File outputFile = new File("response.json");
        objectMapper.writeValue(outputFile, objectMapper.readTree(jsonResponse));
    }
}
