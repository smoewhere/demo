package org.fan.demo.args.handler;

import org.fan.demo.args.ArgsHandler;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.8 18:26
 */
public class HelpHandler<T,E> implements ArgsHandler<T,E> {

    @Override
    public E handle(T arg) {
        StringBuilder sb = new StringBuilder();
        sb.append("用法: MainServer [options] [arg]\n\n").append("其中，options选项为：\n\n")
                .append("\t-h\n").append("\t-help:输出此信息。\n\n")
                .append("\t-start: [server|client] 指定以服务端或者客户端的方式启动\n");
        System.out.println(sb.toString());
        return null;
    }
}
