package org.kerwin612.slbk.enable;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class EnableWebServlet {

    @Bean
    EnableServletFilter servletFilter() {
        return new EnableServletFilter();
    }

    @Bean
    EnableSpringWebMvc springWebMvc() {
        return new EnableSpringWebMvc();
    }

    @Configuration
    @ConditionalOnClass(WebMvcConfigurer.class)
    class ForWebMvc implements WebMvcConfigurer {

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(springWebMvc()).addPathPatterns("/**");
        }

    }

}