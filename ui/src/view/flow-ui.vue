<template>
  <div>
    流程视图

    <span>工作流程名称= {{ rest_api_return.name }}</span>
    <span>工作流程描述= {{ rest_api_return.desc }}</span>


    <a-table :columns="columns" :data-source="rest_api_return.works"
             defaultExpandAllRows="true"
             row-key="id">

      <template #operation="{ record }">

        <a @click="show_swagger(record)">查看</a>


      </template>
      <template #watcher_list="{ record }">

        <a @click="show_watcher(record)">查看</a>

      </template>


    </a-table>
    <a-modal v-model:visible="visible" destroyOnClose title="接口详情" width="75%">
      <swagger-ui :param_inp="pp" :show_op="true"/>
    </a-modal>


    <a-modal v-model:visible="watcher_visible" destroyOnClose title="接口详情" width="75%">
      <watcher-ui :input_data="pp_watcher"></watcher-ui>
    </a-modal>
  </div>


</template>

<script>
import SwaggerUi from "@/view/swagger-ui";
import WatcherUi from "@/view/watcher-ui";

export default {
  name: "flow-ui",
  components: {WatcherUi, SwaggerUi},
  props: {
    input_data: Object,

  },

  data() {
    return {
      columns: [
        {
          title: '步骤号',
          dataIndex: 'step',
          key: 'step',
        }, {
          title: '是否异步执行',
          dataIndex: 'async',
          key: 'async',
          customRender: (text) => {
            return (
                text ? "否" : "是"
            )
          }
        }
        , {
          title: '执行接口id',
          dataIndex: 'restApiForEx.uid',
          key: 'restApiForEx.uid',
        },
        {
          title: "执行参数",
          dataIndex: 'operation',
          slots: {
            customRender: 'operation',
          },
        },
        {
          title: "观察",
          dataIndex: 'watcher_list',
          slots: {
            customRender: 'watcher_list',
          },
        }

      ],
      rest_api_return:
          {
            "id": "62834c5310934224f8bd7b2c",
            "name": "测试",
            "desc": "测试",
            "works": [
              {
                "step": "1",
                "async": false,
                "restId": "62833f1858f599635e0a8cd5",
                "restApiForEx": {
                  "uid": "62833f1858f599635e0a8cd5",
                  "id": null,
                  "url": "/getAge",
                  "method": "get",
                  "desc": "getAge",
                  "params": [
                    {
                      "flag": null,
                      "in": "query",
                      "name": "uid",
                      "require": true,
                      "type": "string",
                      "id": null,
                      "defaultValue": null,
                      "paramEntities": null,
                      "extract": {
                        "el": "$.uuu",
                        "elType": "JSON_PATH",
                        "step": null
                      }
                    }
                  ],
                  "res": [
                    {
                      "flag": null,
                      "in": null,
                      "name": null,
                      "require": false,
                      "type": "integer",
                      "id": null,
                      "defaultValue": null,
                      "paramEntities": [],
                      "extract": null
                    }
                  ]
                },
                "watchers": [
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
                ]
              }
            ],
            "resultId": "62834c5210934224f8bd7b2b"
          },
      visible: false,

      pp: {},
      watcher_visible: false,

      pp_watcher: {},

    };
  },
  created() {
    if (this.input_data) {
      console.log("flow-ui-init ", JSON.stringify(this.input_data));
      this.rest_api_return = this.input_data;

    }
  },
  methods: {
    show_swagger(f) {
      console.log("11111111111111");
      this.pp = f.restApiForEx;
      console.log("pp" ,JSON.stringify(this.pp));
      this.visible = true;
    },
    show_watcher(f) {
      console.log("开始观察");
      console.log("watcher ", JSON.stringify(f.watchers));
      this.pp_watcher = f.watchers;
      this.watcher_visible = true;
    },
  },
  setup() {

  },
}
</script>

<style scoped>

</style>
