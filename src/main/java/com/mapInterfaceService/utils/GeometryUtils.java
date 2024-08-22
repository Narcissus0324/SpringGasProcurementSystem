package com.mapInterfaceService.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;

import java.math.BigDecimal;

public class GeometryUtils {

    public static String convertGeometryToCoordinatePairs(Geometry geometry) {
        StringBuilder coordinates = new StringBuilder();

        coordinates.append("[");
        for (Coordinate coord : geometry.getCoordinates()) {
            if (coordinates.length() > 1) {
                coordinates.append(",");
            }
            coordinates.append("[")
                    .append(formatCoordinate(coord.x))
                    .append(",")
                    .append(formatCoordinate(coord.y))
                    .append("]");
        }
        coordinates.append("]");

        return coordinates.toString();
    }

    private static String formatCoordinate(double value) {
        // 避免科学计数法
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        return bigDecimal.toPlainString();
    }
}

/*
public class GeometryUtils {

    public static String convertGeometryToWKT(Geometry geometry) {
        WKTWriter wktWriter = new WKTWriter();
        return wktWriter.write(geometry); // 将Geometry对象转换为WKT格式的字符串
    }
}*/
