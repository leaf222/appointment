<template>
  <div class="app-list">
    <a-list
      :grid="{ gutter: 24, lg: 2, md: 2, sm: 1, xs: 1 }"
      :dataSource="dataSource"
    >
      <a-list-item slot="renderItem" slot-scope="item" >
        <a-card :hoverable="true">
          <a-card-meta @click="detailClick">
            <div style="margin-bottom: 3px" slot="title">{{ item.title }}</div>
            <a-avatar class="card-avatar" slot="avatar" :src="item.imgUrl" :size="60" />
            <div class="meta-cardInfo" slot="description">
              {{item.description}}
            </div>
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
            <a @click="delClick(item)">
              <a-icon type="delete"></a-icon>
              <span>取消发布</span>
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
        name: 'MPublish',
        components: {},
        data () {
            return {
                dataSource
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

  .app-list {

    .meta-cardInfo {
      zoom: 1;
      margin-top: 16px;

      > div {
        position: relative;
        text-align: left;
        float: left;
        width: 50%;

        p {
          line-height: 32px;
          font-size: 24px;
          margin: 0;

          &:first-child {
            color: rgba(0, 0, 0, .45);
            font-size: 12px;
            line-height: 20px;
            margin-bottom: 4px;
          }
        }

      }
    }
  }

</style>
