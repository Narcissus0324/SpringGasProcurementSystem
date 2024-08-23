# SpingBoot项目

## 1.项目结构

```css
GasProcurementSystem/
│
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
│
├── database/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── mapInterfaceService/
│   │   │               ├── GasProcurementSystemApplication.java
│   │   │               ├── controller/
│   │   │               │   └── GasProcurementController.java
│   │   │               ├── service/
│   │   │               │   ├── GasProcurementService.java
│   │   │               │   └── OrToolsProperties.java
│   │   │               ├── model/
│   │   │               │   ├── Line.java
│   │   │               │   ├── Model.java
│   │   │               │   ├── MonthlySurplusCapacity.java
│   │   │               │   ├── Node.java
│   │   │               │   └── PathResult.java
│   │   │               ├── data/
│   │   │               │   └── DataLoader.java
│   │   │               ├── algorithm/
│   │   │               │   └── PathFinder.java
│   │   │               ├── output/
│   │   │               │   └── ResultPrinter.java
│   │   │               └── utils/
│   │   │                   ├── GeometryUtils.java
│   │   │                   ├── RequestDTO.java
│   │   │                   └── ResultDTO.java
│   │   └── resources/
│   │       └── or-tools/
│   │           ├── jniortools.dll
│   │           ├── libjniortools.dylib
│   │           ├── libjniortools.so
│   │           └── libortools.so.9
│   │       └── application.yml
│
└── pom.xml
```

## 2.请求格式

```json
{
  "date": 2024-05-01,
  "demandNodes": {
    "61": 200,
    "50": 100
  },
  "supplyNodes": [1]
}

{"date":"2024-05-01","demandNodes":{"61":100,"50":90},"supplyNodes":[1,20]}

{"date":"2024-05-01","demandNodes":{"61":100,"50":90},"supplyNodes":null}
```

## 3.返回结果

```json
{"code":"00000","message":null,"data":[{"leng":21.728445288435406,"cost":3000,"path_id":1,"gxmc":null,"sygr":90,"optimalCost":2246950,"gxbm":"GD00003306","geom":"[[12694754.051399998,4221144.1570999995],[12708012.867800001,4203929.944300003]]","gxlx":"省级管网","flow":50,"spjg":0.2783},{"leng":28.419694327043636,"cost":3950,"path_id":1,"gxmc":"豫北支线","sygr":5000,"optimalCost":2246950,"gxbm":"GD00003303","geom":"[[12669171.662999999,4208765.9331],[12694754.051399998,4221144.1570999995]]","gxlx":"省级管网","flow":50,"spjg":0.2783}]}
```

​	对应代码

```java
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
```

