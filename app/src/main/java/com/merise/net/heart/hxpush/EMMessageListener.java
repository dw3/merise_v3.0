package com.merise.net.heart.hxpush;

import com.hyphenate.chat.EMMessage;

import java.util.List;

public interface EMMessageListener {
    void onMessageReceived(List<EMMessage> var1);

    void onCmdMessageReceived(List<EMMessage> var1);

    void onMessageReadAckReceived(List<EMMessage> var1);

    void onMessageDeliveryAckReceived(List<EMMessage> var1);

    void onMessageChanged(EMMessage var1, Object var2);
}