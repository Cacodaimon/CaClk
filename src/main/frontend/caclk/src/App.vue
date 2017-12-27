<template>

  <div id="app">
    <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
      <router-link to="/" class="navbar-brand">CaClk</router-link>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbar"
              aria-controls="navbar" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbar">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item">
            <router-link to="/app" class="nav-link">Apps</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/app/add" class="nav-link">Add App</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/log" class="nav-link">Logging</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/settings" class="nav-link">Settings</router-link>
          </li>
        </ul>
      </div>
    </nav>

    <main role="main" class="container">
      <router-view/>
    </main>
  </div>
</template>

<script>
  export default {
    name: 'CaClk'
  }
</script>

<style lang="scss">
  @import '../static/boostrap.min.css';

  body {
    padding-top: 5rem;
    font-size: 1.4rem;
  }

  .btn {
    cursor: pointer;
  }

  .CodeMirror {
    border: 1px solid #eee;
    height: auto;
  }
</style>
