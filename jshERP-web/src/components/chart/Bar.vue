<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h3 :style="{ marginBottom: '20px' }">{{ title }}</h3>
    <v-chart :forceFit="true" :height="height" :data="dataSource" :scale="scale" :padding="padding">
      <v-tooltip/>
      <v-axis/>
      <v-bar position="x*y" :color="color">
        <v-label content="y" :offset="10" :textStyle="{fill: '#ff4d4f', fontSize: 13, fontWeight: 'bold'}"/>
      </v-bar>
    </v-chart>
  </div>
</template>

<script>
  import { triggerWindowResizeEvent } from '@/utils/util'
  import { DEFAULT_COLOR } from "@/store/mutation-types"
  import Vue from 'vue'

  export default {
    name: 'Bar',
    props: {
      dataSource: {
        type: Array,
        required: true
      },
      yaxisText: {
        type: String,
        default: 'y'
      },
      title: {
        type: String,
        default: ''
      },
      height: {
        type: Number,
        default: 254
      }
    },
    data() {
      return {
        padding: ['auto', 'auto', '40', '50'],
        color: Vue.ls.get(DEFAULT_COLOR)
      }
    },
    computed: {
      scale() {
        // 计算数据最大值，并设置Y轴最大值为数据最大值的1.2倍（确保永远比数据最大值大）
        let yMax = null
        if (this.dataSource && this.dataSource.length > 0) {
          const dataMax = Math.max(...this.dataSource.map(item => item.y || 0))
          if (dataMax > 0) {
            yMax = Math.ceil(dataMax * 1.2) // 向上取整，确保比数据最大值大
          }
        }
        
        return [{
          dataKey: 'y',
          alias: this.yaxisText,
          max: yMax
        }]
      }
    },
    mounted() {
      triggerWindowResizeEvent()
    }
  }
</script>