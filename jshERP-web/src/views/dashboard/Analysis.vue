<template>
  <div style="margin: 12px 12px 0px;">
    <!-- 次级导航栏 - 快速入口 -->
    <quick-nav-bar></quick-nav-bar>
    <index-chart></index-chart>
  </div>
</template>

<script>
  import IndexChart from './IndexChart'
  import QuickNavBar from '@/components/tools/QuickNavBar'

  export default {
    name: "Analysis",
    components: {
      IndexChart,
      QuickNavBar
    },
    data() {
      return {

      }
    },
    created() {

    }
  }
</script>