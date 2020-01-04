module.exports = [{
  name: '活动',
  id: 'Activity',
  sub: [
    {
      name: '活动首页',
      path: '/ActivityMain',
      componentName: 'ActivityMain'
    },
    {
      name: '活动详情',
      path: '/ActivityDetail',
      componentName: 'ActivityDetail'
    },
    {
      name: '活动排行',
      path: '/ActivityRank',
      componentName: 'ActivityRank'
    },
  ]
},{
  name: '用户',
  id: 'User',
  sub: [
    {
      name: '个人中心',
      path: '/User',
      componentName: 'User'
    },
  ]
}]
