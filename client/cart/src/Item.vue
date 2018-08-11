<template>
  <div class="col-md-3">
    <div class="card">
      <img :src="image" :alt="name" class="card-img-top" width="250" height="250">
      <div class="card-body">
        <h4 class="card-title">{{ name }}</h4>
        <div class="cart-text">{{ description }}</div>
        <div class="card-text"><strong>{{ price | dollars }}</strong></div>
        <div class="row justify-content-end">
          <div class="col-md-6">
            <button class="btn btn-success" @click="addToCart(category, itemsInBox)">Buy a Box</button>
          </div>
           <div class="col-md-6">
            <button class="btn btn-primary" @click="addToCart(category, 1)">Buy Now</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { dollars } from './filters';

export default {
  name: 'item',
  props: ['invId', 'name', 'image', 'price', 'description', 'itemsInBox', 'category'],
  filters: {
    dollars,
  },
  methods: {
    addToCart(categoryName, numItems) {
      console.log('add to cart clicked: ', categoryName, numItems);
      this.$store.dispatch('addToCart', {categoryName, numItems});
    },
  },
};
</script>
