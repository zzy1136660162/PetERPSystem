<template>
  <a-config-provider :locale="locale">
    <div id="app">
      <router-view/>
    </div>
  </a-config-provider>
</template>
<script>
  import zhCN from 'ant-design-vue/lib/locale-provider/zh_CN'
  import enquireScreen from '@/utils/device'

  export default {
    data () {
      return {
        locale: zhCN,
      }
    },
    created () {
      let that = this
      enquireScreen(deviceType => {
        // tablet
        if (deviceType === 0) {
          that.$store.commit('TOGGLE_DEVICE', 'mobile')
          that.$store.dispatch('setSidebar', false)
        }
        // mobile
        else if (deviceType === 1) {
          that.$store.commit('TOGGLE_DEVICE', 'mobile')
          that.$store.dispatch('setSidebar', false)
        }
        else {
          that.$store.commit('TOGGLE_DEVICE', 'desktop')
          that.$store.dispatch('setSidebar', true)
        }
      })
    }
  }
</script>
<style>
  #app {
    height: 100%;
  }

  /* 隐藏全局右侧垂直滚动条，保留底部横向滚动条 */
  html, body {
    /* 隐藏垂直滚动条 */
    overflow-y: hidden;
    /* 保留水平滚动 */
    overflow-x: auto;
  }

  /* 针对Webkit浏览器的滚动条样式 */
  ::-webkit-scrollbar {
    width: 0;           /* 隐藏垂直滚动条宽度 */
    height: 8px;        /* 保留水平滚动条高度 */
  }

  /* 水平滚动条轨道 */
  ::-webkit-scrollbar-track:horizontal {
    background: #f1f1f1;
    border-radius: 4px;
  }

  /* 水平滚动条滑块 */
  ::-webkit-scrollbar-thumb:horizontal {
    background: #c1c1c1;
    border-radius: 4px;
  }

  /* 水平滚动条滑块悬停效果 */
  ::-webkit-scrollbar-thumb:horizontal:hover {
    background: #a8a8a8;
  }

  /* Firefox浏览器支持 */
  * {
    scrollbar-width: thin;           /* 细滚动条 */
    scrollbar-color: transparent transparent;  /* 默认透明 */
  }

  /* Firefox水平滚动条 */
  *:hover {
    scrollbar-color: #c1c1c1 #f1f1f1;  /* 悬停时显示水平滚动条 */
  }
</style>