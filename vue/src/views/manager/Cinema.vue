<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入名称查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column type="expand">
          <template #default="props">
            <el-descriptions class="margin-top" title="影院信息" :column="5" border>
              <el-descriptions-item label="账号">{{ props.row.username }}</el-descriptions-item>
              <el-descriptions-item label="电影院名称">
                <el-popover
                    placement="top-start"
                    title="影院名称"
                    :width="200"
                    trigger="hover"
                    :content="props.row.name"
                >
                  <template #reference>
                    {{ props.row.name }}
                  </template>
                </el-popover>
              </el-descriptions-item>
              <el-descriptions-item label="头像">
                <el-image style="width: 40px; height: 40px; display: block" v-if="props.row.avatar"
                          :src="getFullImageUrl(props.row.avatar)" :preview-src-list="[getFullImageUrl(props.row.avatar)]" preview-teleported></el-image>
              </el-descriptions-item>
              <el-descriptions-item label="手机号">
                <template #label>
                  <div class="cell-item">
                    手机号
                  </div>
                </template>{{ props.row.phone }}
              </el-descriptions-item>
              <el-descriptions-item label="邮箱">{{ props.row.email }}</el-descriptions-item>
              <el-descriptions-item label="影院地址">
                <el-popover placement="top-start" title="影院地址" :width="200" trigger="hover" :content="props.row.address">
                  <template #reference>
                    <div style="width: 80px" class="line1">{{ props.row.address }}</div>
                  </template>
                </el-popover>
              </el-descriptions-item>
              <el-descriptions-item label="负责人">{{ props.row.leader }}</el-descriptions-item>
              <el-descriptions-item label="身份证号">
                <el-popover placement="top-start" title="身份证号" :width="200" trigger="hover" :content="props.row.code">
                  <template #reference>
                    <div style="width: 50px" class="line1">{{ props.row.code }}</div>
                  </template>
                </el-popover>
              </el-descriptions-item>
              <el-descriptions-item label="身份证正面">
                <el-image style="width: 40px; height: 40px; display: block" v-if="props.row.front"
                          :src="getFullImageUrl(props.row.front)" :preview-src-list="[getFullImageUrl(props.row.front)]" preview-teleported></el-image>
              </el-descriptions-item>
              <el-descriptions-item label="身份证反面">
                <el-image style="width: 40px; height: 40px; display: block" v-if="props.row.back"
                          :src="getFullImageUrl(props.row.back)" :preview-src-list="[getFullImageUrl(props.row.back)]" preview-teleported></el-image>
              </el-descriptions-item>
              <el-descriptions-item label="营业执照">
                <el-image style="width: 40px; height: 40px; display: block" v-if="props.row.certificate"
                          :src="getFullImageUrl(props.row.certificate)" :preview-src-list="[getFullImageUrl(props.row.certificate)]" preview-teleported></el-image>
              </el-descriptions-item>
              <el-descriptions-item label="审核状态">
                <el-tag v-if="props.row.status === '待审核'" type="warning">{{ props.row.status }}</el-tag>
                <el-tag v-if="props.row.status === '审核通过'" type="success">{{ props.row.status }}</el-tag>
                <el-tag v-if="props.row.status === '审核拒绝'" type="danger">{{ props.row.status }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="角色">{{ props.row.role }}</el-descriptions-item>
            </el-descriptions>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号" />
        <el-table-column prop="avatar" label="头像">
          <template v-slot="scope">
            <el-image style="width: 40px; height: 40px; border-radius: 50%; display: block" v-if="scope.row.avatar"
                      :src="getFullImageUrl(scope.row.avatar)" :preview-src-list="[getFullImageUrl(scope.row.avatar)]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="影院名称" />
        <el-table-column prop="phone" label="电话" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="status" label="审核状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === '待审核'" type="warning">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '审核通过'" type="success">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '审核拒绝'" type="danger">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="role" label="角色" />
        <el-table-column label="操作" width="200" fixed="right">
          <template v-slot="scope">
            <el-button type="success" circle :icon="Select" @click="handleStatus(scope.row, '审核通过')"></el-button>
            <el-button type="info" circle :icon="CloseBold" @click="handleStatus(scope.row, '审核拒绝')"></el-button>
            <el-button type="primary" circle :icon="View" @click="handleView(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="影院信息" v-model="data.viewVisible" width="50%" destroy-on-close>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="用户名">{{ data.viewForm.username }}</el-descriptions-item>
        <el-descriptions-item label="头像">
          <el-image style="width: 100px; height: 100px; border-radius: 50%; display: block" v-if="data.viewForm.avatar"
                    :src="getFullImageUrl(data.viewForm.avatar)" :preview-src-list="[getFullImageUrl(data.viewForm.avatar)]" preview-teleported></el-image>
        </el-descriptions-item>
        <el-descriptions-item label="影院名称">{{ data.viewForm.name }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ data.viewForm.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ data.viewForm.email }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ data.viewForm.address }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ data.viewForm.leader }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ data.viewForm.code }}</el-descriptions-item>
        <el-descriptions-item label="身份证正面">
          <el-image style="width: 200px; height: 120px; display: block" v-if="data.viewForm.front"
                    :src="getFullImageUrl(data.viewForm.front)" :preview-src-list="[getFullImageUrl(data.viewForm.front)]" preview-teleported></el-image>
        </el-descriptions-item>
        <el-descriptions-item label="身份证反面">
          <el-image style="width: 200px; height: 120px; display: block" v-if="data.viewForm.back"
                    :src="getFullImageUrl(data.viewForm.back)" :preview-src-list="[getFullImageUrl(data.viewForm.back)]" preview-teleported></el-image>
        </el-descriptions-item>
        <el-descriptions-item label="营业执照">
          <el-image style="width: 200px; height: 120px; display: block" v-if="data.viewForm.certificate"
                    :src="getFullImageUrl(data.viewForm.certificate)" :preview-src-list="[getFullImageUrl(data.viewForm.certificate)]" preview-teleported></el-image>
        </el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag v-if="data.viewForm.status === '待审核'" type="warning">{{ data.viewForm.status }}</el-tag>
          <el-tag v-if="data.viewForm.status === '审核通过'" type="success">{{ data.viewForm.status }}</el-tag>
          <el-tag v-if="data.viewForm.status === '审核拒绝'" type="danger">{{ data.viewForm.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="角色">{{ data.viewForm.role }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 新增影院表单对话框 -->
    <el-dialog title="新增影院" v-model="data.formVisible" width="50%" destroy-on-close>
      <el-form :model="data.form" label-width="100px" ref="formRef" :rules="rules">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="data.form.username" placeholder="请输入影院账号用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="data.form.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="影院名称" prop="name">
          <el-input v-model="data.form.name" placeholder="请输入影院名称"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="data.form.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="data.form.email" placeholder="请输入邮箱地址"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="data.form.address" placeholder="请输入影院地址"></el-input>
        </el-form-item>
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="data.form.leader" placeholder="请输入负责人姓名"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="code">
          <el-input v-model="data.form.code" placeholder="请输入负责人身份证号"></el-input>
        </el-form-item>
        <el-form-item label="身份证正面" prop="front">
          <el-upload
            class="avatar-uploader"
            :action="baseUrl + '/files/upload'"
            :headers="{ token: getToken() }"
            :show-file-list="false"
            :on-success="handleFrontSuccess"
          >
            <img v-if="data.form.front" :src="getFullImageUrl(data.form.front)" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            <div class="el-upload__tip">请上传身份证正面照片</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="身份证反面" prop="back">
          <el-upload
            class="avatar-uploader"
            :action="baseUrl + '/files/upload'"
            :headers="{ token: getToken() }"
            :show-file-list="false"
            :on-success="handleBackSuccess"
          >
            <img v-if="data.form.back" :src="getFullImageUrl(data.form.back)" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            <div class="el-upload__tip">请上传身份证反面照片</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="营业执照" prop="certificate">
          <el-upload
            class="avatar-uploader"
            :action="baseUrl + '/files/upload'"
            :headers="{ token: getToken() }"
            :show-file-list="false"
            :on-success="handleCertificateSuccess"
          >
            <img v-if="data.form.certificate" :src="getFullImageUrl(data.form.certificate)" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            <div class="el-upload__tip">请上传营业执照照片</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="data.form.status" placeholder="请选择状态">
            <el-option label="待审核" value="待审核"></el-option>
            <el-option label="审核通过" value="审核通过"></el-option>
            <el-option label="审核拒绝" value="审核拒绝"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, View, Select, CloseBold, Plus} from "@element-plus/icons-vue";

const baseUrl = import.meta.env.VITE_BASE_URL
const formRef = ref(null)

const data = reactive({
  viewVisible: false,
  formVisible: false, // 新增表单对话框可见性
  viewForm: {},
  form: {}, // 新增表单数据
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  ids: []
})

// 表单校验规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入影院名称', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  address: [
    { required: true, message: '请输入影院地址', trigger: 'blur' }
  ],
  leader: [
    { required: true, message: '请输入负责人姓名', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 获取完整图片URL
const getFullImageUrl = (url) => {
  if (!url) return '';
  if (url.startsWith('http')) return url;
  return baseUrl + url;
};

// 获取token
const getToken = () => {
  const userStr = localStorage.getItem('xm-user');
  if (userStr) {
    try {
      const user = JSON.parse(userStr);
      return user.token;
    } catch (e) {
      console.error('解析用户信息出错:', e);
      return '';
    }
  }
  return '';
};

const handleStatus = (row, status) => {
  data.viewForm = JSON.parse(JSON.stringify(row))
  data.viewForm.status = status
  update()
}

const load = () => {
  request.get('/cinema/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}

const handleView = (row) => {
  data.viewForm = JSON.parse(JSON.stringify(row))
  data.viewVisible = true
}

const update = () => {
  request.put('/cinema/update', data.viewForm).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      // 不再发送审核结果通知
      load()
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/cinema/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete("/cinema/delete/batch", {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const reset = () => {
  data.name = null
  load()
}

// 新增影院按钮点击处理
const handleAdd = () => {
  data.form = {
    role: 'CINEMA',
    status: '待审核'
  };
  data.formVisible = true;
};

// 身份证正面上传成功回调
const handleFrontSuccess = (res) => {
  if (res.code === '200') {
    data.form.front = res.data;
    ElMessage.success('上传成功');
  } else {
    ElMessage.error('上传失败');
  }
};

// 身份证反面上传成功回调
const handleBackSuccess = (res) => {
  if (res.code === '200') {
    data.form.back = res.data;
    ElMessage.success('上传成功');
  } else {
    ElMessage.error('上传失败');
  }
};

// 营业执照上传成功回调
const handleCertificateSuccess = (res) => {
  if (res.code === '200') {
    data.form.certificate = res.data;
    ElMessage.success('上传成功');
  } else {
    ElMessage.error('上传失败');
  }
};

// 保存新增影院信息
const save = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/cinema/add', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('新增成功');
          data.formVisible = false;
          load();
          // 不再发送审核结果通知
        } else {
          ElMessage.error(res.msg || '新增失败');
        }
      }).catch(err => {
        console.error('新增影院请求错误', err);
        ElMessage.error('系统错误，请稍后重试');
      });
    } else {
      ElMessage.warning('请完善表单信息');
    }
  });
};

load()
</script>
<style>
.line1 {
  overflow:hidden;
  text-overflow:ellipsis;
  white-space:nowrap;
}

/* 上传组件样式 */
.avatar-uploader {
  display: block;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 120px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar {
  width: 178px;
  height: 120px;
  display: block;
}

.el-upload__tip {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
}
</style>