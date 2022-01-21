package com.limu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.limu.oss.service.OssService;
import com.limu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传头像到oss
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ConstantPropertiesUtils.END_POIND;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        OSS ossClient = null;
        try {
            // 创建OSSClient实例。
             ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 填写本地文件的完整路径。
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String fileName = file.getOriginalFilename();

            //1.在文件名称里面添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replace("-","");

            fileName = uuid+fileName;

            //2.把文件按照日期进行分类
            // yyyy/MM/dd/fileName
            //获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            fileName = datePath + "/" + fileName;

            // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName, fileName, inputStream);

            //把上传之后文件文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //https://edu-limu-1.oss-cn-shanghai.aliyuncs.com/01.jpeg
            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;

            return url;

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(ossClient != null){
                // 关闭OSSClient。
                ossClient.shutdown();
            }
        }
        return null;
    }
}
