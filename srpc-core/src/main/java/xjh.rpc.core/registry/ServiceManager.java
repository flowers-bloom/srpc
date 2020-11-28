package xjh.rpc.core.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import xjh.rpc.core.cluster.LoadBalance;
import xjh.rpc.core.cluster.WeightedRandomLoadBalance;
import xjh.rpc.core.common.Constant;
import xjh.rpc.core.util.ZkUtil;
import xjh.rpc.transport.common.Endpoint;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author XJH
 * @Date 2020/11/07
 * @Description 注册中心管理类
 */
@Slf4j
public class ServiceManager {
    private String address;
    private final String ROOT_SERVICE = Constant.ZK_ROOT_SERVICE;
    private final int SESSION_TIMEOUT = Constant.ZK_SESSION_TIMEOUT;
    private static List<String> serviceList;
    private LoadBalance loadBalance;
    private ZooKeeper zooKeeper;

    public ServiceManager(String address) {
        this.address = address;

        try {
            zooKeeper = new ZooKeeper(address, SESSION_TIMEOUT, watchedEvent -> {});

            Stat exists = zooKeeper.exists(ROOT_SERVICE, false);
            if (exists == null) {
                zooKeeper.create(ROOT_SERVICE, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException | KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ServiceManager(String address, LoadBalance loadBalance) {
        this(address);
        this.loadBalance = loadBalance;
    }

    /**
     * 注册服务
     *
     * @param serviceName
     * @param info
     */
    public void register(String serviceName, String info) {
        try {
            String path = ROOT_SERVICE + "/" + serviceName;

            Stat exists = zooKeeper.exists(path, false);
            if (exists != null) {
                ZkUtil.clearNodes(zooKeeper, path);
            }

            zooKeeper.create(path, info.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            log.info("服务 {} 注册成功, info = {}", serviceName, info);
        } catch (InterruptedException | KeeperException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 服务发现
     */
    public void discover() {
        updateServiceList(ROOT_SERVICE);
    }

    /**
     * 更新服务列表
     *
     * @param path
     */
    public void updateServiceList(String path) {
        List<String> list = Collections.synchronizedList(new ArrayList<>());

        try {
            List<String> children = zooKeeper.getChildren(path,
                    new ChildrenWatcher(path, this));
            log.info("children = {}", children);

            for (String child : children) {
                byte[] data = zooKeeper.getData(path + "/" + child, false, null);
                list.add(new String(data, "utf-8"));
            }

            updateServiceList(list);
            log.info("serviceList = {}", serviceList);
        } catch (InterruptedException | KeeperException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateServiceList(List<String> list) {
        serviceList = list;
        loadBalance.push(serviceList);
    }
}
