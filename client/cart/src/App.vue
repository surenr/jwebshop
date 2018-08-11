<template>
  <div id="app" class="container my-5">
    <div class="row mb-3">
      <div class="col-md-9">
        <h1>My online store</h1>
      </div>
      <div class="col-md-3">
        <ShoppingCart />
      </div>
    </div>

    <div class="row">
      <Item
        v-for="item in forSale"
        :key="item.invId"
        :invId="item.invId"
        :name="item.name"
        :category="item.productName"
        :image="item.image"
        :description="item.description"
        :itemsInBox="item.numItemsInPackage"
        :price="item.price" />
    </div>
  </div>
</template>

<script>
import Item from './Item.vue';
import ShoppingCart from './ShoppingCart.vue';
import axios from 'axios';
import safePromise from './utils/safe.js'

export default {
  name: 'app',
  computed: {
    forSale() { return this.$store.getters.forSale; },
  },
  components: {
    Item,
    ShoppingCart,
  },
  created() {
    console.log('Testing fetch for api communication');
    this.$store.dispatch('updateCart');
  }
};
</script>
