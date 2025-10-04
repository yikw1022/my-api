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
            <el-button type="success" @click="downLoadSdk">SDK下载</el-button>
          </el-space>
        </div>
      </div>

      <el-divider style="margin: 30px 0 30px 0" />

      <el-table :header-cell-style="headerStyle" :cell-style="cellStyle" v-loading="loading" element-loading-text="加载中"
        :data="tableData" style="width: 100%">
        <el-table-column fixed prop="name" label="名称" width="220" />
        <el-table-column show-overflow-tooltip prop="description" label="描述" width="200" />
        <el-table-column prop="method" label="请求方法" width="140">
          <template #default="scope">
            <el-tag v-if="scope.row.method === 'POST'" type="success">{{ scope.row.method }}</el-tag>
            <el-tag v-else type="primary">{{ scope.row.method }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="params" label="请求参数" width="200" />
        <el-table-column show-overflow-tooltip prop="requestHeader" label="请求头" width="200" />
        <el-table-column show-overflow-tooltip prop="responseHeader" label="响应头" width="200" />
        <el-table-column fixed="right" label="操作" width="110">
          <template #default="scope">
            <el-button v-has="`btn:interface:invoke`" size="small" :icon="CircleCheck" type="primary"
              @click="doInvoke(scope.row.id)">调用</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div style="margin-top: 20px">
        <el-pagination v-model:current-page="pageNo" :page-size="pageSize" :small="small" :disabled="disabled"
          :background="background" layout="total, prev, pager, next" :total="total" @change="handleChange" />
      </div>
    </el-card>

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
                <div style="margin-top: 15px;padding: 10px;">
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

    <!-- 调用接口抽屉 -->
    <el-drawer :show-close="false" :style="{ '--el-drawer-padding-primary': '0' }" @close="handleDrawerClose" size="50%"
      v-model="isDrawerSDK">
      <!-- 自定义头部 -->
      <template #header>
        <div class="drawer-header">
          <span>Java SDK下载</span>
        </div>
      </template>

      <template #default>
        <h1 class="sdk-description">准备Java SDK环境</h1>
        <div style="padding: 10px;font-size: 16px;">
          <highlightjs language="java" :code="xmlValue"></highlightjs>
        </div>
        <h1 class="sdk-description" style="margin-bottom: 15px;">配置身份认证</h1>
        <div style="padding: 0 0 0 30px;">
          <ul>
            <li style="margin-bottom: 25px">1、创建AccessKey</li>
            <li style="margin-bottom: 25px">2、添加apiclient-spring-boot-starter依赖</li>
            <li style="margin-bottom: 25px">3、Spring配置文件中配置应用key和密钥即可</li>
          </ul>
        </div>
        <div style="padding: 10px;font-size: 16px;">
          <highlightjs language="yaml" :code="yamlValue"></highlightjs>
        </div>
        <h1 class="sdk-description">示例代码</h1>
        <div style="padding: 10px;font-size: 16px;">
          <highlightjs language="java" :code="code"></highlightjs>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import {
  findInterfaceList,
  interfaceEchoById,
  onlineInvokeInterface,
} from "@/api/interfaceinfo";
import { ElMessage } from "element-plus";
import { CircleCheck } from "@element-plus/icons-vue";
import VueJsonPretty from 'vue-json-pretty';
import 'vue-json-pretty/lib/styles.css';
import XmlViewer from 'vue3-xml-viewer';

const loading = ref(true);
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
//SDK抽屉
const isDrawerSDK = ref(false);

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

//在线调用接口
const onlineInvoke = ref({
  id: null,
  accessKey: "",
  params: "",
  url: "",
});

onMounted(() => {
  getInterfaceData();
  loading.value = false;
});

//分页获取接口数据
const getInterfaceData = () => {
  findInterfaceList(pageNo.value, pageSize.value, searchDto.value).then(
    (res) => {
      if (res.code === 200 && res.data !== null) {
        tableData.value = res.data.data;
        total.value = res.data.total;
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
//获取接口详情
const doDetail = (id) => {
  interfaceDetailById(id).then((res) => {
    if (res.code === 200) {
      interfaceInfo.value = res.data;
      isDrawer.value = true;
    }
  });
};

//SDK下载弹框
const xmlValue = ref(`
<dependency>
    <groupId>io.gitee.six-key</groupId>
    <artifactId>apiclient-spring-boot-starter</artifactId>
    <version>2.0.0</version>
</dependency>`)
const downLoadSdk = () => {
  isDrawerSDK.value = true;
}

const yamlValue = ref(`
open:
  api:
    access-key: xxxxxxxxxxxx
    secret-key: xxxxxxxxxxxx`)

//示例代码
const code = ref(`
public class SimpleTest {

    /**
     * 自动注入客户端实例
     */
    @Autowired
    private OpenClient openClient;

    /**
     * 示例代码
     */
    public void simple(){
        //调用客户端内置接口
        String result = openClient.chineseEnglishTranslation("这是一次测试");
        System.out.println(result);
    }
}`)

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

.sdk-description {
  font-weight: 900;
  font-size: 23px;
  color: black;
  padding: 20px;
}
</style>