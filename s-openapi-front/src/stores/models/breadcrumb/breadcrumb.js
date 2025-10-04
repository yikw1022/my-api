// 导入pinia库中的defineStore函数和vue库中的ref函数
import { defineStore } from "pinia";
import { ref } from "vue";

// 定义Breadcrumb模块的store
export const useBreadcrumbStore = defineStore("breadcrumb", () => {
  // 使用ref创建响应式的breadcrumb变量，并初始化为null
  const breadcrumb = ref(null);
  // 使用ref创建响应式的tagList数组，并初始化包含一个对象
  const tagList = ref([
    {
      path: "/home",
      name: "Home",
      meta: {
        title: "首页",
        hidden: false,
        icon: "Odometer",
      },
    },
  ]);

  //退出时清空数据
  const removeBreadcrumb = () => {
    breadcrumb.value = null;
    tagList.value = [
      {
        path: "/home",
        name: "Home",
        meta: {
          title: "首页",
          hidden: false,
          icon: "Odometer",
        },
      },
    ];
  }

  // 设置动态面包屑
  const setBreadcrumb = (newBreadcrumb) => {
    // 如果新面包屑的name为"Home"，则将breadcrumb置为null，否则赋值为newBreadcrumb
    if (newBreadcrumb.name === "Home") {
      breadcrumb.value = null;
    } else {
      breadcrumb.value = newBreadcrumb;
      // 去重逻辑，防止多个相同的面包屑添加进去
      let result = tagList.value.findIndex(
        (item) => item.name === newBreadcrumb.name
      );
      // 如果未找到相同的面包屑，则将newBreadcrumb添加到tagList中
      result === -1 ? tagList.value.push(newBreadcrumb) : "";
    }
  };

  // 清除标签
  const clearTags = (newTags) => {
    // 删除tagList中指定name的元素
    let result = tagList.value.findIndex((item) => item.name === newTags.name);
    // 根据索引位置删除指定元素
    tagList.value.splice(result,1);
  };

  return {
    // 返回tagList, breadcrumb, setBreadcrumb和clearTags供外部访问
    tagList,
    breadcrumb,
    setBreadcrumb,
    clearTags,
    removeBreadcrumb,
  };
});