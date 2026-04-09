<template>
  <div class="dashboard-container">
    <!-- 欢迎区域 -->
    <el-card class="welcome-card" shadow="never">
      <div class="welcome-content">
        <el-avatar :size="64" :icon="UserFilled" class="welcome-avatar" />
        <div class="welcome-text">
          <h2 class="welcome-title">欢迎回来，{{ userStore.username }}！</h2>
          <p class="welcome-subtitle">今天是 {{ currentDate }}，祝您工作愉快！</p>
        </div>
      </div>
    </el-card>

    <!-- 统计卡片区域 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-icon user-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">1</div>
              <div class="stat-label">用户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-icon role-icon">
              <el-icon><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">6</div>
              <div class="stat-label">角色数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-icon menu-icon">
              <el-icon><Menu /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">19</div>
              <div class="stat-label">菜单数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-item">
            <div class="stat-icon branch-icon">
              <el-icon><OfficeBuilding /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">20</div>
              <div class="stat-label">机构数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷操作区域 -->
    <el-card class="quick-actions" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="header-title">快捷操作</span>
        </div>
      </template>
      <div class="action-buttons">
        <el-button type="primary" plain size="large" @click="goTo('/system/user')">
          <el-icon class="action-icon"><User /></el-icon>
          用户管理
        </el-button>
        <el-button type="success" plain size="large" @click="goTo('/system/role')">
          <el-icon class="action-icon"><UserFilled /></el-icon>
          角色管理
        </el-button>
        <el-button type="warning" plain size="large" @click="goTo('/system/menu')">
          <el-icon class="action-icon"><Menu /></el-icon>
          菜单管理
        </el-button>
        <el-button type="danger" plain size="large" @click="goTo('/permission/role')">
          <el-icon class="action-icon"><Lock /></el-icon>
          角色权限
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { User, UserFilled, Menu, OfficeBuilding } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const currentDate = computed(() => {
  const date = new Date()
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  const weekDay = weekDays[date.getDay()]
  return `${year}年${month}月${day}日 ${weekDay}`
})

const goTo = (path: string) => {
  router.push(path)
}
</script>

<style scoped>
.dashboard-container {
  padding: 0;
}

.welcome-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.welcome-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.welcome-avatar {
  background-color: #409eff;
}

.welcome-title {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 500;
  color: #303133;
}

.welcome-subtitle {
  margin: 0;
  font-size: 14px;
  color: #909399;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 8px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  color: #fff;
}

.user-icon {
  background-color: #409eff;
}

.role-icon {
  background-color: #67c23a;
}

.menu-icon {
  background-color: #e6a23c;
}

.branch-icon {
  background-color: #f56c6c;
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.quick-actions {
  border-radius: 8px;
}

.card-header {
  display: flex;
  align-items: center;
}

.header-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.action-buttons {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.action-icon {
  margin-right: 6px;
  font-size: 16px;
}
</style>
