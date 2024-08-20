package com.mapInterfaceService.data;

import com.mapInterfaceService.model.Line;
import com.mapInterfaceService.model.Model;
import com.mapInterfaceService.model.MonthlySurplusCapacity;
import com.mapInterfaceService.model.Node;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {

    private final JdbcTemplate jdbcTemplate;

    public DataLoader(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Model loadData() {
        Model model = new Model();
        model.setNodes(loadNodes());
        model.setLines(loadLines());
        return model;
    }

    private List<Node> loadNodes() {
        List<Node> nodes = new ArrayList<>();
        String sql =
                "SELECT nbi.NODE_ID, nbi.NODE_NAME, fs.SITE_TYPE, fs.SITE_CLASS, " +
                        "fs.GAS_SOURCE_PARTY, fs.GAS_RECEIVE_UNIT, fs.GAS_EXPORT_CAPACITY, " +
                        "fs.GAS_HOUR_FLOW, fs.CONTACTS, fs.CONTACT_NUMBER, fs.AREA_COVERED, " +
                        "fs.EXPANSION_CAPACITY, fs.IMPORT_PIPE_DIAMETER, fs.EXPORT_PIPE_DIAMETER, " +
                        "fs.IMPORT_PRESSURE, fs.EXPORT_PRESSURE, gs.GAS_SITE_PRICE " +
                        "FROM SYSDBA.NODE_BASE_INFO nbi " +
                        "JOIN SYSDBA.FUNCTIONAL_SITES fs ON nbi.NODE_ID = fs.NODE_ID " +
                        "JOIN SYSDBA.Gas_Sites gs ON nbi.NODE_ID = gs.NODE_ID " +
                        "WHERE nbi.NODE_ID < 4130";

        jdbcTemplate.query(sql, rs -> {
            Node node = new Node();
            node.setNodeId(rs.getInt("NODE_ID"));
            node.setNodeName(rs.getString("NODE_NAME"));
            node.setSiteType(rs.getInt("SITE_TYPE"));
            node.setSiteClass(rs.getString("SITE_CLASS"));
            node.setGasPrice(rs.getDouble("GAS_SITE_PRICE"));

            node.setGasSourceParty(rs.getString("GAS_SOURCE_PARTY"));
            node.setGasReceiveUnit(rs.getString("GAS_RECEIVE_UNIT"));
            node.setGasExportCapacity(rs.getDouble("GAS_EXPORT_CAPACITY"));
            node.setGasHourFlow(rs.getDouble("GAS_HOUR_FLOW"));
            node.setContacts(rs.getString("CONTACTS"));
            node.setContactNumber(rs.getString("CONTACT_NUMBER"));
            node.setAreaCovered(rs.getDouble("AREA_COVERED"));
            node.setExpansionCapacity(rs.getDouble("EXPANSION_CAPACITY"));
            node.setImportPipeDiameter(rs.getDouble("IMPORT_PIPE_DIAMETER"));
            node.setExportPipeDiameter(rs.getDouble("EXPORT_PIPE_DIAMETER"));
            node.setImportPressure(rs.getDouble("IMPORT_PRESSURE"));
            node.setExportPressure(rs.getDouble("EXPORT_PRESSURE"));

            nodes.add(node);
        });
        return nodes;
    }

    private List<Line> loadLines() {
        List<Line> lines = new ArrayList<>();
        String sql =
                "SELECT lt.LINE_ID, lt.TOPO_LINE_WEIGHT, lt.TOPO_STNOD, lt.TOPO_EDNOD, " +
                        "li.LINE_NAME, li.LINE_NAME_DETAIL, gld.UNIT_PRICE, gld.DIAMETER, gld.DESIGN_PRESSURE, " +
                        "gld.PRESSURE_LEVEL, gld.MAIN_GAS_SOURCE, gld.DESIGN_CAPACITY, gld.LINE_COMMISSIONING_DATE, " +
                        "gld.GAS_LINE_TYPE, gld.GAS_LINE_STATUS, glsc.SURPLUS_CAPACITY, glsc.MONTH " +
                        "FROM SYSDBA.LINE_TOPO lt " +
                        "JOIN SYSDBA.LINES_BASE_INFO li ON lt.LINE_ID = li.LINE_ID " +
                        "JOIN SYSDBA.GAS_LINES_SURPLUS_CAPACITY glsc ON lt.LINE_ID = glsc.LINE_ID " +
                        "JOIN SYSDBA.GAS_LINE_DETAILS gld ON lt.LINE_ID = gld.LINE_ID";

        jdbcTemplate.query(sql, rs -> {
            int lineId = rs.getInt("LINE_ID");
            Line line = findLineById(lines, lineId);

            if (line == null) {
                line = new Line();
                line.setLineId(lineId);
                line.setLineName(rs.getString("LINE_NAME"));
                line.setLineDetailName(rs.getString("LINE_NAME_DETAIL"));
                line.setLength(rs.getDouble("TOPO_LINE_WEIGHT"));
                line.setEndNode(rs.getInt("TOPO_EDNOD"));
                line.setStartNode(rs.getInt("TOPO_STNOD"));
                line.setUnitPrice(rs.getDouble("UNIT_PRICE"));

                line.setDiameter(rs.getDouble("DIAMETER"));
                line.setDesignPressure(rs.getDouble("DESIGN_PRESSURE"));
                line.setPressureLevel(rs.getString("PRESSURE_LEVEL"));
                line.setMainGasSource(rs.getString("MAIN_GAS_SOURCE"));
                line.setDesignCapacity(rs.getDouble("DESIGN_CAPACITY"));
                line.setCommissioningDate(rs.getString("LINE_COMMISSIONING_DATE"));
                line.setGasLineType(rs.getString("GAS_LINE_TYPE"));
                line.setGasLineStatus(rs.getString("GAS_LINE_STATUS"));
                line.setMonthlyCapacities(new ArrayList<>());

                lines.add(line);
            }

            MonthlySurplusCapacity capacity = new MonthlySurplusCapacity();
            capacity.setMonth(rs.getInt("MONTH"));
            capacity.setSurplusCapacity(rs.getDouble("SURPLUS_CAPACITY"));
            line.getMonthlyCapacities().add(capacity);
        });
        return lines;
    }

    private Line findLineById(List<Line> lines, int lineId) {
        for (Line line : lines) {
            if (line.getLineId() == lineId) {
                return line;
            }
        }
        return null;
    }
}
