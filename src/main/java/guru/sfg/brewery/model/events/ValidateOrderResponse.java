package guru.sfg.brewery.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateOrderResponse implements Serializable {
    private static final long serialVersionUID = 901855218593081819L;

    private String orderId;
    private boolean isValid;
}
