<template>
  <div class="todo-dropdown">
    <a-dropdown placement="bottomRight" @visibleChange="handleVisibleChange">
      <a-button 
        type="default" 
        :class="['todo-btn', { 'has-todo': totalCount > 0 }]"
      >
        <a-icon type="bell" />
        待办
        <a-badge v-if="totalCount > 0" :count="totalCount" :overflow-count="99" class="todo-badge" />
      </a-button>
      <template slot="overlay">
        <a-menu @click="handleMenuClick">
          <a-menu-item v-for="item in todoList" :key="item.type">
            {{ item.title }}
            <a-badge :count="item.count" :overflow-count="99" class="item-badge" />
          </a-menu-item>
          <a-menu-divider v-if="todoList.length === 0" />
          <a-menu-item v-if="todoList.length === 0" disabled>
            暂无待办事项
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
  </div>
</template>

<script>
import { getTodoCount } from '@/api/api'

export default {
  name: 'TodoDropdown',
  data() {
    return {
      todoList: [],
      totalCount: 0,
      refreshInterval: null
    }
  },
  mounted() {
    this.fetchTodoCount()
    // 每1秒刷新一次待办数量
    this.refreshInterval = setInterval(() => {
      this.fetchTodoCount()
    }, 1000)
  },
  beforeDestroy() {
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval)
    }
  },
  methods: {
    async fetchTodoCount() {
      try {
        const res = await getTodoCount()
        if (res.code === 200) {
          this.todoList = res.data.todoList || []
          this.totalCount = res.data.totalCount || 0
        }
      } catch (error) {
        console.error('获取待办数量失败:', error)
      }
    },
    handleMenuClick(e) {
      const item = this.todoList.find(todo => todo.type === e.key)
      if (item && item.path) {
        this.$emit('todoClick', item.path, item.title)
      }
    },
    handleVisibleChange(visible) {
      if (visible) {
        this.fetchTodoCount()
      }
    }
  }
}
</script>

<style lang="less" scoped>
.todo-dropdown {
  .todo-btn {
    position: relative;
    display: flex;
    align-items: center;
    gap: 4px;
    
    &.has-todo {
      color: #1890ff;
      border-color: #1890ff;
    }
  }
  
  .todo-badge {
    position: absolute;
    top: -5px;
    right: -10px;
  }
  
  .item-badge {
    margin-left: 8px;
  }
}
</style>