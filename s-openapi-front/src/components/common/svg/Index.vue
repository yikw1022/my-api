<template>
    <!-- svg图标外层容器节点，需要内部配合use标签使用 -->
    <svg :style="{width,height}">
        <!-- xlink:href表示使用哪一个图标：注意：属性值务必：#icon-图标名字
            指定图标：xlink:href----：#icon-图标名字
            图标颜色：fill----：blue
            -->
        <use :xlink:href="prefix + name" :fill="color"></use>
    </svg>
</template>

<script setup>
import { ref,onMounted} from 'vue'

//接收父组件传递过来的参数
defineProps({
    //xlink:href属性值前缀
    prefix: {
        type: String,
        default: '#icon-'
    },
    //svg名字
    name: String,
    //颜色
    color: {
        type: String,
        default: ''
    },
    height: {
        type: String,
        default: '45px'
    },
    width: {
        type: String,
        default: '45px'
    },
})
</script>
<style scoped lang='scss'>
</style>