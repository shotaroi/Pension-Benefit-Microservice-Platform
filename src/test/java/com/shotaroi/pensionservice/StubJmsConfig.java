package com.shotaroi.pensionservice;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Provides a stub ConnectionFactory for tests that don't need real JMS.
 * Use with @MockBean JmsTemplate when testing flows that publish events.
 */
@TestConfiguration
public class StubJmsConfig {

    @Bean
    @Primary
    public ConnectionFactory connectionFactory() {
        return new ConnectionFactory() {
            @Override
            public Connection createConnection() throws JMSException {
                throw new UnsupportedOperationException("Stub - not for actual JMS use");
            }

            @Override
            public Connection createConnection(String userName, String password) throws JMSException {
                throw new UnsupportedOperationException("Stub - not for actual JMS use");
            }

            @Override
            public JMSContext createContext() {
                throw new UnsupportedOperationException("Stub - not for actual JMS use");
            }

            @Override
            public JMSContext createContext(String userName, String password) {
                throw new UnsupportedOperationException("Stub - not for actual JMS use");
            }

            @Override
            public JMSContext createContext(int sessionMode) {
                throw new UnsupportedOperationException("Stub - not for actual JMS use");
            }

            @Override
            public JMSContext createContext(String userName, String password, int sessionMode) {
                throw new UnsupportedOperationException("Stub - not for actual JMS use");
            }
        };
    }
}
