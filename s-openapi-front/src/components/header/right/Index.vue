<template>
  <!-- 右侧 -->
  <div class="r-content">
    <el-space :size="12" style="margin-right: 15px;">
      <el-button link class="my-el-button" @click="doRefresh">刷新</el-button>
      <el-button link class="my-el-button" @click="changeScreen">全屏</el-button>
    </el-space>
    <el-dropdown>
      <el-space :size="12">
        <svg-icon :name="userStore.gender === 1 ? 'manavatar' : 'womanavatar'" width="30px" height="30px"></svg-icon>
        <span style="color: #fff">{{ userStore.username }}</span>
        <el-icon style="margin-right: 20px; color: #fff">
          <arrow-down />
        </el-icon>
      </el-space>

      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="handlerToAbout">个人中心</el-dropdown-item>
          <el-dropdown-item @click="doLogoOut">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRefreshStore } from "@/stores/models/refresh/refresh.js";
import { useBreadcrumbStore } from "@/stores/models/breadcrumb/breadcrumb.js";
import { useStatusStore } from "@/stores/models/status";
import screenfull from "screenfull";
import { doLogout } from "@/api/user";
import { ElMessage } from "element-plus";
import useUserStore from "@/stores/models/user/user.js";
import router from "@/router/index";

const statusStore = useStatusStore();
const userStore = useUserStore();
const useRefresh = useRefreshStore();
const breadcrumbStore = useBreadcrumbStore();

//刷新
const doRefresh = () => {
  useRefresh.refresh = !useRefresh.refresh;
};

//全屏
const changeScreen = () => {
  if (screenfull.isEnabled) {
    screenfull.toggle();
  } else {
    ElMessage({
      message: "您的浏览器不支持全屏！",
      type: "warning",
    });
  }
};

//退出登录
const doLogoOut = () => {
  doLogout(userStore.token).then((res) => {
    if (res.code === 200) {
      statusStore.tip = true;
      userStore.logout();
      breadcrumbStore.removeBreadcrumb();
      //退出登录成功提示
      ElMessage({
        message: "退出登录成功！",
        type: "success",
      });
      //跳转登录页
      router.push({ name: "Login" });
    }
  })
};

//个人中心
const about = ref({
  path: "/about",
  name: "My",
  meta: {
    title: "个人中心",
    hidden: false,
    icon: "House",
  },
})
const handlerToAbout = () => {
  breadcrumbStore.setBreadcrumb(about.value);
  router.push("/about")
}
</script>


<style scoped lang="scss">
.r-content {
  display: flex;
  align-items: center;
}

.font_common {
  font-family: "微软雅黑";
  font-size: 14px;
}

.my-el-button {
  margin-right: 15px;
  color: #fff;
  font-size: 13px
}
</style>