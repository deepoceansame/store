import { createStore } from 'vuex'

declare let SessionStorage: any;

const store = createStore({
  state: {
    account: SessionStorage.get("ACCOUNT") || {}
  },
  mutations: { //commit
    setAccount(state, account){
      state.account = account;
      SessionStorage.set("ACCOUNT", account)
    }
  },
  actions: {
  },
  modules: {
  }
})

export default store;
