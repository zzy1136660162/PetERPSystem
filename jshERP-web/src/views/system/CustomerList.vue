<!-- f r o m 7 5  2 7 1  8 9 2 0 -->
<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入名称查询" v-model="queryParam.supplier"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入联系人查询" v-model="queryParam.contacts"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入手机号码查询" v-model="queryParam.telephone"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                  <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
                  <a @click="handleToggleSearch" style="margin-left: 8px">
                    {{ toggleSearchStatus ? '收起' : '展开' }}
                    <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                  </a>
                </span>
              </a-col>
            </a-row>
            <template v-if="toggleSearchStatus">
              <a-row :gutter="24">
                <a-col :md="6" :sm="24">
                  <a-form-item label="联系电话" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="请输入联系电话查询" v-model="queryParam.phonenum"></a-input>
                  </a-form-item>
                </a-col>
              </a-row>
            </template>
          </a-form>
        </div>
        <!-- 操作按钮区域 -->
        <div class="table-operator"  style="margin-top: 5px">
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="handleAdd" type="primary" icon="plus">新增</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchDel" icon="delete">删除</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchSetStatus(true)" icon="check-square">启用</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchSetStatus(false)" icon="close-square">禁用</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="handleImportXls()" icon="import">导入</a-button>
          <a-button v-if="btnEnableList.indexOf(3)>-1" @click="handleExportXls('客户信息')" icon="download">导出</a-button>
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
              <a v-if="btnEnableList.indexOf(1)>-1 && customerFlag === '1' && quickBtn.user.indexOf(1)>-1 " @click="btnSetUser(record)" style="font-size: 12px;">分配</a>
              <a-divider v-if="btnEnableList.indexOf(1)>-1 && customerFlag === '1' && quickBtn.user.indexOf(1)>-1 " type="vertical" style="margin: 0 4px;" />
              <a @click="handleEdit(record)" style="font-size: 12px;">编辑</a>
              <a-divider type="vertical" style="margin: 0 4px;" />
              <a @click="btnViewHandler(record)" style="font-size: 12px;">经手记录</a>
              <a-divider type="vertical" style="margin: 0 4px;" />
              <a @click="btnViewOrder(record)" style="font-size: 12px;">历史订单</a>
              <a-divider type="vertical" style="margin: 0 4px;" />
              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                <a style="font-size: 12px;">删除</a>
              </a-popconfirm>
            </span>
            <!-- 状态渲染模板 -->
            <template slot="customRenderFlag" slot-scope="enabled">
              <a-tag v-if="enabled" color="green">启用</a-tag>
              <a-tag v-if="!enabled" color="orange">禁用</a-tag>
            </template>
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <customer-modal ref="modalForm" @ok="modalFormOk"></customer-modal>
        <customer-user-modal ref="customerUserModal" @ok="modalFormOk"></customer-user-modal>
        <customer-handler-record-modal ref="customerHandlerRecordModal"></customer-handler-record-modal>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
  import CustomerModal from './modules/CustomerModal'
  import CustomerUserModal from './modules/CustomerUserModal'
  import CustomerHandlerRecordModal from './modules/CustomerHandlerRecordModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction } from '@/api/manage'
  import Vue from 'vue'

  export default {
    name: "CustomerList",
    mixins:[JeecgListMixin],
    components: {
      CustomerModal,
      CustomerUserModal,
      CustomerHandlerRecordModal
    },
    data () {
      return {
        customerFlag: '2', //客户标记
        // 查询条件
        queryParam: {
          supplier:'',
          contacts:'',
          telephone:'',
          phonenum:''
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
            width: 200,
            align:"center",
            scopedSlots: { customRender: 'action' },
          },
          { title: '名称',dataIndex: 'supplier',width:100,align:"left"},
          { title: '联系人', dataIndex: 'contacts',width:70,align:"left"},
          { title: '手机号码', dataIndex: 'telephone',width:100,align:"left"},
          { title: '联系电话', dataIndex: 'phoneNum',width:100,align:"left"},
          // { title: '电子邮箱', dataIndex: 'email',width:150,align:"left"},
          { title: '期初应收',dataIndex: 'beginNeedGet',width:80,align:"left"},
          { title: '期末应收',dataIndex: 'allNeedGet',width:80,align:"left"},
          { title: '税率(%)', dataIndex: 'taxRate',width:80,align:"left"},
          { title: '排序', dataIndex: 'sort', width: 60,align:"left"},
          { title: '当前经手人', dataIndex: 'handlerName',width:100,align:"left"},
          { title: '状态',dataIndex: 'enabled',width:60, align:"center",
            scopedSlots: { customRender: 'customRenderFlag' }
          }
        ],
        url: {
          list: "/supplier/list",
          delete: "/supplier/delete",
          deleteBatch: "/supplier/deleteBatch",
          importExcelUrl: "/supplier/importCustomer",
          exportXlsUrl: "/supplier/exportExcel",
          batchSetStatusUrl: "/supplier/batchSetStatus"
        }
      }
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}${this.url.importExcelUrl}`;
      }
    },
    created() {
      this.initQuickBtn()
    },
    methods: {
      initQuickBtn() {
        this.quickBtn = {
          user: ''
        }
        getAction('/user/getCurrentUser').then((res) =>{
          if(res.code === 200){
            this.quickBtn.user = res.data.btnStr
          }
        })
      },
      //分配用户
      btnSetUser(record) {
        this.$refs.customerUserModal.edit(record)
        this.$refs.customerUserModal.title = "分配用户"
        this.$refs.customerUserModal.disableSubmit = false
      },
      //查看经手记录
      btnViewHandler(record) {
        this.$refs.customerHandlerRecordModal.edit(record)
        this.$refs.customerHandlerRecordModal.title = "经手记录"
      },
      //查看历史订单
      btnViewOrder(record) {
        this.$message.info('历史订单功能暂不可用')
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  
  /* 优化操作列样式 */
  .ant-table-tbody > tr > td {
    white-space: nowrap !important;
  }
  
  /* 优化操作按钮间距 */
  .ant-table-tbody > tr > td a {
    display: inline-block;
    padding: 2px 4px;
  }
  
  /* 优化分隔线样式 */
  .ant-table-tbody > tr > td .ant-divider-vertical {
    height: 14px;
    margin: 0 2px;
  }
</style>