<template>
  <div>
    <el-card shadow="never">
      <div class="bar">
        <h3 style="margin: 0">今日统计</h3>
        <el-button @click="load">刷新</el-button>
      </div>
      <el-alert type="info" :closable="false" style="margin: 10px 0"
        title="统计日报:stocks 实时库存联结入库/出库流水,由 statistics-service 汇总(经网关 /statistics/showToday)" />
      <!-- 今日出入库汇总指标卡 -->
      <el-row :gutter="12" style="margin: 12px 0">
        <el-col :span="8">
          <el-statistic title="今日入库总量" :value="totalIn" />
        </el-col>
        <el-col :span="8">
          <el-statistic title="今日出库总量" :value="totalOut" />
        </el-col>
        <el-col :span="8">
          <el-statistic title="在库商品种类" :value="rows.length" />
        </el-col>
      </el-row>
      <!-- 每日汇总表(daily_summary 表,统计时点快照) -->
      <el-table :data="rows" border stripe>
        <el-table-column prop="productName" label="商品名称" min-width="140" />
        <el-table-column prop="inQty" label="今日入库" width="110" />
        <el-table-column prop="outQty" label="今日出库" width="110" />
        <el-table-column prop="stockQty" label="当前库存" width="110" />
        <el-table-column prop="statDate" label="统计日期" min-width="130" :formatter="fmtTime" />
      </el-table>
      <el-empty v-if="rows.length === 0" description="暂无统计数据" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { showTodayStatistics, errMsg } from '../api'
import { formatDate } from '../utils/date'

const rows = ref([])

const fmtTime = (row) => formatDate(row.statDate)

// 今日入库/出库总量:汇总各商品行的 inQty / outQty
const totalIn = computed(() => rows.value.reduce((s, r) => s + (r.inQty || 0), 0))
const totalOut = computed(() => rows.value.reduce((s, r) => s + (r.outQty || 0), 0))

const load = async () => {
  try {
    const res = await showTodayStatistics()
    rows.value = res.data || []
  } catch (e) {
    ElMessage.error(errMsg(e))
  }
}

load()
</script>

<style scoped>
.bar { display: flex; justify-content: space-between; align-items: center; }
</style>
