<template>
<div>
  响应体

  <a-table :columns="columns" :data-source="dt.fields" :pagination="pagination"
           childrenColumnName="properties"
           defaultExpandAllRows="true"
           row-key="uid">
    <template #operation="{ record }">

      <a @click="show_swagger(record)">查看</a>


    </template>

  </a-table>
</div>
</template>

<script>
export default {
  name: "result-ui",
  props: {
    input_data: Object,

  },
  created() {
    this.dt = this.input_data;
  },
  data() {
    return {
      columns: [
        {
          title: '字段名称',
          dataIndex: 'fieldName',
          key: 'fieldName',
        },
        {
          title: '字段类型',
          dataIndex: 'type',
          key: 'type',
        },
        {
          title: '取值步骤',
          dataIndex: 'extract.step',
          key: 'extract.step',
        }, {
          title: '取值表达',
          dataIndex: 'extract.el',
          key: 'extract.el',
        },
        {
          title: '默认值',
          dataIndex: 'value',
          key: 'value',
        }

      ],
      dt: {
        "id": "6284996796e28d6fc3e1c0da",
        "flowId": null,
        "fields": [{
          "fieldName": "sss",
          "type": "STRING",
          "value": null,
          "extract": {"el": "$.name", "elType": "JSON_PATH", "step": "2"},
          "properties": []
        }, {
          "fieldName": "age",
          "type": "INTEGER",
          "value": null,
          "extract": {"el": "$.age", "elType": "JSON_PATH", "step": "1"},
          "properties": []
        }]
      }
    }

  },
};
</script>

<style scoped>

</style>
