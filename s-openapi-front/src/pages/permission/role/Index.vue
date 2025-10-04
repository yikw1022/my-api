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
        <div style="width: 60%">
          <el-input v-model="roleName" style="width: 35%" placeholder="请输入角色名" size="large">
          </el-input>
        </div>

        <div>
          <el-space :size="20">
            <el-button type="primary" @click="handlerSearch">查询</el-button>
            <el-button type="info" @click="resetSearch">重置</el-button>
            <el-button v-has="`btn:role:add`" @click="handleAddRole" type="success">新增</el-button>
          </el-space>
        </div>
      </div>

      <el-divider style="margin: 30px 0 30px 0" />

      <el-table :header-cell-style="headerStyle" :cell-style="cellStyle" v-loading="loading" element-loading-text="加载中"
        :data="tableData" style="width: 100%">
        <el-table-column fixed prop="sort" label="排序" width="100" />
        <el-table-column prop="roleName" label="角色" width="150" />
        <el-table-column prop="createTime" label="创建时间" width="220" />
        <el-table-column prop="updateTime" label="修改时间" width="220" />
        <el-table-column fixed="right" label="选项">
          <template #default="scope">
            <el-button :icon="Lock" type="primary" size="small" v-has="`btn:role:per`"
              @click="handleOpenDrawer(scope.row)">
              分配权限
            </el-button>
            <el-button @click="editRole(scope.row.id)" v-has="`btn:role:update`" :icon="Edit" type="warning"
              size="small">编辑</el-button>
            <el-popconfirm confirm-button-text="确认" cancel-button-text="取消" :icon="Warning"
              @confirm="handlerDelete(scope.row.id)" icon-color="#E6A23C" title="确认删除?">
              <template #reference>
                <el-button v-has="`btn:role:remove`" :icon="Delete" type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div style="margin-top: 20px">
        <el-pagination v-model:current-page="pageNo" :page-size="pageSize" :small="small" :disabled="disabled"
          :background="background" layout="total, prev, pager, next" :total="total" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>


      <!-- 新增弹框 -->
      <div>
        <el-dialog @close="handleConsole" style="border-radius: 5px;padding: 0" v-model="isDialog" align-center
          :show-close="false" :width="350">
          <!-- 头部 -->
          <template #header>
            <div class="dialog-header">
              <span v-text="role.id === null ? '新增角色' : '修改角色'"></span>
            </div>
          </template>
          <div style="padding: 0 25px 0 25px;">
            <el-form :model="role" label-position="left" ref="formRef" :rules="rules" label-width="auto" status-icon>
              <el-form-item label="角色" prop="roleName">
                <el-input size="large" v-model="role.roleName" style="width: 100%" autocomplete="off" />
              </el-form-item>
              <el-form-item label="序号" prop="sort">
                <el-input size="large" type="number" v-model="role.sort" style="width: 100%" autocomplete="off" />
              </el-form-item>
            </el-form>
          </div>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="handleConsole">取消</el-button>
              <el-button type="primary" @click="saveRole"> 确认 </el-button>
            </div>
          </template>
        </el-dialog>
      </div>

      <!-- 分配权限抽屉 -->
      <div>
        <el-drawer :show-close="false" :style="{ '--el-drawer-padding-primary': '0' }" @close="handleDrawerClose"
          size="24%" v-model="isDrawer">
          <!-- 自定义头部 -->
          <template #header>
            <div class="drawer-header">
              <span>分配权限</span>
            </div>
          </template>
          <!-- 自定义内容 -->
          <div style="padding: 0 25px 0 25px;">
            <el-tree ref="tree" style="max-width: 600px" :data="menuData" show-checkbox node-key="id"
              :props="defaultProps" :default-checked-keys="checkedKeys" default-expand-all />
          </div>
          <!-- 自定义底部 -->
          <template #footer>
            <div class="dialog-footer">
              <el-button type="primary" plain @click="handleSavePermission">确认</el-button>
            </div>
          </template>
        </el-drawer>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";
import { findRoleList, save, savePermission, deleteRole, updateRoleInfo, queryEchoRoleInfo } from "@/api/role";
import { queryMenuList, queryRoleMenu } from "@/api/menu";
import { Lock, Edit, Delete, Warning } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";

//表格数据
const loading = ref(true);
const tableData = ref([]);
const pageNo = ref(1);
const pageSize = ref(10);
const total = ref(0);
//搜索用户名
const roleName = ref("");
//新增角色弹框
const isDialog = ref(false);
//分配权限抽屉弹出
const isDrawer = ref(false);
const menuData = ref([]);
const defaultProps = {
  children: "children",
  label: "label",
};
//默认选中
const checkedKeys = ref([]);
//获取树节点的ref
const tree = ref(null);
//用来临时存储role的值
const tempRole = ref(null);

//收集角色表单内容
const role = ref({
  id: null,
  roleName: "",
  sort: null,
});

onMounted(() => {
  getRoleList();
  loading.value = false;
});

//获取用户分页数据
const getRoleList = () => {
  findRoleList(pageNo.value, pageSize.value, roleName.value).then((res) => {
    if (res.code === 200 && res.data !== null) {
      tableData.value = res.data.data;
      total.value = res.data.total;
    }
  });
};

//获取菜单数据 抽屉树形数据
const getAllMenuData = () => {
  queryMenuList().then((res) => {
    if (res.code === 200) {
      menuData.value = res.data;
    }
  });
};

//根据角色名称搜索
const handlerSearch = () => {
  console.log("我是随")
  if (roleName.value !== "") {
    getRoleList();
    console.log("我是随")
  }
}

//重置
const resetSearch = () => {
  roleName.value = "";
  getRoleList();
};

//新增角色按钮弹框
const handleAddRole = () => {
  isDialog.value = true;
};

//取消新增角色
const handleConsole = () => {
  isDialog.value = false;
  role.value = ref({});
  role.value.id = null;
};

//新增角色逻辑
const saveRole = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (role.value.id === null) {
        save(role.value).then((res) => {
          if (res.code === 200) {
            ElMessage({
              message: "新增角色成功",
              type: "success",
            });
            isDialog.value = false;
            getRoleList();
          }
        });
      } else {
        updateRoleInfo(role.value).then((res) => {
          if (res.code === 200) {
            ElMessage({
              message: "修改成功",
              type: "success",
            });
            isDialog.value = false;
            getRoleList();
          }
        });
      }
    }
  })
};

//角色信息回显
const editRole = (id) => {
  queryEchoRoleInfo(id).then((res) => {
    if (res.code === 200) {
      role.value = res.data;
      isDialog.value = true;
    }
  })
}

//分配权限抽屉弹出逻辑
const handleOpenDrawer = (role) => {
  tempRole.value = role;
  //获取当前角色所有的菜单权限的id
  queryRoleMenu(role).then((res) => {
    if (res.code === 200) {
      checkedKeys.value = res.data;
      getAllMenuData();
      isDrawer.value = true;
    }
  });
};

//抽屉关闭时的回调
const handleDrawerClose = () => {
  checkedKeys.value = [];
  tempRole.value = null;
  isDrawer.value = false;
};

//保存分配好的权限数据
const handleSavePermission = () => {
  let tempKeys = tree.value.getCheckedKeys();
  let halfKeys = tree.value.getHalfCheckedKeys();
  let finalKeys = tempKeys.concat(halfKeys);
  savePermission(finalKeys, tempRole.value.id).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: "分配权限成功",
        type: "success",
      });
      isDrawer.value = false;
    }
  });
};

//删除角色
const handlerDelete = (id) => {
  deleteRole(id).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: "删除成功！",
        type: 'success'
      });
      getRoleList();
    }
  })
}

//表单验证
const formRef = ref();
const rules = ref({
  roleName: [
    {
      required: true,
      message: '请输入角色名称',
      trigger: 'change',
    },
  ],
  sort: [
    {
      required: true,
      message: '请输入角色序号',
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
.confim {
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
}

.dialog-header {
  padding: 10px;
  height: 40px;
  width: 100%;
  background-color: #409EFF;
  border-radius: 5px 5px 0 0;
  color: #fff;
  text-align: center;
  font-weight: bold;
  font-size: 17px;
  margin-bottom: 20px;
}

.dialog-footer {
  height: 40px;
  width: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 25px 25px 25px;
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