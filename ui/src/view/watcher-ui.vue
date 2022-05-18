<template>
  <div>
    <a-table :columns="columns" :data-source="watchers"
             defaultExpandAllRows="true"
             row-key="uid">

      <template #success="{ record }">

        <a @click="show_flow(record,true)">查看</a>


      </template>

      <template #no="{ record }">

        <a @click="show_flow(record,false)">查看</a>


      </template>

    </a-table>

    <a-modal v-model:visible="visible" destroyOnClose :title="then_or_cat? '查看成功执行接口':'查看失败执行接口'" width="75%">
      <swagger-list :input_data="dd" :show_filter="false"/>
    </a-modal>
  </div>
</template>

<script>
import SwaggerList from "@/view/swagger-list";

export default {
  name: "watcher-ui",
  components: {SwaggerList},
  props: {
    input_data: Object
  },
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
          "cat": [
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
                      "el": "$.ssss",
                      "elType": "JSON_PATH",
                      "step": null
                    }
                  }
                ],
                "res": []
              },
              "watchers": []
            }
          ]
        }
      ],
      dd: {},

      then_or_cat: false,


    };

  },
  created() {
    if (this.input_data) {
      console.log("输入" ,JSON.stringify(this.input_data))
      this.watchers = this.input_data;
    }
  }
  , methods: {
    show_flow(e, t) {
      this.then_or_cat = t;
      console.log("then",e.then);

      const a = [];
      if (t) {

        e.then.forEach(x => {
          const v = x.restApiForEx;
          v.step = x.step;
          a.push(v);
        });
      } else {
        e.cat.forEach(x => {
          const v = x.restApiForEx;
          v.step = x.step;
          a.push(v);

        });
      }
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
