<template>
  <div class="admin-container">
    <!-- 数据展示 -->
    <div>
      <div style="margin-bottom: 20px">
        <el-row :gutter="15">
          <el-col :span="16">
            <div class="admin-card">
              <div class="admin-card-body">
                <div class="first-card">
                  <div>
                    <el-space :size="18" direction="vertical">
                      <div>
                        <span style="font-size: 13px;">已支付订单</span>
                      </div>
                      <div class="order" @click="toOderInfo">
                        <el-icon :size="30" color="rgb(61, 110, 255)">
                          <WalletFilled />
                        </el-icon>
                      </div>
                      <el-button @click="toOderInfo" type="primary" link style="font-size: 13px;">查看订单</el-button>
                    </el-space>
                  </div>

                  <div>
                    <el-space :size="18" direction="vertical">
                      <div>
                        <span style="font-size: 13px;">待处理工单</span>
                      </div>
                      <div class="order">
                        <el-icon :size="30" color="rgb(61, 110, 255)">
                          <List />
                        </el-icon>
                      </div>
                      <el-button type="primary" style="font-size: 13px;" link>处理工单</el-button>
                    </el-space>
                  </div>

                  <div>
                    <el-space :size="18" direction="vertical">
                      <div>
                        <span style="font-size: 13px;">未读消息</span>
                      </div>
                      <div class="order">
                        <el-icon :size="30" color="rgb(61, 110, 255)">
                          <Comment />
                        </el-icon>
                      </div>
                      <el-button type="primary" style="font-size: 13px;" link>查看消息</el-button>
                    </el-space>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="admin-card">
              <div class="admin-card-body">
                <div class="account">
                  <div style="text-align: left;">
                    <p>用户名：</p>
                    <p>性别：</p>
                    <p>绑定手机：</p>
                    <p>绑定邮箱：</p>
                  </div>

                  <div style="text-align: right;">
                    <p>{{ userInfo.username }}</p>
                    <p>{{ userInfo.gender === 1 ? '男' : '女' }}</p>
                    <p>{{ userInfo.phone }}</p>
                    <p>{{ userInfo.email }}</p>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <div>
        <el-row :gutter="15">
          <el-col :span="16">
            <div class="admin-card">
              <div class="admin-card-body">
                <div class="first-card">
                  <div>
                    <el-space :size="18" direction="vertical">
                      <div>
                        <span style="font-size: 13px">已创建应用数量</span>
                      </div>
                      <div>
                        <span style="font-weight: 600; font-size: 40px; color: #73819e">{{ applications }}</span>
                        <span style="color: #73819e;margin-left: 8px;">个</span>
                      </div>
                      <el-button @click="toAppKey" type="primary" link style="font-size: 13px">管理{{ applicationKeys
                        }}个key</el-button>
                    </el-space>
                  </div>

                  <div>
                    <el-space :size="18" direction="vertical">
                      <div>
                        <span style="font-size: 13px;">今日接口调用量</span>
                      </div>
                      <div>
                        <span style="font-weight: 600; font-size: 40px; color: #73819e">{{ todayInvoke }}</span>
                        <span style="color: #73819e;margin-left: 8px;">次</span>
                      </div>
                      <el-button @click="toInterfaceList" type="primary" style="font-size: 13px;" link>查看接口</el-button>
                    </el-space>
                  </div>

                  <div>
                    <el-space :size="18" direction="vertical">
                      <div>
                        <span style="font-size: 13px;">接口调用总量</span>
                      </div>
                      <div>
                        <span style="font-weight: 600; font-size: 40px; color: #73819e">{{ totalInvoke
                          }}</span>
                        <span style="color: #73819e;margin-left: 8px;">次</span>
                      </div>
                      <el-button @click="toInterfaceList" type="primary" style="font-size: 13px;" link>查看接口</el-button>
                    </el-space>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="admin-card">
              <div class="admin-card-body">
                <div>
                  <span style="font-size: 14px;font-weight: 600;color: rgba(0,0,0,.85);">快捷入口</span>
                  <div style="flex-wrap: wrap;display: flex;">
                    <div class="fast-utils" v-for="(item, index) in fastUtils" :key="index">
                      <el-button @click="toFastWhere(item.path)" style="font-size: 13px;" type="primary" link>{{
                        item.name }}</el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 接口热点图 -->
    <div class="echarts-card" style="width: 99%;margin-top: 20px">
      <Echart />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import router from "@/router";
import useUsterStore from "@/stores/models/user/user.js";
import Echart from "@/pages/home/normal/echart/Index.vue";
import { WalletFilled } from "@element-plus/icons-vue";
import { queryHomeEchoUserInfo } from "@/api/user";
import { queryApplicationsByUserId } from "@/api/application";
import { queryApplicationKeyByUserId } from "@/api/applicationkey";
import { findUserInterfaces } from "@/api/userinterfaceinfo";

const fastUtils = ref([{
  name: '我的应用',
  path: '/appmanage/application'
}, {
  name: '消息管理',
  path: '/appmanage/message'
}, {
  name: '接口列表',
  path: '/interface/list'
}, {
  name: '流量详情',
  path: '/interface/traffic'
}, {
  name: '额度充值',
  path: '/lines/order'
}, {
  name: '订单列表',
  path: '/lines/orderList'
}]);
const userStore = useUsterStore();
//当前用户创建的应用总数
const applications = ref(0);
//当前用户管理的应用key总数
const applicationKeys = ref(0);
//当前用户今日调用接口数
const todayInvoke = ref(0);
//当前用户总调用接口数
const totalInvoke = ref(0);
//用户信息
const userInfo = ref({});

onMounted(() => {
  getUserApplications();
  getUserApplicationKeys();
  getUserInvokeInterfaceNumber();
  getUserInfo();
})

//获取当前用户所创建的应用总数
const getUserApplications = () => {
  queryApplicationsByUserId(userStore.userId).then((res) => {
    if (res.code === 200) {
      applications.value = res.data;
    }
  })
}

//获取当前用户所管理的应用Key总数
const getUserApplicationKeys = () => {
  queryApplicationKeyByUserId(userStore.userId).then((res) => {
    if (res.code === 200) {
      applicationKeys.value = res.data;
    }
  })
}

//获取当前用户今日调用接口数
const getUserInvokeInterfaceNumber = () => {
  findUserInterfaces(userStore.userId).then((res) => {
    if (res.code === 200 && res.data !== null) {
      todayInvoke.value = res.data.today;
      totalInvoke.value = res.data.total;
    }
  })
}

//获取用户信息
const getUserInfo = () => {
  queryHomeEchoUserInfo(userStore.userId).then((res) => {
    if (res.code === 200) {
      userInfo.value = res.data;
    }
  })
}

//跳往订单信息页面
const toOderInfo = () => {
  router.push("/lines/orderList")
}

const toAppKey = () => {
  router.push("/appmanage/application");
}

const toInterfaceList = () => {
  router.push("/interface/traffic");
}

const toFastWhere = (path) => {
  router.push(path)
}
</script>



<style scoped lang='scss'>
.admin-container {
  width: 100%;
  padding: 10px;
}

.admin-card {
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  margin: 0;
  padding: 0;
  color: rgba(0, 0, 0, 0.85);
  font-size: 14px;
  font-variant: tabular-nums;
  line-height: 1.5;
  list-style: none;
  font-feature-settings: "tnum", "tnum";
  position: relative;
  background: #fff;
  border-radius: 2px;
  transition: all 0.3s;
  width: 100%;
  height: 190px;
}

.admin-card-body {
  padding: 30px;
  display: flex;

  .account {
    width: 100%;
    display: flex;
    justify-content: space-between;

    p {
      margin-bottom: 18px;
      font-size: 13px;
    }
  }
}

.el-row {
  width: 100%;
}

.order {
  height: 60px;
  width: 60px;
  border-radius: 50%;
  background-color: #ecf5ff;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: background-color 0.5s;
}

.order:hover {
  background-color: rgb(61, 110, 255);
}

.order:hover .el-icon {
  color: #fff;
}

.first-card {
  display: flex;
  justify-content: space-between;
  width: 100%
}

.fast-utils {
  margin: 12px 15px 0 15px;
  height: 28px;
  width: 120px;
  background-color: #ecf5ff;
  border-radius: 5px;
  transition: background-color 0.5s;
  display: flex;
  justify-content: center;
  align-items: center;
}

.fast-utils:hover {
  background-color: rgb(61, 110, 255);
}

.fast-utils:hover .el-button {
  color: #fff;
}

.echarts-card {
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  margin: 0;
  padding: 0;
  color: rgba(0, 0, 0, 0.85);
  font-size: 14px;
  font-variant: tabular-nums;
  line-height: 1.5;
  list-style: none;
  font-feature-settings: "tnum", "tnum";
  position: relative;
  background: #fff;
  border-radius: 2px;
  transition: all 0.3s;
  width: 99%;
}
</style>