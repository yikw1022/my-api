<template>
    <el-container>
        <el-header>
            <div class="header-container">
                <h3 style="display: flex;align-items: center;">
                    <svg style="margin-right: 8px;" t="1715343021813" class="icon" viewBox="0 0 1024 1024" version="1.1"
                        xmlns="http://www.w3.org/2000/svg" p-id="9826" width="28" height="28">
                        <path
                            d="M82.672 315.36C71.472 311.648 64 300.528 64 289.392s7.472-22.256 18.672-29.68l369.6-181.792C489.6 59.36 534.4 59.36 568 77.92l369.6 181.792c14.928 7.424 22.4 18.56 22.4 29.68 0 11.136-7.472 22.256-18.672 29.68l-369.6 181.792c-37.328 18.56-82.128 18.56-115.728 0L82.672 315.36z m858.656 163.248c11.2 3.712 18.672 14.848 18.672 29.68 0 11.136-7.472 22.256-18.672 29.68l-369.6 185.504c-37.328 18.56-82.128 18.56-115.728 0L86.4 537.968c-14.928-7.424-22.4-18.56-22.4-29.68 0-11.136 7.472-22.256 18.672-29.68l93.328-40.8 276.272 140.96c37.328 18.56 82.128 18.56 115.728 0l280-140.96 93.328 40.8z m0 222.608c11.2 3.712 18.672 14.848 18.672 29.68 0 11.136-7.472 22.256-18.672 29.68l-369.6 185.504c-37.328 18.56-82.128 18.56-115.728 0L86.4 760.576c-14.928-7.424-22.4-18.56-22.4-29.68 0-11.12 7.472-22.256 18.672-29.68l93.328-40.8 276.272 140.976c37.328 18.56 82.128 18.56 115.728 0L848 660.4l93.328 40.816z"
                            fill="#1296db" p-id="9827"></path>
                    </svg>
                    API开放平台
                </h3>
                <div style="margin-right: 240px;">
                    <el-button style="color: #c45656" @click="handlerToLogin" link>已有账号？登录</el-button>
                </div>
            </div>
        </el-header>
        <el-scrollbar height="calc(100vh - 60px)">
            <el-main>
                <div class="main-container">
                    <h1>注册成为开发者</h1>
                    <div class="step-container">
                        <el-card shadow="never" style="width: 60%">
                            <div class="step-content">
                                <el-form ref="formRef" style="width: 50%;padding: 20px;" :model="user" :rules="rules"
                                    label-width="auto" status-icon>
                                    <el-form-item prop="username">
                                        <el-input size="large" style="width: 100%" v-model="user.username"
                                            placeholder="用户名" />
                                    </el-form-item>
                                    <el-form-item prop="password">
                                        <el-input show-password size="large" placeholder="密码" style="width: 100%"
                                            type="password" v-model="user.password" />
                                    </el-form-item>
                                    <el-form-item prop="phone">
                                        <el-input size="large" placeholder="手机号" style="width: 100%"
                                            v-model="user.phone" />
                                    </el-form-item>
                                    <el-form-item prop="email">
                                        <el-input size="large" placeholder="邮箱" style="width: 100%"
                                            v-model="user.email" />
                                    </el-form-item>
                                    <el-form-item prop="code">
                                        <el-input @keyup.enter="handleRegister" size="large" v-model="user.code" style="width: 100%"
                                            placeholder="邮箱验证码">
                                            <template #append>
                                                <el-button @click="getCode" :disabled="disabled">{{ content
                                                    }}</el-button>
                                            </template>
                                        </el-input>
                                    </el-form-item>
                                    <el-form-item prop="gender">
                                        <el-radio-group v-model="user.gender">
                                            <el-radio value="1">男</el-radio>
                                            <el-radio value="0">女</el-radio>
                                        </el-radio-group>
                                    </el-form-item>
                                    <el-form-item>
                                        <el-button @click="handleRegister" :loading="loading" color="#626aef"
                                            size="large" style="width: 100%">立即注册</el-button>
                                    </el-form-item>
                                </el-form>
                            </div>
                        </el-card>
                    </div>
                </div>
            </el-main>
        </el-scrollbar>
    </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { doRegister,sendMs } from "@/api/user";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

const user = ref({
    username: '',
    password: '',
    phone: '',
    email: '',
    gender: null,
    code: ''
});
const loading = ref(false);


//获取验证码前确认是否是真实手机号
const disabled = ref(false);
const content = ref("获取验证码");
function getCode() {
    //请求发送短信
    if (user.value.email !== "") {
        sendMs(user.value.email).then((res) => {
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

//注册
const handleRegister = () => {
    formRef.value.validate(async (valid) => {
        if (valid) {
            loading.value = true;
            doRegister(user.value).then((res) => {
                if (res.code === 200) {
                    loading.value = false;
                    ElMessage({
                        message: '注册成功！',
                        type: 'success'
                    });
                    router.push('/login');
                } else {
                    loading.value = false;
                }
            });
        }
    });
}

//已有账号
const handlerToLogin = () => {
    router.push('/login');
}

//表单验证
const formRef = ref();
const rules = ref({
    phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { min: 11, max: 11, message: '长度为11位', trigger: 'blur' },
    ],
    username: [
        {
            required: true,
            message: '请输入用户名',
            trigger: 'change',
        },
    ],
    password: [
        {
            required: true,
            message: '请输入密码',
            trigger: 'change',
        },
    ],
    email: [
        {
            required: true,
            message: '请输入邮箱',
            trigger: 'change',
        },
    ],
    code: [
        {
            required: true,
            message: '请输入邮箱验证码',
            trigger: 'change',
        },
    ],
    gender: [
        {
            required: true,
            message: '请选择性别',
            trigger: 'change',
        },
    ]
})

</script>


<style scoped lang='scss'>
.el-header {
    backdrop-filter: blur(8px);
    background: rgb(255, 255, 255, 0.2);
    overflow: hidden;
    position: relative;
}

.el-main {
    background-color: #eee;
    height: calc(100vh - 60px);

    .main-container {
        .step-container {
            display: flex;
            width: 100%;
            justify-content: center;
            align-items: center;

            .step-content {
                padding: 20px;
                width: 100%;
                display: flex;
                justify-content: center;
                align-items: center;
            }
        }
    }

    h1 {
        text-align: center;
        margin: 20px 0 40px 0;
    }
}

:deep(.el-button) {
    border-radius: 50px;
}

.header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px;

    h3 {
        font-family: '华文行楷';
        font-weight: bold;
        font-size: 24px;
    }
}
</style>