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

import com.github.brick.apiflow.model.flow.FlowEntity;
import com.github.brick.apiflow.model.rest.ApiEntity;
import com.github.brick.apiflow.repo.FlowEntityRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/flow")
public class FlowController {
    @Autowired
    private FlowEntityRepo flowEntityRepo;

    @GetMapping("/page")
    public ResponseEntity<Page<FlowEntity>> page(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "desc", required = false) String desc,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size
    ) {
        PageRequest of = PageRequest.of(page, size);
        FlowEntity probe = new FlowEntity();
        if (StringUtils.isNotBlank(name)) {

            probe.setName(name);
        }
        if (StringUtils.isNotBlank(desc)) {
            probe.setDesc(desc);

        }


        if (StringUtils.isBlank(name) && StringUtils.isBlank(desc)) {
            Page<FlowEntity> all = flowEntityRepo.findAll(of);
            return ResponseEntity.ok(all);
        }
        else {
            //设置模糊查询匹配规则
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreCase(true);
            Page<FlowEntity> all = flowEntityRepo.findAll(Example.of(probe, matcher), of);
            return ResponseEntity.ok(all);
        }
    }


    @GetMapping("/by_id")
    public ResponseEntity<FlowEntity> byId(
            @RequestParam(value = "uid") String uid
    ) {
        Optional<FlowEntity> byId = this.flowEntityRepo.findById(uid);
        if (byId.isPresent()) {
            FlowEntity body = byId.get();
            log.info("data = {}", body);
            return ResponseEntity.ok(body);
        }
        return ResponseEntity.notFound().build();
    }


}
