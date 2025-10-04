<template>
  <el-card shadow="never" style="width: 100%">
    <el-tabs style="margin-top: 30px" @tab-change="handlerTabChange">
      <el-tab-pane label="全部订单">
        <el-table :cell-style="cellStyle" :header-cell-style="headerStyle" v-loading="allLoading"
          element-loading-text="加载中" :data="allOrderData" style="width: 100%">
          <el-table-column show-overflow-tooltip fixed prop="orderId" label="订单号" width="200" />
          <el-table-column prop="amount" label="订单金额" width="120" />
          <el-table-column prop="planType" label="套餐类型" width="120" />
          <el-table-column show-overflow-tooltip prop="accessKey" label="应用Key" width="200" />
          <el-table-column prop="payType" label="支付方式" width="120" />
          <el-table-column prop="status" label="订单状态" width="120">
            <template #default="scope">
              <el-tag effect="dark" :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ?
                "已支付" : "未支付" }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column prop="updateTime" label="支付时间" width="180" />
          <el-table-column fixed="right" width="240">
            <template #header>
              <el-input @change="handlerAllSearch" v-model="allOrderId" size="large" placeholder="请输入订单号" />
            </template>
            <template #default="scope">
              <el-button size="small" v-has="`btn:order:update`" type="warning" :icon="Edit"
                @click="handleEcho(scope.row.id)">编辑</el-button>
              <el-popconfirm title="确认删除该订单?" @confirm="handleDelete(scope.row.id)">
                <template #reference>
                  <el-button size="small" v-has="`btn:order:remove`" type="danger" :icon="Delete">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="已支付">
        <el-table :cell-style="cellStyle" :header-cell-style="headerStyle" v-loading="payLoading"
          element-loading-text="加载中" :data="havedPayOrderData" style="width: 100%">
          <el-table-column show-overflow-tooltip fixed prop="orderId" label="订单号" width="200" />
          <el-table-column prop="amount" label="订单金额" width="120" />
          <el-table-column prop="planType" label="套餐类型" width="120" />
          <el-table-column show-overflow-tooltip prop="accessKey" label="应用Key" width="200" />
          <el-table-column prop="payType" label="支付方式" width="120" />
          <el-table-column prop="status" label="订单状态" width="120">
            <template #default="scope">
              <el-tag effect="dark" :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ?
                "已支付" : "未支付" }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column prop="updateTime" label="支付时间" width="180" />
          <el-table-column fixed="right" width="240">
            <template #header>
              <el-input @change="handlerHavedPaySearch" v-model="havedPayOrderId" size="large" placeholder="请输入订单号" />
            </template>
            <template #default="scope">
              <el-button size="small" v-has="`btn:order:update`" type="warning" :icon="Edit"
                @click="handleEcho(scope.row.id)">编辑</el-button>
              <el-popconfirm title="确认删除该订单?" @confirm="handleDelete(scope.row.id)">
                <template #reference>
                  <el-button size="small" v-has="`btn:order:remove`" type="danger" :icon="Delete">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="未支付">
        <el-table :cell-style="cellStyle" :header-cell-style="headerStyle" v-loading="unPayLoading"
          element-loading-text="加载中" :data="unPayOrderData" style="width: 100%">
          <el-table-column show-overflow-tooltip fixed prop="orderId" label="订单号" width="200" />
          <el-table-column prop="amount" label="订单金额" width="120" />
          <el-table-column prop="planType" label="套餐类型" width="120" />
          <el-table-column show-overflow-tooltip prop="accessKey" label="应用Key" width="200" />
          <el-table-column prop="payType" label="支付方式" width="120" />
          <el-table-column prop="status" label="订单状态" width="120">
            <template #default="scope">
              <el-tag effect="dark" :type="scope.row.status === 1 ? 'success' : 'danger'">{{ scope.row.status === 1 ?
                "已支付" : "未支付" }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column prop="updateTime" label="支付时间" width="180" />
          <el-table-column fixed="right" width="240">
            <template #header>
              <el-input @change="handlerUnPaySearch" v-model="unpayOrderId" size="large" placeholder="请输入订单号" />
            </template>
            <template #default="scope">
              <el-button size="small" v-has="`btn:order:update`" type="warning" :icon="Edit"
                @click="handleEcho(scope.row.id)">编辑</el-button>
              <el-popconfirm title="确认删除该订单?" @confirm="handleDelete(scope.row.id)">
                <template #reference>
                  <el-button size="small" v-has="`btn:order:remove`" type="danger" :icon="Delete">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 分页组件 -->
    <div>

    </div>
  </el-card>

  <!-- 修改弹框 -->
  <div>
    <el-dialog @close="handleConsole" style="border-radius: 5px;padding: 0" v-model="isDialog" align-center
      :show-close="false" :width="400">
      <!-- 头部 -->
      <template #header>
        <div class="dialog-header">
          <span>修改订单</span>
        </div>
      </template>
      <!-- 自定义内容 -->
      <div style="padding: 0 25px 0 25px;">
        <el-form label-position="left" ref="formRef" :rules="rules" label-width="auto" status-icon :model="orderInfo">
          <el-form-item prop="orderId">
            <el-input size="large" style="width: 100%" v-model="orderInfo.orderId" placeholder="订单号" />
          </el-form-item>
          <el-form-item prop="amount">
            <el-input size="large" placeholder="订单金额" style="width: 100%" type="number" v-model="orderInfo.amount" />
          </el-form-item>
          <el-form-item prop="planType">
            <el-input disabled size="large" placeholder="套餐类型" style="width: 100%" v-model="orderInfo.planType" />
          </el-form-item>
          <el-form-item prop="accessKey">
            <el-input size="large" show-password type="password" placeholder="AccessKey" style="width: 100%"
              v-model="orderInfo.accessKey" />
          </el-form-item>
          <el-form-item prop="payType">
            <el-radio-group v-model="orderInfo.payType">
              <el-radio value="wxpay">微信</el-radio>
              <el-radio value="alipay">支付宝</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="status">
            <el-radio-group v-model="orderInfo.status">
              <el-radio :value="1">已支付</el-radio>
              <el-radio :value="0">未支付</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>

      <!-- 底部 -->
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleConsole">取消</el-button>
          <el-button :loading="saveLoading" type="primary" @click="updateOrder"> 确认 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { queryOrderData, deleteOrderById, echoOrderById, updateOrderInfo } from "@/api/order";
import useUserStore from "@/stores/models/user/user.js";
import { Edit, Delete } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";

const allLoading = ref(true);
const payLoading = ref(true);
const unPayLoading = ref(true);
const userStore = useUserStore();
const type = ref(0);
const allOrderData = ref([]);
const havedPayOrderData = ref([]);
const unPayOrderData = ref([]);
const orderInfo = ref({});
const isDialog = ref(false);
//订单号
const allOrderId = ref("");
const havedPayOrderId = ref("");
const unpayOrderId = ref("");

onMounted(() => {
  getAllOrderData();
});

//默认获取全部订单数据
const getAllOrderData = () => {
  allLoading.value = true;
  queryOrderData(userStore.userId, type.value, allOrderId.value).then((res) => {
    if (res.code === 200) {
      allOrderData.value = res.data;
      allLoading.value = false;
    }
  });
};

//根据订单号搜索订单
const handlerAllSearch = () => {
  getAllOrderData();
}

//根据订单号搜索订单
const handlerHavedPaySearch = () => {
  getHavedPayOrderData();
}

//根据订单号搜索订单
const handlerUnPaySearch = () => {
  getUnPayOrderData();
}

const getHavedPayOrderData = () => {
  payLoading.value = true;
  queryOrderData(userStore.userId, type.value, havedPayOrderId.value).then((res) => {
    if (res.code === 200) {
      havedPayOrderData.value = res.data;
      payLoading.value = false;
    }
  });
};

const getUnPayOrderData = () => {
  unPayLoading.value = true;
  queryOrderData(userStore.userId, type.value, unpayOrderId.value).then((res) => {
    if (res.code === 200) {
      unPayOrderData.value = res.data;
      unPayLoading.value = false;
    }
  });
};

//查询订单记录
const handlerTabChange = (item) => {
  if (item === '0') {
    type.value = item;
    getAllOrderData();
  } else if (item === '1') {
    type.value = item;
    getHavedPayOrderData();
  } else if (item === '2') {
    type.value = item;
    getUnPayOrderData();
  }
};

const handleConsole = () => {
  isDialog.value = false;
}

//回显订单数据
const handleEcho = (id) => {
  isDialog.value = true;
  echoOrderById(id).then((res) => {
    if (res.code === 200) {
      orderInfo.value = res.data;
      isDialog.value = true;
    }
  })
}

//修改订单数据
const updateOrder = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      updateOrderInfo(orderInfo.value).then((res) => {
        if (res.code === 200) {
          ElMessage({
            message: '修改成功！',
            type: 'success'
          });
          isDialog.value = false;
          getAllOrderData();
          getHavedPayOrderData();
          getUnPayOrderData();
        }
      })
    }
  })
}

//删除订单
const handleDelete = (id) => {
  deleteOrderById(id).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: '删除成功！',
        type: 'success'
      });
      getAllOrderData();
      getHavedPayOrderData();
      getUnPayOrderData();
    }
  })
}

//表单验证
const formRef = ref();
const rules = ref({
  orderId: [
    {
      required: true,
      message: '请输入订单号',
      trigger: 'change',
    },
  ],
  amount: [
    {
      required: true,
      message: '请输入订单金额',
      trigger: 'change',
    },
  ],
  accessKey: [
    {
      required: true,
      message: '请输入accessKey',
      trigger: 'change',
    },
  ],
  payType: [
    {
      required: true,
      message: '请选择支付方式',
      trigger: 'change',
    },
  ],
  status: [
    {
      required: true,
      message: '请选择订单状态',
      trigger: 'change',
    },
  ]
})

const headerStyle = ref({
  'background': '#f4f4f5 !important',
  'color': '#606266',
  'height': '42px'
})

const cellStyle = ref({
  'color': '#606266',
  'height': '38px'
})
</script>
<style scoped lang='scss'>
.dialog-footer {
  height: 40px;
  width: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 25px 25px 25px;
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
</style>