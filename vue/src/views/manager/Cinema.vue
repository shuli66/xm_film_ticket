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
                          :src="props.row.avatar" :preview-src-list="[props.row.avatar]" preview-teleported></el-image>
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
                          :src="props.row.front" :preview-src-list="[props.row.front]" preview-teleported></el-image>
              </el-descriptions-item>
              <el-descriptions-item label="身份证反面">
                <el-image style="width: 40px; height: 40px; display: block" v-if="props.row.back"
                          :src="props.row.back" :preview-src-list="[props.row.back]" preview-teleported></el-image>
              </el-descriptions-item>
              <el-descriptions-item label="营业执照">
                <el-image style="width: 40px; height: 40px; display: block" v-if="props.row.certificate"
                          :src="props.row.certificate" :preview-src-list="[props.row.certificate]" preview-teleported></el-image>
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
                      :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" preview-teleported></el-image>
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
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="影院信息" v-model="data.formVisible" width="50%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="85px" style="padding: 20px">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="data.form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item prop="avatar" label="头像">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="handleFileUpload"
              list-type="picture"
              >
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="name" label="名称">
          <el-input v-model="data.form.name" placeholder="请输入名称"></el-input>
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="data.form.phone" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="data.form.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="address" label="地址">
          <el-input type="textarea" :rows="4" v-model="data.form.address" placeholder="请输入地址"></el-input>
        </el-form-item>
        <el-form-item prop="leader" label="负责人">
          <el-input v-model="data.form.leader" placeholder="请输入负责人姓名"></el-input>
        </el-form-item>
        <el-form-item prop="code" label="身份证号">
          <el-input v-model="data.form.code" placeholder="请输入负责人身份证号"></el-input>
        </el-form-item>
        <el-form-item prop="front" label="身份证正面">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="handleFrontUpload"
              list-type="picture"
          >
            <el-button type="primary">上传负责人身份证正面</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="back" label="身份证反面">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="handleBackUpload"
              list-type="picture"
          >
            <el-button type="primary">上传负责人身份证反面</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="certificate" label="营业执照">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="handleCertificateUpload"
              list-type="picture"
          >
            <el-button type="primary">上传影院的营业执照</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit, Select, CloseBold} from "@element-plus/icons-vue";

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  ids: []
})

const handleStatus = (row, status) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.form.status = status
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
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const add = () => {
  request.post('/cinema/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/cinema/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
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

const handleFileUpload = (res) => {
  data.form.avatar = res.data
}
const handleFrontUpload = (res) => {
  data.form.front = res.data
}
const handleBackUpload = (res) => {
  data.form.back = res.data
}
const handleCertificateUpload = (res) => {
  data.form.certificate = res.data
}

const reset = () => {
  data.name = null
  load()
}

load()
</script>
<style>
.line1 {
  overflow:hidden;
  text-overflow:ellipsis;
  white-space:nowrap;
}
</style>