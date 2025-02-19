<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <!-- 电影ID查询 -->
      <el-input v-model="data.filmId" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入电影ID查询"></el-input>

      <!-- 评论内容查询 -->
      <el-input v-model="data.commentText" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入评论内容查询"></el-input>

      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="userName" label="用户姓名" />
        <el-table-column prop="filmId" label="电影ID" />
        <el-table-column prop="commentText" label="评论内容" />
        <el-table-column prop="createTime" label="评论时间" />
        <el-table-column label="操作" width="150" fixed="right">
          <template v-slot="scope">
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <!-- 编辑评论弹窗 -->
    <el-dialog title="修改评论" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="commentText" label="评论内容">
          <el-input v-model="data.form.commentText" type="textarea" rows="4" placeholder="请输入评论内容"></el-input>
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
import { reactive } from 'vue'
import request from '@/utils/request.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit } from '@element-plus/icons-vue'

const data = reactive({
  formVisible: false,
  form: {},  // 这里存储表单数据
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  filmId: null,
  commentText: '',  // 增加用于评论内容查询的字段
  ids: []
})

const load = () => {
  // 请求后端接口，确保传递的参数正确
  request.get('/comment/selectComments', {
    params: {
      pageNum: data.pageNum,  // 分页页码
      pageSize: data.pageSize,  // 分页大小
      filmId: data.filmId,  // 根据电影 ID 查询评论
      commentText: data.commentText  // 根据评论内容查询
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []  // 设置评论数据
      data.total = res.data?.total  // 设置评论总数
    } else {
      ElMessage.error(res.msg)
    }
  }).catch(err => {
    console.error(err)
    ElMessage.error("加载数据失败")  // 加载数据失败的提示
  })
}

const del = (commentId) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete(`/comment/deleteCommentByAdmin/${commentId}`).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()  // 删除成功后重新加载评论列表
      } else {
        ElMessage.error(res.msg)
      }
    }).catch(err => {
      console.error(err)
      ElMessage.error("删除失败")
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
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(() => {
    request.delete("/comment/deleteBatchComment", { data: data.ids }).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    }).catch(err => {
      console.error(err)
      ElMessage.error("批量删除失败")
    })
  }).catch(err => {
    console.error(err)
  })
}

const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const reset = () => {
  data.filmId = null
  data.commentText = ''  // 清空评论内容查询字段
  load()
}

// 处理编辑
const handleEdit = (row) => {
  data.form = { ...row }  // 复制数据到表单
  data.formVisible = true  // 显示弹窗
}

// 保存评论
const save = () => {
  if (data.form.id) {
    // 如果评论已有ID，进行更新
    request.put(`/comment/updateComment`, data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success("评论更新成功")
        data.formVisible = false  // 关闭弹窗
        load()  // 重新加载评论列表
      } else {
        ElMessage.error(res.msg)
      }
    }).catch(err => {
      console.error(err)
      ElMessage.error("更新评论失败")
    })
  } else {
    // 如果没有ID，则是新增操作
    request.post('/comment/addComment', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success("评论添加成功")
        data.formVisible = false  // 关闭弹窗
        load()  // 重新加载评论列表
      } else {
        ElMessage.error(res.msg)
      }
    }).catch(err => {
      console.error(err)
      ElMessage.error("添加评论失败")
    })
  }
}

load()
</script>
