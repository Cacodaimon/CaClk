<template>
  <div class="card">
    <div class="card-body">
      <h4 class="card-title">
        Apps
      </h4>
      <table class="table table-responsive">
        <thead>
        <tr>
          <th>
            #
          </th>
          <th>
            Name
          </th>
          <th>
            Version
          </th>
          <th>
            Author
          </th>
          <th>
            Permissions
          </th>
          <th>
            Interval
          </th>
          <th>
            API
          </th>
          <th>
            Autostart
          </th>
          <th>
            Description
          </th>
          <th>
            Action
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="app in apps">
          <td>
            {{ app.id }}
          </td>
          <td>
            {{ app.name }}
          </td>
          <td>
            {{ app.version }}
          </td>
          <td>
            {{ app.author.name }}
          </td>
          <td>
        <span v-for="permission in app.permissions"
              class="badge badge-pill badge-secondary">
          {{ permission }}
        </span>
          </td>
          <td>
            {{ app.interval ? app.interval : '-' }}
          </td>
          <td>
            <a v-if="app.permissions.includes('HTTP_SERVER')"
               :href="'/app/' + app.id + '/api'"
               class="btn btn-info"
               target="_blank"
               rel="noreferrer">
              Show
            </a>
            <span v-else>-</span>
          </td>
          <td>
            {{ app.autoStart ? 'Yes' : 'No' }}
          </td>
          <td>
            {{ app.description ? app.description : '-' }}
          </td>
          <td>
            <div class="btn-group">
              <button @click="runApp(app)"
                      class="btn btn-success">
                &#9658;
              </button>
              <router-link :to="'/app/' + app.id"
                           tag="button"
                           class="btn btn-warning">
                Edit
              </router-link>
              <button @click="deleteApp(app)"
                      class="btn btn-danger">
                Delete
              </button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
  import Axios from 'axios'
  import Notyf from 'notyf-js'
  import '../../node_modules/notyf-js/dist/notyf.min.css'

  export default {
    name: 'Apps',
    data: function () {
      return {
        apps: []
      }
    },
    mounted: function () {
      let that = this;
      Axios.get('/app')
        .then(function (response) {
          that.apps = response.data
        })
        .catch(function () {
          // noinspection JSUnresolvedFunction
          new Notyf().alert(`Error loading installed apps.`)
        })
    },
    methods: {
      runApp: function (app) {
        Axios.get(`/app/${app.id}/run`)
          .then(function () {
            // noinspection JSUnresolvedFunction
            new Notyf().confirm(`Started "${app.name}".`)
          })
          .catch(function () {
            // noinspection JSUnresolvedFunction
            new Notyf().alert(`Could not start "${app.name}".`)
          })
      },
      deleteApp: function (app) {
        if (!confirm(`Do you really want to delete "${app.name}"?`)) {
          return;
        }

        let that = this;
        Axios.delete(`/app/${app.id}`)
          .then(function () {
            // noinspection JSUnresolvedFunction
            new Notyf().confirm(`Deleted "${app.name}".`);
            let index = that.$data.apps.indexOf(app);
            that.$data.apps.splice(index, 1);
          })
          .catch(function () {
            // noinspection JSUnresolvedFunction
            new Notyf().alert(`Could not delete "${app.name}".`);
          })
      }
    }
  }
</script>

<style scoped></style>
