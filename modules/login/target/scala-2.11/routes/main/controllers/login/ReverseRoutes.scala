
// @GENERATOR:play-routes-compiler
// @SOURCE:/opt/sbtworkspace/doomcart/modules/login/conf/login.routes
// @DATE:Fri Sep 30 21:51:40 VET 2016

import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:1
package controllers.login {

  // @LINE:1
  class ReverseLoginController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:1
    def loginPage(): Call = {
      import ReverseRouteContext.empty
      Call("GET", _prefix)
    }
  
  }


}
