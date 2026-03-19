<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item label="客户名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入客户名称查询" v-model="queryParam.customerName"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="经手人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入经手人查询" v-model="queryParam.handlerName"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-range-picker v-model="queryParam.timeRange" @change="onDateChange" />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                  <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
        </div>
        <!-- 操作按钮区域 -->
        <div class="table-operator" style="margin-top: 5px">
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="handleAdd" type="primary" icon="plus">新增</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchDel" icon="delete">删除</a-button>
        </div>
        <!-- table区域-begin -->
        <div>
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :scroll="scroll"
            :loading="loading"
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            @change="handleTableChange">
            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                <a>删除</a>
              </a-popconfirm>
            </span>
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <customer-handler-record-modal ref="modalForm" @ok="modalFormOk"></customer-handler-record-modal>
      </a-card>
    </a-col>
  </a-row>
</template>

<script>
  import CustomerHandlerRecordModal from './modules/CustomerHandlerRecordModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction } from '@/api/manage'

  export default {
    name: "CustomerHandlerRecordList",
    mixins:[JeecgListMixin],
    components: {
      CustomerHandlerRecordModal
    },
    data () {
      return {
        labelCol: {
          span: 5
        },
        wrapperCol: {
          span: 18,
          offset: 1
        },
        // 查询条件
        queryParam: {
          customerName:'',
          handlerName:'',
          beginTime:'',
          endTime:''
        },
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            width: 130,
            align:"center",
            scopedSlots: { customRender: 'action' },
          },
          { title: '客户名称',dataIndex: 'customerName',width:150,align:"left"},
          { title: '经手人', dataIndex: 'handlerName',width:100,align:"left"},
          { title: '开始时间', dataIndex: 'followUpTime',width:150,align:"left"},
          { title: '结束时间', dataIndex: 'nextFollowUpTime',width:150,align:"left"},
          { title: '交接原因', dataIndex: 'content',width:200,align:"left"},
          { title: '备注', dataIndex: 'remark',width:200,align:"left"}
        ],
        url: {
          list: "/customerFollowUp/handlerRecordList",
          delete: "/customerFollowUp/delete",
          deleteBatch: "/customerFollowUp/deleteBatch"
        }
      }
    },
    methods: {
      onDateChange(date, dateString) {
        this.queryParam.beginTime = dateString[0]
        this.queryParam.endTime = dateString[1]
      },
      searchReset() {
        this.queryParam = {}
        this.loadData(1)
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
