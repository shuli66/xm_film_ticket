<template>
  <div class="film-detail-wrapper">
    <!-- 电影头部信息 -->
    <div class="film-header">
      <div class="film-header-content">
        <div class="film-poster">
          <img :src="data.filmData.img" :alt="data.filmData.title">
          </div>
        <div class="film-info">
          <h1 class="film-title">{{ data.filmData.title }}</h1>
          <div class="film-subtitle">{{ data.filmData.english }}</div>
          <div class="film-meta">
            <div class="film-types">
              <span v-for="item in data.filmData.types" :key="item" class="type-tag">{{ item }}</span>
            </div>
            <div class="film-details">
              <span>{{ data.filmData.areaName }}</span>
              <span class="separator">|</span>
              <span>{{ data.filmData.time }}分钟</span>
              <span class="separator">|</span>
              <span>{{ data.filmData.start }} 中国大陆上映</span>
            </div>
          </div>
          <div class="film-actions">
            <div class="action-group">
              <el-button 
                :class="{'action-btn': true, 'active': data.collectFlag}"
                @click="collect">
                <el-icon :size="24">
                  <component :is="data.collectFlag ? 'StarFilled' : 'Star'" />
                </el-icon>
                <span>{{ data.collectFlag ? '已想看' : '想看' }}</span>
              </el-button>
              <el-button 
                :class="{'action-btn': true, 'active': data.scoreFlag}"
                @click="data.filmData.status === '已上映' ? scoreInit() : null"
                :disabled="data.filmData.status !== '已上映'">
                <el-icon :size="24">
                  <component :is="data.scoreFlag ? 'Comment' : 'ChatDotSquare'" />
                </el-icon>
                <span>{{ data.scoreFlag ? '已评分' : '评分' }}</span>
              </el-button>
            </div>
            <el-button 
              v-if="data.filmData.status === '已上映'" 
              class="buy-ticket-btn" 
              @click="$router.push('/front/filmCinema?id=' + data.filmData.id)">
              特惠购票
            </el-button>
            <el-button 
              v-else-if="data.filmData.status === '待上映'" 
              class="coming-soon-btn" 
              @click="showComingSoonAlert">
              即将上映
            </el-button>
            <el-button 
              v-else 
              class="ended-btn" 
              disabled>
              已下映
            </el-button>
          </div>
        </div>
        <div class="film-stats">
          <div class="stat-item">
            <div class="stat-label">影片口碑</div>
            <div class="stat-content">
              <div class="score-display">
                <span class="score-value">{{ data.filmData.score }}</span>
                <span class="score-label">分</span>
        </div>
              <div class="score-details">
                <el-rate v-model="data.halfScore" disabled />
                <div class="score-count">{{ data.scoreTime }}人评分</div>
            </div>
          </div>
        </div>
          <div class="stat-item">
            <div class="stat-label">累计票房</div>
            <div class="stat-content">
              <div class="box-office">
                <span class="box-office-value">{{ (data.filmData.total * 1).toFixed(2) }}</span>
                <span class="box-office-label">元</span>
      </div>
    </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 电影详情内容 -->
    <div class="film-content">
      <div class="main-content">
        <!-- 剧情简介 -->
        <div class="content-section">
          <h2 class="section-title">剧情简介</h2>
          <div class="section-content">{{ data.filmData.content }}</div>
        </div>

        <!-- 演职人员 -->
        <div class="content-section">
          <h2 class="section-title">演职人员</h2>
          <div v-if="data.actorData && data.actorData.length > 0" class="actor-section">
          <el-row :gutter="10">
              <el-col :span="5" v-for="item in data.actorData.filter(v => v.role === '导演')" :key="item.id" class="actor-item">
                <div class="actor-role">{{ item.role }}</div>
                <img :src="item.avatar" :alt="item.name" class="actor-avatar">
                <div class="actor-name">{{ item.name }}</div>
            </el-col>
              <el-col :span="5" v-for="item in data.actorData.filter(v => v.role === '编剧')" :key="item.id" class="actor-item">
                <div class="actor-role">{{ item.role }}</div>
                <img :src="item.avatar" :alt="item.name" class="actor-avatar">
                <div class="actor-name">{{ item.name }}</div>
            </el-col>
              <el-col :span="5" v-for="item in data.actorData.filter(v => v.role === '主演')" :key="item.id" class="actor-item">
                <div class="actor-role">{{ item.role }}</div>
                <img :src="item.avatar" :alt="item.name" class="actor-avatar">
                <div class="actor-name">{{ item.name }}</div>
            </el-col>
              <el-col :span="5" v-for="item in data.actorData.filter(v => v.role === '演员')" :key="item.id" class="actor-item">
                <div class="actor-role">{{ item.role }}</div>
                <img :src="item.avatar" :alt="item.name" class="actor-avatar">
                <div class="actor-name">{{ item.name }}</div>
            </el-col>
          </el-row>
        </div>
          <div class="empty-state" v-else>管理员暂未录入演职人员信息</div>
        </div>

        <!-- 出品信息 -->
        <div class="content-section">
          <h2 class="section-title">出品信息</h2>
          <div class="info-grid">
            <div class="info-item">
              <el-icon class="info-icon"><VideoCamera /></el-icon>
              <div class="info-content">
                <div class="info-title">出品发行</div>
              <el-popover
                  placement="top-start"
                  title="出品发行"
                  :width="220"
                  trigger="hover"
                  :content="data.filmData.employ"
              >
                <template #reference>
                    <div class="info-text line1">{{ data.filmData.employ }}</div>
                </template>
              </el-popover>
            </div>
          </div>
            <div class="info-item">
              <el-icon class="info-icon"><VideoPlay /></el-icon>
              <div class="info-content">
                <div class="info-title">技术参考</div>
                <div class="info-text">{{ data.filmData.time }} 分钟</div>
            </div>
          </div>
        </div>
        </div>

        <!-- 票房信息 -->
        <div class="content-section">
          <h2 class="section-title">票房</h2>
          <div class="box-office-grid">
            <div class="box-office-item">
              <div class="box-office-value">{{ data.priceRanking }}</div>
              <div class="box-office-label">票房排名</div>
          </div>
            <div class="box-office-item">
              <div class="box-office-value">{{ (data.todayPrice * 1).toFixed(2) }}</div>
              <div class="box-office-label">今日票房（元）</div>
          </div>
            <div class="box-office-item">
              <div class="box-office-value">{{ (data.filmData.total * 1).toFixed(2) }}</div>
              <div class="box-office-label">总票房（元）</div>
          </div>
        </div>
        </div>
      </div>

      <!-- 右侧内容区 -->
      <div class="sidebar">
        <!-- 预告视频 -->
        <div class="sidebar-section">
          <h2 class="section-title">预告视频</h2>
          <div class="trailer-list" v-if="data.videoData && data.videoData.length > 0">
            <div v-for="item in data.videoData" :key="item.id" class="trailer-item" @click="viewInit(item)">
              <div class="trailer-thumbnail">
                <img :src="item.img" :alt="item.name">
                <div class="play-icon">
                  <el-icon><VideoPlay /></el-icon>
                </div>
              </div>
              <div class="trailer-info">
                <div class="trailer-title line2">{{ item.name }}</div>
                <div class="trailer-time">{{ item.time }}</div>
              </div>
            </div>
          </div>
          <div class="empty-state" v-else>暂无预告片</div>
        </div>

        <!-- 用户评论 -->
        <div class="sidebar-section">
          <h2 class="section-title">
            <span>用户评论</span>
            <span class="comment-count" v-if="data.commentData.length > 0">({{ data.commentData.length }})</span>
          </h2>
          
          <!-- 添加评论区 -->
          <div class="comment-form">
            <div class="comment-form-header">
              <el-avatar :size="40" :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"></el-avatar>
              <div class="comment-form-info">
                <span class="comment-form-username">{{ data.user.username || '游客' }}</span>
                <span class="comment-form-tip">分享你的观影感受</span>
              </div>
            </div>
            <el-input
                v-model="data.newComment"
                type="textarea"
                :rows="3"
                placeholder="说说你对这部电影的看法..."
                :disabled="data.isCommenting"
            />
            <div class="comment-form-footer">
              <span class="comment-tips">文明评论，理性发言</span>
              <el-button
                  type="primary"
                  :loading="data.isCommenting"
                  @click="submitComment"
                  class="submit-comment-btn"
                  :disabled="!data.user.id"
              >
                {{ data.user.id ? '发布评论' : '请先登录' }}
              </el-button>
            </div>
          </div>
          
          <!-- 评论列表 -->
          <div class="comments-section">
            <div v-if="data.commentData.length > 0" class="comments-list">
              <div v-for="(comment, index) in data.commentData" :key="index" class="comment-item">
                <div class="comment-header">
                  <img :src="comment.userAvatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" :alt="comment.userName" class="comment-avatar">
                  <div class="comment-user">
                    <span class="user-name">{{ comment.userName }}</span>
                    <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
                  </div>
                  <div v-if="comment.userId === data.user.id" class="comment-actions">
                    <el-button type="text" @click="deleteComment(comment.id)">
                      <el-icon><DeleteFilled /></el-icon>
                      <span>删除</span>
                    </el-button>
                  </div>
                </div>
                <div class="comment-content">{{ comment.commentText }}</div>
                <div class="comment-footer">
                  <el-button type="text" class="comment-like-btn">
                    <el-icon><Star /></el-icon>
                    <span>有用</span>
                  </el-button>
                </div>
              </div>
            </div>
            <div v-else class="empty-state">暂无评论，快来发表第一条评论吧！</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 视频播放对话框 -->
    <el-dialog
      v-model="data.formVisible"
      title="预告视频"
      width="50%"
      destroy-on-close
      class="video-dialog"
    >
      <div class="video-content">
        <h3 class="video-title">{{ data.filmData.title }} - 预告片</h3>
        <div class="video-name">{{ data.form.name }}</div>
        <video :src="data.form.video" controls class="video-player"></video>
      </div>
    </el-dialog>

    <!-- 评分对话框 -->
    <el-dialog
      v-model="data.scoreVisible"
      title="电影评分"
      width="40%"
      destroy-on-close
      class="score-dialog"
    >
      <el-form ref="form" label-width="70px">
        <el-form-item prop="score" label="电影评分">
          <el-rate
              v-model="data.score"
              :max="10"
              show-score
              text-color="#ff9900"
              score-template="{value} 分"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.scoreVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitScore">提 交</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, ref, defineEmits} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import { Star } from '@element-plus/icons-vue';

// 声明组件可接收的事件
const emit = defineEmits(['updateUser']);

// 添加时间格式化函数
const formatTime = (time) => {
  if (!time) return '暂无时间';
  const date = new Date(time);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}`;
};

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  filmId: router.currentRoute.value.query.id,
  filmData: {},
  videoData: [],
  form: {},
  formVisible: false,
  actorData: [],
  collectFlag: false,
  score: 10,
  scoreVisible: false,
  scoreFlag: false,
  scoreTime: 0,
  halfScore: 0,
  todayPrice: 0,
  priceRanking: 0,
  commentVisible: false,  // 控制电影评论对话框的显示与隐藏
  commentData: [],             // 存储评论数据
  newComment: '',              // 用来绑定用户输入的新评论内容
  isCommenting: false,         // 防止重复提交评论
})

// 加载电影评论数据
const loadComments = () => {
  request.get('/comment/selectComments', {
    params: {
      filmId: data.filmId,  // 传递电影ID来获取该电影的评论
      commentText: data.commentText,  // 如果有过滤条件
      pageNum: data.pageNum,
      pageSize: data.pageSize
    }
  }).then(res => {
    console.log('评论数据', res.data);  // 打印返回的数据
    if (res.code === '200') {
      data.commentData = res.data.list;  // 确保数据被正确赋值
    } else {
      ElMessage.error(res.msg);
    }
  }).catch(error => {
    ElMessage.error('加载评论失败');
    console.error(error);
  });
};

// 删除评论
const deleteComment = (commentId) => {
  const userId = data.user.id;  // 获取当前登录用户的ID

  // 请求删除评论接口，传递commentId和userId
  request.delete(`/comment/deleteComment/${commentId}`, {
    params: {
      commentId: commentId,
      userId: userId
    }
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('评论删除成功');
      loadComments();  // 删除成功后重新加载评论
    } else {
      ElMessage.error(res.msg);  // 如果失败，显示错误消息
    }
  }).catch(error => {
    ElMessage.error('删除评论失败');
    console.error(error);
  });
};

// 提交新评论
const submitComment = () => {
  if (!data.newComment.trim()) {
    ElMessage.warning('评论内容不能为空');
    return;
  }

  data.isCommenting = true;  // 设置评论提交状态

  // 获取当前时间戳
  const currentTime = new Date().toISOString();  // 格式化时间为 ISO 字符串

  // 提交评论数据，包含时间戳
  request.post('comment/addComment', {
    filmId: data.filmId,
    userId: data.user.id,  // 用户ID
    commentText: data.newComment,
    createTime: currentTime  // 添加创建时间
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('评论提交成功');
      data.newComment = '';  // 清空输入框
      loadComments();        // 重新加载评论
    } else {
      ElMessage.error(res.msg);  // 显示错误信息
    }
  }).catch(error => {
    ElMessage.error('提交评论失败');
    console.error(error);
  }).finally(() => {
    data.isCommenting = false;  // 重置评论提交状态
  });
};

loadComments();  // 加载评论数据

const loadTodayPrice = () => {
  request.get('/orders/selectTodayPrice/' + data.filmId).then(res => {
    if (res.code === '200') {
      data.todayPrice = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadTodayPrice()
const loadPriceRanking = () => {
  request.get('/film/selectPriceRanking/' + data.filmId).then(res => {
    if (res.code === '200') {
      data.priceRanking = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadPriceRanking()
const scoreInit = () => {
  data.scoreVisible = true
}
const submitScore = () => {
  let scoreData = {
    filmId: data.filmId,
    score: data.score
  }
  request.post('/score/add', scoreData).then(res => {
    if (res.code === '200') {
      ElMessage.success('评分成功')
      data.scoreVisible = false
      loadFilm()
      loadScore()
      loadScoreTime()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadScore = () => {
  request.get('/score/selectAll', {
    params: {
      filmId: data.filmId,
      userId: data.user.id
    }
  }).then(res => {
    if (res.code === '200') {
      data.scoreFlag = res.data.length
    }
  })
}
const loadScoreTime = () => {
  request.get('/score/selectAll', {
    params: {
      filmId: data.filmId,
    }
  }).then(res => {
    if (res.code === '200') {
      data.scoreTime = res.data.length
    }
  })
}
loadScore()
loadScoreTime()
const loadCollect = () => {
  request.get('/collect/selectAll', {
    params: {
      userId: data.user.id,
      filmId: data.filmId
    }
  }).then(res => {
    if (res.code === '200') {
      data.collectFlag = res.data.length;
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const showComingSoonAlert = () => {
  ElMessage({
    message: `该影片将于 ${data.filmData.start} 上映，敬请期待！`,
    type: 'info',
    duration: 3000,
    showClose: true,
    center: true
  });
}
const collect = () => {
  let collectData = {
    userId: data.user.id,
    filmId: data.filmId
  }
  request.post('/collect/add', collectData).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      loadCollect()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadFilm = () => {
  data.filmId = router.currentRoute.value.query.id
  request.get('/film/selectById/' + data.filmId).then(res => {
    if (res.code === '200') {
      data.filmData = res.data
      data.halfScore = (data.filmData.score / 2).toFixed(1)
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadVideo = () => {
  request.get('/video/selectAll', {
    params: {
      filmId: data.filmId
    }
  }).then(res => {
    if (res.code === '200') {
      data.videoData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadActor = () => {
  request.get('/actor/selectAll', {
    params: {
      filmId: data.filmId
    }
  }).then(res => {
    if (res.code === '200') {
      data.actorData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const viewInit = (item) => {
  data.form = JSON.parse(JSON.stringify(item))
  data.formVisible = true
}
loadFilm()
loadVideo()
loadActor()
loadCollect()
</script>

<style scoped>
.film-detail-wrapper {
  min-height: 100vh;
  background: #f5f7fa;
}

/* 头部样式 */
.film-header {
  background: linear-gradient(135deg, #46187e 0%, #32046b 50%, #32115b 100%);
  padding: 40px 0;
  position: relative;
  overflow: hidden;
}

.film-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" viewBox="0 0 100 100"><rect width="100" height="100" fill="none"/><circle cx="50" cy="50" r="40" stroke="rgba(255,255,255,0.1)" stroke-width="1" fill="none"/></svg>') repeat;
  opacity: 0.1;
  pointer-events: none;
}

.film-header-content {
  width: 75%;
  margin: 0 auto;
  display: flex;
  gap: 40px;
  position: relative;
  z-index: 1;
}

.film-poster {
  width: 240px;
  height: 320px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;
  position: relative;
}

.film-poster:hover {
  transform: translateY(-5px);
}

.film-poster::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0,0,0,0) 0%, rgba(0,0,0,0.1) 100%);
  pointer-events: none;
}

.film-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.film-poster:hover img {
  transform: scale(1.05);
}

.film-info {
  flex: 1;
  color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.film-title {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 8px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.film-subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 20px;
}

.film-meta {
  margin-bottom: 30px;
}

.film-types {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.type-tag {
  background: rgba(255, 255, 255, 0.1);
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 14px;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.type-tag:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.film-details {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.separator {
  color: rgba(255, 255, 255, 0.5);
}

.film-actions {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: auto;
}

.action-group {
  display: flex;
  gap: 15px;
}

.action-btn {
  flex: 1;
  height: 40px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: #fff;
  font-size: 16px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.action-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.action-btn.active {
  background: rgba(255, 255, 255, 0.2);
}

.action-btn .el-icon {
  transition: transform 0.3s ease;
}

.action-btn:hover .el-icon {
  transform: scale(1.1);
}

.buy-ticket-btn {
  width: 160px;
  height: 44px;
  border-radius: 22px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(45deg, #ff6b6b, #ff9a8b);
  color: #fff;
  border: none;
  box-shadow: 0 6px 16px rgba(255, 107, 107, 0.4);
  transition: all 0.3s ease;
}

.buy-ticket-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(255, 107, 107, 0.5);
}

/* 即将上映按钮样式 */
.coming-soon-btn {
  width: 160px;
  height: 44px;
  border-radius: 22px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(45deg, #ffa502, #ffb142);
  color: #fff;
  border: none;
  box-shadow: 0 6px 16px rgba(255, 165, 2, 0.4);
  transition: all 0.3s ease;
}

.coming-soon-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(255, 165, 2, 0.5);
}

/* 已下映按钮样式 */
.ended-btn {
  width: 160px;
  height: 44px;
  border-radius: 22px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(45deg, #a5b1c2, #d1d8e0);
  color: #fff;
  border: none;
  box-shadow: 0 6px 16px rgba(165, 177, 194, 0.4);
  opacity: 0.9;
}

.film-stats {
  width: 220px;
  color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.stat-item {
  margin-bottom: 30px;
  background: rgba(255, 255, 255, 0.05);
  padding: 20px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.stat-item:hover {
  background: rgba(255, 255, 255, 0.1);
  transform: translateY(-2px);
}

.stat-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 10px;
}

.score-display {
  display: flex;
  align-items: baseline;
  margin-bottom: 10px;
}

.score-value {
  font-size: 36px;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.score-label {
  font-size: 16px;
  margin-left: 4px;
}

.score-details {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.score-count {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.box-office {
  display: flex;
  align-items: baseline;
}

.box-office-value {
  font-size: 28px;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.box-office-label {
  font-size: 16px;
  margin-left: 4px;
}

/* 内容区域样式 */
.film-content {
  width: 80%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 0;
  display: flex;
  gap: 40px;
}

.main-content {
  flex: 3;
}

.sidebar {
  flex: 2;
  position: relative;
}

.content-section {
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  margin-bottom: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.content-section:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.section-title {
  font-size: 22px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
  padding-left: 12px;
  border-left: 4px solid #ef4238;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.comment-count {
  font-size: 14px;
  color: #666;
  font-weight: normal;
  margin-left: 8px;
}

.section-title::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -5px;
  width: 40px;
  height: 2px;
  background: #ef4238;
  transition: width 0.3s ease;
}

.sidebar-section:hover .section-title::after,
.content-section:hover .section-title::after {
  width: 60px;
}

.section-content {
  color: #333;
  line-height: 1.8;
  text-align: justify;
}

/* 演职人员样式 */
.actor-section {
  margin-top: 20px;
}

.actor-item {
  margin-bottom: 20px;
  text-align: center;
  transition: all 0.3s ease;
  padding: 10px;
  border-radius: 8px;
}

.actor-item:hover {
  background: #f8f9fa;
  transform: translateY(-2px);
}

.actor-role {
  font-size: 16px;
  color: #333;
  margin-bottom: 10px;
  font-weight: 500;
}

.actor-avatar {
  width: 100%;
  height: 110px;
  border-radius: 50%;
  margin: 10px 0;
  object-fit: cover;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.actor-item:hover .actor-avatar {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  transform: scale(1.05);
}

.actor-name {
  font-size: 14px;
  color: #333;
  transition: color 0.3s ease;
}

.actor-item:hover .actor-name {
  color: #ef4238;
}

/* 出品信息样式 */
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.info-item:hover {
  background: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transform: translateY(-2px);
}

.info-icon {
  font-size: 24px;
  color: #2d98f3;
  margin-right: 15px;
  transition: transform 0.3s ease;
}

.info-item:hover .info-icon {
  transform: scale(1.1);
}

.info-content {
  flex: 1;
}

.info-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
}

.info-text {
  font-size: 14px;
  color: #666;
}

/* 票房信息样式 */
.box-office-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  background: #f8f9fa;
  padding: 30px;
  border-radius: 8px;
}

.box-office-item {
  text-align: center;
  transition: all 0.3s ease;
  padding: 20px;
  border-radius: 8px;
}

.box-office-item:hover {
  background: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transform: translateY(-2px);
}

.box-office-value {
  font-size: 28px;
  font-weight: 600;
  color: #ef4238;
  margin-bottom: 10px;
  transition: color 0.3s ease;
}

.box-office-item:hover .box-office-value {
  color: #d63a31;
}

.box-office-label {
  font-size: 16px;
  color: #666;
}

/* 评论样式 */
.comments-section {
  max-height: 600px;
  overflow-y: auto;
  padding-right: 5px;
  margin-top: 25px;
}

.comment-item {
  background: #f8f9fa;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 15px;
  transition: all 0.3s ease;
  border-left: 3px solid transparent;
}

.comment-item:hover {
  background: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border-left-color: #ef4238;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 12px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.comment-item:hover .comment-avatar {
  transform: scale(1.1);
}

.comment-user {
  flex: 1;
}

.user-name {
  font-weight: 600;
  color: #333;
  margin-right: 10px;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-actions {
  opacity: 0;
  transition: opacity 0.3s ease;
}

.comment-item:hover .comment-actions {
  opacity: 1;
}

.comment-content {
  color: #333;
  line-height: 1.6;
  margin-bottom: 15px;
  word-break: break-word;
}

.comment-footer {
  display: flex;
  justify-content: flex-end;
}

.comment-like-btn {
  color: #999;
  transition: all 0.3s ease;
}

.comment-like-btn:hover {
  color: #ef4238;
}

/* 预告视频样式 */
.trailer-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.trailer-item {
  display: flex;
  gap: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 10px;
  border-radius: 8px;
}

.trailer-item:hover {
  transform: translateX(5px);
  background: #f8f9fa;
}

.trailer-thumbnail {
  position: relative;
  width: 120px;
  height: 70px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.trailer-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.trailer-item:hover .trailer-thumbnail img {
  transform: scale(1.05);
}

.play-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 30px;
  height: 30px;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  transition: all 0.3s ease;
}

.trailer-item:hover .play-icon {
  background: rgba(239, 66, 56, 0.8);
  transform: translate(-50%, -50%) scale(1.1);
}

.trailer-info {
  flex: 1;
}

.trailer-title {
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
  transition: color 0.3s ease;
}

.trailer-item:hover .trailer-title {
  color: #ef4238;
}

.trailer-time {
  font-size: 12px;
  color: #999;
}

/* 对话框样式 */
.video-dialog {
  .video-content {
    padding: 20px;
}

  .video-title {
    font-size: 24px;
    font-weight: 600;
    text-align: center;
    margin-bottom: 20px;
    color: #333;
  }

  .video-name {
    color: #666;
    margin-bottom: 20px;
  }

  .video-player {
    width: 100%;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
}

.score-dialog {
  .el-rate {
    font-size: 24px;
  }
}

/* 通用样式 */
.empty-state {
  color: #999;
  text-align: center;
  padding: 30px 0;
  font-size: 14px;
}

.line1 {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.line2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 响应式调整 */
@media (max-width: 1400px) {
  .film-header-content,
  .film-content {
    width: 85%;
  }
}

@media (max-width: 1200px) {
  .film-header-content,
  .film-content {
    width: 90%;
  }
}

@media (max-width: 992px) {
  .film-header-content {
    flex-direction: column;
    padding: 30px 0;
  }

  .film-poster {
    margin: 0 auto 20px;
  }

  .film-stats {
    margin-top: 20px;
    display: flex;
    gap: 20px;
  }

  .film-content {
    flex-direction: column;
  }

  .main-content {
    order: 1;
  }

  .sidebar {
    order: 2;
    width: 100%;
  }
}

@media (max-width: 768px) {
  .film-header-content,
  .film-content {
    width: 95%;
    padding: 20px 0;
  }

  .film-title {
    font-size: 24px;
  }

  .film-poster {
    width: 180px;
    height: 240px;
  }

  .section-title {
    font-size: 20px;
  }

  .film-stats {
    flex-direction: column;
    gap: 10px;
  }

  .info-grid,
  .box-office-grid {
    grid-template-columns: 1fr;
  }

  .comment-form-footer {
    flex-direction: column;
    align-items: flex-start;
  }

  .comment-tips {
    margin-bottom: 10px;
  }

  .comment-header {
    flex-wrap: wrap;
  }

  .comment-actions {
    margin-top: 10px;
    width: 100%;
    opacity: 1;
  }

  .content-section,
  .sidebar-section {
    padding: 20px 15px;
  }
}

@media (max-width: 480px) {
  .film-poster {
    width: 150px;
    height: 200px;
  }

  .action-group {
    flex-direction: column;
    gap: 10px;
  }

  .action-btn,
  .buy-ticket-btn,
  .coming-soon-btn,
  .ended-btn {
    width: 100%;
  }
}

/* 侧边栏区域样式 */
.sidebar-section {
  background: #fff;
  border-radius: 12px;
  padding: 25px;
  margin-bottom: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.sidebar-section:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

/* 评论表单样式 */
.comment-form {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  margin-bottom: 25px;
  transition: all 0.3s ease;
}

.comment-form:hover {
  background: #fff;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.comment-form-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.comment-form-info {
  margin-left: 12px;
}

.comment-form-username {
  font-weight: 600;
  color: #333;
  display: block;
  margin-bottom: 4px;
}

.comment-form-tip {
  font-size: 12px;
  color: #999;
}

.comment-form-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}

.comment-tips {
  font-size: 12px;
  color: #999;
}

.submit-comment-btn {
  border-radius: 20px;
  font-weight: 600;
  background: linear-gradient(45deg, #ef4238, #ff6b6b);
  border: none;
  padding: 8px 20px;
  transition: all 0.3s ease;
}

.submit-comment-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(239, 66, 56, 0.3);
}

/* 美化滚动条 */
.comments-section::-webkit-scrollbar {
  width: 6px;
}

.comments-section::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.comments-section::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.comments-section::-webkit-scrollbar-thumb:hover {
  background: #aaa;
}
</style>