<template>
  <el-main :style="{ background: themeStore.isDark ? '' : 'rgb(245,245,245)' }">
    <div style="height: 32.2px;width: 100%;">
      <RouterTags />
    </div>
    <div style="padding: 20px">
      <router-view v-slot="{ Component }">
        <transition name="fade">
          <component :is="Component" v-if="flag" />
        </transition>
      </router-view>
    </div>
    <div class="footer">
      <span>©2023-2024 转载内容版权归作者及来源网站所有，本站原创内容转载请注明来源，联系：2073958890@qq.com</span>
    </div>
  </el-main>
</template>

<script setup>
import { ref, watch, nextTick } from "vue";
import { useRefreshStore } from "@/stores/models/refresh/refresh.js";
import { useThemeStore } from "@/stores/models/theme/theme.js";
import RouterTags from "@/components/common/tags/Index.vue";

const themeStore = useThemeStore();
const useRefresh = useRefreshStore();
//控制当前组件是否销毁重建
const flag = ref(true);

//监听pina中存储的refresh是否改变，若改变则说明刷新
watch(
  () => useRefresh.refresh,
  () => {
    //点击刷新按钮，路由组件销毁
    flag.value = false;
    //紧接着又重新创建组件，相当于重新发送请求
    nextTick(() => {
      flag.value = true;
    });
  }
);
</script>


<style scoped lang="scss">
.el-main {
  width: auto;
  padding: 0;
}

.fade-enter-active {
  transition: opacity 0.5s, transform 0.5s;
}

.fade-enter-from {
  opacity: 0;
  transform: scale(0.9);
}

.fade-enter-to {
  opacity: 1;
  transform: scale(1);
}

.footer{
  text-align: center;
  font-size: 12px;
  color: #A8ABB2;
  margin: 20px 0 20px 0;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}
</style>