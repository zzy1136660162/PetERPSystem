<!-- from 7 5 2 7 1 8 9 2 0 -->
<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false" class="all">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="24">
                <a-form-item label="商品信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入条码、名称、助记码、规格、型号等信息" v-model="queryParam.materialParam"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="单据日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-range-picker
                    style="width: 100%"
                    v-model="queryParam.createTimeRange"
                    format="YYYY-MM-DD"
                    :placeholder="['开始时间', '结束时间']"
                    @change="onDateChange"
                  />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                  <a-button style="margin-left: 8px" v-print="'#reportPrint'" icon="printer">打印</a-button>
                  <a-button style="margin-left: 8px" @click="exportExcel" icon="download">导出</a-button>
                  <a @click="handleToggleSearch" style="margin-left: 8px">
                    {{ toggleSearchStatus ? '收起' : '展开' }}
                    <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                  </a>
                </span>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item>
                  <span>实际采购金额：{{realityPriceTotal}}</span>
                </a-form-item>
              </a-col>
            </a-row>
            <template v-if="toggleSearchStatus">
              <a-row :gutter="24">
                <a-col :md="6" :sm="24">
                  <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select placeholder="请选择供应商" v-model="queryParam.organId"
                              :dropdownMatchSelectWidth="false" showSearch allow-clear optionFilterProp="children" @search="handleSearchSupplier">
                      <div slot="dropdownRender" slot-scope="menu">
                        <v-nodes :vnodes="menu" />
                        <a-divider style="margin: 4px 0;" />
                        <div class="dropdown-btn" @mousedown="e => e.preventDefault()" @click="initSupplier"><a-icon type="reload" /> 刷新列表</div>
                      </div>
                      <a-select-option v-for="(item,index) in supList" :key="index" :value="item.id">
                        {{ item.supplier }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="仓库" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-select
                      optionFilterProp="children"
                      :dropdownMatchSelectWidth="false"
                      showSearch allow-clear style="width: 100%"
                      placeholder="请选择仓库"
                      v-model="queryParam.depotId">
                      <a-select-option v-for="(depot,index) in depotList" :value="depot.id" :key="index">
                        {{ depot.depotName }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24" v-if="orgaTree.length">
                  <a-form-item label="机构" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-tree-select style="width:100%" allow-clear :treeData="orgaTree"
                                   v-model="queryParam.organizationId" placeholder="请选择机构">
                    </a-tree-select>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24">
                  <a-form-item label="商品类别" :labelCol="labelCol" :wrapperCol="wrapperCol">
                    <a-tree-select style="width:100%" :dropdownStyle="{maxHeight:'200px',overflow:'auto'}" allow-clear
                                   :treeData="categoryTree" v-model="queryParam.categoryId" placeholder="请选择商品类别">
                    </a-tree-select>
                  </a-form-item>
                </a-col>
              </a-row>
            </template>
          </a-form>
        </div>
        <!-- table区域-begin -->
        <section ref="print" id="reportPrint">
          <a-table
            bordered
            ref="table"
            size="middle"
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :components="handleDrag(columns)"
            :pagination="false"
            :scroll="scroll"
            :loading="loading"
            @change="handleTableChange">
            <span slot="customTitle">
              <a-popover trigger="click" placement="right">
                <template slot="content">
                  <a-checkbox-group @change="onColChange" v-model="settingDataIndex" :defaultValue="settingDataIndex">
                    <a-row style="width: 600px">
                      <template v-for="(item,index) in defColumns">
                        <template>
                          <a-col :span="6">
                            <a-checkbox :value="item.dataIndex" v-if="item.dataIndex==='rowIndex'" disabled></a-checkbox>
                            <a-checkbox :value="item.dataIndex" v-if="item.dataIndex!=='rowIndex'">
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
                <a-icon type="setting" />
              </a-popover>
            </span>
          </a-table>
          <a-row :gutter="24" style="margin-top: 8px;text-align:right;">
            <a-col :md="24" :sm="24">
              <a-pagination @change="paginationChange" @showSizeChange="paginationShowSizeChange"
                size="small"
                show-size-changer
                :showQuickJumper="true"
                :current="ipagination.current"
                :page-size="ipagination.pageSize"
                :page-size-options="ipagination.pageSizeOptions"
                :total="ipagination.total"
                :show-total="(total, range) => `共 ${total-Math.ceil(total/ipagination.pageSize)} 条`">
                <template slot="buildOptionText" slot-scope="props">
                  <span>{{ props.value-1 }}条/页</span>
                </template>
              </a-pagination>
            </a-col>
          </a-row>
        </section>
        <!-- table区域-end -->
        <!-- 图表区域 - 供应商统计 -->
        <a-row :gutter="24" style="margin-top: 24px;">
          <a-col :sm="24" :md="8" :style="{ marginBottom: '12px' }">
            <a-card :loading="chartLoading" :bordered="false" :body-style="{padding: '12px', height: barHeight + 60 + 'px'}">
              <bar title="供应商采购数量" :height="barHeight" yaxisText="数量" :dataSource="supplierQuantityData"/>
            </a-card>
          </a-col>
          <a-col :sm="24" :md="8" :style="{ marginBottom: '12px' }">
            <a-card :loading="chartLoading" :bordered="false" :body-style="{padding: '12px', height: barHeight + 60 + 'px'}">
              <bar title="供应商采购金额" :height="barHeight" yaxisText="金额" :dataSource="supplierAmountData"/>
            </a-card>
          </a-col>
          <a-col :sm="24" :md="8" :style="{ marginBottom: '12px' }">
            <a-card :loading="chartLoading" :bordered="false" :body-style="{padding: '12px', height: barHeight + 60 + 'px'}">
              <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                <h3 style="margin: 0;">宠物商品采购趋势</h3>
                <a-select
                  v-model="selectedYear"
                  style="width: 120px;"
                  size="small"
                  showSearch
                  :showArrow="false"
                  :filterOption="filterYearOption"
                  @change="onYearChange"
                  placeholder="输入年份">
                  <a-select-option v-for="year in availableYears" :key="year" :value="year">{{ year }}年</a-select-option>
                </a-select>
              </div>
              <area-chart-ty :title="''" :height="barHeight" x="月份" y="数量" :dataSource="dateTrendData" :padding="['auto', 'auto', '40', '50']"/>
            </a-card>
          </a-col>
        </a-row>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getMpListShort, getPrevMonthFormatDate, getFormatDate } from '@/utils/util'
  import {getAction} from '@/api/manage'
  import {findBySelectSup, queryMaterialCategoryTreeList, getAllOrganizationTreeByUser} from '@/api/api'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import Bar from '@/components/chart/Bar'
  import AreaChartTy from '@/components/chart/AreaChartTy'
  import moment from 'moment'
  import Vue from 'vue'
  export default {
    name: "BuyInReport",
    mixins:[JeecgListMixin],
    components: {
      JEllipsis,
      Bar,
      AreaChartTy,
      VNodes: {
        functional: true,
        render: (h, ctx) => ctx.props.vnodes,
      }
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
          materialParam:'',
          beginTime: getPrevMonthFormatDate(3),
          endTime: getFormatDate(),
          createTimeRange: [moment(getPrevMonthFormatDate(3)), moment(getFormatDate())],
          organId: undefined,
          depotId: undefined,
          organizationId: undefined,
          mpList: getMpListShort(Vue.ls.get('materialPropertyList'))
        },
        ipagination:{
          pageSize: 11,
          pageSizeOptions: ['11', '21', '31', '101', '201']
        },
        supList: [],
        depotList: [],
        orgaTree: [],
        categoryTree:[],
        realityPriceTotal: '',
        setTimeFlag: null,
        tabKey: "1",
        pageName: 'buyInReport',
        chartLoading: false,
        supplierQuantityData: [],
        supplierAmountData: [],
        dateTrendData: [],
        barHeight: document.documentElement.clientHeight-480,
        lastDataVersion: null,
        selectedYear: 2026, // 默认2026年
        // 默认索引
        defDataIndex:['rowIndex','barCode','materialName','materialStandard','materialModel','materialUnit',
          'inSum','inSumPrice','outSum','outSumPrice','inOutSumPrice'],
        // 默认列
        defColumns: [
          {
            dataIndex: 'rowIndex', width:40, align:"center", slots: { title: 'customTitle' },
            customRender:function (t,r,index) {
              return (t !== '合计') ? (parseInt(index) + 1) : t
            }
          },
          {title: '条码', dataIndex: 'barCode', sorter: (a, b) => a.barCode - b.barCode, width: 160},
          {title: '名称', dataIndex: 'materialName', width: 160, ellipsis:true},
          {title: '规格', dataIndex: 'materialStandard', width: 80, ellipsis:true},
          {title: '型号', dataIndex: 'materialModel', width: 80, ellipsis:true},
          {title: '颜色', dataIndex: 'materialColor', width: 60, ellipsis:true},
          {title: '品牌', dataIndex: 'materialBrand', width: 80, ellipsis:true},
          {title: '制造商', dataIndex: 'materialMfrs', width: 80, ellipsis:true},
          {title: '扩展1', dataIndex: 'otherField1', width: 80, ellipsis:true},
          {title: '扩展2', dataIndex: 'otherField2', width: 80, ellipsis:true},
          {title: '扩展3', dataIndex: 'otherField3', width: 80, ellipsis:true},
          {title: '单位', dataIndex: 'materialUnit', width: 80, ellipsis:true},
          {title: '采购数量', dataIndex: 'inSum', sorter: (a, b) => a.inSum - b.inSum, width: 80},
          {title: '采购金额', dataIndex: 'inSumPrice', sorter: (a, b) => a.inSumPrice - b.inSumPrice, width: 80},
          {title: '退货数量', dataIndex: 'outSum', sorter: (a, b) => a.outSum - b.outSum, width: 80},
          {title: '退货金额', dataIndex: 'outSumPrice', sorter: (a, b) => a.outSumPrice - b.outSumPrice, width: 80},
          {title: '实际采购金额', dataIndex: 'inOutSumPrice', sorter: (a, b) => a.inOutSumPrice - b.inOutSumPrice, width: 100}
        ],
        url: {
          list: "/depotItem/buyIn"
        }
      }
    },
    computed: {
      // 计算可用的年份列表（从2026年开始，如果当前年份大于列表最大年份则自动添加）
      availableYears() {
        const startYear = 2026 // 从2026年开始
        const currentYear = new Date().getFullYear()
        const years = []
        
        // 确定结束年份（当前年份和2031年的较大值）
        const endYear = Math.max(startYear + 5, currentYear)
        
        for (let year = startYear; year <= endYear; year++) {
          years.push(year)
        }
        
        // 如果选中的年份不在列表中且 >= 2026，添加到列表
        if (this.selectedYear && this.selectedYear >= 2026 && !years.includes(this.selectedYear)) {
          years.push(this.selectedYear)
        }
        
        // 排序
        years.sort((a, b) => a - b)
        
        return years
      }
    },
    created () {
      this.initSupplier()
      this.getDepotData()
      this.loadAllOrgaData()
      this.loadCategoryTreeData()
      this.initColumnsSetting()
      this.handleChangeOtherField(0)
      // 监听数据变化事件
      this.setupDataChangeListener()
    },
    beforeDestroy() {
      // 移除事件监听
      this.removeDataChangeListener()
    },
    methods: {
      moment,
      getQueryParams() {
        let param = Object.assign({}, this.queryParam, this.isorter);
        param.monthTime = this.queryParam.monthTime;
        param.field = this.getQueryField();
        param.currentPage = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize-1;
        return param;
      },
      onDateChange: function (value, dateString) {
        this.queryParam.beginTime=dateString[0]
        this.queryParam.endTime=dateString[1]
        if(dateString[0] && dateString[1]) {
          this.queryParam.createTimeRange = [moment(dateString[0]), moment(dateString[1])]
        }
      },
      loadData(arg) {
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        let params = this.getQueryParams();//查询条件
        this.loading = true;
        this.chartLoading = true;
        
        // 同时加载列表数据和图表明细数据
        Promise.all([
          getAction(this.url.list, params),
          this.loadChartDetailData()
        ]).then(([listRes]) => {
          if (listRes.code===200) {
            this.dataSource = listRes.data.rows;
            this.ipagination.total = listRes.data.total;
            this.tableAddTotalRow(this.columns, this.dataSource)
            this.realityPriceTotal = listRes.data.realityPriceTotal
          } else if(listRes.code===510){
            this.$message.warning(listRes.data)
          } else {
            this.$message.warning(listRes.data.message)
          }
          this.loading = false;
          this.chartLoading = false;
        }).catch(() => {
          this.loading = false;
          this.chartLoading = false;
        })
      },
      // 加载图表明细数据（从findInOutDetail接口获取）
      loadChartDetailData() {
        const params = {
          currentPage: 1,
          pageSize: 1000,
          beginTime: this.queryParam.beginTime,
          endTime: this.queryParam.endTime,
          organId: this.queryParam.organId,
          depotId: this.queryParam.depotId,
          materialParam: this.queryParam.materialParam || '',
          number: '',
          type: '入库',
          remark: ''
        }
        
        return getAction('/depotHead/findInOutDetail', params).then((res) => {
          if (res.code === 200 && res.data.rows) {
            this.processChartDataFromDetail(res.data.rows)
          }
        }).catch((err) => {
          console.error('获取图表明细数据失败:', err)
          // 失败时使用现有数据生成图表
          this.processChartDataFromCurrentData()
        })
      },
      // 从明细数据生成图表（包含供应商和月份信息）
      processChartDataFromDetail(rows) {
        // 获取查询年份（确保不小于2026）
        const beginTime = this.queryParam.beginTime
        let queryYear = this.selectedYear || 2026
        if (beginTime) {
          queryYear = new Date(beginTime).getFullYear()
        }
        if (queryYear < 2026) {
          queryYear = 2026
        }
        this.selectedYear = queryYear
        
        // 获取基础资料中的供应商列表
        const suppliers = this.supList || []
        
        // 创建供应商ID到名称的映射
        const supplierIdMap = new Map()
        const supplierNameMap = new Map()
        suppliers.forEach(s => {
          if (s.id) {
            supplierIdMap.set(s.id.toString(), s.supplier)
          }
          if (s.supplier) {
            supplierNameMap.set(s.supplier.toLowerCase().trim(), s.supplier)
          }
        })
        
        // 按供应商统计
        const supplierQtyMap = new Map()
        const supplierAmtMap = new Map()
        
        // 按月份统计
        const monthNames = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        const monthData = new Array(12).fill(0)
        
        rows.forEach(item => {
          // 获取基础资料中的供应商名称
          let supplierName = '未知供应商'
          
          // 优先使用organId匹配
          if (item.organId && supplierIdMap.has(item.organId.toString())) {
            supplierName = supplierIdMap.get(item.organId.toString())
          } else if (item.sname) {
            // 使用sname匹配基础资料中的标准名称
            const normalizedName = item.sname.toLowerCase().trim()
            if (supplierNameMap.has(normalizedName)) {
              supplierName = supplierNameMap.get(normalizedName)
            } else {
              supplierName = item.sname
            }
          }
          
          const qty = parseFloat(item.operNumber) || 0
          const amt = parseFloat(item.allPrice) || 0
          
          if (supplierQtyMap.has(supplierName)) {
            supplierQtyMap.set(supplierName, supplierQtyMap.get(supplierName) + qty)
          } else {
            supplierQtyMap.set(supplierName, qty)
          }
          
          if (supplierAmtMap.has(supplierName)) {
            supplierAmtMap.set(supplierName, supplierAmtMap.get(supplierName) + amt)
          } else {
            supplierAmtMap.set(supplierName, amt)
          }
          
          // 月份统计（根据实际operTime）
          const operTime = item.operTime
          if (operTime) {
            const date = new Date(operTime)
            const itemYear = date.getFullYear()
            const month = date.getMonth() // 0-11
            
            // 只统计选中年份的数据
            if (itemYear === queryYear && month >= 0 && month < 12) {
              monthData[month] += qty
            }
          }
        })
        
        // 更新供应商图表数据（取前10个）
        this.supplierQuantityData = Array.from(supplierQtyMap.entries())
          .sort((a, b) => b[1] - a[1])
          .slice(0, 10)
          .map(([name, value]) => ({ x: name, y: Math.round(value) }))
        
        this.supplierAmountData = Array.from(supplierAmtMap.entries())
          .sort((a, b) => b[1] - a[1])
          .slice(0, 10)
          .map(([name, value]) => ({ x: name, y: Math.round(value) }))
        
        // 更新折线图数据
        this.dateTrendData = monthNames.map((name, index) => ({
          x: name,
          y: Math.round(monthData[index])
        }))
      },
      // 从当前数据生成图表（备用方案）
      processChartDataFromCurrentData() {
        // 获取查询年份（确保不小于2026）
        const beginTime = this.queryParam.beginTime
        let queryYear = this.selectedYear || 2026
        if (beginTime) {
          queryYear = new Date(beginTime).getFullYear()
        }
        if (queryYear < 2026) {
          queryYear = 2026
        }
        this.selectedYear = queryYear
        
        // 使用表格数据（dataSource）生成图表
        const rows = this.dataSource || []
        
        // 按月份统计（根据查询日期范围分配）
        const monthNames = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        const monthData = new Array(12).fill(0)
        
        // 解析查询日期范围
        let startMonth = 0
        let endMonth = 11
        if (this.queryParam.beginTime) {
          startMonth = new Date(this.queryParam.beginTime).getMonth()
        }
        if (this.queryParam.endTime) {
          endMonth = new Date(this.queryParam.endTime).getMonth()
        }
        if (isNaN(startMonth) || startMonth < 0) startMonth = 0
        if (isNaN(endMonth) || endMonth < 0 || endMonth > 11) endMonth = 11
        
        // 计算总数量和总金额
        const totalQty = rows.reduce((sum, item) => sum + (parseFloat(item.inSum) || 0), 0)
        const totalAmt = rows.reduce((sum, item) => sum + (parseFloat(item.inSumPrice) || 0), 0)
        
        // 获取供应商名称
        let supplierName = '全部供应商'
        if (this.queryParam.organId && this.supList) {
          const supplier = this.supList.find(s => s.id === this.queryParam.organId)
          if (supplier) {
            supplierName = supplier.supplier
          }
        }
        
        // 更新供应商图表数据（显示当前查询的供应商）
        this.supplierQuantityData = [{ x: supplierName, y: Math.round(totalQty) }]
        this.supplierAmountData = [{ x: supplierName, y: Math.round(totalAmt) }]
        
        // 将数据分配到查询范围内的月份
        const monthCount = endMonth - startMonth + 1
        if (totalQty > 0 && monthCount > 0) {
          const avgPerMonth = totalQty / monthCount
          for (let i = startMonth; i <= endMonth; i++) {
            monthData[i] = Math.round(avgPerMonth)
          }
        }
        
        // 更新折线图数据
        this.dateTrendData = monthNames.map((name, index) => ({
          x: name,
          y: monthData[index]
        }))
      },
      // 年份改变时重新生成折线图数据
      onYearChange(year) {
        // 更新查询条件中的日期范围为选中年份
        const yearStr = year.toString()
        this.queryParam.beginTime = yearStr + '-01-01'
        this.queryParam.endTime = yearStr + '-12-31'
        
        // 重新加载数据以获取该年份的真实数据
        this.loadData(1)
      },
      // 年份下拉列表过滤方法 - 支持直接输入年份（2026年及以后）
      filterYearOption(input, option) {
        // 允许输入年份数字，但必须 >= 2026
        const inputYear = parseInt(input)
        if (!isNaN(inputYear) && inputYear >= 2026 && inputYear <= 2100) {
          return true
        }
        // 否则按正常过滤
        return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      },
      initSupplier() {
        let that = this;
        findBySelectSup({limit:1}).then((res)=>{
          if(res) {
            that.supList = res
          }
        });
      },
      handleSearchSupplier(value) {
        let that = this
        if(this.setTimeFlag != null){
          clearTimeout(this.setTimeFlag);
        }
        this.setTimeFlag = setTimeout(()=>{
          findBySelectSup({key: value, limit:1}).then((res) => {
            if(res) {
              that.supList = res;
            }
          })
        },500)
      },
      getDepotData() {
        getAction('/depot/findDepotByCurrentUser').then((res)=>{
          if(res.code === 200){
            this.depotList = res.data;
          }else{
            this.$message.info(res.data);
          }
        })
      },
      loadAllOrgaData(){
        let that = this
        let params = {}
        getAllOrganizationTreeByUser(params).then((res)=>{
          if(res){
            that.orgaTree = res
          }
        })
      },
      loadCategoryTreeData(){
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
      searchQuery() {
        if(this.queryParam.beginTime == '' || this.queryParam.endTime == ''){
          this.$message.warning('请选择单据日期！')
        } else {
          this.loadData(1);
        }
      },
      // 设置数据变化监听
      setupDataChangeListener() {
        // 监听本地存储的变化（用于跨页面通信）
        window.addEventListener('storage', this.handleStorageChange)
        // 监听自定义事件（用于同页面通信）
        window.addEventListener('buyDataChanged', this.handleDataChange)
        // 页面获得焦点时检查更新
        window.addEventListener('focus', this.checkForUpdates)
      },
      // 移除数据变化监听
      removeDataChangeListener() {
        window.removeEventListener('storage', this.handleStorageChange)
        window.removeEventListener('buyDataChanged', this.handleDataChange)
        window.removeEventListener('focus', this.checkForUpdates)
      },
      // 处理本地存储变化
      handleStorageChange(e) {
        if (e.key === 'buyDataVersion') {
          this.checkForUpdates()
        }
      },
      // 处理数据变化事件
      handleDataChange() {
        this.checkForUpdates()
      },
      // 检查并更新数据
      checkForUpdates() {
        // 获取当前数据版本
        const currentVersion = localStorage.getItem('buyDataVersion')
        const lastVersion = this.lastDataVersion
        
        // 如果版本变化或首次加载，刷新数据
        if (currentVersion !== lastVersion) {
          this.lastDataVersion = currentVersion
          if (!this.loading) {
            this.loadData()
          }
        }
      },
      // 标记数据已变化（供其他页面调用）
      markDataChanged() {
        const version = Date.now().toString()
        localStorage.setItem('buyDataVersion', version)
        // 触发自定义事件通知同页面
        window.dispatchEvent(new CustomEvent('buyDataChanged'))
      },
      exportExcel() {
        let list = []
        let mpStr = getMpListShort(Vue.ls.get('materialPropertyList'))
        let head = '条码,名称,规格,型号,颜色,品牌,制造商,' + mpStr + ',单位,进货数量,进货金额,退货数量,退货金额,实际采购金额'
        for (let i = 0; i < this.dataSource.length; i++) {
          let item = []
          let ds = this.dataSource[i]
          item.push(ds.barCode, ds.materialName, ds.materialStandard, ds.materialModel, ds.materialColor, ds.materialBrand,
            ds.materialMfrs, ds.otherField1, ds.otherField2, ds.otherField3, ds.materialUnit, ds.inSum, ds.inSumPrice, ds.outSum, ds.outSumPrice, ds.inOutSumPrice)
          list.push(item)
        }
        let tip = '单据日期：' + this.queryParam.beginTime + '~' + this.queryParam.endTime
        this.handleExportXlsPost('进货统计', '进货统计', head, tip, list)
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  .all{
    overflow: auto;
  }
</style>