<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-if="data.user.role === 'ADMIN'" v-model="data.cinemaName" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入影院名称查询"></el-input>
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入影厅名称查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd" v-if="data.user.role === 'CINEMA' || data.user.role === 'ADMIN'">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="cinemaName" label="影院名称" />
        <el-table-column prop="name" label="影厅名称" />
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)" v-if="data.user.role === 'CINEMA' || data.user.role === 'ADMIN'"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="影厅信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="formRef" :rules="rules" :model="formData" label-width="80px" style="padding: 20px">
        <el-form-item prop="cinemaId" label="所属影院" v-if="data.user.role === 'ADMIN'">
          <el-select v-model="selectedCinemaId" placeholder="请选择所属影院">
            <el-option
                v-for="item in data.cinemaData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="name" label="影厅名称">
          <el-input v-model="formData.name" placeholder="请输入影厅名称"></el-input>
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

import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const formRef = ref()
const formData = ref({})  // 使用ref而不是reactive管理表单数据
const selectedCinemaId = ref(null)  // 单独管理影院ID

const rules = {
  name: [
    { required: true, message: '请输入影厅名称', trigger: 'blur' }
  ],
  cinemaId: [
    { required: true, message: '请选择所属影院', trigger: 'change' }
  ],
}

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  tableData: [],
  pageNum: 1,
  pageSize: 8,
  total: 0,
  name: null,
  cinemaName: null,
  ids: [],
  cinemaData: [],
})

const loadCinema = () => {
  if (data.user.role === 'ADMIN') {
    request.get('/cinema/selectAll').then(res => {
      if (res.code === '200') {
        data.cinemaData = res.data
        console.log("加载的影院数据:", data.cinemaData)
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

const load = () => {
  request.get('/room/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      cinemaName: data.cinemaName,
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleAdd = () => {
  formData.value = {}  // 清空表单数据
  selectedCinemaId.value = null  // 清空选择的影院
  
  if (data.user.role === 'CINEMA') {
    selectedCinemaId.value = data.user.id
  }
  
  data.formVisible = true
}

const handleEdit = (row) => {
  // 深拷贝行数据，避免引用问题
  formData.value = JSON.parse(JSON.stringify(row))
  
  // 设置影院ID
  selectedCinemaId.value = row.cinemaId
  
  console.log("编辑影厅，影院ID:", selectedCinemaId.value)
  
  data.formVisible = true
}

const add = () => {
  const submitData = {
    name: formData.value.name,
    cinemaId: data.user.role === 'ADMIN' ? selectedCinemaId.value : data.user.id
  }
  
  console.log("添加影厅，发送到后端的数据:", submitData)
  
  request.post('/room/add', submitData).then(res => {
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
  // 明确指定ID
  const submitData = {
    id: formData.value.id,
    name: formData.value.name,
    cinemaId: data.user.role === 'ADMIN' ? selectedCinemaId.value : formData.value.cinemaId
  }
  
  console.log("更新影厅，发送到后端的数据:", submitData)
  
  // 添加请求头，尝试避免缓存问题
  request.put('/room/update', submitData, {
    headers: {
      'Cache-Control': 'no-cache',
      'Pragma': 'no-cache',
      'X-Requested-With': 'XMLHttpRequest',
      'timestamp': new Date().getTime() // 添加时间戳避免缓存
    }
  }).then(res => {
    if (res.code === '200') {
      console.log("更新成功，后端响应:", res)
      ElMessage.success('操作成功')
      data.formVisible = false
      
      // 强制刷新数据
      setTimeout(() => {
        load()
      }, 300)
    } else {
      ElMessage.error(res.msg || '更新失败')
      console.error("更新失败，错误信息:", res)
    }
  }).catch(error => {
    console.error("更新请求异常:", error)
    ElMessage.error('请求异常: ' + (error.message || error))
  })
}

const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      if (data.user.role === 'ADMIN' && !selectedCinemaId.value) {
        ElMessage.warning("请选择所属影院")
        return
      }
      
      console.log("保存影厅，选中的影院ID:", selectedCinemaId.value)
      
      formData.value.id ? update() : add()
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/room/delete/' + id).then(res => {
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
    request.delete("/room/delete/batch", {data: data.ids}).then(res => {
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
  data.cinemaName = null
  load()
}

loadCinema()
load()
</script>