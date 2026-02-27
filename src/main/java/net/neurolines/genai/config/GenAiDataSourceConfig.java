package net.neurolines.genai.config;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class GenAiDataSourceConfig {

    @Primary
    @Bean(name = "genAiDataSource")
    @ConfigurationProperties(prefix = "spring.genai-datasource")
    public DataSource genAiDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "genAiSqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("genAiDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/genai/*.xml")
        );
        return factoryBean.getObject();
    }

    @Primary
    @Bean(name = "genAiSqlSessionTemplate")
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("genAiSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
