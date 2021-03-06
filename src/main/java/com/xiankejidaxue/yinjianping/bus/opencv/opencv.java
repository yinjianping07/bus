package com.xiankejidaxue.yinjianping.bus.opencv;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author:yinjianping
 * @Description: opencv操作
 * @Date:Created in 14:25 2019/5/11
 * @Modified By:
 */
@Component
@Log4j2
public class OpenCV {

    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    @Value("classpath:haarcascade_frontalface_alt.xml")
    private Resource xml;

    public Integer faceNum(){
        try{
            //创建临时拷贝文件，因为spring boot项目打包后，无法读取到文件内容。
            File targetXmlFile = new File("src"+xml.getFilename()+"");
            FileUtils.copyInputStreamToFile(xml.getInputStream(),targetXmlFile);

            CascadeClassifier faceDetector = new CascadeClassifier(targetXmlFile.toString());
            if(faceDetector.empty()){
                log.info("模型xml文件创建CascadeClassifier失败");
            }
            VideoCapture videoCapture = new VideoCapture(0);
            if (videoCapture.isOpened()){
                log.info("摄像头已开启！");
            }
            Mat image = new Mat();
            videoCapture.read(image);
            Imgproc.cvtColor(image, new Mat(), Imgproc.COLOR_RGB2GRAY);
            //在图片中检测人脸
            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(image,faceDetections);
            //toArray操作
            Rect[] rects = faceDetections.toArray();
            return rects.length;
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
        return 0;
    }
}
