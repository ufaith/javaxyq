/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 龚德伟
 * @history 2008-6-8 龚德伟 新建
 */
public class TalkConfig implements Config {
    
    private String id;

    private String text;

    private List<LinkConfig> links = new ArrayList<LinkConfig>();

    public TalkConfig(String text) {
        this.text = text;
    }

    public void addLink(LinkConfig link) {
        this.links.add(link);
    }

    public String getType() {
        return "talk";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<LinkConfig> getLinks() {
        return links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
