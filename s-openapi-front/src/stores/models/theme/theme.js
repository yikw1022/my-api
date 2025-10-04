import { defineStore } from "pinia";
import { ref } from "vue";

export const useThemeStore = defineStore("theme", () => {
  //判断是否切换主题，主要用于弹出抽屉
  const theme = ref(false);
  //侧边栏背景默认偏黑色
  const bgColor = ref("linear-gradient(to right, #141e30, #243b55)");
  //侧边栏菜单背景默认偏黑色
  const menuBgColor = ref("#545c64");
  //字体颜色默认白色
  const fontColor = ref("#fff");
  //判断是否是暗黑模式，默认不是暗黑模式
  const isDark = ref(false);

  //修改侧边栏主题色：简约白 / 商务黑
  const changeSideTheme = (val) => {
    if (val === "light") {
      bgColor.value = "#fff";
      fontColor.value = "#000000";
    } else {
      bgColor.value = "#191a23";
      fontColor.value = "#fff";
    }
  };

  //设置默认主题样式
  const setDefaultTheme = () => {
    //侧边栏背景默认偏黑色
    bgColor.value = "linear-gradient(to right, #141e30, #243b55)";
    //侧边栏菜单背景默认偏黑色
    menuBgColor.value = "#545c64";
    //字体颜色默认白色
    fontColor.value = "#fff";
  }

  return {
    theme,
    bgColor,
    fontColor,
    changeSideTheme,
    isDark,
    menuBgColor,
    setDefaultTheme,
  };
});