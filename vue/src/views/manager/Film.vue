<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.title" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入电影名称查询"></el-input>
      <el-select v-model="data.status" placeholder="请选择电影状态" style="width: 240px; margin-right: 10px">
        <el-option label="待上映" value="待上映" />
        <el-option label="已上映" value="已上映" />
        <el-option label="停止上映" value="停止上映" />
      </el-select>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'ADMIN'">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table tooltip-effect="dark widthStyle" stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" v-if="data.user.role === 'ADMIN'" />
        <el-table-column type="expand">
          <template #default="props">
            <el-descriptions class="margin-top" title="电影信息" :column="5" border>
              <el-descriptions-item label="电影名称">{{ props.row.title }}</el-descriptions-item>
              <el-descriptions-item label="英文名称">{{ props.row.english }}</el-descriptions-item>
              <el-descriptions-item label="上映日期">{{ props.row.start }}</el-descriptions-item>
              <el-descriptions-item label="电影时长">{{ props.row.time }} 分钟</el-descriptions-item>
              <el-descriptions-item label="电影类型">
                <el-tag v-for="item in props.row.types" type="info" style="margin-right: 5px">{{ item }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="电影语言">{{ props.row.language }}</el-descriptions-item>
              <el-descriptions-item label="电影分辨率">{{ props.row.resolution }}</el-descriptions-item>
              <el-descriptions-item label="电影简介">
                <el-popover placement="top-start" title="电影简介" :width="400" trigger="hover" :content="props.row.content">
                  <template #reference>
                    <div style="width: 100px" class="line1">{{ props.row.content }}</div>
                  </template>
                </el-popover>
              </el-descriptions-item>
              <el-descriptions-item label="电影封面">
                <el-image style="width: 40px; height: 40px; display: block" v-if="props.row.img"
                          :src="props.row.img" :preview-src-list="[props.row.img]" preview-teleported></el-image>
              </el-descriptions-item>
              <el-descriptions-item label="制作公司">
                <el-popover placement="top-start" title="制作公司" :width="200" trigger="hover" :content="props.row.employ">
                  <template #reference>
                    <div style="width: 100px" class="line1">{{ props.row.employ }}</div>
                  </template>
                </el-popover>
              </el-descriptions-item>
              <el-descriptions-item label="电影区域">{{ props.row.areaName }}</el-descriptions-item>
              <el-descriptions-item label="电影状态">
                <el-tag v-if="props.row.status === '待上映'" type="warning">{{ props.row.status }}</el-tag>
                <el-tag v-if="props.row.status === '已上映'" type="success">{{ props.row.status }}</el-tag>
                <el-tag v-if="props.row.status === '停止上映'" type="danger">{{ props.row.status }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="电影评分">
                <el-rate
                    v-model="props.row.score"
                    disabled
                    :max="10"
                    text-color="#ff9900"
                />
              </el-descriptions-item>
            </el-descriptions>
          </template>
        </el-table-column>
        <el-table-column prop="img" label="电影封面">
          <template v-slot="scope">
            <el-image style="width: 40px; height: 40px; border-radius: 5px; display: block" v-if="scope.row.img"
                      :src="scope.row.img" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="电影名称" />
        <el-table-column prop="english" label="英文名" show-overflow-tooltip />
        <el-table-column prop="start" label="上映日期" />
        <el-table-column prop="time" label="电影时长/分" />
        <el-table-column prop="types" label="电影类型" width="180">
          <template v-slot="scope">
            <el-tag v-for="item in scope.row.types" type="info" style="margin-right: 5px">{{ item }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="language" label="语言" />
        <el-table-column prop="content" label="电影简介" show-overflow-tooltip />
        <el-table-column prop="areaName" label="电影区域" />
        <el-table-column prop="status" label="电影状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === '待上映'" type="warning">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '已上映'" type="success">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '停止上映'" type="danger">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right" v-if="data.user.role === 'ADMIN'">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="电影信息" v-model="data.formVisible" width="50%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="85px" style="padding: 20px">
        <el-form-item prop="title" label="电影名称">
          <el-input v-model="data.form.title" placeholder="请输入电影名称"></el-input>
        </el-form-item>
        <el-form-item prop="english" label="英文名称">
          <el-input v-model="data.form.english" placeholder="请输入英文名称"></el-input>
        </el-form-item>
        <el-form-item prop="start" label="上映日期">
          <el-date-picker v-model="data.form.start" type="date" value-format="YYYY-MM-DD" style="width: 250px"></el-date-picker>
        </el-form-item>
        <el-form-item prop="time" label="电影时长">
          <el-input-number v-model="data.form.time" :min="30" style="width: 250px" />
        </el-form-item>
        <el-form-item prop="typeId" label="电影类型">
          <el-select v-model="data.form.ids" multiple multiple-limit="3" placeholder="请选择电影分类">
            <el-option
                v-for="item in data.typeData"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="language" label="电影语言">
          <el-select v-model="data.form.language" placeholder="请选择电影语言">
            <el-option label="普通话" value="普通话" />
            <el-option label="港语" value="港语" />
            <el-option label="英语" value="英语" />
            <el-option label="法语" value="法语" />
            <el-option label="俄语" value="俄语" />
            <el-option label="日语" value="日语" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item prop="resolution" label="分辨率">
          <el-select v-model="data.form.resolution" placeholder="请选择分辨率">
            <el-option label="普通" value="普通" />
            <el-option label="2DIMAX" value="2DIMAX" />
            <el-option label="3DIMAX" value="3DIMAX" />
          </el-select>
        </el-form-item>
        <el-form-item prop="content" label="电影简介">
          <el-input type="textarea" :rows="5" v-model="data.form.content" placeholder="请输入电影简介"></el-input>
        </el-form-item>
        <el-form-item prop="img" label="电影封面">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="handleFileUpload"
              list-type="picture"
              >
            <el-button type="primary">点击上传电影封面图</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="employ" label="制作公司">
          <el-input v-model="data.form.employ" placeholder="请输入制作公司"></el-input>
        </el-form-item>
        <el-form-item prop="areaId" label="电影区域">
          <el-select v-model="data.form.areaId" placeholder="请选择电影区域">
            <el-option
                v-for="item in data.areaData"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="status" label="电影状态" v-if="data.form.id">
          <el-radio-group v-model="data.form.status">
            <el-radio-button v-if="isAllowedStatus('待上映')" label="待上映" value="待上映" />
            <el-radio-button v-if="isAllowedStatus('已上映')" label="已上映" value="已上映" />
            <el-radio-button label="停止上映" value="停止上映" />
          </el-radio-group>
          <div class="status-info" v-if="data.form.status !== '停止上映'">
            <small>（当前状态由上映日期自动判断，仅可手动选择停止上映）</small>
          </div>
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
import {Delete, Edit} from "@element-plus/icons-vue";
import { API } from '@/utils/api.js';

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 8,
  total: 0,
  title: null,
  status: null,
  ids: [],
  typeData: [],
  areaData: []
})

const loadType = () => {
  request.get(API.TYPE.SELECT_ALL).then(res  => {
    if (res.code === '200') {
      data.typeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadArea = () => {
  request.get(API.AREA.SELECT_ALL).then(res => {
    if (res.code === '200') {
      data.areaData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const load = () => {
  request.get(API.FILM.SELECT_PAGE, {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
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
  data.form = {
    time: 90,
    ids: []
  }
  data.formVisible = true
  loadType()
  loadArea()
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
  loadType()
  loadArea()
}
const save = () => {
  let saveData = {...data.form};
  if (!saveData.id) {
    saveData.status = null;
  }
  
  if (saveData.id) {
    request.put(API.FILM.UPDATE, saveData).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        data.formVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  } else {
    request.post(API.FILM.ADD, saveData).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        data.formVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}
const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete(API.FILM.DELETE(id)).then(res => {
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
    request.delete(API.FILM.DELETE_BATCH, {data: data.ids}).then(res => {
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
  data.form.img = res.data
}

const reset = () => {
  data.title = null
  data.status = null
  load()
}

const isAllowedStatus = (status) => {
  if (data.form.status === '停止上映') {
    return false;
  }
  
  const currentDate = new Date().toISOString().slice(0, 10);
  const startDate = data.form.start;
  
  if (status === '待上映') {
    return startDate > currentDate;
  }
  
  if (status === '已上映') {
    return startDate <= currentDate;
  }
  
  return true;
}

load()
loadType()
loadArea()
</script>
<style>
.line1 {
  overflow:hidden;
  text-overflow:ellipsis;
  white-space:nowrap;
}
.widthStyle {
  width: 40%;
}
</style>