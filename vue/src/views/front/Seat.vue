<template>
  <div style="width: 50%; margin: 40px auto; border: 1px solid #eeeeee; padding: 20px">
    <div style="font-weight: bold; font-size: 26px; text-align: center">座位选择</div>
    <div style="margin-top: 50px; display: flex">
      <div style="flex: 3">
        <div style="display: flex; text-align: center">
          <div style="flex: 1">
            <el-button type="danger"></el-button><span style="margin-left: 5px">已售座位</span>
          </div>
          <div style="flex: 1">
            <el-button type="success"></el-button><span style="margin-left: 5px">可选座位</span>
          </div>
          <div style="flex: 1">
            <el-button type="warning"></el-button><span style="margin-left: 5px">已选座位</span>
          </div>
        </div>
        <div style="height: 5px; background-color: #d9d9d9; margin: 30px 40px"></div>
        <div style="text-align: center">荧幕中央</div>
        <div style="text-align: center; padding: 40px 60px">
          <el-row :gutter="5" v-for="row in 8" style="margin-bottom: 12px">
            <el-col :span="3" v-for="col in 8">
              <el-button style="height: 30px; width: 30px" :type="initType(row, col)" @click="selectSeat(row, col)" :disabled="isAlreadySelected(row, col)">{{ col }}</el-button>
            </el-col>
          </el-row>
        </div>
      </div>
      <div style="flex: 2; background-color: #fdfcfc; padding: 20px">
        <div style="display: flex">
          <img :src="data.film.img" alt="" style="width: 100px; height: 120px">
          <div style="flex: 1; margin-left: 10px">
            <div style="font-size: 20px; font-weight: bold">{{ data.film.title }}</div>
            <div style="margin-top: 10px">
              <span style="color: #666666">类型：</span>
              <span style="margin-right: 5px" v-for="item in data.film.types">{{ item }}</span>
            </div>
            <div style="margin-top: 10px">时长：{{ data.film.time }}分钟</div>
          </div>
        </div>
        <div style="margin-top: 20px; font-size: 15px"><span style="color: #666666">影院：</span>{{ data.showData.cinemaName }}</div>
        <div style="margin-top: 10px; font-size: 15px"><span style="color: #666666">影厅：</span>{{ data.showData.roomName }}</div>
        <div style="margin-top: 10px; font-size: 15px"><span style="color: #666666">语言：</span>{{ data.film.language }}</div>
        <div style="margin-top: 10px; font-size: 15px"><span style="color: #666666">场次：</span>{{ data.showData.time }}</div>
        <div style="margin-top: 10px; font-size: 15px"><span style="color: #666666">票价：</span><span style="color: red">￥ {{ data.showData.price }}/张</span></div>
        <div style="margin-top: 10px; font-size: 15px; display: flex; align-items: center">
          <div style="color: #666666; width: 50px">座位：</div>
          <div style="flex: 1">
            <el-row :gutter="10">
              <el-col :span="6" v-for="item in data.selectedSeats">
                <el-button type="info" style="width: 60px; height: 30px">{{ item.row + '排' + item.col + '座' }}</el-button>
              </el-col>
            </el-row>
          </div>
        </div>
        <div style="margin-top: 20px; font-size: 15px">
          <span style="color: #666666">费用：
            <span style="color: red">￥<span style="font-size: 25px">{{ data.total.toFixed(2) }}</span>
            </span></span>
        </div>
        <div style="text-align: center; margin-top: 50px">
          <el-button style="padding: 20px 100px; border-radius: 20px" type="danger" @click="submit">确认选座</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import router from "@/router/index.js";


const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  showId: router.currentRoute.value.query.id,
  showData: {},
  film: {},
  total: 0,
  // 存储所有已经出售过的座位的按钮坐标
  alreadySaleSeats: [],
  // 存储用户当前选中的座位按钮坐标
  selectedSeats: []
})
const isAlreadySelected = (row, col) => {
  let arr = data.alreadySaleSeats.filter(v => v.row === row && v.col === col)
  return arr && arr.length > 0;
}
const initType = (row, col) => {
  // 先判断该坐标的座位有没有在alreadySaleSeats里面，也就是说有么有出售过
  let alreadyArr = data.alreadySaleSeats.filter(v => v.row === row && v.col === col)
  if (alreadyArr && alreadyArr.length > 0) {
    return "danger"
  }
  // 再判断该坐标的座位有没有在selectedSeats里面，也就是说用户当前有没有选
  let selectedArr = data.selectedSeats.filter(v => v.row === row && v.col === col)
  if (selectedArr && selectedArr.length > 0) {
    return "warning"
  }
  return "success"
}
const selectSeat = (row, col) => {
  // 如果当前没有选择任何一个座位的时候，无脑塞
  if (!data.selectedSeats.length) {
    addSeat(row, col)
  } else {

    let arr = data.selectedSeats.filter(v => v.row === row && v.col === col)
    if (arr && arr.length > 0) {
      // 我就要把这个座位取消掉，也就是说要把这个坐标从selectedSeats里面移除掉
      data.selectedSeats = data.selectedSeats.filter(v => !(v.row === row && v.col === col))
    } else {
      // 如果当前点击的座位坐标不在selectedSeats里面，那就说明这个座位仍然是第一次点击，无脑塞进去
      addSeat(row, col)
    }
  }
  data.total = data.showData.price * data.selectedSeats.length
}
const addSeat = (row, col) => {
  let seat = {
    row: row, col: col
  }
  if (data.selectedSeats.length >= 4) {
    ElMessage.info('最多只能选择4个座位')
    return
  }
  data.selectedSeats.push(seat)
}
const loadShow = () => {
  data.showId = router.currentRoute.value.query.id
  request.get('/show/selectById/' +  data.showId).then(res => {
    if (res.code === '200') {
      data.showData = res.data
      data.film = res.data.film
      loadAlreadySeat()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const submit = () => {
  // 判断是否有选中座位
  if (data.selectedSeats.length === 0) {
    ElMessage.error('请至少选择一张座位');
    return;
  }

  // 弹出确认框
  ElMessageBox.confirm('您确定选择这些座位并且下单购买吗？', '购票确认', { type: 'warning' }).then(res => {
    let ordersData = {
      filmId: data.film.id,
      time: data.showData.time,
      cinemaId: data.showData.cinemaId,
      roomId: data.showData.roomId,
      seatList: data.selectedSeats,
      showId: data.showId
    }

    // 提交订单请求
    request.post('/orders/add', ordersData).then(res => {
      if (res.code === '200') {
        ElMessage.success('下单成功')
        // 跳转到订单页面（购票记录）
        router.push('/front/orders')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

const loadAlreadySeat = () => {
  request.get('/orders/selectAll', {
    params: {
      filmId: data.film.id,
      cinemaId: data.showData.cinemaId,
      roomId: data.showData.roomId,
    }
  }).then(res => {
    if (res.code === '200') {
      let orders = res.data.filter(v => v.status !== '已退票')
      orders.forEach(item => {
        item.seatList.forEach(v => {
          data.alreadySaleSeats.push(v)
        })
      })
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadShow()
</script>

<style>

</style>