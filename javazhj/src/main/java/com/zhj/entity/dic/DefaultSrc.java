package com.zhj.entity.dic;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 789
 */
@Component
public class DefaultSrc{
    private static String photoSuffix;
    private static String userPhotoSuffix;
    private static String groupPhotoSuffix;
    private static String musicSuffix;
    private static String rootUserFile;
    private static String rootGroupFile;
    private static String serveAddress;
    private static String serveLocation;
    private static String rootMusicFile;
    private static String rootFilePath;
    private static String rootUrlPath;
    private static String kafkaPath;

    @Value("${default.photoSrc}")
    public void setPhotoSuffix(String photoSuffixSrc){
        photoSuffix = photoSuffixSrc;
    }

    @Value("${default.userPhoto}")
    public void setUserPhotoSuffix(String photoSuffixSrc){
        userPhotoSuffix = photoSuffixSrc;
    }

    @Value("${default.groupPhoto}")
    public void setGroupPhotoSuffix(String photoSuffixSrc){
        groupPhotoSuffix = photoSuffixSrc;
    }

    @Value("${default.musicSrc}")
    public void setMusicSrc(String musicSrc){
        musicSuffix = musicSrc;
    }

    @Value("${default.rootUserFile}")
    public void setRootUserFile(String photoSuffixSrc){
        rootUserFile = photoSuffixSrc;
    }

    @Value("${default.rootGroupFile}")
    public void setRootGroupFile(String photoSuffixSrc){
        rootGroupFile = photoSuffixSrc;
    }

    @Value("${default.serveAddress}")
    public void setServeAddress(String photoSuffixSrc){
        serveAddress = photoSuffixSrc;
    }

    @Value("${default.serveLocation}")
    public void setServeLocation(String location){
        serveLocation = location;
    }

    @Value("${default.rootMusicFile}")
    public void setRootMusicFile(String location){
        rootMusicFile = location;
    }

    @Value("${default.rootFilePath}")
    public  void setRootFilePath(String location) {
        rootFilePath = location;
    }

    @Value("${default.rootUrlPath}")
    public  void setRootUrlPath(String location) {
        rootUrlPath = location;
    }

    @Value("${default.kafkaPath}")
    public  void setKafkaPath(String location) {
        kafkaPath = location;
    }

    public static String getPhotoSuffix() {
        return photoSuffix;
    }

    public static String getUserPhotoSuffix() {
        return userPhotoSuffix;
    }

    public static String getGroupPhotoSuffix() {
        return groupPhotoSuffix;
    }

    public static String getMusicSuffix() {
        return musicSuffix;
    }

    public static String getRootUserFile() {
        return rootUserFile;
    }

    public static String getRootGroupFile() {
        return rootGroupFile;
    }

    public static String getServeAddress() {
        return serveAddress;
    }

    public static String getServeLocation() {
        return serveLocation;
    }

    public static String getRootMusicFile() {
        return rootMusicFile;
    }

    public static String getRootFilePath() {
        return rootFilePath;
    }

    public static String getRootUrlPath() {
        return rootUrlPath;
    }

    public static String getKafkaPath() {
        return kafkaPath;
    }
}
