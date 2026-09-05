<template>
  <div>
    <el-card shadow="never">
      <div class="bar">
        <h3 style="margin: 0">商品管理</h3>
        <div>
          <el-input v-model="searchId" placeholder="按商品 ID 查询" style="width: 220px" clearable />
          <el-button type="primary" :icon="'Search'" style="margin-left: 8px" @click="doSearch">查询</el-button>
          <el-button @click="loadAll">全部商品</el-button>
        </div>
      </div>
      <!-- 商品档案列表:来自网关 /product/queryAllProduct(经 Feign 到 product-service) -->
      <el-table :data="rows" border stripe style="margin-top: 12px">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="productCode" label="商品编码" width="120" />
        <el-table-column prop="productName" label="商品名称" min-width="140" />
        <el-table-column prop="spec" label="规格" min-width="140" />
        <el-table-column prop="unit" label="单位" width="100" />
      </el-table>
      <el-empty v-if="rows.length === 0" description="暂无商品数据" />
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { queryAllProduct, findByProductId, errMsg } from '../api'

const rows = ref([])
const searchId = ref('')

// 查询全部商品
const loadAll = async () => {
  try {
    const res = await queryAllProduct()
    rows.value = res.data || []
  } catch (e) {
    ElMessage.error(errMsg(e))
  }
}

// 按 ID 单条查询(网关鉴权通过后,Feign 调用 product-service)
const doSearch = async () => {
  const id = String(searchId.value).trim()
  if (!id) return loadAll()
  // 后端接口参数是 Long:非数字会触发 Spring 派生 400,提前拦截
  if (!/^\d+$/.test(id)) return ElMessage.warning('请输入数字商品 ID')
  try {
    const res = await findByProductId(id)
    rows.value = res.data ? [res.data] : []
    if (!res.data) ElMessage.warning('未查询到该商品')
  } catch (e) {
    ElMessage.error(errMsg(e))
  }
}

loadAll()
</script>

<style scoped>
.bar { display: flex; justify-content: space-between; align-items: center; }
</style>
