package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadFileStreamRequest;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.vod.service.MediaService;
import com.atguigu.guli.service.vod.util.AliyunVodSDKUtils;
import com.atguigu.guli.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class MediaServiceImpl implements MediaService {

    @Autowired
    private VodProperties vodProperties;

    //上传视频到vod
    @Override
    public String upload(MultipartFile file) {
        String videoId;

        try {
            //获取要上传的文件的文件名和输入流
            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();

            //根据输入流等参数创建上传请求
            UploadStreamRequest request = new UploadStreamRequest(vodProperties.getKeyId(), vodProperties.getKeySecret(), title, originalFilename, inputStream);
            //为上传请求设置模板组id
//        request.setTemplateGroupId(vodProperties.getTemplateGroupId());
            //为上传请求设置工作流id
//            request.setWorkflowId(vodProperties.getWorkflowId());

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            if (response.isSuccess()) {
                videoId = response.getVideoId();
            } else {
                log.error(response.getMessage());
                throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
            }
            return videoId;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    //根据视频id 获取vod 播放地址
    @Override
    public String getPlayUrl(String id) {
        //初始化 vod 客户端
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyId(), vodProperties.getKeySecret());
        //初始化获取播放地址的请求
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(id);
        try {
            GetPlayInfoResponse response = client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            String playURL = playInfoList.get(0).getPlayURL();
            return playURL;
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.FETCH_PLAYURL_ERROR);
        }
    }

    //根据视频id 获取vod 播放凭证
    @Override
    public String getPlayAuth(String id) {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyId(), vodProperties.getKeySecret());
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(id);
        try {
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            //播放凭证
            String playAuth = response.getPlayAuth();
            return playAuth;
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.FETCH_PLAYAUTH_ERROR);
        }
    }

    //根据视频id 从 vod 删除视频
    @Override
    public void delete(String videoSourceId) {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyId(), vodProperties.getKeySecret());
        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoSourceId);
        try {
            client.getAcsResponse(request);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }

    }

    //根据视频id 集合 从 vod 删除视频
    @Override
    public void removeVideoByList(List<String> videoSourceIdList) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyId(), vodProperties.getKeySecret());

        StringBuffer idListStr = new StringBuffer();
        int size =  videoSourceIdList.size();
        DeleteVideoRequest request = new DeleteVideoRequest();
        //由videoIdList 判断并拼接视频id 字符串 idListStr
        for (int i = 0; i < size; i++) {
            idListStr.append(videoSourceIdList.get(i));

            if (i == size - 1 || i % 20 == 19) {
                request.setVideoIds(idListStr.toString());
                client.getAcsResponse(request);
            } else if (i % 20 <19) {
                idListStr.append(",");
            }
        }

    }
}
