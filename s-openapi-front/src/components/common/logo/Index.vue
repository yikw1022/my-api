<template>
  <div class="logo" v-if="!setting.logoHidden" :style="{ background: '#252b48'}">
    <el-space :size="10">
      <svg-icon class="icon" name="logo" width="30px" height="30px"></svg-icon>
      <el-button link class="logo_title" @click="handlerToIndex">{{
        setting.title
        }}</el-button>
    </el-space>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
//引入全局设置
import router from "@/router/index.js";
import setting from "@/setting.js";

//跳往首页
const handlerToIndex = () => {
  router.push({ path: "/login" });
}
</script>


<style scoped lang="scss">
.logo {
  // 和侧边栏宽度保持一致
  width: $base-menu-side-width;
  display: flex;
  justify-content: center;
  align-items: center;
  height: $base-menu-logo-height;

  // 左侧菜单收缩样式
  &.collapse {
    width: $base-menu-min-side-width;
  }

  .logo_title {
    font-family: "微软雅黑体";
    font-size: $base-logo-title-size;
    color: #fff
  }
}
</style>