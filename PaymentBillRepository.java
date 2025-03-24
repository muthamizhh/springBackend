package bankBackend.bank.repository;

import bankBackend.bank.model.PaymentBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentBillRepository extends JpaRepository<PaymentBill, Long> {
    List<PaymentBill> findByUserId(Long userId);
}
