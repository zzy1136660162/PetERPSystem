<!-- by 7527189 2 0 -->
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
                <a-form-item label="类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-tree-select style="width:100%" :dropdownStyle="{maxHeight:'200px',overflow:'auto'}" allow-clear
                   :treeData="categoryTree" v-model="queryParam.categoryId" placeholder="请选择类别">
                  </a-tree-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="关键词" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入条码、名称、助记码等查询" v-model="queryParam.materialParam"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入规格查询" v-model="queryParam.standard"></a-input>
                </a-form-item>
              </a-col>
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-col :md="6" :sm="24">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                  <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
                  <a @click="handleToggleSearch" style="margin-left: 8px">
                    {{ toggleSearchStatus ? '收起' : '展开' }}
                    <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                  </a>
                </a-col>
              </span>
            </a-row>
            <template v-if="toggleSearchStatus">
              <a-row :gutter="24">
                <a-col :md="6" :sm="24">
                  <a-form-item label="型号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="请输入型号查询" v-model="queryParam.model"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="颜色" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="请输入颜色查询" v-model="queryParam.color"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="品牌" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="请输入品牌查询" v-model="queryParam.brand"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="制造商" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="请输入制造商查询" v-model="queryParam.mfrs"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item :label="queryTitle.mp1" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :placeholder="'请输入'+ queryTitle.mp1 +'查询'" v-model="queryParam.otherField1"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item :label="queryTitle.mp2" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :placeholder="'请输入'+ queryTitle.mp2 +'查询'" v-model="queryParam.otherField2"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item :label="queryTitle.mp3" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input :placeholder="'请输入'+ queryTitle.mp3 +'查询'" v-model="queryParam.otherField3"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择状态" v-model="queryParam.enabled">
                      <a-select-option value="1">启用</a-select-option>
                      <a-select-option value="0">禁用</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="序列号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="有无序列号" v-model="queryParam.enableSerialNumber">
                      <a-select-option value="1">有</a-select-option>
                      <a-select-option value="0">无</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="批号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="有无批号" v-model="queryParam.enableBatchNumber">
                      <a-select-option value="1">有</a-select-option>
                      <a-select-option value="0">无</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="仓位货架" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input style="width: 100%" placeholder="请输入仓位货架查询" v-model="queryParam.position"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="基础重量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number style="width: 100%" placeholder="请输入基础重量查询" v-model="queryParam.weight"></a-input-number>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="保质期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input-number style="width: 100%" placeholder="请输入保质期查询" v-model="queryParam.expiryNum"></a-input-number>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="请输入备注查询" v-model="queryParam.remark"></a-input>
                  </a-form-item>
                </a-col>
                <!-- 宠物相关查询条件 -->
                <a-col :md="6" :sm="24">
                  <a-form-item label="宠物类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择宠物类型" v-model="queryParam.petType">
                      <a-select-option value="">全部</a-select-option>
                      <a-select-option value="猫">猫</a-select-option>
                      <a-select-option value="狗">狗</a-select-option>
                      <a-select-option value="其他">其他</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="品种" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-input placeholder="请输入品种查询" v-model="queryParam.petBreed"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="健康状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择健康状态" v-model="queryParam.healthStatus">
                      <a-select-option value="">全部</a-select-option>
                      <a-select-option value="健康">健康</a-select-option>
                      <a-select-option value="观察中">观察中</a-select-option>
                      <a-select-option value="治疗中">治疗中</a-select-option>
                      <a-select-option value="禁售">禁售</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="疫苗状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择疫苗状态" v-model="queryParam.vaccineStatus">
                      <a-select-option value="">全部</a-select-option>
                      <a-select-option value="未接种">未接种</a-select-option>
                      <a-select-option value="部分接种">部分接种</a-select-option>
                      <a-select-option value="已完成">已完成</a-select-option>
                    </a-select>
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
          <a-button v-if="btnEnableList.indexOf(3)>-1" @click="handleExportXls('商品信息')" icon="download">导出</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchEdit()" icon="edit">批量编辑</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchSetMaterialCurrentStock()" icon="stock">修正库存</a-button>
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="batchSetMaterialCurrentUnitPrice()" icon="fund">修正成本</a-button>
          <a-popover trigger="click" placement="right">
            <template slot="content">
              <a-checkbox-group @change="onColChange" v-model="settingDataIndex" :defaultValue="settingDataIndex">
                <a-row style="width: 500px">
                  <template v-for="(item,index) in defColumns">
                    <template>
                      <a-col :span="8">
                        <a-checkbox :value="item.dataIndex">
                          <j-ellipsis :value="item.title" :length="10"></j-ellipsis>
                        </a-checkbox>
                      </a-col>
                    </template>
                  </template>
                </a-row>
                <a-row style="padding-top: 10px;">
                  <a-col>
                    恢复默认列配置：<a-button @click="handleRestDefault" type="link" size="small">恢复默认</a-button>
                  </a-col>
                </a-row>
              </a-checkbox-group>
            </template>
            <a-button icon="setting">列设置</a-button>
          </a-popover>
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
            :components="handleDrag(columns)"
            :pagination="ipagination"
            :scroll="scroll"
            :loading="loading"
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange, columnWidth:'40px'}"
            @change="handleTableChange">
            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record)">编辑</a>
              <a-divider v-if="btnEnableList.indexOf(1)>-1" type="vertical" />
              <a v-if="btnEnableList.indexOf(1)>-1" @click="handleCopyAdd(record)">复制</a>
            </span>
            <template slot="customPic" slot-scope="text, record">
              <a-popover placement="right" trigger="click">
                <template slot="content">
                  <img :src="getImgUrl(record.imgName, record.imgLarge)" width="500px" />
                </template>
                <div class="item-info" v-if="record.imgName">
                  <img v-if="record.imgName" :src="getImgUrl(record.imgName, record.imgSmall)" class="item-img" title="查看大图" />
                </div>
              </a-popover>
            </template>
            <template slot="customBarCode" slot-scope="text, record">
              {{record.mBarCode}}
            </template>
            <template slot="customName" slot-scope="text, record">
              {{record.name}}
              <a-tag v-if="record.enableSerialNumber==1" color="orange">序</a-tag>
              <a-tag v-if="record.enableBatchNumber==1" color="orange">批</a-tag>
            </template>
            <template slot="customRenderInitialStock" slot-scope="text, record">
              <a-tooltip :title="record.bigUnitInitialStock">
                {{text}}
              </a-tooltip>
            </template>
            <template slot="customRenderStock" slot-scope="text, record">
              <a-tooltip :title="record.bigUnitStock">
                {{text}}
              </a-tooltip>
            </template>
            <template slot="customRenderEnabled" slot-scope="enabled">
              <a-tag v-if="enabled" color="green">启用</a-tag>
              <a-tag v-if="!enabled" color="orange">禁用</a-tag>
            </template>
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <material-modal ref="modalForm" @ok="modalFormOk"></material-modal>
        <import-file-modal ref="modalImportForm" @ok="modalFormOk"></import-file-modal>
        <batch-set-info-modal ref="batchSetInfoModalForm" @ok="modalFormOk"></batch-set-info-modal>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
  import MaterialModal from './modules/MaterialModal'
  import ImportFileModal from '@/components/tools/ImportFileModal'
  import BatchSetInfoModal from './modules/BatchSetInfoModal'
  import { queryMaterialCategoryTreeList } from '@/api/api'
  import { postAction, getFileAccessHttpUrl } from '@/api/manage'
  import { getMpListShort } from '@/utils/util'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import JDate from '@/components/jeecg/JDate'
  import Vue from 'vue'

  export default {
    name: "MaterialList",
    mixins:[JeecgListMixin],
    components: {
      MaterialModal,
      ImportFileModal,
      BatchSetInfoModal,
      JEllipsis,
      JDate
    },
    data () {
      return {
        categoryTree:[],
        mPropertyListShort: '',
        model: {},
        labelCol: {
          span: 5
        },
        wrapperCol: {
          span: 18,
          offset: 1
        },
        queryTitle: {
          mp1: '扩展1',
          mp2: '扩展2',
          mp3: '扩展3'
        },
        // 查询条件
        queryParam: {
          categoryId: undefined,
          materialParam:'',
          standard:'',
          model:'',
          color:'',
          brand:'',
          mfrs:'',
          otherField1:'',
          otherField2:'',
          otherField3:'',
          weight:'',
          expiryNum:'',
          enabled: undefined,
          enableSerialNumber: undefined,
          enableBatchNumber: undefined,
          position: '',
          remark:'',
          petType:'',
          petBreed:'',
          healthStatus:'',
          vaccineStatus:'',
          mpList: getMpListShort(Vue.ls.get('materialPropertyList'))  //扩展属性
        },
        urlPath: '/material/material',
        ipagination:{
          pageSizeOptions: ['10', '20', '30', '50', '100', '200']
        },
        // 实际索引
        settingDataIndex:[],
        // 实际列
        columns:[],
        // 默认索引
        defDataIndex:['action','mBarCode','name','standard','model','color','categoryName','unit', 'stock',
          'purchaseDecimal','commodityDecimal','wholesaleDecimal','lowDecimal','enabled','petType','petBreed','healthStatus','vaccineStatus'],
        // 默认列
        defColumns: [
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            width: 100,maxHeight:20,
            scopedSlots: { customRender: 'action' },
          },
          {title: '图片', dataIndex: 'pic', width: 60,maxHeight:20, scopedSlots: { customRender: 'customPic' }},
          {title: '条码', dataIndex: 'mBarCode',maxHeight:20, width: 120},
          {title: '名称', dataIndex: 'name', width: 160,maxHeight:20, scopedSlots: { customRender: 'customName' }},
          {title: '规格', dataIndex: 'standard', maxHeight:20,width: 120},
          {title: '型号', dataIndex: 'model', maxHeight:20,width: 120},
          {title: '颜色', dataIndex: 'color', maxHeight:20,width: 70, ellipsis:true},
          {title: '品牌', dataIndex: 'brand', maxHeight:20,width: 100, ellipsis:true},
          {title: '助记码', dataIndex: 'mnemonic',maxHeight:20,width: 80, ellipsis:true},
          {title: '类别', dataIndex: 'categoryName',maxHeight:20, width: 100, ellipsis:true},
          {title: '扩展1', dataIndex: 'otherField1',maxHeight:20, width: 100, ellipsis:true},
          {title: '扩展2', dataIndex: 'otherField2',maxHeight:20, width: 100, ellipsis:true},
          {title: '扩展3', dataIndex: 'otherField3', maxHeight:20,width: 100, ellipsis:true},
          {title: '单位', dataIndex: 'unit',maxHeight:20, width: 100, ellipsis:true,
            customRender:function (t,r,index) {
              if (r) {
                let name = t?t:r.unitName
                if(r.sku) {
                  return name + '[SKU]';
                } else {
                  return name;
                }
              }
            }
          },
          {title: '基础重量', dataIndex: 'weight',maxHeight:20, width: 80},
          {title: '保质期', dataIndex: 'expiryNum',maxHeight:20, width: 60},
          {title: '制造商', dataIndex: 'mfrs',maxHeight:20, width: 120, ellipsis:true},
          {title: '初始库存', dataIndex: 'initialStock',maxHeight:20, width: 80,
            scopedSlots: { customRender: 'customRenderInitialStock' }
          },
          {title: '库存', dataIndex: 'stock',maxHeight:20, width: 80,
            scopedSlots: { customRender: 'customRenderStock' }
          },
          {title: '采购价', dataIndex: 'purchaseDecimal',maxHeight:20, width: 80},
          {title: '零售价', dataIndex: 'commodityDecimal',maxHeight:20, width: 80},
          {title: '销售价', dataIndex: 'wholesaleDecimal',maxHeight:20, width: 80},
          {title: '最低售价', dataIndex: 'lowDecimal',maxHeight:20, width: 80},
          {title: '仓位货架', dataIndex: 'position',maxHeight:20, width: 80},
          {title: '备注', dataIndex: 'remark',maxHeight:20, width: 80},
          {title: '状态', dataIndex: 'enabled', align: "center",maxHeight:20, width: 60,
            scopedSlots: { customRender: 'customRenderEnabled' }
          },
          {title: '宠物类型', dataIndex: 'petType',maxHeight:20, width: 80},
          {title: '品种', dataIndex: 'petBreed',maxHeight:20, width: 100, ellipsis:true},
          {title: '健康状态', dataIndex: 'healthStatus',maxHeight:20, width: 80},
          {title: '疫苗状态', dataIndex: 'vaccineStatus',maxHeight:20, width: 80}
        ],
        url: {
          list: "/material/list",
          delete: "/material/delete",
          deleteBatch: "/material/deleteBatch",
          importExcelUrl: "/material/importExcel",
          exportXlsUrl: "/material/exportExcel",
          batchSetStatusUrl: "/material/batchSetStatus",
          batchSetMaterialCurrentStockUrl: "/material/batchSetMaterialCurrentStock",
          batchSetMaterialCurrentUnitPriceUrl: "/material/batchSetMaterialCurrentUnitPrice",
        }
      }
    },
    created() {
      this.model = Object.assign({}, {});
      this.initColumnsSetting()
      this.loadTreeData()
      this.handleChangeOtherField(1)
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}${this.url.importExcelUrl}`;
      }
    },
    methods: {
      //加载初始化列
      initColumnsSetting(){
        let columnsStr = Vue.ls.get('materialColumns')
        if(columnsStr && columnsStr.indexOf(',')>-1) {
          this.settingDataIndex = columnsStr.split(',')
        } else {
          this.settingDataIndex = this.defDataIndex
        }
        this.columns = this.defColumns.filter(item => {
          return this.settingDataIndex.includes(item.dataIndex)
        })
      },
      //列设置更改事件
      onColChange (checkedValues) {
        this.columns = this.defColumns.filter(item => {
          return checkedValues.includes(item.dataIndex)
        })
        let columnsStr = checkedValues.join()
        Vue.ls.set('materialColumns', columnsStr)
      },
      //恢复默认
      handleRestDefault() {
        Vue.ls.remove('materialColumns')
        this.initColumnsSetting()
      },
      loadTreeData(){
        let that = this;
        let params = {};
        params.id='';
        queryMaterialCategoryTreeList(params).then((res)=>{
          if(res){
            that.categoryTree = [];
            for (let i = 0; i < res.length; i++) {
              let temp = res[i];
              that.categoryTree.push(temp);
            }
          }
        })
      },
      batchSetMaterialCurrentStock () {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
        } else {
          let ids = "";
          for (let a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          let that = this;
          this.$confirm({
            title: "确认操作",
            content: "是否操作选中数据?",
            onOk: function () {
              that.loading = true;
              postAction(that.url.batchSetMaterialCurrentStockUrl, {ids: ids}).then((res) => {
                if(res.code === 200){
                  that.$message.info('修正库存成功！');
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.data.message);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
      },
      batchSetMaterialCurrentUnitPrice () {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
        } else {
          let ids = "";
          for (let a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          let that = this;
          this.$confirm({
            title: "确认操作",
            content: "是否操作选中数据?",
            onOk: function () {
              that.loading = true;
              postAction(that.url.batchSetMaterialCurrentUnitPriceUrl, {ids: ids}).then((res) => {
                if(res.code === 200){
                  that.$message.info('修正成本成功！');
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.data.message);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
      },
      batchEdit() {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
        } else {
          let ids = "";
          for (let a = 0; a < this.selectedRowKeys.length; a++) {
            if(a === this.selectedRowKeys.length-1) {
              ids += this.selectedRowKeys[a]
            } else {
              ids += this.selectedRowKeys[a] + ','
            }
          }
          this.$refs.batchSetInfoModalForm.edit(ids);
          this.$refs.batchSetInfoModalForm.title = "批量编辑";
        }
      },
      handleAdd: function () {
        this.$refs.modalForm.action = "add";
        this.$refs.modalForm.add();
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleEdit: function (record) {
        this.$refs.modalForm.action = "edit";
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
        if(this.btnEnableList.indexOf(1)===-1) {
          this.$refs.modalForm.showOkFlag = false
        }
      },
      handleCopyAdd(record) {
        this.$refs.modalForm.action = "copyAdd";
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "复制新增";
        this.$refs.modalForm.disableSubmit = false;
      },
      getImgUrl(imgName, type) {
        if(imgName && imgName.split(',')) {
          type = type? type + '/':''
          return getFileAccessHttpUrl('systemConfig/static/' + type + imgName.split(',')[0])
        } else {
          return ''
        }
      },
      handleImportXls() {
        let importExcelUrl = this.url.importExcelUrl
        let templateUrl = '/doc/goods_template.xls'
        let templateName = '商品Excel模板[下载]'
        this.$refs.modalImportForm.initModal(importExcelUrl, templateUrl, templateName);
        this.$refs.modalImportForm.title = "商品导入";
      },
      searchReset() {
        this.queryParam = {
          mpList: getMpListShort(Vue.ls.get('materialPropertyList'))  //扩展属性
        }
        this.loadData(1);
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  
  /* 修复商品信息表头高度异常 */
  .ant-table-thead>tr>th{
    padding: 10px 8px !important;
    line-height: 1.4 !important;
    min-height: 38px !important;
  }
  /* 修复商品信息表格行高度 */
  .ant-table-tbody > tr > td {
    padding: 8px !important;
  }
  /* 修复图片列高度 */
  .ant-table-tbody > tr > td .item-info {
    margin: 0 !important;
    vertical-align: middle;
  }
  
  /* 修复操作按钮对齐 */
  .ant-table-tbody > tr > td .ant-btn {
    margin: 0 4px 0 0 !important;
  }
  
  /* 修复拖拽表头高度异常 */
  .ant-table-thead > tr > th.resize-table-th {
    position: relative !important;
    padding-right: 20px !important;
  }
  
  /* 修复表头拖拽句柄位置 */
  .ant-table-thead > tr > th.resize-table-th .table-draggable-handle {
    position: absolute !important;
    right: 2px !important;
    top: 50% !important;
    transform: translateY(-50%) !important;
    height: 60% !important;
    width: 8px !important;
    background: transparent !important;
    cursor: col-resize !important;
  }
  
  /* 修复表头文本换行 */
  .ant-table-thead > tr > th .ant-table-header-column {
    white-space: nowrap !important;
    overflow: hidden !important;
    text-overflow: ellipsis !important;
  }
  
  /* 修复商品信息表格整体高度 */
  .ant-table-wrapper {
    line-height: 1.4 !important;
  }
  
  /* 修复表头过滤图标位置 */
  .ant-table-thead > tr > th .ant-table-filter-icon {
    position: absolute !important;
    right: 8px !important;
    top: 50% !important;
    transform: translateY(-50%) !important;
  }
  
  /* 修复列排序图标位置 */
  .ant-table-thead > tr > th .ant-table-column-sorter {
    position: absolute !important;
    right: 8px !important;
    top: 50% !important;
    transform: translateY(-50%) !important;
  }
</style>
<style>
  .item-info {
    float:left;
    width:38px;
    height:38px;
    margin-left:6px
  }
  .item-img {
    cursor:pointer;
    position: static;
    display: block;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
</style>