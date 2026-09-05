<template>
  <!-- 整体布局:左侧功能菜单 + 右侧内容区(路由视图) -->
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="logo">WIMS 仓库库存管理系统</div>
      <el-menu :default-active="$route.path" router background-color="#1f2d3d" text-color="#bfcbd9" active-text-color="#409eff">
        <el-menu-item index="/products">商品管理</el-menu-item>
        <el-menu-item index="/stock">库存管理</el-menu-item>
        <el-menu-item index="/inbound">入库单</el-menu-item>
        <el-menu-item index="/outbound">出库单</el-menu-item>
        <el-menu-item index="/alert">库存预警</el-menu-item>
        <el-menu-item index="/statistics">今日统计</el-menu-item>
      </el-menu>
      <div class="footer-note">云原生技术实践 B · 第 7 章选做</div>
    </el-aside>
    <el-main class="main">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
// App 骨架:引入布局样式即可,页面由 router-view 渲染
</script>

<style>
body { margin: 0; font-family: "Helvetica Neue", Arial, "PingFang SC", "Microsoft YaHei", sans-serif; }
.layout { height: 100vh; }
.aside { background: #1f2d3d; }
.aside .logo { color: #fff; font-size: 16px; font-weight: 600; padding: 18px 16px; border-bottom: 1px solid #2d3d4f; }
.aside .footer-note { color: #7c8793; font-size: 12px; padding: 12px 16px; position: absolute; bottom: 0; }
.main { background: #f0f2f5; padding: 16px; }
.el-menu { border-right: none; }
</style>
