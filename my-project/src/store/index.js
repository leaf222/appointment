import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate"
Vue.use(Vuex)

const  store=new Vuex.Store({
  state:{
    //用户相关
    userID:'',
    status:'',
    userName:'',
    password:'',
    isAdmin:'',
    phone:'',
    email:'',
    avatarUrl:'',

    //活动相关
    imageUrl:'',
    title:'',
    collectionNum:'',
    score:'',
    participantNum:'',
    commentsNum:'',
    startTime:'',
    endTime:'',
    comments:[],

    //路由相关
    routes:[]

  },
  mutations:{
    setImageUrl(state,data){
      this.state.imageUrl=data;
    },

    setUserName(state,data){
      this.state.userName=data;
    },

    setUserID(state,data){
      this.state.userID=data;
    },

    setAvatarUrl(state,data){
      this.state.avatarUrl=data;
    },
    setUserStatus(state,data){
      this.state.status=data;
    }

  },
  //本地化存储state数据
  plugins: [createPersistedState({
    storage: window.sessionStorage
  })]
})
export default store
