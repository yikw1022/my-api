<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <Header />
      </el-header>
      <el-container>
        <!-- 左侧菜单栏 -->
        <Aside />
        <Main />
      </el-container>
    </el-container>
  </div>
</template>


<script setup>
import Aside from "@/components/aside/Index.vue";
import Main from "@/components/main/Index.vue";
import Header from "@/components/header/Index.vue";
</script>


<style scoped lang="scss">
.common-layout {
  width: 100%;
  height: 100vh;

  .el-container {
    width: 100%;
    height: calc(100vh - $base-menu-header-height);

    .el-header {
      padding: 0;
      margin: 0;
      height: $base-menu-header-height;
      width: auto;
      background: $base-menu-header-background;
    }
  }
}

:deep(.el-divider--horizontal) {
  margin: 0;
}
</style>