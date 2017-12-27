<template>
  <div class="card">
    <div class="card-body">
      <h4 class="card-title">
        <span class="text-muted">Edit</span> {{ app.name }}
      </h4>

      <ul class="nav nav-tabs nav-justified">
        <li class="nav-item">
          <a @click.prevent="activeTab = 0"
             :class="{active: activeTab === 0}"
             class="nav-link"
             href="#">Meta</a>
        </li>
        <li class="nav-item">
          <a @click.prevent="activeTab = 1"
             :class="{active: activeTab === 1}"
             class="nav-link"
             href="#">Settings</a>
        </li>
        <li class="nav-item">
          <a @click.prevent="activeTab = 2"
             :class="{active: activeTab === 2}"
             class="nav-link"
             href="#">Code</a>
        </li>
      </ul>

      <form @submit.prevent="saveApp(app)">
        <section v-show="activeTab == 0">
          <div class="form-row">
            <div class="form-group col">
              <label for="app.name">Name</label>

              <input v-model="app.name"
                     id="app.name"
                     class="form-control"
                     type="text"
                     required="required"/>
            </div>

            <div class="form-group col">
              <label for="app.version">Version</label>

              <input v-model="app.version"
                     id="app.version"
                     class="form-control"
                     type="text"
                     required="required"/>
            </div>
          </div>

          <div class="form-group">
            <label for="app.description">Description</label>

            <input v-model="app.description"
                   id="app.description"
                   class="form-control"
                   type="text"/>
          </div>

          <div v-if="app.author"
               class="form-row">
            <div class="form-group col">
              <label for="app.author.name">Author Name</label>

              <input v-model="app.author.name"
                     id="app.author.name"
                     class="form-control"
                     type="text"
                     required="required"/>
            </div>

            <div class="form-group col">
              <label for="app.author.eMail">Author E-mail</label>

              <input v-model="app.author.eMail"
                     id="app.author.eMail"
                     class="form-control"
                     type="email"/>
            </div>
          </div>
        </section>

        <section v-show="activeTab == 1">
          <div class="form-group">
            <label for="app.permissions">Permissions</label>
            <select v-model="app.permissions"
                    id="app.permissions"
                    class="form-control"
                    multiple>
              <option>LED</option>
              <option>HTTP_CLIENT</option>
              <option>HTTP_SERVER</option>
              <option>INTERVAL</option>
              <option>DATE_TIME</option>
              <option>SETTINGS</option>
              <option>PROCESS</option>
            </select>
          </div>

          <div v-if="app.permissions && app.permissions.includes('INTERVAL')"
               class="form-group">
            <label for="app.interval">Interval</label>
            <input v-model="app.interval"
                   id="app.interval"
                   class="form-control"
                   type="number"
                   min="10"
                   max="9223372036854775807"/>
          </div>

          <div v-if="app.permissions && app.permissions.includes('SETTINGS')"
               class="form-group">
            <label for="app.settings">
              Settings
            </label>
            <textarea v-model="app.settings"
                      id="app.settings"
                      class="form-control"
                      rows="10"></textarea>
          </div>

          <div class="form-check">
            <label class="form-check-label">
              <input v-model="app.autoStart"
                     type="checkbox"
                     class="form-check-input">
              Autostart
            </label>
          </div>
        </section>

        <section v-show="activeTab == 2">
          <div class="form-group">
            <label for="app.code">
              Code
            </label>
            <div class="editor-space">
              <codemirror v-model="app.code"
                          :options="codeMirrorOptions"></codemirror>
            </div>
          </div>

        </section>

        <hr/>

        <button class="btn btn-secondary">Save</button>
        <button @click.prevent="saveAndRunApp(app)"
                class="btn btn-primary">Save & Run
        </button>
      </form>
    </div>
  </div>
</template>

<script>
  import Axios from 'axios'
  import Notyf from 'notyf-js'
  import '../../node_modules/notyf-js/dist/notyf.min.css'
  import {codemirror} from 'vue-codemirror-lite'
  import 'codemirror/mode/javascript/javascript'
  import 'codemirror/addon/scroll/simplescrollbars'
  import 'codemirror/addon/scroll/simplescrollbars.css'
  import 'codemirror/addon/display/fullscreen'
  import 'codemirror/addon/display/fullscreen.css'
  import 'codemirror/theme/monokai.css'

  export default {
    name: 'AppEdit',
    data: function () {
      return {
        app: [],
        activeTab: 2,
        codeMirrorOptions: {
          theme: 'monokai',
          mode: 'javascript',
          lineNumbers: true,
          scrollbarStyle: 'overlay',
          extraKeys: {
            'F11': function(cm) {
              cm.setOption('fullScreen', !cm.getOption('fullScreen'));
            },
            'Esc': function(cm) {
              if (cm.getOption('fullScreen')) cm.setOption('fullScreen', false);
            }
          }
        }
      }
    },
    components: {
      codemirror
    },
    created: function () {
      let that = this;
      // noinspection JSUnresolvedVariable
      Axios.get(`/app/${that.$route.params.id}`)
        .then(function (response) {
          that.app = response.data
        })
        .catch(function () {
          // noinspection JSUnresolvedFunction, JSUnresolvedVariable
          new Notyf().alert(`Error loading app with id ${that.$route.params.id}.`)
        })
    },
    methods: {
      saveAndRunApp: function (app) {
        let that = this;
        Axios.put(`/app/${app.id}`, app)
          .then(function () {
            Axios.get(`/app/${app.id}/run`)
              .then(function () {
                // noinspection JSUnresolvedFunction
                new Notyf().confirm(`Started "${app.name}".`)
              })
              .catch(function () {
                // noinspection JSUnresolvedFunction
                new Notyf().alert(`Could not start "${app.name}".`)
              })
            // noinspection JSUnresolvedFunction
            new Notyf().confirm(`Saved "${that.$data.app.name}".`)
          })
          .catch(function () {
            // noinspection JSUnresolvedFunction
            new Notyf().alert(`Could not save "${that.$data.app.name}".`)
          })
      },
      saveApp: function (app) {
        let that = this;
        Axios.put(`/app/${app.id}`, app)
          .then(function () {
            // noinspection JSUnresolvedFunction
            new Notyf().confirm(`Saved "${that.$data.app.name}".`)
          })
          .catch(function () {
            // noinspection JSUnresolvedFunction
            new Notyf().alert(`Could not save "${that.$data.app.name}".`)
          })
      }
    }
  }
</script>

<style scoped>
  .editor-space {
    min-height: 400px;
  }
</style>
