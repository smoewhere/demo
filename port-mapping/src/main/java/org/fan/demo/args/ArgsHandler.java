package org.fan.demo.args;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.8 17:44
 */
public interface ArgsHandler<T,E> {

    E handle(T arg);
}
