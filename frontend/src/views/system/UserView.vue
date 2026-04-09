<template>
  <el-card>
    <!-- 搜索栏 -->
    <el-form inline class="search-form">
      <el-form-item>
        <el-input
          v-model="searchKeyword"
          placeholder="用户名/工号"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
        <el-button :icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增用户</el-button>
    </div>

    <!-- 用户列表 -->
    <el-table :data="userList" v-loading="loading" stripe>
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column prop="clerkNo" label="工号" min-width="100" />
      <el-table-column prop="branchName" label="机构名" min-width="150" />
      <el-table-column prop="deptName" label="部门名称" min-width="120" />
      <el-table-column label="角色" min-width="180">
        <template #default="{ row }">
          <el-tag
            v-for="role in row.roles"
            :key="role.roleId"
            size="small"
            style="margin-right: 5px; margin-bottom: 3px"
          >
            {{ role.roleName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status ? 'success' : 'danger'">
            {{ row.status ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" min-width="160" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="warning" @click="handleResetPassword(row)">重置密码</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="page"
      v-model:page-size="size"
      :page-sizes="[10, 20, 50, 100]"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      class="pagination"
      @size-change="handleSizeChange"
      @current-change="handlePageChange"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="请输入用户名" maxlength="20" />
        </el-form-item>
        <el-form-item label="工号" prop="clerkNo">
          <el-input v-model="formData.clerkNo" placeholder="请输入工号" maxlength="20" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="机构" prop="branchNo">
          <el-select
            v-model="formData.branchNo"
            placeholder="请选择机构"
            filterable
            style="width: 100%"
            @change="handleBranchChange"
          >
            <el-option
              v-for="item in branchOptions"
              :key="item.branchNo"
              :label="item.branchName"
              :value="item.branchNo"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-select
            v-model="formData.deptName"
            placeholder="请选择部门"
            style="width: 100%"
            @change="handleDeptChange"
          >
            <el-option
              v-for="item in deptOptions"
              :key="item.deptNo"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="部门号">
          <el-input v-model="formData.deptNo" disabled />
        </el-form-item>
        <el-form-item label="用户角色" prop="roleIds">
          <el-select
            v-model="formData.roleIds"
            placeholder="请选择角色"
            multiple
            style="width: 100%"
          >
            <el-option
              v-for="item in roleOptions"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="formData.status" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { getUserList, addUser, updateUser, deleteUser, resetPassword } from '@/api/user'
import { getRoleList } from '@/api/role'
import { getBranchList } from '@/api/branch'
import type { User, Role, Branch } from '@/types'

// 搜索相关
const searchKeyword = ref('')
const loading = ref(false)

// 分页相关
const page = ref(1)
const size = ref(10)
const total = ref(0)
const userList = ref<User[]>([])

// 弹窗相关
const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref<FormInstance>()
const currentUserId = ref('')

// 选项数据
const roleOptions = ref<Role[]>([])
const branchOptions = ref<Branch[]>([])

// 部门选项
const deptOptions = [
  { label: '合规部', value: '合规部', deptNo: 'D001' },
  { label: '督察部', value: '督察部', deptNo: 'D002' },
  { label: '诉讼部', value: '诉讼部', deptNo: 'D003' },
  { label: '财务部', value: '财务部', deptNo: 'D004' },
  { label: '人力资源部', value: '人力资源部', deptNo: 'D005' },
]

// 表单数据
const formData = reactive({
  username: '',
  clerkNo: '',
  branchNo: '',
  branchName: '',
  deptName: '',
  deptNo: '',
  roleIds: [] as string[],
  status: true,
})

// 表单校验规则
const formRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
  ],
  clerkNo: [
    { required: true, message: '请输入工号', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
  ],
  branchNo: [{ required: true, message: '请选择机构', trigger: 'change' }],
  deptName: [{ required: true, message: '请选择部门', trigger: 'change' }],
  roleIds: [
    { required: true, message: '请选择至少一个角色', trigger: 'change' },
    { type: 'array', min: 1, message: '请选择至少一个角色', trigger: 'change' },
  ],
}

// 获取用户列表
const fetchUserList = async () => {
  loading.value = true
  try {
    const res = await getUserList({
      page: page.value,
      size: size.value,
      keyword: searchKeyword.value || undefined,
    })
    userList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

// 获取角色列表
const fetchRoleList = async () => {
  try {
    const res = await getRoleList()
    roleOptions.value = res.data
  } catch (error) {
    console.error('获取角色列表失败', error)
  }
}

// 获取机构列表
const fetchBranchList = async () => {
  try {
    const res = await getBranchList()
    // 排除总公司（level=1 的不显示）
    branchOptions.value = res.data.filter((item) => item.level !== 1)
  } catch (error) {
    console.error('获取机构列表失败', error)
  }
}

// 机构选择变化
const handleBranchChange = (branchNo: string) => {
  const branch = branchOptions.value.find((item) => item.branchNo === branchNo)
  if (branch) {
    formData.branchName = branch.branchName
  }
}

// 部门选择变化
const handleDeptChange = (deptName: string) => {
  const dept = deptOptions.find((item) => item.value === deptName)
  if (dept) {
    formData.deptNo = dept.deptNo
  }
}

// 搜索
const handleSearch = () => {
  page.value = 1
  fetchUserList()
}

// 重置搜索
const handleReset = () => {
  searchKeyword.value = ''
  page.value = 1
  fetchUserList()
}

// 分页变化
const handleSizeChange = (val: number) => {
  size.value = val
  page.value = 1
  fetchUserList()
}

const handlePageChange = (val: number) => {
  page.value = val
  fetchUserList()
}

// 重置表单
const resetForm = () => {
  formData.username = ''
  formData.clerkNo = ''
  formData.branchNo = ''
  formData.branchName = ''
  formData.deptName = ''
  formData.deptNo = ''
  formData.roleIds = []
  formData.status = true
}

// 新增用户
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增用户'
  resetForm()
  dialogVisible.value = true
}

// 编辑用户
const handleEdit = (row: User) => {
  isEdit.value = true
  dialogTitle.value = '编辑用户'
  currentUserId.value = row.userId
  formData.username = row.username
  formData.clerkNo = row.clerkNo
  formData.branchNo = row.branchNo
  formData.branchName = row.branchName
  formData.deptName = row.deptName
  formData.deptNo = row.deptNo
  formData.roleIds = row.roleIds || row.roles?.map((r) => r.roleId) || []
  formData.status = row.status
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      const submitData = {
        username: formData.username,
        clerkNo: formData.clerkNo,
        branchNo: formData.branchNo,
        branchName: formData.branchName,
        deptName: formData.deptName,
        deptNo: formData.deptNo,
        roleIds: formData.roleIds,
        status: formData.status,
      }
      if (isEdit.value) {
        await updateUser(currentUserId.value, submitData)
        ElMessage.success('更新成功')
      } else {
        await addUser(submitData)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      fetchUserList()
    } finally {
      submitLoading.value = false
    }
  })
}

// 删除用户
const handleDelete = (row: User) => {
  ElMessageBox.confirm('确认删除该用户吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    await deleteUser(row.userId)
    ElMessage.success('删除成功')
    fetchUserList()
  })
}

// 重置密码
const handleResetPassword = (row: User) => {
  ElMessageBox.confirm('确认将该用户密码重置为 123456 吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    await resetPassword(row.userId)
    ElMessage.success('密码重置成功')
  })
}

onMounted(() => {
  fetchUserList()
  fetchRoleList()
  fetchBranchList()
})
</script>

<style scoped>
.search-form {
  margin-bottom: 20px;
}

.operation-bar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
