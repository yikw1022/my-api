<template>
  <div id="MonthEchart" style="width: 100%; height: 380px"></div>
</template>

<script setup>
import { onMounted, ref, nextTick } from "vue";
import echarts from "@/echart";
import { querySixMonthTraffic } from "@/api/userinterfaceinfo";
import useUserStore from "@/stores/models/user/user.js";

const userStore = useUserStore();
const switchColor = ref(false);
//key数据
const appKeys = ref([]);
//最近7天日期
const createTimes = ref([]);
const series = ref([]);

onMounted(() => {
  getSixMonthTraffic();
});

//获取当前用户的应用key调用接口详情数据
const getSixMonthTraffic = () => {
  querySixMonthTraffic(userStore.userId).then((res) => {
    if (res.code === 200 && res.data !== null) {
      handlerResult(res);
      initMonthEchart();
    }else{
      initMonthEchart();
    }
  });
}

const handlerResult = (res) => {
  let result = res.data;
  createTimes.value = result[0].createTime;
  result.forEach((item) => {
    appKeys.value.push(item.keyName);
  });
  result.forEach((item) => {
    series.value.push({
      name: item.keyName,
      type: 'line',
      stack: 'Total',
      areaStyle: {},
      emphasis: {
        focus: 'series'
      },
      data: item.count
    });
  });
}

//动态切换背景颜色
function switchColorMethod() {
  initMonthEchart();
}

// 用户数据展示
function initMonthEchart() {
  var myChart = echarts.init(document.getElementById("MonthEchart"));
  var option = {
    title: {
      text: "6个月流量详情",
      textStyle: {
        fontSize: 14,
        color: '#606266'
      }
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "cross",
        label: {
          backgroundColor: "#3370ff",
        },
      },
    },
    legend: {
      data: appKeys.value
    },
    toolbox: {
      feature: {
        myTool1: {
          show: true,
          title: "主题",
          icon: "path://M432.45,595.444c0,2.177-4.661,6.82-11.305,6.82c-6.475,0-11.306-4.567-11.306-6.82s4.852-6.812,11.306-6.812C427.841,588.632,432.452,593.191,432.45,595.444L432.45,595.444z M421.155,589.876c-3.009,0-5.448,2.495-5.448,5.572s2.439,5.572,5.448,5.572c3.01,0,5.449-2.495,5.449-5.572C426.604,592.371,424.165,589.876,421.155,589.876L421.155,589.876z M421.146,591.891c-1.916,0-3.47,1.589-3.47,3.549c0,1.959,1.554,3.548,3.47,3.548s3.469-1.589,3.469-3.548C424.614,593.479,423.062,591.891,421.146,591.891L421.146,591.891zM421.146,591.891",
          onclick: function () {
            switchColor.value = !switchColor.value;
            initMonthEchart();
          },
        },
        saveAsImage: {
          show: true,
          title: '保存为图片',
        }
      },
    },
    grid: {
      left: "3%",
      right: "4%",
      bottom: "3%",
      containLabel: true,
    },
    xAxis: [
      {
        type: "category",
        boundaryGap: false,
        data: createTimes.value
      },
    ],
    yAxis: [
      {
        type: "value",
      },
    ],
    series: series.value
  };
  if (switchColor.value) {
    myChart.dispose();
    //黑色模式
    myChart = echarts.init(document.getElementById("MonthEchart"), "dark");
    myChart.setOption(option);
    //随着屏幕大小调节图表
    window.addEventListener("resize", () => {
      myChart.resize();
    });
  } else {
    myChart.dispose();
    //白色模式
    myChart = echarts.init(document.getElementById("MonthEchart"));
    myChart.setOption(option);
    //随着屏幕大小调节图表
    window.addEventListener("resize", () => {
      myChart.resize();
    });
  }
}
</script>

<style scoped>
</style>
