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
        v-for="item in inventory"
        :key="item.category"
        :name="item.product.displayName"
        :category="item.category"
        :image="item.product.imgUrl"
        :description="item.product.description"
        :itemsInBox="item.product.numItemsInPackage"
        :itemSellingPrice="item.individualItemPrice"
        :numBoxes="item.boxQuantity"
        :numItems="item.numOfProductsInCategory"
        :boxSellingPrice="item.product.packagePrice" />
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
    inventory() { return this.$store.getters.inventory; },
  },
  components: {
    Item,
    ShoppingCart,
  },
  created() {
    console.log('Testing fetch for api communication');
    this.$store.dispatch('updateInventory');
    this.$store.dispatch('updateCart');
  }
};
</script>
