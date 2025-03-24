package bankBackend.bank.service;
import bankBackend.bank.model.PaymentBill;
import bankBackend.bank.model.Transaction;
import bankBackend.bank.model.UserSpring;
import bankBackend.bank.repository.PaymentBillRepository;
import bankBackend.bank.repository.TransactionRepository;
import bankBackend.bank.repository.UserSpringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentBillService {
    @Autowired
    private PaymentBillRepository paymentBillRepository;

    @Autowired
    private UserSpringRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Get all bills
    public List<PaymentBill> getAllBills() {
        return paymentBillRepository.findAll();
    }

    // Get bills by User ID
    public List<PaymentBill> getBillsByUserId(Long userId) {
        return paymentBillRepository.findByUserId(userId);
    }

    // Create a new bill
    public PaymentBill createBill(Long userId, PaymentBill billDetails) {
        UserSpring user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PaymentBill bill = new PaymentBill(
                billDetails.getBillName(),
                billDetails.getAmount(),
                billDetails.getDueDate(),
                user
        );

        return paymentBillRepository.save(bill);
    }

    // Pay the bill and update the transaction history
    @Transactional
    public PaymentBill payBill(Long billId) {
        PaymentBill bill = paymentBillRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        UserSpring user = bill.getUser();

        // Ensure user has enough savings
        if (user.getSavings().compareTo(bill.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance to pay the bill");
        }

        // Deduct the bill amount from savings
        user.setSavings(user.getSavings().subtract(bill.getAmount()));

        // Create a transaction for bill payment
        Transaction transaction = new Transaction(
                bill.getAmount().negate(),  // Negative because it's an expense
                "Bill Payment: " + bill.getBillName(),
                LocalDateTime.now(),
                user
        );

        // Save the transaction and update the user
        transactionRepository.save(transaction);
        userRepository.save(user);

        // Mark the bill as paid
        bill.setPaid(true);
        return paymentBillRepository.save(bill);
    }

    // Get all bills




}
