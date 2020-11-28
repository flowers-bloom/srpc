package xjh.rpc.core.util;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * @author XJH
 * @date 2020/11/28
 * zk 工具类
 */
public class ZkUtil {

    /**
     * 更新节点数据
     *
     * @param zk
     * @param path
     * @param data
     * @param version
     */
    public static void update(ZooKeeper zk, String path, byte[] data, int version) {
        try {
            zk.setData(path, data, version+1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除当前节点
     *
     * @param zk
     * @param path
     */
    public static void delete(ZooKeeper zk, String path) {
        try {
            zk.delete(path, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除路径下所有节点
     *
     * @param zk
     * @param parentPath
     */
    public static void clearNodes(ZooKeeper zk, String parentPath) {
        List<String> children = null;
        try {
            children = zk.getChildren(parentPath, false);

            for (String child : children) {
                String childPath = parentPath + "/" + child;

                if (zk.getChildren(childPath, false).size() != 0) {
                    clearNodes(zk, parentPath);
                }

                zk.delete(childPath, -1);
            }

            zk.delete(parentPath, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
