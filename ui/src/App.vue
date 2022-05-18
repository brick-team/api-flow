<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider v-model:collapsed="collapsed" collapsible>
      <a-menu v-model:selectedKeys="current" mode="inline">


        <a-menu-item key="5">
          <a href="/#/swagger-list">swagger</a>
        </a-menu-item>
        <!--        <a-menu-item key="2">-->
        <!--          <a href="/#/swagger">swagger=ui</a>-->
        <!--        </a-menu-item>-->
        <a-menu-item key="3">
          <a href="/#/flow-list">流程列表</a>
        </a-menu-item>
        <!--        <a-menu-item key="6">-->
        <!--          <a href="/#/flow-ui">流程视图</a>-->
        <!--        </a-menu-item>-->
        <a-menu-item key="7">
          <a href="/#/result-ui">响应体</a>
        </a-menu-item>
        <!--        <a-menu-item key="8">-->
        <!--          <a href="/#/watcher-ui">观察</a>-->
        <!--        </a-menu-item>-->
      </a-menu>
    </a-layout-sider>
    <a-layout-content style="margin: 0 16px">
      <router-view/>
    </a-layout-content>
  </a-layout>

</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
