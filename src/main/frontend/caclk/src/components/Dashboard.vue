<template>
  <div v-if="runningApp"
       class="card">
    <div class="card-body">
      <h4 v-if="runningApp.name"
          class="card-title">
        <span class="text-muted">Running App</span> {{ runningApp.name }}
      </h4>
      <h6 class="card-subtitle mb-2 text-muted">
        <template v-if="runningApp.version">
        V{{ runningApp.version }}
        </template>
        <template v-if="runningApp.author && runningApp.author.name">
          by {{ runningApp.author.name }}
        </template>
      </h6>
      <p v-if="runningApp.description"
         class="card-text">
        {{ runningApp.description }}
      </p>
      <router-link :to="'/app/' + runningApp.id" class="card-link">Edit</router-link>
    </div>
  </div>
</template>

<script>
  import Axios from 'axios'
  import Notyf from 'notyf-js'
  import '../../node_modules/notyf-js/dist/notyf.min.css'

export default {
  name: 'Dashboard',
  data: function () {
    return {
      runningApp: null
    }
  },
  mounted: function () {
    let that = this;

    Axios.get('/running-app')
      .then(function (response) {
        that.runningApp = response.data
      })
      .catch(function () {
        // noinspection JSUnresolvedFunction
        new Notyf().alert(`Error loading running app "${runningApp.name}".`)
      })
  }
}
</script>

<style scoped>
</style>
