# SpingBoot项目

## 1.项目结构

```
GasProcurementSystem/
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
│   │   │               │   └── GasProcurementService.java
│   │   │               ├── repository/
│   │   │               ├── model/
│   │   │               │   ├── Node.java
│   │   │               │   ├── Line.java
│   │   │               │   ├── Model.java
│   │   │               │   ├── MonthlySurplusCapacity.java
│   │   │               │   └── PathResult.java
│   │   │               ├── data/
│   │   │               │   └── DataLoader.java
│   │   │               ├── algorithm/
│   │   │               │   └── PathFinder.java
│   │   │               └── utils/
│   │   │                   ├── RequestDTO.java
│   │   │                   └── ResultDTO.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └──  java/
│           └── com/
│               └── mapInterfaceService/
│                   └── GasProcurementSystem/
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
  "supplyNodes": [1, 50]
}

{"date":"2024-05-01","demandNodes":{"61":100,"50":90},"supplyNodes":[1,20]}
{"date":"2024-05-01","demandNodes":{"61":100,"50":90},"supplyNodes":null}
```

## 3.返回结果

```json
{"code":"00000","message":null,"data":[{"leng":21728,"cost":5442300,"gxmc":null,"sygr":90,"gxlx":"省级管网","flow":90,"spjg":0.2783},{"leng":28419,"cost":7909200,"gxmc":"豫北支线","sygr":5000,"gxlx":"省级管网","flow":100,"spjg":0.2783}]}
```

​	对应代码

```java
pathJson.put("gxmc", trimString(line.getLineName()));
pathJson.put("gxlx", trimString(line.getGasLineType()));
pathJson.put("leng", (int)line.getLength());
pathJson.put("sygr", result.getCapacity());
pathJson.put("flow", result.getFlow());
pathJson.put("spjg", line.getUnitPrice());
pathJson.put("cost", result.getCost());
```

