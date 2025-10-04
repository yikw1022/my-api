<template>
  <!-- 管理员端 -->
  <Admin v-if="isAdmin" />

  <!-- 普通用户端 -->
  <Normal v-if="!isAdmin" />
</template>

<script setup>
import { ref } from "vue";
import useUserStore from "@/stores/models/user/user.js";
import { useStatusStore } from "@/stores/models/status";
import Admin from "@/pages/home/admin/Index.vue";
import Normal from "@/pages/home/normal/Index.vue";
import { ElNotification } from 'element-plus'

const statusStore = useStatusStore();
const userStore = useUserStore();
const isAdmin = userStore.isAdmin;
if (statusStore.tip){
  ElNotification({
    message: `欢迎${userStore.username}回来！`,
    type: 'success'
  })
  statusStore.tip = false;

  setTimeout(() => {
    ElNotification({
    message: '测试环境下无法进行删除操作！',
    type: 'warning',
  })
  }, 5000);
}
</script>

<style scoped lang="scss">
</style>
