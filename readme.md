# Api Flow

api 编排工具，api 数据来源swagger，存储介质使用 mongo 。 本项目从 [action-flow](https://github.com/brick-team/action-flow) 拆分

**注意**

1. 暂无数据录入页面
2. 前端样式不完备

## demo

基础数据准备需要执行如下两个接口

1. 将本项目中的swagger接口进行初始化 `curl localhost:8080/swagger`
2. 将本项目的工作流初始化 `curl localhost:8080/flow` 初始化关键数据有[flow_json](file/flow_json.json)和[result](file/result.json)
3. 启动前端项目
```shell
cd ui 
yarn install
```
4. 执行 `com.github.brick.apiflow.ApiFlowApplicationTests.testFLow` 相关日志如下

```

2022-05-17 15:28:02.983 INFO 38648 --- [           main] c.g.b.apiflow.ApiFlowApplicationTests    : 接口 url
= http://localhost:8080//getAge , 执行结果 = {"age":22} 2022-05-17 15:28:03.023 INFO 38648 --- [           main]
c.g.b.apiflow.ApiFlowApplicationTests    : 接口 url = http://localhost:8080//getUs , 执行结果 = {"name":"zhangsan","age":1,"
uss":null} 2022-05-17 15:28:03.023 INFO 38648 --- [           main] c.g.b.apiflow.ApiFlowApplicationTests    : 步骤数据 =
{1={"age":22}, 2={"name":"zhangsan","age":1,"uss":null}} 2022-05-17 15:28:03.032 INFO 38648 --- [           main]
c.g.b.apiflow.ApiFlowApplicationTests    : 实际响应 = {sss=zhangsan, age=22}

```