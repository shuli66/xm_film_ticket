<template>
  <div style="width: 55%; margin: 20px auto">
    <div style="border: 1px solid #eeeeee; padding: 15px 30px; border-radius: 5px">
      <div style="display: flex; align-items: center; border-bottom: 1px solid #eeeeee; padding: 10px 0">
        <div style="width: 60px; font-size: 16px">类型：</div>
        <div style="flex: 1">
          <el-row :gutter="10">
            <el-col :span="2">
              <div class="item_style" :class="{'item_active' : !data.typeFlag}" @click="changeTypeFlag(null)">全部</div>
            </el-col>
            <el-col :span="2" v-for="item in data.typeData">
              <div class="item_style" :class="{'item_active' : data.typeFlag === item.id}" @click="changeTypeFlag(item.id)">{{ item.title }}</div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div style="display: flex; align-items: center; border-bottom: 1px solid #eeeeee; padding: 10px 0">
        <div style="width: 60px; font-size: 16px">年代：</div>
        <div style="flex: 1">
          <el-row :gutter="10">
            <el-col :span="2">
              <div class="item_style" :class="{'item_active' : !data.yearFlag}" @click="changeYearFlag(null)">全部</div>
            </el-col>
            <el-col :span="2" v-for="item in data.yearData">
              <div class="item_style" :class="{'item_active' : data.yearFlag === item}" @click="changeYearFlag(item)">{{ item }}</div>
            </el-col>
          </el-row>
        </div>
      </div>
      <div style="display: flex; align-items: center; padding: 10px 0">
        <div style="width: 60px; font-size: 16px">区域：</div>
        <div style="flex: 1">
          <el-row :gutter="10">
            <el-col :span="2">
              <div class="item_style" :class="{'item_active' : !data.areaFlag}" @click="changeAreaFlag(null)">全部</div>
            </el-col>
            <el-col :span="3" v-for="item in data.areaData">
              <div class="item_style" :class="{'item_active' : data.areaFlag === item.id}" @click="changeAreaFlag(item.id)">{{ item.title }}</div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>
    <div style="margin-top: 50px">
      <el-row :gutter="15">
        <el-col :span="5" v-for="item in data.filmData" style="margin-bottom: 30px">
          <img :src="item.img" alt="" style="width: 100%; height: 230px; border-top-left-radius: 5px; border-top-right-radius: 5px; cursor: pointer" @click="$router.push('/front/filmDetail?id=' + item.id)">
          <div style="margin-top: 5px; font-size: 16px">{{ item.title }}</div>
          <div style="margin-top: 10px; display: flex; align-items: center">
            <div style="flex: 1">
              <el-tag v-if="item.status === '待上映'" type="warning">{{ item.status }}</el-tag>
              <el-tag v-if="item.status === '已上映'" type="success">{{ item.status }}</el-tag>
              <el-tag v-if="item.status === '停止上映'" type="danger">{{ item.status }}</el-tag>
            </div>
            <div style="color: orange; font-size: 20px; width: 50px">{{ item.score }}分</div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div style="margin: 20px 0 30px 0" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  typeFlag: null,
  areaFlag: null,
  yearFlag: null,
  typeData: [],
  areaData: [],
  yearData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  filmData: []
})
const load = () => {
  request.get('/film/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      typeId: data.typeFlag,
      areaId: data.areaFlag,
      year: data.yearFlag
    }
  }).then(res => {
    if (res.code === '200') {
      data.filmData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadType = () => {
  request.get('/type/selectAll').then(res => {
    if (res.code === '200') {
      data.typeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadArea = () => {
  request.get('/area/selectAll').then(res => {
    if (res.code === '200') {
      data.areaData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadYear = () => {
  request.get('/getYear').then(res => {
    if (res.code === '200') {
      data.yearData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const changeTypeFlag = (id) => {
  data.typeFlag = id
  load()
}
const changeYearFlag = (year) => {
  data.yearFlag = year
  load()
}
const changeAreaFlag = (id) => {
  data.areaFlag = id
  load()
}
loadType()
loadArea()
loadYear()
load()
</script>

<style scoped>
.item_style {
  padding: 8px 2px;
  border: 1px solid #cccccc;
  border-radius: 5px;
  text-align: center;
  margin: 5px 0;
  cursor: pointer;
}
.item_active {
  background-color: #ef4238;
  color: white;
  border: none;
}
.el-col-5 {
  width: 20%;
  max-width: 20%;
}
</style>