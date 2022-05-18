/*
 *    Copyright [2022] [brick-team]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.brick.apiflow.ctr;

import com.github.brick.apiflow.model.flow.*;
import com.github.brick.apiflow.model.rest.ApiEntity;
import com.github.brick.apiflow.model.rest.ApiParamEntity;
import com.github.brick.apiflow.repo.ApiEntityRepo;
import com.github.brick.apiflow.repo.FlowEntityRepo;
import com.github.brick.apiflow.repo.ResultEntityRepo;
import com.github.brick.apiflow.service.SwaggerParseImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class DemoController {
    //    public static final String url = "https://petstore.swagger.io/v2/swagger.json";
    public static final String url = "http://localhost:8080/v2/api-docs";
    @Autowired
    private ApiEntityRepo apiEntityRepo;
    @Autowired
    private FlowEntityRepo flowEntityRepo;
    @Autowired
    private ResultEntityRepo resultEntityRepo;

    @GetMapping("/swagger")
    public void swagger() {
        SwaggerParseImpl swaggerParse = new SwaggerParseImpl();
        List<ApiEntity> apiEntities = swaggerParse.parseUrl(url);
        this.apiEntityRepo.saveAll(apiEntities);
    }

    @GetMapping("/flow")
    public void flow() {
        FlowEntity flow = new FlowEntity();
        flow.setName("测试");
        flow.setDesc("测试流程");

        ArrayList<WorkExecuteEntity> works = new ArrayList<>();

        WorkExecuteEntity e = new WorkExecuteEntity();
        e.setStep("1");
        e.setAsync(false);
        e.setRestId("62833f1858f599635e0a8cd5");


        Optional<ApiEntity> byId = this.apiEntityRepo.findById("62833f1858f599635e0a8cd5");
        if (byId.isPresent()) {
            ApiEntity apiEntity = byId.get();

            List<ApiParamEntity> params = apiEntity.getParams();
            for (ApiParamEntity param : params) {
                ExtractExecuteEntity extract = new ExtractExecuteEntity();
                extract.setEl("$.uuu");
                extract.setElType(ExtractModel.JSON_PATH);
                param.setExtract(extract);

            }

            e.setRestApiForEx(apiEntity);

        }
        ArrayList<WatcherExecuteEntity> watchers = new ArrayList<>();
        WatcherExecuteEntity e1 = new WatcherExecuteEntity();
        e1.setCondition("$.age > 0");
        e1.setElType(ExtractModel.JSON_PATH);
        ArrayList<WorkExecuteEntity> then = new ArrayList<>();
        WorkExecuteEntity e2 = new WorkExecuteEntity();
        e2.setStep("2");
        e2.setAsync(false);
        e2.setRestId("62833f1858f599635e0a8cd6");

        Optional<ApiEntity> byId2 = this.apiEntityRepo.findById("62833f1858f599635e0a8cd6");
        if (byId2.isPresent()) {
            ApiEntity apiEntity = byId2.get();

            List<ApiParamEntity> params = apiEntity.getParams();
            for (ApiParamEntity param : params) {
                ExtractExecuteEntity extract = new ExtractExecuteEntity();
                extract.setEl("$.uuu");
                extract.setElType(ExtractModel.JSON_PATH);
                param.setExtract(extract);

            }

            e2.setRestApiForEx(apiEntity);

        }


        then.add(e2);
        e1.setThen(then);
        e1.setCat(null);

        watchers.add(e1);
        e.setWatchers(watchers);
        works.add(e);
        flow.setWorks(works);
        ResultExecuteEntity result = result(flow.getId());
        this.resultEntityRepo.save(result);

        flow.setResultId(result.getId());
        this.flowEntityRepo.save(flow);



    }


    ResultExecuteEntity result(String  flowId) {
        ResultExecuteEntity resultExecuteEntity = new ResultExecuteEntity();
        resultExecuteEntity.setFlowId(flowId);
        ArrayList<FieldExecuteEntity> fields = new ArrayList<>();
        FieldExecuteEntity e = new FieldExecuteEntity();
        e.setFieldName("sss");
        e.setType(FieldType.STRING);
        ExtractExecuteEntity extract = new ExtractExecuteEntity();
        extract.setEl("$.name");
        extract.setElType(ExtractModel.JSON_PATH);
        extract.setStep("2");
        e.setExtract(extract);
        e.setProperties(Lists.newArrayList());

        fields.add(e);
        FieldExecuteEntity e1 = new FieldExecuteEntity();
        e1.setFieldName("age");
        e1.setType(FieldType.INTEGER);
        ExtractExecuteEntity extract1 = new ExtractExecuteEntity();
        extract1.setEl("$.age");
        extract1.setElType(ExtractModel.JSON_PATH);
        extract1.setStep("1");

        e1.setExtract(extract1);
        e1.setProperties(Lists.newArrayList());


        fields.add(e1);

        resultExecuteEntity.setFields(fields);

        return resultExecuteEntity;
    }
}
