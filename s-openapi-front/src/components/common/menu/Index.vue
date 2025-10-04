<template>
  <el-menu class="el-menu-vertical" :default-active="activeMenu" text-color="block" active-text-color="#409EFF"
    :collapse="collapseStore.collapse" :collapse-transition="false">
    <!-- 一级菜单 -->
    <template v-for="(item, index) in noChildren">
      <el-menu-item :index="item.path" :key="index" @click="changeRouter(item)" class="el_menu_"
        v-if="!item.meta.hidden">
        <component class="icons" :is="item.icon" />
        <template #title>{{ item.meta.title }}</template>
      </el-menu-item>
    </template>

    <!-- 二级菜单 -->
    <template v-for="(item, index) in hasChildren()" :key="index">
      <template v-if="item.children && item.children.length === 1">
        <el-menu-item :index="item.children[0].path" :key="index" @click="changeRouter(item.children[0])"
          class="el_menu_" v-if="!item.children[0].meta.hidden">
          <component class="icons" :is="item.children[0].meta.icon" />
          <template #title>{{ item.children[0].meta.title }}</template>
        </el-menu-item>
      </template>

      <template v-else>
        <el-sub-menu :index="item.path">
          <template #title>
            <component class="icons" :is="item.meta.icon" />
            <span>{{ item.meta.title }}</span>
          </template>
          <el-menu-item-group>
            <el-menu-item :index="subItem.path" v-for="(subItem, index) in item.children" :key="index"
              @click="changeRouter(subItem)" class="el_menu_">
              <component class="icons" :is="subItem.meta.icon" />
              <span>{{ subItem.meta.title }}</span>
            </el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>
      </template>
    </template>
  </el-menu>
</template>

<script setup>
import { computed, ref, onMounted } from "vue";
import router from "@/router/index.js";
import { useCollapseStore } from "@/stores/models/collapse/collapse.js";
import { useBreadcrumbStore } from "@/stores/models/breadcrumb/breadcrumb.js";
import { useThemeStore } from "@/stores/models/theme/theme.js";
import { useRoute } from "vue-router";

const themeStore = useThemeStore();
const breadcrumbStore = useBreadcrumbStore();
//接收父组件传进来的路由数据
const props = defineProps({
  menuList: [],
});

//默认激活的菜单
const route = useRoute();
const activeMenu = ref("/home");
activeMenu.value = computed(() => {
  return route.path;
})

//控制侧边栏收缩和展开时的控制
const collapseStore = useCollapseStore();

//过滤出无子菜单的路由
const noChildren = () => {
  return props.menuList.filter((item) => !item.children);
};

//过滤出菜单路由，有子菜单的路由
const hasChildren = () => {
  return props.menuList.filter((item) => item.children);
};

//路由跳转
const changeRouter = (item) => {
  //点击菜单时路由跳转
  router.push(item.path);

  //点击菜单时获取头部面包屑路由展示数据
  breadcrumbStore.setBreadcrumb(item);
};
</script>

<script>
export default {
  name: "Menu",
};
</script>


<!-- 样式 -->
<style lang="scss" scoped>
.el-menu-vertical {
  border-right: none;
  height: 100vh;
}

:deep(.el-menu-item:hover) {
  background: #fff !important;
  color: #3370ff !important;
}

:deep(.el-submenu__title:hover) {
  background: #fff !important;
  color: #3370ff !important;
}

:deep(.el-menu-item.is-active) {
  background: #ecf5ff !important;
  color: #3370ff !important;
}

:deep(.el-submenu__title.is-active) {
  background: #ecf5ff !important;
  color: #3370ff !important;
}

.icons {
  width: 16px;
  height: 16px;
  margin-right: 10px;
}
</style>