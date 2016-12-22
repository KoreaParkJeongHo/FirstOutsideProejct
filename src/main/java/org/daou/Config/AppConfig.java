package org.daou.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by user on 2016-12-07.
 */
@Configuration
public class AppConfig {

    /**
     * Jsp파일을 인식할 수 있도록 ViewREsolve를 세팅한다.
     *
     * @return the view resolver
     */
    @Bean
    public ViewResolver getViewResolver() {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;

    }

}
