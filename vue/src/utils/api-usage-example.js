/**
 * API模块使用示例
 * 本文件仅作为示例，不会在项目中实际使用
 */

import { 
  baseApiUrl,
  userApi, 
  filmApi, 
  orderApi, 
  paymentApi, 
  fileApi,
  recommendationApi
} from '@/utils/api';

// 示例组件
export default {
  data() {
    return {
      username: '',
      password: '',
      films: [],
      orders: [],
      loadingFilms: false,
      uploadUrl: fileApi.uploadUrl,
    }
  },
  methods: {
    // 用户登录示例
    async login() {
      try {
        const response = await userApi.login({
          username: this.username,
          password: this.password
        });
        
        if (response.code === '200') {
          // 登录成功处理
          localStorage.setItem('xm-user', JSON.stringify(response.data));
          return true;
        } else {
          // 错误处理
          console.error(response.msg);
          return false;
        }
      } catch (error) {
        console.error('登录失败:', error);
        return false;
      }
    },
    
    // 加载电影列表示例
    async loadFilms() {
      this.loadingFilms = true;
      try {
        const response = await filmApi.getPage({
          pageNum: 1,
          pageSize: 10,
          status: '已上映'
        });
        
        if (response.code === '200') {
          this.films = response.data.list;
        }
      } catch (error) {
        console.error('加载电影失败:', error);
      } finally {
        this.loadingFilms = false;
      }
    },
    
    // 加载排行榜示例
    async loadRankings() {
      try {
        // 并行请求多个接口
        const [totalResponse, scoreResponse] = await Promise.all([
          filmApi.getTotalTop(),
          filmApi.getScoreTop()
        ]);
        
        if (totalResponse.code === '200' && scoreResponse.code === '200') {
          return {
            topTotal: totalResponse.data,
            topScore: scoreResponse.data
          };
        }
      } catch (error) {
        console.error('加载排行榜失败:', error);
      }
    },
    
    // 支付订单示例
    payOrder(orderNo) {
      // 使用支付API获取完整URL并打开支付页面
      window.open(paymentApi.alipayUrl(orderNo));
    },
    
    // 上传文件示例（模板中使用）
    /*
    <el-upload
      :action="uploadUrl" 
      :on-success="handleUploadSuccess"
      :before-upload="beforeUpload"
      list-type="picture"
    >
      <el-button type="primary">上传文件</el-button>
    </el-upload>
    */
    
    // 文件上传成功处理
    handleUploadSuccess(response) {
      if (response.code === '200') {
        // 获取上传后的文件URL
        const fileUrl = response.data;
        // 进行后续处理...
      }
    },
    
    // 上传前验证
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt2M = file.size / 1024 / 1024 < 2;
      
      if (!isImage) {
        alert('只能上传图片文件!');
        return false;
      }
      if (!isLt2M) {
        alert('图片大小不能超过2MB!');
        return false;
      }
      return true;
    },
    
    // 获取用户推荐电影
    async getRecommendations() {
      const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
      if (!user.id) {
        console.error('用户未登录，无法获取推荐');
        return [];
      }
      
      try {
        const response = await recommendationApi.getRecommendations(user.id);
        if (response.code === '200') {
          return response.data;
        }
        return [];
      } catch (error) {
        console.error('获取推荐失败:', error);
        return [];
      }
    }
  }
}; 