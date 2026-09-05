<template>
  <div>
    <el-card shadow="never">
      <div class="bar">
        <h3 style="margin: 0">入库单</h3>
        <el-button type="primary" @click="dialog.visible = true">新建入库单</el-button>
      </div>
      <!-- 入库单列表:经网关 /inbound/list(inbound-service) -->
      <el-table :data="rows" border stripe style="margin-top: 12px">
        <el-table-column prop="billNo" label="入库单号" min-width="180" />
        <el-table-column prop="productName" label="商品名称" min-width="140" />
        <el-table-column prop="quantity" label="数量" width="100" />
        <el-table-column prop="operator" label="经办人" width="120" />
        <el-table-column prop="inboundTime" label="入库时间" min-width="170" :formatter="fmtTime" />
      </el-table>
      <el-empty v-if="rows.length === 0" description="暂无入库单" />
    </el-card>

    <!-- 新建入库单:POST /inbound,触发库存增加(inbound-service 内 Feign 联动) -->
    <el-dialog v-model="dialog.visible" title="新建入库单" width="420px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="商品ID">
          <el-input v-model.number="form.productId" placeholder="如 1" />
        </el-form-item>
        <el-form-item label="入库数量">
          <el-input v-model.number="form.quantity" placeholder="如 100" />
        </el-form-item>
        <el-form-item label="经办人">
          <el-input v-model="form.operator" placeholder="如 张伟" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="submit">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { getInboundList, createInbound, errMsg } from '../api'
import { formatDate } from '../utils/date'

const rows = ref([])
const dialog = reactive({ visible: false })
const form = reactive({ productId: 1, quantity: 100, operator: '' })

const fmtTime = (row) => formatDate(row.inboundTime)

const load = async () => {
  try {
    const res = await getInboundList()
    rows.value = res.data || []
  } catch (e) {
    ElMessage.error(errMsg(e))
  }
}

const submit = async () => {
  if (!form.productId || !form.quantity || !form.operator) return ElMessage.warning('请填写完整信息')
  try {
    const res = await createInbound({ ...form })
    // 后端返回 Result{success,message} 结构
    if (res.data?.success === false) return ElMessage.error(res.data.message || '入库失败')
    ElMessage.success(res.data?.message || '入库成功')
    dialog.visible = false
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
