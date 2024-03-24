package com.app.zhardem.dto.message;

import com.app.zhardem.enums.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private String content;

    private String sender;

    private MessageType type;

}
