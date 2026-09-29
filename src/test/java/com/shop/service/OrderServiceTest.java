package com.shop.service;

import com.shop.dao.OrderDao;
import com.shop.dao.ProductDao;
import com.shop.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/** Tests order business rules. */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private ProductDao productDao;

    @Test
    void shouldRejectOrderWhenStockIsInsufficient() throws Exception {
        Product product = new Product();
        product.setId(10L);
        product.setPrice(BigDecimal.TEN);
        product.setQuantity(2);
        when(productDao.findById(10L)).thenReturn(product);

        OrderService service = new OrderService(orderDao, productDao);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.create(1L, 10L, 3)
        );
        verify(orderDao, never()).create(anyLong(), anyLong(), anyInt(), any(BigDecimal.class));
    }

    @Test
    void shouldCalculateTotalPrice() throws Exception {
        Product product = new Product();
        product.setId(10L);
        product.setPrice(new BigDecimal("12.50"));
        product.setQuantity(10);
        when(productDao.findById(10L)).thenReturn(product);

        OrderService service = new OrderService(orderDao, productDao);
        service.create(1L, 10L, 4);

        verify(orderDao).create(1L, 10L, 4, new BigDecimal("50.00"));
    }
}
