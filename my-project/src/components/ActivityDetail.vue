<template>
  <div align="left">
   <div>
<!--     <a-card title="活动详情" :bordered="false">-->
     <a-row :gutter="24" >
       <a-col :lg="3"/>

       <a-col :lg="7" >
         <a-card hoverable title="活动图片">
                   <img
                     :src="imageUrl"
                     slot="cover"
                   />
         </a-card>

       </a-col>

        <a-col :lg="11">
          <div>
            <a-row>
              <h3>标题</h3>
              <span >样例活动</span>
            </a-row>
            <a-row>
              <h3>描述</h3>
              <span >样例活动</span>
            </a-row>
            <a-row>
              <h3>时间</h3>
              <span >{{timeStart}}~{{endTime}}</span>
            </a-row>
            <a-row>
              <h3>地点</h3>
              <span >样例活动</span>
            </a-row>
            <a-row align="bottom">
              <a-col :md="8"  >
                <h3>收藏人数</h3>
                <span>
                  <a-icon type="star" style="margin-right: 8px" theme="twoTone"></a-icon>
                  {{collectionNum}}
                </span>
              </a-col>
              <a-col :md="8">
                <h3>参加人数</h3>
                <span style="">
                  <a-icon type="plus-circle" style="margin-right: 8px" theme="twoTone"></a-icon>
                  {{joinNum}}
                </span>
              </a-col>
              <a-col :md="8">
                <h3>评论数目</h3>
                <span style="">
                  <a-icon type="edit" style="margin-right: 8px" theme="twoTone" ></a-icon>
                  {{commentsNum}}
                </span>
              </a-col>
            </a-row>
            <a-row>
              <h3>评分</h3>
              <template>
                <span>
                 <a-rate   v-model="scoreIndex"   @change="rateChange"></a-rate>
                  {{scoreList[scoreIndex - 1]}}
                </span>
              </template>
              </a-row>
            <a-button  slot="extra" :icon="iconType"  :type="btnType" @click="btnClick">{{btnText}}</a-button>
          </div>
        </a-col>

       <a-col :lg="3"/>
     </a-row>
   </div>

  <br>
    <div>
      <a-card  title="评论" :bordered="false">
        <a-list  :loading="loading">
          <div
            v-if="showLoadingMore"
            slot="loadMore"
            :style="{ textAlign: 'center', marginTop: '12px', height: '32px', lineHeight: '32px' }"
          >
            <a-spin v-if="loadingMore" />
            <a-button v-else @click="onLoadMore">loading more</a-button>
          </div>
          <a-list-item :key="index" v-for="(item, index) in comments" >
            <a-list-item-meta >
              <a slot="title" href="https://www.antdv.com/">{{item.content}}</a>
              <span slot="description">{{item.description}}</span>
              <a-avatar
                slot="avatar"
                :src="avatarUrl"
              />
            </a-list-item-meta>

          </a-list-item>
        </a-list>
      </a-card>
    </div>
  </div>
</template>

<script>
  // import PageView from "../../layouts/PageView";
  // import HeadInfo from "../../components/tools/HeadInfo";
  // import Homepage from "../user/Homepage";
  // import {getWarehousePreview} from "../../api/warehouse";

    export default {

        name: "ActivityDetail",
        components:{
           },
        data() {
            return {
                avatarUrl:'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
                loading: true,
                loadingMore: false,
                showLoadingMore: true,
                btnType:"primary",
                iconType:"plus",
                btnText:"申请",
                timeStart :"2019/08/25",
                endTime:"2019/11/25",
                imageUrl:'',
                collectionNum:"100",
                joinNum:"100",
                commentsNum:"4",
                scoreIndex: 4,
                scoreList:[
                    2,4,6,8,10
                ],
                comments:[
                    {content:"hello1",description:"1"},
                    {content:"hello2",description:"2"},
                    {content:"hello3",description:"3"},
                    {content:"hello4",description:"4"}
                ],
                params:[],
                responseBody:[],
            }
        },
        methods:{
            onLoadMore(){
                this.loadingMore = true;
                this.comments.push({content:"hello1",description:"8"},)
                this.loading = false;
                this.loadingMore = false;
                // console.log("Loading状态"+this.loading)
                // //axios测试
                // getWarehousePreview().then((response)=>{
                //     console.log(response.data);
                //     this.responseBody=response.data
                //     //遍历数组,去除指定项目
                //     this.responseBody.forEach(item=>{
                //         if(item.name==='嘉定仓库'){
                //             //变量使用，承接responseBody的内容
                //             this.comments.push({content:item.id,description:item.address},)
                //             this.responseBody.splice(item,1)
                //         }
                //     })
                //     console.log(response.data);
                //     this.loadingMore = false;
                // }).catch((response)=>{
                //     console.log(response);
                // })
                // this.$http.get('/warehouse/preview').then((response)=>{
                //     console.log(response.data);
                //     this.loadingMore = false;
                // }).catch((response)=>{
                //     console.log(response);
                // })
            },
            rateChange:function(){
                //this.score=score;
                console.log("新的" + this.score)
            },
            btnClick:function () {
                console.log(this.btnText)
             if(this.btnText==="申请")
              {   this.btnText="he";
                console.log("新的" + this.btnText)
              return;}
             if(this.btnText==="he"){
                 this.btnText="heidie";
                 console.log("新的" + this.btnText);
                 return;
             }

             if(this.btnText==="heidie"){
                 this.btnText="退出活动";
                 this.iconType="delete";
                 this.btnType="danger"
                 console.log("最终的" + this.btnText);
                 return;
             }
            }
        },
        activated() {
            console.log("11111111111111111111111111")
           this.imageUrl=this.$store.state.imageUrl;
          this.loading = false;
            // this.params=this.$route.query
            // this.collectionNum=this.$route.query.test,
            // console.log(this.params.userID)
            // if(this.params.userID==='id1'){
            //     this.iconType='delete',
            //         this.btnText="取消申请"
            //     this.btnType='danger'
            //     this.imageUrl=this.params.imageUrl
            // }

        },

    }

</script>

<style scoped>

</style>
