<template>
  <div class="ant-pro-pages-account-projects-cardList">
    <a-list  :data-source="dataSource" :grid="{ gutter: 24, xxl: 3, xl: 2, lg: 2, md: 2, sm: 2, xs: 1 }">
      <a-list-item slot="renderItem" slot-scope="item">
        <a-card class="ant-pro-pages-account-projects-card" hoverable>
          <img slot="cover" :src="item.imgUrl" :alt="item.title" @click="detailClick"/>
          <a-card-meta :title="item.title" @click="detailClick">
            <template slot="description">
              <p>{{ item.description }}</p>
            </template>
          </a-card-meta>
            <template class="ant-card-actions" slot="actions">
              <a>
                <a-icon type="like"/>
                <span>{{item.like}}</span>
              </a>
              <a>
                <a-icon type="message"/>
                <span>{{item.commentNum}}</span>
              </a>
              <a>
                <a-icon type="share-alt"/>
                <span>{{item.mScore}}</span>
              </a>
              <a>
                <a-icon type="share-alt"/>
                <span>{{item.participantNum}}</span>
              </a>
              <a @click="delClick(item)" >
                <a-icon type="delete"></a-icon>
                <span>退出活动</span>
              </a>
            </template>
        </a-card>
      </a-list-item>
    </a-list>
  </div>
</template>

<script>
    // import IconText from '../list/search/components/IconText'
    const dataSource = []
    for (let i = 0; i < 11; i++) {
        dataSource.push({
            imgUrl:"https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png",
            title:'样例活动'+i,
            description:'发布日期',
            like:'收藏数量',
            commentNum:'评论数量',
            mScore:'这里是评分',
            participantNum:'参与人数',
            organiser:'这里是举办人名称'
        })
    }
    export default {
        name: "MParticipant",
        data(){
            return{
                dataSource,
            }

        },
        methods:{
            delClick(item){
                this.dataSource.splice(this.dataSource.indexOf(item),1);
                console.log("你点击了删除按钮"+item.toString())
            },
            detailClick(e){
                console.log("你点击了查看详情"+e.toString())
            }

        }
    }
</script>

<style lang="less" scoped>
  .ant-pro-pages-account-projects-cardList {
    margin-top: 24px;

    /deep/ .ant-card-meta-title {
      margin-bottom: 4px;
    }

    /deep/ .ant-card-meta-description {
      height: 44px;
      overflow: hidden;
      line-height: 22px;
    }

    .cardItemContent {
      display: flex;
      height: 20px;
      margin-top: 16px;
      margin-bottom: -4px;
      line-height: 20px;

      > span {
        flex: 1 1;
        color: rgba(0,0,0,.45);
        font-size: 12px;
      }

      /deep/ .ant-pro-avatar-list {
        flex: 0 1 auto;
      }
    }
  }
</style>
