<template>
  <el-card shadow="never" style="width: 100%">
    <!-- 头部搜索 -->
    <div style="
          width: 100%;
          display: flex;
          justify-content: space-between;
          align-items: center;
        ">
      <div style="width: 20%">
        <el-input size="large" v-model="orderName" class="search-input" placeholder="请输入套餐名称">
        </el-input>
      </div>
      <div>
        <el-space :size="20">
          <el-button type="primary" @click="handlerSearch">查询</el-button>
          <el-button type="info" @click="resetSearch">重置</el-button>
          <el-button v-has="`btn:plan:setting:add`" @click="handleAddPlan" type="success">新增</el-button>
        </el-space>
      </div>
    </div>

    <el-divider style="margin: 30px 0 30px 0" />

    <!-- 数据展示 -->
    <el-table :cell-style="cellStyle" :header-cell-style="headerStyle" style="width: 100%" :data="tableData"
      v-loading="loading" element-loading-text="加载中">
      <el-table-column fixed prop="name" label="名称" />
      <el-table-column prop="price" label="价格">
        <template #default="scope">
          <el-tag effect="dark" size="large" color="#fff">

            <h3 style="font-size: 16px; font-weight: bold; color: #d81e06">
              <svg t="1714890095530" class="icon" viewBox="0 0 1024 1024" version="1.1"
                xmlns="http://www.w3.org/2000/svg" p-id="1427" width="16" height="16">
                <path
                  d="M808.99 79.466h-97.602l-199.993 354.22-197.59-354.22h-98.795l243.374 432.526h-207.238v68.674h218.078v90.374h-218.078v69.869h218.078v203.623h84.34v-203.623h220.482v-69.869h-220.482v-90.374h220.482v-68.674h-209.642z"
                  fill="#d81e06" p-id="1428"></path>
              </svg>
              {{
                scope.row.price
              }}
            </h3>
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="unit" label="调用次数" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column fixed="right" label="选项">
        <template #default="scope">
          <el-button v-has="`btn:plan:setting:update`" type="warning" size="small" :icon="Edit"
            @click="handleEcho(scope.row.id)">编辑</el-button>
          <el-popconfirm title="确认删除该套餐?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button v-has="`btn:plan:setting:remove`" type="danger" size="small" :icon="Delete">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <!-- 新增弹框 -->
  <div>
    <el-dialog style="border-radius: 5px;padding: 0;" v-model="isDialog" align-center :show-close="false" :width="400">
      <!-- 头部 -->
      <template #header>
        <div class="dialog-header">
          <span v-text="plan.id === null ? '新增套餐' : '设置套餐'"></span>
        </div>
      </template>
      <div style="padding: 0 25px 0 25px;">
        <el-form label-width="auto" status-icon ref="formRef" :rules="rules" label-position="left" :model="plan">
          <el-form-item label="名称" prop="name">
            <el-input placeholder="请输入套餐名称" size="large" style="width: 100%" v-model="plan.name" autocomplete="off" />
          </el-form-item>
          <el-form-item label="价格" prop="price">
            <el-input placeholder="请输入套餐价格" size="large" style="width: 100%" v-model="plan.price" autocomplete="off" />
          </el-form-item>
          <el-form-item label="单位" prop="unit">
            <el-input size="large" style="width: 100%" placeholder="如: /100次" v-model="plan.unit" autocomplete="off" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="savePlan"> 确认 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { save, queryPlanData, remove, echo, edit } from "@/api/plan";
import { ElMessage } from "element-plus";
import { Edit, Delete } from "@element-plus/icons-vue";

const isDialog = ref(false);
const tableData = ref([]);
const loading = ref(true);
const plan = ref({
  id: null,
  name: '',
  price: null,
  unit: "",
});
//套餐名称
const orderName = ref('');

onMounted(() => {
  getPlanData();
});

//获取套餐数据
const getPlanData = () => {
  queryPlanData(orderName.value).then((res) => {
    if (res.code === 200 && res.data !== null) {
      tableData.value = res.data;
      loading.value = false;
    } else {
      loading.value = false;
    }
  });
};

//搜索
const handlerSearch = () => {
  if (orderName.value !== '') {
    getPlanData();
  }
}

//重置
const resetSearch = () => {
  orderName.value = "";
  getPlanData();
}

//新增套餐弹框
const handleAddPlan = () => {
  isDialog.value = true;
};

//取消保存套餐
const handleCancel = () => {
  isDialog.value = false;
  plan.value = ref({});
  plan.value.id = null;
};

//保存/修改套餐
const savePlan = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (plan.value.id === null) {
        save(plan.value).then((res) => {
          if (res.code === 200) {
            isDialog.value = false;
            plan.value.id = null;
            plan.value = ref({});
            ElMessage({
              message: "新增成功!",
              type: "success",
            });
            getPlanData();
          }
        });
      } else {
        edit(plan.value).then((res) => {
          if (res.code === 200) {
            isDialog.value = false;
            plan.value.id = null;
            plan.value = ref({});
            ElMessage({
              message: "修改成功!",
              type: "success",
            });
            getPlanData();
          }
        });
      }
    }
  });
};

//数据回显
const handleEcho = (id) => {
  echo(id).then((res) => {
    if (res.code === 200) {
      plan.value = res.data;
      isDialog.value = true;
    }
  });
};

//删除套餐
const handleDelete = (id) => {
  remove(id).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: "删除成功!",
        type: "success",
      });
      getPlanData();
    }
  });
};

//表单验证
const formRef = ref();
const rules = ref({
  name: [
    {
      required: true,
      message: "请输入套餐名称",
      trigger: "change",
    },
  ],
  price: [
    {
      required: true,
      message: "请输入套餐价格",
      trigger: "change",
    },
  ],
  unit: [
    {
      required: true,
      message: "请输入套餐单位",
      trigger: "change",
    },
  ],
});

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