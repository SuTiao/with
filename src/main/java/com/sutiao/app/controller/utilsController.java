package com.sutiao.app.controller;

import com.sutiao.app.constant.UserConstant;
import com.sutiao.app.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/utils")
public class utilsController {
    private static boolean using = false;
    private static long NOT_AUTHORIZED = -1;
    private static long REALITY_CAPTURE_IS_USING = -1;
    private static String uploadPath = "C:\\";
    private boolean authorized(HttpServletRequest request){
        Object s = request.getSession().getAttribute(UserConstant.User_LOGIN_STATE);
        User user = (User) s;
        if(user == null || user.getRole() != UserConstant.ADMINFLAG){
            return false;
        }
        return true;
    }

    @PostMapping("/realitycapture")
    public long callRealityCapture(HttpServletRequest request, HttpServletResponse response, @RequestParam("files") MultipartFile[] files) throws IOException, InterruptedException {
        if(!authorized(request)){
            return NOT_AUTHORIZED;
        }
        if(using){
            return REALITY_CAPTURE_IS_USING;
        }
        using = true;
        for(MultipartFile file : files){
            String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            File saveAddress = new File(uploadPath);
            if (!saveAddress.exists()) {
                saveAddress.mkdirs();// 如果文件夹不存在 创建保存文件对应的文件夹
            }
            // 将上传的文件保存到指定路径
            file.transferTo(new File(uploadPath + fileName));
        }
        String batPath = "C:\\Program Files\\Capturing Reality\\RealityCapture\\a.bat";
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(batPath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null){
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("脚本执行完毕，退出码: " + exitCode);
        UUID uuid = UUID.randomUUID();
        String filePath = "D:/" + uuid + ".fbx";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".obj");
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            fileInputStream.close();
            outputStream.close();
        }
        catch (Exception ignored){}
        using = false;
        return Long.parseLong(String.valueOf(uuid));
    }



}
