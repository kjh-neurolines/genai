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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class NlchemDataSourceConfig {

    @Bean(name = "nlchemDataSource")
    @ConfigurationProperties(prefix = "spring.nlchem-datasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "nlchemSqlSessionFactory")
    public SqlSessionFactory secondarySqlSessionFactory(@Qualifier("nlchemDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/nlchem/*.xml")
        );
        return factoryBean.getObject();
    }

    @Bean(name = "nlchemSqlSessionTemplate")
    public SqlSessionTemplate secondarySqlSessionTemplate(@Qualifier("nlchemSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
