package v.rabetsky.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoleRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        // может вернуть null → будет использован defaultTargetDataSource
        return RoleContext.get();
    }
}
