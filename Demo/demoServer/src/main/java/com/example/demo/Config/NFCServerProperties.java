package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("nfcserver.properties")
public class NFCServerProperties {

    @Value("${sms.host.token}")
    private static String smsHostToken;

    public static String getSmsHostToken() {
        return smsHostToken;
    }
}
