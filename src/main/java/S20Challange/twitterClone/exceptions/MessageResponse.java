package S20Challange.twitterClone.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {

        private int status;
        private String message;
        private long timestamp;

}
