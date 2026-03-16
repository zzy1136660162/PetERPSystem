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
      <template slot="footer">
        <a-button key="back" v-if="isReadOnly" @click="handleCancel">
          取消
        </a-button>
      </template>
      <a-spin :spinning="confirmLoading">
        <a-form :form="form" id="customerFollowUpModal">
          <a-row class="form-row" :gutter="24">
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="客户">
                <a-select
                  placeholder="请选择客户"
                  v-decorator="['customerId', validatorRules.customerId]"
                  showSearch
                  :filterOption="filterOption"
                  @change="handleCustomerChange">
                  <a-select-option v-for="item in customerList" :key="item.id" :value="item.id">
                    {{ item.supplier }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="跟进时间">
                <a-date-picker
                  showTime
                  format="YYYY-MM-DD HH:mm:ss"
                  style="width: 100%"
                  v-decorator="['followUpTime', validatorRules.followUpTime]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="24">
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="跟进类型">
                <a-select placeholder="请选择跟进类型" v-decorator="['followUpType', validatorRules.followUpType]">
                  <a-select-option value="电话沟通">电话沟通</a-select-option>
                  <a-select-option value="上门拜访">上门拜访</a-select-option>
                  <a-select-option value="邮件沟通">邮件沟通</a-select-option>
                  <a-select-option value="微信沟通">微信沟通</a-select-option>
                  <a-select-option value="其他">其他</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="跟进状态">
                <a-select placeholder="请选择跟进状态" v-decorator="['followUpStatus', validatorRules.followUpStatus]">
                  <a-select-option value="待跟进">待跟进</a-select-option>
                  <a-select-option value="跟进中">跟进中</a-select-option>
                  <a-select-option value="已成交">已成交</a-select-option>
                  <a-select-option value="已放弃">已放弃</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="24">
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="下次跟进时间">
                <a-date-picker
                  showTime
                  format="YYYY-MM-DD HH:mm:ss"
                  style="width: 100%"
                  v-decorator="['nextFollowUpTime']" />
              </a-form-item>
            </a-col>
            <a-col :span="24/2">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="跟进人">
                <a-input placeholder="请输入跟进人" v-decorator="['operator']" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="24">
            <a-col :span="24">
              <a-form-item :labelCol="{span: 2}" :wrapperCol="{span: 22}" label="跟进内容">
                <a-textarea
                  :rows="4"
                  placeholder="请输入跟进内容"
                  v-decorator="['content', validatorRules.content]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="24">
            <a-col :span="24">
              <a-form-item :labelCol="{span: 2}" :wrapperCol="{span: 22}" label="备注">
                <a-textarea
                  :rows="2"
                  placeholder="请输入备注"
                  v-decorator="['remark']" />
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
  import { addCustomerFollowUp, editCustomerFollowUp, findBySelectCus } from '@/api/api'
  import { autoJumpNextInput } from "@/utils/util"
  import { mixinDevice } from '@/utils/mixin'
  import moment from 'moment'

  export default {
    name: "CustomerFollowUpModal",
    mixins: [mixinDevice],
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        isReadOnly: false,
        customerList: [],
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
          followUpTime:{
            rules: [
              { required: true, message: '请选择跟进时间!' }
            ]
          },
          followUpType:{
            rules: [
              { required: true, message: '请选择跟进类型!' }
            ]
          },
          followUpStatus:{
            rules: [
              { required: true, message: '请选择跟进状态!' }
            ]
          },
          content:{
            rules: [
              { required: true, message: '请输入跟进内容!' }
            ]
          }
        },
      }
    },
    created () {
      this.loadCustomerList()
    },
    methods: {
      loadCustomerList() {
        findBySelectCus({}).then((res) => {
          if (res) {
            this.customerList = res
          }
        })
      },
      filterOption(input, option) {
        return (
          option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
        );
      },
      handleCustomerChange(value) {
        const customer = this.customerList.find(item => item.id === value)
        if (customer) {
          this.model.customerName = customer.supplier
        }
      },
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          let formData = pick(this.model, 'customerId', 'followUpType', 'followUpStatus', 'content', 'operator', 'remark')
          // 处理日期字段
          if (this.model.followUpTime) {
            formData.followUpTime = moment(this.model.followUpTime)
          }
          if (this.model.nextFollowUpTime) {
            formData.nextFollowUpTime = moment(this.model.nextFollowUpTime)
          }
          this.form.setFieldsValue(formData)
          autoJumpNextInput('customerFollowUpModal')
        });
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let formData = Object.assign(this.model, values);
            // 处理日期格式
            if (formData.followUpTime) {
              formData.followUpTime = formData.followUpTime.format('YYYY-MM-DD HH:mm:ss')
            }
            if (formData.nextFollowUpTime) {
              formData.nextFollowUpTime = formData.nextFollowUpTime.format('YYYY-MM-DD HH:mm:ss')
            }
            let obj;
            if(!this.model.id){
              obj = addCustomerFollowUp(formData);
            } else {
              obj = editCustomerFollowUp(formData);
            }
            obj.then((res)=>{
              if(res.code === 200){
                that.$emit('ok');
              } else {
                that.$message.warning(res.data.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
        })
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>

<style scoped>
</style>
