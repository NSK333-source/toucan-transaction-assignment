package com.example.transactionstarter;

import com.example.transactionstarter.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionStarterApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

//    @Test
//    void contextLoads() {
//    }

    @Test
    void shouldReturnNotFoundForNonExistingTransaction() throws Exception {

        mockMvc.perform(get("/api/transactions/INVALID-001"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction not found"));
    }

    @Test
    void shouldCreateTransactionSuccessfully() throws Exception {

        String requestBody = """
                {
                    "transactionId": "TXN-TEST-001",
                    "customerId": "CUST-TEST-001",
                    "amount": 500.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TXN-TEST-001"))
                .andExpect(jsonPath("$.customerId").value("CUST-TEST-001"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
    @Test
    void shouldRejectTransactionWithInvalidData() throws Exception {

        String requestBody = """
            {
                "transactionId": "",
                "customerId": "",
                "amount": -100,
                "currency": "INR",
                "transactionType": "PAYMENT"
            }
            """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldRejectDuplicateTransactionId() throws Exception {

        String requestBody = """
            {
                "transactionId": "TXN-TEST-001",
                "customerId": "CUST-TEST-001",
                "amount": 500.00,
                "currency": "INR",
                "transactionType": "PAYMENT"
            }
            """;

        // Create the transaction for the first time
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        // Try creating another transaction with the same ID
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldGetTransactionsByCustomerId() throws Exception {

        String transaction1 = """
            {
                "transactionId": "TXN-TEST-005",
                "customerId": "CUST-TEST-001",
                "amount": 500.00,
                "currency": "INR",
                "transactionType": "PAYMENT"
            }
            """;

        String transaction2 = """
            {
                "transactionId": "TXN-TEST-006",
                "customerId": "CUST-TEST-001",
                "amount": 1000.00,
                "currency": "INR",
                "transactionType": "REFUND"
            }
            """;

        // Create first transaction
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transaction1))
                .andExpect(status().isCreated());

        // Create second transaction for the same customer
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transaction2))
                .andExpect(status().isCreated());

        // Get transactions for the customer
        mockMvc.perform(get("/api/transactions/customer/CUST-TEST-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customerId")
                        .value("CUST-TEST-001"))
                .andExpect(jsonPath("$[1].customerId")
                        .value("CUST-TEST-001"));
    }
    @Test
    void shouldUpdateTransactionStatusSuccessfully() throws Exception {

        String createRequest = """
            {
                "transactionId": "TXN-STATUS-001",
                "customerId": "CUST-STATUS-001",
                "amount": 500.00,
                "currency": "INR",
                "transactionType": "PAYMENT"
            }
            """;

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequest))
                .andExpect(status().isCreated());

        String statusRequest = """
            {
                "status": "PROCESSING"
            }
            """;

        mockMvc.perform(patch("/api/transactions/TXN-STATUS-001/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSING"));
    }

}