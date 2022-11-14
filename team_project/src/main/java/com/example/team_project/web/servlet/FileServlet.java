package com.example.team_project.web.servlet;

import com.example.team_project.framkwork.core.annotation.ContentType;
import com.example.team_project.framkwork.core.mvc.BeanFactory;
import com.example.team_project.framkwork.pojo.vo.ApiMsg;
import com.example.team_project.framkwork.utils.MapUtils;
import com.example.team_project.pojo.User;
import com.example.team_project.service.IslandVoiceService;
import com.example.team_project.utils.ServiceConfUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@MultipartConfig
@WebServlet("/file/*")
public class FileServlet extends BaseServlet {
    private IslandVoiceService voiceService = BeanFactory.getBean("islandVoiceService", IslandVoiceService.class);

    @ContentType
    public void uploadVoice(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        String host = ServiceConfUtils.getConfig("HOST");
        ApiMsg msg;
        if (user != null) {
            Collection<Part> parts = request.getParts();
            String path = "/file-upload/voice";
            String realPath = request.getServletContext().getRealPath(path);
            int fileCount = parts.size();
            //若上传文件数量不小于等于0，则可以上传
            if (fileCount > 0) {
                //上传的文件的相对路径
                List<String> pathList = voiceUpload(parts, realPath, path,user.getId());
                LOGGER.info("文件上传完成，共有:" + fileCount + "\t完成了:" + pathList.size());
                List<String> realPathList = new ArrayList<>(pathList.size());
                for (String filePath : pathList) {
                    realPathList.add(host + request.getContextPath() + filePath);
                }
                msg = new ApiMsg(200, "success");
                //把上传后的路径返回
                msg.setData(MapUtils.getMap("path",realPathList.get(0)));
            } else {
                LOGGER.info("上传了0个文件...");
                msg = new ApiMsg(200, "fail");
            }
        }else {
            msg = ApiMsg.error();
        }
        mapper.writeValue(response.getOutputStream(), msg);
    }


    /**
     * 上传文件
     * @param files part文件的集合
     * @param pathLackFileName 缺少文件名字的残缺的文件夹名
     * @return 上传成功的条数
     */
    private int upload(Collection<Part> files, String pathLackFileName) throws IOException {
        int count = 0;
        StringBuilder path = new StringBuilder(pathLackFileName);
        for (Part file : files) {
            long size = file.getSize();
            LOGGER.debug("文件的大小为:" + size + "bytes");
            if (size > 0) {
                //获取文件名字并且保存下来
                String fileName = file.getSubmittedFileName();
                //拼接保存路径
                path.append(File.separator).append(fileName);
                String real = path.toString();
                //保证存在父文件夹，才能够保存文件
                createFatherDir(new File(real));
                file.write(real);
                count++;
                path.delete(pathLackFileName.length(),path.length());
            }else {
                LOGGER.debug("文件大小小于等于0bytes，无法保存...");
            }
        }
        return count;
    }

    private List<String> voiceUpload(Collection<Part> files, String pathLackFileName, String savePath, long belong) throws IOException {
        StringBuilder path = new StringBuilder(pathLackFileName);
        String host = ServiceConfUtils.getConfig("HOST");
        List<String> pathList = new ArrayList<>();
        for (Part file : files) {
            long size = file.getSize();
            LOGGER.debug("文件的大小为:" + size + "bytes");
            if (size > 0) {
                //获取文件名字并且保存下来
                String fileName = UUID.randomUUID().toString().replaceAll("-","") + ".mav";
                //拼接保存路径
                path.append(File.separator).append(fileName);
                String real = path.toString();
                //保证存在父文件夹，才能够保存文件
                createFatherDir(new File(real));
                file.write(real);
                String save = savePath + "/" + fileName;
                //保存到数据库
                int upload = voiceService.upload(save, belong);
                LOGGER.info("音频成功上传"+upload+"个");
                pathList.add(save);
                path.delete(pathLackFileName.length(),path.length());
            }else {
                LOGGER.debug("文件大小小于等于0bytes，无法保存...");
            }
        }
        return pathList;
    }

    /**
     * 创建文件夹
     * @param file 要创建的文件夹
     */
    private void createFatherDir(File file) {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            //递归直到找到存在的父文件夹,一直创建到可以创建文件/文件夹
            createFatherDir(parentFile);
            boolean result = parentFile.mkdirs();
            if (result) {
                LOGGER.debug("创建文件夹 \""+parentFile.getName()+"\" 成功");
            }else {
                LOGGER.debug("创建文件夹 \"" + parentFile.getName() +"\"失败");
            }
        }
    }

}
