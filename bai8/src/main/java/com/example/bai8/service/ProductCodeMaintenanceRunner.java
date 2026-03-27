package com.example.bai8.service;

import com.example.bai8.model.Product;
import com.example.bai8.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class ProductCodeMaintenanceRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ProductCodeMaintenanceRunner.class);
    private static final int MAX_CODE_LENGTH = 50;

    private final ProductRepository productRepository;
    private final JdbcTemplate jdbcTemplate;

    public ProductCodeMaintenanceRunner(ProductRepository productRepository, JdbcTemplate jdbcTemplate) {
        this.productRepository = productRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        int fixedCount = normalizeAndDeduplicateCodes();
        ensureUniqueCodeIndex();
        if (fixedCount > 0) {
            logger.info("Da chuan hoa/khac phuc trung ma san pham: {} ban ghi", fixedCount);
        }
    }

    private int normalizeAndDeduplicateCodes() {
        List<Product> products = productRepository.findAll(org.springframework.data.domain.Sort.by("id"));
        Set<String> usedCodes = new HashSet<>();
        List<Product> changedProducts = new ArrayList<>();

        for (Product product : products) {
            String base = normalizeBaseCode(product.getCode(), product.getId());
            String uniqueCode = buildUniqueCode(base, product.getId(), usedCodes);
            usedCodes.add(uniqueCode.toLowerCase(Locale.ROOT));

            if (!uniqueCode.equals(product.getCode())) {
                product.setCode(uniqueCode);
                changedProducts.add(product);
            }
        }

        if (!changedProducts.isEmpty()) {
            productRepository.saveAllAndFlush(changedProducts);
        }

        return changedProducts.size();
    }

    private void ensureUniqueCodeIndex() {
        Integer indexCount = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(1)
                FROM information_schema.statistics
                WHERE table_schema = DATABASE()
                  AND table_name = 'products'
                  AND index_name = 'uk_products_code'
                """,
                Integer.class
        );

        if (indexCount != null && indexCount > 0) {
            return;
        }

        try {
            jdbcTemplate.execute("CREATE UNIQUE INDEX uk_products_code ON products (code)");
            logger.info("Da tao unique index uk_products_code cho products(code)");
        } catch (DataAccessException e) {
            logger.warn("Khong tao duoc unique index uk_products_code: {}", e.getMessage());
        }
    }

    private String normalizeBaseCode(String code, int id) {
        String raw = code == null ? "" : code.trim().toUpperCase(Locale.ROOT);
        if (raw.isEmpty()) {
            raw = "SP" + id;
        }
        if (raw.length() > MAX_CODE_LENGTH) {
            return raw.substring(0, MAX_CODE_LENGTH);
        }
        return raw;
    }

    private String buildUniqueCode(String base, int id, Set<String> usedCodes) {
        int attempt = 0;
        while (true) {
            String candidate = candidateForAttempt(base, id, attempt);
            String lookup = candidate.toLowerCase(Locale.ROOT);
            if (!usedCodes.contains(lookup)) {
                return candidate;
            }
            attempt++;
        }
    }

    private String candidateForAttempt(String base, int id, int attempt) {
        String suffix = "";
        if (attempt > 0) {
            suffix = "-" + id;
            if (attempt > 1) {
                suffix += "-" + attempt;
            }
        }

        int maxBaseLength = Math.max(1, MAX_CODE_LENGTH - suffix.length());
        String safeBase = base.length() > maxBaseLength ? base.substring(0, maxBaseLength) : base;
        return safeBase + suffix;
    }
}
