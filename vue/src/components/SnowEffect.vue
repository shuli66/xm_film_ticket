<template>
  <div class="snow-container">
    <!-- 雪花效果 -->
    <div v-for="(snow, index) in snowflakes" 
         :key="'snow-' + index" 
         class="snowflake"
         :style="{
           left: snow.x + 'px',
           top: snow.y + 'px',
           width: snow.size + 'px',
           height: snow.size + 'px',
           animationDuration: snow.duration + 's',
           animationDelay: snow.delay + 's',
           opacity: snow.opacity,
           transform: `rotate(${snow.rotation}deg)`
         }">
      <div class="snowflake-inner"></div>
    </div>

    <!-- 星星效果 -->
    <div v-for="(star, index) in stars" 
         :key="'star-' + index" 
         class="star"
         :style="{
           left: star.x + 'px',
           top: star.y + 'px',
           width: star.size + 'px',
           height: star.size + 'px',
           animationDuration: star.duration + 's',
           animationDelay: star.delay + 's',
           opacity: star.opacity
         }">
      <div class="star-inner"></div>
    </div>

    <!-- 光点效果 -->
    <div v-for="(light, index) in lights" 
         :key="'light-' + index" 
         class="light"
         :style="{
           left: light.x + 'px',
           top: light.y + 'px',
           width: light.size + 'px',
           height: light.size + 'px',
           animationDuration: light.duration + 's',
           animationDelay: light.delay + 's',
           opacity: light.opacity
         }">
      <div class="light-inner"></div>
    </div>

    <!-- 流星效果 -->
    <div v-for="(meteor, index) in meteors" 
         :key="'meteor-' + index" 
         class="meteor"
         :style="{
           left: meteor.x + 'px',
           top: meteor.y + 'px',
           width: meteor.size + 'px',
           height: meteor.size + 'px',
           animationDuration: meteor.duration + 's',
           animationDelay: meteor.delay + 's',
           transform: `rotate(${meteor.angle}deg)`
         }">
      <div class="meteor-inner"></div>
    </div>

    <!-- 气泡效果 -->
    <div v-for="(bubble, index) in bubbles" 
         :key="'bubble-' + index" 
         class="bubble"
         :style="{
           left: bubble.x + 'px',
           top: bubble.y + 'px',
           width: bubble.size + 'px',
           height: bubble.size + 'px',
           animationDuration: bubble.duration + 's',
           animationDelay: bubble.delay + 's',
           opacity: bubble.opacity
         }">
      <div class="bubble-inner"></div>
    </div>

    <!-- 粒子效果 -->
    <div v-for="(particle, index) in particles" 
         :key="'particle-' + index" 
         class="particle"
         :style="{
           left: particle.x + 'px',
           top: particle.y + 'px',
           width: particle.size + 'px',
           height: particle.size + 'px',
           animationDuration: particle.duration + 's',
           animationDelay: particle.delay + 's',
           opacity: particle.opacity,
           background: particle.color
         }">
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const snowflakes = ref([]);
const stars = ref([]);
const lights = ref([]);
const meteors = ref([]);
const bubbles = ref([]);
const particles = ref([]);

const createSnowflake = () => {
  const x = Math.random() * window.innerWidth;
  const y = -20;
  const size = Math.random() * 6 + 4;
  const duration = Math.random() * 10 + 8;
  const delay = Math.random() * 3;
  const opacity = Math.random() * 0.7 + 0.5;
  const rotation = Math.random() * 360;
  
  return {
    x,
    y,
    size,
    duration,
    delay,
    opacity,
    rotation
  };
};

const createStar = () => {
  const x = Math.random() * window.innerWidth;
  const y = Math.random() * window.innerHeight;
  const size = Math.random() * 3 + 1;
  const duration = Math.random() * 3 + 2;
  const delay = Math.random() * 2;
  const opacity = Math.random() * 0.6 + 0.2;
  
  return {
    x,
    y,
    size,
    duration,
    delay,
    opacity
  };
};

const createLight = () => {
  const x = Math.random() * window.innerWidth;
  const y = Math.random() * window.innerHeight;
  const size = Math.random() * 6 + 2;
  const duration = Math.random() * 4 + 3;
  const delay = Math.random() * 3;
  const opacity = Math.random() * 0.4 + 0.1;
  
  return {
    x,
    y,
    size,
    duration,
    delay,
    opacity
  };
};

const createMeteor = () => {
  const x = Math.random() * window.innerWidth;
  const y = -20;
  const size = Math.random() * 5 + 3;
  const duration = Math.random() * 2 + 1.2;
  const delay = Math.random() * 6;
  const angle = Math.random() * 45 - 22.5;
  
  return {
    x,
    y,
    size,
    duration,
    delay,
    angle
  };
};

const createBubble = () => {
  const x = Math.random() * window.innerWidth;
  const y = window.innerHeight + 20;
  const size = Math.random() * 8 + 4;
  const duration = Math.random() * 10 + 8;
  const delay = Math.random() * 4;
  const opacity = Math.random() * 0.3 + 0.1;
  
  return {
    x,
    y,
    size,
    duration,
    delay,
    opacity
  };
};

const createParticle = () => {
  const x = Math.random() * window.innerWidth;
  const y = Math.random() * window.innerHeight;
  const size = Math.random() * 2 + 1;
  const duration = Math.random() * 2 + 1;
  const delay = Math.random() * 2;
  const opacity = Math.random() * 0.4 + 0.1;
  const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#ffeead'];
  const color = colors[Math.floor(Math.random() * colors.length)];
  
  return {
    x,
    y,
    size,
    duration,
    delay,
    opacity,
    color
  };
};

onMounted(() => {
  // 创建雪花
  for (let i = 0; i < 80; i++) {
    snowflakes.value.push(createSnowflake());
  }
  
  // 创建星星
  for (let i = 0; i < 30; i++) {
    stars.value.push(createStar());
  }
  
  // 创建光点
  for (let i = 0; i < 25; i++) {
    lights.value.push(createLight());
  }

  // 创建流星
  for (let i = 0; i < 6; i++) {
    meteors.value.push(createMeteor());
  }

  // 创建气泡
  for (let i = 0; i < 15; i++) {
    bubbles.value.push(createBubble());
  }

  // 创建粒子
  for (let i = 0; i < 40; i++) {
    particles.value.push(createParticle());
  }
  
  // 定期更新元素位置
  setInterval(() => {
    // 每次只更新一部分元素，避免全部重置造成的闪烁感
    for (let i = 0; i < 20; i++) {
      const index = Math.floor(Math.random() * snowflakes.value.length);
      if (index < snowflakes.value.length) {
        snowflakes.value[index] = createSnowflake();
      }
    }
    
    for (let i = 0; i < 2; i++) {
      meteors.value[i % meteors.value.length] = createMeteor();
    }
    
    // 每隔一段时间完全更新所有元素
    if (Math.random() > 0.8) {
      stars.value = stars.value.map(() => createStar());
      lights.value = lights.value.map(() => createLight());
      bubbles.value = bubbles.value.map(() => createBubble());
      particles.value = particles.value.map(() => createParticle());
    }
  }, 3000);
});
</script>

<style scoped>
.snow-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 2;
  overflow: hidden;
}

/* 雪花样式 */
.snowflake {
  position: absolute;
  animation: snowfall linear infinite;
  will-change: transform, opacity;
}

.snowflake-inner {
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.8), 0 0 5px rgba(173, 216, 230, 0.8);
  filter: blur(1px);
  transform: scale(1);
  transition: transform 0.3s ease;
}

.snowflake:hover .snowflake-inner {
  transform: scale(1.2);
}

/* 星星样式 */
.star {
  position: absolute;
  animation: twinkle ease-in-out infinite;
  will-change: transform, opacity;
}

.star-inner {
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.8);
  clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
  transform: scale(1);
  transition: transform 0.3s ease;
}

.star:hover .star-inner {
  transform: scale(1.2) rotate(180deg);
}

/* 光点样式 */
.light {
  position: absolute;
  animation: pulse ease-in-out infinite;
  will-change: transform, opacity;
}

.light-inner {
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(255,255,255,0.8) 0%, rgba(255,255,255,0) 70%);
  border-radius: 50%;
  transform: scale(1);
  transition: transform 0.3s ease;
}

.light:hover .light-inner {
  transform: scale(1.5);
}

/* 流星样式 */
.meteor {
  position: absolute;
  animation: meteor linear infinite;
  will-change: transform, opacity;
}

.meteor-inner {
  width: 100%;
  height: 100%;
  background: linear-gradient(to right, rgba(255,255,255,0.9), rgba(255,255,255,0));
  box-shadow: 0 0 15px 5px rgba(255, 255, 255, 0.7), 0 0 30px 15px rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  transform: scale(1);
  transition: transform 0.3s ease;
}

.meteor:hover .meteor-inner {
  transform: scale(1.5);
}

/* 气泡样式 */
.bubble {
  position: absolute;
  animation: bubble linear infinite;
  will-change: transform, opacity;
}

.bubble-inner {
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  border: 1px solid rgba(255, 255, 255, 0.3);
  transform: scale(1);
  transition: transform 0.3s ease;
}

.bubble:hover .bubble-inner {
  transform: scale(1.3);
}

/* 粒子样式 */
.particle {
  position: absolute;
  border-radius: 50%;
  animation: particle ease-in-out infinite;
  box-shadow: 0 0 8px currentColor;
  will-change: transform, opacity;
}

/* 动画定义 */
@keyframes snowfall {
  0% {
    transform: translateY(-20px) translateX(0) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(100vh) translateX(var(--drift, 50px)) rotate(360deg);
    opacity: 0;
  }
}

@keyframes twinkle {
  0%, 100% {
    opacity: 0.2;
    transform: scale(1);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.2);
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.1;
    transform: scale(1);
  }
  50% {
    opacity: 0.4;
    transform: scale(1.3);
  }
}

@keyframes meteor {
  0% {
    transform: translateY(-20px) translateX(0) rotate(var(--angle, 0deg));
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(100vh) translateX(200px) rotate(var(--angle, 0deg));
    opacity: 0;
  }
}

@keyframes bubble {
  0% {
    transform: translateY(100vh) translateX(0);
    opacity: 0;
  }
  10% {
    opacity: 0.3;
  }
  90% {
    opacity: 0.3;
  }
  100% {
    transform: translateY(-20px) translateX(20px);
    opacity: 0;
  }
}

@keyframes particle {
  0%, 100% {
    opacity: 0.1;
    transform: scale(1) translateY(0);
  }
  50% {
    opacity: 0.4;
    transform: scale(1.2) translateY(-5px);
  }
}
</style> 