<template>
  <div class="card">
    <div class="card-body">
      <h4 class="card-title">Add an app</h4>

      <ul class="nav nav-tabs nav-justified">
        <li class="nav-item">
          <a @click.prevent="activeTab = 0"
             :class="{active: activeTab === 0}"
             class="nav-link"
             href="#">Simple add</a>
        </li>
        <li class="nav-item">
          <a @click.prevent="activeTab = 1"
             :class="{active: activeTab === 1}"
             class="nav-link"
             href="#">Add from JSON</a>
        </li>
      </ul>

      <form @submit.prevent="addApp">

        <section v-show="activeTab === 0">
        <div class="form-group">
          <label for="app.name">Name</label>

          <input v-model="app.name"
                 id="app.name"
                 class="form-control"
                 type="text"
                 required="required"/>
        </div>

        <div class="form-group">
          <label for="app.author.name">Author</label>
          <input v-model="app.author.name"
                 id="app.author.name"
                 class="form-control"
                 type="text"
                 required="required"/>
        </div>
        </section>

        <section v-show="activeTab === 1">

          <div class="form-group">
            <label for="json">
              App JSON
            </label>
            <textarea v-model="json"
                      id="json"
                      class="form-control"
                      rows="10"></textarea>
          </div>
        </section>

        <hr/>

        <button type="submit" class="btn btn-primary">Add</button>
      </form>
    </div>
  </div>
</template>

<script>
  import Axios from 'axios'
  import Notyf from 'notyf-js'
  import '../../node_modules/notyf-js/dist/notyf.min.css'
  import router from '../router'

  export default {
    name: 'AppAdd',
    data: function () {
      return {
        activeTab: 0,
        json: "",
        app: {
          name: "",
          version: "0.0.1",
          author: {
            name: ""
          },
          settings: "{}",
          autoStart: false,
          permissions: ["LED"],
          code: `var init = function() {
  ledDisplay
    .clear()
    .putPixel(1, 1, Color.GREEN)
    .render();
};`
        }
      }
    },
    mounted: function () {
      this.$data.json = JSON.stringify(this.$data.app, null, 2)
    },
    watch: {
      json: function () {
        this.$data.app = JSON.parse(this.$data.json)
      },
      "app.name": function () {
        this.$data.json = JSON.stringify(this.$data.app, null, 2)
      },
      "app.author.name": function () {
        this.$data.json = JSON.stringify(this.$data.app, null, 2)
      }
    },
    methods: {
      addApp: function () {
        let that = this
        Axios.post('/app', this.$data.app)
          .then(function (response) {
            // noinspection JSUnresolvedFunction
            new Notyf().confirm(`Added "${that.$data.app.name}".`)
            router.push(response.headers.location)
          })
          .catch(function (e) {
            // noinspection JSUnresolvedFunction
            new Notyf().alert(`Could not add "${that.$data.app.name}".`)
            console.log(e)
          })
      }
    }
  }
</script>

<style scoped></style>
