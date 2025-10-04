<template>
  <el-container>
    <el-header>
      <div class="header-container">
        <div style="display: flex;align-items: center;">
          <svg style="margin-right: 8px;" t="1715343021813" class="icon" viewBox="0 0 1024 1024" version="1.1"
            xmlns="http://www.w3.org/2000/svg" p-id="9826" width="28" height="28">
            <path
              d="M82.672 315.36C71.472 311.648 64 300.528 64 289.392s7.472-22.256 18.672-29.68l369.6-181.792C489.6 59.36 534.4 59.36 568 77.92l369.6 181.792c14.928 7.424 22.4 18.56 22.4 29.68 0 11.136-7.472 22.256-18.672 29.68l-369.6 181.792c-37.328 18.56-82.128 18.56-115.728 0L82.672 315.36z m858.656 163.248c11.2 3.712 18.672 14.848 18.672 29.68 0 11.136-7.472 22.256-18.672 29.68l-369.6 185.504c-37.328 18.56-82.128 18.56-115.728 0L86.4 537.968c-14.928-7.424-22.4-18.56-22.4-29.68 0-11.136 7.472-22.256 18.672-29.68l93.328-40.8 276.272 140.96c37.328 18.56 82.128 18.56 115.728 0l280-140.96 93.328 40.8z m0 222.608c11.2 3.712 18.672 14.848 18.672 29.68 0 11.136-7.472 22.256-18.672 29.68l-369.6 185.504c-37.328 18.56-82.128 18.56-115.728 0L86.4 760.576c-14.928-7.424-22.4-18.56-22.4-29.68 0-11.12 7.472-22.256 18.672-29.68l93.328-40.8 276.272 140.976c37.328 18.56 82.128 18.56 115.728 0L848 660.4l93.328 40.816z"
              fill="#1296db" p-id="9827"></path>
          </svg>
          <el-button link class="logo_title" @click="handlerToIndex">{{
            setting.title
            }}</el-button>
        </div>
        <div style="margin-right: 240px;display: flex;">
          <el-button style="margin-right: 12px" @click="handlerToDashbord" link>控制台</el-button>
          <div v-if="!userStore.token">
            <el-button link @click="handleLogin">登录</el-button>
            <el-button type="primary" @click="handleRegister">
              注册
              <el-icon style="margin-left: 5px;" :size="17">
                <ArrowRightBold />
              </el-icon>
            </el-button>
          </div>
          <el-dropdown v-if="userStore.token">
            <svg-icon :name="userStore.gender === 1 ? 'manavatar' : 'womanavatar'" width="30px"
              height="30px"></svg-icon>

            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="doLogoOut">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>
    <el-scrollbar height="calc(100vh - 60px)">
      <el-main>
        <div class="logo-container">
          <svg-icon name="logo" width="200px" height="230px" />
          <div class="log-text">
            {{ setting.title }}
          </div>
          <div class="logo-button">
            <button class="button" @click="handlerToDashbord">
              快速开始
              <svg-icon class="icon" name="startbutton" width="22px" height="22px" />
            </button>
          </div>
        </div>
        <waveBg />
      </el-main>
    </el-scrollbar>
  </el-container>

  <el-dialog width="350px" :style="{ '--el-dialog-padding-primary': '0px', 'border-radius': '40px' }"
    :show-close="false" align-center v-model="LoginDialog" @close="handleClose">
    <div class="card-container">
      <div>
        <h1 class="logo-title">
          {{ setting.title }}
        </h1>
      </div>
      <div class="card">
        <el-tabs v-model="activeName" style="width: 75%;">
          <el-tab-pane label="密码登录" name="passwordLogin">
            <el-form ref="loginForm" :model="user" :rules="rules">
              <el-form-item prop="username">
                <template #default>
                  <el-input placeholder="用户名" :prefix-icon="User" size="large" v-model="user.username"></el-input>
                </template>
              </el-form-item>
              <el-form-item prop="password">
                <el-input placeholder="密码" @keyup.enter="submitForm" :prefix-icon="Lock" size="large" type="password"
                  show-password v-model="user.password"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button :loading="loading" style="width: 100%; margin-top: 20px" size="large" type="primary"
                  color="#626aef" @click="submitForm">立即登录</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="邮箱登录" name="mailLogin">
            <el-form ref="emailForm" :model="userEmail" :rules="emailRules">
              <el-form-item prop="email">
                <template #default>
                  <el-input placeholder="邮箱" :prefix-icon="Eleme" size="large" v-model="userEmail.email"></el-input>
                </template>
              </el-form-item>
              <el-form-item prop="code">
                <el-input size="large" @keyup.enter="doEmailLogin" v-model="userEmail.code" style="width: 100%"
                  placeholder="邮箱验证码">
                  <template #append>
                    <el-button @click="getCode" :disabled="disabled">{{ content
                      }}</el-button>
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button :loading="loading" style="width: 100%; margin-top: 20px" size="large" type="primary"
                  color="#626aef" @click="doEmailLogin">立即登录</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ArrowRightBold, User, Lock, Eleme } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { doLogout, sendMs } from "@/api/user";
import useUserStore from "@/stores/models/user/user.js";
import { useBreadcrumbStore } from "@/stores/models/breadcrumb/breadcrumb.js";
import { useStatusStore } from "@/stores/models/status";
import router from "@/router/index.js";
import setting from "@/setting.js";
import waveBg from "@/pages/login/background/Index.vue";

const statusStore = useStatusStore();
const breadcrumbStore = useBreadcrumbStore();
const LoginDialog = ref(false);
const userStore = useUserStore();
const loading = ref(false);
const activeName = ref("passwordLogin");
const user = ref({
  username: "admin",
  password: "123456",
});

const userEmail = ref({
  email: "",
  code: "",
});

onMounted(() => {
})

//登录
const handleLogin = () => {
  LoginDialog.value = true;
}

//注册
const handleRegister = () => {
  router.push({ path: "/register" });
}

//关闭
const handleClose = () => {
  LoginDialog.value = false;
}

//密码登录提交
const submitForm = () => {
  loginForm.value.validate(async (valid) => {
    if (valid) {
      try {
        //进到这里说明已经登录成功
        loading.value = true;
        await userStore.login(user.value);
        ElMessage({
          message: "登录成功",
          type: "success",
        });
        loading.value = false;
        //浏览器刷新
        window.location.reload();
      } catch (error) {
        loading.value = false;
      }
    }
  });
};

//邮箱登录提交
const doEmailLogin = () => {
  emailForm.value.validate(async (valid) => {
    if (valid) {
      try {
        //进到这里说明已经登录成功
        loading.value = true;
        await userStore.emailLogin(userEmail.value);
        ElMessage({
          message: "登录成功",
          type: "success",
        });
        loading.value = false;
        //浏览器刷新
        window.location.reload();
      } catch (error) {
        loading.value = false;
      }
    }
  });
};

const handlerToIndex = () => {
  //浏览器刷新
  window.location.reload();
}

//跳往控制台
const handlerToDashbord = () => {
  if (userStore.token) {
    router.push({ path: "/" });
  } else {
    LoginDialog.value = true;
  }
}

//获取验证码前确认是否是真实手机号
const disabled = ref(false);
const content = ref("获取验证码");
function getCode() {
  //请求发送短信
  if (userEmail.value.email !== "") {
    sendMs(userEmail.value.email).then((res) => {
      if (res.code === 200) {
        ElMessage({
          type: 'success',
          message: '发送成功！'
        });
      }
    });
    var time = 180;
    //禁用获取验证码按钮
    disabled.value = true;
    // 开启定时器
    var timer = setInterval(function () {
      // 判断剩余秒数
      if (time == 0) {
        // 清除定时器和复原按钮
        clearInterval(timer);
        disabled.value = false;
        content.value = "获取验证码";
      } else {
        content.value = `${time}秒后重新获取`;
        time--;
      }
    }, 1000);
  } else {
    ElMessage({
      message: '请输入邮箱',
      type: 'error'
    });
  }
}

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

//表单验证
const loginForm = ref();
const emailForm = ref();
const rules = ref({
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
});
const emailRules = ref({
  email: [{ required: true, message: "请输入邮箱", trigger: "blur" }],
  code: [{ required: true, message: "请输入邮箱验证码", trigger: "blur" }],
});
</script>


<style scoped lang="scss">
.el-header {
  backdrop-filter: blur(8px);
  background: rgb(255, 255, 255, 0.2);
  overflow: hidden;
  position: relative;
}

.el-main {
  width: 100%;
  height: calc(100vh - 100px);

  .logo-container {
    top: 45%;
    left: 50%;
    transform: translate(-50%, -50%);
    position: absolute;
    z-index: 2;
    text-align: center;

    .log-text {
      margin-top: 25px;
      color: #1296db;
      font-size: 54px;
      font-weight: bolder;
      -webkit-animation: log-text 2s infinite;
    }

    @-webkit-keyframes log-text {
      0% {
        opacity: 1;
      }

      50% {
        opacity: 0.5;
      }

      100% {
        opacity: 1;
      }
    }
  }
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
}

:deep(.el-button) {
  border-radius: 50px;
}

.logo-title{
  font-family: '华文行楷';
    font-size: 40px;
    color: #000000;
    font-weight: bold;
    text-align: center;
    margin-bottom: 30px;
}

.card-container {
  background-image: linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%);
  height: 400px;
  width: 350px;
  border-radius: 40px;
  padding-top: 35px;
}

.card {
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo_title {
  font-family: "华文行楷";
  font-size: 24px;
  color: #000000;
  font-weight: bold;
}

.logo-button {
  height: 60px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 35px;
}

.button {
  position: relative;
  transition: all 0.3s ease-in-out;
  box-shadow: 0px 10px 20px rgba(0, 0, 0, 0.2);
  padding-block: 0.5rem;
  padding-inline: 1.25rem;
  background-color: rgb(0 107 179);
  border-radius: 9999px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #ffff;
  gap: 10px;
  font-weight: bold;
  border: 3px solid #ffffff4d;
  outline: none;
  overflow: hidden;
  font-size: 15px;
}

.icon {
  width: 24px;
  height: 24px;
  transition: all 0.3s ease-in-out;
}

.button:hover {
  transform: scale(1.05);
  border-color: #fff9;
}

.button:hover .icon {
  transform: translate(4px);
}

.button:hover::before {
  animation: shine 1.5s ease-out infinite;
}

.button::before {
  content: "";
  position: absolute;
  width: 100px;
  height: 100%;
  background-image: linear-gradient(120deg,
      rgba(255, 255, 255, 0) 30%,
      rgba(255, 255, 255, 0.8),
      rgba(255, 255, 255, 0) 70%);
  top: 0;
  left: -100px;
  opacity: 0.6;
}

@keyframes shine {
  0% {
    left: -100px;
  }

  60% {
    left: 100%;
  }

  to {
    left: 100%;
  }
}
</style>