// 导入 pinia 库中的 defineStore 方法
import { defineStore } from "pinia";
// 导入 vue 库中的 ref 方法
import { ref } from "vue";

// 定义名为 useCollapseStore 的 store
export const useCollapseStore = defineStore("collapse", () => {
  // 创建一个名为 collapse 的响应式数据，并初始化为 false
  const collapse = ref(false);

  // 定义名为 setCollapse 的方法，用于设置 collapse 的值
  const setCollapse = (newCollapse) => {
    collapse.value = newCollapse;
  };

  // 返回 collapse 和 setCollapse 方法
  return {
    collapse,
    setCollapse,
  };
});