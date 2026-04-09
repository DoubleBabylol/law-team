<template>
  <div class="permission-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>权限配置</span>
        </div>
      </template>

      <el-table :data="roleList" v-loading="loading" border>
        <el-table-column prop="roleId" label="角色 ID" width="180" />
        <el-table-column prop="roleName" label="角色名称" width="180" />
        <el-table-column label="角色组" width="150">
          <template #default="{ row }">
            <el-tag :type="getRoleGroupType(row.roleGroup)">
              {{ row.roleGroup }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status ? 'success' : 'danger'">
              {{ row.status ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="菜单权限状态" width="150">
          <template #default="{ row }">
            <el-tag :type="getPermissionStatusType(row)">
              {{ getPermissionStatusText(row) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleConfigPermission(row)">
              配置权限
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 配置权限弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="配置权限"
      width="600px"
      :close-on-click-modal="false"
    >
      <div v-if="currentRole" class="permission-dialog-content">
        <!-- 顶部信息区 -->
        <div class="dialog-header-info">
          <div class="role-info">
            <span class="role-name">{{ currentRole.roleName }}</span>
            <el-tag :type="getRoleGroupType(currentRole.roleGroup)" size="small">
              {{ currentRole.roleGroup }}
            </el-tag>
          </div>
          <div class="permission-actions">
            <span class="selected-count">已选择 {{ selectedMenuCount }} 个菜单</span>
            <div class="switch-wrapper">
              <span class="switch-label">全部启用</span>
              <el-switch
                v-model="allEnabled"
                @change="handleAllEnabledChange"
              />
            </div>
          </div>
        </div>

        <!-- 菜单树区域 -->
        <div class="menu-tree-wrapper" v-loading="treeLoading">
          <el-tree
            ref="treeRef"
            :data="menuTree"
            node-key="resourceId"
            :props="{ label: 'resourceName', children: 'children' }"
            show-checkbox
            :check-strictly="false"
            default-expand-all
            @check-change="handleCheckChange"
          >
            <template #default="{ node, data }">
              <span class="custom-tree-node">
                <span class="node-label">{{ node.label }}</span>
                <el-tag size="small" type="info" class="node-type-tag">
                  {{ data.resourceType }}
                </el-tag>
              </span>
            </template>
          </el-tree>
        </div>
      </div>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { ElTree } from 'element-plus'
import { getRoleList } from '@/api/role'
import { getMenuTree } from '@/api/menu'
import { getRoleMenus, saveRoleMenus } from '@/api/permission'
import type { Role, Menu } from '@/types'

// 角色列表
const roleList = ref<Role[]>([])
const loading = ref(false)

// 菜单树
const menuTree = ref<Menu[]>([])
const allMenuIds = ref<string[]>([])
const allLeafMenuIds = ref<string[]>([])

// 弹窗相关
const dialogVisible = ref(false)
const currentRole = ref<Role | null>(null)
const treeLoading = ref(false)
const saveLoading = ref(false)
const allEnabled = ref(false)

// Tree 引用
const treeRef = ref<InstanceType<typeof ElTree>>()

// 角色权限状态缓存
const rolePermissionMap = ref<Map<string, string[]>>(new Map())

// 计算已选择的菜单数量
const selectedMenuCount = computed(() => {
  if (!treeRef.value) return 0
  return treeRef.value.getCheckedKeys().length
})

// 获取角色组标签类型
function getRoleGroupType(group: string): '' | 'warning' | 'danger' {
  switch (group) {
    case '督察':
      return 'warning'
    case '诉讼':
      return 'danger'
    case '合规':
    default:
      return ''
  }
}

// 获取权限状态文本
function getPermissionStatusText(role: Role): string {
  const menuIds = rolePermissionMap.value.get(role.roleId)
  if (!menuIds) return '加载中...'
  
  if (menuIds.length === 0) return '全部禁用'
  if (menuIds.length >= allMenuIds.value.length) return '全部启用'
  return '部分启用'
}

// 获取权限状态标签类型
function getPermissionStatusType(role: Role): 'success' | 'warning' | 'info' {
  const menuIds = rolePermissionMap.value.get(role.roleId)
  if (!menuIds) return 'info'
  
  if (menuIds.length === 0) return 'info'
  if (menuIds.length >= allMenuIds.value.length) return 'success'
  return 'warning'
}

// 提取所有菜单 ID（包括非叶子节点）
function extractAllMenuIds(menus: Menu[]): string[] {
  const ids: string[] = []
  function traverse(list: Menu[]) {
    for (const menu of list) {
      ids.push(menu.resourceId)
      if (menu.children && menu.children.length > 0) {
        traverse(menu.children)
      }
    }
  }
  traverse(menus)
  return ids
}

// 提取所有叶子菜单 ID
function extractLeafMenuIds(menus: Menu[]): string[] {
  const ids: string[] = []
  function traverse(list: Menu[]) {
    for (const menu of list) {
      if (!menu.children || menu.children.length === 0) {
        ids.push(menu.resourceId)
      } else {
        traverse(menu.children)
      }
    }
  }
  traverse(menus)
  return ids
}

// 判断是否为叶子节点
function isLeafNode(menuId: string, menus: Menu[]): boolean {
  function traverse(list: Menu[]): boolean {
    for (const menu of list) {
      if (menu.resourceId === menuId) {
        return !menu.children || menu.children.length === 0
      }
      if (menu.children && menu.children.length > 0) {
        const found = traverse(menu.children)
        if (found) return true
      }
    }
    return false
  }
  return traverse(menus)
}

// 加载角色列表
async function loadRoleList() {
  loading.value = true
  try {
    const res = await getRoleList()
    roleList.value = res.data
    
    // 加载每个角色的权限状态
    await loadAllRolePermissions()
  } finally {
    loading.value = false
  }
}

// 加载所有角色的权限（用于显示状态）
async function loadAllRolePermissions() {
  const promises = roleList.value.map(async (role) => {
    try {
      const res = await getRoleMenus(role.roleId)
      const menuIds = res.data.map(m => m.resourceId)
      rolePermissionMap.value.set(role.roleId, menuIds)
    } catch (error) {
      rolePermissionMap.value.set(role.roleId, [])
    }
  })
  await Promise.all(promises)
}

// 加载菜单树
async function loadMenuTree() {
  const res = await getMenuTree()
  menuTree.value = res.data
  allMenuIds.value = extractAllMenuIds(res.data)
  allLeafMenuIds.value = extractLeafMenuIds(res.data)
}

// 配置权限
async function handleConfigPermission(role: Role) {
  currentRole.value = role
  dialogVisible.value = true
  treeLoading.value = true
  
  try {
    // 加载菜单树（如果还没加载）
    if (menuTree.value.length === 0) {
      await loadMenuTree()
    }
    
    // 获取角色已分配的菜单
    const res = await getRoleMenus(role.roleId)
    const assignedMenuIds = res.data.map(m => m.resourceId)
    
    // 等待 DOM 更新后设置选中状态
    await nextTick()
    
    // 设置所有已分配的菜单为选中状态
    treeRef.value?.setCheckedKeys(assignedMenuIds)
    
    // 更新全部启用开关状态
    updateAllEnabledStatus()
  } finally {
    treeLoading.value = false
  }
}

// 更新全部启用开关状态
function updateAllEnabledStatus() {
  if (!treeRef.value) return
  const checkedCount = treeRef.value.getCheckedKeys().length
  allEnabled.value = checkedCount >= allLeafMenuIds.value.length
}

// 处理全部启用/禁用开关变化
async function handleAllEnabledChange(val: boolean) {
  if (val) {
    // 全部启用
    try {
      await ElMessageBox.confirm('确认全部启用？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      treeRef.value?.setCheckedKeys(allLeafMenuIds.value)
    } catch {
      allEnabled.value = false
    }
  } else {
    // 全部禁用
    try {
      await ElMessageBox.confirm('确认全部禁用？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      treeRef.value?.setCheckedKeys([])
    } catch {
      allEnabled.value = true
    }
  }
}

// 处理树节点勾选变化
function handleCheckChange() {
  updateAllEnabledStatus()
}

// 保存权限配置
async function handleSave() {
  if (!currentRole.value || !treeRef.value) return
  
  saveLoading.value = true
  try {
    // 获取选中的节点（包括半选中的父节点）
    const checkedKeys = treeRef.value.getCheckedKeys() as string[]
    const halfCheckedKeys = treeRef.value.getHalfCheckedKeys() as string[]
    const allSelectedIds = [...checkedKeys, ...halfCheckedKeys]
    
    await saveRoleMenus(currentRole.value.roleId, allSelectedIds)
    ElMessage.success('权限配置保存成功')
    
    // 更新缓存
    rolePermissionMap.value.set(currentRole.value.roleId, allSelectedIds)
    
    dialogVisible.value = false
  } catch (error) {
    // 错误已在 request 拦截器中处理
  } finally {
    saveLoading.value = false
  }
}

// 初始化
loadRoleList()
</script>

<style scoped>
.permission-page {
  padding: 20px;
}

.card-header {
  font-size: 16px;
  font-weight: 500;
}

.permission-dialog-content {
  .dialog-header-info {
    margin-bottom: 20px;
    padding-bottom: 16px;
    border-bottom: 1px solid #e4e7ed;

    .role-info {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 12px;

      .role-name {
        font-size: 16px;
        font-weight: 500;
      }
    }

    .permission-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .selected-count {
        color: #606266;
        font-size: 14px;
      }

      .switch-wrapper {
        display: flex;
        align-items: center;
        gap: 8px;

        .switch-label {
          font-size: 14px;
          color: #606266;
        }
      }
    }
  }

  .menu-tree-wrapper {
    max-height: 400px;
    overflow-y: auto;
    border: 1px solid #e4e7ed;
    border-radius: 4px;
    padding: 12px;

    .custom-tree-node {
      display: flex;
      align-items: center;
      gap: 6px;
      flex: 1;

      .node-label {
        flex: 1;
      }

      .node-type-tag {
        margin-left: 8px;
      }
    }
  }
}
</style>
