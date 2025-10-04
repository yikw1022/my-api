<template>
  <el-drawer size="15%" v-model="themeStore.theme" title="主题设置">
    <template #default>
      <el-space direction="vertical" :size="50">
        <!-- 主题颜色 -->
        <div>
          <el-space :size="50">
            <span>主题颜色</span>
            <el-color-picker
              v-model="color"
              show-alpha
              :predefine="predefineColors"
              @change="changeThemeColor"
            />
          </el-space>
        </div>

        <!-- 暗黑模式 -->
        <div>
          <el-space :size="50">
            <span>暗黑模式</span>
            <el-switch
              v-model="dark"
              class="mt-2"
              inline-prompt
              :active-icon="MoonNight"
              :inactive-icon="Sunrise"
              @change="change"
            />
          </el-space>
        </div>

        <!-- 侧边栏主题设置 -->
        <div>
          <el-space :size="10">
            <el-radio-group @change="changeSideTheme" v-model="radio">
              <el-radio value="light" size="large">
                <template #default>
                  <el-image
                    style="width: 40px; height: 40px"
                    src="https://s2.loli.net/2024/04/20/scWZaDBK8OgzrTp.png"
                    fit="contain"
                  />
                  <div>
                    <span>简约白</span>
                  </div>
                </template>
              </el-radio>
              <el-radio value="dark" size="large">
                <template #default>
                  <el-image
                    style="width: 40px; height: 40px"
                    src="https://s2.loli.net/2024/04/20/jbiGB59u3SURsvA.png"
                    fit="contain"
                  />
                  <div>
                    <span>商务黑</span>
                  </div>
                </template>
              </el-radio>
            </el-radio-group>
          </el-space>
        </div>

        <!-- 回复默认主题设置 -->
        <div style="height: 300px; display: flex; align-items: flex-end">
          <el-button @click="setDefault" type="primary">设置默认主题</el-button>
        </div>
      </el-space>
    </template>
  </el-drawer>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useThemeStore } from "@/stores/models/theme/theme.js";
import { Sunrise, MoonNight } from "@element-plus/icons-vue";

const themeStore = useThemeStore();
const color = ref("rgba(255, 69, 0, 0.68)");
const predefineColors = ref([
  "#ff4500",
  "#ff8c00",
  "#ffd700",
  "#90ee90",
  "#00ced1",
  "#1e90ff",
  "#c71585",
  "rgba(255, 69, 0, 0.68)",
  "rgb(255, 120, 0)",
  "hsv(51, 100, 98)",
  "hsva(120, 40, 94, 0.5)",
  "hsl(181, 100%, 37%)",
  "hsla(209, 100%, 56%, 0.73)",
  "#c7158577",
]);

const dark = ref(false);

//暗黑模式切换
const change = () => {
  //获取html根节点
  let html = document.documentElement;
  if (dark.value) {
    themeStore.isDark = true;
  } else {
    themeStore.isDark = false;
  }
  //判断html是否有类名dark
  dark.value ? (html.className = "dark") : (html.className = "");
};

//修改主题颜色
const changeThemeColor = () => {
  //通知js修改根节点的样式对象的属性与属性值
  let html = document.documentElement;
  themeStore.menuBgColor = color.value;
  html.style.setProperty("--el-color-primary", color.value);
};

//侧边栏主题修改
const radio = ref("");
const changeSideTheme = (val) => {
  themeStore.changeSideTheme(val);
};

//设置默认主题
const setDefault = () => {
  themeStore.setDefaultTheme();
}
</script>

<style scoped lang="scss">
span {
  font-size: 13px;
}
</style>
