package org.fan.demo.spel;

import java.time.LocalDateTime;
import org.fan.demo.spel.model.ListDemo;
import org.fan.demo.spel.model.Person;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

/**
 * https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions
 * 
 * @version 1.0
 * @author: Fan
 * @date 2021.1.25 18:26
 */
public class SpelMain {

  private static final ExpressionParser PARSER = new SpelExpressionParser();

  public static void main(String[] args) {
    SpelMain spelMain = new SpelMain();
    spelMain.parseNormalConfig();
  }

  private void parseSimple() {
    Expression exp = PARSER.parseExpression("'Hello World'.bytes.length");
    Integer bytes = (Integer) exp.getValue();
    System.out.println(bytes);
  }

  private void parseNormal() {
    LocalDateTime birthday = LocalDateTime.of(1896, 5, 12, 12, 23, 45, 112);
    Person jack = new Person("jack", birthday, 2021 - 1896);
    Expression exp = PARSER.parseExpression("name");
    String value = exp.getValue(jack, String.class);
    System.out.println(value);
    exp = PARSER.parseExpression("name == 'jack'");
    Boolean result = exp.getValue(jack, Boolean.class);
    System.out.println(result);
  }

  /**
   * 配置后，自动null值，collection自动增长
   */
  private void parseNormalConfig(){
    SpelParserConfiguration config = new SpelParserConfiguration(true,true);
    ExpressionParser parser = new SpelExpressionParser(config);
    ListDemo listDemo = new ListDemo();
    EvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
    parser.parseExpression("booleanList[0]").setValue(context, listDemo, "false");
    System.out.println(listDemo);
    Expression expression = parser.parseExpression("stringList[3]");
    expression.getValue(listDemo);
    System.out.println(listDemo);
  }

}
