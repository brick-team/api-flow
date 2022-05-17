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

import io.swagger.annotations.*;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api("用户")
public class UserController {

    static Map<String, Integer> ageMap = new HashMap<>();
    static Map<String, Us> usMap = new HashMap<>();

    static {
        ageMap.put("1", 22);
        Us value = new Us();
        value.setAge(1);
        value.setName("zhangsan");

        usMap.put("1", value);
    }

    @ApiOperation(value = "getAge", notes = "获取年龄", response = Integer.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "唯一标识", dataType = "String", example = "1"),
    })

    @GetMapping("/getAge")
    public ResponseEntity<Map<String, Integer>> getAge(
            @RequestParam("uid") String uid) {

        Integer integer = ageMap.get(uid);
        Map<String, Integer> res = new HashMap<>();
        res.put("age", integer);
        return ResponseEntity.ok(res);
    }

    @ApiOperation(value = "getUs", notes = "获取us对象", response = Us.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "唯一标识", dataType = "String", example = "1"),
    })
    @GetMapping("/getUs")
    public ResponseEntity<Us> getUs(
            @RequestParam("uid") String uid) {

        return ResponseEntity.ok(usMap.get(uid));
    }

    @Data
    @ApiModel(value = "Us")
    public static class Us {

        @ApiModelProperty(value = "name")
        private String name;
        @ApiModelProperty(value = "age")
        private Integer age;
        @ApiModelProperty(value = "uss")
        private List<Us> uss;
    }
}
