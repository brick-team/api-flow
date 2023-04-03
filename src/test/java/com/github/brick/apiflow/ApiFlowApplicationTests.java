package com.github.brick.apiflow;

import com.github.brick.apiflow.core.ActionFlowCondition;
import com.github.brick.apiflow.core.ActionFlowConditionImpl;
import com.github.brick.apiflow.core.Extract;
import com.github.brick.apiflow.core.HttpWorker;
import com.github.brick.apiflow.core.JsonPathExtract;
import com.github.brick.apiflow.core.OkHttpWorkerImpl;
import com.github.brick.apiflow.model.flow.ExtractExecuteEntity;
import com.github.brick.apiflow.model.flow.ExtractModel;
import com.github.brick.apiflow.model.flow.FieldExecuteEntity;
import com.github.brick.apiflow.model.flow.FieldType;
import com.github.brick.apiflow.model.flow.FlowEntity;
import com.github.brick.apiflow.model.flow.ResultExecuteEntity;
import com.github.brick.apiflow.model.flow.WatcherExecuteEntity;
import com.github.brick.apiflow.model.flow.WorkExecuteEntity;
import com.github.brick.apiflow.model.rest.ApiEntity;
import com.github.brick.apiflow.model.rest.ApiParamEntity;
import com.github.brick.apiflow.model.rest.ParamIn;
import com.github.brick.apiflow.repo.FlowEntityRepo;
import com.github.brick.apiflow.repo.ResultEntityRepo;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

@SpringBootTest
class ApiFlowApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(ApiFlowApplicationTests.class);
    Gson gson = new Gson();
    ActionFlowCondition actionFlowCondition = new ActionFlowConditionImpl();
    @Autowired
    private FlowEntityRepo flowEntityRepo;
    @Autowired
    private ResultEntityRepo resultEntityRepo;

    @Test
    void testFLow() throws Exception {
        Optional<FlowEntity> byId = flowEntityRepo.findById("t1");

        FlowEntity flow = byId.get();
        Map<String, String> u = new HashMap<>();

        u.put("uuu", "1");

        List<WorkExecuteEntity> works = flow.getWorks();

        Map<String, Object> stepMap = new HashMap<>();
        for (WorkExecuteEntity work : works) {
            executeWork(u, stepMap, work);

        }
//        System.out.println(stepMap);

        logger.info("步骤数据 = {} ", stepMap);
        String resultId = flow.getResultId();
        Optional<ResultExecuteEntity> byId1 = resultEntityRepo.findById(resultId);
        ResultExecuteEntity resultExecuteEntity = byId1.get();

        Map<String, Object> stringObjectMap = handlerResult(resultExecuteEntity, stepMap);
        logger.info("实际响应 = {} ", stringObjectMap);
    }

    private Map<String, Object> handlerResult(ResultExecuteEntity resultExecuteEntity, Map<String, Object> stepWorkResult) {
        List<FieldExecuteEntity> fields = resultExecuteEntity.getFields();

        Map<String, Object> res = new HashMap<>(32);

        for (FieldExecuteEntity field : fields) {
            res.putAll(handlerFieldExecuteEntity(stepWorkResult, field));

        }
        return res;
    }

    private Map<String, Object> handlerFieldExecuteEntity(Map<String, Object> stepWorkResult, FieldExecuteEntity field) {
        Map<String, Object> res = new HashMap<>();
        FieldType type = field.getType();

        String fieldName = field.getFieldName();
        ExtractExecuteEntity extract = field.getExtract();
        ExtractModel elType = extract.getElType();
        Extract factory = new JsonPathExtract();

        List<FieldExecuteEntity> properties = field.getProperties();
        Object objDataFrom = factory.extract((String) stepWorkResult.get(extract.getStep()), extract.getEl());
        if (type == FieldType.OBJECT) {
            res.put(fieldName, handlerResultObject(properties, objDataFrom));
        }
        else if (type == FieldType.ARRAY) {
            res.put(fieldName, handlerResultArray(properties, objDataFrom));
        }
        else if (type == FieldType.ARRAY_OBJECT) {
            res.put(fieldName, handlerResultArrayObject(properties, objDataFrom, stepWorkResult));

        }
        else {
            res.put(fieldName, objDataFrom);
        }
        return res;
    }

    private Object handlerResultObject(List<FieldExecuteEntity> properties, Object objDataFrom) {
        Map<String, Object> map = new HashMap<>(32);
        if (null == properties || properties.isEmpty()) {
            return objDataFrom;
        }
        for (FieldExecuteEntity property : properties) {
            ExtractExecuteEntity extract = property.getExtract();
            ExtractModel elType = extract.getElType();
            Extract factory = new JsonPathExtract();

            Object extract1 = factory.extract(objDataFrom, extract.getEl());
            map.put(property.getFieldName(), extract1);
        }
        return map;
    }

    private Object handlerResultArray(List<FieldExecuteEntity> properties, Object objDataFrom) {
        List<Object> res = new ArrayList<>();
        if (null == properties || properties.isEmpty()) {
            return objDataFrom;
        }

        for (FieldExecuteEntity property : properties) {
            ExtractExecuteEntity extract = property.getExtract();
            ExtractModel elType = extract.getElType();
            Extract factory = new JsonPathExtract();

            if (objDataFrom instanceof JSONArray) {
                for (Object o : ((JSONArray) objDataFrom)) {
                    Object extract1 = factory.extract(o, extract.getEl());
                    res.add(extract1);
                }
            }

        }
        return res;
    }

    private Object handlerResultArrayObject(List<FieldExecuteEntity> properties, Object objDataFrom, Map<String, Object> stepWorkResult) {
        List<Map> res = new ArrayList<>();
        // todo:  objDataFrom 不同数据类型
        if (objDataFrom instanceof LinkedHashMap) {

            Map<String, Object> data = new HashMap<>();

            for (FieldExecuteEntity field : properties) {
                String fieldName = field.getFieldName();
                ExtractExecuteEntity extract = field.getExtract();
                ExtractModel elType = extract.getElType();
                Extract factory = new JsonPathExtract();
                Object elValue = factory.extract(objDataFrom, extract.getEl());
                data.put(fieldName, elValue);
            }
            res.add(data);
        }
        if (objDataFrom instanceof JSONArray) {
            for (Object k : ((JSONArray) objDataFrom)) {
                Map<String, Object> data = new HashMap<>();

                for (FieldExecuteEntity field : properties) {
                    String fieldName = field.getFieldName();
                    ExtractExecuteEntity extract = field.getExtract();
                    ExtractModel elType = extract.getElType();
                    Extract factory = new JsonPathExtract();
                    Object elValue = factory.extract(k, extract.getEl());
                    data.put(fieldName, elValue);
                }
                res.add(data);
            }
        }


        return res;
    }

    private void executeWork(Map<String, String> u, Map<String, Object> stepMap, WorkExecuteEntity work) throws IOException {
        ApiEntity restApiForEx = work.getRestApiForEx();
        String step = work.getStep();

        Object o = handlerRestApi(restApiForEx, gson.toJson(u));
        stepMap.put(step, o);

        List<WatcherExecuteEntity> watchers = work.getWatchers();
        if (!CollectionUtils.isEmpty(watchers)) {

            for (WatcherExecuteEntity watcher : watchers) {
                ExtractModel elType = watcher.getElType();
                String condition = watcher.getCondition();
                boolean aBoolean = actionFlowCondition.condition(condition, elType, o);

                if (aBoolean) {
                    List<WorkExecuteEntity> then = watcher.getThen();
                    for (WorkExecuteEntity workExecuteEntity : then) {
                        executeWork(u, stepMap, workExecuteEntity);
                    }
                } else {
                    List<WorkExecuteEntity> cat = watcher.getCat();
                    for (WorkExecuteEntity workExecuteEntity : cat) {
                        executeWork(u, stepMap, workExecuteEntity);
                    }
                }
            }
        }

    }

    private Object handlerRestApi(ApiEntity restApi, String jsonData) throws IOException {

        String url = restApi.getUrl();
        url = "http://localhost:8080/" + url;
        String method = restApi.getMethod();


        List<ApiParamEntity> params = restApi.getParams();
        // todo: 支持HTTPCLIENT选择
        HttpWorker httpWorker = new OkHttpWorkerImpl();


        Map<String, String> queryParam = new HashMap<>(32);
        Map<String, String> pathParam = new HashMap<>(32);
        Map<String, String> headers = new HashMap<>(32);
        Map<String, String> formatData = new HashMap<>(32);
        Map<String, String> body = new HashMap<>(32);

        for (ApiParamEntity paramExecuteEntity : params) {
            handlerRestApiParam(jsonData, queryParam, pathParam, headers, formatData, body, paramExecuteEntity);

        }


        // TODO: 2022/4/26 此处需要调整，增加 rest template 支持

        String work = httpWorker.work(url, method, pathParam, queryParam, headers, formatData, body);
        logger.info("接口 url = {} , 执行结果 = {}", url, work);
        return work;
    }

    private void handlerRestApiParam(String jsonData, Map<String, String> queryParam, Map<String, String> pathParam, Map<String, String> headers, Map<String, String> formatData, Map<String, String> body,
                                     ApiParamEntity restApi1) {
        ParamIn in = restApi1.getIn();
        ExtractExecuteEntity extract = restApi1.getExtract();
        String name = restApi1.getName();
        String value = restApi1.getDefaultValue();

        if (in == ParamIn.header) {
            handlerDataMap(value, extract, jsonData, headers, name);
        }
        else if (in == ParamIn.formdata) {
            handlerDataMap(value, extract, jsonData, formatData, name);
        }
        else if (in == ParamIn.body) {
            handlerDataMap(value, extract, jsonData, body, name);
        }
        else if (in == ParamIn.path) {
            handlerDataMap(value, extract, jsonData, pathParam, name);
        }
        else if (in == ParamIn.query) {
            handlerDataMap(value, extract, jsonData, queryParam, name);
        }

        // todo: 子集参数处理
        List<ApiParamEntity> restApis = restApi1.getParamEntities();

        if (restApis != null) {

            for (ApiParamEntity api1c : restApis) {
                List<ApiParamEntity> restApis1 = api1c.getParamEntities();

                for (ApiParamEntity forRestApi : restApis1) {
                    handlerRestApiParam(jsonData, queryParam, pathParam, headers, formatData, body, forRestApi);
                }
            }
        }
    }

    private boolean strValueIsNull(String value) {
        return "".equals(value) || value == null;
    }

    private void handlerDataMap(String value, ExtractExecuteEntity extract, String jsonData, Map<String, String> dataMap, String name) {

        // todo: json 表达式处理
        String el = extract.getEl();
        Extract extract1 = new JsonPathExtract();

        Object extract2 = extract1.extract(jsonData, el);


        dataMap.put(name, value == null ? extract2.toString() : value);

    }
}
