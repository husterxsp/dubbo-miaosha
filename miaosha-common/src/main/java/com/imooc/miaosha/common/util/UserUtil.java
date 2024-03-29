package com.imooc.miaosha.common.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imooc.miaosha.common.domain.miaosha.MiaoshaUser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserUtil {

    int cnt = 1000;
    List<MiaoshaUser> users = new ArrayList<>(cnt);
//
//    public static void main(String[] args) throws Exception {
//        UserUtil userUtil = new UserUtil();
//        userUtil.createUser();
//    }

    public void createUser() throws Exception {
        System.out.println("create user");

        initList();

        insertDb();

        createToken();

        System.out.println("over");
    }

    public void initList() {

        //生成用户
        for (int i = 0; i < cnt; i++) {
            MiaoshaUser user = new MiaoshaUser();
            user.setId(13000000000L + i);
            user.setLoginCount(1);
            user.setNickname("user" + i);
            user.setRegisterDate(new Date());
            user.setSalt("1a2b3c");
            user.setPassword(MD5Util.inputPass2DbPass("123456", user.getSalt()));
            users.add(user);
        }
    }

    public void insertDb() throws Exception {
        //插入数据库
        Connection conn = DBUtil.getConn();
        String sql = "insert into miaosha_user(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            MiaoshaUser user = users.get(i);
            pstmt.setInt(1, user.getLoginCount());
            pstmt.setString(2, user.getNickname());
            pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            pstmt.setString(4, user.getSalt());
            pstmt.setString(5, user.getPassword());
            pstmt.setLong(6, user.getId());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        System.out.println("insert to db");
    }

    public void createToken() throws IOException {
        //登录，生成token
        String urlString = "http://localhost:8080/create_token";
        File file = new File("/Users/xushaopeng/Desktop/tokens.txt");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);

        for (int i = 0; i < users.size(); i++) {
            MiaoshaUser user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPass2FormPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("data");
            System.out.println("create token : " + user.getId());

            String row = user.getId() + "," + token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();
    }
}
