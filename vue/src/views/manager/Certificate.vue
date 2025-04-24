<template>
  <div class="certificate-container">
    <div class="alert-box">
      <el-alert
        title="认证信息说明"
        type="warning"
        :closable="false"
        show-icon
      >
        <div class="alert-content">
          一旦认证信息修改，影院状态就会变为待审核状态，将无法使用影院功能。请确保提供真实有效的信息。
        </div>
      </el-alert>
    </div>
    
    <div class="certificate-content">
      <div class="certificate-form">
        <el-form ref="formRef" :rules="data.rules" :model="data.user" label-width="120px">
          <div class="section-title">证件照片上传</div>
          
          <el-form-item prop="front" label="身份证正面">
            <el-upload
                :action="baseUrl + '/files/upload'"
                :on-success="handleFrontUpload"
                :on-error="handleUploadError"
                :before-upload="beforeUpload"
                :show-file-list="false"
                class="avatar-uploader"
            >
              <img v-if="data.user.front" :src="getFullImageUrl(data.user.front)" class="avatar" />
              <div v-else class="upload-placeholder">
                <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
                <span>点击上传</span>
              </div>
            </el-upload>
          </el-form-item>
          
          <el-form-item prop="back" label="身份证反面">
            <el-upload
                :action="baseUrl + '/files/upload'"
                :on-success="handleBackUpload"
                :on-error="handleUploadError"
                :before-upload="beforeUpload"
                :show-file-list="false"
                class="avatar-uploader"
            >
              <img v-if="data.user.back" :src="getFullImageUrl(data.user.back)" class="avatar" />
              <div v-else class="upload-placeholder">
                <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
                <span>点击上传</span>
              </div>
            </el-upload>
          </el-form-item>
          
          <el-form-item prop="certificate" label="营业执照">
            <el-upload
                :action="baseUrl + '/files/upload'"
                :on-success="handleCertificateUpload"
                :on-error="handleUploadError"
                :before-upload="beforeUpload"
                :show-file-list="false"
                class="avatar-uploader"
            >
              <img v-if="data.user.certificate" :src="getFullImageUrl(data.user.certificate)" class="avatar" />
              <div v-else class="upload-placeholder">
                <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
                <span>点击上传</span>
              </div>
            </el-upload>
          </el-form-item>
          
          <div class="section-title">负责人信息</div>
          
          <el-form-item prop="leader" label="负责人姓名">
            <el-input v-model="data.user.leader" placeholder="请输入负责人姓名"></el-input>
          </el-form-item>
          
          <el-form-item prop="code" label="身份证号">
            <el-input v-model="data.user.code" placeholder="请输入身份证号"></el-input>
          </el-form-item>
          
          <div class="section-title">影院地址信息</div>
          
          <el-form-item prop="address" label="影院地址">
            <div class="address-input-group">
              <el-input v-model="data.user.address" placeholder="请选择影院地址" readonly></el-input>
              <el-button type="primary" @click="openMapDialog">地图选择</el-button>
            </div>
          </el-form-item>
          
          <el-form-item prop="status" label="审核状态">
            <el-tag type="warning" v-if="data.user.status === '待审核'">{{ data.user.status }}</el-tag>
            <el-tag type="success" v-if="data.user.status === '审核通过'">{{ data.user.status }}</el-tag>
            <el-tag type="danger" v-if="data.user.status === '审核拒绝'">{{ data.user.status }}</el-tag>
          </el-form-item>
          
          <div class="form-actions">
            <el-button type="primary" size="large" @click="update">提交认证</el-button>
          </div>
        </el-form>
      </div>
    </div>
    
    <!-- 地图选择对话框 -->
    <el-dialog
      v-model="mapDialogVisible"
      title="选择影院位置"
      width="800px"
      destroy-on-close
      class="map-dialog"
    >
      <div class="map-container">
        <BaiduMap
          ref="mapRef"
          :height="500"
          :selectable="true"
          @select-address="handleSelectAddress"
        />
      </div>
      
      <div v-if="selectedLocation" class="selected-location">
        <div class="location-title">已选位置信息</div>
        <div class="location-item">
          <span class="item-label">名称：</span>
          <span class="item-value">{{ selectedLocation.title }}</span>
        </div>
        <div class="location-item">
          <span class="item-label">详细地址：</span>
          <span class="item-value">{{ selectedLocation.address }}</span>
        </div>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="mapDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmLocation">确认选择</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import BaiduMap from '@/components/BaiduMap.vue';
import { useRouter } from 'vue-router';

const baseUrl = import.meta.env.VITE_BASE_URL;
const formRef = ref();
const mapRef = ref(null);
const mapDialogVisible = ref(false);
const selectedLocation = ref(null);
const router = useRouter();

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  rules: {
    front: [
      { required: true, message: '请上传身份证正面', trigger: 'blur' }
    ],
    back: [
      { required: true, message: '请上传身份证反面', trigger: 'blur' }
    ],
    certificate: [
      { required: true, message: '请上传营业执照', trigger: 'blur' }
    ],
    leader: [
      { required: true, message: '请填写负责人姓名', trigger: 'blur' }
    ],
    code: [
      { required: true, message: '请填写负责人身份证号', trigger: 'blur' }
    ],
    address: [
      { required: true, message: '请填写影院地址', trigger: 'blur' }
    ],
  }
});

const getFullImageUrl = (url) => {
  if (!url) return '';
  if (url.startsWith('http')) return url;
  return baseUrl + url;
};

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/');
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error('只能上传图片格式文件!');
    return false;
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!');
    return false;
  }
  return true;
};

const handleFrontUpload = (res) => {
  if (res.code === '200') {
    data.user.front = res.data;
  } else {
    ElMessage.error('上传失败');
  }
};

const handleBackUpload = (res) => {
  if (res.code === '200') {
    data.user.back = res.data;
  } else {
    ElMessage.error('上传失败');
  }
};

const handleCertificateUpload = (res) => {
  if (res.code === '200') {
    data.user.certificate = res.data;
  } else {
    ElMessage.error('上传失败');
  }
};

// 打开地图对话框
const openMapDialog = () => {
  mapDialogVisible.value = true;
  // 如果已有地址，自动搜索
  setTimeout(() => {
    if (data.user.address && mapRef.value) {
      mapRef.value.search(data.user.address);
    }
  }, 1000);
};

// 处理地图位置选择
const handleSelectAddress = (address) => {
  selectedLocation.value = address;
};

// 确认选择地址
const confirmLocation = () => {
  if (selectedLocation.value) {
    data.user.address = selectedLocation.value.address;
    mapDialogVisible.value = false;
    ElMessage.success('地址已更新');
  } else {
    ElMessage.warning('请先在地图上选择位置');
  }
};

const emit = defineEmits(['updateUser']);

const update = () => {
  formRef.value.validate(valid => {
    if (valid) {
      data.user.status = '待审核';
      request.put('/cinema/update', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('认证信息提交成功');
          localStorage.setItem('xm-user', JSON.stringify(data.user));
          emit('updateUser');
          
          // 显示成功提示后延迟跳转
          setTimeout(() => {
            router.push('/manager/home');
          }, 1500);
        } else {
          ElMessage.error(res.msg);
        }
      }).catch(err => {
        ElMessage.error('提交失败，请稍后重试');
        console.error(err);
      });
    }
  });
};

const handleUploadError = () => {
  ElMessage.error('上传失败，请重试');
};
</script>

<style scoped>
.certificate-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.alert-box {
  margin-bottom: 20px;
}

.alert-content {
  font-size: 14px;
  line-height: 1.5;
  margin-top: 8px;
}

.certificate-content {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.certificate-form {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #409EFF;
  margin: 20px 0;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.avatar-uploader {
  height: 150px;
  width: 250px;
  display: block;
}

.avatar-uploader .avatar {
  width: 250px;
  height: 150px;
  display: block;
  object-fit: contain;
  border-radius: 6px;
  border: 1px solid #ebeef5;
}

.upload-placeholder {
  width: 250px;
  height: 150px;
  background-color: #f5f7fa;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.3s;
}

.upload-placeholder:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  margin-bottom: 8px;
}

.avatar-uploader .el-upload {
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.form-actions {
  margin-top: 30px;
  text-align: center;
}

.address-input-group {
  display: flex;
  gap: 10px;
}

.map-container {
  width: 100%;
  border-radius: 6px;
  overflow: hidden;
}

.selected-location {
  margin-top: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 6px;
  border-left: 3px solid #409EFF;
}

.location-title {
  font-weight: 600;
  margin-bottom: 10px;
  color: #303133;
}

.location-item {
  margin: 8px 0;
  color: #606266;
}

.item-label {
  font-weight: 500;
  margin-right: 6px;
}

.item-value {
  color: #333;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}

.map-dialog :deep(.el-dialog__body) {
  padding: 20px;
}
</style>