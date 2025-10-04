<template>
  <el-card shadow="never" style="width: 100%;">
    <div class="qweather-tips">
      <el-icon class="icon_style">
        <InfoFilled />
      </el-icon>
      <p>
        提供最近24小时逐小时/最近30天逐天/最近6个月逐月的数据统计，如需更多统计结果，请自行记录。
      </p>
    </div>

    <el-radio-group v-model="tabPosition" @change="handlerTab" style="margin: 30px 0 30px 0">
      <el-radio-button value="hour">24小时</el-radio-button>
      <el-radio-button value="day">30天</el-radio-button>
      <el-radio-button value="month">6个月</el-radio-button>
    </el-radio-group>

    <HourEchart v-if="hourFlag" />
    <DayEchart v-if="dayFlag" />
    <MonthEchart v-if="monthFlag" />
  </el-card>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";
import HourEchart from "@/pages/interface/traffic/hourechart/Index.vue";
import DayEchart from "@/pages/interface/traffic/dayechart/Index.vue";
import MonthEchart from "@/pages/interface/traffic/monthechart/Index.vue";

const tabPosition = ref("hour");
//控制当前组件是否销毁重建
const hourFlag = ref(true);
const dayFlag = ref(false);
const monthFlag = ref(false);

//切换数据操作
const handlerTab = (name) => {
  //点击刷新按钮，路由组件销毁
  hourFlag.value = false;
  dayFlag.value = false;
  monthFlag.value = false;
  //紧接着又重新创建组件，相当于重新发送请求
  switch (name) {
    case "hour":
      //获取24小时数据
      console.log("获取24小时数据");
      nextTick(() => {
        hourFlag.value = true;
        dayFlag.value = false;
        monthFlag.value = false;
      });
      break;
    case "day":
      //获取24小时数据
      console.log("获取30天数据");
      nextTick(() => {
        dayFlag.value = true;
        hourFlag.value = false;
        monthFlag.value = false;
      });
      break;
    case "month":
      //获取24小时数据
      console.log("获取6个月数据");
      nextTick(() => {
        monthFlag.value = true;
        dayFlag.value = false;
        hourFlag.value = false;
      });
      break;
  }
};
</script>
<style scoped lang='scss'>

.qweather-tips {
  background-color: #f0fcff;
  border: 1px solid #abf0ff;
}

p {
  font-weight: 400;
  font-size: 14px;
  line-height: 20px;
  color: #333;
}

.icon_style {
  font-size: 20px;
  line-height: 20px;
  color: #333;
  margin-right: 10px;
}

.qweather-tips {
  display: flex;
  -webkit-box-align: start;
  -ms-flex-align: start;
  align-items: flex-start;
  padding: 10px 15px;
  border-radius: 10px;
}
</style>