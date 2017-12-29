<template>
  <div class="card">
    <div class="card-body">
      <h4 class="card-title">
        Logging
      </h4>
      <div v-for="logMessage in logMessages"
           :class="{'alert-warning': logMessage.level === 'WARN', 'alert-info': logMessage.level === 'INFO', 'alert-secondary': logMessage.level === 'DEBUG'}"
           class="alert"
           role="alert">
        <p>
          <span>{{ new Date(logMessage.timeStamp) | moment("from") }}</span>
          <b>{{ logMessage.level }}</b>
          {{ logMessage.renderedMessage }}
        </p>
        <p class="font-small">
          {{ logMessage.loggerName }}@{{ logMessage.threadName }}
        </p>
      </div>
      <div v-if="logMessages.length === 0"
           class="alert alert-info"
           role="alert">
        <p>New log messages will appear here soon.</p>
      </div>
    </div>
  </div>
</template>

<script>
  import moment from 'vue-moment'
  import Vue from 'vue'
  import ReconnectingWebSocket from 'reconnectingwebsocket'
  import Notyf from 'notyf-js'
  import '../../node_modules/notyf-js/dist/notyf.min.css'

  Vue.use(moment);

  export default {
    name: 'Log',
    data: function () {
      return {
        logMessages: []
      }
    },
    mounted: function () {
      let that = this;
      let webSocket = new ReconnectingWebSocket(`ws://${location.hostname}:${location.port}/log`);
      webSocket.onmessage = function (msg) {
        that.logMessages.unshift(JSON.parse(msg.data))
      };

      webSocket.onclose = function () {
        new Notyf().alert(`Connection to server "${location.hostname}:${location.port}" closed, reconnecting.`)
      }
    }
  }
</script>

<style scoped>
  .font-small {
    font-size: smaller;
  }
</style>
