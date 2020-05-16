package com.yuan.myrule;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.annotation.Configuration;
//import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
@Configuration
public class MyRandomRule extends AbstractLoadBalancerRule {

    private int total = 0;
    private int currentIndex = 0;
    public MyRandomRule() {
    }

    //@SuppressWarnings({"RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE"})
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        } else {
            Server server = null;

            while(server == null) {
                if (Thread.interrupted()) {
                    return null;
                }

                List<Server> upList = lb.getReachableServers();
                List<Server> allList = lb.getAllServers();
                int serverCount =upList.size();
                if (serverCount == 0) {
                    return null;
                }

                if (total<5&&currentIndex<serverCount-1){
                    total++;
                } else if (total==5&&currentIndex<serverCount-1){
                    total=0;
                    currentIndex++;
                }else if(currentIndex>=serverCount-1){
                    currentIndex=0;
                    total = 0;
                }

                server = (Server)upList.get(currentIndex);
                if (server == null) {
                    Thread.yield();
                } else {
                    if (server.isAlive()) {
                        return server;
                    }

                    server = null;
                    Thread.yield();
                }
            }

            return server;
        }
    }

    /*protected int chooseRandomInt(int serverCount) {
        return ThreadLocalRandom.current().nextInt(serverCount);
    }*/

    public Server choose(Object key) {
        return this.choose(this.getLoadBalancer(), key);
    }

    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }
}
