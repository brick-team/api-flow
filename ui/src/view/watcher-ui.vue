<template>
  <div>
    <a-table :columns="columns" :data-source="watchers"
             defaultExpandAllRows="true"
             row-key="uid">

      <template #success="{ record }">

        <a @click="show_flow(record)">查看</a>


      </template>

      <template #no="{ record }">

        <a @click="show_flow(record)">查看</a>


      </template>

    </a-table>

    <a-modal v-model:visible="visible" destroyOnClose title="信息" width="75%">
      <swagger-list :input_data="dd" :show_filter="false"/>
    </a-modal>
  </div>
</template>

<script>
import SwaggerList from "@/view/swagger-list";

export default {
  name: "watcher-ui",
  components: {SwaggerList},
  data() {
    return {
      visible: false,
      columns: [
        {
          title: '条件表达式',
          dataIndex: 'condition',
          key: 'condition',
        },
        {
          title: "满足",
          dataIndex: 'success',
          slots: {
            customRender: 'success',
          },
        },
        {
          title: "不满足",
          dataIndex: 'no',
          slots: {
            customRender: 'no',
          },
        }

      ],
      watchers: [
        {
          "condition": "$.age > 0",
          "elType": "JSON_PATH",
          "then": [
            {
              "step": "2",
              "async": false,
              "restId": "62833f1858f599635e0a8cd6",
              "restApiForEx": {
                "uid": "62833f1858f599635e0a8cd6",
                "id": null,
                "url": "/getUs",
                "method": "get",
                "desc": "getUs",
                "params": [
                  {
                    "flag": null,
                    "in": "query",
                    "name": "uid",
                    "require": true,
                    "type": "string",
                    "id": null,
                    "defaultValue": null,
                    "paramEntities": [],
                    "extract": {
                      "el": "$.uuu",
                      "elType": "JSON_PATH",
                      "step": null
                    }
                  }
                ],
                "res": []
              },
              "watchers": []
            }
          ],
          "cat": []
        }
      ],
      dd: {},


    };

  },
  created() {

  }
  , methods: {
    show_flow(e) {
      console.log(e.then);

      const a = [];
      e.then.forEach(x=>{
        a.push(x.restApiForEx);
      })
      console.log(a);
      this.dd = {'content': a};
      this.visible = true;
    },

  },
  setup() {

  },
};
</script>

<style scoped>

</style>
