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
import axios from "axios";


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
      rest_api_return: {}
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
    console.log("kaishi");
    console.log(this.param_inp);

    if (this.uid) {
      this.search_api(this.uid);
    }



  },
  methods: {
    change_param_node(text, record, index, e) {
      record.value = e.target.value;
      console.log(text, record, index, e.target.value);
    },

    search_api(uid) {
      console.log("开始")
      const api_by_id = 'http://localhost:8080/rest_api/by_id';
      axios.get(api_by_id, {
        params: {
          uid: uid,
        },
      }).then(res => {
        console.log("接口响应")
        console.log(res.data);
        this.rest_api_return = res.data;

      }).catch(e=>{
        console.log(e);
      })
    }
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
