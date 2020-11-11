package com.neu.srpc.registry;

import com.neu.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @Author XJH
 * @Date 2020/11/11
 * @Description
 */
@Slf4j
public class ChildrenWatcher implements Watcher {
    private String path;
    private ServiceManager manager;

    public ChildrenWatcher(String path, ServiceManager manager) {
        this.path = path;
        this.manager = manager;
    }

    @Override
    public void process(WatchedEvent event) {
        log.info("has changed path: {}", path);

        if (event.getPath().equals(path) &&
                event.getType() == Event.EventType.NodeChildrenChanged) {

            manager.updateServiceList(path);
        }
    }
}
