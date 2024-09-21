package org.zerock.b01.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //해당 클래스가 스프링의 설정 클래스임을 명시하는 역할
//이 클래스 내의 @Beand으로 등록된 메소드들이 Spring 컨테이너에 의해 관리될 Bean을 생성합니다.
public class RootConfig {

    @Bean
    public ModelMapper getMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true) //필드매칭여부 결정 - true : 활성화
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) //private필드에도 접근할 수 있도록 접근권한을 설정
                .setMatchingStrategy(MatchingStrategies.STRICT); //매칭전략정의 - STRICT : 엄격한 소스와 대상의 필드명이 정확히 일치해야 함

        return modelMapper;
    }
}
