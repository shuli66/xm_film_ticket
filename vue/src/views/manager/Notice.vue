<template>
  <div class="notice-manager-container">
    <div class="notice-manager-header">
      <div class="notice-manager-title">公告管理</div>
      <div class="notice-manager-subtitle">管理系统公告信息</div>
    </div>

    <!-- 搜索区域 -->
    <div class="card search-card">
      <div class="card-body">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input 
              v-model="data.title" 
              placeholder="请输入公告标题查询" 
              clearable
              prefix-icon="Search"
              @keyup.enter="load"
            >
            </el-input>
          </el-col>
          <el-col :span="12">
            <el-button type="primary" @click="load" :icon="Search">查询</el-button>
            <el-button @click="reset" :icon="RefreshRight">重置</el-button>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 操作按钮区域 -->
    <div class="card action-card">
      <div class="card-body">
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增公告</el-button>
        <el-button type="danger" :icon="Delete" @click="delBatch" :disabled="!data.ids.length">批量删除</el-button>
        <el-tooltip content="刷新数据" placement="top">
          <el-button :icon="Refresh" circle @click="load"></el-button>
        </el-tooltip>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="card table-card">
      <div class="card-body">
        <el-table
          v-loading="data.loading"
          border
          stripe
          :data="data.tableData"
          @selection-change="handleSelectionChange"
          style="width: 100%"
          :header-cell-style="{background: '#f8f9fa', color: '#606266'}"
          highlight-current-row
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column type="index" label="#" width="60" align="center" />
          <el-table-column prop="title" label="公告标题" min-width="150" show-overflow-tooltip>
            <template #default="scope">
              <el-tag 
                effect="plain" 
                type="info" 
                size="small" 
                style="margin-right: 5px"
              >公告</el-tag>
              {{ scope.row.title }}
            </template>
          </el-table-column>
          <el-table-column prop="content" label="公告内容" min-width="250" show-overflow-tooltip />
          <el-table-column prop="time" label="发布时间" min-width="180" align="center">
            <template #default="scope">
              <div class="time-display">
                <el-icon><Calendar /></el-icon>
                {{ formatTime(scope.row.time) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160" fixed="right" align="center">
            <template #default="scope">
              <div class="action-buttons">
                <el-tooltip content="编辑" placement="top">
                  <el-button type="primary" :icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
                </el-tooltip>
                <el-tooltip content="查看" placement="top">
                  <el-button type="success" :icon="View" circle @click="handleView(scope.row)"></el-button>
                </el-tooltip>
                <el-tooltip content="删除" placement="top">
                  <el-button type="danger" :icon="Delete" circle @click="del(scope.row.id)"></el-button>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 空数据 -->
        <div class="empty-block" v-if="!data.tableData.length && !data.loading">
          <el-empty description="暂无公告数据" />
        </div>
      </div>
    </div>

    <!-- 分页区域 -->
    <div class="card pagination-card" v-if="data.total">
      <div class="card-body">
        <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
          @size-change="handleSizeChange"
          @current-change="load"
        />
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog 
      v-model="data.formVisible" 
      :title="data.form.id ? '编辑公告' : '新增公告'" 
      width="600px" 
      destroy-on-close
      :close-on-click-modal="false"
    >
      <el-form 
        ref="formRef" 
        :model="data.form" 
        :rules="data.rules" 
        label-width="100px" 
        label-position="right"
        status-icon
      >
        <el-form-item prop="title" label="公告标题" required>
          <el-input 
            v-model="data.form.title" 
            placeholder="请输入公告标题" 
            maxlength="50" 
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item prop="content" label="公告内容" required>
          <el-input 
            type="textarea" 
            :rows="6" 
            v-model="data.form.content" 
            placeholder="请输入公告内容"
            maxlength="500"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item prop="important" label="重要程度">
          <el-radio-group v-model="data.form.important">
            <el-radio :label="true">重要</el-radio>
            <el-radio :label="false">普通</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save" :loading="data.saveLoading">保 存</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog 
      v-model="data.viewVisible" 
      title="公告详情" 
      width="600px" 
      destroy-on-close
    >
      <div class="notice-view-container">
        <div class="notice-view-header">
          <h2 class="notice-view-title">{{ data.viewForm.title }}</h2>
          <div class="notice-view-meta">
            <el-tag v-if="data.viewForm.important" type="danger" size="small" effect="dark">重要</el-tag>
            <span class="notice-view-time">{{ formatTime(data.viewForm.time) }}</span>
          </div>
        </div>
        <div class="notice-view-content">
          {{ data.viewForm.content }}
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delete, Edit, View, Search, Plus, RefreshRight, Refresh, Calendar } from "@element-plus/icons-vue";
import dayjs from 'dayjs';

const formRef = ref(null);

const data = reactive({
  formVisible: false,
  viewVisible: false,
  form: {},
  viewForm: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  title: null,
  ids: [],
  loading: false,
  saveLoading: false,
  rules: {
    title: [
      { required: true, message: '请输入公告标题', trigger: 'blur' },
      { min: 2, max: 50, message: '标题长度在 2 到 50 个字符', trigger: 'blur' }
    ],
    content: [
      { required: true, message: '请输入公告内容', trigger: 'blur' },
      { min: 5, max: 500, message: '内容长度在 5 到 500 个字符', trigger: 'blur' }
    ]
  }
})

const formatTime = (time) => {
  if (!time) return '';
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss');
}

const load = () => {
  data.loading = true;
  request.get('/notice/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title || undefined
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total || 0;
    } else {
      ElMessage.error(res.msg || '获取数据失败');
    }
  }).finally(() => {
    data.loading = false;
  });
}

const handleSizeChange = (size) => {
  data.pageSize = size;
  data.pageNum = 1;
  load();
}

const handleAdd = () => {
  data.form = {
    important: false
  };
  data.formVisible = true;
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row));
  data.form.important = !!data.form.important;
  data.formVisible = true;
}

const handleView = (row) => {
  data.viewForm = JSON.parse(JSON.stringify(row));
  data.viewForm.important = !!data.viewForm.important;
  data.viewVisible = true;
}

const add = () => {
  data.saveLoading = true;
  data.form.time = new Date(); // 设置当前时间为发布时间
  
  request.post('/notice/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('新增公告成功');
      data.formVisible = false;
      load();
    } else {
      ElMessage.error(res.msg || '操作失败');
    }
  }).finally(() => {
    data.saveLoading = false;
  });
}

const update = () => {
  data.saveLoading = true;
  request.put('/notice/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新公告成功');
      data.formVisible = false;
      load();
    } else {
      ElMessage.error(res.msg || '操作失败');
    }
  }).finally(() => {
    data.saveLoading = false;
  });
}

const save = () => {
  if (!formRef.value) return;
  
  formRef.value.validate((valid) => {
    if (valid) {
      data.form.id ? update() : add();
    } else {
      return false;
    }
  });
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { 
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    draggable: true
  }).then(() => {
    request.delete('/notice/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功");
        load();
      } else {
        ElMessage.error(res.msg || '删除失败');
      }
    });
  }).catch(() => {});
}

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请至少选择一条数据");
    return;
  }
  ElMessageBox.confirm(`确定要删除选中的 ${data.ids.length} 条数据吗？删除后不可恢复！`, '批量删除确认', { 
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    draggable: true
  }).then(() => {
    request.delete("/notice/delete/batch", { data: data.ids }).then(res => {
      if (res.code === '200') {
        ElMessage.success(`成功删除 ${data.ids.length} 条数据`);
        data.ids = [];
        load();
      } else {
        ElMessage.error(res.msg || '批量删除失败');
      }
    });
  }).catch(() => {});
}

const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id);
}

const reset = () => {
  data.title = null;
  data.pageNum = 1;
  load();
}

// 初始加载数据
load();
</script>

<style scoped>
.notice-manager-container {
  padding: 20px;
}

.notice-manager-header {
  margin-bottom: 20px;
  text-align: left;
}

.notice-manager-title {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.notice-manager-subtitle {
  font-size: 14px;
  color: #909399;
}

.card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
  transition: all 0.3s;
}

.card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
}

.card-body {
  padding: 20px;
}

.search-card .el-input {
  width: 100%;
}

.action-card .el-button {
  margin-right: 10px;
}

.time-display {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 5px;
}

.action-buttons .el-button {
  transition: all 0.3s;
}

.action-buttons .el-button:hover {
  transform: translateY(-2px);
}

.empty-block {
  padding: 30px 0;
}

.notice-view-container {
  padding: 10px;
}

.notice-view-header {
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 15px;
  margin-bottom: 15px;
}

.notice-view-title {
  font-size: 18px;
  color: #303133;
  margin-bottom: 10px;
}

.notice-view-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.notice-view-time {
  color: #909399;
  font-size: 14px;
}

.notice-view-content {
  padding: 10px;
  line-height: 1.8;
  font-size: 15px;
  white-space: pre-wrap;
  color: #606266;
  background-color: #f8f9fa;
  border-radius: 4px;
  min-height: 120px;
}

/* 平板响应式 */
@media screen and (max-width: 992px) {
  .el-col-6 {
    width: 50%;
  }
}

/* 移动端响应式 */
@media screen and (max-width: 768px) {
  .notice-manager-container {
    padding: 10px;
  }
  
  .el-col-6 {
    width: 100%;
  }
  
  .action-card .el-button {
    margin-bottom: 10px;
  }
}
</style>