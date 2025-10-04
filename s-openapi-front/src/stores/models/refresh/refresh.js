// 定义名为 useRefreshStore 的 store
import { defineStore } from "pinia";
import { ref } from "vue";

export const useRefreshStore = defineStore("refresh", () => {
  // 使用 ref 创建名为 refresh 的响应式变量，并初始化为 false
  const refresh = ref(false);
  // 返回包含 refresh 变量的对象
  return {
    refresh
  };
});