<template>

  <div>
    <br>
    <div align="center" margin-top="10px">
      <a-input-search
        placeholder="input search text"
        style="width: 400px"
        slot="extra"
        @search="onSearch"
      />
    </div>
    <br>
  <a-tabs
    align="left"
    type="card"
    @change="panesChange"
    defaultActiveKey="0"
    :tabPosition="mode"
    @prevClick="callback"
    @nextClick="callback">
    <a-tab-pane v-for="pane in panes" :tab="pane.title" :key="pane.key" ></a-tab-pane>
    <a-button slot="tabBarExtraContent" icon="plus" @click="publishClick">我要发布</a-button>
  </a-tabs>

      <a-list :grid="{ gutter: 24, xxl: 4, xl:4 , lg: 4, md: 4, sm: 2, xs: 1 }" :dataSource="data">
        <a-list-item slot="renderItem" slot-scope="item, index">
          <a-card :title="item.title"  @click="handleDetail(item)" >
            <img :src="item.imgUrl"
                 slot="cover"
            />
            <a-card-meta :title="item.address">
              <template slot="description">{{item.description}}</template>
            </a-card-meta>
<!--            <router-link :to="{name: 'ActivityDetail',params:{test:mode}}" active-class="active" >-->
<!--              <a-card-meta-->
<!--                title="活动详情"-->
<!--                description="点击查看活动详细信息">-->
<!--              </a-card-meta>-->
<!--            </router-link>-->
          </a-card>
        </a-list-item>
      </a-list>

<!--发布活动弹出框-->
    <a-modal
      title="发布活动"
      v-model="visible"
      :footer="null"
      width="600px"
    >
      <div class="account-settings-info-view">
        <a-row :gutter="8">
          <a-form :form="form" @submit="handleSubmit" layout="horizontal">
            <a-form-item label="活动图片" >
              <div>
                <el-upload
                  v-decorator="['title', { rules: [{ required: true, message: 'Please select picture' }] }]"
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
              </div>>
            </a-form-item>
            <a-form-item label="活动标题" >
              <a-input
                v-decorator="['title', { rules: [{ required: true, message: 'Please input your title!' }] }]"
              />
            </a-form-item>
            <a-form-item label="活动标签" >
              <a-select mode="multiple"
                        v-decorator="['tag', { rules: [{ required: true, message: 'Please input your tag!' }] }]"
                       style="width: 100%" @change="handleChange" placeholder="Please select">
                <a-select-option v-for="i in 25" :key="(i + 9).toString(36) + i">
                  {{(i + 9).toString(36) + i}}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item  label="活动地点">
              <a-input
                v-decorator="['address', { rules: [{ required: true, message: 'Please input your address!' }] }]"
              />
            </a-form-item>

            <a-form-item label="活动时间">
              <a-range-picker
                v-decorator="['time', { rules: [{ required: true, message: 'Please input your time!' }] }]"
              >
                <template slot="dateRender" slot-scope="current" >
                  <div class="ant-calendar-date" :style="getCurrentStyle(current)">
                      {{current.date()}}
                  </div>
                </template>
              </a-range-picker>
            </a-form-item>
            <a-form-item  label="活动描述">
              <a-textarea rows="2"
                v-decorator="['description', { rules: [{ required: true, message: 'Please input your description!' }] }]"
              />
              </a-form-item>
              <a-form-item  label="联系方式">
                <a-input
                  v-decorator="['phone', { rules: [{ required: true, message: 'Please input your phone!' }] }]"
                />
                </a-form-item>
            <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
              <a-button type="primary" html-type="submit">
                发布
              </a-button>
            </a-form-item>
          </a-form>
        </a-row>
      </div>
    </a-modal>
  </div>
</template>

<script>
    import {genUpToken} from "../api/qiniuToken";

    const data = [
        {
            title: '这里是标题',//活动标题
            imgUrl:"https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png",//活动图片
            description:"这里是描述", //活动描述
            address:"这里是地址"//活动地址
        }
    ];
    //panes是标签页，由后台给出
    const panes=[
        {title:"全部",key:0},
        {title:"推荐",key:1}
    ];
    export default {
        name: "ActivityMain",
        data(){
            return{
                createValue:[],
                form: this.$form.createForm(this, { name: 'publish' }),
                actionPath:'https://upload.qiniup.com', //
                postData:{},
                imageUrllocal:'',
                visible:false,
                data,
                mode:"top",
                panes,
                title:'',//标签名字
                Index:1,//标签的key
                userID:'id1',
                userName:"haijun"
            }},
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
            handleChange(){

            },
            handleDetail(item){
              console.log("活动图片Url"+item.imgUrl)
              this.$store.commit('setImageUrl',item.imgUrl)
              this.$router.push({path: '/ActivityDetail'})
                // this.$router.push({path: '/ActivityDetail',query:{test:this.mode,
                //         userID:this.userID,
                //         userName:this.userName,
                //         imageUrl:item.imgUrl
                //     }})
                // console.log("你点击了查看详情页面")
                // console.log("图片的url"+item.imgUrl)
            },
            handleSubmit(e){
                e.preventDefault();
                this.form.validateFields((err, values) => {
                    if (!err) {
                        console.log(values.tag)
                        console.log('Received values of form: ', values.time);
                        console.log('Received values of form: ', values.time);
                    }
                });
              console.log("你点击了提交按钮")
            },
            getCurrentStyle(current) {
                const style = {};
                if (current.date() === 1) {
                    style.border = '1px solid #1890ff';
                    style.borderRadius = '50%';
                }
                return style;
            },
            imgSaveToUrl(file){  // 也可以用file
                this.localFile=file.raw // 或者 this.localFile=file.raw
                let reader = new FileReader()
                reader.readAsDataURL(this.localFile);// 这里也可以直接写参数event.raw
                let URL = window.URL || window.webkitURL;
                this.imageUrllocal= URL.createObjectURL(file.raw);
            },
            // uploadClick(){
            //     this.$refs.upload.submit();
            //     console.log("上传开始")
            // },
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

            hideModal(){
                this.$refs.upload.submit();
                console.log("上传开始")
                this.visible=false
                console.log("你点击了确定")
            },
            publishClick(){
                this.imageUrllocal=''
                this.visible=true
                console.log("你点击了发布按钮")
            },
            panesChange:function(e){

                const data=this.data;
                const panes=this.panes;
                this.title="学校";
                this.Index++;


                console.log("key value  "+this.Index)
                panes.push({
                    title:this.title,key:this.Index
                })
                // panes.push({
                //     title:"学校",key:this.Index,
                // })
                this.panes=panes;

                console.log("happy"+e);
                this.panes.forEach((pane,i)=>{
                    if (pane.key === e) {
                        console.log("title"+pane.title);
                        data.push({
                            title:pane.title,
                             imgUrl:"https://os.alipayobjects.com/rmsportal/QBnOOoLaAfKPirc.png"
                        })
                    }
                });
            },
            onSearch(val){
                console.log("你搜索了"+val);

            },
            callback:function (val) {
                console.log(val);
            }
        }

    }
</script>

<style scoped>
  .avatar-uploader .el-upload {
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    border-style: outset;
    border-color: #52c41a;
    border-width: 50px;
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
