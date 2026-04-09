<template>
  <div v-loading="loading" class="menu-view">
    <el-card>
      <template #header>
        <div class="card-header">
          <span class="title">菜单管理</span>
          <el-button type="primary" :icon="Plus" @click="openAddDirectoryDialog">
            新增目录
          </el-button>
        </div>
      </template>

      <el-table
        :data="menuTree"
        row-key="resourceId"
        :tree-props="{ children: 'children' }"
        border
        style="width: 100%"
      >
        <el-table-column prop="resourceName" label="菜单名称" min-width="200" />

        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.resourceType)">
              {{ row.resourceType }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="resourcePath" label="路径" min-width="150" />

        <el-table-column prop="permission" label="权限标识" min-width="150" />

        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status ? 'success' : 'danger'">
              {{ row.status ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEditDialog(row)">
              编辑
            </el-button>
            <el-button
              v-if="row.resourceType !== '按钮'"
              link
              type="success"
              @click="openAddChildDialog(row)"
            >
              添加子项
            </el-button>
            <el-button link type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增目录弹窗 -->
    <el-dialog
      v-model="directoryDialogVisible"
      title="新增目录"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="directoryFormRef"
        :model="directoryForm"
        :rules="directoryRules"
        label-width="100px"
      >
        <el-form-item label="菜单名称" prop="resourceName">
          <el-input v-model="directoryForm.resourceName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="路径" prop="resourcePath">
          <el-input v-model="directoryForm.resourcePath" placeholder="请输入路径" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-select v-model="directoryForm.icon" placeholder="请选择图标" clearable style="width: 100%">
            <el-option
              v-for="icon in iconOptions"
              :key="icon"
              :label="icon"
              :value="icon"
            >
              <div class="icon-option">
                <component :is="icon" />
                <span>{{ icon }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="directoryForm.status" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="directoryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDirectoryForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 添加子项弹窗 -->
    <el-dialog
      v-model="childDialogVisible"
      title="添加子项"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="childFormRef"
        :model="childForm"
        :rules="childRules"
        label-width="100px"
      >
        <el-form-item label="父节点">
          <el-input v-model="childForm.parentName" disabled />
        </el-form-item>
        <el-form-item label="菜单名称" prop="resourceName">
          <el-input v-model="childForm.resourceName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="childForm.resourceType" disabled />
        </el-form-item>
        <el-form-item
          v-if="childForm.resourceType !== '按钮'"
          label="路径"
          prop="resourcePath"
        >
          <el-input v-model="childForm.resourcePath" placeholder="请输入路径" />
        </el-form-item>
        <el-form-item
          v-if="childForm.resourceType === '按钮'"
          label="权限标识"
          prop="permission"
        >
          <el-input v-model="childForm.permission" placeholder="如 system:role:add" />
        </el-form-item>
        <el-form-item
          v-if="childForm.resourceType !== '按钮'"
          label="图标"
          prop="icon"
        >
          <el-select v-model="childForm.icon" placeholder="请选择图标" clearable style="width: 100%">
            <el-option
              v-for="icon in iconOptions"
              :key="icon"
              :label="icon"
              :value="icon"
            >
              <div class="icon-option">
                <component :is="icon" />
                <span>{{ icon }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="childForm.sortOrder" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="childForm.status" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="childDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitChildForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 编辑弹窗 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑菜单"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="100px"
      >
        <el-form-item label="菜单类型">
          <el-radio-group v-model="editForm.resourceType" disabled>
            <el-radio-button label="目录" />
            <el-radio-button label="菜单" />
            <el-radio-button label="按钮" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="resourceName">
          <el-input v-model="editForm.resourceName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item
          v-if="editForm.resourceType !== '按钮'"
          label="路径"
          prop="resourcePath"
        >
          <el-input v-model="editForm.resourcePath" placeholder="请输入路径" />
        </el-form-item>
        <el-form-item
          v-if="editForm.resourceType === '按钮'"
          label="权限标识"
          prop="permission"
        >
          <el-input v-model="editForm.permission" placeholder="如 system:role:add" />
        </el-form-item>
        <el-form-item
          v-if="editForm.resourceType !== '按钮'"
          label="图标"
          prop="icon"
        >
          <el-select v-model="editForm.icon" placeholder="请选择图标" clearable style="width: 100%">
            <el-option
              v-for="icon in iconOptions"
              :key="icon"
              :label="icon"
              :value="icon"
            >
              <div class="icon-option">
                <component :is="icon" />
                <span>{{ icon }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="editForm.sortOrder" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="editForm.status" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getMenuTree, addMenu, updateMenu, deleteMenu } from '@/api/menu'
import type { Menu } from '@/types'

// 图标选项
const iconOptions = [
  'Monitor',
  'Setting',
  'UserFilled',
  'User',
  'Menu',
  'Lock',
  'Key',
  'Document',
  'Folder',
  'Star',
  'Bell',
  'Calendar',
  'ChatDotRound',
  'Connection',
  'Cpu',
  'DataLine',
  'Files',
  'Flag',
  'House',
  'Location'
]

// 加载状态
const loading = ref(false)

// 菜单树数据
const menuTree = ref<Menu[]>([])

// 获取菜单树
const fetchMenuTree = async () => {
  loading.value = true
  try {
    const res = await getMenuTree()
    menuTree.value = res.data
  } catch (error) {
    console.error('获取菜单树失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取类型标签样式
const getTypeTagType = (type: string) => {
  switch (type) {
    case '目录':
      return ''
    case '菜单':
      return 'success'
    case '按钮':
      return 'warning'
    default:
      return ''
  }
}

// ========== 新增目录 ==========
const directoryDialogVisible = ref(false)
const directoryFormRef = ref<FormInstance>()
const directoryForm = reactive({
  resourceName: '',
  resourcePath: '',
  icon: '',
  status: true,
  parentResourceId: null as string | null,
  resourceType: '目录',
  sortOrder: 0
})

const directoryRules: FormRules = {
  resourceName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ]
}

const openAddDirectoryDialog = () => {
  directoryForm.resourceName = ''
  directoryForm.resourcePath = ''
  directoryForm.icon = ''
  directoryForm.status = true
  directoryForm.parentResourceId = null
  directoryForm.resourceType = '目录'
  directoryForm.sortOrder = 0
  directoryDialogVisible.value = true
}

const submitDirectoryForm = async () => {
  if (!directoryFormRef.value) return
  await directoryFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await addMenu({ ...directoryForm })
        ElMessage.success('新增目录成功')
        directoryDialogVisible.value = false
        fetchMenuTree()
      } catch (error) {
        console.error('新增目录失败:', error)
      }
    }
  })
}

// ========== 添加子项 ==========
const childDialogVisible = ref(false)
const childFormRef = ref<FormInstance>()
const childForm = reactive({
  resourceName: '',
  resourcePath: '',
  permission: '',
  icon: '',
  sortOrder: 0,
  status: true,
  parentResourceId: '',
  parentName: '',
  resourceType: ''
})

const childRules: FormRules = {
  resourceName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  permission: [
    { required: true, message: '请输入权限标识', trigger: 'blur' }
  ]
}

const openAddChildDialog = (row: Menu) => {
  childForm.resourceName = ''
  childForm.resourcePath = ''
  childForm.permission = ''
  childForm.icon = ''
  childForm.sortOrder = 0
  childForm.status = true
  childForm.parentResourceId = row.resourceId
  childForm.parentName = row.resourceName
  // 根据父节点类型决定子节点类型
  if (row.resourceType === '目录') {
    childForm.resourceType = '菜单'
  } else if (row.resourceType === '菜单') {
    childForm.resourceType = '按钮'
  }
  childDialogVisible.value = true
}

const submitChildForm = async () => {
  if (!childFormRef.value) return
  await childFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const { parentName, ...submitData } = childForm
        await addMenu(submitData)
        ElMessage.success('添加子项成功')
        childDialogVisible.value = false
        fetchMenuTree()
      } catch (error) {
        console.error('添加子项失败:', error)
      }
    }
  })
}

// ========== 编辑菜单 ==========
const editDialogVisible = ref(false)
const editFormRef = ref<FormInstance>()
const editForm = reactive({
  resourceId: '',
  resourceName: '',
  resourcePath: '',
  permission: '',
  icon: '',
  sortOrder: 0,
  status: true,
  parentResourceId: null as string | null,
  resourceType: ''
})

const editRules: FormRules = {
  resourceName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  permission: [
    { required: true, message: '请输入权限标识', trigger: 'blur' }
  ]
}

const openEditDialog = (row: Menu) => {
  editForm.resourceId = row.resourceId
  editForm.resourceName = row.resourceName
  editForm.resourcePath = row.resourcePath
  editForm.permission = row.permission
  editForm.icon = row.icon
  editForm.sortOrder = row.sortOrder
  editForm.status = row.status
  editForm.parentResourceId = row.parentResourceId
  editForm.resourceType = row.resourceType
  editDialogVisible.value = true
}

const submitEditForm = async () => {
  if (!editFormRef.value) return
  await editFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const { resourceId, ...submitData } = editForm
        await updateMenu(resourceId, submitData)
        ElMessage.success('编辑菜单成功')
        editDialogVisible.value = false
        fetchMenuTree()
      } catch (error) {
        console.error('编辑菜单失败:', error)
      }
    }
  })
}

// ========== 删除菜单 ==========
const handleDelete = (row: Menu) => {
  ElMessageBox.confirm('确认删除该菜单及其所有子节点吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMenu(row.resourceId)
      ElMessage.success('删除成功')
      fetchMenuTree()
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => {
    // 取消删除
  })
}

onMounted(() => {
  fetchMenuTree()
})
</script>

<style scoped>
.menu-view {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 16px;
  font-weight: bold;
}

.icon-option {
  display: flex;
  align-items: center;
  gap: 10px;
}

.icon-option .el-icon {
  font-size: 18px;
  color: #606266;
}
</style>
