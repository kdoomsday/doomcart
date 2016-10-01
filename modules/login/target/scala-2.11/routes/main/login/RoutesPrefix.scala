
// @GENERATOR:play-routes-compiler
// @SOURCE:/opt/sbtworkspace/doomcart/modules/login/conf/login.routes
// @DATE:Fri Sep 30 21:51:40 VET 2016


package login {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
