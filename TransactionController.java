package bankBackend.bank.controller;
import bankBackend.bank.model.Transaction;
import bankBackend.bank.repository.TransactionRepository;
import bankBackend.bank.service.UserSpringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
public class TransactionController {
    @Autowired
    private UserSpringService userService;
    @Autowired
    private TransactionRepository transactionRepository;
    @PostMapping("/{userId}")
    public ResponseEntity<Transaction> createTransaction(
            @PathVariable Long userId,
            @RequestBody Transaction transaction) {
        return ResponseEntity.ok(userService.createTransaction(userId, transaction));
    }

    @PostMapping("/transfer/{senderId}/{receiverId}")
    public ResponseEntity<Transaction> transferMoney(
            @PathVariable Long senderId,
            @PathVariable Long receiverId,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(userService.transferMoney(senderId, receiverId, amount));
    }

    // Get all transactions
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Get transactions by User ID
    @GetMapping("/user/{userId}")
    public List<Transaction> getTransactionsByUserId(@PathVariable Long userId) {
        return transactionRepository.findByUserId(userId);
    }
}
