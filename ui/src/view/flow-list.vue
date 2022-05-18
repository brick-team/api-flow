<template>
  <div>
    流程列表

    <a-row v-if="show_filter">
      <a-col :span="12">
        <a-input v-model:value="flow_desc" placeholder="输入流程描述"/>
      </a-col>
      <a-col :span="12">
        <a-input v-model:value="flow_name" placeholder="输入流程名称"/>

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

        <a @click="show_flow(record)">查看</a>


      </template>
    </a-table>

    <a-modal v-model:visible="visible" destroyOnClose="true" title="flow详情" width="75%">
      <flow-ui :input_data="dd"></flow-ui>

    </a-modal>
  </div>
</template>

<script>
import axios from "axios";
import FlowUi from "@/view/flow-ui";

export default {
  name: "flow-list",
  components: {FlowUi},
  props: {
    input_data: Object,
    show_filter: Boolean,

  },
  data() {
    return {
      visible: false,
      dd: {},
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
          title: '流程说明',
          dataIndex: 'desc',
          key: 'desc',
        },
        {
          title: '流程名称',
          dataIndex: 'name',
          key: 'name',
        },

        {
          title: '操作',
          dataIndex: 'operation',
          slots: {
            customRender: 'operation',
          },
        }

      ],
      res_ret: {},
      flow_desc: '',
      flow_name: '',

    };
  },
  created() {
    if (this.input_data) {
      console.log(22312313)
      console.log(this.input_data);
      this.res_ret = this.input_data;
    } else {
      this.search_api(1, 3);
    }
  },
  methods: {
    show_flow(e) {
      console.log("准备写出", e.works);
      this.dd = e;
      this.visible = true;
    },
    search_api(page, size) {
      const api_page = 'http://localhost:8080/flow/page';
      const param = {
        name: this.flow_name,
        desc: this.flow_desc,
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
  },
  setup() {

  },
}
</script>

<style scoped>

</style>
