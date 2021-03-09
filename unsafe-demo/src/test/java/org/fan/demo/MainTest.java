package org.fan.demo;

import java.lang.reflect.Field;
import org.fan.demo.entity.Cat;
import org.fan.demo.entity.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/8 19:05
 */
public class MainTest {

  private static Unsafe unsafe = null;

  @BeforeAll
  static void init() {
    Class<Unsafe> unsafeClass = Unsafe.class;
    Unsafe o = null;
    try {
      Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
      unsafeField.setAccessible(true);
      o = (Unsafe) unsafeField.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    unsafe = o;
  }

  @Test
  public void test() {
    Cat cat = new Cat().setAge(12).setAgePri(12).setName("cat").setSex("male");
    Class<Cat> catClass = Cat.class;
    try {
      Field nameField = catClass.getDeclaredField("name");
      Field ageField = catClass.getDeclaredField("age");
      Field agePriField = catClass.getDeclaredField("agePri");
      long nameOffset = unsafe.objectFieldOffset(nameField);
      long ageFieldOffset = unsafe.objectFieldOffset(ageField);
      long agePriFieldOffset = unsafe.objectFieldOffset(agePriField);
      System.out.println("name is " + cat.getName());
      unsafe.putObject(cat, nameOffset, "new name");
      System.out.println("new name is " + cat.getName());
      int anInt = unsafe.getInt(cat, agePriFieldOffset);
      System.out.println("unsafe get int is " + anInt);
      // 使用错误的类型获取，直接fatal error， jvm停止运行
      // Object object = unsafe.getObject(cat, agePriFieldOffset);
      // System.out.println("unsafe get int by object " + object);
      int ageFieldOffsetInt = unsafe.getInt(cat, ageFieldOffset);
      System.out.println("unsafe get Integer by getint: " + ageFieldOffsetInt);
      Object ageFieldOffsetInteger = unsafe.getObject(cat, ageFieldOffset);
      System.out.println("unsafe get Integer by getObject: " + ageFieldOffsetInteger);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testUnsafe() {
    try {
      Person instance = (Person)unsafe.allocateInstance(Person.class);
      instance.setAddress("name");
      System.out.println(instance);
    } catch (InstantiationException e) {
      e.printStackTrace();
    }
  }

}
