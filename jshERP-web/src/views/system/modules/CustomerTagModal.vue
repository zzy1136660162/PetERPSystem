<template>
  <div ref="container">
    <a-modal
      :title="title"
      :width="600"
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
        <a-form :form="form" id="customerTagModal">
          <a-row class="form-row" :gutter="24">
            <a-col :span="24">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标签名称">
                <a-input placeholder="请输入标签名称" v-decorator.trim="[ 'tagName', validatorRules.tagName]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="24">
            <a-col :span="24">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标签颜色">
                <a-select placeholder="请选择标签颜色" v-decorator="['tagColor', validatorRules.tagColor]">
                  <a-select-option value="red">
                    <a-tag color="red">红色</a-tag>
                  </a-select-option>
                  <a-select-option value="orange">
                    <a-tag color="orange">橙色</a-tag>
                  </a-select-option>
                  <a-select-option value="green">
                    <a-tag color="green">绿色</a-tag>
                  </a-select-option>
                  <a-select-option value="cyan">
                    <a-tag color="cyan">青色</a-tag>
                  </a-select-option>
                  <a-select-option value="blue">
                    <a-tag color="blue">蓝色</a-tag>
                  </a-select-option>
                  <a-select-option value="purple">
                    <a-tag color="purple">紫色</a-tag>
                  </a-select-option>
                  <a-select-option value="magenta">
                    <a-tag color="magenta">洋红</a-tag>
                  </a-select-option>
                  <a-select-option value="#f50">
                    <a-tag color="#f50">#f50</a-tag>
                  </a-select-option>
                  <a-select-option value="#2db7f5">
                    <a-tag color="#2db7f5">#2db7f5</a-tag>
                  </a-select-option>
                  <a-select-option value="#87d068">
                    <a-tag color="#87d068">#87d068</a-tag>
                  </a-select-option>
                  <a-select-option value="#108ee9">
                    <a-tag color="#108ee9">#108ee9</a-tag>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="24">
            <a-col :span="24">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="排序">
                <a-input-number style="width:100%" placeholder="请输入排序" v-decorator.trim="[ 'sort' ]" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row class="form-row" :gutter="24">
            <a-col :span="24">
              <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="备注">
                <a-textarea :rows="3" placeholder="请输入备注" v-decorator.trim="[ 'remark' ]" />
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
  import { addCustomerTag, editCustomerTag } from '@/api/api'
  import { autoJumpNextInput } from "@/utils/util"
  import { mixinDevice } from '@/utils/mixin'

  export default {
    name: "CustomerTagModal",
    mixins: [mixinDevice],
    data () {
      return {
        title:"操作",
        visible: false,
        model: {},
        isReadOnly: false,
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
          tagName:{
            rules: [
              { required: true, message: '请输入标签名称!' },
              { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
            ]
          },
          tagColor:{
            rules: [
              { required: true, message: '请选择标签颜色!' }
            ]
          }
        },
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'tagName', 'tagColor', 'sort', 'remark'))
          autoJumpNextInput('customerTagModal')
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
            let obj;
            if(!this.model.id){
              obj = addCustomerTag(formData);
            } else {
              obj = editCustomerTag(formData);
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
