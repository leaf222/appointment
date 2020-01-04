import axios from 'axios'
axios.defaults.withCredentials = false;
import VueAxios from 'vue-axios';
import Vue from 'vue'
// Vue.prototype.$axios = axios;
// axios.defaults.headers.post['Content-Type'] = 'application/json;charset=utf-8'
import qs from 'qs'
Vue.prototype.$qs = qs;
Vue.use(VueAxios, axios)

function post(url, data) {
  return axios({
    method: "POST",
    // url: "http://localhost:80/ChinesePoker_war/" + url,
    url: "http://127.0.0.1:8082/" + url,
    //data: data,
    data: qs.stringify(data),//传递的是string

  })
}
  //1 登录
  function login(data){
    //post(,data)
  }

  //2 注册
  function mRegister (data) {

  }
  //3 查看活动
  function viewActivity (data) {

  }
  //4 搜索活动
  function searchActivity (data) {

  }
  //5 查看活动详情
  function viewDetail (data) {

  }
  //6 发布活动
  function publishActivity (data) {

  }
  //7 取消活动
  function cancelActivity (data) {

  }
  //8 审核加入
  function checkParticipantInfo (data) {

  }
  //9 修改活动信息
  function editActivityInfo (data) {

  }
  //10 评价活动
  function valuateActivity (data) {

  }
  //11参加活动
  function joinActivity (data) {

  }
  //12退出活动
  function exitActivity (data) {

  }
  //13收藏活动
  function collectActivity (data) {

  }
  //14取消收藏
  function cancelCollection (data) {

  }
  //15查看我的收藏
  function viewMyCollection (data) {

  }
  //16查看我的发布
  function viewMyPublish (data) {

  }
  //17查看我的参与
  function viewMyParticipant (data) {

  }
  //18查看我的推荐
  function viewMyRecommend (data) {

  }
  //19查看我的申请
  function viewMyApplication (data) {

  }
  //20评分最高
  function viewHighestScore (data) {

  }
  //21参与人数最多
  function viewLargestParticipant (data) {

  }
  //22收藏人数最多
  function viewLargestCollection (data) {

  }
  //23查看活动通知
  function viewMessage (data) {

  }
  //24管理员身份认证
  function checkInfo (data) {

  }
  //25删除活动
  function deleteActivity (data) {

  }
  //26修改个人信息
  function editUserInfo (data) {

  }
  //27退出登录
  function logout (data) {

  }

export default {
  login,
  mRegister,
  viewActivity,
  searchActivity,
  viewDetail,
  publishActivity,
  cancelActivity,
  checkParticipantInfo,
  editActivityInfo,
  valuateActivity,
  joinActivity,
  exitActivity,
  collectActivity,
  cancelCollection,
  viewMyCollection,
  viewMyPublish,
  viewMyParticipant,
  viewMyRecommend,
  viewMyApplication,
  viewHighestScore,
  viewLargestParticipant,
  viewLargestCollection,
  viewMessage,
  checkInfo,
  deleteActivity,
  editUserInfo,
  logout,
}
