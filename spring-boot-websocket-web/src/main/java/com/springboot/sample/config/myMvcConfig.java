/**
 * FileName: myMvcConfig
 * Author:   huang.yj
 * Date:     2019/9/27 14:52
 * Description: 页面转向配置类
 */
package com.springboot.sample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 〈页面转向配置类〉
 *
 * @author huang.yj
 * @create 2019/9/27
 * @since 1.0.0
 */
@Configuration
public class myMvcConfig implements WebMvcConfigurer {
    /**
     * 无逻辑的页面跳转
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
         registry.addViewController("/client").setViewName("client");
    }
}