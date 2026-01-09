package com.gorillamusic.controller;

import com.gorillamusic.entity.config.AppConfig;
import com.gorillamusic.entity.constants.Constants;
import com.gorillamusic.utils.StringTools;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.reflection.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

import java.io.*;
import java.util.Date;

/**
 * Date：2026/1/8  17:33
 * Description：TODO
 *
 * @author Leon
 * @version 1.0
 */


@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private AppConfig appConfig;

    @RequestMapping("/getResource")
    public void getResource(HttpServletResponse response, @NotEmpty String filePath) {

        //路径合法性
        if (!StringTools.pathIsOk(filePath)) {
            return;
        }
        filePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + filePath;
        String suffix = StringTools.getFileSuffix(filePath);
        //缓存
        if (!StringTools.isEmpty(suffix) && ArrayUtils.contains(Constants.IMAGES_SUFFIX, suffix.toLowerCase())) {
            response.setHeader("Cache-Control", "max-age=2592000");
            response.setContentType("image/png");
        }
//        readFile(response, filePath);

        //分片读取
        readFile(response, response.getHeader("Range"), filePath);
    }

    //    private void readFile(HttpServletResponse response, String filePath) {
//        File file = new File(filePath);
//        try (OutputStream os = response.getOutputStream(); InputStream is = new FileInputStream(file)) {
//            int len;
//            byte[] buffer = new byte[1024];
//            while ((len = is.read(buffer)) != -1) {
//                os.write(buffer, 0, len);
//            }
//            os.flush();
//        } catch (Exception e) {
//            log.error("读取文件异常", e);
//        }
//    }
    protected void readFile(HttpServletResponse response,
                            @RequestHeader(name = "range", required = false) String rangeHeader,
                            @NotEmpty String filePath) {
        File file = new File(filePath);

        // 验证文件存在性和可读性
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try (RandomAccessFile randomFile = new RandomAccessFile(file, "r"); ServletOutputStream out = response.getOutputStream()) {
            long fileSize = randomFile.length();
            long start = 0;          // 读取的起始位置（默认第0个字节，就是文件开头）
            long end = fileSize - 1; // 读取的结束位置（默认最后一个字节）


            // 解析范围请求头 若有range,则更新start和end
            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                try {
                    String rangeValue = rangeHeader.substring(6); // 把"bytes=100-200"变成"100-200"
                    String[] ranges = rangeValue.split("-");      // 把"100-200"拆成["100", "200"]

                    start = Long.parseLong(ranges[0]); // 起始位置改成100
                    if (ranges.length > 1 && !ranges[1].isEmpty()) {
                        end = Long.parseLong(ranges[1]); // 结束位置改成200
                    }

                    // 检查要求的范围是不是合理（比如不能要求读“第500字节到第100字节”）
                    if (start < 0 || end >= fileSize || start > end) {
                        response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                        response.setHeader("Content-Range", "bytes */" + fileSize);
                        return; // 范围不合理，直接结束
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }

            long contentLength = end - start + 1;
            // 设置响应头
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Last-Modified", new Date(file.lastModified()).toString());
            // 处理范围请求
            if (rangeHeader != null) {
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);
            }
            response.setHeader("Content-Length", String.valueOf(contentLength));
            byte[] buffer = new byte[1024 * 1024];
            randomFile.seek(start);

            long remaining = contentLength;
            while (remaining > 0) {
                int read = randomFile.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (read == -1) {
                    break; // 文件结束
                }

                out.write(buffer, 0, read);
                remaining -= read;

                // 刷新输出流，确保数据及时发送
                if (remaining <= 0 || read < buffer.length) {
                    out.flush();
                }
            }

        } catch (AsyncRequestNotUsableException e) {
        } catch (Exception e) {
            // 记录错误日志
            log.error("文件读取失败: " + filePath, e);
            // 如果响应还未提交，设置错误状态
            if (!response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }


}











