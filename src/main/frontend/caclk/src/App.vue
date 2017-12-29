<template>

  <div id="app">
    <nav class="navbar navbar-expand-md navbar-dark bg-primary fixed-top">
      <div class="container">
        <router-link to="/" class="navbar-brand">CaClk</router-link>
        <button @click="navToggled = !navToggled"
                class="navbar-toggler"
                type="button"
                data-toggle="collapse"
                data-target="#navbar"
                aria-controls="navbar" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div :class="{show: navToggled}"
             class="collapse navbar-collapse"
             id="navbar">
          <ul class="navbar-nav mr-auto">
            <router-link to="/app"
                         tag="li"
                         exact="true"
                         class="nav-item"
                         active-class="active">
              <a class="nav-link">Apps</a>
            </router-link>

            <router-link to="/app/add"
                         tag="li"
                         exact="true"
                         class="nav-item"
                         active-class="active">
              <a class="nav-link">Add App</a>
            </router-link>

            <router-link to="/log"
                         tag="li"
                         exact="true"
                         class="nav-item"
                         active-class="active">
              <a class="nav-link">Logging</a>
            </router-link>

            <router-link to="/settings"
                         tag="li"
                         exact="true"
                         class="nav-item"
                         active-class="active">
              <a class="nav-link">Settings</a>
            </router-link>
          </ul>
        </div>
      </div>
    </nav>

    <main role="main" class="container">
      <transition
        name="custom-classes-transition"
        enter-active-class="animated fadeInDown"
        leave-active-class="animated fadeOutUp"
        mode="out-in">
        <router-view></router-view>
      </transition>
    </main>
  </div>
</template>

<script>
  import '../node_modules/animate.css/animate.css'

  export default {
    name: 'CaClk',
    data: function () {
      return {
        navToggled: false
      }
    }
  }
</script>

<style lang="scss">
  @import '../static/boostrap.min.css';

  body {
    padding-top: 5rem;
  }

  .btn {
    cursor: pointer;
  }
</style>
