<template>
  <div>

    <!--    <button @click="console_log_data">输出</button>-->

    <span>接口地址= {{ rest_api_return.url }}</span>
    <span>请求方式= {{ rest_api_return.method }}</span>
    <span>接口描述= {{ rest_api_return.desc }}</span>


    <a-table :columns="columns" :data-source="rest_api_return.params" childrenColumnName="paramEntities"
             defaultExpandAllRows="true"
             row-key="id">
      <template v-if="show_op" #value="{ text, record ,index}">
        <div class="editable-cell">
          <div class="editable-cell-input-wrapper">
            <a-input :value="record.value" @change="(e) =>change_param_node(text, record ,index,e)"/>
          </div>
        </div>
      </template>
    </a-table>
  </div>
</template>

<script>
import {defineComponent} from 'vue';


export default defineComponent({
  name: "swagger-ui",
  props: {
    show_op: Boolean,
    uid: String,
    param_inp: Object,

  },
  data() {
    return {
      columns: [
        {
          title: '参数名称',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '参数位置',
          dataIndex: 'in',
          key: 'in',
        },
        {
          title: '数据类型',
          dataIndex: 'type',
          key: 'type',
          width: '12%',
        },
      ],
      rest_api_return: {
        "uid": "62833f1858f599635e0a8cd5",
        "id": null,
        "url": "/getAge",
        "method": "get",
        "desc": "getAge",
        "params": [{
          "flag": null,
          "in": "query",
          "name": "uid",
          "require": true,
          "type": "string",
          "id": null,
          "defaultValue": null,
          "paramEntities": [],
          "extract": null
        }],
        "res": [{
          "flag": null,
          "in": null,
          "name": null,
          "require": false,
          "type": "integer",
          "id": null,
          "defaultValue": null,
          "paramEntities": [],
          "extract": null
        }]
      }
    }

  },
  created() {
    if (this.show_op) {
      this.columns.push({
        title: '取值表达式',
        dataIndex: 'extract.el',
        key: 'extract.el',
        width: '12%',
        slots: {
          customRender: 'extract.el',
        },
      },);

    }
    console.log("开始初始化swagger-ui");
    if (this.param_inp) {
      console.log("初始化数据= ", JSON.stringify(this.param_inp));
      this.rest_api_return = this.param_inp;
    }


  },
  methods: {


  },
  setup(props) {
    console.log(props);
    console.log(props.uid);
    console.log(props.param_inp);
    return {};
  },
})


</script>

<style scoped>

</style>
