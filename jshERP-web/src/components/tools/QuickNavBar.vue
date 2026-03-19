<template>
  <div class="quick-nav-bar">
    <div class="nav-buttons">
      <a-button
        :type="isActive('/bill/sale_order') ? 'primary' : 'default'"
        icon="file-add"
        @click="goToPage('/bill/sale_order', '销售订单')"
        class="nav-btn"
        :class="{ 'active-btn': isActive('/bill/sale_order') }">
        销售订单
      </a-button>
      <a-button
        :type="isActive('/bill/purchase_order') ? 'primary' : 'default'"
        icon="shopping"
        @click="goToPage('/bill/purchase_order', '采购订单')"
        class="nav-btn"
        :class="{ 'active-btn': isActive('/bill/purchase_order') }">
        采购订单
      </a-button>
      <a-button
        :type="isActive('/bill/sale_out') ? 'primary' : 'default'"
        icon="export"
        @click="goToPage('/bill/sale_out', '销售出库')"
        class="nav-btn"
        :class="{ 'active-btn': isActive('/bill/sale_out') }">
        销售出库
      </a-button>
      <a-button
        :type="isActive('/bill/sale_back') ? 'primary' : 'default'"
        icon="rollback"
        @click="goToPage('/bill/sale_back', '销售退货')"
        class="nav-btn"
        :class="{ 'active-btn': isActive('/bill/sale_back') }">
        销售退货
      </a-button>
    </div>
    <div class="todo-section">
      <todo-dropdown @todoClick="goToPage" />
    </div>
  </div>
</template>

<script>
import TodoDropdown from './TodoDropdown.vue'

export default {
  name: 'QuickNavBar',
  components: {
    TodoDropdown
  },
  data() {
    return {
      currentPath: ''
    }
  },
  watch: {
    '$route': {
      immediate: true,
      handler(route) {
        this.currentPath = route.path
      }
    }
  },
  methods: {
    // 判断当前路径是否激活
    isActive(path) {
      return this.currentPath === path
    },

    // 跳转到指定页面
    goToPage(path, title) {
      // 如果已经在当前页面，不再跳转
      if (this.currentPath === path) {
        return
      }

      // 存储路由标题到localStorage，与左侧菜单点击保持一致
      let storeKey = 'route:title:' + path
      this.$ls.set(storeKey, title)

      // 获取当前路由的菜单信息（从permissionList中获取菜单数据）
      let menuItem = this.findMenuByPath(this.$store.state.user.permissionList, path)
      if (menuItem) {
        // 使用全局事件总线触发dynamicRouterShow，确保TabLayout能正确接收
        this.$root.$emit('dynamicRouterShow', path, menuItem.id, title, menuItem.component)
        
        // 延迟执行路由跳转，确保TabLayout已经处理完标签页添加
        this.$nextTick(() => {
          // 使用router.push进行路由跳转
          this.$router.push({ path: path })
        })
      } else {
        // 如果没有找到菜单项，直接路由跳转
        this.$router.push(path)
      }
    },
    
    // 根据路径查找菜单项（支持通过component路径匹配）
    findMenuByPath(menus, path) {
      for (let menu of menus) {
        // 同时检查url和component是否匹配
        if (menu.url === path || menu.component === path) {
          return menu
        }
        if (menu.children && menu.children.length > 0) {
          let result = this.findMenuByPath(menu.children, path)
          if (result) {
            return result
          }
        }
      }
      return null
    }
  }
}
</script>

<style lang="less" scoped>
.quick-nav-bar {
  background: #fff;
  padding: 12px 24px;
  margin-bottom: 12px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;

  .nav-buttons {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .todo-section {
    margin-left: auto;
  }

  .nav-btn {
    border-radius: 4px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    }

    // 激活状态样式
    &.active-btn {
      background: #1890ff;
      border-color: #1890ff;
      color: #fff;
      box-shadow: 0 2px 8px rgba(24, 144, 255, 0.4);
    }
  }
}
</style>
