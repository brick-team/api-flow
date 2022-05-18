# Api Flow

api 编排工具，api 数据来源swagger，存储介质使用 mongo 。 本项目从 [action-flow](https://github.com/brick-team/action-flow) 拆分

**注意**

1. 暂无数据录入页面
2. 前端样式不完备

## demo

基础数据准备需要执行如下两个接口

1. 将本项目中的swagger接口进行初始化 `curl localhost:8080/swagger`
2. 将本项目的工作流初始化 `curl localhost:8080/flow` 初始化数据如下.

<details>
<summary>flow</summary>
<code>
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
            // 需要执行的api
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
                    {
                        "require":false,
                        "type":"integer",
                        "paramEntities":[

                        ]
                    }
                ]
            },
            // 执行API后的操作
            "watchers":[
                {
                    // 判断执行API后的age属性是否大于0，如果是则执行then反之则执行cat
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
    // 结果对象
    "resultId":"6284996796e28d6fc3e1c0da",
    "_class":"com.github.brick.apiflow.model.flow.FlowEntity"

}
</code>
</details>





3. 执行 `com.github.brick.apiflow.ApiFlowApplicationTests.testFLow` 相关日志如下

```

2022-05-17 15:28:02.983 INFO 38648 --- [           main] c.g.b.apiflow.ApiFlowApplicationTests    : 接口 url
= http://localhost:8080//getAge , 执行结果 = {"age":22} 2022-05-17 15:28:03.023 INFO 38648 --- [           main]
c.g.b.apiflow.ApiFlowApplicationTests    : 接口 url = http://localhost:8080//getUs , 执行结果 = {"name":"zhangsan","age":1,"
uss":null} 2022-05-17 15:28:03.023 INFO 38648 --- [           main] c.g.b.apiflow.ApiFlowApplicationTests    : 步骤数据 =
{1={"age":22}, 2={"name":"zhangsan","age":1,"uss":null}} 2022-05-17 15:28:03.032 INFO 38648 --- [           main]
c.g.b.apiflow.ApiFlowApplicationTests    : 实际响应 = {sss=zhangsan, age=22}

```