package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.jclouds.services.ComputeService;
import io.cloudslang.content.jclouds.services.JcloudsComputeService;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.domain.Location;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by persdana on 6/5/2015.
 */
public class ComputeServiceImpl extends JcloudsComputeService implements ComputeService{
    private static final String NOT_IMPLEMENTED_ERROR_MESSAGE = "Not implemented. Use 'amazon\' or 'openstack' providers in the provider input";

    org.jclouds.compute.ComputeService computeService = null;

    private String provider;
    protected String region;

    public ComputeServiceImpl(String provider, String endpoint, String identity, String credential, String proxyHost, String proxyPort) {
        super(endpoint, identity, credential, proxyHost, proxyPort);
        this.provider = provider;
    }

    protected void init() {
        ContextBuilder contextBuilder = super.init(region, provider);
        ComputeServiceContext context = contextBuilder.buildView(ComputeServiceContext.class);
        computeService = context.getComputeService();
    }

    protected void lazyInit() {
        if(computeService == null) {
            this.init();
        }
    }

    protected void lazyInit(String region) {
        if(this.region == null || !this.region.equals(region)) {
            this.region = region;
            this.init();
        } else if(computeService == null) {
            this.init();
        }
    }

    @Override
    public void resume(String region, String serverId) {
        lazyInit(region);
        computeService.resumeNode(region + "/" + serverId);
    }

    @Override
    public String removeServer(String region, String serverId) {
        lazyInit(region);
        computeService.destroyNode(serverId);
        return "Server Removed";
    }

    @Override
    public String suspend(String region, String serverId) {
        lazyInit(region);
        computeService.suspendNode(region + "/" + serverId);

        return "";
    }


    protected void reboot(String region, String serverId) {
        lazyInit(region);
        computeService.rebootNode(region + "/" + serverId);
    }

    @Override
    public String start(String region, String serverId) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    @Override
    public String stop(String region, String serverId) throws Exception {
        throw new Exception(NOT_IMPLEMENTED_ERROR_MESSAGE);
    }

    public void softReboot(String region, String serverId) {
        reboot(region, serverId);
    }

    public void hardReboot(String region, String serverId) {
        reboot(region, serverId);
    }

    public Set<String> listRegions() {
        lazyInit();
        Set<? extends Location> locations = computeService.listAssignableLocations();
        Set<String> res = new HashSet<>();
        for(Location l : locations) {
            res.add(l.getDescription());
        }

        return res;
    }

    @Override
    public Set<String> listNodes(String region) {
        lazyInit(region);
        Set<? extends ComputeMetadata> nodes = computeService.listNodes();
        Set<String> result = new HashSet<>();
        for(ComputeMetadata cm: nodes) {
            result.add(cm.toString());
        }
        return result;
    }

    public Set<String> listNodes() {
        lazyInit();
        Set<? extends ComputeMetadata> locations = computeService.listNodes();
        Set<String> res = new HashSet<>();
        for(ComputeMetadata cm : locations) {
            res.add(cm.toString());
        }
        return res;
    }

    public String createServer(String region, String name, String imageRef, String flavorRef) throws Exception {

        throw new Exception("not implemented yet");
//        String res = null;
//        lazyInit(region);
//
//        Template template = computeService.templateBuilder().build();
//
//        template.getOptions().as(EC2TemplateOptions.class)
//                .authorizePublicKey("aaa");
//
//        computeService.createNodesInGroup(region, 1);
//
//        return res;
    }
}
