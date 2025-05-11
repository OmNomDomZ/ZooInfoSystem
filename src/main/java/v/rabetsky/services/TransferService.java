package v.rabetsky.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v.rabetsky.dao.TransferDAO;
import v.rabetsky.dto.TransferDTO;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TransferService {
    private final TransferDAO transferDAO;

    public TransferService(TransferDAO transferDAO) {
        this.transferDAO = transferDAO;
    }

    public List<TransferDTO> findAll() {
        return transferDAO.findAll();
    }

    @Transactional
    public void recordOutgoing(int animalId, String reason, Integer destinationZooId, LocalDate date) {
        transferDAO.save(animalId, reason, destinationZooId, date);
    }
}
