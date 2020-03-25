package com.register.move.service.dao;

import com.google.common.eventbus.Subscribe;
import com.register.move.service.event.DaoEvent;

/**
 *  Data access layer.
 * On the basis of Nacos Config As  the storage
 * @linkl{https://nacos.io/zh-cn/docs/sdk.html}
 */

public interface ServiceMetaDataOperation {

    @Subscribe
    boolean publish(DaoEvent event);

    @Subscribe
    boolean remove(DaoEvent event) ;

    @Subscribe
    String get(DaoEvent event);
}
