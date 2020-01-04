<template>
  <div align="center">
  <div align="center" style="width: 450px">
<a-button @click="push">replace</a-button>
<a-form :form="form" @submit="handleSubmit">
  <a-form-item label="Phone" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
    <a-input
      v-decorator="['phone', { rules: [{ required: true, message: 'Please input your phone!' }] }]"
    >
      <a-icon slot="prefix" type="user" />
    </a-input>
  </a-form-item>

  <a-form-item label="Password" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
    <a-input-password
      v-decorator="['password', { rules: [{ required: true, message: 'Please input your note!' }] }]">
      <a-icon slot="prefix" type="user" />
    </a-input-password>
  </a-form-item>

  <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
    <a-button type="primary" html-type="submit">
      登录
    </a-button>
  </a-form-item>

</a-form>
  </div>
  </div>
</template>

<script>
  export default {
    data(){
      return {
        form: this.$form.createForm(this),
      }
    },
    inject: ['reload'],
    name: 'Login',
    methods:{
        handleSubmit(e){
          e.preventDefault();
          this.form.validateFields((err, values) => {
            if (!err) {
             console.log(values)
            }
          });
          console.log("你点击了提交按钮")
        },
      push(){
        this.reload()
        this.$router.push({path:'/home',query:{userName:'lvhaijun'}})
        console.log("你点击跳转按钮")
      }
    }
  }
</script>

<style scoped>

</style>
