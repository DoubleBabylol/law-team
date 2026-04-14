<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>{{ isEdit ? '编辑索赔事项' : '新增索赔事项' }}</span>
        <el-button @click="goBack">返回列表</el-button>
      </div>
    </template>

    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="160px"
      v-loading="loading"
    >
      <!-- 重大索赔事项 -->
      <el-divider content-position="left">重大索赔事项</el-divider>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务名称" prop="taskName">
            <el-input v-model="formData.taskName" placeholder="请输入任务名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务编码">
            <el-input v-model="formData.taskCode" disabled placeholder="自动生成" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="任务类型" prop="taskType">
            <el-select v-model="formData.taskType" placeholder="请选择任务类型" style="width: 100%">
              <el-option label="股份诉讼" value="股份诉讼" />
              <el-option label="集团诉讼" value="集团诉讼" />
              <el-option label="重大索赔" value="重大索赔" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法院文书号码" prop="courtDocumentNo">
            <el-input v-model="formData.courtDocumentNo" placeholder="请输入法院文书号码" maxlength="50" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 基本信息 -->
      <el-divider content-position="left">基本信息</el-divider>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="是否重大案件" prop="isMajorCase">
            <el-select v-model="formData.isMajorCase" placeholder="请选择" style="width: 100%">
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="是否理赔给付纠纷" prop="isClaimPaymentDispute">
            <el-select v-model="formData.isClaimPaymentDispute" placeholder="请选择" style="width: 100%">
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="是否退保纠纷" prop="isRefundDispute">
            <el-select v-model="formData.isRefundDispute" placeholder="请选择" style="width: 100%">
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="发生区间" prop="occurrencePeriod">
            <el-select v-model="formData.occurrencePeriod" placeholder="请选择发生区间" style="width: 100%">
              <el-option
                v-for="item in periodOptions"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 被索赔机构 -->
      <el-divider content-position="left">被索赔机构</el-divider>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="省" prop="claimedOrgProvince">
            <el-select
              v-model="formData.claimedOrgProvince"
              placeholder="请选择省"
              style="width: 100%"
              filterable
              @change="handleProvinceChange"
            >
              <el-option
                v-for="item in provinceOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="市" prop="claimedOrgCity">
            <el-select
              v-model="formData.claimedOrgCity"
              placeholder="请选择市"
              style="width: 100%"
              filterable
              @change="handleCityChange"
            >
              <el-option
                v-for="item in cityOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="区/县" prop="claimedOrgDistrict">
            <el-select
              v-model="formData.claimedOrgDistrict"
              placeholder="请选择区/县"
              style="width: 100%"
              filterable
              @change="handleDistrictChange"
            >
              <el-option
                v-for="item in districtOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="formData.claimedOrgDistrict === '其他'">
        <el-col :span="12">
          <el-form-item label="其他（补充说明）" prop="claimedOrgOther">
            <el-input v-model="formData.claimedOrgOther" placeholder="请输入具体区域信息" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="索赔机构" prop="claimOrg">
            <el-input v-model="formData.claimOrg" placeholder="请输入索赔机构" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 案件详情 -->
      <el-divider content-position="left">案件详情</el-divider>

      <el-form-item label="事实与理由" prop="factsAndReasons">
        <el-input
          v-model="formData.factsAndReasons"
          type="textarea"
          :rows="6"
          placeholder="请输入事实与理由"
          maxlength="1000"
          show-word-limit
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="起诉可能性评估" prop="prosecutionProbability">
            <el-select v-model="formData.prosecutionProbability" placeholder="请选择" style="width: 100%">
              <el-option label="可能性大" value="可能性大" />
              <el-option label="可能性小" value="可能性小" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="败诉可能性评估" prop="loseProbability">
            <el-select v-model="formData.loseProbability" placeholder="请选择" style="width: 100%">
              <el-option label="可能性大" value="可能性大" />
              <el-option label="可能性小" value="可能性小" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 附件 -->
      <el-divider content-position="left">附件</el-divider>

      <el-form-item label="附件">
        <el-upload
          :action="''"
          :auto-upload="false"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
          :file-list="fileList"
          multiple
        >
          <el-button type="primary">选择文件</el-button>
          <template #tip>
            <div class="el-upload__tip">单个文件不超过10MB</div>
          </template>
        </el-upload>
      </el-form-item>

      <!-- 操作按钮 -->
      <el-form-item>
        <el-button type="primary" :loading="submitLoading" @click="handleSave">
          {{ isEdit ? '保存修改' : '保存草稿' }}
        </el-button>
        <el-button type="success" :loading="submitLoading" @click="handleSaveAndSubmit">
          保存并提交审批
        </el-button>
        <el-button @click="goBack">取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules, UploadUserFile } from 'element-plus'
import { createLawsuitClaim, updateLawsuitClaim, getLawsuitClaimDetail, approveLawsuitClaim, uploadFile } from '@/api/lawsuitClaim'
import regionData from '@/data/regionData'
import type { RegionItem } from '@/data/regionData'

const router = useRouter()
const route = useRoute()
const formRef = ref<FormInstance>()
const loading = ref(false)
const submitLoading = ref(false)
const fileList = ref<UploadUserFile[]>([])

const isEdit = computed(() => !!route.params.id)
const claimId = computed(() => route.params.id as string)

// 表单数据
const formData = reactive({
  taskName: '',
  taskCode: '',
  taskType: '',
  courtDocumentNo: '',
  isMajorCase: '',
  isClaimPaymentDispute: '',
  isRefundDispute: '',
  occurrencePeriod: '',
  claimedOrgProvince: '',
  claimedOrgCity: '',
  claimedOrgDistrict: '',
  claimedOrgOther: '',
  claimOrg: '',
  factsAndReasons: '',
  prosecutionProbability: '',
  loseProbability: '',
  attachments: '',
})

// 表单验证规则
const formRules: FormRules = {
  taskName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }],
  taskType: [{ required: true, message: '请选择任务类型', trigger: 'change' }],
  courtDocumentNo: [{ max: 50, message: '不能超过50个字符', trigger: 'blur' }],
  isMajorCase: [{ required: true, message: '请选择是否重大案件', trigger: 'change' }],
  isClaimPaymentDispute: [{ required: true, message: '请选择是否理赔给付纠纷', trigger: 'change' }],
  isRefundDispute: [{ required: true, message: '请选择是否退保纠纷', trigger: 'change' }],
  occurrencePeriod: [{ required: true, message: '请选择发生区间', trigger: 'change' }],
  claimedOrgProvince: [{ required: true, message: '请选择省', trigger: 'change' }],
  claimedOrgCity: [{ required: true, message: '请选择市', trigger: 'change' }],
  claimedOrgDistrict: [{ required: true, message: '请选择区/县', trigger: 'change' }],
  claimOrg: [{ required: true, message: '请输入索赔机构', trigger: 'blur' }],
  factsAndReasons: [
    { required: true, message: '请输入事实与理由', trigger: 'blur' },
    { max: 1000, message: '不能超过1000个字符', trigger: 'blur' },
  ],
  prosecutionProbability: [{ required: true, message: '请选择起诉可能性评估', trigger: 'change' }],
  loseProbability: [{ required: true, message: '请选择败诉可能性评估', trigger: 'change' }],
}

// 发生区间选项：当季及之前5年
const periodOptions = computed(() => {
  const options: string[] = []
  const now = new Date()
  const currentYear = now.getFullYear()
  const currentMonth = now.getMonth() + 1
  const currentQuarter = Math.ceil(currentMonth / 3)
  const quarterNames = ['一', '二', '三', '四']

  for (let year = currentYear - 5; year <= currentYear; year++) {
    const maxQ = year === currentYear ? currentQuarter : 4
    for (let q = 1; q <= maxQ; q++) {
      options.push(`${year}年${quarterNames[q - 1]}季度`)
    }
  }
  return options.reverse()
})

// 省市区联动
const provinceOptions = computed<RegionItem[]>(() => regionData)

const cityOptions = ref<RegionItem[]>([])
const districtOptions = ref<RegionItem[]>([])

const handleProvinceChange = (val: string) => {
  formData.claimedOrgCity = ''
  formData.claimedOrgDistrict = ''
  formData.claimedOrgOther = ''
  districtOptions.value = []
  const province = regionData.find(p => p.value === val)
  cityOptions.value = province?.children || []
}

const handleCityChange = (val: string) => {
  formData.claimedOrgDistrict = ''
  formData.claimedOrgOther = ''
  const province = regionData.find(p => p.value === formData.claimedOrgProvince)
  const city = province?.children?.find(c => c.value === val)
  districtOptions.value = city?.children || []
}

const handleDistrictChange = (val: string) => {
  if (val !== '其他') {
    formData.claimedOrgOther = ''
  }
}

// 文件上传
const handleFileChange = (file: any) => {
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过10MB')
    return false
  }
}

const handleFileRemove = (_file: any) => {
  // 文件移除处理
}

// 上传所有文件并返回路径
const uploadAllFiles = async (): Promise<string> => {
  const paths: string[] = []
  if (formData.attachments) {
    // 保留已有的附件路径
    paths.push(...formData.attachments.split(',').filter(p => p))
  }
  for (const file of fileList.value) {
    if (file.raw) {
      try {
        const res = await uploadFile(file.raw)
        paths.push(res.data)
      } catch {
        ElMessage.error(`文件 ${file.name} 上传失败`)
      }
    }
  }
  return paths.join(',')
}

// 保存
const handleSave = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      const attachments = await uploadAllFiles()
      const submitData = { ...formData, attachments }
      delete (submitData as any).taskCode

      if (isEdit.value) {
        await updateLawsuitClaim(claimId.value, submitData)
        ElMessage.success('修改成功')
      } else {
        await createLawsuitClaim(submitData)
        ElMessage.success('保存成功')
      }
      goBack()
    } finally {
      submitLoading.value = false
    }
  })
}

// 保存并提交审批
const handleSaveAndSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitLoading.value = true
    try {
      const attachments = await uploadAllFiles()
      const submitData = { ...formData, attachments }
      delete (submitData as any).taskCode

      let savedId: string
      if (isEdit.value) {
        const res = await updateLawsuitClaim(claimId.value, submitData)
        savedId = res.data.id
      } else {
        const res = await createLawsuitClaim(submitData)
        savedId = res.data.id
      }

      // 提交审批
      await approveLawsuitClaim(savedId, { action: 'SUBMIT' })
      ElMessage.success('保存并提交审批成功')
      goBack()
    } finally {
      submitLoading.value = false
    }
  })
}

const goBack = () => {
  router.push('/lawsuit/claims')
}

// 编辑模式：加载数据
onMounted(async () => {
  if (isEdit.value) {
    loading.value = true
    try {
      const res = await getLawsuitClaimDetail(claimId.value)
      const data = res.data
      formData.taskName = data.taskName
      formData.taskCode = data.taskCode
      formData.taskType = data.taskType
      formData.courtDocumentNo = data.courtDocumentNo || ''
      formData.isMajorCase = data.isMajorCase
      formData.isClaimPaymentDispute = data.isClaimPaymentDispute
      formData.isRefundDispute = data.isRefundDispute
      formData.occurrencePeriod = data.occurrencePeriod
      formData.claimedOrgProvince = data.claimedOrgProvince
      formData.claimedOrgCity = data.claimedOrgCity
      formData.claimedOrgDistrict = data.claimedOrgDistrict
      formData.claimedOrgOther = data.claimedOrgOther || ''
      formData.claimOrg = data.claimOrg
      formData.factsAndReasons = data.factsAndReasons
      formData.prosecutionProbability = data.prosecutionProbability
      formData.loseProbability = data.loseProbability
      formData.attachments = data.attachments || ''

      // 联动加载市和区县选项
      handleProvinceChange(data.claimedOrgProvince)
      formData.claimedOrgCity = data.claimedOrgCity
      handleCityChange(data.claimedOrgCity)
      formData.claimedOrgDistrict = data.claimedOrgDistrict
    } finally {
      loading.value = false
    }
  }
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
