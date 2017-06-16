package com.contact;

import com.contact.converters.ModelConverter;
import com.contact.models.*;
import com.contact.utils.Functions;
import org.springframework.boot.SpringApplication;
import com.lyncode.jtwig.mvc.JtwigViewResolver;
import com.lyncode.jtwig.services.api.assets.AssetResolver;
import com.lyncode.jtwig.services.impl.assets.BaseAssetResolver;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

@SpringBootApplication

public class Application  extends WebMvcConfigurerAdapter {

    @Bean
    public Functions userFunctions () {
        return new Functions();
    }

    @Bean
    public ViewResolver viewResolver() {
        JtwigViewResolver viewResolver = new JtwigViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".twig");
        viewResolver.setCached(false);
        viewResolver.configuration().render()
                .functionRepository().include(userFunctions());
        return viewResolver;
    }

    @Bean
    public AssetResolver assetResolver () {
        BaseAssetResolver assetResolver = new BaseAssetResolver();
        assetResolver.setPrefix("/static/");
        return assetResolver;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**/*").addResourceLocations("/WEB-INF/static/");
    }

    /*@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor()).addPathPatterns("*//**");
    }*/

    /*@Bean
    public HandlerInterceptor securityInterceptor() {
        return new SecurityInterceptor();
    }*/


    @Override
    public void addFormatters(FormatterRegistry registry) {
        Class<?>[] classes = {
                Account.class
        };

        Arrays.asList(classes).forEach(p -> registry.addConverter(new ModelConverter(p)));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
