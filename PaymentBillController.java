package bankBackend.bank.controller;
import bankBackend.bank.model.PaymentBill;
import bankBackend.bank.service.PaymentBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
public class PaymentBillController {

    @Autowired
    private PaymentBillService paymentBillService;

    // Get all bills
    @GetMapping
    public List<PaymentBill> getAllBills() {
        return paymentBillService.getAllBills();
    }

    // Get bills for a specific user
    @GetMapping("/user/{userId}")
    public List<PaymentBill> getBillsByUserId(@PathVariable Long userId) {
        return paymentBillService.getBillsByUserId(userId);
    }

    // Create a new bill
    @PostMapping("/user/{userId}")
    public ResponseEntity<PaymentBill> createBill(@PathVariable Long userId, @RequestBody PaymentBill bill) {
        PaymentBill newBill = paymentBillService.createBill(userId, bill);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBill);
    }

    // Pay a bill
    @PutMapping("/pay/{billId}")
    public ResponseEntity<PaymentBill> payBill(@PathVariable Long billId) {
        PaymentBill paidBill = paymentBillService.payBill(billId);
        return ResponseEntity.ok(paidBill);
    }
}

