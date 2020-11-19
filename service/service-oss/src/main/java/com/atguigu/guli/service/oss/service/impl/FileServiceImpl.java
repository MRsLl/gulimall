package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;

    String protocol;
    String endpoint;
    String accessKeyId;
    String accessKeySecret;
    String bucketName;

    @PostConstruct // 标注的方法会在类的构造器调用后执行一次
    public void init() {
        this.protocol = ossProperties.getProtocol();
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        this.endpoint = ossProperties.getEndPoint();
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        this.accessKeyId = ossProperties.getKeyId();
        this.accessKeySecret = ossProperties.getKeySecret();
        this.bucketName = ossProperties.getBucketName();
    }

    @Override
    public String fileUpload(String module, MultipartFile file) throws IOException {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(protocol+ endpoint, accessKeyId, accessKeySecret);

        //上传的文件原名
        String fileName = file.getOriginalFilename();
        //唯一的文件名
        String objectName = module +"/" + UUID.randomUUID().toString().replace("-","") + fileName.substring(fileName.lastIndexOf("."));
        //拼接文件地址
        //https://gulimall-edu-file.oss-cn-shanghai.aliyuncs.com/avatar/timg.jpg
        String fileUrl = protocol + bucketName + "." + endpoint + "/" + objectName;

        //从上传的文件获取输入流
        InputStream stream = file.getInputStream();

        // 上传文件流。
        ossClient.putObject(bucketName, objectName, stream);

        // 关闭OSSClient。
        ossClient.shutdown();

        return fileUrl;
    }

    @Override
    public void removeFile(String url) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(protocol+ endpoint, accessKeyId, accessKeySecret);

        //http://gulimall-edu-file.oss-cn-shanghai.aliyuncs.com/avatar/default.jpg
        String host = protocol + bucketName + "." + endpoint + "/" ;
        String objectName = url.substring(host.length());

        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();

    }

    @Override
    public void batchRemove(List<String> urls) {
// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(protocol + endpoint, accessKeyId, accessKeySecret);

// 删除文件。key等同于ObjectName，表示删除OSS文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        List<String> keys = new ArrayList<String>();

        String host = protocol + bucketName + "." + endpoint + "/" ;

        for (String url : urls) {
            String objectName = url.substring(host.length());
            keys.add(objectName);
        }

        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));

// 关闭OSSClient。
        ossClient.shutdown();
    }
}
