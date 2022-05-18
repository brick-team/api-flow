<template>
  <div>
    <h2>接口列表</h2>

    <a-row v-if="show_filter">

      <a-col :span="12">
        <a-input v-model:value="api_desc" placeholder="输入接口描述"/>
      </a-col>
      <a-col :span="12">
        <a-input v-model:value="api_path" placeholder="输入接口地址"/>

      </a-col>
      <a-col :span="12">
        <a-button type="primary" @click="search_api(1, pageSize)">搜索</a-button>
      </a-col>
    </a-row>

    <a-table :columns="columns" :data-source="res_ret.content" :pagination="pagination"
             childrenColumnName="paramEntities"
             defaultExpandAllRows="true"
             row-key="uid">
      <template #operation="{ record }">

        <a @click="show_swagger(record)">查看</a>


      </template>

    </a-table>

    <a-modal v-model:visible="visible" destroyOnClose="true" title="信息" width="75%">
      <swagger-ui :show_op="show_op" :param_inp="fd"/>
    </a-modal>
  </div>
</template>

<script>
import SwaggerUi from "@/view/swagger-ui";
import axios from "axios";

export default {

  name: "swagger-list",
  components: {SwaggerUi},
  props: {
    show_step: Boolean,
    input_data: Object,
    show_filter: Boolean,

  },

  data() {
    return {
      show_op: false,
      uid: 1,
      pagination: {
        pageSize: 0,
        current: 1,
        total: 0,
        onChange: (page, pageSize) => {
          console.log(page, pageSize);
          this.search_api(page, pageSize);
        }
      },
      columns: [
        {
          title: '接口地址',
          dataIndex: 'url',
          key: 'url',
        },
        {
          title: '请求方式',
          dataIndex: 'method',
          key: 'method',
        },
        {
          title: '接口说明',
          dataIndex: 'desc',
          key: 'desc',
        },
        {
          title: '步骤号',
          dataIndex: 'step',
          key: 'step',
          slots: {
            customRender: 'step',
          },
        },
        {
          title: '操作',
          dataIndex: 'operation',
          slots: {
            customRender: 'operation',
          },
        }

      ],
      res_ret: {
        "content": [
          {
            "uid": "62833f1858f599635e0a8cd4",
            "id": null,
            "url": "/flow",
            "method": "get",
            "desc": "flow",
            "params": [],
            "res": []
          },
          {
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
                "paramEntities": [],
                "extract": null
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
          {
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
                "extract": null
              }
            ],
            "res": []
          }
        ],
        "pageable": {
          "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
          },
          "pageNumber": 0,
          "pageSize": 3,
          "offset": 0,
          "paged": true,
          "unpaged": false
        },
        "last": false,
        "totalPages": 3,
        "totalElements": 8,
        "first": true,
        "numberOfElements": 3,
        "size": 3,
        "number": 0,
        "sort": {
          "sorted": false,
          "unsorted": true,
          "empty": true
        },
        "empty": false
      },

      visible: false,
      api_desc: "",
      api_path: "",
      fd: {},

    };
  },
  created() {
    if (this.input_data) {
      this.res_ret = this.input_data;
      console.log("外部输入", JSON.stringify(this.input_data));
    } else {
      this.search_api(1, 3);

    }

  },
  methods: {

    search_api(page, size) {
      const api_page = 'http://localhost:8080/rest_api/page';
      const param = {
        url: this.api_path,
        desc: this.api_desc,
        page: page - 1,
        size: size,
      };
      console.log(param);
      axios.get(api_page, {
        params: param,
      }).then(res => {
        console.log(res.data);
        this.res_ret = res.data;
        this.pagination.pageSize = res.data.pageable.pageSize;
        this.pagination.current = res.data.pageable.pageNumber + 1;
        this.pagination.total = res.data.totalElements;
        console.log(this.pagination);

      }).catch(e => {
        console.log(e);
      })
    },

    show_swagger(key) {
      if (this.input_data) {
        console.log("data = ", this.input_data);
        this.fd = this.input_data.content[0];
        console.log("即将输出 ", JSON.stringify(this.fd));
        this.show_op = true;
        this.visible = true;


      } else {
        console.log(key);
        console.log(key.uid);

        const api_by_id = 'http://localhost:8080/rest_api/by_id';

        axios.get(api_by_id, {
          params: {
            uid: key.uid,
          },
        }).then(res => {
          console.log("数据信息" + JSON.stringify(res.data));
          this.fd = res.data;
          this.visible = true;
        }).catch(e => {
          console.log(e);
        });

      }
    },
  },


  setup() {

  },
};
</script>

<style scoped>

</style>
