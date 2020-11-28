package xjh.rpc.core.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @Author XJH
 * @Date 2020/11/11
 * @Description 子节点 watcher，监听子节点的新增和删除操作
 */
@Slf4j
public class ChildrenWatcher implements Watcher {
    private String path;
    private ServiceManager manager;

    public ChildrenWatcher(String path, ServiceManager manager) {
        this.path = path;
        this.manager = manager;
    }

    /**
     * 当子节点发生变化时的回调方法，更新注册的服务列表
     *
     * @param event
     */
    @Override
    public void process(WatchedEvent event) {
        log.info("has changed path: {}", path);

        if (event.getPath().equals(path) &&
                event.getType() == Event.EventType.NodeChildrenChanged) {

            manager.updateServiceList(path);
        }
    }
}
