<template>
  <div style="height: auto">
    <!-- 表格数据展示 -->
    <el-card style="width: 100%" shadow="never">
      <!-- 头部搜索 -->
      <div style="
          width: 100%;
          display: flex;
          justify-content: space-between;
          align-items: center;
        ">
        <div style="width: 80%">
          <el-input size="large" v-model="searchDto.name" class="search-input" placeholder="请输入接口名称">
          </el-input>
          <el-input size="large" v-model="searchDto.description" class="search-input" placeholder="请输入接口描述">
          </el-input>
          <el-select size="large" class="search-input" v-model="searchDto.method" clearable placeholder="选择请求方式">
            <el-option v-for="item in options" :key="item.value" :label="item.value" :value="item.value" />
          </el-select>
        </div>
        <div>
          <el-space :size="20">
            <el-button type="primary" @click="searchInterface">查询</el-button>
            <el-button type="info" @click="resetSearch">重置</el-button>
            <el-button v-has="`btn:interface:add`" @click="handleAddInterface" type="success">新增</el-button>
          </el-space>
        </div>
      </div>

      <el-divider style="margin: 30px 0 30px 0" />

      <el-table :header-cell-style="headerStyle" :cell-style="cellStyle" :data="tableData" style="width: 100%"
        v-loading="loading" element-loading-text="加载中">
        <el-table-column fixed prop="name" label="名称" width="220" />
        <el-table-column show-overflow-tooltip prop="description" label="描述" width="200" />
        <el-table-column prop="method" label="请求方法" width="140">
          <template #default="scope">
            <el-tag v-if="scope.row.method === 'POST'" type="success">{{ scope.row.method }}</el-tag>
            <el-tag v-else type="primary">{{ scope.row.method }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="url" label="url" width="200" />
        <el-table-column show-overflow-tooltip prop="params" label="请求参数" width="200" />
        <el-table-column show-overflow-tooltip prop="requestHeader" label="请求头" width="200" />
        <el-table-column show-overflow-tooltip prop="responseHeader" label="响应头" width="200" />
        <el-table-column fixed="right" label="操作" width="220">
          <template #default="scope">
            <el-popconfirm confirm-button-text="确认" cancel-button-text="取消" :icon="Warning"
              @confirm="handlePublish(scope.row)" icon-color="#E6A23C" :title="scope.row.status ? '确认下线？' : '确认发布？'">
              <template #reference>
                <el-button link v-has="`btn:interface:publish`"
                  :style="{ color: scope.row.status ? '#ff4949' : '#13ce66' }" type="primary">{{ scope.row.status ? "下线"
                  :
                  "发布" }}</el-button>
              </template>
            </el-popconfirm>
            <el-button v-has="`btn:interface:invoke`" link type="primary" @click="doInvoke(scope.row.id)">调用</el-button>
            <el-button v-has="`btn:interface:update`" link type="warning"
              @click="handleEdit(scope.row.id)">编辑</el-button>
            <el-popconfirm confirm-button-text="确认" cancel-button-text="取消" :icon="Warning"
              @confirm="handleDelete(scope.row.id)" icon-color="#E6A23C" title="确认删除?">
              <template #reference>
                <el-button v-has="`btn:interface:delete`" link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div style="margin-top: 20px">
        <el-pagination v-model:current-page="pageNo" :page-size="pageSize" :small="small" :disabled="disabled"
          :background="background" layout="total, prev, pager, next" :total="total" @change="handleChange" />
      </div>
    </el-card>

    <!-- 创建接口抽屉 -->
    <el-drawer :show-close="false" :style="{ '--el-drawer-padding-primary': '0' }" @close="handleDrawerClose" size="25%"
      v-model="isDrawer">
      <!-- 自定义头部 -->
      <template #header>
        <div class="drawer-header">
          <span v-text="interfaceInfo.id === null ? '新增接口' : '修改接口'"></span>
        </div>
      </template>

      <!-- 自定义内容 -->
      <div style="padding: 0 25px 0 25px;">
        <el-form label-width="auto" status-icon ref="formRef" :rules="rules" label-position="top"
          :model="interfaceInfo">
          <el-form-item label="接口名称" prop="name">
            <el-input size="large" style="width: 100%;" v-model="interfaceInfo.name"></el-input>
          </el-form-item>
          <el-form-item label="接口描述" prop="description">
            <el-input size="large" style="width: 100%;" v-model="interfaceInfo.description"></el-input>
          </el-form-item>
          <el-form-item label="请求方式" prop="method">
            <el-select size="large" v-model="interfaceInfo.method" clearable placeholder="选择请求方式" style="width: 330px">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="url" prop="url">
            <el-input size="large" style="width: 100%;" v-model="interfaceInfo.url"></el-input>
          </el-form-item>
          <el-form-item label="请求参数" prop="params">
            <el-input :rows="3" size="large" style="width: 100%;" v-model="interfaceInfo.params"
              type="textarea"></el-input>
          </el-form-item>
          <el-form-item label="请求头" prop="requestHeader">
            <el-input :rows="3" size="large" style="width: 100%;" v-model="interfaceInfo.requestHeader"
              type="textarea"></el-input>
          </el-form-item>
          <el-form-item label="响应头" prop="responseHeader">
            <el-input :rows="3" size="large" style="width: 100%;" v-model="interfaceInfo.responseHeader"
              type="textarea"></el-input>
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="saveInterfaceLoading" type="primary" @click="handleSaveInterface">
            确认
          </el-button>
        </div>
      </template>
    </el-drawer>

    <!-- 调用接口抽屉 -->
    <el-drawer :show-close="false" :style="{ '--el-drawer-padding-primary': '0' }" @close="handleDrawerClose" size="50%"
      v-model="isDrawerInvoke">
      <!-- 自定义头部 -->
      <template #header>
        <div class="drawer-header">
          <span>接口文档</span>
        </div>
      </template>

      <template #default>
        <div style="padding: 20px; background-color: #eee">
          <!-- 文档说明 -->
          <el-card shadow="never">
            <template #header>
              <div class="card-header">
                <h2>{{ interfaceView.name }}</h2>
              </div>
            </template>
            <div>
              <p>
                接口状态：<span class="view-span">{{
                  interfaceView.status ? "在线" : "离线"
                  }}</span>
              </p>
              <p>
                接口描述：<span class="view-span">{{
                  interfaceView.description
                  }}</span>
              </p>
              <p>
                请求地址：<span class="view-span">{{ interfaceView.url }}</span>
              </p>
              <p>
                请求方法：<span class="view-span">{{
                  interfaceView.method
                  }}</span>
              </p>
              <p>
                请求参数：<span class="view-span">{{
                  interfaceView.params
                  }}</span>
              </p>
              <p>
                请求头：<span class="view-span">{{
                  interfaceView.requestHeader
                  }}</span>
              </p>
              <p>
                响应头：<span class="view-span">{{
                  interfaceView.responseHeader
                  }}</span>
              </p>
            </div>
          </el-card>

          <el-divider style="margin: 30px 0 30px 0; border-color: #fff" />

          <el-card shadow="never">
            <template #header>
              <div class="card-header">
                <span>在线调试</span>
              </div>
            </template>
            <div>
              <!-- 获取accessKey -->
              <div style="margin-bottom: 35px">
                <span>accessKey </span>
                <el-tooltip class="box-item" effect="light" content="创建应用后分配相应的accessKey" placement="right">
                  <el-icon style="margin: 5px 0 0 10px" color="#409EFF" size="14px">
                    <QuestionFilled />
                  </el-icon>
                </el-tooltip>
                <div style="margin-top: 15px">
                  <el-input v-model="onlineInvoke.accessKey"></el-input>
                </div>
              </div>

              <!-- 请求参数 -->
              <div v-if="interfaceView.params === '' ? false : true">
                <span>请求参数</span>
                <div style="margin-top: 15px">
                  <el-input :rows="5" v-model="onlineInvoke.params" type="textarea"></el-input>
                </div>
              </div>

              <!-- 响应结果 -->
              <div style="margin-top: 35px">
                <span>响应结果</span>
                <div style="margin-top: 15px">
                  <vue-json-pretty :showLine="false" :showLength="true" :data="result" />
                </div>
              </div>
            </div>

            <!-- 调用按钮 -->
            <template #footer>
              <div>
                <el-button :loading="onlineInvokeLoading" type="primary" @click="handleOnlineInvoke">
                  调用
                </el-button>
              </div>
            </template>
          </el-card>
        </div>
      </template>
    </el-drawer>

    <!-- 发布接口弹框 -->
    <div>
      <el-dialog style="border-radius: 5px;padding: 0" v-model="isDialog" :show-close="false" :width="400">
        <!-- 自定义头部 -->
        <template #header>
          <div class="dialog-header">
            <span>发布接口</span>
          </div>
        </template>
        <div style="padding: 0 25px 0 25px;">
          <el-form label-width="auto" status-icon ref="formRef" :rules="rules" label-position="top" :model="apiKey">
            <el-form-item label="URL" prop="url">
              <el-input size="large" style="width: 100%" placeholder="请输入接口请求地址" v-model="apiKey.url"
                autocomplete="off" />
            </el-form-item>
            <el-form-item label="请求参数" prop="params">
              <el-input :rows="5" style="width: 100%" type="textarea" placeholder="请根据接口请求参数填写" v-model="apiKey.params"
                autocomplete="off" />
            </el-form-item>
          </el-form>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="handleConsole">取消</el-button>
            <el-button :loading="publishLoading" type="primary" @click="doPublishInterface">
              发布
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import useUserStore from "@/stores/models/user/user.js";
import { User, Edit, Delete } from "@element-plus/icons-vue";
import router from "@/router";
import {
  saveInterfaceInfo,
  findAdminInterfaceList,
  publishInterface,
  interfaceEchoById,
  updateInterfaceInfo,
  removeInterface,
  lineInterface,
  onlineInvokeInterface,
} from "@/api/interfaceinfo";
import { ElMessage, ElMessageBox } from "element-plus";
import VueJsonPretty from 'vue-json-pretty';
import 'vue-json-pretty/lib/styles.css';

//表格数据
const userStore = useUserStore();
const tableData = ref([]);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);

const interfaceInfo = ref({
  id: null,
  userId: null,
  name: "",
  description: "",
  method: "",
  url: "",
  params: "",
  requestParam: "",
  requestHeader: "",
  responseHeader: "",
});
const searchDto = ref({
  name: "",
  description: "",
  method: "",
});
//是否弹出分配角色抽屉
const isDrawer = ref(false);
//在线调用接口抽屉
const isDrawerInvoke = ref(false);
//新增用户弹框
const isDialog = ref(false);
//多选默认选中数据
const checkedRoles = ref([]);
const options = [
  {
    value: "GET",
    label: "GET",
  },
  {
    value: "POST",
    label: "POST",
  },
  {
    value: "PUT",
    label: "PUT",
  },
  {
    value: "DELETE",
    label: "DELETE",
  },
];
const loading = ref(true);
//获取ak 和 sk
const apiKey = ref({
  id: null,
  params: "",
  url: "",
});

//在线调用接口
const onlineInvoke = ref({
  id: null,
  accessKey: "",
  params: "",
  url: "",
});

onMounted(() => {
  getInterfaceData();
});

//分页获取接口数据
const getInterfaceData = () => {
  findAdminInterfaceList(pageNo.value, pageSize.value, searchDto.value).then(
    (res) => {
      if (res.code === 200 && res.data !== null) {
        tableData.value = res.data.data;
        total.value = res.data.total;
        loading.value = false;
      }
    }
  );
};

//模糊查询
const searchInterface = () => {
  if (
    searchDto.value.name !== "" ||
    searchDto.value.description !== "" ||
    searchDto.value.method !== ""
  ) {
    getInterfaceData();
  }
};

//监听分页
const handleChange = (current, page) => {
  pageNo.value = current;
  pageSize.value = page;
  getInterfaceData();
};

//重置
const resetSearch = () => {
  searchDto.value = {
    name: "",
    description: "",
    method: "",
  };
  getInterfaceData();
};

//抽屉关闭时的回调
const handleDrawerClose = () => {
  isDrawer.value = false;
  interfaceInfo.value = ref({});
  interfaceInfo.value.id = null;
  onlineInvoke.value = ref({});
  onlineInvoke.value.id = null;
  result.value = null;
};

//新增接口按钮抽屉
const handleAddInterface = () => {
  isDrawer.value = true;
};

//保存接口数据
const saveInterfaceLoading = ref(false);
const handleSaveInterface = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      saveInterfaceLoading.value = true;
      interfaceInfo.value.userId = userStore.userId;
      if (interfaceInfo.value.id === null) {
        save();
      } else {
        update();
      }
    }
  });
};

//保存
const save = () => {
  saveInterfaceInfo(interfaceInfo.value).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: "新增成功！",
        type: "success",
      });
      getInterfaceData();
      saveInterfaceLoading.value = false;
      isDrawer.value = false;
    } else {
      getInterfaceData();
      saveInterfaceLoading.value = false;
    }
  });
};

//修改
const update = () => {
  updateInterfaceInfo(interfaceInfo.value).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: "修改成功！",
        type: "success",
      });
      getInterfaceData();
      saveInterfaceLoading.value = false;
      isDrawer.value = false;
    } else {
      getInterfaceData();
      saveInterfaceLoading.value = false;
    }
  });
};

//修改接口数据
const handleEdit = (id) => {
  interfaceEchoById(id).then((res) => {
    if (res.code === 200) {
      interfaceInfo.value = res.data;
      isDrawer.value = true;
    }
  });
};

//修改接口状态
const handleStatus = (id) => {
  updateStatus(id).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: "操作成功！",
        type: "success",
      });
      getInterfaceData();
    }
  });
};

//删除接口
const handleDelete = (id) => {
  ElMessageBox.confirm(
    "该接口会被彻底删除,请您谨慎操作!",
    "确定要删除此接口吗？",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  ).then(() => {
    removeInterface(id).then((res) => {
      if (res.code === 200) {
        ElMessage({
          type: "success",
          message: "删除成功！",
        });
        getInterfaceData();
      }
    });
  });
};

//发布接口弹框
const handlePublish = (item) => {
  if (item.status) {
    //说明是要下线接口
    lineInterface(item.id).then((res) => {
      if (res.code === 200) {
        ElMessage({
          message: "接口下线成功！",
          type: "success",
        });
        getInterfaceData();
      }
    });
  } else {
    apiKey.value.id = item.id;
    if (item.params !== "") {
      isDialog.value = true;
    } else {
      apiKey.value.url = item.url;
      commonPublishInterface();
    }
  }
};

//取消发布
const handleConsole = () => {
  isDialog.value = false;
  apiKey.value = ref({});
};

//发布接口
const publishLoading = ref(false);
const doPublishInterface = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      publishLoading.value = true;
      commonPublishInterface();
    }
  });
};

//发布接口方法
const commonPublishInterface = () => {
  publishInterface(apiKey.value).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: "接口发布成功！",
        type: "success",
      });
      getInterfaceData();
      publishLoading.value = false;
      isDialog.value = false;
      apiKey.value = ref({});
    } else {
      publishLoading.value = false;
    }
  });
};

//调用接口文档抽屉
const interfaceView = ref();
const doInvoke = (id) => {
  onlineInvoke.value.id = id;
  interfaceEchoById(id).then((res) => {
    if (res.code === 200) {
      interfaceView.value = res.data;
      isDrawerInvoke.value = true;
      onlineInvoke.value.url = interfaceView.value.url;
    }
  });
};

//正式调用接口
const onlineInvokeLoading = ref(false);
//接口响应结果
const result = ref(null);
const handleOnlineInvoke = () => {
  if (onlineInvoke.value.accessKey !== "") {
    onlineInvokeLoading.value = true;
    onlineInvokeInterface(onlineInvoke.value).then((res) => {
      if (res.code === 200) {
        //将result拼接成json格式输出,包括换行
        result.value = res;
        onlineInvokeLoading.value = false;
      } else {
        result.value = res;
        onlineInvokeLoading.value = false;
      }
    });
  } else {
    ElMessage({
      message: "请设置accessKey的值!",
      type: "warning",
    });
  }
};

//表单校验
const formRef = ref(null);
const rules = ref({
  name: [
    {
      required: true,
      message: "请输入请求名称",
      trigger: "change",
    },
  ],
  description: [
    {
      required: true,
      message: "请输入请求描述",
      trigger: "change",
    },
  ],
  method: [
    {
      required: true,
      message: "请输入请求方式",
      trigger: "change",
    },
  ],
  url: [
    {
      required: true,
      message: "请输入请求url",
      trigger: "change",
    },
  ],
  requestParam: [
    {
      required: true,
      message: "请输入请求参数",
      trigger: "change",
    },
  ],
  requestHeader: [
    {
      required: true,
      message: "请输入请求头",
      trigger: "change",
    },
  ],
  responseHeader: [
    {
      required: true,
      message: "请输入响应头",
      trigger: "change",
    },
  ],
  accessKey: [
    {
      required: true,
      message: "请输入应用key",
      trigger: "change",
    },
  ],
  secretKey: [
    {
      required: true,
      message: "请输入密钥",
      trigger: "change",
    },
  ],
  salt: [
    {
      required: true,
      message: "请输入盐值",
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



<style scoped lang="scss">
.dialog-footer {
  height: 40px;
  width: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 25px 25px 25px;
}

.search-input {
  width: 26%;
  margin-right: 35px;
}

p {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 20px;
}

.view-span {
  color: #73767a;
  font-size: 14px;
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

.drawer-header {
  padding: 10px;
  height: 40px;
  width: 100%;
  background-color: #409EFF;
  border-radius: 0 5px 0 0;
  color: #fff;
  text-align: center;
  font-weight: bold;
  font-size: 17px;
}

:deep(.el-textarea__inner) {
  background: #252b48;
  color: #fff;
}
</style>