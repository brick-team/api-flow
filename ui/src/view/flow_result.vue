<!--
  -    Copyright [2022] [brick-team]
  -
  -    Licensed under the Apache License, Version 2.0 (the "License");
  -    you may not use this file except in compliance with the License.
  -    You may obtain a copy of the License at
  -
  -        http://www.apache.org/licenses/LICENSE-2.0
  -
  -    Unless required by applicable law or agreed to in writing, software
  -    distributed under the License is distributed on an "AS IS" BASIS,
  -    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  -    See the License for the specific language governing permissions and
  -    limitations under the License.
  -->

<template>
  <div>
    执行参数
    <a-textarea v-model:value="pp" placeholder="Basic usage" :rows="4"/>
    <a-button @click="sub">执行</a-button>

    <div>
      执行结果
      <a-textarea v-model:value="rs" disabled="true" :rows="4"/>


    </div>

  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "flow-result",
  props: {
    flow_id: String
  },
  created() {

  },
  methods: {
    sub() {
      console.log(this.flow_id);
      axios.post('http://localhost:8080/flow/execute', {
        flowId: this.flow_id,
        u: this.pp,
      })
          .then((response) => {
            console.log(response)
            this.rs = JSON.stringify( response.data);
          })
          .catch((error) => console.log(error));
    },
  },
  data() {
    return {
      pp: "{\"uuu\":\"1\"}",
      rs: "",

    }
  },
}
</script>

<style scoped>

</style>