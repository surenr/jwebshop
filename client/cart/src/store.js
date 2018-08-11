import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    forSale: [
      { invId: 1,
        itemsInStock: 50,
        image: 'http://img4.wikia.nocookie.net/__cb20140418020028/clubpenguin/images/e/e0/Clothing_Icons_1849.png',
        name: 'Penguine Ears',
        description: 'Penguine Ears are great to keep you warm',
        productName: 'PenguineEars', // NB: Must be unique
        packagePrice: 175,
        numItemsInPackage: 20,
        price: 875,
      },
      { invId: 2,
        itemsInStock: 50,
        image: 'https://images-na.ssl-images-amazon.com/images/I/71iqNBxfV2L._SL1024_.jpg',
        name: 'Horse Shoe',
        description: 'Horse Shoes are great at bringing you better luck',
        productName: 'HorseShoes', // NB: Must be unique
        packagePrice: 825,
        numItemsInPackage: 5,
        price: 16500,
      },
      { invId: 3,
        itemsInStock: 50,
        image: 'https://www.geschenke-trolle.de/prodima/840191.jpg',
        name: 'Norwegian Troll',
        description: 'They are the best!!',
        productName: 'NorwegianTrolls', // NB: Must be unique
        packagePrice: 400,
        numItemsInPackage: 10,
        price: 4000,
      },
      { invId: 4,
        itemsInStock: 50,
        image: 'http://www.houseofgifts.lk/media/catalog/product/cache/1/thumbnail/480x/17f82f742ffe127f42dca9de82fb58b1/i/m/img_7164.jpg',
        name: 'Sri Lankan Decorated Elephant',
        description: 'Feel free to gift away',
        productName: 'DecoratedElephant', // NB: Must be unique
        packagePrice: 300,
        numItemsInPackage: 10,
        price: 3000,
      },
    ],
    inCart: [],
    cart: {},
  },
  getters: {
    forSale: state => state.forSale,
    inCart: state => state.inCart,
    cart: state => state.cart,
  },
  mutations: {
    UPDATE_CART(state, cart) {
      state.cart = cart;
    }
  },
  actions: {
    removeItems({ state, dispatch}, {categoryName, numItems}) {
      const productToRemove = state.forSale.find(product => product.productName===categoryName);
      const {
        productName='',
        packagePrice=0,
        numItemsInPackage=0
      } = productToRemove;
      const productRequest = {
        product: {
          productName,
          packagePrice,
          numItemsInPackage,
        },
        numItems,
      }
      axios.delete('http://localhost:3000/api/cart/product',{ data: productRequest })
      .then(respons => {
        console.log(respons.data);
        dispatch('updateCart');
      }).catch( error => {
        console.log(error);
        alert(error.message);
      })
    },
    addToCart({ state, dispatch}, {categoryName, numItems}) { 
      const productToAdd = state.forSale.find(product => product.productName===categoryName);
      console.log('add to cart action, product to add: ', productToAdd);
      const {
        productName='',
        packagePrice=0,
        numItemsInPackage=0
      } = productToAdd;
      const productRequest = {
        product: {
          productName,
          packagePrice,
          numItemsInPackage,
        },
        numItems,
      }
      axios.post('http://localhost:3000/api/cart/product', productRequest)
      .then(respons => {
        console.log(respons.data);
        dispatch('updateCart');
      }).catch( error => {
        console.log(error, error.data, error.error);
        alert(error.message);
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
          const relatedProduct = state.forSale.find(product =>
            product.productName === cartItem.category);
            cartItem.name = relatedProduct.name;
            cartItem.totalBoxPrice = (cartItem.totalBoxPrice * 100);
            cartItem.totalItemPrice = (cartItem.totalItemPrice * 100);
            cartItem.totalPrice = (cartItem.totalPrice * 100);
            cartItem.numItemsInPackage = relatedProduct.numItemsInPackage;
            // NB: Add what ever from the product to the cart item here.
            return cartItem;
        });

        cart.cartItems = shoppingCartItems.sort((a,b) => a.name > b.name);
        commit('UPDATE_CART', cart);
      }).catch( error => {
        console.log(error);
        alert(error.message);
      })
    },
    removeFromCart({ dispatch}, categoryName) {
      console.log('****************** REMOVE ENTIERLY FROM CART CALLED **********');
      axios.delete(`http://localhost:3000/api/cart/category/${categoryName}`)
      .then(respons => {
        console.log(respons.data);
        dispatch('updateCart');
      }).catch( error => {
        console.log(error);
        alert(error.message);
      })
    },
  },
});
