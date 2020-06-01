package org.fan.demo.dynamic.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.1 13:09
 */
@Component
@Data
public class DruidDataSourceProperties {
    private String name;
    private String url;
    private String userName;
    private String password;
    private String driverClassName;
    private Prop prop = new Prop();

    public class Prop {
        public Prop() {
        }

        public Prop(Integer initialSize, Integer minIdle, Integer maxActive, Integer maxWait, Integer timeBetweenEvictionRunsMillis, Integer minEvictableIdleTimeMillis, Integer maxPoolPreparedStatementPerConnectionSize, Integer connectionErrorRetryAttempts, Integer timeBetweenConnectErrorMillis, Integer removeAbandonedTimeout, Integer transactionQueryTimeout, String validationQuery, String filters, boolean testWhileIdle, boolean testOnBorrow, boolean testOnReturn, boolean poolPreparedStatements, boolean breakAfterAcquireFailure, boolean asyncInit, boolean removeAbandoned) {
            this.initialSize = initialSize;
            this.minIdle = minIdle;
            this.maxActive = maxActive;
            this.maxWait = maxWait;
            this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
            this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
            this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
            this.connectionErrorRetryAttempts = connectionErrorRetryAttempts;
            this.timeBetweenConnectErrorMillis = timeBetweenConnectErrorMillis;
            this.removeAbandonedTimeout = removeAbandonedTimeout;
            this.transactionQueryTimeout = transactionQueryTimeout;
            this.validationQuery = validationQuery;
            this.filters = filters;
            this.testWhileIdle = testWhileIdle;
            this.testOnBorrow = testOnBorrow;
            this.testOnReturn = testOnReturn;
            this.poolPreparedStatements = poolPreparedStatements;
            this.breakAfterAcquireFailure = breakAfterAcquireFailure;
            this.asyncInit = asyncInit;
            this.removeAbandoned = removeAbandoned;
        }

        private Integer initialSize;
        private Integer minIdle;
        private Integer maxActive;
        private Integer maxWait;
        private Integer timeBetweenEvictionRunsMillis;
        private Integer minEvictableIdleTimeMillis;
        private Integer maxPoolPreparedStatementPerConnectionSize;
        private Integer connectionErrorRetryAttempts;
        private Integer timeBetweenConnectErrorMillis;
        private Integer removeAbandonedTimeout;
        private Integer transactionQueryTimeout;
        private String validationQuery;
        private String filters;
        private boolean testWhileIdle;
        private boolean testOnBorrow;
        private boolean testOnReturn;
        private boolean poolPreparedStatements;
        private boolean breakAfterAcquireFailure;
        private boolean asyncInit;
        private boolean removeAbandoned;

        public Integer getInitialSize() {
            return initialSize;
        }

        public void setInitialSize(Integer initialSize) {
            this.initialSize = initialSize;
        }

        public Integer getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(Integer minIdle) {
            this.minIdle = minIdle;
        }

        public Integer getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(Integer maxActive) {
            this.maxActive = maxActive;
        }

        public Integer getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(Integer maxWait) {
            this.maxWait = maxWait;
        }

        public Integer getTimeBetweenEvictionRunsMillis() {
            return timeBetweenEvictionRunsMillis;
        }

        public void setTimeBetweenEvictionRunsMillis(Integer timeBetweenEvictionRunsMillis) {
            this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        }

        public Integer getMinEvictableIdleTimeMillis() {
            return minEvictableIdleTimeMillis;
        }

        public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
            this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
        }

        public Integer getMaxPoolPreparedStatementPerConnectionSize() {
            return maxPoolPreparedStatementPerConnectionSize;
        }

        public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
            this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
        }

        public Integer getConnectionErrorRetryAttempts() {
            return connectionErrorRetryAttempts;
        }

        public void setConnectionErrorRetryAttempts(Integer connectionErrorRetryAttempts) {
            this.connectionErrorRetryAttempts = connectionErrorRetryAttempts;
        }

        public Integer getTimeBetweenConnectErrorMillis() {
            return timeBetweenConnectErrorMillis;
        }

        public void setTimeBetweenConnectErrorMillis(Integer timeBetweenConnectErrorMillis) {
            this.timeBetweenConnectErrorMillis = timeBetweenConnectErrorMillis;
        }

        public Integer getRemoveAbandonedTimeout() {
            return removeAbandonedTimeout;
        }

        public void setRemoveAbandonedTimeout(Integer removeAbandonedTimeout) {
            this.removeAbandonedTimeout = removeAbandonedTimeout;
        }

        public Integer getTransactionQueryTimeout() {
            return transactionQueryTimeout;
        }

        public void setTransactionQueryTimeout(Integer transactionQueryTimeout) {
            this.transactionQueryTimeout = transactionQueryTimeout;
        }

        public String getValidationQuery() {
            return validationQuery;
        }

        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }

        public String getFilters() {
            return filters;
        }

        public void setFilters(String filters) {
            this.filters = filters;
        }

        public boolean getTestWhileIdle() {
            return testWhileIdle;
        }

        public void setTestWhileIdle(boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
        }

        public boolean getTestOnBorrow() {
            return testOnBorrow;
        }

        public void setTestOnBorrow(boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
        }

        public boolean getTestOnReturn() {
            return testOnReturn;
        }

        public void setTestOnReturn(boolean testOnReturn) {
            this.testOnReturn = testOnReturn;
        }

        public boolean getPoolPreparedStatements() {
            return poolPreparedStatements;
        }

        public void setPoolPreparedStatements(boolean poolPreparedStatements) {
            this.poolPreparedStatements = poolPreparedStatements;
        }

        public boolean getBreakAfterAcquireFailure() {
            return breakAfterAcquireFailure;
        }

        public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
            this.breakAfterAcquireFailure = breakAfterAcquireFailure;
        }

        public boolean getAsyncInit() {
            return asyncInit;
        }

        public void setAsyncInit(boolean asyncInit) {
            this.asyncInit = asyncInit;
        }

        public boolean getRemoveAbandoned() {
            return removeAbandoned;
        }

        public void setRemoveAbandoned(boolean removeAbandoned) {
            this.removeAbandoned = removeAbandoned;
        }


    }
}
