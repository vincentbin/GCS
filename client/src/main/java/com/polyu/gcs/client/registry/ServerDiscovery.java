package com.polyu.gcs.client.registry;

import com.polyu.gcs.client.connect.Connector;
import com.polyu.gcs.common.config.NameSpaceEnum;
import com.polyu.gcs.common.info.RegistryPackage;
import com.polyu.gcs.common.zk.CuratorClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServerDiscovery {
    private static final Logger logger = LoggerFactory.getLogger(ServerDiscovery.class);

    private CuratorClient zkClient;
    private List<String> nodeList;

    public ServerDiscovery(String registryAddress) throws Exception {
        this.zkClient = new CuratorClient(registryAddress, 5000);
        pullServerInfo();
    }

    public void connectServer(int index) throws Exception {
        String node = nodeList.get(index);
        byte[] bytes = zkClient.getData(NameSpaceEnum.ZK_REGISTRY_PATH.getValue().concat("/").concat(node));
        String json = new String(bytes);
        RegistryPackage registryPackage = RegistryPackage.fromJson(json);
        Connector.connect(registryPackage.getHost(), registryPackage.getPort());
    }

    public void pullServerInfo() throws Exception {
        this.nodeList = zkClient.getChildren(NameSpaceEnum.ZK_REGISTRY_PATH.getValue());
    }

    public List<String> getNodeList() {
        return nodeList;
    }

}
