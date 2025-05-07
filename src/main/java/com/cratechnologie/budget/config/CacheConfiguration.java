package com.cratechnologie.budget.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.cratechnologie.budget.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.cratechnologie.budget.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.cratechnologie.budget.domain.User.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.Authority.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.User.class.getName() + ".authorities");
            createCache(cm, com.cratechnologie.budget.domain.AppUser.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.FinancialYear.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.SubTitle.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.SubTitle.class.getName() + ".chapters");
            createCache(cm, com.cratechnologie.budget.domain.Chapter.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.Chapter.class.getName() + ".articles");
            createCache(cm, com.cratechnologie.budget.domain.Article.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.Article.class.getName() + ".recipes");
            createCache(cm, com.cratechnologie.budget.domain.Article.class.getName() + ".expenses");
            createCache(cm, com.cratechnologie.budget.domain.Recipe.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.Recipe.class.getName() + ".articles");
            createCache(cm, com.cratechnologie.budget.domain.Expense.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.Expense.class.getName() + ".articles");
            createCache(cm, com.cratechnologie.budget.domain.AnnexDecision.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.AnnexDecision.class.getName() + ".purchaseOrders");
            createCache(cm, com.cratechnologie.budget.domain.AnnexDecision.class.getName() + ".decisions");
            createCache(cm, com.cratechnologie.budget.domain.Supplier.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.Supplier.class.getName() + ".purchaseOrders");
            createCache(cm, com.cratechnologie.budget.domain.PurchaseOrder.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.PurchaseOrder.class.getName() + ".purchaseOrderItems");
            createCache(cm, com.cratechnologie.budget.domain.PurchaseOrderItem.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.Decision.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.Decision.class.getName() + ".decisionItems");
            createCache(cm, com.cratechnologie.budget.domain.DecisionItem.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.Engagement.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.Engagement.class.getName() + ".purchaseOrders");
            createCache(cm, com.cratechnologie.budget.domain.Mandate.class.getName());
            createCache(cm, com.cratechnologie.budget.domain.FinancialYear.class.getName() + ".recipes");
            createCache(cm, com.cratechnologie.budget.domain.FinancialYear.class.getName() + ".expenses");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
