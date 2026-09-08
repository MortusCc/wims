<template>
  <div>
    <el-card shadow="never">
      <div class="bar">
        <h3 style="margin: 0">库存预警</h3>
        <div>
          <el-button type="primary" @click="doCheck">检查预警</el-button>
          <el-button @click="load">刷新</el-button>
        </div>
      </div>
      <!-- 预警记录:经网关 /alert/list(alert-service),来自 alert 表 -->
      <el-table :data="rows" border stripe style="margin-top: 12px">
        <el-table-column prop="productId" label="商品ID" width="80" />
        <el-table-column prop="productName" label="商品名称" min-width="130" />
        <el-table-column prop="alertType" label="预警类型" width="110">
          <template #default="{ row }">
            <!-- 契约:后端 alert_type 取值为 'LOW'(库存不足) / 'HIGH'(库存过剩),见 sql/wims.sql -->
            <el-tag :type="row.alertType === 'LOW' ? 'danger' : 'warning'" size="small">{{ row.alertType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="curQuantity" label="当前库存" width="100" />
        <el-table-column prop="safeStock" label="安全库存" width="100" />
        <el-table-column prop="alertTime" label="预警时间" min-width="170" :formatter="fmtTime" />
        <el-table-column prop="status" label="状态" width="110">
          <template #default="{ row }">
            <!-- 契约:后端 status 为数字 0=未处理 1=已处理,见 AlertRecord.java / sql/wims.sql -->
            <el-badge :value="row.status === 1 ? '处理' : '待处理'" :type="row.status === 1 ? 'success' : 'danger'" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button v-if="row.status !== 1" size="small" type="success" @click="doDeal(row)">处理</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="rows.length === 0" description="暂无预警记录" />
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAlertList, checkAlert, dealAlert, errMsg } from '../api'
import { formatDate } from '../utils/date'

const rows = ref([])

const fmtTime = (row) => formatDate(row.alertTime)

const load = async () => {
  try {
    const res = await getAlertList()
    rows.value = res.data || []
  } catch (e) {
    ElMessage.error(errMsg(e))
  }
}

// 手动触发扫描:库存低于安全库存的商品生成预警记录
const doCheck = async () => {
  try {
    const res = await checkAlert()
    ElMessage.success(res.data?.message || '检查完成')
    load()
  } catch (e) {
    ElMessage.error(errMsg(e))
  }
}

// 处理预警:标记 alert 记录为已处理
const doDeal = async (row) => {
  await ElMessageBox.confirm(`确认处理商品「${row.productName}」的预警?`, '提示', { type: 'warning' })
  try {
    const res = await dealAlert(row.id)
    ElMessage.success(res.data?.message || '处理成功')
    load()
  } catch (e) {
    ElMessage.error(errMsg(e))
  }
}

load()
</script>

<style scoped>
.bar { display: flex; justify-content: space-between; align-items: center; }
</style>
