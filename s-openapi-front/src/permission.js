//路由鉴权文件
import router from "./router";
//引入nprogress进度条
import nprogress from "nprogress";
//引入全局配置
import setting from "./setting.js";
//引入nprogress样式
import "nprogress/nprogress.css";
import useUserStore from "@/stores/models/user/user.js";
nprogress.configure({ showSpinner: false });

//全局前置路由
router.beforeEach(async (to, from, next) => {
  //进度条开始动
  nprogress.start();
  //获取token判断用户是否登录
  const userStore = useUserStore();
  let username = userStore.username;
  let token = userStore.token;
  if (token) {
    //用户已登录
    if (to.path === "/login") {
      next();
    } else {
      if (username) {
        next();
      } else {
        try {
          await userStore.userInfo();
          next({ ...to });
        } catch (error) {
          userStore.logout();
          next({ path: "/login", query: { redirect: to.path } });
        }
      }
    }
  } else {
    //用户未登录
    if (to.path === "/login") {
      next();
    } else if (to.path === "/register") {
      next();
    } else {
      next({ path: "/login" });
    }
  }
});

//全局后置路由
router.afterEach((to, from) => {
  //切换浏览器标题
  document.title = `${setting.title} - ${to.meta.title}`;
  //进度条结束
  nprogress.done();
});
