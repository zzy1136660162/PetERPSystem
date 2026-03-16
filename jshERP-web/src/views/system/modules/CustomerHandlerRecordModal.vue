<template>
  <div ref="container">
    <a-modal
      :title="title"
      :width="800"
      :visible="visible"
      :confirmLoading="confirmLoading"
      :getContainer="() => $refs.container"
      :maskStyle="{'top':'93px','left':'154px'}"
      :wrapClassName="wrapClassNameInfo()"
      :mask="isDesktop()"
      :maskClosable="false"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="取消"
      okText="保存"
      style="top:10%;height: 80%;">
      <a-spin :spinning="confirmLoading">
        <a-form :form="form" id="customerHandlerRecordModal">
          <a-row class="form-row" :gutter="24">
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户">
                <a-select
                  placeholder="请选择客户"
                  v-decorator="['customerId', validatorRules.customerId]"
                  :disabled="isEdit"
                  showSearch
                  optionFilterProp="children"
                  @change="handleCustomerChange">
                  <a-select-option v-for="item in customerList" :key="item.id" :value="item.id">
                    {{ item.supplier }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="经手人">
                <a-select
                  placeholder="请选择经手人"
                  v-decorator="['handlerId', validatorRules.handlerId]"
                  showSearch
                  optionFilterProp="children">
                  <a-select-option v-for="item in userList" :key="item.id" :value="item.id">
                    {{ item.username }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="24">
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开始时间">
                <a-date-picker
                  showTime
                  format="YYYY-MM-DD HH:mm:ss"
                  v-decorator="['followUpTime', validatorRules.followUpTime]"
                  style="width: 100%"/>
              </a-form-item>
            </a-col>
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="结束时间">
                <a-date-picker
                  showTime
                  format="YYYY-MM-DD HH:mm:ss"
                  v-decorator="['nextFollowUpTime']"
                  style="width: 100%"/>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="24">
            <a-col :span="24">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="交接原因">
                <a-textarea :rows="2" placeholder="请输入交接原因" v-decorator="['content']" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="24">
            <a-col :span="24">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                <a-textarea :rows="2" placeholder="请输入备注" v-decorator="['remark']" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script>
  import pick from 'lodash.pick'
  import { addCustomerHandlerRecord, getCustomerList, getUserList } from '@/api/api'
  import { autoJumpNextInput } from "@/utils/util"
  import { mixinDevice } from '@/utils/mixin'
  import moment from 'moment'

  export default {
    name: "CustomerHandlerRecordModal",
    mixins: [mixinDevice],
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        isEdit: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 4 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        confirmLoading: false,
        form: this.$form.createForm(this),
        validatorRules:{
          customerId:{
            rules: [
              { required: true, message: '请选择客户!' }
            ]
          },
          handlerId:{
            rules: [
              { required: true, message: '请选择经手人!' }
            ]
          },
          followUpTime:{
            rules: [
              { required: true, message: '请选择开始时间!' }
            ]
          }
        },
        customerList: [],
        userList: []
      }
    },
    created () {
      this.loadCustomerList()
      this.loadUserList()
    },
    methods: {
      add () {
        this.edit({})
      },
      edit (record) {
        this.isEdit = record.id ? true : false
        this.form.resetFields()
        this.model = Object.assign({}, record)
        this.visible = true
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'customerId','handlerId','content','remark'))
          if(this.model.followUpTime) {
            this.form.setFieldsValue({followUpTime: moment(this.model.followUpTime)})
          }
          if(this.model.nextFollowUpTime) {
            this.form.setFieldsValue({nextFollowUpTime: moment(this.model.nextFollowUpTime)})
          }
        })
      },
      close () {
        this.$emit('close')
        this.visible = false
      },
      handleOk () {
        const that = this
        this.form.validateFields((err, values) => {
          if(!err) {
            that.confirmLoading = true
            let formData = Object.assign(this.model, values)
            formData.followUpTime = values.followUpTime ? values.followUpTime.format('YYYY-MM-DD HH:mm:ss') : null
            formData.nextFollowUpTime = values.nextFollowUpTime ? values.nextFollowUpTime.format('YYYY-MM-DD HH:mm:ss') : null
            // 经手记录只支持新增，不支持编辑
            addCustomerHandlerRecord(formData).then((res)=>{
              if(res.code === 200) {
                that.$emit('ok')
              } else {
                that.$message.warning(res.data)
              }
            }).finally(() => {
              that.confirmLoading = false
              that.close()
            })
          }
        })
      },
      handleCancel () {
        this.close()
      },
      loadCustomerList() {
        getCustomerList({ type: '客户' }).then(res => {
          if(res.code === 200) {
            this.customerList = res.data
          }
        })
      },
      loadUserList() {
        getUserList().then(res => {
          if(res.code === 200) {
            this.userList = res.data
          }
        })
      },
      handleCustomerChange(value) {
        // 可以在这里加载客户的当前经手人信息
      }
    }
  }
</script>
<style scoped>

</style>
