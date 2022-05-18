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

import com.github.brick.apiflow.model.rest.ApiEntity;
import com.github.brick.apiflow.repo.ApiEntityRepo;
import com.github.brick.apiflow.service.SwaggerParseImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/rest_api")
public class RestApiController {
    @Autowired
    private ApiEntityRepo apiEntityRepo;

    @GetMapping("/page")
    public ResponseEntity<Page<ApiEntity>> page(
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "desc", required = false) String desc,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size
    ) {
        PageRequest of = PageRequest.of(page, size);
        ApiEntity probe = new ApiEntity();
        if (StringUtils.isNotBlank(url)) {

            probe.setUrl(url);
        }
        if (StringUtils.isNotBlank(desc)) {
            probe.setDesc(desc);

        }


        if (StringUtils.isBlank(url) && StringUtils.isBlank(desc)) {
            Page<ApiEntity> all = apiEntityRepo.findAll(of);
            return ResponseEntity.ok(all);
        }
        else {
            //设置模糊查询匹配规则
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreCase(true);
            Page<ApiEntity> all = apiEntityRepo.findAll(Example.of(probe, matcher), of);
            return ResponseEntity.ok(all);
        }
    }

    @GetMapping("/by_id")
    public ResponseEntity<ApiEntity> byId(
            @RequestParam(value = "uid") String uid

    ) {
        Optional<ApiEntity> byId = this.apiEntityRepo.findById(uid);
        if (byId.isPresent()) {
            ApiEntity body = byId.get();
            log.info("data = {}", body);
            return ResponseEntity.ok(body);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/import_swagger")
    public ResponseEntity<Boolean> importSwagger(
            String url
    ) {
        SwaggerParseImpl swaggerParse = new SwaggerParseImpl();
        List<ApiEntity> apiEntities = swaggerParse.parseUrl(url);
        this.apiEntityRepo.saveAll(apiEntities);
        return ResponseEntity.ok(true);
    }
}
