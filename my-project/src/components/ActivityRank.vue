<template>
    <div >
<!--      <PageView/>-->
        <a-tabs
          type="card"
          @change="panesChange"
          tabPosition="top">
          <a-tab-pane v-for="pane in panes" :tab="pane.title" :key="pane.key"></a-tab-pane>
          <a-button slot="tabBarExtraContent" >Extra Action</a-button>
        </a-tabs>

        <a-row :gutter="24"  >
          <a-list :grid="{ gutter: 16, column: 4 }" v-for="item in data" :key="item.title">
            <a-list-item >
              <a-card :title="item.title"   >
                <img :src="item.imgUrl"
                     slot="cover"
                />
                <a-card-meta :title="item.address">
                  <template slot="description">{{item.description}}</template>
                </a-card-meta>
                <router-link :to="{name: '活动详情'}" active-class="active">
                  <a-card-meta
                    title="活动详情"
                    description="点击查看活动详细信息">
                  </a-card-meta>
                </router-link>
              </a-card>
            </a-list-item>
          </a-list>
        </a-row>
    </div>
</template>
<script>
    // import PageView from "../../layouts/PageView";
    export default {
        name: "ActivityRank",
        components:{
            // PageView,
        },

        data(){
            return{
                position:"center",
                //panes 定义标签类型
                panes:[{title:"评分最高",key:0},{title:"收藏最高",key:1},{title:"参与最多",key:2},{title:"评论最多",key:3}],
                data:[{
                    title: '这里是标题',//活动标题
                    imgUrl:"https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png",//活动图片
                    description:"这里是描述", //活动描述
                    address:"这里是地址"//活动地址
                }]
            }
        },
        methods:{
            panesChange(key) {
                const data=this.data
                data.pop() //清空所有图片
                data.push({
                    title:"收藏最高"+key,
                    imgUrl:"https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png",
                    description:"这里是描述",
                    address:"这里是地址"
                })
                this.data=data
                console.log(this.data)
            },
        },
    }
</script>

<style scoped>


</style>
