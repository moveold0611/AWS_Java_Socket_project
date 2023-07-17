package server.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SendWhisper {
	private String fromUsername;
	private String toUsername;
	private String messageBody;
}
