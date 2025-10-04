<template>
  <div>
    <el-result icon="success" title="Success Tip" sub-title="Please follow the instructions">
      <template #extra>
        <el-button type="primary">Back</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup>
import { ref,onMounted} from 'vue'
</script>
<style scoped lang='scss'>
</style>