<template>
  <div class="top-container">
    <div>
      <span>我的应用</span>
    </div>
    <div>
      <el-button>应用说明</el-button>
      <el-button v-has="`btn:application:create`" @click="handleCreate" color="rgb(61, 110, 255)">创建新应用</el-button>
    </div>
  </div>

  <el-skeleton v-if="requestLoding" :rows="13" animated />
  <!-- 数据展示 -->
  <el-card v-else v-for="(item, index) in appList" :key="index" style="width: 100%; margin-bottom: 10px" shadow="hover">
    <el-collapse accordion v-model="activeNames" @click="handleChange(item)">
      <el-collapse-item :name="index">
        <template #title>
          <div class="title-container">
            <div style="display: flex; align-items: center">
              <el-icon style="margin-right: 15px" :size="18" color="rgb(61, 110, 255)">
                <Menu />
              </el-icon>
              <span style="font-size: 14px; text-align: center">{{
                item.name
                }}</span>
              <span style="
                  font-size: 12px;
                  color: rgba(0, 0, 0, 0.65);
                  margin-left: 8px;
                  text-align: center;
                ">{{ item.createTime }} 创建</span>
            </div>
            <div style="margin-right: 10px">
              <el-switch v-has="`btn:application:open`" @click="handleStatus(item.id)" style="margin-right: 10px"
                v-model="item.status" inline-prompt active-text="开启" inactive-text="禁用" />
              <el-button v-has="`btn:application:update`" class="btn-style" @click="handleEdit(item.id)" :icon="Edit"
                link>编辑</el-button>
              <el-button v-has="`btn:application:addkey`" @click="handleAddKey(item.id)" class="btn-style"
                :icon="CirclePlus" link>添加key</el-button>
              <el-button v-has="`btn:application:delete`" @click="handleDelete(item.id)" class="btn-style" type="danger"
                :icon="Delete" link>删除</el-button>
            </div>
          </div>
        </template>
        <template #default>
          <el-table v-loading="loading" element-loading-text="加载中" :data="tableData" height="250"
            style="width: 100%; padding: 10px">
            <el-table-column fixed prop="keyName" label="Key名称" width="120" />
            <el-table-column prop="line" label="剩余额度" width="120">
              <template #default="scope">
                <el-tag :type="scope.row.line < 10 ? 'danger' : 'primary'" effect="dark">
                  {{ scope.row.line }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="accessKey" label="AccessKey" width="400">
              <template #default="scope">
                <el-input style="width: 300px" v-model="scope.row.accessKey" show-password type="password">{{
                  scope.row.accessKey }}</el-input>
              </template>
            </el-table-column>
            <el-table-column prop="secretKey" label="SecretKey" width="400">
              <template #default="scope">
                <el-input style="width: 300px" v-model="scope.row.secretKey" show-password type="password">{{
                  scope.row.secretKey }}</el-input>
              </template>
            </el-table-column>
            <el-table-column fixed="right" label="选项" width="280">
              <template #default="scope">
                <el-button v-has="`btn:application:key:line`" :icon="Setting" type="primary" size="small"
                  @click="handlerSetting(scope.row)">设置</el-button>
                <el-button v-has="`btn:application:key:line`" :icon="CircleCheck" type="success" size="small"
                  @click="buyLines">购买额度</el-button>
                <el-button v-has="`btn:application:key:delete`" @click="handleDeleteKey(scope.row.id)" type="danger"
                  :icon="Delete" size="small">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-collapse-item>
    </el-collapse>
  </el-card>

  <el-card v-if="isEmpty" style="width: 100%; margin-bottom: 10px" shadow="hover">
    <!-- 空状态 -->
    <el-empty description="创建应用体验吧！" />
  </el-card>

  <!--创建应用弹框 -->
  <div>
    <el-dialog style="border-radius: 5px; padding: 0" v-model="isDialog" :show-close="false" :width="400">
      <!-- 头部 -->
      <template #header>
        <div class="dialog-header">
          <span v-text="application.id ? '编辑应用' : '创建新应用'"></span>
        </div>
      </template>
      <div style="padding: 0 25px 0 25px">
        <el-form label-width="auto" status-icon ref="formRef" :rules="rules" label-position="left" :model="application">
          <el-form-item prop="name" label="应用名称">
            <el-input size="large" style="width: 100%" v-model="application.name" autocomplete="off" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleConsole">取消</el-button>
          <el-button type="primary" @click="createApp">{{
            application.id ? "确认" : "创建"
            }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>

  <!--添加Key弹框 -->
  <div>
    <el-dialog style="border-radius: 5px; padding: 0" v-model="isDialogKey" :show-close="false" :width="400">
      <!-- 头部 -->
      <template #header>
        <div class="dialog-header">
          <span>添加Key</span>
        </div>
      </template>
      <div style="padding: 0 25px 0 25px">
        <el-form label-width="auto" status-icon ref="formRef" :rules="rules" label-position="left"
          :model="applicationKey">
          <el-form-item prop="keyName" label="Key名称">
            <el-input size="large" style="width: 100%" v-model="applicationKey.keyName" autocomplete="off" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleConsole">取消</el-button>
          <el-button type="primary" @click="saveKey"> 添加 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>

  <!-- 设置白名单弹框 -->
  <div>
    <el-dialog :close-on-click-modal="false" @close="handleConsole" v-model="settingDialog"
      style="border-radius: 5px; padding: 0" :show-close="false" :width="450">
      <!-- 头部 -->
      <template #header>
        <div class="dialog-header">
          <span>设置IP白名单</span>
        </div>
      </template>
      <div style="padding: 0 25px 0 25px">
        <el-table :data="whiteList" height="160px">
          <el-table-column prop="keyName" label="key名称" width="110" />
          <el-table-column prop="address" label="IP地址" width="170" />
          <el-table-column prop="address" fixed="right" label="选项">
            <template #default="scope">
              <el-popconfirm confirm-button-text="确认" cancel-button-text="取消" :icon="Warning"
                @confirm="handlerDelete(scope.row.id)" icon-color="#E6A23C" title="确认删除?">
                <template #reference>
                  <el-button :icon="Delete" type="danger" size="small">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>

        <el-form style="margin-top: 30px" label-width="auto" status-icon ref="ipFormRef" :rules="rules" label-position="left" :model="IPList">
          <el-form-item label="IP白名单" prop="address">
            <el-input v-model="IPList.address" :placeholder="placeholder" :rows="4" type="textarea"
              label="IP白名单"></el-input>
          </el-form-item></el-form>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleConsole"> 取消 </el-button>
          <el-button type="primary" @click="saveIpWhite"> 提交 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import router from "@/router";
import {
  CirclePlus,
  Delete,
  Edit,
  CircleCheck,
  Setting,
} from "@element-plus/icons-vue";
import useUserStore from "@/stores/models/user/user.js";
import { BreedingRhombusSpinner } from "epic-spinners";
import {
  saveApplication,
  queryApplicationList,
  applicationEchoById,
  updateApp,
  updateStatus,
  removeApplication,
} from "@/api/application";
import {
  saveApplicationKey,
  queryKeysByAppId,
  removeKey,
} from "@/api/applicationkey";
import {
  saveIpWhiteList,
  queryIpWhiteList,
  removeIPWhite
} from "@/api/ipwhite";
import { ElMessage, ElMessageBox } from "element-plus";

const requestLoding = ref(true);
const loading = ref(true);
const userStore = useUserStore();
const isDialog = ref(false);
const isDialogKey = ref(false);
const application = ref({
  id: null,
  userId: null,
  name: "",
});
const applicationKey = ref({
  applicationId: null,
  keyName: "",
});
//应用数据
const appList = ref([]);
const activeNames = ref([]);
const tableData = ref([]);
const isEmpty = ref(false);
const IPList = ref({
  keyId: null,
  keyName: "",
  address: ""
})

onMounted(() => {
  getUserAppList();
});

//获取当前用户的应用数据
const getUserAppList = () => {
  queryApplicationList(userStore.userId).then((res) => {
    if (res.code === 200 && res.data !== null) {
      appList.value = res.data;
      isEmpty.value = false;
      requestLoding.value = false;
    } else {
      isEmpty.value = true;
      requestLoding.value = false;
    }
  });
};

//根据应用id查询keys数据
const handleChange = (item) => {
  loading.value = true;
  queryKeysByAppId(item.id).then((res) => {
    if (res.code === 200) {
      tableData.value = res.data;
      loading.value = false;
    }
  });
};

const setSomeValue = () => {
  isDialog.value = false;
  isDialogKey.value = false;
  application.value = {};
  application.value.id = null;
  applicationKey.value = {};
  settingDialog.value = false;
  IPList.value = ref({});
};
const handleConsole = () => {
  setSomeValue();
};

//创建应用弹框
const handleCreate = () => {
  isDialog.value = true;
};

//创建/编辑应用保存
const createApp = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (application.value.id === null) {
        application.value.userId = userStore.userId;
        saveApplication(application.value).then((res) => {
          if (res.code === 200) {
            setSomeValue();
            ElMessage({
              message: "创建成功！",
              type: "success",
            });
            getUserAppList();
          }
        });
      } else {
        //编辑
        updateApp(application.value).then((res) => {
          if (res.code === 200) {
            setSomeValue();
            ElMessage({
              message: "编辑成功！",
              type: "success",
            });
            getUserAppList();
          }
        });
      }
    }
  });
};

//添加key弹框
const handleAddKey = (appId) => {
  console.log(appId);
  isDialogKey.value = true;
  applicationKey.value.applicationId = appId;
};

//保存添加的key
const saveKey = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      saveApplicationKey(applicationKey.value).then((res) => {
        if (res.code === 200) {
          setSomeValue();
          ElMessage({
            message: "添加成功！",
            type: "success",
          });
        }
      });
    }
  });
};

//编辑应用
const handleEdit = (appId) => {
  applicationEchoById(appId).then((res) => {
    if (res.code === 200) {
      application.value = res.data;
      isDialog.value = true;
    }
  });
};

//删除应用
const handleDelete = (appId) => {
  ElMessageBox.confirm(
    "删除应用会将该应用及其下所有Key一并删除，请您谨慎操作!",
    "确定要删除应用吗？",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    }
  ).then(() => {
    removeApplication(appId).then((res) => {
      if (res.code === 200) {
        ElMessage({
          type: "success",
          message: "删除成功！",
        });
        getUserAppList();
      }
    });
  });
};

//删除key
const handleDeleteKey = (id) => {
  ElMessageBox.confirm("该Key会被彻底,请您谨慎操作!", "您确定要删除Key吗？", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    removeKey(id).then((res) => {
      if (res.code === 200) {
        ElMessage({
          type: "success",
          message: "删除成功！",
        });
      }
    });
  });
};

//修改应用状态
const handleStatus = (id) => {
  updateStatus(id).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: "操作成功！",
        type: "success",
      });
      getUserAppList();
    }
  });
};

//查看额度
const buyLines = () => {
  router.push({ name: "Order" });
};

//设置白名单
const placeholder = ref("非必填，留空表示无IP限制 \n添加IP名单后，只有白名单中的IP可访问服务\n多个IP地址请用英文分号（;）隔开");
const whiteList = ref(null);
const settingDialog = ref(false);
const handlerSetting = (item) => {
  IPList.value.keyId = item.id;
  IPList.value.keyName = item.keyName;
  //查询IP白名单数据
  getIPWhiteData();
  settingDialog.value = true;
};

//查询IP白名单数据
const getIPWhiteData = async () => {
  await queryIpWhiteList(IPList.value.keyId).then((res) => {
    if (res.code === 200) {
      whiteList.value = res.data;
    }
  });
}

//设置IP白名单
const saveIpWhite = () => {
  ipFormRef.value.validate((valid) => {
    if (valid) {
      saveIpWhiteList(IPList.value).then((res) => {
        if (res.code === 200) {
          ElMessage({
            message: '设置成功！',
            type: 'success'
          });
          //查询IP白名单数据
          getIPWhiteData();
          IPList.value.address = "";
        }
      })
    }
  })
}

//删除白名单
const handlerDelete = (id) => {
  removeIPWhite(id).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: '删除成功！',
        type: 'success'
      });
      //查询IP白名单数据
      getIPWhiteData();
    }
  })
}

//表单校验
const ipFormRef = ref(null);
const formRef = ref(null);
const rules = ref({
  name: [
    {
      required: true,
      message: "请输入应用名称",
      trigger: "change",
    },
  ],
  keyName: [
    {
      required: true,
      message: "请输入Key名称",
      trigger: "change",
    },
  ],
  address: [
    {
      required: true,
      message: "请设置IP地址",
      trigger: "change",
    },
  ],
});
</script>

<style lang="scss" scoped>
.top-container {
  display: flex;
  justify-content: space-between;
  height: 50px;
}

:deep(.el-collapse) {
  border: none;
}

:deep(.el-collapse-item__content) {
  border: none;
  padding: 0;
}

.title-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.btn-style {
  font-size: 13.5px;
  text-align: center;
  color: rgb(61, 110, 255);
}

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
  background-color: #409eff;
  border-radius: 5px 5px 0 0;
  color: #fff;
  text-align: center;
  font-weight: bold;
  font-size: 17px;
}
</style>
