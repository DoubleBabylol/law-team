<template>
  <el-card>
    <!-- 搜索栏 -->
    <el-form inline class="search-form">
      <el-form-item>
        <el-input
          v-model="searchKeyword"
          placeholder="任务名称/编码"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchStatus" placeholder="审批状态" clearable style="width: 160px">
          <el-option label="草稿" value="DRAFT" />
          <el-option label="待总经办审批" value="PENDING_OFFICE_REVIEW" />
          <el-option label="待总审核" value="PENDING_FINAL_REVIEW" />
          <el-option label="已通过" value="APPROVED" />
          <el-option label="已驳回" value="REJECTED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
        <el-button :icon="Refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <div class="operation-bar">
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增索赔事项</el-button>
    </div>

    <!-- 列表 -->
    <el-table :data="claimList" v-loading="loading" stripe>
      <el-table-column prop="taskCode" label="任务编码" min-width="180" />
      <el-table-column prop="taskName" label="任务名称" min-width="150" />
      <el-table-column prop="taskType" label="任务类型" width="100" />
      <el-table-column prop="claimOrg" label="索赔机构" min-width="120" />
      <el-table-column label="被索赔机构" min-width="180">
        <template #default="{ row }">
          {{ row.claimedOrgProvince }} {{ row.claimedOrgCity !== '无' ? row.claimedOrgCity : '' }}
          {{ row.claimedOrgDistrict !== '无' ? row.claimedOrgDistrict : '' }}
        </template>
      </el-table-column>
      <el-table-column label="审批状态" width="130">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.approvalStatus)">
            {{ row.approvalStatusLabel }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdByName" label="创建人" width="100" />
      <el-table-column prop="createdAt" label="创建时间" min-width="160" />
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleView(row)">查看</el-button>
          <el-button
            v-if="row.approvalStatus === 'DRAFT' || row.approvalStatus === 'REJECTED'"
            link type="primary"
            @click="handleEdit(row)"
          >编辑</el-button>
          <el-button
            v-if="row.approvalStatus === 'DRAFT' || row.approvalStatus === 'REJECTED'"
            link type="success"
            @click="handleSubmitApproval(row)"
          >提交审批</el-button>
          <el-button
            v-if="row.approvalStatus === 'PENDING_OFFICE_REVIEW' || row.approvalStatus === 'PENDING_FINAL_REVIEW'"
            link type="success"
            @click="handleApprove(row)"
          >通过</el-button>
          <el-button
            v-if="row.approvalStatus === 'PENDING_OFFICE_REVIEW' || row.approvalStatus === 'PENDING_FINAL_REVIEW'"
            link type="danger"
            @click="handleReject(row)"
          >驳回</el-button>
          <el-button link type="info" @click="handleViewLogs(row)">审批记录</el-button>
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

    <!-- 驳回弹窗 -->
    <el-dialog v-model="rejectDialogVisible" title="驳回" width="500px" destroy-on-close>
      <el-form ref="rejectFormRef" :model="rejectForm" :rules="rejectRules" label-width="80px">
        <el-form-item label="驳回原因" prop="comment">
          <el-input
            v-model="rejectForm.comment"
            type="textarea"
            :rows="4"
            placeholder="请输入驳回原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="danger" :loading="approvalLoading" @click="confirmReject">确认驳回</el-button>
      </template>
    </el-dialog>

    <!-- 审批日志弹窗 -->
    <el-dialog v-model="logsDialogVisible" title="审批记录" width="700px" destroy-on-close>
      <el-table :data="approvalLogs" stripe>
        <el-table-column prop="operatorName" label="操作人" width="100" />
        <el-table-column label="操作类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getActionType(row.actionType)">
              {{ getActionLabel(row.actionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="comment" label="审批意见" min-width="200" />
        <el-table-column prop="createdAt" label="操作时间" width="170" />
      </el-table>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { getLawsuitClaimList, approveLawsuitClaim, getApprovalLogs as fetchApprovalLogs } from '@/api/lawsuitClaim'
import type { LawsuitClaim, ApprovalLog } from '@/types'

const router = useRouter()

// 搜索
const searchKeyword = ref('')
const searchStatus = ref('')
const loading = ref(false)

// 分页
const page = ref(1)
const size = ref(10)
const total = ref(0)
const claimList = ref<LawsuitClaim[]>([])

// 驳回弹窗
const rejectDialogVisible = ref(false)
const rejectFormRef = ref<FormInstance>()
const approvalLoading = ref(false)
const currentRejectId = ref('')
const rejectForm = ref({ comment: '' })
const rejectRules: FormRules = {
  comment: [{ required: true, message: '请输入驳回原因', trigger: 'blur' }],
}

// 审批日志
const logsDialogVisible = ref(false)
const approvalLogs = ref<ApprovalLog[]>([])

// 获取列表
const fetchList = async () => {
  loading.value = true
  try {
    const res = await getLawsuitClaimList({
      page: page.value,
      size: size.value,
      keyword: searchKeyword.value || undefined,
      approvalStatus: searchStatus.value || undefined,
    })
    claimList.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.value = 1
  fetchList()
}

const handleReset = () => {
  searchKeyword.value = ''
  searchStatus.value = ''
  page.value = 1
  fetchList()
}

const handleSizeChange = (val: number) => {
  size.value = val
  page.value = 1
  fetchList()
}

const handlePageChange = (val: number) => {
  page.value = val
  fetchList()
}

// 状态标签颜色
const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    DRAFT: 'info',
    PENDING_OFFICE_REVIEW: 'warning',
    PENDING_FINAL_REVIEW: 'warning',
    APPROVED: 'success',
    REJECTED: 'danger',
  }
  return map[status] || 'info'
}

const getActionType = (action: string) => {
  const map: Record<string, string> = {
    SUBMIT: '',
    APPROVE: 'success',
    REJECT: 'danger',
  }
  return map[action] || 'info'
}

const getActionLabel = (action: string) => {
  const map: Record<string, string> = {
    SUBMIT: '提交审批',
    APPROVE: '审批通过',
    REJECT: '驳回',
  }
  return map[action] || action
}

// 操作
const handleAdd = () => {
  router.push('/lawsuit/claims/add')
}

const handleView = (row: LawsuitClaim) => {
  router.push(`/lawsuit/claims/detail/${row.id}`)
}

const handleEdit = (row: LawsuitClaim) => {
  router.push(`/lawsuit/claims/edit/${row.id}`)
}

const handleSubmitApproval = (row: LawsuitClaim) => {
  ElMessageBox.confirm('确认提交审批吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'info',
  }).then(async () => {
    await approveLawsuitClaim(row.id, { action: 'SUBMIT' })
    ElMessage.success('提交审批成功')
    fetchList()
  })
}

const handleApprove = (row: LawsuitClaim) => {
  ElMessageBox.confirm('确认审批通过吗？', '提示', {
    confirmButtonText: '确认通过',
    cancelButtonText: '取消',
    type: 'success',
  }).then(async () => {
    await approveLawsuitClaim(row.id, { action: 'APPROVE' })
    ElMessage.success('审批通过')
    fetchList()
  })
}

const handleReject = (row: LawsuitClaim) => {
  currentRejectId.value = row.id
  rejectForm.value.comment = ''
  rejectDialogVisible.value = true
}

const confirmReject = async () => {
  if (!rejectFormRef.value) return
  await rejectFormRef.value.validate(async (valid) => {
    if (!valid) return
    approvalLoading.value = true
    try {
      await approveLawsuitClaim(currentRejectId.value, {
        action: 'REJECT',
        comment: rejectForm.value.comment,
      })
      ElMessage.success('已驳回')
      rejectDialogVisible.value = false
      fetchList()
    } finally {
      approvalLoading.value = false
    }
  })
}

const handleViewLogs = async (row: LawsuitClaim) => {
  try {
    const res = await fetchApprovalLogs(row.id)
    approvalLogs.value = res.data
    logsDialogVisible.value = true
  } catch {
    ElMessage.error('获取审批记录失败')
  }
}

onMounted(() => {
  fetchList()
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
