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
                <a-form-item label="客户等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select placeholder="请选择客户等级" v-model="queryParam.customerLevel">
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option value="VIP客户">VIP客户</a-select-option>
                    <a-select-option value="重要客户">重要客户</a-select-option>
                    <a-select-option value="普通客户">普通客户</a-select-option>
                    <a-select-option value="新客户">新客户</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="价值等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select placeholder="请选择价值等级" v-model="queryParam.valueLevel">
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option value="高价值客户">高价值客户</a-select-option>
                    <a-select-option value="重要客户">重要客户</a-select-option>
                    <a-select-option value="普通客户">普通客户</a-select-option>
                    <a-select-option value="潜在客户">潜在客户</a-select-option>
                  </a-select>
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
                  <a-form-item label="客户来源" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择客户来源" v-model="queryParam.customerSource">
                      <a-select-option value="">全部</a-select-option>
                      <a-select-option value="线上推广">线上推广</a-select-option>
                      <a-select-option value="线下活动">线下活动</a-select-option>
                      <a-select-option value="客户推荐">客户推荐</a-select-option>
                      <a-select-option value="自然进店">自然进店</a-select-option>
                      <a-select-option value="其他">其他</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker style="width: 100%" placeholder="请选择开始时间" v-model="queryParam.beginTime" format="YYYY-MM-DD" />
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-date-picker style="width: 100%" placeholder="请选择结束时间" v-model="queryParam.endTime" format="YYYY-MM-DD" />
                  </a-form-item>
                </a-col>
              </a-row>
            </template>
          </a-form>
        </div>
        <!-- 操作按钮区域 -->
        <div class="table-operator" style="margin-top: 5px">
          <a-button v-if="btnEnableList.indexOf(3)>-1" @click="handleExportXls('客户分析')" icon="download">导出</a-button>
        </div>
        <!-- table区域-begin -->
        <div>
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="customerId"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :scroll="scroll"
            :loading="loading"
            @change="handleTableChange">
            <template slot="customRenderValueLevel" slot-scope="valueLevel">
              <a-tag v-if="valueLevel === '高价值客户'" color="red">{{ valueLevel }}</a-tag>
              <a-tag v-else-if="valueLevel === '重要客户'" color="orange">{{ valueLevel }}</a-tag>
              <a-tag v-else-if="valueLevel === '普通客户'" color="blue">{{ valueLevel }}</a-tag>
              <a-tag v-else color="default">{{ valueLevel }}</a-tag>
            </template>
            <template slot="customRenderFollowStatus" slot-scope="followUpStatus">
              <a-tag v-if="followUpStatus === '未跟进'" color="orange">未跟进</a-tag>
              <a-tag v-else-if="followUpStatus === '需跟进'" color="red">需跟进</a-tag>
              <a-tag v-else color="green">跟进正常</a-tag>
            </template>
          </a-table>
        </div>
        <!-- table区域-end -->
      </a-card>
    </a-col>
  </a-row>
</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDate from '@/components/jeecg/JDate'
  export default {
    name: "CustomerAnalysisList",
    mixins:[JeecgListMixin],
    components: {
      JDate
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
        queryParam: {
          customerName:'',
          customerLevel:'',
          customerSource:'',
          valueLevel:'',
          beginTime:'',
          endTime:''
        },
        ipagination:{
          pageSizeOptions: ['10', '20', '30', '100', '200']
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
          { title: '客户名称', dataIndex: 'customerName', width: 150, align: "left" },
          { title: '手机号码', dataIndex: 'phone', width: 120, align: "left" },
          { title: '客户等级', dataIndex: 'customerLevel', width: 100, align: "center" },
          { title: '客户来源', dataIndex: 'customerSource', width: 100, align: "center" },
          { title: '订单数', dataIndex: 'orderCount', width: 80, align: "center" },
          { title: '总金额', dataIndex: 'totalAmount', width: 100, align: "right" },
          { title: '平均金额', dataIndex: 'avgAmount', width: 100, align: "right" },
          { title: '最后下单时间', dataIndex: 'lastOrderTime', width: 150, align: "center" },
          { title: '距今天数', dataIndex: 'daysSinceLastOrder', width: 90, align: "center" },
          { title: '价值等级', dataIndex: 'valueLevel', width: 100, align: "center",
            scopedSlots: { customRender: 'customRenderValueLevel' }
          },
          { title: '跟进次数', dataIndex: 'followUpCount', width: 90, align: "center" },
          { title: '最后跟进时间', dataIndex: 'lastFollowUpTime', width: 150, align: "center" },
          { title: '跟进状态', dataIndex: 'followUpStatus', width: 100, align: "center",
            scopedSlots: { customRender: 'customRenderFollowStatus' }
          }
        ],
        url: {
          list: "/supplier/customerAnalysis",
          exportXlsUrl: "/supplier/exportCustomerAnalysis"
        }
      }
    },
    methods: {
      searchQuery() {
        this.loadData(1);
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
