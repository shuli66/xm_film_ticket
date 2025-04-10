<template>
  <div style="width: 70%; margin: 20px auto" class="card">
    <div style="margin: 10px">
      <el-input v-model="data.orderNo" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入订单编号查询"></el-input>
      <el-input v-model="data.cinemaName" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入电影院名称查询"></el-input>
      <el-select v-model="data.status" placeholder="请选择订单状态" style="width: 240px; margin-right: 10px">
        <el-option label="待取票" value="待取票" />
        <el-option label="已取票" value="已取票" />
        <el-option label="已退票" value="已退票" />
        <el-option label="待支付" value="待支付" />
      </el-select>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div style="margin-top: 20px">
      <el-table tooltip-effect="dark" stripe :data="data.tableData">
        <el-table-column type="expand">
          <template #default="props">
            <el-descriptions class="margin-top" title="购票信息" :column="4" border>
              <el-descriptions-item label="订单号">{{ props.row.orderNo }}</el-descriptions-item>
              <el-descriptions-item label="用户姓名">{{ props.row.userName }}</el-descriptions-item>
              <el-descriptions-item label="电影名称">{{ props.row.filmName }}</el-descriptions-item>
              <el-descriptions-item label="电影图片">
                <el-image style="width: 40px; height: 40px; display: block" v-if="props.row.filmImg"
                          :src="props.row.filmImg" :preview-src-list="[props.row.filmImg]" preview-teleported></el-image>
              </el-descriptions-item>
              <el-descriptions-item label="影院名称">{{ props.row.cinemaName }}</el-descriptions-item>
              <el-descriptions-item label="座位号">
                <div v-if="props.row.seatList && props.row.seatList.length > 0">
                  <el-tag v-for="item in props.row.seatList" type="danger" style="margin-right: 5px">{{ item.row + '排' + item.col + '座' }}</el-tag>
                </div>
              </el-descriptions-item>
              <el-descriptions-item label="放映时间">{{ props.row.time }}</el-descriptions-item>
              <el-descriptions-item label="购票数量">{{ props.row.seatList?.length }}</el-descriptions-item>
              <el-descriptions-item label="订单金额">{{ props.row.price }}</el-descriptions-item>
              <el-descriptions-item label="下单时间">{{ props.row.createTime }}</el-descriptions-item>
              <el-descriptions-item label="订单状态">
                <el-tag v-if="props.row.status === '待取票'" type="warning">{{ props.row.status }}</el-tag>
                <el-tag v-if="props.row.status === '已取票'" type="success">{{ props.row.status }}</el-tag>
                <el-tag v-if="props.row.status === '已退票'" type="danger">{{ props.row.status }}</el-tag>
                <el-tag v-if="props.row.status === '待支付'" type="primary">{{ props.row.status }}</el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="订单号" show-overflow-tooltip />
        <el-table-column prop="filmName" label="电影名称" show-overflow-tooltip />
        <el-table-column prop="filmImg" label="电影封面" width="90">
          <template v-slot="scope">
            <el-image style="width: 40px; height: 40px; border-radius: 5px; display: block" v-if="scope.row.filmImg"
                      :src="scope.row.filmImg" :preview-src-list="[scope.row.filmImg]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="cinemaName" label="影院名称" show-overflow-tooltip />
        <el-table-column prop="roomName" label="影厅名称" />
        <el-table-column prop="time" label="放映时间" show-overflow-tooltip />
        <el-table-column prop="num" label="购票数量">
          <template v-slot="scope">
            {{ scope.row.seatList?.length }}
          </template>
        </el-table-column>
        <el-table-column prop="price" label="订单金额">
          <template v-slot="scope">
            {{ scope.row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === '待取票'" type="warning">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '已取票'" type="success">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '已退票'" type="danger">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '待支付'" type="primary">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template v-slot="scope">
            <div class="button-wrapper">
              <!-- 显示支付按钮（仅在待支付状态下） -->
              <el-button
                  v-if="scope.row.status === '待支付'"
                  type="primary"
                  @click="pay(scope.row)"
              >
                支付
              </el-button>

              <!-- 显示取票按钮（仅在待取票状态下） -->
              <el-button
                  v-else-if="scope.row.status === '待取票'"
                  type="success"
                  @click="showTicket(scope.row)"
              >
                取票
              </el-button>

              <!-- 显示退票按钮（仅在待取票状态下） -->
              <el-button
                  v-if="scope.row.status === '待取票'"
                  type="danger"
                  @click="cancel(scope.row.id)"
              >
                退票
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="margin-top: 20px" v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>

  <!-- 取票对话框 -->
  <el-dialog
    v-model="data.ticketVisible"
    title="电影票"
    width="400px"
    destroy-on-close
    class="ticket-dialog"
  >
    <div class="ticket-content">
      <div class="ticket-header">
        <div class="ticket-title">{{ data.currentTicket.filmName }}</div>
        <div class="ticket-code">取票码：{{ data.currentTicket.ticketCode }}</div>
      </div>
      <div class="ticket-qrcode">
        <qrcode-vue :value="data.currentTicket.ticketCode" :size="200" level="H" />
      </div>
      <div class="ticket-info">
        <div class="info-item">
          <span class="label">影院：</span>
          <span class="value">{{ data.currentTicket.cinemaName }}</span>
        </div>
        <div class="info-item">
          <span class="label">影厅：</span>
          <span class="value">{{ data.currentTicket.roomName }}</span>
        </div>
        <div class="info-item">
          <span class="label">场次：</span>
          <span class="value">{{ data.currentTicket.time }}</span>
        </div>
        <div class="info-item">
          <span class="label">座位：</span>
          <span class="value">
            <el-tag v-for="seat in data.currentTicket.seatList" :key="seat.id" type="danger" style="margin-right: 5px">
              {{ seat.row }}排{{ seat.col }}座
            </el-tag>
          </span>
        </div>
      </div>
      <div class="ticket-actions" v-if="data.currentTicket.status === '待取票'">
        <el-button type="primary" @click="confirmPickup">确认取票</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import QrcodeVue from 'qrcode.vue';

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  orderNo: null,
  cinemaName: null,
  status: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  ticketVisible: false,
  currentTicket: {
    filmName: '',
    cinemaName: '',
    roomName: '',
    time: '',
    seatList: [],
    ticketCode: ''
  }
})

const load = () => {
  request.get('/orders/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      orderNo: data.orderNo,
      cinemaName: data.cinemaName,
      status: data.status
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const reset = () => {
  data.orderNo = null
  data.cinemaName = null
  data.status = null
  load()
}

const cancel = (id) => {
  request.get('/orders/cancel/' + id).then(res => {
    if (res.code === '200') {
      ElMessage.success('退票成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const pay = (row) => {
  // 打开支付页面，URL 中携带订单编号作为参数
  window.open('http://localhost:9090/alipay/pay?orderNo=' + row.orderNo);
}

const showTicket = (row) => {
  // 生成取票码（这里使用订单号作为取票码）
  data.currentTicket = {
    ...row,
    ticketCode: row.orderNo
  }
  data.ticketVisible = true
}

const confirmPickup = () => {
  request.get('/orders/pickup/' + data.currentTicket.id).then(res => {
    if (res.code === '200') {
      ElMessage.success('取票成功')
      data.ticketVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

load();
</script>

<style scoped>
.card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.ticket-dialog {
  .ticket-content {
    padding: 20px;
    text-align: center;
  }

  .ticket-header {
    margin-bottom: 20px;
  }

  .ticket-title {
    font-size: 24px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
  }

  .ticket-code {
    font-size: 18px;
    color: #666;
  }

  .ticket-qrcode {
    margin: 20px 0;
    padding: 20px;
    background: #f5f7fa;
    border-radius: 8px;
  }

  .ticket-info {
    text-align: left;
    margin-top: 20px;
  }

  .info-item {
    margin-bottom: 10px;
    display: flex;
    align-items: flex-start;
  }

  .label {
    width: 60px;
    color: #666;
  }

  .value {
    flex: 1;
    color: #333;
  }

  .ticket-actions {
    margin-top: 20px;
    text-align: center;
  }
}
</style>
