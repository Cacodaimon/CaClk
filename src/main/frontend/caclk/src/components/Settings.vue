<template>
  <div class="card">
    <div class="card-body">
      <h4 class="card-title">Settings</h4>
      <form @submit.prevent="saveSettings">

        <div class="form-group">
          <label for="settings.maxThreads">Max HTTP Threads</label>

          <input v-model="settings.maxThreads"
                 id="settings.maxThreads"
                 class="form-control"
                 type="number"
                 min="4"
                 max="8"
                 required="required"/>
        </div>

        <div class="form-group">
          <label for="settings.port">HTTP Port</label>
          <input v-model="settings.port"
                 id="settings.port"
                 class="form-control"
                 type="number"
                 min="0"
                 max="65535"
                 required="required"/>
        </div>

        <div class="form-group">
          <label for="settings.softBrightness">Soft Brightness</label>
          <input v-model="settings.softBrightness"
                 id="settings.softBrightness"
                 class="form-control"
                 type="number"
                 min="0.01"
                 max="1.00"
                 step="0.01"
                 required="required"/>
        </div>

        <div class="form-group">
          <label for="settings.gpioPin">GPIO Pin</label>
          <input v-model="settings.gpioPin"
                 id="settings.gpioPin"
                 class="form-control"
                 type="number"
                 min="1"
                 max="27"
                 required="required"/>
        </div>

        <div class="form-group">
          <label for="settings.ledCount">Number of LEDs</label>
          <input v-model="settings.ledCount"
                 id="settings.ledCount"
                 class="form-control"
                 type="number"
                 min="0"
                 max="65535"
                 required="required"/>
        </div>

        <div class="form-group">
          <label for="settings.brightness">Brightness</label>
          <input v-model="settings.brightness"
                 id="settings.brightness"
                 class="form-control"
                 type="number"
                 min="0"
                 max="255"
                 required="required"/>
        </div>

        <div class="form-group">
          <label for="settings.stripType">Type of strip</label>
          <select v-model="settings.stripType"
                  id="settings.stripType"
                  class="form-control">
            <option>WS2811_STRIP_RGB</option>
            <option>WS2811_STRIP_RBG</option>
            <option>WS2811_STRIP_GRB</option>
            <option>WS2811_STRIP_GBR</option>
            <option>WS2811_STRIP_BRG</option>
            <option>WS2811_STRIP_BGR</option>
          </select>
        </div>

        <div class="form-check">
          <label class="form-check-label">
            <input v-model="settings.invert"
                   type="checkbox"
                   class="form-check-input">
            Invert
          </label>
        </div>

        <hr/>

        <button type="submit" class="btn btn-primary">Save</button>
      </form>
    </div>
  </div>
</template>

<script>
  import Axios from 'axios'
  import Notyf from 'notyf-js'
  import '../../node_modules/notyf-js/dist/notyf.min.css'

  export default {
    name: 'Settings',
    data: function () {
      return {
        settings: {
          gpioPin: 0,
          maxThreads: 0,
          port: 0,
          ledCount: 0,
          stripType: null,
          invert: false,
          brightness: 255
        }
      }
    },
    mounted: function () {
      let that = this;
      Axios.get('/settings')
        .then(function (response) {
          that.settings = response.data
        })
        .catch(function () {
          // noinspection JSUnresolvedFunction
          new Notyf().alert('Could not load your settings.')
        })
    },
    methods: {
      saveSettings: function () {
        Axios.put('/settings', this.$data.settings)
          .then(function () {
            // noinspection JSUnresolvedFunction
            new Notyf().confirm('Saved your settings successfully.')
          })
          .catch(function () {
            // noinspection JSUnresolvedFunction
            new Notyf().alert('Could not save your settings.')
          })
      }
    }
  }
</script>

<style scoped></style>
