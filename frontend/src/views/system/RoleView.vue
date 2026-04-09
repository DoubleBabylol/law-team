<template>
  <div class="role-view">
    <el-card>
      <!-- 头部 -->
      <template #header>
        <div class="card-header">
          <span class="title">角色管理</span>
          <el-button type="primary" :icon="Plus" @click="handleAdd">
            新增角色
          </el-button>
        </div>
      </template>

      <!-- 角色列表 -->
      <el-table v-loading="loading" :data="roleList" border style="width: 100%">
        <el-table-column prop="roleId" label="角色 ID" min-width="180" />
        <el-table-column prop="roleName" label="角色名称" min-width="150" />
        <el-table-column label="角色类别" min-width="120">
          <template #default="{ row }">
            <el-tag :type="getRoleGroupType(row.roleGroup)">
              {{ row.roleGroup }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" min-width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="100">
          <template #default="{ row }">
            <el-tag :type="row.status ? 'success' : 'danger'">
              {{ row.status ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" min-width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button link type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑角色' : '新增角色'"
      width="500px"
      destroy-on-close
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input
            v-model="formData.roleName"
            placeholder="请输入角色名称"
            maxlength="20"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="角色类别" prop="roleGroup">
          <el-select
            v-model="formData.roleGroup"
            placeholder="请选择角色类别"
            style="width: 100%"
          >
            <el-option label="合规" value="合规" />
            <el-option label="督察" value="督察" />
            <el-option label="诉讼" value="诉讼" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="formData.status"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { getRoleList, addRole, updateRole, deleteRole } from '@/api/role'
import type { Role } from '@/types'

// 列表数据
const loading = ref(false)
const roleList = ref<Role[]>([])

// 弹窗相关
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

// 表单数据
const formData = reactive<{
  roleId?: string
  roleName: string
  roleGroup: string
  status: boolean
}>({
  roleName: '',
  roleGroup: '',
  status: true
})

// 表单校验规则
const formRules: FormRules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 20, message: '角色名称长度应为 2-20 字符', trigger: 'blur' }
  ],
  roleGroup: [
    { required: true, message: '请选择角色类别', trigger: 'change' }
  ]
}

// 获取角色类别标签类型
const getRoleGroupType = (roleGroup: string): string => {
  const typeMap: Record<string, string> = {
    '合规': '',
    '督察': 'warning',
    '诉讼': 'danger'
  }
  return typeMap[roleGroup] || ''
}

// 格式化日期时间
const formatDateTime = (dateStr: string): string => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 加载角色列表
const loadRoleList = async () => {
  loading.value = true
  try {
    const res = await getRoleList()
    roleList.value = res.data
  } catch (error) {
    console.error('加载角色列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 新增角色
const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑角色
const handleEdit = (row: Role) => {
  isEdit.value = true
  resetForm()
  formData.roleId = row.roleId
  formData.roleName = row.roleName
  formData.roleGroup = row.roleGroup
  formData.status = row.status
  dialogVisible.value = true
}

// 删除角色
const handleDelete = async (row: Role) => {
  try {
    await ElMessageBox.confirm('确认删除该角色吗？', '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await deleteRole(row.roleId)
    ElMessage.success('删除成功')
    loadRoleList()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

// 重置表单
const resetForm = () => {
  formData.roleId = undefined
  formData.roleName = ''
  formData.roleGroup = ''
  formData.status = true
  formRef.value?.resetFields()
}

// 弹窗关闭
const handleDialogClose = () => {
  resetForm()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      if (isEdit.value && formData.roleId) {
        await updateRole(formData.roleId, {
          roleName: formData.roleName,
          roleGroup: formData.roleGroup,
          status: formData.status
        })
        ElMessage.success('编辑成功')
      } else {
        await addRole({
          roleName: formData.roleName,
          roleGroup: formData.roleGroup,
          status: formData.status
        })
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      loadRoleList()
    } catch (error: any) {
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitLoading.value = false
    }
  })
}

// 页面加载
onMounted(() => {
  loadRoleList()
})
</script>

<style scoped>
.role-view {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 16px;
  font-weight: 600;
}
</style>
