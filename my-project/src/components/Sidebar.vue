<template>

  <a-layout-sider width="250" theme="light" >
    <a-menu
      mode="inline"
      :defaultSelectedKeys="['ActivityMain']"
      :defaultOpenKeys="['Activity', 'User']"
      :style="{ height: '91vh', borderRight: 0 }"
      theme="light"
    >
      <a-sub-menu  v-for="item in SidebarMenu" :key="item.id">
        <span slot="title">
          <a-icon type="bars" />{{item.name}}
        </span>
        <a-menu-item v-for="subItem in item.sub" :key="subItem.componentName" >
          <router-link :to="subItem.path+''">{{subItem.name}}</router-link>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<script>
import SidebarMenu from './SidebarMenuConfig.js';

export default {
  name: "Sidebar",
  data() {
    return {
      SidebarMenu: SidebarMenu
    };
  }
};
</script>

<style scoped>
.logo {
  height: 32px;
  background: rgba(255,255,255,.2);
  margin: 16px;
}
</style>
