package bankBackend.bank.service;
import bankBackend.bank.model.Transaction;
import bankBackend.bank.model.UserSpring;
import bankBackend.bank.repository.TransactionRepository;
import bankBackend.bank.repository.UserSpringRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserSpringService {
    @Autowired
    private UserSpringRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public List<UserSpring> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserSpring> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserSpring createUser(UserSpring user) {
        return userRepository.save(user);
    }

    public UserSpring updateUser(Long id, UserSpring userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setSavings(userDetails.getSavings());
            user.setStatus(userDetails.getStatus());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Transaction createTransaction(Long userId, Transaction transactionDetails) {
        UserSpring user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Deduct savings
        if (user.getSavings().compareTo(transactionDetails.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        user.setSavings(user.getSavings().subtract(transactionDetails.getAmount()));

        transactionDetails.setUser(user);
        transactionDetails.setTransactionDate(LocalDateTime.now());

        userRepository.save(user);
        return transactionRepository.save(transactionDetails);
    }

    // Transfer money to another user
    public Transaction transferMoney(Long senderId, Long receiverId, BigDecimal amount) {
        UserSpring sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        UserSpring receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getSavings().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct from sender and add to receiver
        sender.setSavings(sender.getSavings().subtract(amount));
        receiver.setSavings(receiver.getSavings().add(amount));

        // Create transaction record
        Transaction senderTransaction = new Transaction(amount.negate(), "Transfer to " + receiver.getUsername(), LocalDateTime.now(), sender);
        Transaction receiverTransaction = new Transaction(amount, "Received from " + sender.getUsername(), LocalDateTime.now(), receiver);

        userRepository.save(sender);
        userRepository.save(receiver);
        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);

        return senderTransaction;
    }
}