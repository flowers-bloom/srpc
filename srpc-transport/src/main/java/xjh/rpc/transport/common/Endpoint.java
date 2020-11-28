package xjh.rpc.transport.common;


/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description 可用的服务端点
 */
public class Endpoint {
    private String ip;
    private int port;

    public Endpoint() {}

    public Endpoint(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Endpoint(String address) {
        int sep = address.indexOf(':');

        this.ip = address.substring(0, sep);
        this.port = Integer.parseInt(address.substring(sep+1));
    }

    @Override
    public String toString() {
        return ip  + ":" + port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
