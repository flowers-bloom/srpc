package com.neu.srpc.registry;

import com.neu.cluster.LoadBalance;
import com.neu.common.Constant;
import com.neu.common.Endpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

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
    private final String ROOT_SERVICE = Constant.ZK_ROOT_SERVICE;
    private final String LOCAL_ADDR = Constant.ZK_ADDR;
    private final int SESSION_TIMEOUT = Constant.ZK_SESSION_TIMEOUT;
    private ZooKeeper zooKeeper;

    public ServiceManager() {
        try {
            zooKeeper = new ZooKeeper(LOCAL_ADDR, SESSION_TIMEOUT, watchedEvent -> { });

            Stat exists = zooKeeper.exists(ROOT_SERVICE, false);
            if (exists == null) {
                zooKeeper.create(ROOT_SERVICE, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException | KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注册服务
     *
     * @param serviceName
     * @param ip
     * @param port
     */
    public void register(String serviceName, String ip, int port) {
        try {
            String path = ROOT_SERVICE + "/" + serviceName;
            String point = ip + ":" + port;

            Stat exists1 = zooKeeper.exists(path, false);
            if (exists1 == null) {
                zooKeeper.create(path, point.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("服务 {} 注册成功, 地址 {}:{} ", ip, port);
            }else {
                log.info("服务 {} 已注册, 地址 {}:{} ", ip, port);
            }
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
        List<Endpoint> list = Collections.synchronizedList(new ArrayList<>());

        try {
            List<String> children = zooKeeper.getChildren(path,
                    new ChildrenWatcher(path, this));
            log.info("children = {}", children);

            for (String child : children) {
                byte[] data = zooKeeper.getData(path + "/" + child, false, null);
                list.add(new Endpoint(new String(data, "utf-8")));
            }

            LoadBalance.SERVICE_LIST = list;
        } catch (InterruptedException | KeeperException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }
}
