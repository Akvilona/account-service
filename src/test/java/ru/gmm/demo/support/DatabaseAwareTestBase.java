package ru.gmm.demo.support;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionTemplate;
import ru.gmm.demo.initializer.PostgreSqlInitializer;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {
    PostgreSqlInitializer.class
})
@ActiveProfiles("test")
public abstract class DatabaseAwareTestBase {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TransactionTemplate transactionTemplate;

    protected abstract String getSchema();

    protected abstract Set<String> getTables();

    protected void executeInTransaction(final Runnable runnable) {
        transactionTemplate.execute(status -> {
            runnable.run();
            return null;
        });
    }

    @BeforeEach
    void check() {
        getTables().stream()
            .map(this::countRecordsInTable)
            .forEach(count -> assertThat(count)
                .isZero());
    }

    @AfterEach
    void truncateTables() {
        jdbcTemplate.execute("TRUNCATE TABLE " + getTables()
            .stream()
            .map(this::tableNameWithSchema)
            .collect(Collectors.joining(", ")) + " RESTART IDENTITY CASCADE");
    }

    protected long countRecordsInTable(final String tableName) {
        final var queryResult = jdbcTemplate.queryForObject("select count(*) from " + tableNameWithSchema(tableName), Long.class);
        return Objects.requireNonNullElse(queryResult, 0L);
    }

    protected String tableNameWithSchema(final String tableName) {
        final var schema = getSchema();
        return tableName.startsWith(schema) ? tableName : String.format("%s.%s", schema, tableName);
    }

}
