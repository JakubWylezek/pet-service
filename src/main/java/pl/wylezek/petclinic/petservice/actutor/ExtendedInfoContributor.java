package pl.wylezek.petclinic.petservice.actutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Map;

@Component
public class ExtendedInfoContributor implements InfoContributor {

    @Autowired
    private DataSource dataSource;

    @Value("${server.port}")
    private int port;

    @Value("${management.endpoints.web.path-mapping.health}")
    private String healthCheckContextPath;

    private static final String healthCheckAddressPrefix = "http://";
    private static final String healthCheckContextPathPrefix = "/actuator";

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("heathCheck", getHealthCheckAddress());
        builder.withDetails(Map.of("java",
                Map.of("vm", Map.of("version", getJavaVersion(),"name", getJavaVmName()),
                        "runtime", Map.of("version", getJavaRuntimeVersion()),
                        "version", getJavaVmVersion())));
        builder.withDetail("database", Map.of("version", getDatabaseVersion(),"name", getDatabaseName()));
    }

    private String getHealthCheckAddress() {
        try {
            return healthCheckAddressPrefix + InetAddress.getLocalHost().getHostAddress() + ":" + port
                    + healthCheckContextPathPrefix + healthCheckContextPath;
        } catch (UnknownHostException e) {
            return "Unable to load address";
        }
    }

    private String getJavaVersion() {
        return System.getProperty("java.version");
    }

    private String getJavaVmName() {
        return System.getProperty("java.vm.name");
    }

    private String getJavaVmVersion() {
        return System.getProperty("java.vm.version");
    }

    private String getJavaRuntimeVersion() {
        return System.getProperty("java.runtime.version");
    }

    private String getDatabaseName() {
        try {
            return dataSource.getConnection().getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            return "Unable to load datasource version";
        }
    }

    private String getDatabaseVersion() {
        try {
            return dataSource.getConnection().getMetaData().getDatabaseProductVersion();
        } catch (SQLException e) {
            return "Unable to load datasource version";
        }
    }
}
