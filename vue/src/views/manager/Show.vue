<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-if="data.user.role === 'ADMIN'" v-model="data.cinemaName" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入影院名称查询"></el-input>
      <el-input v-model="data.filmName" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入电影名称查询"></el-input>
      <el-date-picker style="width: 240px; margin-right: 10px"
          v-model="data.time"
          type="date"
          placeholder="请选择日期"
          value-format="YYYY-MM-DD"
      />
      <el-select v-model="data.status" placeholder="请选择放映状态" style="width: 240px; margin-right: 10px">
        <el-option label="购票中" value="购票中" />
        <el-option label="已放映" value="已放映" />
      </el-select>
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
        <el-table-column prop="filmImg" label="电影封面">
          <template v-slot="scope">
            <el-image style="width: 40px; height: 40px; border-radius: 5px; display: block" v-if="scope.row.filmImg"
                      :src="scope.row.filmImg" :preview-src-list="[scope.row.filmImg]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="filmName" label="电影名称" />
        <el-table-column prop="cinemaName" label="影院名称" v-if="data.user.role === 'ADMIN'" />
        <el-table-column prop="roomName" label="影厅名称" />
        <el-table-column prop="price" label="电影票价/元" />
        <el-table-column prop="time" label="放映时间" />
        <el-table-column prop="status" label="放映状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === '购票中'" type="success">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '已放映'" type="danger">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
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

    <el-dialog title="电影放映信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding: 20px">
        <el-form-item prop="cinemaId" label="所属影院" v-if="data.user.role === 'ADMIN'">
          <el-select v-model="data.form.cinemaId" placeholder="请选择所属影院" @change="handleCinemaChange">
            <el-option
                v-for="item in data.cinemaData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="roomId" label="影厅房间">
          <el-select v-model="data.form.roomId" placeholder="请选择影厅房间">
            <el-option
                v-for="item in data.roomData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="filmId" label="放映电影">
          <el-select v-model="data.form.filmId" placeholder="请选择放映电影">
            <el-option
                v-for="item in data.filmData"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="time" label="放映时间">
          <el-date-picker style="width: 200px"
              v-model="data.form.time"
              type="datetime"
              placeholder="请选择日期"
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item prop="price" label="电影票价">
          <el-input-number style="width: 200px" v-model="data.form.price" :min="1" :step="0.1" :precision="2" @change="handleChange" />
        </el-form-item>
        <el-form-item prop="status" label="放映状态">
          <el-radio-group v-model="data.form.status">
            <el-radio-button label="购票中" value="购票中" />
            <el-radio-button label="已放映" value="已放映" />
          </el-radio-group>
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
const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 8,
  total: 0,
  name: null,
  cinemaName: null,
  filmName: null,
  time: null,
  status: null,
  ids: [],
  roomData: [],
  filmData: [],
  cinemaData: [],
  rules: {
    roomId: [
      { required: true, message: '请选择影厅房间', trigger: 'change' }
    ],
    filmId: [
      { required: true, message: '请选择放映电影', trigger: 'change' }
    ],
    time: [
      { required: true, message: '请选择放映时间', trigger: 'change' }
    ],
    price: [
      { required: true, message: '请输入电影票价', trigger: 'blur' }
    ],
    status: [
      { required: true, message: '请选择放映状态', trigger: 'change' }
    ],
    cinemaId: [
      { required: true, message: '请选择所属影院', trigger: 'change' }
    ],
  }
})

const loadCinema = () => {
  if (data.user.role === 'ADMIN') {
    request.get('/cinema/selectAll').then(res => {
      if (res.code === '200') {
        data.cinemaData = res.data
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

const handleCinemaChange = (cinemaId) => {
  // 当选择影院后，加载该影院的影厅
  loadRoom(cinemaId)
}

const loadRoom = (cinemaId) => {
  request.get('/room/selectAll', {
    params: {
      cinemaId: cinemaId || data.user.id
    }
  }).then(res => {
    if (res.code === '200') {
      data.roomData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadFilm = () => {
  request.get('/film/selectAll').then(res => {
    if (res.code === '200') {
      data.filmData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const load = () => {
  request.get('/show/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      cinemaName: data.cinemaName,
      filmName: data.filmName,
      time: data.time,
      status: data.status,
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
  data.form = {}
  data.form.status = '购票中'
  if (data.user.role === 'CINEMA') {
    data.form.cinemaId = data.user.id
    loadRoom()
  }
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  if (data.user.role === 'ADMIN') {
    loadRoom(data.form.cinemaId)
  }
  data.formVisible = true
}

const add = () => {
  request.post('/show/add', data.form).then(res => {
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
  request.put('/show/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    }
  })
}

const save = () => {
  formRef.value.validate(valid => {
    if (valid) {
      data.form.id ? update() : add()
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/show/delete/' + id).then(res => {
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
    request.delete("/show/delete/batch", {data: data.ids}).then(res => {
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
  data.filmName = null
  data.time = null
  data.status = null
  load()
}

const handleChange = (value) => {
  console.log(value)
}

loadCinema()
loadFilm()
load()
</script>