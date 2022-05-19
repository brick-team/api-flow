# Api Flow 
在开始进行 Api Flow 设计的说明之前需要先介绍为什么需要API编排。在现在的WEB开发模式下微服务十分流行，在微服务体系下会有大量的API接口出现，这些API接口可以分为如下两部分。
1. 单元服务提供的API接口，即最小应用提供的服务。
2. 聚合服务提供的API接口，即将多个服务组合后对外提供的服务。

在上述两种API接口中第二种聚合性质的API接口通常会调用多个单元服务的接口然后配合一些条件完成对外接口的提供，同时随着微服务项目的演进聚合服务的数量会越来越多，因此聚合服务中的API会越来越多维护成本会逐渐增加，我们是否有一定的方式对这些聚合服务做一些减法。Api Flow 就可以解决这类问题，它是怎么解决的呢？ Api Flow 解决了聚合服务的编码过程，通过配置的方式将API接口调用流程进行描述，然后通过一定的方式将其执行。

## Api Flow 设计说明
Api Flow 的核心模型包含如下几个
1. 接口模型：该模型用于定义Rest Api，本项目采用Swagger文档进行抽象设计。
2. 流程模型：该模型用于定义接口执行逻辑。
3. 结果模型：该模型用于定义流程执行结果。


### 接口模型
本节将对接口模型进行介绍，接口模型类是 `com.github.brick.apiflow.model.rest.ApiEntity` 定义代码如下。

```java
@Data
@Document("rest_api")
public class ApiEntity {
    @Id
    private String uid;
    private String url;
    private String method;
    private String desc;
    private List<ApiParamEntity> params ;
    private List<ApiParamEntity> res ;
}
```
1. uid: 唯一标识 
2. url: HTTP 接口地址 
3. method: HTTP接口调用方式
4. desc: HTTP 接口描述
5. params: HTTP 接口请求参数
6. res: HTTP 接口返回参数

参数对象是 `com.github.brick.apiflow.model.rest.ApiParamEntity` 定义代码如下。

```java
@Data
public class ApiParamEntity {
    private ParamIn in;
    private String name;
    private boolean require;
    private String type;
    private Long id;
    private List<ApiParamEntity> paramEntities ;
    private String defaultValue;
    private ExtractExecuteEntity extract;

}
```
1. in: 参数所在位置，一般的有请求地址、请求体、表单。
2. name: 参数名称
3. require: 是否必填
4. type: 参数类型
5. id: 唯一标识
6. paramEntities: 子参数，一般用于请求体情况下
7. defaultValue: 执行期间使用，默认值
8. extract: 执行期间使用，取值表达式

类ExtractExecuteEntity 定义代码如下
```java

@Data
public class ExtractExecuteEntity {
    private String el;
    private ExtractModel elType;
    private String step;

}
```

1. el：取值表达式
2. elType: 取值表达式类型，JSON_PATH
3. step: 数据来自那个步骤

类ApiEntity和类ApiParamEntity组合可以将Swagger接口文档进行存储，这部分数据将会在流程模型中产生具体使用价值。

### 流程模型
本节将对流程模型进行介绍，流程模型类是 `com.github.brick.apiflow.model.flow.FlowEntity` 定义代码如下。

```java
@Data
@Document("flow_def")
public class FlowEntity {
    @Id
    private String id;
    private String name;
    private String desc;
    private List<WorkExecuteEntity> works;
    private String resultId;
}
```

1. id: 唯一标识
2. name: 流程名称
3. desc: 流程描述
4. works: 工作节点集合
5. resultId: 结果模型id

工作节点定义代码如下。

```java
@Data
public class WorkExecuteEntity {
    private String step;
    private boolean async = false;
    private String restId;
    private ApiEntity restApiForEx;
    private List<WatcherExecuteEntity> watchers;
}
```
1. step: 步骤号，在整个工作节点集合中不允许重复
2. async: 是否异步执行
3. restId: 请求接口id
4. restApiForEx: 请求接口id对应的数据信息，注意参数对象中的defaultValue属性和extract属性需要填写。
5. watchers: 观察者集合

观察者定义代码如下。
```java
@Data
public class WatcherExecuteEntity {
    private String condition;
    private ExtractModel elType;
    private List<WorkExecuteEntity> then;
    private List<WorkExecuteEntity> cat;
}
```
1. condition: 条件表达式
2. elType: 条件表达式中的取值表达式类型
3. then: 通过条件表达式时执行的工作节点
4. cat: 没有通过条件表达式时执行的工作节点


### 结果模型
本节将对结果模型进行介绍，结果模型类是 `com.github.brick.apiflow.model.flow.ResultExecuteEntity` 定义代码如下。
```java
@Data
@Document("result_def")
public class ResultExecuteEntity {

    @Id
    private String id;

    private String flowId;

    private List<FieldExecuteEntity> fields;

}
```
1. id: 唯一标识
2. flowId: 流程ID
3. fields: 响应字段集合

响应字段定义如下。

```java
@Data
public class FieldExecuteEntity {
    private String fieldName;
    private FieldType type;
    private String value;
    private ExtractExecuteEntity extract;
    private List<FieldExecuteEntity> properties = new ArrayList<>();
}
```
1. fieldName: 字段名称
2. type: 字段类型
3. value: 数据值（默认值）
4. extract: 取值对象
5. properties: 子属性，一般在type为对象类型是使用




### 使用说明
1. 通过流程id找到流程对象。
2. 向流程对象中传输JSON参数执行。
3. 将执行结果和结果模型进行绑定得到真正的响应。

下面将使用一个例子进行上述处理流程的说明，首先是流程数据。

```json
{
  "_id":{
    "$oid":"6284996796e28d6fc3e1c0db"
  },
  "name":"测试",
  "desc":"测试流程",
  "works":[
    {
      "step":"1",
      "async":false,
      "restId":"62833f1858f599635e0a8cd5",
      "restApiForEx":{
        "_id":{
          "$oid":"62833f1858f599635e0a8cd5"
        },
        "url":"/getAge",
        "method":"get",
        "desc":"getAge",
        "params":[
          {
            "in":"query",
            "name":"uid",
            "require":true,
            "type":"string",
            "paramEntities":[

            ],
            "extract":{
              "el":"$.uuu",
              "elType":"JSON_PATH"
            }
          }
        ],
        "res":[
        ]
      },
      "watchers":[
        {
          "condition":"$.age > 0",
          "elType":"JSON_PATH",
          "then":[
            {
              "step":"2",
              "async":false,
              "restId":"62833f1858f599635e0a8cd6",
              "restApiForEx":{
                "_id":{
                  "$oid":"62833f1858f599635e0a8cd6"
                },
                "url":"/getUs",
                "method":"get",
                "desc":"getUs",
                "params":[
                  {
                    "in":"query",
                    "name":"uid",
                    "require":true,
                    "type":"string",
                    "paramEntities":[

                    ],
                    "extract":{
                      "el":"$.uuu",
                      "elType":"JSON_PATH"
                    }
                  }
                ],
                "res":[

                ]
              },
              "watchers":[

              ]
            }
          ]
        }
      ]
    }
  ],
  "resultId":"6284996796e28d6fc3e1c0da",
  "_class":"com.github.brick.apiflow.model.flow.FlowEntity"
}
```

在上述流程对象中存在一个工作节点，步骤号：1，需要执行的接口是/getAge,执行时需要传输参数uid，参数位于请求地址中，具体请求地址：`/getAge?uid=xxxx` xxxx的数据通过 `$.uuu` 进行获取。
步骤号1 的工作节点具备一个观察器，该观察器对放回结果的age属性进行判断，如果age属性值大于0则需要执行步骤号2。

执行该流程需要传输如下两个参数
1. flowId：6284996796e28d6fc3e1c0db
2. 执行参数：{"uuu":1}


## 项目代码
1. https://gitee.com/pychfarm_admin/api-flow
   **HELP: 由于笔者并不擅长前端工作，在该项目中仅有数据展示和模拟执行的功能，并没有具备添加操作相关功能，如果您有兴趣可以一起参与**
2. https://github.com/brick-team/action-flow

