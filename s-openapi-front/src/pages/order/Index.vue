<template>
  <el-card shadow="never" style="width: 100%">
    <template #header>
      <div class="card-header">
        <span>可用额度</span>
      </div>
    </template>
    <div>
      <el-table
        :header-cell-style="headerStyle"
        :cell-style="cellStyle"
        :data="tableData"
        height="250"
        v-loading="loading"
        element-loading-text="加载中"
        style="width: 100%; padding: 10px"
      >
        <el-table-column fixed prop="keyName" label="Key名称" width="120" />
        <el-table-column prop="line" label="剩余额度" width="120">
          <template #default="scope">
            <el-tag
              :color="scope.row.line < 10 ? '#F56C6C' : '#409EFF'"
              effect="dark"
            >
              {{ scope.row.line }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="accessKey" label="AccessKey" width="400">
          <template #default="scope">
            <el-input
              style="width: 300px"
              v-model="scope.row.accessKey"
              show-password
              type="password"
              >{{ scope.row.accessKey }}</el-input
            >
          </template>
        </el-table-column>
        <el-table-column prop="secretKey" label="SecretKey" width="400">
          <template #default="scope">
            <el-input
              style="width: 300px"
              v-model="scope.row.secretKey"
              show-password
              type="password"
              >{{ scope.row.secretKey }}</el-input
            >
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="选项" width="120">
          <template #default="scope">
            <el-button
              v-has="`btn:plan:buy`"
              :icon="CircleCheck"
              type="success"
              size="small"
              @click="buyLines(scope.row)"
              >购买额度</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-card>

  <!-- 购买额度抽屉 -->
  <el-drawer
    :show-close="false"
    :style="{ '--el-drawer-padding-primary': '0' }"
    @close="handleDrawerClose"
    size="48%"
    v-model="isDrawer"
  >
    <!-- 自定义头部 -->
    <template #header>
      <div class="drawer-header">
        <span>购买额度</span>
      </div>
    </template>

    <div style="padding: 20px; background-color: #eee">
      <el-card shadow="never">
        <template #header>
          <span style="color: #909399; font-size: 12px">当前可用额度</span>
          <div class="card-header">
            <h2 style="margin-top: 15px">{{ line }}</h2>
          </div>
        </template>

        <!-- 选择套餐 -->
        <div>
          <div style="margin-bottom: 20px">
            <span style="color: #909399; font-size: 12px">规格</span>
          </div>
          <el-radio-group v-model="planRadio">
            <el-radio
              style="margin-bottom: 15px"
              :value="item.price"
              size="large"
              border
              v-for="(item, index) in planData"
              :key="index"
              @click="handleRadio(item)"
            >
              <div style="display: flex">
                <svg
                  t="1714874060711"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="1427"
                  width="20"
                  height="20"
                >
                  <path
                    d="M808.99 79.466h-97.602l-199.993 354.22-197.59-354.22h-98.795l243.374 432.526h-207.238v68.674h218.078v90.374h-218.078v69.869h218.078v203.623h84.34v-203.623h220.482v-69.869h-220.482v-90.374h220.482v-68.674h-209.642z"
                    fill="#d81e06"
                    p-id="1428"
                  ></path>
                </svg>
                <h2 style="font-weight: bold; color: #d81e06">
                  {{ item.price }}
                </h2>
                <span
                  style="color: #909399; font-size: 12px; margin-top: 10px"
                  >{{ item.unit }}</span
                >
              </div>
            </el-radio>
          </el-radio-group>
        </div>

        <template #footer>
          <span style="color: #909399; font-size: 12px">支付方式</span>
          <div class="dialog-footer" style="margin-top: 15px">
            <el-radio-group v-model="payType">
              <el-radio value="aliPay" size="large" border>
                <svg
                  t="1714875785961"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="1814"
                  width="30"
                  height="30"
                >
                  <path
                    d="M1024.0512 701.0304V196.864A196.9664 196.9664 0 0 0 827.136 0H196.864A196.9664 196.9664 0 0 0 0 196.864v630.272A196.9152 196.9152 0 0 0 196.864 1024h630.272a197.12 197.12 0 0 0 193.8432-162.0992c-52.224-22.6304-278.528-120.32-396.4416-176.64-89.7024 108.6976-183.7056 173.9264-325.3248 173.9264s-236.1856-87.2448-224.8192-194.048c7.4752-70.0416 55.552-184.576 264.2944-164.9664 110.08 10.3424 160.4096 30.8736 250.1632 60.5184 23.1936-42.5984 42.496-89.4464 57.1392-139.264H248.064v-39.424h196.9152V311.1424H204.8V267.776h240.128V165.632s2.1504-15.9744 19.8144-15.9744h98.4576V267.776h256v43.4176h-256V381.952h208.8448a805.9904 805.9904 0 0 1-84.8384 212.6848c60.672 22.016 336.7936 106.3936 336.7936 106.3936zM283.5456 791.6032c-149.6576 0-173.312-94.464-165.376-133.9392 7.8336-39.3216 51.2-90.624 134.4-90.624 95.5904 0 181.248 24.4736 284.0576 74.5472-72.192 94.0032-160.9216 150.016-253.0816 150.016z"
                    fill="#009FE8"
                    p-id="1815"
                  ></path>
                </svg>
              </el-radio>
              <el-radio value="wxPay" size="large" border>
                <svg
                  t="1714875825043"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="1963"
                  width="30"
                  height="30"
                >
                  <path
                    d="M275.692308 278.449231A39.384615 39.384615 0 0 0 230.4 315.076923 39.384615 39.384615 0 0 0 275.692308 351.704615 34.658462 34.658462 0 0 0 310.744615 315.076923 34.264615 34.264615 0 0 0 275.692308 278.449231zM478.916923 351.704615a34.658462 34.658462 0 0 0 36.627692-36.627692 34.264615 34.264615 0 0 0-36.627692-36.627692A39.384615 39.384615 0 0 0 433.230769 315.076923a39.384615 39.384615 0 0 0 45.686154 36.627692z"
                    fill="#00C901"
                    p-id="1964"
                  ></path>
                  <path
                    d="M870.4 0H153.6A153.6 153.6 0 0 0 0 153.6v716.8A153.6 153.6 0 0 0 153.6 1024h716.8a153.6 153.6 0 0 0 153.6-153.6V153.6A153.6 153.6 0 0 0 870.4 0zM369.427692 651.421538a500.972308 500.972308 0 0 1-102.4-14.572307l-102.006154 51.2 29.144616-87.827693a239.458462 239.458462 0 0 1-118.153846-196.923076C78.769231 263.876923 208.738462 154.387692 369.427692 154.387692c143.753846 0 269.390769 87.433846 294.596923 205.193846a253.243077 253.243077 0 0 0-27.963077 0A239.458462 239.458462 0 0 0 387.544615 590.769231a217.796923 217.796923 0 0 0 9.058462 61.046154z m430.867693 102.4l22.055384 73.255385-78.769231-43.716923a367.064615 367.064615 0 0 1-87.827692 14.572308c-139.027692 0-248.516923-94.916923-248.516923-211.889231s109.489231-211.889231 248.516923-211.889231c131.150769 0 248.123077 95.310769 248.123077 211.889231a221.341538 221.341538 0 0 1-103.581538 167.778461z"
                    fill="#00C901"
                    p-id="1965"
                  ></path>
                  <path
                    d="M734.523077 490.338462a29.144615 29.144615 0 0 0 0 58.28923 33.476923 33.476923 0 0 0 36.627692-29.144615 33.476923 33.476923 0 0 0-36.627692-29.144615zM573.833846 490.338462a29.144615 29.144615 0 1 0 0 58.28923 33.083077 33.083077 0 0 0 36.627692-29.144615 33.476923 33.476923 0 0 0-36.627692-29.144615z"
                    fill="#00C901"
                    p-id="1966"
                  ></path>
                </svg>
              </el-radio>
            </el-radio-group>
          </div>
          <el-divider
            border-style="hidden"
            style="margin: 20px 0 20px 0"
          ></el-divider>
          <el-button
            :loading="payLoading"
            @click="handlePay"
            color="rgb(61, 110, 255)"
            >支付</el-button
          >
          <el-drawer
            v-model="innerDrawer"
            :title="payType === 'aliPay' ? '支付宝支付' : '微信支付'"
            size="22%"
            :append-to-body="true"
            :before-close="handleBeforeClose"
          >
            <div class="pay_url">
              <el-image
                style="width: 200px; height: 200px"
                :src="payUrl"
                fit="cover"
              />
            </div>
            <div class="tip_style">
              <el-icon color="#E6A23C" size="20">
                <WarningFilled />
              </el-icon>
              <span>支付完成后请点击任意黑色区域返回</span>
            </div>
          </el-drawer>
        </template>
      </el-card>
    </div>
  </el-drawer>

  <!-- 支付弹框 -->
  <el-dialog
    v-model="isDialog"
    title="Tips"
    width="400"
    @close="hanlerClose"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :show-close="false"
    style="padding: 30px"
  >
    <template #header>
      <div style="display: flex">
        <el-icon :size="20" style="margin-right: 10px">
          <InfoFilled />
        </el-icon>
        <h3 style="font-weight: bold">正在支付中...</h3>
      </div>
    </template>
    <div>
      <ul>
        <li style="margin-bottom: 15px">如已完成付款，请点击“我已付款”按钮</li>
        <li>如付款失败或需要重新选择付款方式，请点击“取消付款”按钮</li>
      </ul>
    </div>
    <div style="display: flex">
      <el-space :size="50">
        <el-button @click="havedPay" color="rgb(61, 110, 255)"
          >我已付款</el-button
        >
        <el-button @click="cancelPay" color="rgb(61, 110, 255)"
          >取消付款</el-button
        >
        <el-button @click="goToPay" color="rgb(61, 110, 255)"
          >立即付款</el-button
        >
      </el-space>
    </div>
  </el-dialog>

  <!-- 模拟支付提示弹框 -->
  <el-dialog
    v-model="testSaveOrderTipDialog"
    title="支付提示！"
    width="500"
    align-center
  >
    <span>这是模拟支付环境，不会产生真实费用~</span>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="testSaveOrderTipDialog = false">取消</el-button>
        <el-button type="primary" @click="handleTestSaveOrder">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { CircleCheck } from "@element-plus/icons-vue";
import useUserStore from "@/stores/models/user/user.js";
import { queryKeysByUserId } from "@/api/applicationkey";
import { queryPlanData } from "@/api/plan";
import { save, queryPay, queryOrderStatus,testSave } from "@/api/order";
import { ElMessage } from "element-plus";

const orderName = ref("");
const userStore = useUserStore();
const tableData = ref([]);
const isEmpty = ref(false);
const loading = ref(true);
const isDrawer = ref(false);
const testSaveOrderTipDialog = ref(false);
const innerDrawer = ref(false);
const planRadio = ref(null);
const payType = ref("");
const isDialog = ref(false);
const planData = ref([]);
//支付扫码弹框
const orderId = ref("");
//订单实体
const orderInfo = ref({
  userId: null,
  planId: null,
  amount: null,
  payType: "",
  accessKey: "",
});
//支付实体
const pay = ref({
  orderId: "",
  name: "额度套餐",
  price: null,
  paymentType: "",
});
//支付二维码
const payUrl = ref("");
const payLoading = ref(false);

onMounted(() => {
  getUserKeysData();
  getPlanData();
});

//根据用户id查询keys数据
const getUserKeysData = () => {
  queryKeysByUserId(userStore.userId).then((res) => {
    if (res.code === 200 && res.data !== null) {
      tableData.value = res.data;
      isEmpty.value = false;
      loading.value = false;
    } else {
      isEmpty.value = true;
      loading.value = false;
    }
  });
};

//获取套餐数据
const getPlanData = () => {
  queryPlanData(orderName.value).then((res) => {
    if (res.code === 200) {
      planData.value = res.data;
    }
  });
};

//抽屉关闭时的回调
const handleDrawerClose = () => {
  getUserKeysData();
  isDrawer.value = false;
  planRadio.value = ref(null);
  payType.value = ref("");
  setSomeValue();
};

//内层抽屉关闭前
const handleBeforeClose = () => {
  isDialog.value = true;
};

//购买额度抽屉
const line = ref(null);
const buyLines = (item) => {
  line.value = item.line;
  orderInfo.value.accessKey = item.accessKey;
  isDrawer.value = true;
};

//点击套餐时触发
const handleRadio = (item) => {
  orderInfo.value.planId = item.id;
  orderInfo.value.amount = item.price;
  pay.value.price = item.price;
};

//点击支付按钮
const handlePay = () => {
  testSaveOrderTipDialog.value = true;
  // payLoading.value = true;
  // orderInfo.value.userId = userStore.userId;
  // orderInfo.value.payType = payType.value;
  // pay.value.paymentType = payType.value;
  // //保存订单信息

  // handlerSaveOrderInfo(orderInfo.value);
};

//模拟保存订单数据
const handleTestSaveOrder = () => {
  testSaveOrderTipDialog.value = false;
  orderInfo.value.userId = userStore.userId;
  orderInfo.value.payType = payType.value;
  pay.value.paymentType = payType.value;
  //测试保存订单数据
  testSaveOrderInfo(orderInfo.value);
};


//保存订单数据
const handlerSaveOrderInfo = (item) => {
  save(item).then((res) => {
    if (res.code === 200) {
      orderId.value = res.data;
      if (orderId.value !== null) {
        //获取订单号
        pay.value.orderId = orderId.value;
        //发送真实支付请求
        handlerQueryPay(pay.value);
      }
    } else {
      payLoading.value = false;
    }
  });
};

/**
 *
 * 模拟保存订单接口，对接真实支付接口后删除这段代码
 * @param item 订单实体
 */
const testSaveOrderInfo = (item) => {
  testSave(item).then((res) => {
    if (res.code === 200) {
      //这是模拟支付请求接口，真实对接支付接口后，可以删除这段代码，解开以下真实支付请求代码段
      ElMessage({
        message: "支付成功!",
        type: "success",
      });
      setSomeValue();
    } else {
      payLoading.value = false;
    }
  });
};

//发送支付请求逻辑
const handlerQueryPay = (item) => {
  queryPay(item).then((res) => {
    if (res.code === 200) {
      payLoading.value = false;
      payUrl.value = res.data.payUrl;
      innerDrawer.value = true;
    }
  });
};

//取消付款
const cancelPay = () => {
  planRadio.value = null;
  payType.value = "";
  payLoading.value = false;
  isDialog.value = false;
  innerDrawer.value = false;
  pay.value = ref({});
  orderInfo.value = ref({});
  orderId.value = ref("");
  payUrl.value = ref("");
};

//我已付款
const havedPay = () => {
  queryOrderStatus(orderId.value).then((res) => {
    if (res.code === 200) {
      if (res.data) {
        ElMessage({
          message: "支付成功!",
          type: "success",
        });
        setSomeValue();
      } else {
        ElMessage({
          message: "支付失败!",
          type: "error",
        });
        setSomeValue();
      }
    }
  });
};

//立即付款
const goToPay = () => {
  isDialog.value = false;
};

const setSomeValue = () => {
  testSaveOrderTipDialog.value = false;
  payLoading.value = false;
  isDialog.value = false;
  innerDrawer.value = false;
  isDrawer.value = false;
  pay.value = ref({});
  orderInfo.value = ref({});
  orderId.value = ref("");
  payUrl.value = ref("");
};

const headerStyle = ref({
  background: "#f4f4f5 !important",
  color: "#606266",
  height: "42px",
});

const cellStyle = ref({
  color: "#606266",
  height: "38px",
});
</script>

<style scoped lang="scss">
:deep(.el-radio) {
  height: 100px;
  width: 190px;
  display: flex;
  align-items: center;
  justify-content: center;
}

:deep(.el-radio--large) {
  height: 80px;
  width: 160px;
}

ul {
  list-style: disc;
  padding: 0 0 0 20px;
  margin: 15px 0 24px 0;

  li {
    font-style: normal;
    font-weight: 400;
    font-size: 16px;
    line-height: 24px;
    color: #888;
  }
}

.pay_url {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 250px;
}

.tip_style {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100px;
}

.drawer-header {
  padding: 10px;
  height: 40px;
  width: 100%;
  background-color: #409eff;
  border-radius: 0 5px 0 0;
  color: #fff;
  text-align: center;
  font-weight: bold;
  font-size: 17px;
}
</style>
