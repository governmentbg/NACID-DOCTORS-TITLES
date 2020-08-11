package com.nacid.web;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.nacid.web.config.xml.Group;
import com.nacid.web.config.xml.Handler2Group;
import com.nacid.web.config.xml.Webconfig;

/**
 * Loads the data that maps handlers to groupIds
 */
public class HandlerToGroupManager implements java.io.Serializable {
  
  private static Map<String, Integer> handler2GroupMap = new HashMap<String, Integer>();
  private String configLocation;
  public HandlerToGroupManager(String configLocation) {
      this.configLocation = configLocation;
  }
  public void init() {
    parseConfig();
  }

  private void parseConfig() {
    try {
      JAXBContext jc = JAXBContext.newInstance("com.nacid.web.config.xml");
      Unmarshaller unmarshaller = jc.createUnmarshaller();

      Webconfig webconfig = (Webconfig) unmarshaller.unmarshal(HandlerToGroupManager.class.getClassLoader().getResourceAsStream(
              configLocation));
      Handler2Group handler2Group = webconfig.getHandler2Group();
      
      for (Group group : handler2Group.getGroup()) {
        handler2GroupMap.put(group.getHandler(), group.getGroupid());
      }
    } catch (Exception e) {
    	e.printStackTrace();
    }
  }

  /**
   * Get groupId for given handler
   * @param screen
   * @return
   */
  public Integer getGroupId(String handlerName) {
    return handler2GroupMap.get(handlerName);
   }
  
  public static Map<String, Integer> getHandler2GroupMap() {
	  return handler2GroupMap;
  }

  public static void main(String[] args) throws Exception {
    HandlerToGroupManager screenManager = new HandlerToGroupManager("com/nacid/web/config/xml/webconfig.xml");
    screenManager.parseConfig();
  }

}
