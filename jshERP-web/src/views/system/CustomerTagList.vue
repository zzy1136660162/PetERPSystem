<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item label="标签名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入标签名称查询" v-model="queryParam.tagName"></a-input>
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
            <template slot="customRenderColor" slot-scope="color">
              <span>
                <a-tag :color="color">{{ color }}</a-tag>
              </span>
            </template>
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <customer-tag-modal ref="modalForm" @ok="modalFormOk"></customer-tag-modal>
      </a-card>
    </a-col>
  </a-row>
</template>

<script>
  import CustomerTagModal from './modules/CustomerTagModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { deleteCustomerTag } from '@/api/api'
  export default {
    name: "CustomerTagList",
    mixins:[JeecgListMixin],
    components: {
      CustomerTagModal
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
          tagName:''
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
          { title: '标签名称', dataIndex: 'tagName', width: 150, align: "left" },
          { title: '标签颜色', dataIndex: 'tagColor', width: 120, align: "center",
            scopedSlots: { customRender: 'customRenderColor' }
          },
          { title: '排序', dataIndex: 'sort', width: 80, align: "center" },
          { title: '备注', dataIndex: 'remark', width: 200, align: "left" },
          { title: '创建时间', dataIndex: 'createTime', width: 150, align: "left" }
        ],
        url: {
          list: "/customerTag/list",
          delete: "/customerTag/delete"
        }
      }
    },
    methods: {
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
            deleteCustomerTag({ ids: ids }).then((res) => {
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
