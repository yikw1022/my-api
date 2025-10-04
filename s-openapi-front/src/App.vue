<template>
  <router-view v-slot="{ Component }">
      <!-- 添加动画效果 -->
      <transition name="fade">
        <component :is="Component" />
      </transition>
    </router-view>
</template>

<style>
/* 设置根元素的高度为100% */
#app {
  height: 100%;
}

/* 设置进入动画效果 */
.fade-enter-active {
  transition: opacity 0.5s, transform 0.5s;
}

/* 设置从出动画效果 */
.fade-enter-from {
  opacity: 0;
  transform: scale(0.9);
}

.fade-enter-to {
  opacity: 1;
  transform: scale(1);
}
</style>