<template>

  <!-- 表格数据展示 -->
  <el-card style="width: 100%" shadow="never">
    <div style="margin-bottom: 10px">
      <el-button v-has="`btn:menu:add`" type="primary" @click="handleAdd">新增</el-button>
    </div>
    <el-table :header-cell-style="headerStyle" :cell-style="cellStyle" v-loading="loading" element-loading-text="加载中"
      :data="menuData" style="width: 100%; margin-bottom: 20px" row-key="id">
      <el-table-column fixed="left" prop="label" label="标题" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="path" label="路径" />
      <el-table-column prop="type" label="分类">
        <template #default="scope">
          <el-tag type="primary" effect="dark" v-if="scope.row.type === 0">目录</el-tag>
          <el-tag type="success" effect="dark" v-if="scope.row.type === 1">菜单</el-tag>
          <el-tag type="warning" effect="dark" v-if="scope.row.type === 2">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="button" label="权限值" />
      <el-table-column prop="button" fixed="right" label="选项">
        <template #default="scope">
          <el-button :icon="User" type="primary" size="small" v-has="`btn:menu:add`"
            :disabled="scope.row.type === 2 ? true : false" @click="handleAddMenu(scope.row)">
            新增
          </el-button>
          <el-button @click="editMenu(scope.row)" v-has="`btn:menu:update`" :icon="Edit" type="warning"
            size="small">编辑</el-button>
          <el-popconfirm confirm-button-text="确认" cancel-button-text="取消" :icon="Warning"
            @confirm="handleDelete(scope.row.id)" icon-color="#E6A23C" title="确认删除?">
            <template #reference>
              <el-button v-has="`btn:menu:remove`" :icon="Delete" type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <!-- 一级目录新增弹框 -->
  <div>
    <el-dialog @close="handleConsole" style="border-radius: 5px;padding: 0;" v-model="isDialog" align-center
      :show-close="false" :width="350">
      <!-- 头部 -->
      <template #header>
        <div class="dialog-header">
          <span>新增目录</span>
        </div>
      </template>
      <div style="padding: 0 25px 0 25px;">
        <el-form label-position="left" ref="formRef" :rules="rules" label-width="auto" status-icon :model="menu">
          <el-form-item label="名称" prop="name">
            <el-input size="large" style="width: 100%" placeholder="请输入首字母大写的英文名称" v-model="menu.name"
              autocomplete="off" />
          </el-form-item>
          <el-form-item label="路径" prop="path">
            <el-input size="large" style="width: 100%" placeholder="如: /path" v-model="menu.path" autocomplete="off" />
          </el-form-item>
          <el-form-item label="标题" prop=title>
            <el-input size="large" v-model="menu.title" style="width: 100%" autocomplete="off" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleConsole">取消</el-button>
          <el-button type="primary" @click="handleSaveDirectory">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>

  <!-- 菜单新增弹框 -->
  <div>
    <el-dialog @close="handleConsole" style="border-radius: 5px;padding: 0;" v-model="isDialogMenu" align-center
      :show-close="false" :width="350">
      <!-- 头部 -->
      <template #header>
        <div class="dialog-header">
          <span v-text="menu.id === null ? '新增菜单' : '修改目录 / 菜单'"></span>
        </div>
      </template>
      <div style="padding: 0 25px 0 25px;">
        <el-form label-position="left" ref="menuFormRef" :rules="rules" label-width="auto" status-icon :model="menu">
          <el-form-item label="名称" prop="name">
            <el-input size="large" placeholder="请输入首字母大写的英文名称" v-model="menu.name" autocomplete="off" />
          </el-form-item>
          <el-form-item label="路径" prop="path">
            <el-input size="large" placeholder="如: /path" v-model="menu.path" autocomplete="off" />
          </el-form-item>
          <el-form-item label="标题" prop="title">
            <el-input size="large" v-model="menu.title" autocomplete="off" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleConsole">取消</el-button>
          <el-button type="primary" @click="handleSaveMenu"> 确认 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>

  <!-- 菜单按钮新增弹框 -->
  <div>
    <el-dialog @close="handleConsole" style="border-radius: 5px;padding: 0;" v-model="isDialogBtn" align-center
      :show-close="false" :width="350">
      <!-- 头部 -->
      <template #header>
        <div class="dialog-header">
          <span v-text="menuBtn.id === null ? '新增权限' : '修改权限'"></span>
        </div>
      </template>
      <div style="padding: 0 25px 0 25px;">
        <el-form label-position="left" ref="btnFormRef" :rules="rules" label-width="auto" status-icon :model="menuBtn">
          <el-form-item label="权限" prop="button">
            <el-input size="large" placeholder="如: btn:user:add" v-model="menuBtn.button" autocomplete="off" />
          </el-form-item>
          <el-form-item label="标题" prop="title">
            <el-input size="large" v-model="menuBtn.title" autocomplete="off" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleConsole">取消</el-button>
          <el-button type="primary" @click="handleSaveMenuBtn">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import {
  updateMenuDirInfo,
  updateMenuBtnInfo,
  updateMenuInfo,
  queryMenuList,
  queryRoleMenu,
  saveDirectory,
  saveMenu,
  saveMenuBtn,
  deleteMenu,
  queryEchoMenuInfo,
  queryEchoMenuBtnInfo
} from "@/api/menu";
import { ElMessage } from "element-plus";

const loading = ref(true);
const isDialog = ref(false);
const isDialogMenu = ref(false);
const isDialogBtn = ref(false);
const menu = ref({
  id: null,
  name: "",
  path: "",
  title: "",
});
const menuBtn = ref({
  id: null,
  button: "",
  title: "",
});

//菜单展示数据
const menuData = ref([]);
onMounted(() => {
  getAllMenuData();
  loading.value = false;
});

//获取菜单数据 抽屉树形数据
const getAllMenuData = () => {
  queryMenuList().then((res) => {
    if (res.code === 200) {
      menuData.value = res.data;
    }
  });
};

//新增一级目录弹框
const handleAdd = () => {
  isDialog.value = true;
};

//关闭弹框
const handleConsole = () => {
  menu.value = ref({});
  menu.value.id = null;
  menuBtn.value = ref({});
  menuBtn.value.id = null;
  isDialog.value = false;
  isDialogMenu.value = false;
  isDialogBtn.value = false;
  isDirUpdate.value = false;
};

//新增一级目录
const handleSaveDirectory = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      saveDirectory(menu.value).then((res) => {
        if (res.code === 200) {
          isDialog.value = false;
          ElMessage({
            message: "添加成功！",
            type: "success",
          });
          getAllMenuData();
        }
      });
    }
  })
};

//菜单数据回显
const isDirUpdate = ref(false);
const editMenu = (item) => {
  if (item.type === 0) {
    queryEchoMenuInfo(item.id).then((res) => {
      if (res.code === 200) {
        menu.value = res.data;
        isDialogMenu.value = true;
        isDirUpdate.value = true;
      }
    })
  } else if (item.type === 1) {
    queryEchoMenuInfo(item.id).then((res) => {
      if (res.code === 200) {
        menu.value = res.data;
        isDialogMenu.value = true;
      }
    })
  } else if (item.type === 2) {
    queryEchoMenuBtnInfo(item.id).then((res) => {
      if (res.code === 200) {
        menuBtn.value = res.data;
        isDialogBtn.value = true;
      }
    })
  }
}

//新增菜单弹框
const parentId = ref(null);
const handleAddMenu = (item) => {
  parentId.value = item.id;
  if (item.type === 0) {
    isDialogMenu.value = true;
  }
  if (item.type === 1) {
    isDialogBtn.value = true;
  }
};

//保存菜单
const handleSaveMenu = () => {
  menuFormRef.value.validate((valid) => {
    if (valid) {
      if (menu.value.id === null) {
        saveMenu(parentId.value, menu.value).then((res) => {
          if (res.code === 200) {
            isDialogMenu.value = false;
            parentId.value = null;
            ElMessage({
              message: "添加成功！",
              type: "success",
            });
            getAllMenuData();
          }
        });
      } else {
        if (isDirUpdate.value) {
          //一级目录修改
          updateMenuDirInfo(menu.value).then((res) => {
            if (res.code === 200) {
              isDialog.value = false;
              isDialogMenu.value = false;
              ElMessage({
                message: "修改成功！",
                type: "success",
              });
              getAllMenuData();
            }
          });
        } else {
          updateMenuInfo(menu.value).then((res) => {
            if (res.code === 200) {
              isDialogMenu.value = false;
              ElMessage({
                message: "修改成功！",
                type: "success",
              });
              getAllMenuData();
            }
          });
        }
      }
    }
  });
};

//保存菜单按钮
const handleSaveMenuBtn = () => {
  btnFormRef.value.validate((valid) => {
    if (valid) {
      if (menuBtn.value.id === null) {
        saveMenuBtn(parentId.value, menuBtn.value).then((res) => {
          if (res.code === 200) {
            isDialogBtn.value = false;
            parentId.value = null;
            ElMessage({
              message: "添加成功！",
              type: "success",
            });
            getAllMenuData();
          }
        });
      } else {
        updateMenuBtnInfo(menuBtn.value).then((res) => {
          if (res.code === 200) {
            isDialogBtn.value = false;
            ElMessage({
              message: "修改成功！",
              type: "success",
            });
            getAllMenuData();
          }
        });
      }
    }
  })
};

//删除菜单
const handleDelete = (id) => {
  deleteMenu(id).then((res) => {
    if (res.code === 200) {
      ElMessage({
        message: "删除成功！",
        type: "success",
      });
      getAllMenuData();
    }
  });
}

//表单验证
const formRef = ref();
const menuFormRef = ref();
const btnFormRef = ref();
const rules = ref({
  name: [
    {
      required: true,
      message: '请输入菜单名称',
      trigger: 'change',
    },
  ],
  path: [
    {
      required: true,
      message: '请输入路径',
      trigger: 'change',
    },
  ],
  title: [
    {
      required: true,
      message: '请输入标题',
      trigger: 'change',
    },
  ],
  button: [
    {
      required: true,
      message: '请输入权限标识符',
      trigger: 'change',
    },
  ],
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
</style>