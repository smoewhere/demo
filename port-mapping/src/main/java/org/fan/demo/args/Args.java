package org.fan.demo.args;

import org.fan.demo.args.handler.HelpHandler;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.8 17:41
 */
public enum Args{

    /**
     * 打印Help信息
     */
    HELP(new HelpHandler());

    private ArgsHandler argsHandler;

    Args(ArgsHandler argsHandler) {
        this.argsHandler = argsHandler;
    }

    public static Args getHandler(String arg){
        if ("-h".equals(arg) || "-help".equals(arg)) {
            return HELP;
        } else if ("-start".equals(arg)){

        }
        return HELP;
    }

    public Object handle(String arg){
       return this.argsHandler.handle(arg);
    }
}
