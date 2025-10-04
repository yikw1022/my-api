<template>
  <div class="admin-container">
    <!-- 数据展示 -->
    <div style="margin-bottom: 30px;">
      <el-row :gutter="15">
        <el-col :span="6">
          <div class="admin-card">
            <div class="admin-card-body">
              <div style="margin-right: 35px">
                <el-space :size="25" direction="vertical">
                  <div style="display: flex; align-items: center">
                    <el-icon :size="20" style="margin-right: 8px">
                      <UserFilled />
                    </el-icon>
                    <h3 style="font-size: 18px">注册用户</h3>
                  </div>
                  <count-to style="font-weight: 600; font-size: 35px; color: #73819e" :start-val="0"
                    :end-val="userTotal"></count-to>
                </el-space>
              </div>
              <div style="height: 110px; display: flex; align-items: center">
                <h3>
                  <span style="color: #73767a;">昨日新增：</span>
                  <count-to style="font-weight: bold; color: #67c23a" :start-val="0"
                    :end-val="yesterdayUser"></count-to>
                </h3>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="admin-card">
            <div class="admin-card-body">
              <div style="margin-right: 35px">
                <el-space :size="25" direction="vertical">
                  <div style="display: flex; align-items: center">
                    <el-icon :size="20" style="margin-right: 8px">
                      <HelpFilled />
                    </el-icon>
                    <h3 style="font-size: 18px">创建应用</h3>
                  </div>
                  <count-to style="font-weight: 600; font-size: 35px; color: #73819e" :start-val="0"
                    :end-val="appTotal"></count-to>
                </el-space>
              </div>
              <div style="height: 110px; display: flex; align-items: center">
                <h3 style="color: #303133">
                  <span style="color: #73767a;">昨日新增：</span>
                  <count-to style="font-weight: bold; color: #67c23a" :start-val="0" :end-val="yesterdayApp"></count-to>
                </h3>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="admin-card">
            <div class="admin-card-body">
              <div style="margin-right: 35px">
                <el-space :size="25" direction="vertical">
                  <div style="display: flex; align-items: center">
                    <el-icon :size="20" style="margin-right: 8px">
                      <Promotion />
                    </el-icon>
                    <h3 style="font-size: 18px">接口管理</h3>
                  </div>
                  <count-to style="font-weight: 600; font-size: 35px; color: #73819e" :start-val="0"
                    :end-val="interfaceTotal"></count-to>
                </el-space>
              </div>
              <div style="height: 110px; display: flex; align-items: center">
                <h3 style="color: #303133">
                  <span style="color: #73767a;">昨日新增：</span>
                  <count-to style="font-weight: bold; color: #67c23a" :start-val="0"
                    :end-val="yesterdayInterface"></count-to>
                </h3>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="admin-card">
            <div class="admin-card-body">
              <div style="margin-right: 35px">
                <el-space :size="25" direction="vertical">
                  <div style="display: flex; align-items: center">
                    <el-icon :size="20" style="margin-right: 8px">
                      <Menu />
                    </el-icon>
                    <h3 style="font-size: 18px">订单管理</h3>
                  </div>
                  <count-to style="font-weight: 600; font-size: 35px; color: #73819e" :start-val="0"
                    :end-val="orderTotal"></count-to>
                </el-space>
              </div>
              <div style="height: 110px; display: flex; align-items: center">
                <h3 style="color: #303133">
                  <span style="color: #73767a;">昨日新增：</span>
                  <count-to style="font-weight: bold; color: #67c23a" :start-val="0"
                    :end-val="yesterdayOrder"></count-to>
                </h3>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="echarts-card" style="width: 99%">
      <!-- Top 5热点API -->
      <div>
        <Echart />
      </div>

      <el-divider></el-divider>

      <!-- Top 10活跃用户 -->
      <div>
        <TopTenUser />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { CountTo } from 'vue3-count-to';
import Echart from "@/pages/home/admin/echart/Index.vue";
import TopTenUser from "@/pages/home/admin/userechart/Index.vue";
import { findRegisterUserNum } from "@/api/user";
import { findApplications } from "@/api/application";
import { findInterfaces } from "@/api/interfaceinfo";
import { findOrders } from "@/api/order";

//总用户数
const userTotal = ref(0);
//昨日新增用户
const yesterdayUser = ref(0);
//应用总数
const appTotal = ref(0);
//昨日新增应用数
const yesterdayApp = ref(0);
//接口总数
const interfaceTotal = ref(0);
//昨日新增接口数
const yesterdayInterface = ref(0);
//订单总数
const orderTotal = ref(0);
//昨日新增订单数
const yesterdayOrder = ref(0);

onMounted(() => {
  getUserTotal();
  getApplicationTotal();
  getInterfaceTotal();
  getOrderTotal();
})

//获取用户数据
const getUserTotal = () => {
  findRegisterUserNum().then((res) => {
    if (res.code === 200) {
      userTotal.value = res.data.total;
      yesterdayUser.value = res.data.yesterday;
    }
  })
}

//获取应用数据
const getApplicationTotal = () => {
  findApplications().then((res) => {
    if (res.code === 200) {
      appTotal.value = res.data.total;
      yesterdayApp.value = res.data.yesterday;
    }
  })
}

//获取接口数据
const getInterfaceTotal = () => {
  findInterfaces().then((res) => {
    if (res.code === 200) {
      interfaceTotal.value = res.data.total;
      yesterdayInterface.value = res.data.yesterday;
    }
  })
}

//获取订单数据
const getOrderTotal = () => {
  findOrders().then((res) => {
    if (res.code === 200) {
      orderTotal.value = res.data.total;
      yesterdayOrder.value = res.data.yesterday;
    }
  })
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
  height: 140px;
}

.admin-card-body {
  padding: 15px;
  display: flex;
}

.el-row {
  width: 100%;
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