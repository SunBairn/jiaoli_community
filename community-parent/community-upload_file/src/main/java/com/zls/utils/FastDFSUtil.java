package com.zls.utils;

import com.zls.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * 主要封装一些文件上传的API
 */
public class FastDFSUtil {
    /**
     * 加载tracker的连接信息
     */
    static{
        String configFilename = new ClassPathResource("fdfs_client_conf").getPath();
        // 加载tracker的连接信息
        try {
            ClientGlobal.init(configFilename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取trackerClient
     * @return
     */
    private static TrackerClient getTrackerClient(){
        return new TrackerClient();
    }

    /**
     * 获取TrackerServer
     * @return
     * @throws IOException
     */
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = getTrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    /**
     * 获取StorageClient
     * @param
     * @return
     */
    private static StorageClient getStorageClient() throws IOException {
        StorageClient storageClient = new StorageClient(FastDFSUtil.getTrackerServer(), null);
        return storageClient;
    }

    /**
     * 文件上传
     * @param fastDFSFile 上传文件的信息封装
     *
     *
     * uploadFile:
     *                    uploadFile[0]: 文件组的名字
     *                    uploadFile[1]: 文件存储到storage的名字(也叫文件ID)：M00/00/00/wKibgV50b1mAfxf0AABBHg-Uixs939.jpg
     */
    public static String[] upload(FastDFSFile fastDFSFile) throws IOException, MyException {
        // 文件的一些附加参数
        NameValuePair[] meta_list = new NameValuePair[2];
        meta_list[0] = new NameValuePair("author", fastDFSFile.getAuthor());
        meta_list[1] = new NameValuePair("time", fastDFSFile.getTime()+"");
        String[] uploadFile = getStorageClient().upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), meta_list);
        return uploadFile;
    }

    /**
     * 文件下载
     * @param groupName  文件所在的组名
     * @param remoteFileName  文件的存储路径
     * @return
     * @throws Exception
     */
    public static InputStream downFile(String groupName, String remoteFileName) throws Exception {
        byte[] bytes = getStorageClient().download_file(groupName, remoteFileName);
        return new ByteArrayInputStream(bytes);
    }


    /**
     * 删除文件
     * @param groupName 文件所在的组名
     * @param remoteFileName 文件的存储路径
     * @return
     * @throws Exception
     */
    public static int deleteFile(String groupName, String remoteFileName) throws Exception {
        int i = getStorageClient().delete_file(groupName, remoteFileName);
        return i;
    }

    /**
     * 获取文件信息
     * @param groupName   文件组名
     * @param remoteFileName  文件的存储路径 例如：M00/00/00/wKibgV50b1mAfxf0AABBHg-Uixs939.jpg这种格式
     * @return
     */
    public static FileInfo getFile(String groupName,String remoteFileName) throws Exception {
        FileInfo file_info = getStorageClient().get_file_info(groupName, remoteFileName);
        return file_info;
    }

    /**
     * 根据组名获取storageServer组信息
     * @param groupName
     * @return
     */
    public static StorageServer getStorage(String groupName) throws IOException {
        TrackerClient trackerClient = getTrackerClient();
        TrackerServer trackerServer = getTrackerServer();
        StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer,groupName);
        return storeStorage;
    }


    /**
     * 根据文件组名和文件存储路径获取storage服务的IP、端口信息
     * @param groupName
     * @param remoteFileName
     */
    public static ServerInfo[] getServerInfo(String groupName,String remoteFileName) throws IOException {
        ServerInfo[] serverInfos = getTrackerClient().getFetchStorages(getTrackerServer(), groupName, remoteFileName);
        return serverInfos;
    }


    /**
     * 获取 tracker 服务地址
     * @return
     */
    public static String getTrackerUrl() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        // 获取IP地址
        String ip = trackerServer.getInetSocketAddress().getHostString();
        // 获取端口
        int port = ClientGlobal.getG_tracker_http_port();
        String url =  "http://"+ip+":"+port;
        return url;
    }


    public static void main(String[] args) throws IOException {
//        InputStream is=null;
//        FileOutputStream os=null;
//
//        try {
//             is = FastDFSUtil.downFile("group1", "M00/00/00/wKibgV50s5KACdqQAABdsIirH3Y064.jpg");
//             os = new FileOutputStream("D:/1.jpg");
//            byte[] bytes = new byte[1024];
//            while (is.read(bytes)!= -1){
//                os.write(bytes);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            os.flush();
//            os.close();
//            is.close();
//        }

        //删除文件
//        try {
//            FastDFSUtil.deleteFile("group1", "M00/00/00/wKibgV50wF6AbZWNAAAVUPAyH9w888.jpg");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String trackerUrl = FastDFSUtil.getTrackerUrl();
        System.out.println(trackerUrl);
    }
}
