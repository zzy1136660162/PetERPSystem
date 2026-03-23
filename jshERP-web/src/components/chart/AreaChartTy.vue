<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h3 :style="{ marginBottom: '20px' }">{{ title }}</h3>

    <v-chart ref="chart" :forceFit="true" :height="height" :data="dataSource" :scale="scale" :padding="padding">
      <v-tooltip :shared="false"/>
      <v-axis/>
      <v-line position="x*y" :size="lineSize" :color="lineColor"/>
      <v-area position="x*y" :color="color"/>
      <v-point position="x*y" :size="4" color="#1890ff" :v-style="{stroke: '#fff', lineWidth: 1}">
        <v-label content="y" :offset="-25" :textStyle="{fill: '#1890ff', fontSize: 13, fontWeight: 'bold'}"/>
      </v-point>
    </v-chart>

  </div>
</template>

<script>
  import { triggerWindowResizeEvent } from '@/utils/util'

  export default {
    name: 'AreaChartTy',
    props: {
      // 图表数据
      dataSource: {
        type: Array,
        required: true
      },
      // 图表标题
      title: {
        type: String,
        default: ''
      },
      // x 轴别名
      x: {
        type: String,
        default: 'x'
      },
      // y 轴别名
      y: {
        type: String,
        default: 'y'
      },
      // Y轴最小值
      min: {
        type: Number,
        default: 0
      },
      // Y轴最大值
      max: {
        type: Number,
        default: null
      },
      // 图表高度
      height: {
        type: Number,
        default: 254
      },
      // 线的粗细
      lineSize: {
        type: Number,
        default: 2
      },
      // 面积的颜色
      color: {
        type: String,
        default: ''
      },
      // 线的颜色
      lineColor: {
        type: String,
        default: ''
      },
      // 图表padding
      padding: {
        type: Array,
        default: () => ['auto', 'auto', '40', '50']
      }
    },
    computed: {
      scale() {
        // 计算Y轴最大值：数据最大值 * 1.2（确保总是大于数据最大值）
        let dataMax = 0
        if (this.dataSource && this.dataSource.length > 0) {
          const values = this.dataSource.map(item => item.y || 0)
          dataMax = Math.max(...values)
        }
        const yMax = dataMax > 0 ? Math.ceil(dataMax * 1.2) : 100

        return [
          { dataKey: 'x', title: this.x, alias: this.x },
          { dataKey: 'y', title: this.y, alias: this.y, min: this.min, max: yMax }
        ]
      }
    },
    mounted() {
      triggerWindowResizeEvent()
    }
  }
</script>

<style lang="less" scoped>
  @import "chart";
</style>