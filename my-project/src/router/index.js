import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/Home'
import Login from '../components/Login'
import ActivityMain from '../components/ActivityMain'
import ActivityDetail from '../components/ActivityDetail'
import ActivityRank from '../components/ActivityRank'
import User from '../components/User'
import MApplication from '../components/MApplication'
import MCollection from '../components/MCollection'
import MParticipant from '../components/MParticipant'
import MPublish from '../components/MPublish'
import MRecommend from '../components/MRecommend'

Vue.use(Router)

export default new Router({

  routes: [
    {
      path: '/',
      name: '登录',
      component:Login,
    },
    {
      path: '/home',
      name: '首页',
      component:Home,
      redirect: '/ActivityMain',
      children:[
        {
          path:'/ActivityMain',
          name:'活动首页',
          component:ActivityMain,
          meta: {
            keepalive: true,
            breadcrumb: ['首页', '活动首页']
          }
        },
        {
          path:'/ActivityDetail',
          name:'活动详情',
          component:ActivityDetail,
          meta: {
            keepalive: false,
            breadcrumb: ['首页', '活动详情']
          }
        },
        {
          path:'/ActivityRank',
          name:'活动排行',
          component:ActivityRank,
          meta: {
            keepalive: false,
            breadcrumb: ['首页', '活动排行']
          }
        },
        {
          path:'/User',
          name:'个人主页',
          component:User,
          meta: {
            keepalive: false,
            breadcrumb: ['用户', '个人中心']
          }
        }

      ]
    },

  ]
})
