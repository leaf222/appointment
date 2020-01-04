<template>
  <a-layout >
    <sidebar></sidebar>
    <a-layout>
      <a-layout-header style="background: #e9e9e9;" >
        <div align="right">
        <span>{{userName}}</span>
        <a-avatar :src="avaterUrl" >
        </a-avatar>
        </div>
      </a-layout-header>
      <breadcrumb></breadcrumb>
      <a-layout-content :style="{ margin: '24px 16px', padding: '0px',background: '#fff', minHeight: '280px' }">
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script>
  import Breadcrumb from './Breadcrumb'
  import Sidebar from './Sidebar'

  export default {
    name: 'Home',
    data () {
      return {
        userName:'lvhaijun',
        avaterUrl:'https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png'
      }
    },
    components: {
      Breadcrumb,
      Sidebar
    },
    mounted () {
      let pa=this.$route.query.userName
      console.log(pa)
    }
  }
</script>

<style scoped>

</style>
