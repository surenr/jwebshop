import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    inventory: [],
    inCart: [],
    cart: {},
  },
  getters: {
    forSale: state => state.forSale,
    inventory: state => state.inventory,
    inCart: state => state.inCart,
    cart: state => state.cart,
  },
  mutations: {
    UPDATE_CART(state, cart) {
      state.cart = cart;
    },
    UPDATE_INVENTORY(state, inventory){
      state.inventory = inventory;
    }
  },
  actions: {
    removeItems({ state, dispatch}, {categoryName, numItems}) {
      const productToRemove = state.inventory.find(product => product.category===categoryName);
      const {
        product: {
          productName = '',
          packagePrice=0,
          numItemsInPackage=0
        }= {}
      } = productToRemove;
      const productRequest = {
        product: {
          productName,
          packagePrice: (packagePrice / 100),
          numItemsInPackage,
        },
        numItems,
      }
      axios.delete('http://localhost:3000/api/cart/product',{ data: productRequest })
      .then(respons => {
        console.log(respons.data);
        dispatch('updateInventory');
        dispatch('updateCart');
      }).catch( error => {
        console.log(error);
        alert(error.response.data.message);
      })
    },
    addToCart({ state, dispatch}, {categoryName, numItems}) { 
      const productToAdd = state.inventory.find(product => product.category===categoryName);
      console.log('add to cart action, product to add: ', productToAdd);
      const {
        product: {
          productName='',
          packagePrice=0,
          numItemsInPackage=0
        } = {}
      } = productToAdd;
      const productRequest = {
        product: {
          productName,
          packagePrice: (packagePrice / 100),
          numItemsInPackage,
        },
        numItems,
      }
      axios.post('http://localhost:3000/api/cart/product', productRequest)
      .then(respons => {
        console.log(respons.data);
        dispatch('updateInventory');
        dispatch('updateCart');
      }).catch( error => {
        alert(error.response.data.message);
      })
     
    },
    updateInventory({state, commit}) {
      axios.get('http://localhost:3000/api/products')
      .then(respons => {
        const inventory = respons.data.map(inventoryItem => {
          inventoryItem.individualItemPrice = (inventoryItem.individualItemPrice * 100);
          inventoryItem.product.packagePrice = (inventoryItem.product.packagePrice * 100);
          inventoryItem.product.itemPrice = (inventoryItem.product.itemPrice * 100);
          return inventoryItem;
        });
        commit('UPDATE_INVENTORY', inventory.sort((a,b) => a.category > b.category));
      }).catch( error => {
        console.log(error);
        alert(error.response.data.message);
      })

    },
    updateCart({commit, state}) {
      axios.get('http://localhost:3000/api/cart')
      .then(respons => {
        const cart = respons.data;
        console.log('************** CART RETERIVED!!!! **************');
        console.log(cart);
        cart.total = (cart.total * 100);
        cart.totalDiscount = (cart.totalDiscount * 100);
        const shoppingCartItems = cart.cartItems.map(cartItem =>{
          const relatedProduct = state.inventory.find(product =>
            product.category === cartItem.category);
            cartItem.name = relatedProduct.product.displayName;
            cartItem.totalBoxPrice = (cartItem.totalBoxPrice * 100);
            cartItem.totalItemPrice = (cartItem.totalItemPrice * 100);
            cartItem.totalPrice = (cartItem.totalPrice * 100);
            cartItem.numItemsInPackage = relatedProduct.product.numItemsInPackage;
            // NB: Add what ever from the product to the cart item here.
            return cartItem;
        });

        cart.cartItems = shoppingCartItems.sort((a,b) => a.name > b.name);
        commit('UPDATE_CART', cart);
      }).catch( error => {
        console.log(error);
        alert(error.response.data.message);
      })
    },
    removeFromCart({ dispatch}, categoryName) {
      console.log('****************** REMOVE ENTIERLY FROM CART CALLED **********');
      axios.delete(`http://localhost:3000/api/cart/category/${categoryName}`)
      .then(respons => {
        console.log(respons.data);
        dispatch('updateCart');
        dispatch('updateInventory');
      }).catch( error => {
        console.log(error);
        alert(error.response.data.message);
      })
    },
  },
});
