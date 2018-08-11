<template>
  <div>
    <button class="btn btn-primary" data-toggle="modal" data-target="#shoppingCart">Cart (Boxs: {{ numInCart.boxCount }}, Items: {{ numInCart.itemCount }})</button>

    <div id="shoppingCart" class="modal fade">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Shopping cart</h5>
            <button class="close" data-dismiss="modal">
              &times;
            </button>
          </div>
          <div class="modal-body">
            <table class="table">
              <tbody>
                <tr>
                  <th>Name</th>
                  <th>No.Boxes</th>
                  <th>No.Items</th>
                  <th>Price</th>
                  <th></th>
                </tr>
                <tr v-for="item in cart.cartItems">
                  <td>{{ item.name }}</td>
                  <td>{{ item.boxQuantity }} 
                   <button
                    class="btn btn-sm btn-outline-success button-xs"
                    @click="addToCart(item.category, item.numItemsInPackage)">&#9652;</button>&nbsp;
                   <button
                    class="btn btn-sm btn-outline-danger button-xs"
                    @click="removeItems(item.category, item.numItemsInPackage)">&#9662;</button>
                  </td>
                  <td>{{ item.itemQuantity }}
                    <button class="btn btn-sm btn-outline-success button-xs"
                    @click="addToCart(item.category, 1)" >&#9652;</button>&nbsp;
                   <button class="btn btn-sm btn-outline-danger button-xs"
                    @click="removeItems(item.category, 1)">&#9662;</button>
                  </td>
                  <td>{{ item.totalPrice | dollars }}</td>
                  <td>
                    <button class="btn btn-sm btn-danger button-xs" @click="removeFromCart(item.category)">&times;</button>
                  </td>
                </tr>
                <tr>
                  <th></th>
                  <th></th>
                  <th colspan="3">
                    <div class="row">
                      <div class="col-md-6">Total: </div>
                      <div class="col-md-6"> {{ total | dollars }}</div>
                    </div>
                    <div class="row">
                      <div class="col-md-6">Discount: </div>
                      <div class="col-md-6"> {{ discount | dollars }}</div>
                    </div>
                   </th>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" data-dismiss="modal">Keep shopping</button>
            <button class="btn btn-primary">Check out</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { dollars } from './filters';

export default {
  name: 'shoppingCart',
  computed: {
    cart() {
      return this.$store.getters.cart;
    },
    numInCart() {
      const boxCount = 0;
      const itemCount = 0;
      let cartSummary = { boxCount, itemCount };
      if(this.cart.cartItems){
        cartSummary = this.cart.cartItems.reduce((tempSummary, cartItem) => {
          let {
            boxCount = 0,
            itemCount = 0,
          } = tempSummary;
          boxCount += cartItem.boxQuantity;
          itemCount += cartItem.itemQuantity;
          return {boxCount, itemCount};
        }, { boxCount, itemCount})
      }
      return cartSummary;
    },
    total() {
      return this.cart.total;
    },
    discount() {
      return this.cart.totalDiscount;
    },
  },
  filters: {
    dollars,
  },
  methods: {
    removeFromCart(categoryName) { this.$store.dispatch('removeFromCart', categoryName); },
    addToCart(categoryName, numItems) { this.$store.dispatch('addToCart',
      {categoryName, numItems});},
    removeItems(categoryName, numItems) { this.$store.dispatch('removeItems', 
      {categoryName, numItems});}
  },
};
</script>
<style scoped>
.button-xs {
  width:20px; 
  height:20px; 
  line-height:20px; 
  margin:0px; 
  padding:0px;
}
</style>
