<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    v-bind:prefixNo="prefixNo"
    fullscreen
    switchHelp
    switchFullscreen
    @cancel="handleCancel"
    :id="prefixNo"
    :style="modalStyle">
    <template slot="footer">
      <a-button key="back" @click="handleCancel">取消</a-button>
      <a-button type="primary" v-if="showOkFlag" :loading="confirmLoading" @click="handleOk">保存（Ctrl+S）</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-tabs v-model:activeKey="activeKey" size="small">
          <a-tab-pane key="1" tab="宠物信息" id="materialPetModal" forceRender>
            <a-row class="form-row" :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="宠物名称">
                  <a-input placeholder="请输入宠物名称" v-decorator.trim="[ 'name', validatorRules.name ]" @change="handleNameChange" />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="宠物类型">
                  <a-select placeholder="请选择宠物类型" v-decorator="[ 'petType' ]">
                    <a-select-option value="猫">猫</a-select-option>
                    <a-select-option value="狗">狗</a-select-option>
                    <a-select-option value="其他">其他</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="品种">
                  <a-input placeholder="请输入品种" v-decorator.trim="[ 'petBreed' ]" />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="昵称">
                  <a-input placeholder="请输入昵称（可为空）" v-decorator.trim="[ 'petName' ]" />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="性别">
                  <a-select placeholder="请选择性别" v-decorator="[ 'petGender' ]">
                    <a-select-option value="公">公</a-select-option>
                    <a-select-option value="母">母</a-select-option>
                    <a-select-option value="未知">未知</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="毛色">
                  <a-input placeholder="请输入毛色" v-decorator.trim="[ 'petColor' ]" />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="出生日期">
                  <j-date placeholder="请选择出生日期" v-decorator="[ 'petBirthday' ]" />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="重量(kg)">
                  <a-input-number placeholder="请输入重量" style="width: 100%" v-decorator.trim="[ 'petWeight' ]" />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="来源类型">
                  <a-select placeholder="请选择来源类型" v-decorator="[ 'sourceType' ]">
                    <a-select-option value="采购">采购</a-select-option>
                    <a-select-option value="自繁">自繁</a-select-option>
                    <a-select-option value="寄售">寄售</a-select-option>
                    <a-select-option value="领养代管">领养代管</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="采购价">
                  <a-input-number placeholder="请输入采购价" style="width: 100%" v-decorator.trim="[ 'purchasePrice' ]" />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="疫苗状态">
                  <a-select placeholder="请选择疫苗状态" v-decorator="[ 'vaccineStatus' ]">
                    <a-select-option value="未接种">未接种</a-select-option>
                    <a-select-option value="部分接种">部分接种</a-select-option>
                    <a-select-option value="已完成">已完成</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="驱虫状态">
                  <a-select placeholder="请选择驱虫状态" v-decorator="[ 'dewormStatus' ]">
                    <a-select-option value="未驱虫">未驱虫</a-select-option>
                    <a-select-option value="已驱虫">已驱虫</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="是否绝育">
                  <a-select placeholder="请选择是否绝育" v-decorator="[ 'sterilizedFlag' ]">
                    <a-select-option value="0">否</a-select-option>
                    <a-select-option value="1">是</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="健康状态">
                  <a-select placeholder="请选择健康状态" v-decorator="[ 'healthStatus' ]">
                    <a-select-option value="健康">健康</a-select-option>
                    <a-select-option value="观察中">观察中</a-select-option>
                    <a-select-option value="治疗中">治疗中</a-select-option>
                    <a-select-option value="禁售">禁售</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24" v-if="manySkuSelected>=1">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 4 }}" :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" :label="skuOneTitle">
                  <a-select mode="multiple" v-decorator="[ 'skuOne' ]" showSearch optionFilterProp="children"
                            placeholder="请选择（可多选）" @select="onSkuChange" @deselect="onSkuOneDeSelect">
                    <a-select-option v-for="(item,index) in skuOneList" :key="index" :value="item.value">
                      {{ item.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="12" :sm="24" v-if="manySkuSelected>=2">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 4 }}" :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" :label="skuTwoTitle">
                  <a-select mode="multiple" v-decorator="[ 'skuTwo' ]" showSearch optionFilterProp="children"
                            placeholder="请选择（可多选）" @select="onSkuChange" @deselect="onSkuTwoDeSelect">
                    <a-select-option v-for="(item,index) in skuTwoList" :key="index" :value="item.value">
                      {{ item.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="12" :sm="24" v-if="manySkuSelected>=3">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 4 }}" :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" :label="skuThreeTitle">
                  <a-select mode="multiple" v-decorator="[ 'skuThree' ]" showSearch optionFilterProp="children"
                            placeholder="请选择（可多选）" @select="onSkuChange" @deselect="onSkuThreeDeSelect">
                    <a-select-option v-for="(item,index) in skuThreeList" :key="index" :value="item.value">
                      {{ item.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <div style="margin-top:8px;" id="materialDetailModal">
              <j-editable-table
                ref="editableMeTable"
                :loading="meTable.loading"
                :columns="meTable.columns"
                :dataSource="meTable.dataSource"
                :height="300"
                :minWidth="1000"
                :maxHeight="300"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="true"
                @valueChange="onValueChange"
                @added="onAdded"
                @deleted="onDeleted">
                <template #buttonAfter>
                  <a-button @click="batchSetPrice('purchase')">采购价-批量</a-button>
                  <a-button style="margin-left: 8px" @click="batchSetPrice('commodity')">零售价-批量</a-button>
                  <a-button style="margin-left: 8px" @click="batchSetPrice('wholesale')">销售价-批量</a-button>
                  <a-button style="margin-left: 8px" @click="batchSetPrice('low')">最低售价-批量</a-button>
                </template>
              </j-editable-table>
              <!-- 表单区域 -->
              <batch-set-price-modal ref="priceModalForm" @ok="batchSetPriceModalFormOk"></batch-set-price-modal>
            </div>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="24" :md="24" :sm="24">
                <a-form-item :labelCol="labelCol" :wrapperCol="{xs: { span: 24 },sm: { span: 24 }}" label="">
                  <a-textarea :rows="1" placeholder="请输入备注" v-decorator="[ 'remark' ]" style="margin-top:8px;"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-tab-pane>
          <a-tab-pane key="2" tab="库存数量" forceRender>
            <j-editable-table
              ref="editableDepotTable"
              :loading="depotTable.loading"
              :columns="depotTable.columns"
              :dataSource="depotTable.dataSource"
              :minWidth="1000"
              :maxHeight="300"
              :rowNumber="true"
              :rowSelection="false"
              :actionButton="false">
              <template #buttonAfter>
                <a-button style="margin: 0px 0px 8px 0px" @click="batchSetStock('initStock')">期初库存-批量</a-button>
                <a-button style="margin-left: 8px" @click="batchSetStock('lowSafeStock')">最低安全库存-批量</a-button>
                <a-button style="margin-left: 8px" @click="batchSetStock('highSafeStock')">最高安全库存-批量</a-button>
              </template>
            </j-editable-table>
            <!-- 表单区域 -->
            <batch-set-stock-modal ref="stockModalForm" @ok="batchSetStockModalFormOk"></batch-set-stock-modal>
          </a-tab-pane>
          <a-tab-pane key="3" tab="图片信息" forceRender>
            <a-row class="form-row" :gutter="24" style="padding-top:20px">
              <a-col :lg="18" :md="18" :sm="24">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 3 }}" :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" label="图片信息">
                  <j-image-upload v-model="fileList" bizPath="material" text="上传" isMultiple></j-image-upload>
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="6" :sm="24"></a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :lg="18" :md="18" :sm="24">
                <a-form-item :labelCol="{xs: { span: 24 },sm: { span: 3 }}" :wrapperCol="{xs: { span: 24 },sm: { span: 20 }}" label="上传提示">
                  图片最多4张，且单张大小不超过1M
                </a-form-item>
              </a-col>
              <a-col :lg="6" :md="6" :sm="24"></a-col>
            </a-row>
          </a-tab-pane>
          </a-tab-pane>
        </a-tabs>
      </a-form>
    </a-spin>
    <unit-modal ref="unitModalForm" @ok="unitModalFormOk"></unit-modal>
  </j-modal>
</template>
<script>
  import pick from 'lodash.pick'
  import BatchSetPriceModal from './BatchSetPriceModal'
  import BatchSetStockModal from './BatchSetStockModal'
  import UnitModal from '../../system/modules/UnitModal'
  import JEditableTable from '@/components/jeecg/JEditableTable'
  import { FormTypes, getRefPromise, VALIDATE_NO_PASSED, validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { changeNameToPinYin, checkMaterial, checkMaterialBarCode, getMaterialAttributeNameList, getMaterialAttributeValueListById, getMaxBarCode, queryMaterialCategoryTreeList } from '@/api/api'
  import { autoJumpNextInput, handleIntroJs, removeByVal, addBigNumbers } from '@/utils/util'
  import { getAction, httpAction } from '@/api/manage'
  import JImageUpload from '@/components/jeecg/JImageUpload'
  import JDate from '@/components/jeecg/JDate'
  import Vue from 'vue'

  export default {
    name: "MaterialModal",
    components: {
      BatchSetPriceModal,
      BatchSetStockModal,
      UnitModal,
      JImageUpload,
      JDate,
      JEditableTable,
      VNodes: {
        functional: true,
        render: (h, ctx) => ctx.props.vnodes,
      }
    },
    data () {
      return {
        title:"操作",
        width: '1300px',
        visible: false,
        modalStyle: '',
        action: '',
        activeKey: '1',
        categoryTree: [],
        unitList: [],
        depotList: [],
        fileList:[],
        unitStatus: false,
        manyUnitStatus: true,
        unitChecked: false,
        switchDisabled: false, //开关的启用状态
        barCodeSwitch: false, //生成条码开关
        maxBarCodeInfo: '', //最大条码
        meDeleteIdList: [], //删除条码信息的id数组
        prefixNo: 'material',
        attributeStatus: false,
        materialAttributeList: [],
        skuOneTitle: '属性1',
        skuTwoTitle: '属性2',
        skuThreeTitle: '属性3',
        skuOneList: [],
        skuTwoList: [],
        skuThreeList: [],
        meOldDataSource: [],
        manySkuSelected: 0,
        model: {},
        showOkFlag: true,
        setTimeFlag: null,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        mpShort: {
          mfrs: {},
          otherField1: { name: '扩展1' },
          otherField2: { name: '扩展2' },
          otherField3: { name: '扩展3' }
        },
        meTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '条码', key: 'barCode', width: '15%', type: FormTypes.input, defaultValue: '', placeholder: '请输入${title}',
              validateRules: [
                { pattern: /^.{4,40}$/, message: '长度为4到40位' },
                { handler: this.validateBarCode}]
              // 移除必填验证，让宠物商品条码可选
            },
            {
              title: '单位', key: 'commodityUnit', width: '8%', type: FormTypes.input, defaultValue: '', placeholder: '请输入${title}'
              // 移除必填验证，让宠物商品不受单位限制
            },
            {
              title: '多属性', key: 'sku', width: '25%', type: FormTypes.input, defaultValue: '', readonly:true, placeholder: '请输入${title}'
            },
            {
              title: '采购价', key: 'purchaseDecimal', width: '9%', type: FormTypes.inputNumber, defaultValue: '', placeholder: '请输入${title}'
            },
            {
              title: '零售价', key: 'commodityDecimal', width: '9%', type: FormTypes.inputNumber, defaultValue: '', placeholder: '请输入${title}'
            },
            {
              title: '销售价', key: 'wholesaleDecimal', width: '9%', type: FormTypes.inputNumber, defaultValue: '', placeholder: '请输入${title}'
            },
            {
              title: '最低售价', key: 'lowDecimal', width: '9%', type: FormTypes.inputNumber, defaultValue: '', placeholder: '请输入${title}'
            }
          ]
        },
        depotTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '仓库', key: 'name', width: '15%', type: FormTypes.normal
            },
            {
              title: '期初库存数量', key: 'initStock', width: '15%', type: FormTypes.inputNumber, defaultValue: '', placeholder: '请输入${title}'
            },
            {
              title: '最低安全库存数量', key: 'lowSafeStock', width: '15%', type: FormTypes.inputNumber, defaultValue: '', placeholder: '请输入${title}'
            },
            {
              title: '最高安全库存数量', key: 'highSafeStock', width: '15%', type: FormTypes.inputNumber, defaultValue: '', placeholder: '请输入${title}'
            }
          ]
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
          name:{
            rules: [
              { required: true, message: '请输入名称!' },
              { max: 100, message: '长度请小于100个字符', trigger: 'blur' }
            ]
          },
          standard:{
            rules: [
              { max: 100, message: '长度请小于100个字符', trigger: 'blur' }
            ]
          },
          model:{
            rules: [
              { max: 100, message: '长度请小于100个字符', trigger: 'blur' }
            ]
          },
          unit:{
            rules: [
              { required: true, message: '请输入单位!' }
            ]
          },
          unitId:{
            rules: [
              { required: true, message: '请选择多单位!' }
            ]
          }
        },
        url: {
          add: '/material/add',
          edit: '/material/update',
          materialsExtendList: '/materialsExtend/getDetailList',
          depotWithStock: '/depot/getAllListWithStock'
        }
      }
    },
    created () {
      this.loadParseMaterialProperty()
      let realScreenWidth = window.screen.width
      this.width = realScreenWidth<1500?'1200px':'1400px'
    },
    mounted() {
      document.getElementById(this.prefixNo).addEventListener('keydown', this.handleOkKey)
    },
    beforeDestroy() {
      document.getElementById(this.prefixNo).removeEventListener('keydown', this.handleOkKey)
    },
    methods: {
      // 快捷键
      handleOkKey(e) {
        const key = window.event.keyCode ? window.event.keyCode : window.event.which
        if (key === 83 && e.ctrlKey) {
          //保存 CTRL+S
          this.handleOk()
          e.preventDefault()
        }
      },
      // 获取所有的editableTable实例
      getAllTable() {
        return Promise.all([
          getRefPromise(this, 'editableMeTable'),
          getRefPromise(this, 'editableDepotTable')
        ])
      },
      add () {
        //隐藏多属性
        this.meTable.columns[2].type = FormTypes.hidden
        // 默认新增一条数据
        this.getAllTable().then(editableTables => {
          editableTables[0].add()
        })
        this.edit({})
        this.$nextTick(() => {
          handleIntroJs('material', 11)
        })
      },
      edit (record) {
        let that = this
        this.form.resetFields();
        this.model = Object.assign({}, record);
        let attribute = record.attribute
        if(attribute) {
          //构造多属性
          let attrObj = JSON.parse(attribute)
          this.model.manySku = attrObj.manySku
          this.model.skuOne = attrObj.skuOne
          this.model.skuTwo = attrObj.skuTwo
          this.model.skuThree = attrObj.skuThree
        }
        this.activeKey = '1'
        this.manySkuSelected = 0
        this.barCodeSwitch = false
        this.maxBarCodeInfo = ''
        this.visible = true
        this.meDeleteIdList = []
        this.modalStyle = 'top:20px;height: 95%;'
        if(JSON.stringify(record) === '{}') {
          this.fileList = []
        } else {
          if(this.action === 'edit') {
            setTimeout(() => {
              this.fileList = record.imgName
            }, 5)
          }
        }
        this.$nextTick(() => {
          // 为宠物商品设置默认单位
          if(this.model.petType && !this.model.unit) {
            this.model.unit = '只'
          }
          this.form.setFieldsValue(pick(this.model, 'name', 'standard', 'unit', 'unitId', 'model', 'color', 'brand', 'mnemonic',
            'categoryId','enableSerialNumber','enableBatchNumber','position','expiryNum','weight','remark','mfrs',
            'otherField1','otherField2','otherField3','manySku','skuOne','skuTwo','skuThree'))
          autoJumpNextInput('materialHeadModal')
          autoJumpNextInput('materialDetailModal')
        });
        this.initMaterialAttribute()
        this.loadTreeData()
        this.loadUnitListData()
        // 加载子表数据
        if (this.model.id) {
          //禁用多属性开关
          this.switchDisabled = true
          // 判断是否是多单位
          if(this.model.unit){
            this.unitChecked = false
            this.unitStatus = false
            this.manyUnitStatus = true
          } else {
            this.unitChecked = true
            this.unitStatus = true
            this.manyUnitStatus = false
          }
          //编辑状态下有多属性，则不允许修改
          if(this.model.manySku) {
            this.attributeStatus = true
            //加载每个多属性的下拉框
            setTimeout(function() {
              that.loadSkuList(that.model.manySku)
            },1000)
          } else {
            this.attributeStatus = false
          }
          let params = { materialId: this.model.id }
          this.requestMeTableData(this.url.materialsExtendList, params, this.meTable)
          this.requestDepotTableData(this.url.depotWithStock, { mId: this.model.id }, this.depotTable)
        } else {
          this.attributeStatus = false
          this.switchDisabled = false
          this.meTable.columns[2].readonly = true
          this.requestDepotTableData(this.url.depotWithStock, { mId: 0 }, this.depotTable)
        }
      },
      /** 查询条码tab的数据 */
      requestMeTableData(url, params, tab) {
        tab.loading = true
        getAction(url, params).then(res => {
          for (let i = 0; i < res.data.rows.length; i++) {
            if(res.data.rows[i].sku) {
              this.meTable.columns[2].type = FormTypes.input
            } else {
              this.meTable.columns[2].type = FormTypes.hidden
            }
          }
          tab.dataSource = res.data.rows || []
          this.meOldDataSource = res.data.rows || []
          //复制新增商品-初始化条码信息
          if(this.action === 'copyAdd') {
            getMaxBarCode({}).then((res)=> {
              if (res && res.code === 200) {
                let maxBarCode = res.data.barCode
                let meTableData = []
                for (let i = 0; i < tab.dataSource.length; i++) {
                  let meInfo = tab.dataSource[i]
                  console.log(maxBarCode)
                  console.log(addBigNumbers(maxBarCode, i+1))
                  meInfo.barCode = addBigNumbers(maxBarCode, i+1)
                  console.log(meInfo.barCode)
                  meTableData.push(meInfo)
                }
                tab.dataSource = meTableData
              }
            })
          }
        }).finally(() => {
          tab.loading = false
        })
      },
      /** 查询仓库tab的数据 */
      requestDepotTableData(url, params, tab) {
        tab.loading = true
        getAction(url, params).then(res => {
          tab.dataSource = res.data || []
        }).finally(() => {
          tab.loading = false
        })
      },
      close () {
        this.$emit('close')
        this.visible = false
        this.modalStyle = ''
        this.unitStatus = false
        this.manyUnitStatus = true
        this.unitChecked = false
        this.getAllTable().then(editableTables => {
          editableTables[0].initialize()
          editableTables[1].initialize()
        })
      },
      handleOk () {
        this.validateFields()
      },
      handleCancel () {
        this.close()
      },
      /** 触发表单验证 */
      validateFields() {
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          let formData = this.classifyIntoFormData(allValues)
          formData.sortList = [];
          if(formData.unit === undefined) {formData.unit = ''}
          if(formData.unitId === undefined) {formData.unitId = ''}
          if(this.unitChecked) {formData.unit = ''} else {formData.unitId = ''}
          // 发起请求
          return this.requestAddOrEdit(formData)
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : (e.index + 1).toString()
          } else {
            console.error(e)
          }
        })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let materialMain = Object.assign(this.model, allValues.formValue)
        return {
          ...materialMain, // 展开
          meList: allValues.tablesValue[0].values,
          stock: allValues.tablesValue[1].values,
        }
      },
      /** 发起新增或修改的请求 */
      requestAddOrEdit(formData) {
        //复制新增商品-初始化id和租户id
        if(this.action === 'copyAdd') {
          this.model.id = ''
          this.model.tenantId = ''
          formData.id = ''
          formData.tenantId = ''
        }
        // 宠物商品特殊处理 - 条码可选
        if(formData.meList.length === 0 && !formData.petType) {
          this.$message.warning('抱歉，请输入条码信息！');
          return;
        }
        if(formData.enableSerialNumber === '1' && formData.enableBatchNumber === '1') {
          this.$message.warning('抱歉，序列号和批号只能选择一项！');
          return;
        }
        // 宠物商品特殊处理 - 允许多属性和多单位同时存在
        if(formData.manySku && formData.unitId && !formData.petType) {
          this.$message.warning('抱歉，多属性商品不能勾选多单位，请切换为单个单位！');
          return;
        }
        //校验商品是否存在，通过校验商品的名称、型号、规格、颜色、单位、制造商等
        // 宠物商品特殊处理 - 跳过单位相关的重复性校验
        let param = {
          id: this.model.id?this.model.id:0,
          name: this.model.name,
          model: this.parseParam(this.model.model),
          color: this.parseParam(this.model.color),
          standard: this.parseParam(this.model.standard),
          mfrs: this.parseParam(this.model.mfrs),
          otherField1: this.parseParam(this.model.otherField1),
          otherField2: this.parseParam(this.model.otherField2),
          otherField3: this.parseParam(this.model.otherField3),
          unit: this.model.petType ? '' : this.parseParam(this.model.unit),  // 宠物商品不校验单位
          unitId: this.model.petType ? '' : this.parseParam(this.model.unitId)  // 宠物商品不校验多单位
        }
        checkMaterial(param).then((res)=>{
          if(res && res.code===200) {
            if(res.data.status){
              this.$message.warning('抱歉，该商品已存在！');
              return;
            } else {
              //进一步校验单位
              let basicUnit = '', otherUnit = '', otherUnitTwo = '', otherUnitThree = ''
              if(formData.unitId) {
                let unitArr = this.unitList
                for(let i=0; i < unitArr.length; i++) {
                  if(unitArr[i].id == formData.unitId) {
                    basicUnit = unitArr[i].basicUnit
                    otherUnit = unitArr[i].otherUnit
                    if(unitArr[i].otherUnitTwo) {
                      otherUnitTwo = unitArr[i].otherUnitTwo
                    }
                    if(unitArr[i].otherUnitThree) {
                      otherUnitThree = unitArr[i].otherUnitThree
                    }
                  }
                }
              }
              // 宠物商品完全跳过单位验证
              if(formData.petType) {
                // 宠物商品不受单位限制，跳过所有单位验证
              } else if(!formData.unit) {
                //此时为多单位
                if (formData.meList.length<1){
                  this.$message.warning('多单位的商品条码行数至少要有一行，请新增条码信息！');
                  return;
                }
                // 只验证存在的行，不强制要求两行
                for(let i = 0; i < formData.meList.length; i++) {
                  if(i === 0 && formData.meList[i].commodityUnit != basicUnit) {
                    this.$message.warning('条码之后的单位填写有误，单位【' + formData.meList[i].commodityUnit
                      + '】请修改为【' + basicUnit + '】！');
                    return;
                  }
                  if(i === 1 && formData.meList[i].commodityUnit != otherUnit) {
                    this.$message.warning('条码之后的单位填写有误，单位【' + formData.meList[i].commodityUnit
                      + '】请修改为【' + otherUnit + '】！');
                    return;
                  }
                }
              }
              let skuCount = 0
              for(let i=0; i<formData.meList.length; i++) {
                let commodityUnit = formData.meList[i].commodityUnit;
                // 宠物商品完全跳过单位验证
                if(formData.petType) {
                  // 宠物商品不受单位限制，跳过验证
                } else if(formData.unit) {
                  if(commodityUnit != formData.unit) {
                    this.$message.warning('条码之后的单位填写有误，单位【' + commodityUnit + '】请修改为【'
                      + formData.unit + '】！');
                    return;
                  }
                } else if(formData.unitId) {
                  if(commodityUnit != basicUnit && commodityUnit != otherUnit && commodityUnit != otherUnitTwo && commodityUnit != otherUnitThree) {
                    let warnInfo = '条码之后的单位填写有误，单位【' + commodityUnit + '】请修改为【' + basicUnit+ '】或【' + otherUnit+ '】'
                    if(otherUnitTwo) {
                      warnInfo += '或【' + otherUnitTwo+ '】'
                    }
                    if(otherUnitThree) {
                      warnInfo += '或【' + otherUnitThree+ '】'
                    }
                    warnInfo += '！'
                    this.$message.warning(warnInfo);
                    return;
                  }
                }
                if(formData.sku) {
                  skuCount++
                }
              }
              //对最低和最高安全库存进行校验
              for (let i = 0; i < formData.stock.length; i++) {
                let depotStockObj = formData.stock[i]
                if(skuCount && depotStockObj.initStock && depotStockObj.initStock-0) {
                  this.$message.warning('抱歉，多属性商品不能录入期初库存，建议进行盘点录入！')
                  return
                }
                if(formData.enableSerialNumber === '1' && depotStockObj.initStock && depotStockObj.initStock-0) {
                  this.$message.warning('抱歉，序列号商品不能录入期初库存，建议进行入库单据录入！')
                  return
                }
                if(formData.enableBatchNumber === '1' && depotStockObj.initStock && depotStockObj.initStock-0) {
                  this.$message.warning('抱歉，批号商品不能录入期初库存，建议进行入库单据录入！')
                  return
                }
                if(depotStockObj.lowSafeStock && depotStockObj.highSafeStock) {
                  if(depotStockObj.lowSafeStock-0 > depotStockObj.highSafeStock-0) {
                    this.$message.warning('抱歉，' + depotStockObj.name + '的最低安全库存大于最高安全库存！')
                    return
                  }
                }
              }
              //图片校验
              if(this.fileList && this.fileList.length > 0) {
                formData.imgName = this.fileList
                let fileArr = this.fileList.split(',')
                if(fileArr.length > 4) {
                  this.$message.warning('抱歉，商品图片不能超过4张！');
                  return
                }
              } else {
                formData.imgName = ''
              }
              formData.meDeleteIdList = this.meDeleteIdList
              //接口调用
              let url = this.url.add, method = 'post'
              if (this.model.id) {
                url = this.url.edit
                method = 'put'
              }
              const that = this;
              this.confirmLoading = true
              httpAction(url, formData, method).then((res) => {
                if(res.code === 200){
                  that.$emit('ok');
                  that.confirmLoading = false
                  that.close();
                }else{
                  that.$message.warning(res.data.message);
                  that.confirmLoading = false
                }
              }).finally(() => {
              })
            }
          }
        })
      },
      parseParam(param) {
        return param ? param: ""
      },
      validateBarCode(type, value, row, column, callback, target) {
        let params = {
          barCode: value,
          id: row.id.length >= 20?0: row.id
        };
        checkMaterialBarCode(params).then((res)=>{
          if(res && res.code===200) {
            if(!res.data.status){
              callback(true);
            } else {
              callback(false, '该条码已经存在');
            }
          } else {
            callback(false, res.data);
          }
        });
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
      loadUnitListData(){
        let that = this;
        let params = {};
        params.currentPage = 1;
        params.pageSize = 100;
        getAction('/unit/getAllList', params).then((res) => {
          if(res){
            that.unitList = res.data;
          }
        })
      },
      onManySkuChange(value) {
        this.manySkuSelected = value.length
        //控制多属性下拉框中选择项的状态
        if(value.length < 3){
          this.materialAttributeList.forEach((item,index,array)=>{
            (array.indexOf(item.value) === -1)?Vue.set(array[index], 'disabled', false):''
          })
        }else{
          this.materialAttributeList.forEach((item,index,array)=>{
            (value.indexOf(item.value) === -1)?Vue.set(array[index], 'disabled', true):''
          })
        }
        //更新属性1和属性2和属性3的下拉框
        if(value.length <= 3) {
          let skuOneId = value[0]
          let skuTwoId = value[1]
          let skuThreeId = value[2]
          this.materialAttributeList.forEach(item => {
            if(item.value === skuOneId) {
              this.skuOneTitle = item.name
            }
            if(item.value === skuTwoId) {
              this.skuTwoTitle = item.name
            }
            if(item.value === skuThreeId) {
              this.skuThreeTitle = item.name
            }
          })
          if(skuOneId) {
            getMaterialAttributeValueListById({'id': skuOneId}).then((res)=>{
              this.skuOneList = res? res:[]
            })
          }
          if(skuTwoId) {
            getMaterialAttributeValueListById({'id': skuTwoId}).then((res)=>{
              this.skuTwoList = res? res:[]
            })
          }
          if(skuThreeId) {
            getMaterialAttributeValueListById({'id': skuThreeId}).then((res)=>{
              this.skuThreeList = res? res:[]
            })
          }
        }
        //控制条码列表中的多属性列
        if(value.length>0) {
          this.meTable.columns[2].type = FormTypes.input
        } else {
          this.meTable.columns[2].type = FormTypes.hidden
        }
        this.barCodeSwitch = false;
        this.meTable.dataSource = []
      },
      //编辑页面加载的时候加载存在的sku
      loadSkuList(value) {
        this.manySkuSelected = value.length
        //更新属性1和属性2和属性3的下拉框
        if(value.length <= 3) {
          let skuOneId = value[0]
          let skuTwoId = value[1]
          let skuThreeId = value[2]
          this.materialAttributeList.forEach(item => {
            if(item.value === skuOneId) {
              this.skuOneTitle = item.name
            }
            if(item.value === skuTwoId) {
              this.skuTwoTitle = item.name
            }
            if(item.value === skuThreeId) {
              this.skuThreeTitle = item.name
            }
          })
          if(skuOneId) {
            getMaterialAttributeValueListById({'id': skuOneId}).then((res)=>{
              this.skuOneList = res? res:[]
              this.form.setFieldsValue(pick(this.model, 'skuOne'))
            })
          }
          if(skuTwoId) {
            getMaterialAttributeValueListById({'id': skuTwoId}).then((res)=>{
              this.skuTwoList = res? res:[]
              this.form.setFieldsValue(pick(this.model, 'skuTwo'))
            })
          }
          if(skuThreeId) {
            getMaterialAttributeValueListById({'id': skuThreeId}).then((res)=>{
              this.skuThreeList = res? res:[]
              this.form.setFieldsValue(pick(this.model, 'skuThree'))
            })
          }
        }
        this.barCodeSwitch = false
      },
      onSkuChange() {
        let skuOneData = this.form.getFieldValue('skuOne')
        let skuTwoData = this.form.getFieldValue('skuTwo')
        let skuThreeData = this.form.getFieldValue('skuThree')
        this.autoSkuList(skuOneData, skuTwoData, skuThreeData)
      },
      onSkuOneDeSelect(value) {
        let skuOneData = this.form.getFieldValue('skuOne')
        let skuTwoData = this.form.getFieldValue('skuTwo')
        let skuThreeData = this.form.getFieldValue('skuThree')
        removeByVal(skuOneData, value)
        this.autoSkuList(skuOneData, skuTwoData, skuThreeData)
      },
      onSkuTwoDeSelect(value) {
        let skuOneData = this.form.getFieldValue('skuOne')
        let skuTwoData = this.form.getFieldValue('skuTwo')
        let skuThreeData = this.form.getFieldValue('skuThree')
        removeByVal(skuTwoData, value)
        this.autoSkuList(skuOneData, skuTwoData, skuThreeData)
      },
      onSkuThreeDeSelect(value) {
        let skuOneData = this.form.getFieldValue('skuOne')
        let skuTwoData = this.form.getFieldValue('skuTwo')
        let skuThreeData = this.form.getFieldValue('skuThree')
        removeByVal(skuThreeData, value)
        this.autoSkuList(skuOneData, skuTwoData, skuThreeData)
      },
      autoSkuList(skuOneData, skuTwoData, skuThreeData) {
        let unit = this.form.getFieldValue('unit')
        // 如果单位为空且是宠物商品，设置默认单位
        if(!unit && this.model.petType) {
          unit = '只'  // 宠物商品的默认单位
        }
        //计算多属性已经选择了几个
        let skuArr = []
        if(this.getNumByField('skuOne')) {
          skuArr.push(skuOneData)
        }
        if(this.getNumByField('skuTwo')) {
          skuArr.push(skuTwoData)
        }
        if(this.getNumByField('skuThree')) {
          skuArr.push(skuThreeData)
        }
        let skuArrOne = skuArr[0]
        let skuArrTwo = skuArr[1]
        let skuArrThree = skuArr[2]
        let count = this.getNumByField('skuOne') + this.getNumByField('skuTwo') + this.getNumByField('skuThree')
        let barCodeSku = []
        if(count === 1) {
          let skuArrOnly = []
          if(this.getNumByField('skuOne')) {
            skuArrOnly = skuOneData
          } else if(this.getNumByField('skuTwo')) {
            skuArrOnly = skuTwoData
          } else if(this.getNumByField('skuThree')) {
            skuArrOnly = skuThreeData
          }
          for (let i = 0; i < skuArrOnly.length; i++) {
            barCodeSku.push(skuArrOnly[i])
          }
        } else if(count === 2) {
          for (let i = 0; i < skuArrOne.length; i++) {
            for (let j = 0; j < skuArrTwo.length; j++) {
              barCodeSku.push(skuArrOne[i] + '/' + skuArrTwo[j])
            }
          }
        } else if(count === 3) {
          for (let i = 0; i < skuArrOne.length; i++) {
            for (let j = 0; j < skuArrTwo.length; j++) {
              for (let k = 0; k < skuArrThree.length; k++) {
                barCodeSku.push(skuArrOne[i] + '/' + skuArrTwo[j] + '/' + skuArrThree[k])
              }
            }
          }
        }
        let meTableData = []
        getMaxBarCode({}).then((res)=>{
          if(res && res.code===200) {
            let k = 0
            let maxBarCode = res.data.barCode
            for (let i = 0; i < barCodeSku.length; i++) {
              let currentBarCode = ''
              let currentId = ''
              let purchaseDecimal = ''
              let commodityDecimal = ''
              let wholesaleDecimal = ''
              let lowDecimal = ''
              for (let j = 0; j < this.meOldDataSource.length; j++) {
                if(barCodeSku[i] === this.meOldDataSource[j].sku) {
                  currentBarCode = this.meOldDataSource[j].barCode
                  currentId = this.meOldDataSource[j].id
                  purchaseDecimal = this.meOldDataSource[j].purchaseDecimal
                  commodityDecimal = this.meOldDataSource[j].commodityDecimal
                  wholesaleDecimal = this.meOldDataSource[j].wholesaleDecimal
                  lowDecimal = this.meOldDataSource[j].lowDecimal
                }
              }
              if(currentBarCode) {
                //此时说明该sku之前就存在
                meTableData.push({id: currentId, barCode: currentBarCode, commodityUnit: unit, sku: barCodeSku[i],
                  purchaseDecimal: purchaseDecimal, commodityDecimal: commodityDecimal,
                  wholesaleDecimal: wholesaleDecimal, lowDecimal: lowDecimal})
              } else {
                k = k+1
                currentBarCode = addBigNumbers(maxBarCode, k)
                meTableData.push({barCode: currentBarCode, commodityUnit: unit, sku: barCodeSku[i]})
              }
            }
            this.meTable.dataSource = meTableData
          }
        })
      },
      getNumByField(field) {
        let num = 0
        if(this.form.getFieldValue(field)) {
          if(this.form.getFieldValue(field).length>0) {
            num = 1
          }
        }
        return num
      },
      onAdded(event) {
        const { row, target } = event
        let unit = ''
        if(this.unitStatus == false) {
          unit = this.form.getFieldValue('unit')
        }
        // 如果单位为空且是宠物商品，设置默认单位
        if(!unit && this.model.petType) {
          unit = '只'  // 宠物商品的默认单位
        }
        if(this.maxBarCodeInfo === '') {
          getMaxBarCode({}).then((res)=> {
            if (res && res.code === 200) {
              this.maxBarCodeInfo = res.data.barCode
              this.maxBarCodeInfo = addBigNumbers(this.maxBarCodeInfo, 1)
              target.setValues([{rowKey: row.id, values: {barCode: this.maxBarCodeInfo, commodityUnit: unit?unit:'只'}}])
            }
          })
        } else {
          this.maxBarCodeInfo = addBigNumbers(this.maxBarCodeInfo, 1)
          target.setValues([{rowKey: row.id, values: {barCode: this.maxBarCodeInfo, commodityUnit: unit?unit:'只'}}])
        }
      },
      onDeleted(value) {
        this.meDeleteIdList = (value)
      },
      //单元值改变一个字符就触发一次
      onValueChange(event) {
        const { type, row, column, value, target } = event
        switch(column.key) {
          case "purchaseDecimal":
          case "commodityDecimal":
          case "wholesaleDecimal":
          case "lowDecimal":
            this.changeDecimalByValue(row)
            break;
        }
      },
      //修改商品明细中的价格触发计算
      changeDecimalByValue(row) {
        let unitArr = this.unitList
        let basicUnit = '', otherUnit = '', ratio = 1, otherUnitTwo = '', ratioTwo = 1, otherUnitThree = '', ratioThree = 1
        for (let i = 0; i < unitArr.length; i++) {
          if(unitArr[i].id === this.form.getFieldValue('unitId')) {
            basicUnit = unitArr[i].basicUnit
            otherUnit = unitArr[i].otherUnit
            ratio = unitArr[i].ratio
            if(unitArr[i].otherUnitTwo) {
              otherUnitTwo = unitArr[i].otherUnitTwo
              ratioTwo = unitArr[i].ratioTwo
            }
            if(unitArr[i].otherUnitThree) {
              otherUnitThree = unitArr[i].otherUnitThree
              ratioThree = unitArr[i].ratioThree
            }
          }
        }
        if(row.commodityUnit === basicUnit) {
          this.$refs.editableMeTable.getValues((error, values) => {
            let mArr = values, basicPurchaseDecimal='', basicCommodityDecimal='', basicWholesaleDecimal='', basicLowDecimal=''
            for (let i = 0; i < mArr.length; i++) {
              let mInfo = mArr[i]
              if(i===0) {
                basicPurchaseDecimal = mInfo.purchaseDecimal
                basicCommodityDecimal = mInfo.commodityDecimal
                basicWholesaleDecimal = mInfo.wholesaleDecimal
                basicLowDecimal = mInfo.lowDecimal
              } else {
                //副单位进行换算
                if(basicPurchaseDecimal) { mInfo.purchaseDecimal = (basicPurchaseDecimal*ratio).toFixed(2)}
                if(basicCommodityDecimal) { mInfo.commodityDecimal = (basicCommodityDecimal*ratio).toFixed(2)}
                if(basicWholesaleDecimal) { mInfo.wholesaleDecimal = (basicWholesaleDecimal*ratio).toFixed(2)}
                if(basicLowDecimal) { mInfo.lowDecimal = (basicLowDecimal*ratio).toFixed(2)}
                if(otherUnitTwo && i===2) {
                  if(basicPurchaseDecimal) { mInfo.purchaseDecimal = (basicPurchaseDecimal*ratioTwo).toFixed(2)}
                  if(basicCommodityDecimal) { mInfo.commodityDecimal = (basicCommodityDecimal*ratioTwo).toFixed(2)}
                  if(basicWholesaleDecimal) { mInfo.wholesaleDecimal = (basicWholesaleDecimal*ratioTwo).toFixed(2)}
                  if(basicLowDecimal) { mInfo.lowDecimal = (basicLowDecimal*ratioTwo).toFixed(2)}
                }
                if(otherUnitThree && i===3) {
                  if(basicPurchaseDecimal) { mInfo.purchaseDecimal = (basicPurchaseDecimal*ratioThree).toFixed(2)}
                  if(basicCommodityDecimal) { mInfo.commodityDecimal = (basicCommodityDecimal*ratioThree).toFixed(2)}
                  if(basicWholesaleDecimal) { mInfo.wholesaleDecimal = (basicWholesaleDecimal*ratioThree).toFixed(2)}
                  if(basicLowDecimal) { mInfo.lowDecimal = (basicLowDecimal*ratioThree).toFixed(2)}
                }
              }
            }
            this.meTable.dataSource = mArr
          })
        }
      },
      batchSetPrice(type) {
        if(this.manySkuSelected>0 || this.model.id){
          this.$refs.priceModalForm.add(type);
          this.$refs.priceModalForm.disableSubmit = false;
        } else {
          this.$message.warning('抱歉，只有开启多属性才能进行批量操作！');
        }
      },
      batchSetStock(type) {
        this.$refs.stockModalForm.add(type);
        this.$refs.stockModalForm.disableSubmit = false;
      },
      batchSetPriceModalFormOk(price, batchType) {
        let arr = this.meTable.dataSource
        if(arr.length === 0) {
          this.$message.warning('请先录入条码、单位等信息！');
        } else {
          let meTableData = []
          for (let i = 0; i < arr.length; i++) {
            let meInfo = {barCode: arr[i].barCode, commodityUnit: arr[i].commodityUnit, sku: arr[i].sku,
              purchaseDecimal: arr[i].purchaseDecimal, commodityDecimal: arr[i].commodityDecimal,
              wholesaleDecimal: arr[i].wholesaleDecimal, lowDecimal: arr[i].lowDecimal}
            if(batchType === 'purchase') {
              meInfo.purchaseDecimal = price-0
            } else if(batchType === 'commodity') {
              meInfo.commodityDecimal = price-0
            } else if(batchType === 'wholesale') {
              meInfo.wholesaleDecimal = price-0
            } else if(batchType === 'low') {
              meInfo.lowDecimal = price-0
            }
            if(arr[i].id) {
              meInfo.id = arr[i].id
            }
            meTableData.push(meInfo)
          }
          this.meTable.dataSource = meTableData
        }
      },
      batchSetStockModalFormOk(stock, batchType) {
        let arr = this.depotTable.dataSource
        let depotTableData = []
        for (let i = 0; i < arr.length; i++) {
          let depotInfo = {name: arr[i].name, initStock: arr[i].initStock,
            lowSafeStock: arr[i].lowSafeStock, highSafeStock: arr[i].highSafeStock}
          if (batchType === 'initStock') {
            depotInfo.initStock = stock - 0
          } else if (batchType === 'lowSafeStock') {
            depotInfo.lowSafeStock = stock - 0
          } else if (batchType === 'highSafeStock') {
            depotInfo.highSafeStock = stock - 0
          }
          if (arr[i].id) {
            depotInfo.id = arr[i].id
          }
          depotTableData.push(depotInfo)
        }
        this.depotTable.dataSource = depotTableData
      },
      initMaterialAttribute() {
        getMaterialAttributeNameList().then((res)=>{
          if(res) {
            this.materialAttributeList = res
          }
        })
      },
      loadParseMaterialProperty() {
        let mpList = Vue.ls.get('materialPropertyList')
        for (let i = 0; i < mpList.length; i++) {
          if (mpList[i].nativeName === "扩展1") {
            this.mpShort.otherField1.name = mpList[i].anotherName
          }
          if (mpList[i].nativeName === "扩展2") {
            this.mpShort.otherField2.name = mpList[i].anotherName
          }
          if (mpList[i].nativeName === "扩展3") {
            this.mpShort.otherField3.name = mpList[i].anotherName
          }
        }
      },
      handleNameChange(e) {
        let that = this
        if(e.target.value) {
          if(this.setTimeFlag != null){
            clearTimeout(this.setTimeFlag)
          }
          this.setTimeFlag = setTimeout(()=>{
            changeNameToPinYin({name: e.target.value}).then((res) => {
              if (res && res.code === 200) {
                that.form.setFieldsValue({'mnemonic':res.data})
              } else {
                that.$message.warning(res.data)
              }
            })
          },500)
        } else {
          that.form.setFieldsValue({'mnemonic':''})
        }
      },
      onlyUnitOnChange(e) {
        this.$refs.editableMeTable.getValues((error, values) => {
          let mArr = values
          for (let i = 0; i < mArr.length; i++) {
            let mInfo = mArr[i]
            mInfo.commodityUnit = e.target.value
          }
          this.meTable.dataSource = mArr
        })
      },
      manyUnitOnChange(value) {
        let unitArr = this.unitList
        let basicUnit = '', otherUnit = '', ratio = 1, otherUnitTwo = '', ratioTwo = 1, otherUnitThree = '', ratioThree = 1
        for (let i = 0; i < unitArr.length; i++) {
          if(unitArr[i].id === value) {
            basicUnit = unitArr[i].basicUnit
            otherUnit = unitArr[i].otherUnit
            ratio = unitArr[i].ratio
            if(unitArr[i].otherUnitTwo) {
              otherUnitTwo = unitArr[i].otherUnitTwo
              ratioTwo = unitArr[i].ratioTwo
            }
            if(unitArr[i].otherUnitThree) {
              otherUnitThree = unitArr[i].otherUnitThree
              ratioThree = unitArr[i].ratioThree
            }
          }
        }
        this.$refs.editableMeTable.getValues((error, values) => {
          let mArr = values, basicPurchaseDecimal='', basicCommodityDecimal='', basicWholesaleDecimal='', basicLowDecimal=''
          for (let i = 0; i < mArr.length; i++) {
            let mInfo = mArr[i]
            if(i===0) {
              mInfo.commodityUnit = basicUnit
              basicPurchaseDecimal = mInfo.purchaseDecimal
              basicCommodityDecimal = mInfo.commodityDecimal
              basicWholesaleDecimal = mInfo.wholesaleDecimal
              basicLowDecimal = mInfo.lowDecimal
            } else {
              //副单位进行换算
              mInfo.commodityUnit = otherUnit
              if(basicPurchaseDecimal) { mInfo.purchaseDecimal = (basicPurchaseDecimal*ratio).toFixed(2)}
              if(basicCommodityDecimal) { mInfo.commodityDecimal = (basicCommodityDecimal*ratio).toFixed(2)}
              if(basicWholesaleDecimal) { mInfo.wholesaleDecimal = (basicWholesaleDecimal*ratio).toFixed(2)}
              if(basicLowDecimal) { mInfo.lowDecimal = (basicLowDecimal*ratio).toFixed(2)}
              if(otherUnitTwo && i===2) {
                mInfo.commodityUnit = otherUnitTwo
                if(basicPurchaseDecimal) { mInfo.purchaseDecimal = (basicPurchaseDecimal*ratioTwo).toFixed(2)}
                if(basicCommodityDecimal) { mInfo.commodityDecimal = (basicCommodityDecimal*ratioTwo).toFixed(2)}
                if(basicWholesaleDecimal) { mInfo.wholesaleDecimal = (basicWholesaleDecimal*ratioTwo).toFixed(2)}
                if(basicLowDecimal) { mInfo.lowDecimal = (basicLowDecimal*ratioTwo).toFixed(2)}
              }
              if(otherUnitThree && i===3) {
                mInfo.commodityUnit = otherUnitThree
                if(basicPurchaseDecimal) { mInfo.purchaseDecimal = (basicPurchaseDecimal*ratioThree).toFixed(2)}
                if(basicCommodityDecimal) { mInfo.commodityDecimal = (basicCommodityDecimal*ratioThree).toFixed(2)}
                if(basicWholesaleDecimal) { mInfo.wholesaleDecimal = (basicWholesaleDecimal*ratioThree).toFixed(2)}
                if(basicLowDecimal) { mInfo.lowDecimal = (basicLowDecimal*ratioThree).toFixed(2)}
              }
            }
          }
          this.meTable.dataSource = mArr
        })
      },
      unitOnChange (e) {
        let isChecked = e.target.checked;
        if(isChecked) {
          this.unitStatus = true;
          this.manyUnitStatus = false;
          this.unitChecked = true;
        } else {
          this.unitStatus = false;
          this.manyUnitStatus = true;
          this.unitChecked = false;
        }
      },
      addUnit() {
        this.$refs.unitModalForm.add();
        this.$refs.unitModalForm.title = "新增多单位";
        this.$refs.unitModalForm.disableSubmit = false;
      },
      unitModalFormOk() {
        this.loadUnitListData()
      }
    }
  }
</script>
<style scoped>
  .input-table {
    max-width: 100%;
    min-width: 1200px;
  }
  .tag-info {
    font-size:14px;
    height:32px;
    line-height:32px;
    width:100%;
    padding: 0px 11px;
    color: #bbb;
    background-color: #ffffff;
  }
</style>