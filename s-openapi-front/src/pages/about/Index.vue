<template>
    <div style="height: calc(100vh - 200px);display: flex;align-items: center;justify-content: center;">
        <div class="card">
            <div class="container">
                <div class="cloud front">
                    <span class="left-front"></span>
                    <span class="right-front"></span>
                </div>
                <span class="sun sunshine"></span>
                <span class="sun"></span>
                <div class="cloud back">
                    <span class="left-back"></span>
                    <span class="right-back"></span>
                </div>
            </div>

            <div class="card-header">
                <span>Hi，{{ user.username }} {{ currentTime }}，欢迎加入API开放平台</span>
                <span>{{ user.createTime }}</span>
            </div>

            <span class="temp">
                第 {{ registerDay }} 天
            </span>

            <div class="temp-scale">
                <el-button link @click="handlerEdit">修改信息</el-button>
            </div>
        </div>
    </div>


    <!-- 新增弹框 -->
    <div>
        <el-dialog @close="handleConsole" style="border-radius: 5px;padding: 0" v-model="isDialog" align-center
            :show-close="false" :width="400">
            <!-- 头部 -->
            <template #header>
                <div class="dialog-header">
                    <span>修改个人信息</span>
                </div>
            </template>
            <!-- 自定义内容 -->
            <div style="padding: 0 25px 0 25px;">
                <el-form label-position="left" ref="formRef" :rules="rules" label-width="auto" status-icon
                    :model="user">
                    <el-form-item prop="username">
                        <el-input size="large" style="width: 100%" v-model="user.username" placeholder="用户名" />
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input show-password size="large" placeholder="密码" style="width: 100%" type="password"
                            v-model="user.password" />
                    </el-form-item>
                    <el-form-item prop="phone">
                        <el-input size="large" placeholder="手机号" style="width: 100%" v-model="user.phone" />
                    </el-form-item>
                    <el-form-item prop="email">
                        <el-input size="large" placeholder="邮箱" style="width: 100%" v-model="user.email" />
                    </el-form-item>
                    <el-form-item prop="gender">
                        <el-radio-group v-model="user.gender">
                            <el-radio :value="1">男</el-radio>
                            <el-radio :value="0">女</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
            </div>

            <!-- 底部 -->
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="handleConsole">取消</el-button>
                    <el-button :loading="saveLoading" type="primary" @click="updateUser"> 确认 </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { queryEchoUserInfo, updateUserInfo } from "@/api/user";
import useUserStore from "@/stores/models/user/user.js";
import { ElMessage } from "element-plus";

const userStore = useUserStore();
const user = ref({});
const isDialog = ref(false);
const registerDay = ref(1);
const currentTime = ref('');

onMounted(() => {
    getUserInfo();
    getTimeOfDayGreeting();
})

const getUserInfo = async () => {
    await queryEchoUserInfo(userStore.userId).then((res) => {
        if (res.code === 200) {
            user.value = res.data;
            registerDay.value = calculateDaysSinceRegistration(user.value.registerDate);
            if (registerDay.value === 0) {
                registerDay.value = 1;
            }
        }
    })
}

const handleConsole = () => {
    isDialog.value = false;
};

//查询用户回显数据
const handlerEdit = () => {
    isDialog.value = true;
}

//保存新增用户
const saveLoading = ref(false);
const updateUser = () => {
    formRef.value.validate(async (valid) => {
        if (valid) {
            saveLoading.value = true;
            updateUserInfo(user.value).then((res) => {
                if (res.code === 200) {
                    saveLoading.value = false;
                    ElMessage({
                        message: '修改成功！',
                        type: 'success'
                    });
                    isDialog.value = false;
                } else {
                    saveLoading.value = false;
                }
            });
        }
    });
};

function calculateDaysSinceRegistration(registrationDate) {
    // 将注册日期字符串转换为Date对象
    const regDate = new Date(registrationDate);

    // 获取当前日期
    const currentDate = new Date();

    // 计算两个日期之间的毫秒数差
    const differenceInMilliseconds = currentDate - regDate;

    // 将毫秒数差转换为天数并返回
    return Math.floor(differenceInMilliseconds / (1000 * 60 * 60 * 24));
}

function getTimeOfDayGreeting() {
    const currentHour = new Date().getHours();
    if (currentHour >= 5 && currentHour < 12) {
        currentTime.value = "早上好";
    } else if (currentHour >= 12 && currentHour < 17) {
        currentTime.value = "下午好";
    } else {
        currentTime.value = "晚上好";
    }
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
.card {
    width: 40%;
    height: 235px;
    position: relative;
    padding: 25px;
    background: radial-gradient(178.94% 106.41% at 26.42% 106.41%, #FFF7B1 0%, rgba(255, 255, 255, 0) 71.88%)
        , #FFFFFF;
    box-shadow: 0px 155px 62px rgba(0, 0, 0, 0.01), 0px 87px 52px rgba(0, 0, 0, 0.05), 0px 39px 39px rgba(0, 0, 0, 0.09), 0px 10px 21px rgba(0, 0, 0, 0.1), 0px 0px 0px rgba(0, 0, 0, 0.1);
    border-radius: 23px;
    transition: all 0.8s cubic-bezier(0.15, 0.83, 0.66, 1);
    cursor: pointer;
}

.card:hover {
    transform: scale(1.05);
}

.container {
    width: 250px;
    height: 250px;
    position: absolute;
    right: -35px;
    top: -50px;
    display: flex;
    align-items: center;
    justify-content: center;
    transform: scale(0.7);
}

.cloud {
    width: 250px;
}

.front {
    padding-top: 45px;
    margin-left: 25px;
    display: inline;
    position: absolute;
    z-index: 11;
    animation: clouds 8s infinite;
    animation-timing-function: ease-in-out;
}

.back {
    margin-top: -30px;
    margin-left: 150px;
    z-index: 12;
    animation: clouds 12s infinite;
    animation-timing-function: ease-in-out;
}

.right-front {
    width: 45px;
    height: 45px;
    border-radius: 50% 50% 50% 0%;
    background-color: #4c9beb;
    display: inline-block;
    margin-left: -25px;
    z-index: 5;
}

.left-front {
    width: 65px;
    height: 65px;
    border-radius: 50% 50% 0% 50%;
    background-color: #4c9beb;
    display: inline-block;
    z-index: 5;
}

.right-back {
    width: 50px;
    height: 50px;
    border-radius: 50% 50% 50% 0%;
    background-color: #4c9beb;
    display: inline-block;
    margin-left: -20px;
    z-index: 5;
}

.left-back {
    width: 30px;
    height: 30px;
    border-radius: 50% 50% 0% 50%;
    background-color: #4c9beb;
    display: inline-block;
    z-index: 5;
}

.sun {
    width: 120px;
    height: 120px;
    background: -webkit-linear-gradient(to right, #fcbb04, #fffc00);
    background: linear-gradient(to right, #fcbb04, #fffc00);
    border-radius: 60px;
    display: inline;
    position: absolute;
}

.sunshine {
    animation: sunshines 2s infinite;
}

@keyframes sunshines {
    0% {
        transform: scale(1);
        opacity: 0.6;
    }

    100% {
        transform: scale(1.4);
        opacity: 0;
    }
}

@keyframes clouds {
    0% {
        transform: translateX(15px);
    }

    50% {
        transform: translateX(0px);
    }

    100% {
        transform: translateX(15px);
    }
}

.card-header {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.card-header span:first-child {
    word-break: break-all;
    font-weight: 800;
    font-size: 15px;
    line-height: 135%;
    color: rgba(87, 77, 51, 0.66);
}

.card-header span:last-child {
    font-weight: 700;
    font-size: 15px;
    line-height: 135%;
    color: rgba(87, 77, 51, 0.33);
}

.temp {
    position: absolute;
    left: 25px;
    bottom: 12px;
    font-weight: 700;
    font-size: 64px;
    line-height: 77px;
    color: rgba(87, 77, 51, 1);
}

.temp-scale {
    width: 80px;
    height: 36px;
    position: absolute;
    right: 25px;
    bottom: 25px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(0, 0, 0, 0.06);
    border-radius: 9px;
}

.temp-scale button {
    font-weight: 700;
    font-size: 13px;
    line-height: 134.49%;
    color: rgba(87, 77, 51, 0.66);
}

.dialog-header {
    padding: 10px;
    margin-bottom: 20px;
    height: 40px;
    width: 100%;
    background-color: #409EFF;
    border-radius: 5px 5px 0 0;
    color: #fff;
    text-align: center;
    font-weight: bold;
    font-size: 17px;
}

.dialog-footer {
    height: 40px;
    width: 100%;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    padding: 0 25px 25px 25px;
}
</style>