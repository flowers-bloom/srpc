package com.neu.srpc.registry;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author XJH
 * @Date 2020/11/07
 * @Description 服务管理类，实现服务注册，服务发现功能
 */
public class ServiceManager {
    private static final String ROOT_SERVICE = "/srpc";
    private static final String LOCAL_ADDR = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 5000;
    private static ZooKeeper zooKeeper;

    public static void register(String serviceName, String ip, int port) {
        try {
            zooKeeper = new ZooKeeper(LOCAL_ADDR, SESSION_TIMEOUT, watchedEvent -> {});

            Stat exists = zooKeeper.exists(ROOT_SERVICE, false);
            if (exists == null) {
                zooKeeper.create(ROOT_SERVICE, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }


            String path = ROOT_SERVICE + serviceName, addr = ip + ":" + port;
            zooKeeper.create(path, addr.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            System.out.println("服务注册完成");
        } catch (IOException | InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    public static void discover(String serviceName) {
        try {
            String path = ROOT_SERVICE + serviceName;

            zooKeeper = new ZooKeeper(LOCAL_ADDR, SESSION_TIMEOUT, watchedEvent -> {
                if (watchedEvent.getPath().equals(path) &&
                watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    System.out.println("zk 注册信息发生改变，更新服务注册表");
                    updateServiceList(path);
                }
            });

            updateServiceList(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateServiceList(String path) {
        List<String> list = new ArrayList<>();

        try {
            List<String> children = zooKeeper.getChildren(path, true);
            for (String child : children) {
                byte[] data = zooKeeper.getData(path + "/" + child, false, null);
                list.add(new String(data, "utf-8"));
            }

            LoadBalance.SERVICE_LIST = list;
        } catch (InterruptedException | KeeperException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
