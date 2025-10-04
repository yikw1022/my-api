<template>
  <div style="height: auto">
    <el-card style="width: 100%" shadow="never">
      <!-- 头部搜索 -->
      <div style="
          width: 100%;
          display: flex;
          justify-content: space-between;
          align-items: center;
        ">
        <div style="width: 80%">
          <el-input class="search-input" v-model="searchUser.username" placeholder="请输入用户名" size="large">
          </el-input>
          <el-input class="search-input" v-model="searchUser.phone" placeholder="请输入手机号" size="large">
          </el-input>
          <el-select size="large" class="search-input" v-model="searchUser.status" clearable placeholder="请选择用户状态">
            <el-option v-for="item in options" :key="item.value" :label="item.value" :value="item.label" />
          </el-select>
        </div>
        <div>
          <el-space :size="20">
            <el-button type="primary" @click="handlerSearch">查询</el-button>
            <el-button type="info" @click="resetSearch">重置</el-button>
            <el-button v-has="`btn:user:add`" @click="handleAddUser" type="success">新增</el-button>
          </el-space>
        </div>
      </div>

      <el-divider style="margin: 30px 0 30px 0" />

      <el-table :header-cell-style="headerStyle" :cell-style="cellStyle" v-loading="loading" element-loading-text="加载中"
        :data="tableData" style="width: 100%">
        <el-table-column fixed prop="username" label="用户名" width="120" />
        <el-table-column prop="roles" label="角色" width="140">
          <template #default="scope">
            <el-tag effect="dark" style="margin-right: 8px" v-for="(item, index) in scope.row.roles" :key="index"
              :type="scope.row.username === 'admin' ? 'success' : 'primary'">{{ item }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="160" />
        <el-table-column prop="email" label="邮箱" width="230" />
        <el-table-column prop="gender" label="性别" width="100">
          <template #default="scope">
            <el-tag effect="dark" :type="scope.row.gender === 1 ? 'primary' : 'success'">
              {{ scope.row.gender === 1 ? '男' : '女' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag effect="dark" :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column fixed="right" label="选项" width="280">
          <template #default="scope">
            <el-button :icon="User" type="primary" size="small" v-has="`btn:user:per:role`"
              @click="handleRole(scope.row)">
              分配角色
            </el-button>
            <el-button v-has="`btn:user:update`" @click="editUser(scope.row.id)" :icon="Edit" type="warning"
              size="small">编辑</el-button>
            <el-popconfirm confirm-button-text="确认" cancel-button-text="取消" :icon="Warning"
              @confirm="handlerDelete(scope.row.id)" icon-color="#E6A23C" title="确认删除?">
              <template #reference>
                <el-button v-has="`btn:user:remove`" :icon="Delete" type="danger" size="small">删除</el-button>
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

      <!-- 分配角色抽屉 -->
      <el-drawer :show-close="false" :style="{ '--el-drawer-padding-primary': '0' }" @close="handleDrawerClose"
        size="24%" v-model="isDrawer">
        <!-- 自定义头部 -->
        <template #header>
          <div class="drawer-header">
            <span>分配角色</span>
          </div>
        </template>

        <!-- 自定义内容 -->
        <div style="padding: 0 25px 0 25px;">
          <el-form>
            <el-form-item label="用户名称">
              <el-input size="large" v-model="tempName" disabled=""></el-input>
            </el-form-item>
            <el-form-item label="角色列表">
              <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">
                全选
              </el-checkbox>
              <hr>
              <el-checkbox-group v-model="checkedRoles" @change="handleCheckedCitiesChange">
                <el-checkbox v-for="role in roleList" :key="role" :label="role" :value="role">
                  {{ role }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form>
        </div>
        <!-- 自定义底部 -->
        <template #footer>
          <div class="dialog-footer">
            <el-button plain type="primary" @click="handleAssign">
              确认
            </el-button>
          </div>
        </template>
      </el-drawer>

      <!-- 新增弹框 -->
      <div>
        <el-dialog @close="handleConsole" style="border-radius: 5px;padding: 0" v-model="isDialog" align-center
          :show-close="false" :width="400">
          <!-- 头部 -->
          <template #header>
            <div class="dialog-header">
              <span v-text="user.id === null ? '新增用户' : '修改用户'"></span>
            </div>
          </template>
          <!-- 自定义内容 -->
          <div style="padding: 0 25px 0 25px;">
            <el-form label-position="left" ref="formRef" :rules="rules" label-width="auto" status-icon :model="user">
              <el-form-item prop="username">
                <el-input size="large" style="width: 100%" v-model="user.username" placeholder="用户名" />
              </el-form-item>
              <el-form-item prop="password">
                <el-input show-password size="large" placeholder="密码" style="width: 100%" type="password"
                  v-model="user.password" />
              </el-form-item>
              <el-form-item prop="phone">
                <el-input size="large" placeholder="手机号" style="width: 100%" v-model="user.phone" />
              </el-form-item>
              <el-form-item prop="email">
                <el-input size="large" placeholder="邮箱" style="width: 100%" v-model="user.email" />
              </el-form-item>
              <el-form-item prop="gender">
                <el-radio-group v-model="user.gender">
                  <el-radio :value="1">男</el-radio>
                  <el-radio :value="0">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item prop="status">
                <el-switch v-model="userStatus" inline-prompt active-text="启用" inactive-text="禁用" />
              </el-form-item>
            </el-form>
          </div>

          <!-- 底部 -->
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="handleConsole">取消</el-button>
              <el-button :loading="saveLoading" type="primary" @click="saveRole"> 确认 </el-button>
            </div>
          </template>
        </el-dialog>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { findUserList, updateUserInfo, doRegister, queryRoles, queryEchoUserInfo, saveRoles, deleteUser } from "@/api/user";
import { queryRoleList } from "@/api/role";
import { User, Edit, Delete, Warning } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";

//表格数据
const loading = ref(true);
const saveLoading = ref(false);
const tableData = ref([]);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);
//搜索用户名
const searchUser = ref({
  username: '',
  phone: '',
  status: null
})
//用户状态
const userStatus = ref(false);
const user = ref({
  id: null,
  username: "",
  password: "",
  phone: '',
  email: '',
  status: null,
  gender: null
});
const options = [
  {
    value: "禁用",
    label: "0",
  },
  {
    value: "启用",
    label: "1",
  }
];
//所有角色数据
const roleList = ref([]);
//是否弹出分配角色抽屉
const isDrawer = ref(false);
//新增用户弹框
const isDialog = ref(false);
//是否全选，默认不
const checkAll = ref(false);
//多选默认选中数据
const checkedRoles = ref([]);
const isIndeterminate = ref(false);

onMounted(() => {
  getUserList();
  getAllRoleList();
  loading.value = false;
});

//获取用户分页数据
const getUserList = () => {
  findUserList(pageNo.value, pageSize.value, searchUser.value).then((res) => {
    if (res.code === 200 && res.data !== null) {
      tableData.value = res.data.data;
      total.value = res.data.total;
    }
  });
};

//获取所有角色数据
const getAllRoleList = () => {
  queryRoleList().then((res) => {
    if (res.code === 200) {
      roleList.value = res.data;
    }
  });
};

//监听分页
const handleChange = (current, page) => {
  pageNo.value = current;
  pageSize.value = page;
  getUserList();
}

//用户搜索
const handlerSearch = () => {
  if (
    searchUser.value.username !== "" ||
    searchUser.value.phone !== "" ||
    searchUser.value.status !== null
  ) {
    getUserList();
  }
}

//重置
const resetSearch = () => {
  searchUser.value = {
    username: "",
    phone: ""
  };
  getUserList();
};

//分配角色弹出抽屉
const tempName = ref("");
const handleRole = (user) => {
  tempName.value = user.username;
  //根据用户id查询用户当前拥有的所有角色
  queryRoles(user.id).then((res) => {
    if (res.code === 200 && res.data !== null) {
      checkedRoles.value = res.data;
    }
  });
  isDrawer.value = !isDrawer.value;
};

//将一些共用的值
const setSomeValue = () => {
  isDrawer.value = false;
  tempName.value = "";
  checkedRoles.value = [];
};

//抽屉关闭时的回调
const handleDrawerClose = () => {
  setSomeValue();
};

//全选角色逻辑
const handleCheckAllChange = (val) => {
  checkedRoles.value = val ? roleList.value : [];
  isIndeterminate.value = !isIndeterminate.value;
};
const handleCheckedCitiesChange = (value) => {
  const checkedCount = value.length;
  checkAll.value = checkedCount === roleList.value.length;
  isIndeterminate.value =
    checkedCount > 0 && checkedCount < roleList.value.length;
};

//处理分配角色逻辑
const handleAssign = () => {
  console.log(checkedRoles.value);
  saveRoles(tempName.value, checkedRoles.value).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: "角色分配成功",
        type: "success",
      });
      getUserList();
      setSomeValue();
    }
  });
};

//取消新增用户
const handleConsole = () => {
  user.value = ref({});
  user.value.id = null;
  isDialog.value = false;
  userStatus.value = false;
};

//新增用户按钮
const handleAddUser = () => {
  isDialog.value = true;
};

//保存新增用户
const saveRole = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      saveLoading.value = true;
      if (userStatus.value) {
        user.value.status = 1;
      } else {
        user.value.status = 0;
      }
      if (user.value.id === null) {
        doRegister(user.value).then((res) => {
          if (res.code === 200) {
            saveLoading.value = false;
            ElMessage({
              message: '新增成功！',
              type: 'success'
            });
            isDialog.value = false;
            getUserList();
          } else {
            saveLoading.value = false;
          }
        });
      } else {
        updateUserInfo(user.value).then((res) => {
          if (res.code === 200) {
            saveLoading.value = false;
            ElMessage({
              message: '修改成功！',
              type: 'success'
            });
            isDialog.value = false;
            getUserList();
          } else {
            saveLoading.value = false;
          }
        });
      }
    }
  });
};

//查询用户回显数据
const editUser = (userId) => {
  queryEchoUserInfo(userId).then((res) => {
    if (res.code === 200) {
      user.value = res.data;
      isDialog.value = true;
      if (user.value.status === 1) {
        userStatus.value = true;
      } else {
        userStatus.value = false;
      }
    }
  })
}

//删除用户
const handlerDelete = (userId) => {
  deleteUser(userId).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: "删除成功！",
        type: 'success'
      });
      isDialog.value = false;
      getUserList();
    }
  })
}

//表单验证
const formRef = ref();
const rules = ref({
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { min: 11, max: 11, message: '长度为11位', trigger: 'blur' },
  ],
  username: [
    {
      required: true,
      message: '请输入用户名',
      trigger: 'change',
    },
  ],
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: 'change',
    },
  ],
  email: [
    {
      required: true,
      message: '请输入邮箱',
      trigger: 'change',
    },
  ],
  gender: [
    {
      required: true,
      message: '请选择性别',
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
  margin-bottom: 20px;
  height: 40px;
  width: 100%;
  background-color: #409EFF;
  border-radius: 0 5px 0 0;
  color: #fff;
  text-align: center;
  font-weight: bold;
  font-size: 17px;
}
</style>