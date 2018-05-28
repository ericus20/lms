package com.developersboard.lms.proxy.amazon;

import feign.Headers;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by Eric on 5/19/2018.
 *
 * @author Eric Opoku
 */
@FeignClient(name = "amazon-s3-service")
@RibbonClient(name = "amazon-s3-service")
public interface AmazonS3ServiceProxy {

    /**
     * It stores the given file name in S3 and returns the key under which the file has been stored
     *
     * @param uploadedFile The multipart file uploaded by the user
     * @param username     The username for which to upload this file
     * @return The URL of the uploaded image
     */


    @PostMapping(path = "/amazon-s3-service/storeProfileImage/{username}")
    String storeProfileImage(@RequestParam(name = "file") MultipartFile uploadedFile,
                             @PathVariable(name = "username") String username);

    /**
     * It stores the given file name in S3 and returns the key under which the file has been stored
     *
     * @param multipartFile The multipart file uploaded by the user
     * @param path The folder within which this file will be placed
     * @param fileName The file name eg. fileName.png
     * @return The URL of the uploaded image
     * @throws IOException If something goes wrong.
     */
    @Headers("Content-Type: multipart/mixed; boundary=gc0p4Jq0M2Yt08jU534c0p")
    @PostMapping(path = "/amazon-s3-service/storeGenericImage/{path}/{fileName}", consumes = {"multipart/form-data"})
    String storeGenericImage(@RequestParam(name = "file") MultipartFile multipartFile,
                             @PathVariable(name = "path") String path,
                             @PathVariable(name = "fileName") String fileName) throws IOException;

    /**
     * Deletes the folder hosted on amazon s3 assigned to user.
     * @param key the key of the user as path to image file
     * @return  if delete was a success or not
     * @throws Exception An exception will arise if an error occurs
     */
    @DeleteMapping(path = "/amazon-s3-service/delete")
    boolean deleteProfileImageFromS3(@RequestParam(name = "key") String key) throws Exception;


}
