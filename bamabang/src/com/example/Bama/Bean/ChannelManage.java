package com.example.Bama.Bean;

import java.util.ArrayList;
import java.util.List;

public class ChannelManage {
    public static ChannelManage channelManage;

    public static List<ChannelItem> defaultChannels;

    static {
        defaultChannels = new ArrayList<ChannelItem>();
        defaultChannels.add(new ChannelItem(1, "环信真实群组", 1, 1));
        defaultChannels.add(new ChannelItem(2, "baobaodazahui", 2, 1));
        defaultChannels.add(new ChannelItem(3, "citycitycity", 3, 1));
        defaultChannels.add(new ChannelItem(4, "babybabybaby", 4, 1));
        defaultChannels.add(new ChannelItem(5, "babycity", 5, 1));
        defaultChannels.add(new ChannelItem(6, "mybabycity", 6, 1));
        defaultChannels.add(new ChannelItem(7, "babyhandrd", 7, 1));
        defaultChannels.add(new ChannelItem(8, "babyteach", 8, 1));
    }

    public static ChannelManage getManage() {
        if (channelManage == null)
            channelManage = new ChannelManage();
        return channelManage;
    }
}
