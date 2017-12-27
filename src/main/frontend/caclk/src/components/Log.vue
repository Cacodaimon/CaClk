<template>
  <div class="card">
    <div class="card-body">
      <h4 class="card-title">
        Logging
      </h4>
  <div v-for="logMessage in logMessagesReversed"
       :class="{'alert-warning': logMessage.level === 'WARN', 'alert-info': logMessage.level === 'INFO', 'alert-secondary': logMessage.level === 'DEBUG'}"
       class="alert"
       role="alert">
    <p>
      <span>{{ new Date(logMessage.timeStamp) | moment("dddd, MMMM Do YYYY, h:mm:ss a") }}</span>
      <b>{{ logMessage.level }}</b>
      {{ logMessage.renderedMessage }}
    </p>
    <hr>
    <p>
      {{ logMessage.loggerName }}@{{ logMessage.threadName }}
    </p>
  </div>
    </div>
  </div>
</template>

<script>
  import moment from 'vue-moment'

  export default {
    name: 'Log',
    data: function () {
      return {
        logMessages: []
      }
    },
    mounted: function () {
      let that = this;
      let webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/log");
      webSocket.onmessage = function (msg) {
        that.logMessages.push(JSON.parse(msg.data))
      }
    },
    computed: {
      logMessagesReversed() {
        return this.logMessages.slice().reverse();
      }
    }
  }
</script>

<style scoped></style>
