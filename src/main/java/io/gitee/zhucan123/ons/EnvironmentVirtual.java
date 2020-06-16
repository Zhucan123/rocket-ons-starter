package io.gitee.zhucan123.ons;

import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

/**
 * @author: zhuCan
 * @date: 2020/3/19 10:41
 * @description:
 */
public class EnvironmentVirtual {

  @Value("${groupSuffix:}")
  private String env;

  public EnvironmentEnum environmentEnum(){
    return EnvironmentEnum.environment(env);
  }

  public String getEnv(){
    return this.env;
  }


  public enum EnvironmentEnum{
    DEV("dev"),
    FAT("fat"),
    LOCAL("local"),
    PRO("pro");

    private final String env;

    EnvironmentEnum(String env){
      this.env=env;
    }

    public String getEnv(){
      return this.env;
    }

    public static EnvironmentEnum environment(String envString){
     return Arrays.stream(EnvironmentEnum.values())
         .filter(x->x.env.equals(envString))
         .findFirst()
         .orElse(DEV);
    }
  }
}
