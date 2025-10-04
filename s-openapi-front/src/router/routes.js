// 路由配置

//常量路由
export const constantRoutes = [
  {
    path: "/",
    name: "Layout",
    component: () => import("@/components/layout/Index.vue"),
    redirect: "/home",
    meta: {
      title: "",
      hidden: false,
      icon: "",
    },
    children: [
      {
        path: "/home",
        name: "Home",
        component: () => import("@/pages/home/Index.vue"),
        meta: {
          title: "首页",
          hidden: false,
          icon: "Odometer",
        },
      },
    ],
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/pages/login/Index.vue"),
    meta: {
      title: "登录",
      hidden: true,
      icon: "UploadFilled",
    },
  },
  {
    path: "/register",
    name: "Register",
    component: () => import("@/pages/register/Index.vue"),
    meta: {
      title: "注册",
      hidden: true,
      icon: "UploadFilled",
    },
  },
  {
    path: "/404",
    name: "404",
    component: () => import("@/pages/404/Index.vue"),
    meta: {
      title: "404",
      hidden: true,
      icon: "UploadFilled",
    },
  },
];

//异步路由(动态路由)
export const asyncRoutes = [
  {
    path: "/permission",
    name: "Permission",
    component: () => import("@/components/layout/Index.vue"),
    meta: {
      title: "权限管理",
      hidden: false,
      icon: "Lock",
    },
    redirect: "/permission/user",
    children: [
      {
        path: "/permission/user",
        name: "User",
        component: () => import("@/pages/permission/user/Index.vue"),
        meta: {
          title: "用户管理",
          hidden: false,
          icon: "User",
        },
      },
      {
        path: "/permission/role",
        name: "Role",
        component: () => import("@/pages/permission/role/Index.vue"),
        meta: {
          title: "角色管理",
          hidden: false,
          icon: "Connection",
        },
      },
      {
        path: "/permission/menu",
        name: "Menu",
        component: () => import("@/pages/permission/menu/Index.vue"),
        meta: {
          title: "菜单管理",
          hidden: false,
          icon: "Present",
        },
      },
    ],
  },
  {
    path: "/appmanage",
    name: "AppManage",
    component: () => import("@/components/layout/Index.vue"),
    meta: {
      title: "应用管理",
      hidden: false,
      icon: "Stopwatch",
    },
    redirect: "/system/application",
    children: [
      {
        path: "/appmanage/application",
        name: "Application",
        component: () => import("@/pages/appmanage/application/Index.vue"),
        meta: {
          title: "我的应用",
          hidden: false,
          icon: "DataAnalysis",
        },
      },
      {
        path: "/appmanage/message",
        name: "Message",
        component: () => import("@/pages/appmanage/message/Index.vue"),
        meta: {
          title: "消息管理",
          hidden: false,
          icon: "Message",
        },
      },
    ],
  },
  {
    path: "/interface",
    name: "Interface",
    component: () => import("@/components/layout/Index.vue"),
    meta: {
      title: "接口管理",
      hidden: false,
      icon: "Connection",
    },
    redirect: "/interface/list",
    children: [
      {
        path: "/interface/list",
        name: "List",
        component: () => import("@/pages/interface/list/Index.vue"),
        meta: {
          title: "接口列表",
          hidden: false,
          icon: "More",
        },
      },
      {
        path: "/interface/switch",
        name: "Switch",
        component: () => import("@/pages/interface/switch/Index.vue"),
        meta: {
          title: "接口开关",
          hidden: false,
          icon: "Switch",
        },
      },
      {
        path: "/interface/traffic",
        name: "Traffic",
        component: () => import("@/pages/interface/traffic/Index.vue"),
        meta: {
          title: "流量详情",
          hidden: false,
          icon: "Notebook",
        },
      },
    ],
  },
  {
    path: "/lines",
    name: "Lines",
    component: () => import("@/components/layout/Index.vue"),
    meta: {
      title: "配额管理",
      hidden: false,
      icon: "CreditCard",
    },
    redirect: "/lines/order",
    children: [
      {
        path: "/lines/order",
        name: "Order",
        component: () => import("@/pages/order/Index.vue"),
        meta: {
          title: "额度充值",
          hidden: false,
          icon: "Wallet",
        },
      },
      {
        path: "/lines/orderList",
        name: "OrderList",
        component: () => import("@/pages/order/list/Index.vue"),
        meta: {
          title: "订单列表",
          hidden: false,
          icon: "TakeawayBox",
        },
      },
      {
        path: "/lines/setting",
        name: "Setting",
        component: () => import("@/pages/order/setting/Index.vue"),
        meta: {
          title: "配额设置",
          hidden: false,
          icon: "Setting",
        },
      },
    ],
  },
  {
    path: "/",
    name: "About",
    component: () => import("@/components/layout/Index.vue"),
    redirect: "/about",
    meta: {
      title: "",
      hidden: false,
      icon: "",
    },
    children: [
      {
        path: "/about",
        name: "My",
        component: () => import("@/pages/about/Index.vue"),
        meta: {
          title: "个人中心",
          hidden: false,
          icon: "House",
        },
      },
    ],
  },
];

//任意路由
export const anyRoute = {
  path: "/:pathMatch(.*)*",
  redirect: "/404",
  name: "any",
  meta: {
    title: "任意路由",
    hidden: true,
    icon: "",
  },
};
