#!/bin/bash
# ============================================================
# 省级录入审批流程 - 场景测试脚本
# 测试覆盖：
#   场景1: 完整通过流程 (草稿→省经办审批→省审核→已通过)
#   场景2: 省经办驳回 → 重新提交 → 全流程通过
#   场景3: 省审核驳回 → 重新提交 → 全流程通过
#   场景4: 异常操作 (状态不匹配的操作)
#   场景5: 查询与日志验证
# ============================================================

BASE_URL="http://localhost:8080"
TOKEN=""
PASS=0
FAIL=0
TOTAL=0

# ===== 颜色输出 =====
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

log_info()  { echo -e "${CYAN}[INFO]${NC} $1"; }
log_pass()  { echo -e "${GREEN}[PASS]${NC} $1"; PASS=$((PASS+1)); TOTAL=$((TOTAL+1)); }
log_fail()  { echo -e "${RED}[FAIL]${NC} $1"; FAIL=$((FAIL+1)); TOTAL=$((TOTAL+1)); }
log_scene() { echo -e "\n${YELLOW}========== $1 ==========${NC}"; }

# ===== 断言函数 =====
assert_status() {
    local response="$1"
    local expected_code="$2"
    local test_name="$3"
    local actual_code
    actual_code=$(echo "$response" | python3 -c "import sys,json; print(json.load(sys.stdin).get('code',''))" 2>/dev/null)
    if [ "$actual_code" = "$expected_code" ]; then
        log_pass "$test_name (code=$actual_code)"
    else
        log_fail "$test_name (期望code=$expected_code, 实际code=$actual_code)"
        echo "  响应: $response"
    fi
}

assert_approval_status() {
    local response="$1"
    local expected_status="$2"
    local test_name="$3"
    local actual_status
    actual_status=$(echo "$response" | python3 -c "import sys,json; print(json.load(sys.stdin).get('data',{}).get('approvalStatus',''))" 2>/dev/null)
    if [ "$actual_status" = "$expected_status" ]; then
        log_pass "$test_name (approvalStatus=$actual_status)"
    else
        log_fail "$test_name (期望=$expected_status, 实际=$actual_status)"
        echo "  响应: $response"
    fi
}

assert_approval_path() {
    local response="$1"
    local expected_path="$2"
    local test_name="$3"
    local actual_path
    actual_path=$(echo "$response" | python3 -c "import sys,json; print(json.load(sys.stdin).get('data',{}).get('approvalPath',''))" 2>/dev/null)
    if [ "$actual_path" = "$expected_path" ]; then
        log_pass "$test_name (approvalPath=$actual_path)"
    else
        log_fail "$test_name (期望=$expected_path, 实际=$actual_path)"
    fi
}

assert_contains() {
    local response="$1"
    local keyword="$2"
    local test_name="$3"
    if echo "$response" | grep -q "$keyword"; then
        log_pass "$test_name"
    else
        log_fail "$test_name (响应中未包含: $keyword)"
    fi
}

assert_log_count() {
    local response="$1"
    local expected_min="$2"
    local test_name="$3"
    local count
    count=$(echo "$response" | python3 -c "import sys,json; d=json.load(sys.stdin).get('data',[]); print(len(d) if isinstance(d,list) else 0)" 2>/dev/null)
    if [ "$count" -ge "$expected_min" ] 2>/dev/null; then
        log_pass "$test_name (日志条数=$count, 期望>=$expected_min)"
    else
        log_fail "$test_name (日志条数=$count, 期望>=$expected_min)"
    fi
}

# ===== 创建索赔事项（省级录入路径）=====
create_province_claim() {
    local suffix="${1:-}"
    curl -s -X POST "$BASE_URL/api/lawsuit-claims" \
        -H "Authorization: Bearer $TOKEN" \
        -H "Content-Type: application/json" \
        -d "{
            \"taskName\": \"省级测试索赔事项${suffix}\",
            \"taskType\": \"诉讼\",
            \"courtDocumentNo\": \"(2026)京0105民初${RANDOM}号\",
            \"isMajorCase\": \"否\",
            \"isClaimPaymentDispute\": \"是\",
            \"isRefundDispute\": \"否\",
            \"occurrencePeriod\": \"2026-01\",
            \"claimedOrgProvince\": \"北京市\",
            \"claimedOrgCity\": \"北京市\",
            \"claimedOrgDistrict\": \"朝阳区\",
            \"claimedOrgOther\": \"\",
            \"claimOrg\": \"测试索赔机构\",
            \"factsAndReasons\": \"省级录入审批流程测试数据\",
            \"prosecutionProbability\": \"高\",
            \"loseProbability\": \"中\",
            \"approvalPath\": \"PROVINCE_ENTRY\"
        }"
}

extract_id() {
    echo "$1" | python3 -c "import sys,json; print(json.load(sys.stdin).get('data',{}).get('id',''))" 2>/dev/null
}

# ============================================================
# 前置: 登录获取 Token
# ============================================================
log_scene "前置: 登录获取Token"

LOGIN_RESP=$(curl -s -X POST "$BASE_URL/api/auth/login" \
    -H "Content-Type: application/json" \
    -d '{"username":"张三","password":"123456"}')

TOKEN=$(echo "$LOGIN_RESP" | python3 -c "import sys,json; print(json.load(sys.stdin).get('data',{}).get('token',''))" 2>/dev/null)

if [ -z "$TOKEN" ] || [ "$TOKEN" = "None" ]; then
    echo -e "${RED}登录失败，无法获取Token，终止测试${NC}"
    echo "响应: $LOGIN_RESP"
    exit 1
fi
log_pass "登录成功，获取Token"

# ============================================================
# 场景1: 省级录入审批 - 完整通过流程
# 草稿 → 提交 → 待省经办审批 → 省经办通过 → 待省审核 → 省审核通过 → 已通过
# ============================================================
log_scene "场景1: 省级录入审批 - 完整通过流程"

# 1.1 创建省级录入索赔事项
log_info "1.1 创建省级录入索赔事项"
RESP=$(create_province_claim "S1")
assert_status "$RESP" "200" "创建索赔事项"
assert_approval_status "$RESP" "DRAFT" "初始状态为草稿"
assert_approval_path "$RESP" "PROVINCE_ENTRY" "审批路径为省级录入"
CLAIM_ID_1=$(extract_id "$RESP")
log_info "事项ID: $CLAIM_ID_1"

# 1.2 提交审批 (DRAFT → PENDING_PROVINCE_OFFICE_REVIEW)
log_info "1.2 提交审批"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_1/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"SUBMIT","comment":"提交省级录入审批"}')
assert_status "$RESP" "200" "提交审批成功"
assert_approval_status "$RESP" "PENDING_PROVINCE_OFFICE_REVIEW" "状态变为待省经办审批"

# 1.3 省经办审批通过 (PENDING_PROVINCE_OFFICE_REVIEW → PENDING_PROVINCE_FINAL_REVIEW)
log_info "1.3 省经办审批通过"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_1/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"APPROVE","comment":"省经办审批通过，材料齐全"}')
assert_status "$RESP" "200" "省经办审批通过"
assert_approval_status "$RESP" "PENDING_PROVINCE_FINAL_REVIEW" "状态变为待省审核"

# 1.4 省审核通过 (PENDING_PROVINCE_FINAL_REVIEW → APPROVED)
log_info "1.4 省审核通过"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_1/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"APPROVE","comment":"省审核通过，同意录入"}')
assert_status "$RESP" "200" "省审核通过"
assert_approval_status "$RESP" "APPROVED" "状态变为已通过"

# 1.5 验证审批日志
log_info "1.5 验证审批日志"
RESP=$(curl -s -X GET "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_1/approval-logs" \
    -H "Authorization: Bearer $TOKEN")
assert_status "$RESP" "200" "获取审批日志"
assert_log_count "$RESP" 3 "审批日志至少3条(提交+省经办通过+省审核通过)"

# ============================================================
# 场景2: 省经办驳回 → 重新提交 → 全流程通过
# ============================================================
log_scene "场景2: 省经办驳回 → 重新编辑 → 重新提交 → 全流程通过"

# 2.1 创建并提交
log_info "2.1 创建并提交"
RESP=$(create_province_claim "S2")
CLAIM_ID_2=$(extract_id "$RESP")
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_2/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"SUBMIT","comment":"提交审批"}')
assert_approval_status "$RESP" "PENDING_PROVINCE_OFFICE_REVIEW" "提交后状态为待省经办审批"

# 2.2 省经办驳回 (PENDING_PROVINCE_OFFICE_REVIEW → REJECTED)
log_info "2.2 省经办驳回"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_2/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"REJECT","comment":"材料不齐，请补充法院文书号"}')
assert_status "$RESP" "200" "省经办驳回成功"
assert_approval_status "$RESP" "REJECTED" "状态变为已驳回"

# 2.3 驳回后可以编辑
log_info "2.3 驳回后编辑"
RESP=$(curl -s -X PUT "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_2" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
        "taskName": "省级测试索赔事项S2-已补充材料",
        "taskType": "诉讼",
        "courtDocumentNo": "(2026)京0105民初99999号",
        "isMajorCase": "否",
        "isClaimPaymentDispute": "是",
        "isRefundDispute": "否",
        "occurrencePeriod": "2026-01",
        "claimedOrgProvince": "北京市",
        "claimedOrgCity": "北京市",
        "claimedOrgDistrict": "朝阳区",
        "claimOrg": "测试索赔机构",
        "factsAndReasons": "已补充完整材料，重新提交审批",
        "prosecutionProbability": "高",
        "loseProbability": "中"
    }')
assert_status "$RESP" "200" "驳回后编辑成功"

# 2.4 重新提交 (REJECTED → PENDING_PROVINCE_OFFICE_REVIEW)
log_info "2.4 重新提交"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_2/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"SUBMIT","comment":"补充材料后重新提交"}')
assert_status "$RESP" "200" "重新提交成功"
assert_approval_status "$RESP" "PENDING_PROVINCE_OFFICE_REVIEW" "状态重新变为待省经办审批"

# 2.5 省经办通过 → 省审核通过
log_info "2.5 走完剩余审批流程"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_2/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"APPROVE","comment":"补充材料后通过"}')
assert_approval_status "$RESP" "PENDING_PROVINCE_FINAL_REVIEW" "省经办通过→待省审核"

RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_2/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"APPROVE","comment":"省审核最终通过"}')
assert_approval_status "$RESP" "APPROVED" "省审核通过→已通过"

# 2.6 验证审批日志（应有5条：提交+驳回+重新提交+省经办通过+省审核通过）
log_info "2.6 验证审批日志"
RESP=$(curl -s -X GET "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_2/approval-logs" \
    -H "Authorization: Bearer $TOKEN")
assert_log_count "$RESP" 5 "审批日志至少5条"

# ============================================================
# 场景3: 省审核驳回 → 重新提交 → 全流程通过
# ============================================================
log_scene "场景3: 省审核阶段驳回 → 重新提交 → 全流程通过"

# 3.1 创建 → 提交 → 省经办通过
log_info "3.1 创建并通过省经办审批"
RESP=$(create_province_claim "S3")
CLAIM_ID_3=$(extract_id "$RESP")
curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_3/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"SUBMIT","comment":"提交"}' > /dev/null
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_3/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"APPROVE","comment":"省经办通过"}')
assert_approval_status "$RESP" "PENDING_PROVINCE_FINAL_REVIEW" "状态为待省审核"

# 3.2 省审核驳回 (PENDING_PROVINCE_FINAL_REVIEW → REJECTED)
log_info "3.2 省审核驳回"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_3/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"REJECT","comment":"省审核驳回：败诉可能性评估不合理"}')
assert_status "$RESP" "200" "省审核驳回成功"
assert_approval_status "$RESP" "REJECTED" "状态变为已驳回"

# 3.3 重新提交后走完全流程
log_info "3.3 重新提交并走完全流程"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_3/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"SUBMIT","comment":"修改后重新提交"}')
assert_approval_status "$RESP" "PENDING_PROVINCE_OFFICE_REVIEW" "重新提交→待省经办审批"

RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_3/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"APPROVE","comment":"省经办再次通过"}')
assert_approval_status "$RESP" "PENDING_PROVINCE_FINAL_REVIEW" "省经办再次通过→待省审核"

RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_3/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"APPROVE","comment":"省审核最终通过"}')
assert_approval_status "$RESP" "APPROVED" "省审核通过→已通过"

# ============================================================
# 场景4: 异常操作测试
# ============================================================
log_scene "场景4: 异常操作测试"

# 4.1 已通过状态不允许再次审批
log_info "4.1 已通过状态不允许再次审批"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_1/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"APPROVE","comment":"尝试对已通过事项再次审批"}')
assert_contains "$RESP" "不允许" "已通过状态禁止审批"

# 4.2 已通过状态不允许提交
log_info "4.2 已通过状态不允许提交"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_1/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"SUBMIT","comment":"尝试对已通过事项提交"}')
assert_contains "$RESP" "不允许" "已通过状态禁止提交"

# 4.3 已通过状态不允许驳回
log_info "4.3 已通过状态不允许驳回"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_1/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"REJECT","comment":"尝试对已通过事项驳回"}')
assert_contains "$RESP" "不允许" "已通过状态禁止驳回"

# 4.4 草稿状态不允许直接审批通过
log_info "4.4 草稿状态不允许直接审批通过"
RESP=$(create_province_claim "S4")
CLAIM_ID_4=$(extract_id "$RESP")
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_4/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"APPROVE","comment":"尝试对草稿直接审批"}')
assert_contains "$RESP" "不允许" "草稿状态禁止直接审批通过"

# 4.5 草稿状态不允许直接驳回
log_info "4.5 草稿状态不允许直接驳回"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_4/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"REJECT","comment":"尝试对草稿直接驳回"}')
assert_contains "$RESP" "不允许" "草稿状态禁止直接驳回"

# 4.6 待省经办审批状态不允许编辑
log_info "4.6 待审批状态不允许编辑"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_4/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"SUBMIT","comment":"提交"}')
assert_approval_status "$RESP" "PENDING_PROVINCE_OFFICE_REVIEW" "提交成功"

RESP=$(curl -s -X PUT "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_4" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
        "taskName": "尝试编辑",
        "taskType": "诉讼",
        "courtDocumentNo": "xxx",
        "isMajorCase": "否",
        "isClaimPaymentDispute": "是",
        "isRefundDispute": "否",
        "occurrencePeriod": "2026-01",
        "claimedOrgProvince": "北京市",
        "claimedOrgCity": "北京市",
        "claimedOrgDistrict": "朝阳区",
        "claimOrg": "测试",
        "factsAndReasons": "尝试在审批中编辑",
        "prosecutionProbability": "高",
        "loseProbability": "中"
    }')
assert_contains "$RESP" "不允许编辑" "待审批状态禁止编辑"

# 4.7 不支持的操作类型
log_info "4.7 不支持的操作类型"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_4/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"CANCEL","comment":"尝试取消"}')
assert_contains "$RESP" "不支持" "不支持的操作类型"

# 4.8 不存在的事项ID
log_info "4.8 不存在的事项ID"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/nonexistent-id-12345/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"SUBMIT","comment":"测试"}')
assert_contains "$RESP" "不存在" "不存在的事项返回错误"

# ============================================================
# 场景5: 查询与过滤验证
# ============================================================
log_scene "场景5: 查询与过滤验证"

# 5.1 按审批状态过滤 - 查询已通过的
log_info "5.1 按状态过滤-已通过"
RESP=$(curl -s -X GET "$BASE_URL/api/lawsuit-claims?approvalStatus=APPROVED&page=1&size=10" \
    -H "Authorization: Bearer $TOKEN")
assert_status "$RESP" "200" "按状态过滤查询成功"

# 5.2 关键词搜索
log_info "5.2 关键词搜索"
RESP=$(curl -s -X GET "$BASE_URL/api/lawsuit-claims?keyword=%E7%9C%81%E7%BA%A7%E6%B5%8B%E8%AF%95&page=1&size=10" \
    -H "Authorization: Bearer $TOKEN")
assert_status "$RESP" "200" "关键词搜索成功"

# 5.3 按ID查询详情
log_info "5.3 按ID查询详情"
RESP=$(curl -s -X GET "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_1" \
    -H "Authorization: Bearer $TOKEN")
assert_status "$RESP" "200" "按ID查询成功"
assert_approval_status "$RESP" "APPROVED" "场景1事项最终状态为已通过"
assert_approval_path "$RESP" "PROVINCE_ENTRY" "审批路径仍为省级录入"

# 5.4 验证与重大索赔路径的隔离 - 创建重大索赔事项
log_info "5.4 验证与重大索赔路径隔离"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
        "taskName": "重大索赔对比测试",
        "taskType": "诉讼",
        "courtDocumentNo": "(2026)沪0101民初001号",
        "isMajorCase": "是",
        "isClaimPaymentDispute": "否",
        "isRefundDispute": "否",
        "occurrencePeriod": "2026-02",
        "claimedOrgProvince": "上海市",
        "claimedOrgCity": "上海市",
        "claimedOrgDistrict": "黄浦区",
        "claimOrg": "对比测试机构",
        "factsAndReasons": "用于验证重大索赔与省级录入路径隔离",
        "prosecutionProbability": "低",
        "loseProbability": "低",
        "approvalPath": "MAJOR_CLAIM"
    }')
CLAIM_ID_5=$(extract_id "$RESP")
assert_approval_path "$RESP" "MAJOR_CLAIM" "重大索赔路径正确"

# 提交重大索赔事项 → 应进入总经办审批（不是省经办）
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_5/approve" \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{"action":"SUBMIT","comment":"提交重大索赔"}')
assert_approval_status "$RESP" "PENDING_OFFICE_REVIEW" "重大索赔提交后进入总经办审批(非省经办)"

# ============================================================
# 场景6: 无Token访问验证
# ============================================================
log_scene "场景6: 未认证访问验证"

log_info "6.1 无Token创建事项"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims" \
    -H "Content-Type: application/json" \
    -d '{"taskName":"无认证测试","taskType":"诉讼","isMajorCase":"否","isClaimPaymentDispute":"否","isRefundDispute":"否","occurrencePeriod":"2026-01","claimedOrgProvince":"北京市","claimedOrgCity":"北京市","claimedOrgDistrict":"朝阳区","claimOrg":"测试","factsAndReasons":"无认证","prosecutionProbability":"低","loseProbability":"低"}')
assert_contains "$RESP" "401" "无Token返回401"

log_info "6.2 无Token审批操作"
RESP=$(curl -s -X POST "$BASE_URL/api/lawsuit-claims/$CLAIM_ID_1/approve" \
    -H "Content-Type: application/json" \
    -d '{"action":"APPROVE","comment":"无认证审批"}')
assert_contains "$RESP" "401" "无Token审批返回401"

# ============================================================
# 测试结果汇总
# ============================================================
echo ""
echo -e "${YELLOW}============================================================${NC}"
echo -e "${YELLOW}                    测试结果汇总${NC}"
echo -e "${YELLOW}============================================================${NC}"
echo -e "  总用例数: ${TOTAL}"
echo -e "  ${GREEN}通过: ${PASS}${NC}"
echo -e "  ${RED}失败: ${FAIL}${NC}"
echo -e "${YELLOW}============================================================${NC}"

if [ "$FAIL" -gt 0 ]; then
    exit 1
else
    echo -e "${GREEN}所有测试通过!${NC}"
    exit 0
fi
