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
                <a-form-item label="跟进类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select placeholder="请选择跟进类型" v-model="queryParam.followUpType">
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option value="电话沟通">电话沟通</a-select-option>
                    <a-select-option value="上门拜访">上门拜访</a-select-option>
                    <a-select-option value="邮件沟通">邮件沟通</a-select-option>
                    <a-select-option value="微信沟通">微信沟通</a-select-option>
                    <a-select-option value="其他">其他</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="跟进状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select placeholder="请选择跟进状态" v-model="queryParam.followUpStatus">
                    <a-select-option value="">全部</a-select-option>
                    <a-select-option value="待跟进">待跟进</a-select-option>
                    <a-select-option value="跟进中">跟进中</a-select-option>
                    <a-select-option value="已成交">已成交</a-select-option>
                    <a-select-option value="已放弃">已放弃</a-select-option>
                  </a-select>
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
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchSetStatus('跟进中')" icon="check-square">标记跟进中</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchSetStatus('已成交')" icon="check-circle">标记已成交</a-button>
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
            <template slot="customRenderStatus" slot-scope="followUpStatus">
              <a-tag v-if="followUpStatus === '待跟进'" color="orange">待跟进</a-tag>
              <a-tag v-if="followUpStatus === '跟进中'" color="blue">跟进中</a-tag>
              <a-tag v-if="followUpStatus === '已成交'" color="green">已成交</a-tag>
              <a-tag v-if="followUpStatus === '已放弃'" color="red">已放弃</a-tag>
            </template>
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <customer-follow-up-modal ref="modalForm" @ok="modalFormOk"></customer-follow-up-modal>
      </a-card>
    </a-col>
  </a-row>
</template>

<script>
  import CustomerFollowUpModal from './modules/CustomerFollowUpModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { deleteBatchCustomerFollowUp, batchSetFollowUpStatus } from '@/api/api'
  export default {
    name: "CustomerFollowUpList",
    mixins:[JeecgListMixin],
    components: {
      CustomerFollowUpModal
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
          followUpType:'',
          followUpStatus:''
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
          { title: '客户名称', dataIndex: 'customerName', width: 150, align: "left" },
          { title: '跟进时间', dataIndex: 'followUpTime', width: 150, align: "left" },
          { title: '跟进类型', dataIndex: 'followUpType', width: 100, align: "left" },
          { title: '跟进内容', dataIndex: 'content', width: 200, align: "left" },
          { title: '下次跟进时间', dataIndex: 'nextFollowUpTime', width: 150, align: "left" },
          { title: '跟进状态', dataIndex: 'followUpStatus', width: 100, align: "center",
            scopedSlots: { customRender: 'customRenderStatus' }
          },
          { title: '跟进人', dataIndex: 'operator', width: 100, align: "left" },
          { title: '备注', dataIndex: 'remark', width: 150, align: "left" }
        ],
        url: {
          list: "/customerFollowUp/list",
          delete: "/customerFollowUp/delete",
          deleteBatch: "/customerFollowUp/deleteBatch"
        }
      }
    },
    methods: {
      batchSetStatus(status) {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        }
        const ids = this.selectedRowKeys.join(",");
        const that = this;
        this.$confirm({
          title: "确认操作",
          content: "是否操作选中数据?",
          onOk: function () {
            batchSetFollowUpStatus({ ids: ids, status: status }).then((res) => {
              if (res.code === 200) {
                that.$message.success("操作成功!");
                that.loadData();
                that.onClearSelected();
              } else {
                that.$message.warning(res.data.message);
              }
            });
          }
        });
      },
      batchDel: function () {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        }
        const ids = this.selectedRowKeys.join(",");
        const that = this;
        this.$confirm({
          title: "确认删除",
          content: "是否删除选中数据?",
          onOk: function () {
            deleteBatchCustomerFollowUp({ ids: ids }).then((res) => {
              if (res.code === 200) {
                that.$message.success("删除成功!");
                that.loadData();
                that.onClearSelected();
              } else {
                that.$message.warning(res.data.message);
              }
            });
          }
        });
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>
