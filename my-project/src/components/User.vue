<template>
  <div class="page-header-index-wide page-header-wrapper-grid-content-main">
  <a-row :gutter="24">
    <a-col :md="24" :lg="7">
      <a-card :bordered="false">
        <div class="account-center-avatarHolder">
          <div class="avatar">
            <img :src="imageUrl">
          </div>
          <div class="username">{{ nickname}}</div>
          <div class="bio">{{mTarget}}</div>
        </div>
        <div class="account-center-detail">
          <p>
            <i class="phone"></i>{{phoneNum}}
          </p>
          <p>
            <i class="group"></i>{{emailNum}}
          </p>
          <p>
            <i class="address"></i>
           {{addressDetail}}
          </p>
        </div>
        <a-divider/>

        <div class="account-center-tags">
          <div class="tagsTitle">我的兴趣</div>
          <div>
            <template v-for="(tag, index) in tags">
              <a-tooltip v-if="tag.length > 20" :key="tag" :title="tag">
                <a-tag
                  :key="tag"
                  :closable="index !== 0"
                  :afterClose="() => handleTagClose(tag)"
                >{{ `${tag.slice(0, 20)}...` }}</a-tag>
              </a-tooltip>
              <a-tag
                v-else
                :key="tag"
                :closable="index !== 0"
                :afterClose="() => handleTagClose(tag)"
              >{{ tag }}</a-tag>
            </template>
            <a-input
              v-if="tagInputVisible"
              ref="tagInput"
              type="text"
              size="small"
              :style="{ width: '78px' }"
              :value="tagInputValue"
              @change="handleInputChange"
              @blur="handleTagInputConfirm"
              @keyup.enter="handleTagInputConfirm"
            />
            <a-tag v-else @click="showTagInput" style="background: #fff; borderStyle: dashed;">
              <a-icon type="plus"/>New Tag
            </a-tag>
          </div>
        </div>
        <a-divider :dashed="true"/>
        <div class="account-center-avatarHolder">
          <a-button type="default" @click="editClick">修改个人信息</a-button>
        </div>
      </a-card>
    </a-col>
    <a-col :md="24" :lg="17">
      <a-card>
      <a-tabs
        type="card"
        @change="panesChange"
        tabPosition="top">
        <a-tab-pane v-for="pane in panes" :tab="pane" :key="pane"></a-tab-pane>
      </a-tabs>
        <MCollection v-if="pane==='我的收藏'"></MCollection>
        <MPublish v-if="pane==='我的发布'"></MPublish>
        <MApplication v-if="pane==='我的申请'"></MApplication>
        <MParticipant v-if="pane==='我的参与'"></MParticipant>
        <MRecommend v-if="pane==='我的推荐'"></MRecommend>
      </a-card>
    </a-col>
  </a-row>
    <a-modal
      title="修改个人信息"
      v-model="visible"
      @ok="hideModal"
      okText="确认"
      cancelText="取消"
      width="800px"
    >
      <div class="account-settings-info-view">
        <a-row :gutter="16">
          <a-col :md="16" :lg="16">
            <a-form layout="vertical">
              <a-form-item
                label="昵称"
              >
                <a-input placeholder="给自己起个名字" />
              </a-form-item>
              <a-form-item
                label="Bio"
              >
                <a-textarea rows="4" placeholder="You are not alone."/>
              </a-form-item>
              <a-form-item
                label="电话号码"
                :required="false"
              >
                <a-input placeholder="17717808496"/>
              </a-form-item>
              <a-form-item
                label="电子邮件"
                :required="false"
              >
                <a-input placeholder="exp@admin.com"/>
              </a-form-item>
              <a-form-item
                label="登录密码"
                :required="false"
              >
                <a-input placeholder="密码"/>
              </a-form-item>

            </a-form>
          </a-col>
          <a-col :md="8" :lg="8" :style="{ minHeight: '180px' }">
            <el-upload
              ref="upload"
              class="avatar-uploader"
              :show-file-list="false"
              :multiple="false"
              :auto-upload="false"
              :action="actionPath"
              :on-change="imgSaveToUrl"
              :before-upload="beforeAvatarUpload"
              :data="postData"
              :on-success="handleAvatarSuccess">
              <img v-if="imageUrllocal" :src="imageUrllocal" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
              <a-button  type="primary" @click="uploadClick">上传</a-button>
          </a-col>
        </a-row>
      </div>
    </a-modal>
    <div>
      <a-back-top />
      Scroll down to see the bottom-right
      <strong style="color: rgba(64, 64, 64, 0.6)"> gray </strong>
      button.
    </div>
  </div>
</template>

<script>
    import {genUpToken} from '../api/qiniuToken'
    import  MCollection from '../components/MCollection'
    import  MPublish from '../components/MPublish'
    import  MApplication from '../components/MApplication'
    import  MParticipant from '../components/MParticipant'
    import  MRecommend from '../components/MRecommend'
    export default {
        name: "User",
        components: {

            MCollection,
            MPublish,
            MApplication,
            MParticipant,
            MRecommend,
        },
        data(){
            return{
                localFile:{},
                imageUrllocal:'',
                imageUrl: 'https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png',
                actionPath:'https://upload.qiniup.com', //
                postData:{},
                previewVisible: false,
                previewImage: '',
                visible:false,
                activityKey:'',
                emailNum:"这里是邮箱地址",
                addressDetail:"这里是详细地址",
                phoneNum:"这里是电话号码",
                mTarget:"这里是个性签名",
                nickname:"这里是用户名",
                password:"这里是密码",
                pane:'我的发布',
                panes:['我的申请','我的发布','我的收藏','我的参与','我的推荐'],
                tags: ['运动', '游泳', '历史', '电子', '游戏', '睡觉'],
                tagInputVisible: false,
                tagInputValue: '',
                data:[]
            }
        },
        created(){
            let token;
            const policy = {};
            const bucketName = 'lvhaijunzhdr';
            let AK = 'Ej7p69t0bM_Jmu9B-sGSN53IpDuoiEEx60q8vSa0';
            let SK = 'Mu6uejyDNQJ1NeiNYwS93ukQi2V4qJCOUv3J9Si7';
            const deadline = Math.round(new Date().getTime() / 1000) + 3600;
            policy.scope = bucketName;
            policy.deadline = deadline;
            token=genUpToken(AK, SK, policy);
            this.postData.token=token;
            console.log("Token生成"+ token);

        },
        methods:{
            imgSaveToUrl(file){  // 也可以用file
                this.localFile=file.raw // 或者 this.localFile=file.raw

                // 转换操作可以不放到这个函数里面，
                // 因为这个函数会被多次触发，上传时触发，上传成功也触发
                let reader = new FileReader()
                reader.readAsDataURL(this.localFile);// 这里也可以直接写参数event.raw
                // 转换成功后的操作，reader.result即为转换后的DataURL ，
                // 它不需要自己定义，你可以console.log(reader.result)看一下
                // reader.onload=()=>{
                //     console.log(reader.result)
                // }
                // /* 另外一种本地预览方法 */
                let URL = window.URL || window.webkitURL;
                this.imageUrllocal= URL.createObjectURL(file.raw);
            },
            uploadClick(){
                this.$refs.upload.submit();
                console.log("上传开始")
            },
            handleAvatarSuccess(res, file) {
                //这个是上传成功后的操作
                //最后根据你七牛云上绑定的域名 拼接了这个key 就是你上传文件的地址了
                this.imageUrl = 'http://q3botokli.bkt.clouddn.com/'+ res.key
                console.log(this.imageUrl)
            },
            beforeAvatarUpload(file) {
                //这个函数是上传之前进行的操作，我们在这里可以限制上传文件的大小

                const isLt2M = file.size / 1024 / 1024 < 2;


                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                }
                return isLt2M;
            },

            editClick(){
                this.imageUrllocal=this.imageUrl
                console.log("你点击了修改按钮");
                this.visible=true
            },
            hideModal(){
                this.isUpload=true
                console.log("你点击了确认按钮");
                this.visible=false
            },
            panesChange(key){
                this.pane=key,
                console.log(key);
            },
            handleTabChange (key, type) {
                this[type] = key
                console.log("key"+e)
            },

            handleTagClose (removeTag) {
                const tags = this.tags.filter(tag => tag !== removeTag)
                this.tags = tags
                console.log("key"+this.tags)
            },

            showTagInput () {
                this.tagInputVisible = true
                this.$nextTick(() => {
                    this.$refs.tagInput.focus()
                })
            },

            handleInputChange (e) {
                this.tagInputValue = e.target.value
            },

            handleTagInputConfirm () {
                const inputValue = this.tagInputValue
                let tags = this.tags
                if (inputValue && !tags.includes(inputValue)) {
                    tags = [...tags, inputValue]
                }

                Object.assign(this, {
                    tags,
                    tagInputVisible: false,
                    tagInputValue: ''
                })
            },
            // getList () {
            //     this.$http.get('/list/article').then(res => {
            //         console.log('res', res)
            //         this.data = res.result
            //         this.loading = false
            //     })
            // },
            // loadMore () {
            //     this.loadingMore = true
            //     this.$http.get('/list/article').then(res => {
            //         this.data = this.data.concat(res.result)
            //     }).finally(() => {
            //         this.loadingMore = false
            //     })
            // }

        },
        mounted(){
            this.pane='我的申请'
        },

    }
</script>

<style lang='less' scoped>
  .page-header-wrapper-grid-content-main {
    width: 100%;
    height: 100%;
    min-height: 100%;
    transition: 0.3s;

    .account-center-avatarHolder {
      text-align: center;
      margin-bottom: 24px;

      & > .avatar {
        width: 104px;
        height: 104px;
        margin: 0 auto 20px;
        border-radius: 50%;
        overflow: hidden;
        img {
          height: 100%;
          width: 100%;
        }
      }

      .username {
        color: rgba(0, 0, 0, 0.85);
        font-size: 20px;
        line-height: 28px;
        font-weight: 500;
        margin-bottom: 4px;
      }
    }

    .account-center-detail {
      p {
        margin-bottom: 8px;
        padding-left: 26px;
        position: relative;
      }

      i {
        position: absolute;
        height: 14px;
        width: 14px;
        left: 0;
        top: 4px;
        background: url(https://gw.alipayobjects.com/zos/rmsportal/pBjWzVAHnOOtAUvZmZfy.svg);
      }

      .title {
        background-position: 0 0;
      }
      .group {
        background-position: 0 -22px;
      }
      .address {
        background-position: 0 -44px;
      }
    }

    .account-center-tags {
      .ant-tag {
        margin-bottom: 8px;
      }
    }

    .account-center-team {
      .members {
        a {
          display: block;
          margin: 12px 0;
          line-height: 24px;
          height: 24px;
          .member {
            font-size: 14px;
            color: rgba(0, 0, 0, 0.65);
            line-height: 24px;
            max-width: 100px;
            vertical-align: top;
            margin-left: 12px;
            transition: all 0.3s;
            display: inline-block;
          }
          &:hover {
            span {
              color: #1890ff;
            }
          }
        }
      }
    }
    .tagsTitle,
    .teamTitle {
      font-weight: 500;
      color: rgba(0, 0, 0, 0.85);
      margin-bottom: 12px;
    }
  }
  .avatar-upload-wrapper {
    height: 200px;
    width: 100%;
  }

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
