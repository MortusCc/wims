<template>
  <div>
    <el-card shadow="never">
      <div class="bar">
        <h3 style="margin: 0">库存管理</h3>
        <div>
          <el-button type="primary" @click="openDialog('increase')">增加库存</el-button>
          <el-button type="warning" @click="openDialog('decrease')">扣减库存</el-button>
          <el-button @click="load">刷新</el-button>
        </div>
      </div>
      <!-- 库存台账:经网关 /stock/list(inventory-service),低于安全库存的行标红 -->
      <el-table :data="rows" border stripe style="margin-top: 12px">
        <el-table-column prop="productId" label="商品ID" width="80" />
        <el-table-column prop="productName" label="商品名称" min-width="140" />
        <el-table-column prop="quantity" label="当前库存" width="120">
          <template #default="{ row }">
            <el-tag :type="row.quantity < row.safeStock ? 'danger' : 'success'" size="small">
              {{ row.quantity }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="safeStock" label="安全库存" width="110" />
        <el-table-column prop="updateTime" label="更新时间" min-width="170" :formatter="fmtTime" />
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="row.quantity < row.safeStock ? 'danger' : 'info'" size="small">
              {{ row.quantity < row.safeStock ? '库存不足' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 库存增减对话框:POST /stock/increase 或 /stock/decrease -->
    <el-dialog v-model="dialog.visible" :title="dialog.type === 'increase' ? '增加库存' : '扣减库存'" width="400px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="商品ID">
          <el-input v-model.number="form.productId" placeholder="如 1" />
        </el-form-item>
        <el-form-item label="数量">
          <el-input v-model.number="form.quantity" placeholder="如 20" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { getStockList, increaseStock, decreaseStock, errMsg } from '../api'
import { formatDate } from '../utils/date'

const rows = ref([])
const dialog = reactive({ visible: false, type: 'increase' })
const form = reactive({ productId: 1, quantity: 10 })

const fmtTime = (row) => formatDate(row.updateTime)

const load = async () => {
  try {
    const res = await getStockList()
    rows.value = res.data || []
  } catch (e) {
    ElMessage.error(errMsg(e))
  }
}

const openDialog = (type) => {
  dialog.type = type
  dialog.visible = true
}

// 提交后由库存服务的 SQL 约束保证不足拦截(扣减时 where quantity >= ?)
const submit = async () => {
  if (!form.productId || !form.quantity) return ElMessage.warning('请填写商品ID与数量')
  const fn = dialog.type === 'increase' ? increaseStock : decreaseStock
  try {
    const res = await fn({ productId: form.productId, quantity: form.quantity })
    ElMessage.success(res.data?.message || '操作成功')
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
