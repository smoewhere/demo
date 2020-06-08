package org.fan.demo;

import io.netty.bootstrap.ServerBootstrap;
import org.fan.demo.args.Args;
import org.fan.demo.bootstrap.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 服务启动类，监听服务端口，转发到客户端。
 *
 * @author Fan
 * @version 1.0
 * @date 2020.6.8 17:00
 */
public class MainServer {

    public static void main(String[] args) {

        Map<String, String> map = checkArgs(args);
        String bootstrap = map.get("bootstrap");
        if ("server".equals(bootstrap)) {
            new Server();
        }
    }


    private static Map<String, String> checkArgs(String[] args) {
        Map<String, String> commandMap = new HashMap(4);
        int length = args.length;
        LinkedList<String> list = new LinkedList(Arrays.asList(args).subList(0, length));
        if (list.isEmpty()) {
            // 默认以服务端的方式启动
            commandMap.put("bootstrap", "server");
            return commandMap;
        }
        while (!list.isEmpty()) {
            String s = list.pop();
            if ("-h".equals(s) || "-help".equals(s)) {
                printHelp();
                break;
            }
            if ("-start".equals(s)) {
                String value = list.pop();
                if (!"server".equals(value) && !"client".equals(value)) {
                    System.out.println("不能识别的参数 " + value);
                    continue;
                } else {
                    commandMap.put("bootstrap", value);
                }
            }
            // TODO 其余参数添加，暂时未完成
        }
        return commandMap;
    }

    public static void printHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("用法: MainServer [options] [arg]\n\n").append("其中，options选项为：\n\n")
                .append("\t-h\n").append("\t-help:输出此信息。\n\n")
                .append("\t-start: [server|client] 指定以服务端或者客户端的方式启动\n");
        System.out.println(sb.toString());
    }
}
