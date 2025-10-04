<!-- 侧边栏组件 -->
<template>
  <el-aside :class="{ collapse: collapseStore.collapse ? true : false }">
    <!-- 左侧 -->
    <div id="aside-collapse" :class="{ collapse: collapseStore.collapse ? true : false }">
      <div>
        <el-button link v-if="!collapseStore.collapse" class="icons" @click="changeCollapse" :icon="Fold"></el-button>
        <el-button link class="icons" v-if="collapseStore.collapse" @click="changeCollapse" :icon="Expand"></el-button>
      </div>
    </div>
    <el-scrollbar class="scrollbar">
      <Meun :menuList="userStore.menuRoutes" />
    </el-scrollbar>
  </el-aside>
</template>

<script setup>
import Logo from "@/components/common/logo/Index.vue";
import Meun from "@/components/common/menu/Index.vue";
// 导入折叠状态管理模块
import { useCollapseStore } from "@/stores/models/collapse/collapse.js";
// 导入常量路由配置
import useUserStore from "@/stores/models/user/user.js";
import { Fold, Expand } from "@element-plus/icons-vue";


//主要是获取常量路由数据传递给子组件（菜单组件Menu）
const userStore = useUserStore();
// 使用折叠状态管理模块
const collapseStore = useCollapseStore();
const changeCollapse = () => {
  collapseStore.collapse = !collapseStore.collapse;
};
</script>

<style scoped lang="scss">
.el-aside {
  background-color: #fff;
  width: $base-menu-side-width; // 设置侧边栏宽度
  height: calc(100vh - $base-menu-header-height); // 设置侧边栏高度

  .scrollbar {
    height: calc((calc(100vh - $base-menu-header-height)) - $base-menu-logo-height); // 设置滚动条容器高度
  }

  #aside-collapse {
    background-color: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
    height: $base-menu-logo-height;
  }

  // 左侧菜单收缩样式
  &.collapse {
    width: $base-menu-min-side-width; // 设置折叠状态下侧边栏宽度
  }
}

.el-menu-vertical {
  border-right: none; // 设置垂直菜单样式，去掉右边边框
}

.icons {
  color: #303133;
  font-size: 16px
}
</style>