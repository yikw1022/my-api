// 定义名为 useRefreshStore 的 store
import { defineStore } from "pinia";
import { ref } from "vue";

export const useStatusStore = defineStore("status", () => {
  // 使用 ref 创建名为 tip 的响应式变量，并初始化为 true
  const tip = ref(true);
  // 返回包含 tip 变量的对象
  return {
    tip,
  };
});
