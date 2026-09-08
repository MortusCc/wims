<template>
  <div>
    <el-card shadow="never">
      <div class="bar">
        <h3 style="margin: 0">今日统计</h3>
        <el-button @click="load">刷新</el-button>
      </div>
      <el-alert type="info" :closable="false" style="margin: 10px 0"
        title="统计日报:stocks 实时库存联结入库/出库流水,由 statistics-service 汇总(经网关 /statistics/daily 触发生成,本页进入/刷新时重算当日日报)" />
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
        <!-- statDate 是 "yyyy-MM-dd" 字符串,原样显示即可(走 formatDate 会按 UTC 解析成 08:00:00,日期对但显示噪音大) -->
        <el-table-column prop="statDate" label="统计日期" min-width="130" />
      </el-table>
      <el-empty v-if="rows.length === 0" description="暂无统计数据" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { genDailyReport, errMsg } from '../api'

const rows = ref([])

// 今日入库/出库总量:汇总各商品行的 inQty / outQty
const totalIn = computed(() => rows.value.reduce((s, r) => s + (r.inQty || 0), 0))
const totalOut = computed(() => rows.value.reduce((s, r) => s + (r.outQty || 0), 0))

const load = async () => {
  try {
    // 先触发生成:Feign 聚合入库/出库/库存三份数据 → 写入 daily_summary(幂等覆盖)
    // /statistics/daily 返回的就是"今日日报行",与只读接口 showToday 数据形态一致
    const res = await genDailyReport()
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
