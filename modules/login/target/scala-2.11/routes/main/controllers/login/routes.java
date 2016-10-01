
// @GENERATOR:play-routes-compiler
// @SOURCE:/opt/sbtworkspace/doomcart/modules/login/conf/login.routes
// @DATE:Fri Sep 30 21:51:40 VET 2016

package controllers.login;

import login.RoutesPrefix;

public class routes {
  
  public static final controllers.login.ReverseLoginController LoginController = new controllers.login.ReverseLoginController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.login.javascript.ReverseLoginController LoginController = new controllers.login.javascript.ReverseLoginController(RoutesPrefix.byNamePrefix());
  }

}
