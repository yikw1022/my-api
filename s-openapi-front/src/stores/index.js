// 导入名为 createPinia 的函数，来自 "pinia" 模块
import { createPinia } from "pinia";

// 使用 createPinia 函数创建一个名为 pinia 的应用程序级别的 store 实例
const pinia = createPinia();

// 导出 pinia 实例作为默认值
export default pinia;