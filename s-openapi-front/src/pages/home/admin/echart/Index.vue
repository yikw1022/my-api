<template>
  <el-empty v-if="isEmpty" description="API热点数据为空" />
  <div v-else id="interfaceEchart" style="width: 100%; height: 380px"></div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import echarts from "@/echart";
import { queryTopFive } from "@/api/interfaceinfo";

const isEmpty = ref(false);
const switchColor = ref(false);
const legendData = ref([]);
const seriesData = ref([]);

onMounted(() => {
  findTopFiveData();
});

//获取Top 5数据
const findTopFiveData = () => {
  queryTopFive().then((res) => {
    if (res.code === 200 && res.data !== null) {
      legendData.value = res.data.legend;
      seriesData.value = res.data.series;
      isEmpty.value = false;
      initInterfaceEchart();
    } else {
      isEmpty.value = true;
    }
  })
}

//动态切换背景颜色
function switchColorMethod() {
  initInterfaceEchart();
}

// 用户数据展示
function initInterfaceEchart() {
  var myChart = echarts.init(document.getElementById("interfaceEchart"));
  var option = {
    title: {
      text: 'Top 5热点API',
      left: 'left',
      textStyle: {
        fontSize: 18,
        color: '#606266'
      }
    },
    tooltip: {
      trigger: 'item'
    },
    toolbox: {
      feature: {
        myTool1: {
          show: true,
          title: "主题",
          icon: "path://M432.45,595.444c0,2.177-4.661,6.82-11.305,6.82c-6.475,0-11.306-4.567-11.306-6.82s4.852-6.812,11.306-6.812C427.841,588.632,432.452,593.191,432.45,595.444L432.45,595.444z M421.155,589.876c-3.009,0-5.448,2.495-5.448,5.572s2.439,5.572,5.448,5.572c3.01,0,5.449-2.495,5.449-5.572C426.604,592.371,424.165,589.876,421.155,589.876L421.155,589.876z M421.146,591.891c-1.916,0-3.47,1.589-3.47,3.549c0,1.959,1.554,3.548,3.47,3.548s3.469-1.589,3.469-3.548C424.614,593.479,423.062,591.891,421.146,591.891L421.146,591.891zM421.146,591.891",
          onclick: function () {
            switchColor.value = !switchColor.value;
            initInterfaceEchart();
          },
        },
        saveAsImage: {
          show: true,
          title: '保存为图片',
        }
      },
    },
    legend: {
      type: 'scroll',
      left: 'left',
      orient: 'vertical',
      right: 10,
      top: 40,
      data: legendData.value
    },
    series: [
      {
        name: 'API调用次数',
        type: 'pie',
        radius: '55%',
        center: ['50%', '50%'],
        data: seriesData.value,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
  if (switchColor.value) {
    myChart.dispose();
    //黑色模式
    myChart = echarts.init(document.getElementById("interfaceEchart"), "dark");
    myChart.setOption(option);
    //随着屏幕大小调节图表
    window.addEventListener("resize", () => {
      myChart.resize();
    });
  } else {
    myChart.dispose();
    //白色模式
    myChart = echarts.init(document.getElementById("interfaceEchart"));
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
