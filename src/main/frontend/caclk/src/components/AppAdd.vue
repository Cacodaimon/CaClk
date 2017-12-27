<template>
  <div class="card">
    <div class="card-body">
      <h4 class="card-title">Add an app</h4>
      <form @submit.prevent="addApp">

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
        app: {
          name: "",
          version: "0.0.1",
          author: {
            name: ""
          },
          autoStart: false,
          permissions: ["LED"],
          code: `var app = function(ledDisplay) {
  ledDisplay
    .clear()
    .putPixel(1, 1, Color.GREEN)
    .render();
};`
        }
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
