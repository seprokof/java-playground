package com.seprokof.sikp;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MsgDataTransformed extends MsgData {

    private LocalDateTime timestamp;

    public MsgDataTransformed(MsgData msgData) {
        this.stringField = msgData.stringField;
        this.integerField = msgData.integerField;
        this.timestamp = LocalDateTime.now();
    }

}
