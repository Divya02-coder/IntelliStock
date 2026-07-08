package interfaces;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/*
  Contract for anything whose state changes should be traceable —
  every stock/price change gets timestamped and logged so the
  business can answer "what happened to this product and when."
 */
public interface Auditable {

    LocalDateTime getLastModified();

    List<String> getAuditLog();

    void logChange(String description);
}