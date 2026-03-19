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
                  <a-select
                    placeholder="请选择客户"
                    v-model="queryParam.customerId"
                    showSearch
                    optionFilterProp="children"
                    @change="handleCustomerChange">
                    <a-select-option v-for="item in customerList" :key="item.id" :value="item.id">
                      {{ item.supplier }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="单据类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select placeholder="请选择单据类型" v-model="queryParam.type">
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option value="销售订单">销售订单</a-select-option>
                    <a-select-option value="销售出库">销售出库</a-select-option>
                    <a-select-option value="销售退货">销售退货</a-select-option>
                    <a-select-option value="零售出库">零售出库</a-select-option>
                    <a-select-option value="零售退货">零售退货</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="单据日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
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
          <a-button @click="handleExportXls('客户历史订单')" icon="download">导出</a-button>
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
            @change="handleTableChange">
            <span slot="action" slot-scope="text, record">
              <a @click="handleViewDetail(record)">查看详情</a>
            </span>
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 详情弹窗 -->
        <bill-detail ref="billDetailModal"></bill-detail>
      </a-card>
    </a-col>
  </a-row>
</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction } from '@/api/manage'
  import BillDetail from '@/views/bill/dialog/BillDetail'

  export default {
    name: "CustomerOrderHistoryList",
    mixins:[JeecgListMixin],
    components: {
      BillDetail
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
          customerId: null,
          type:'',
          beginTime:'',
          endTime:''
        },
        customerList: [],
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
            width: 100,
            align:"center",
            scopedSlots: { customRender: 'action' },
          },
          { title: '单据编号',dataIndex: 'number',width:150,align:"left"},
          { title: '单据类型', dataIndex: 'type',width:100,align:"left"},
          { title: '客户名称', dataIndex: 'organName',width:150,align:"left"},
          { title: '单据日期', dataIndex: 'billTime',width:120,align:"left"},
          { title: '商品信息', dataIndex: 'materialsList',width:200,align:"left"},
          { title: '数量', dataIndex: 'totalNum',width:80,align:"right"},
          { title: '金额', dataIndex: 'totalPrice',width:100,align:"right"},
          { title: '经手人', dataIndex: 'operPersonName',width:100,align:"left"},
          { title: '状态', dataIndex: 'status',width:80,align:"center"}
        ],
        url: {
          list: "/depotHead/list",
          exportXlsUrl: "/depotHead/exportExcel"
        }
      }
    },
    created() {
      this.loadCustomerList()
    },
    methods: {
      loadCustomerList() {
        getAction('/supplier/findBySelect', { type: '客户' }).then(res => {
          if(res.code === 200) {
            this.customerList = res.data
          }
        })
      },
      handleCustomerChange(value) {
        this.queryParam.customerId = value
      },
      onDateChange(date, dateString) {
        this.queryParam.beginTime = dateString[0]
        this.queryParam.endTime = dateString[1]
      },
      searchQuery() {
        if(!this.queryParam.customerId) {
          this.$message.warning('请先选择客户')
          return
        }
        this.loadData(1)
      },
      searchReset() {
        this.queryParam = {
          type: '',
          beginTime: '',
          endTime: ''
        }
        this.loadData(1)
      },
      getQueryParams() {
        let param = Object.assign({}, this.queryParam)
        param.organId = this.queryParam.customerId
        param.pageSize = this.ipagination.pageSize
        param.currentPage = this.ipagination.current
        return param
      },
      handleViewDetail(record) {
        this.$refs.billDetailModal.show(record.id, record.type)
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
