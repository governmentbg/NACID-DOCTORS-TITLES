package com.nacid.web;

import com.nacid.web.config.xml.Screen;
import com.nacid.web.config.xml.Screen2Jsp;
import com.nacid.web.config.xml.Webconfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads the data that maps screen name to JSP templates, e.g. searchResult state corresponds to /search/searchResult.jsp.
 * ScreenManager itself is stored in servlet context.
 * ScreenManager knows which screen to show next.
 */
public class ScreenManager implements java.io.Serializable {
  
  public static String DEFAULT_SCREEN = "home";
  private String configLocation;
  private static Map<String, String> screen2jspMap = new HashMap<String, String>();
  public ScreenManager(String configLocation) {
   this.configLocation = configLocation;   
  }

  public void init() {
    parseConfig();
  }

  private void parseConfig() {
    try {
      JAXBContext jc = JAXBContext.newInstance("com.nacid.web.config.xml");
      Unmarshaller unmarshaller = jc.createUnmarshaller();

      Webconfig webconfig = (Webconfig) unmarshaller.unmarshal(ScreenManager.class.getClassLoader().getResourceAsStream(
              configLocation ));
      Screen2Jsp screen2jsp = webconfig.getScreen2Jsp();
      
      for (Screen screen : screen2jsp.getScreen()) {
        screen2jspMap.put(screen.getName(), screen.getJspTemplate());
      }
    } catch (Exception e) {
    	e.printStackTrace();
    }
  }

  /**
   * Get the jsp template for the given screen.
   * @param screen
   * @return
   */
  public String getJspTemplate(String screen, boolean returnDefault) {
    if (screen != null) {
      screen = screen2jspMap.get(screen);
    }
    if (screen == null && returnDefault) {
      // returns a default one
      screen = screen2jspMap.get(DEFAULT_SCREEN);
    }

    return screen;
  }


  public static void main(String[] args) throws Exception {
    String url = "com/ext/nacid/regprof/web/config/xml/webconfig.xml";
      RequestProcessor rp = new RequestProcessor(url);
      rp.init();
      HandlerToGroupManager hgm = new HandlerToGroupManager(url);
      hgm.init();
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      for (String s : rp.getUrlActionMapping().keySet()) {
          String handler = rp.getUrlActionMapping().get(s);
          Integer sec = hgm.getGroupId(handler);
          if (sec != null) {
              String role = sec == 1 ? "security.role.user"  : "security.role.expert";
              List<String> list = map.get(role);
              if (list == null) {
                  list = new ArrayList<String>();
                  map.put(role, list);
              }
              list.add("<sec:intercept-url pattern=\"/control" + s + "/**\" access=\"${" + role + "}\" />");

          }
          //System.out.println(s + "  " + hgm.getGroupId(handler));
      }
      for (String s : map.keySet()) {
          for (String s1 : map.get(s)) {
              System.out.println(s1);
          }
      }

  }

}
