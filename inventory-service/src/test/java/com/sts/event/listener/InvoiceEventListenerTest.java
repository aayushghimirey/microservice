//package com.sts.event.listener;
//
//import com.sts.event.InvoiceEvent;
//import com.sts.event.StockUpdateEvent;
//import com.sts.event.factory.StockUpdateFactoryRegistry;
//import com.sts.helper.event.DomainEventPublisher;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.kafka.support.Acknowledgment;
//
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class InvoiceEventListenerTest {
//
//    @Mock
//    private StockUpdateFactoryRegistry stockUpdateFactoryRegistry;
//
//    @Mock
//    private DomainEventPublisher domainEventPublisher;
//
//    @Mock
//    private Acknowledgment acknowledgment;
//
//    @Mock
//    private StockUpdateEvent stockUpdateEvent;
//
//    @InjectMocks
//    private InvoiceEventListener invoiceEventListener;
//
//    private InvoiceEvent invoiceEvent;
//
//    @BeforeEach
//    void setUp() {
//        invoiceEvent = new InvoiceEvent();
//        invoiceEvent.setInvoiceId(UUID.randomUUID());   // adjust setter to your actual fields
//    }
//
//    @Test
//    @DisplayName("listen() — happy path: publishes stock event and acknowledges")
//    void listen_success() {
//        // Arrange
//        when(stockUpdateFactoryRegistry.forInvoice(invoiceEvent)).thenReturn(stockUpdateEvent);
//
//        // Act
//        invoiceEventListener.listen(invoiceEvent, acknowledgment);
//
//        // Assert
//        verify(stockUpdateFactoryRegistry).forInvoice(invoiceEvent);
//        verify(domainEventPublisher).publish(stockUpdateEvent);
//        verify(acknowledgment).acknowledge();         // must ACK on success
//    }
//
//}