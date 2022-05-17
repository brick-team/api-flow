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

package com.github.brick.apiflow.model.flow;

import com.github.brick.apiflow.model.rest.ApiEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkExecuteEntity {
    private String step;
    private boolean async = false;

    /**
     * {@link  com.github.brick.apiflow.model.rest.ApiEntity#uid}
     */
    private String restId;

    private ApiEntity restApiForEx;

    private List<WatcherExecuteEntity> watchers = new ArrayList<>();

}