<template>
  <div>
    <div class="top">
      <!-- 左侧 -->
      <Left />
      <!-- 右侧 -->
      <Right />
    </div>
    <!-- <el-divider></el-divider> -->
    <!-- <RouterTags /> -->
    <!-- 主题设置弹出框 -->
    <Drawer />
  </div>
</template>

<script setup>
import { ref } from "vue";
import RouterTags from "@/components/common/tags/Index.vue";
import Left from "@/components/header/left/Index.vue";
import Right from "@/components/header/right/Index.vue";
import Drawer from "@/components/header/drawer/Index.vue";
</script>


<style scoped lang="scss">
.top {
  height: $base-top-header-height;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>