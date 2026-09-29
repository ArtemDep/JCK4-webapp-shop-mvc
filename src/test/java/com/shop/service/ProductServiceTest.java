package com.shop.service;

import com.shop.dao.ProductDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/** Tests product validation business logic. */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Test
    void shouldRejectNegativePrice() {
        ProductService service = new ProductService(productDao);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.create("Mouse", "Description", BigDecimal.valueOf(-1), 5)
        );

        verify(productDao, never()).create(org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.anyInt());
    }
}
