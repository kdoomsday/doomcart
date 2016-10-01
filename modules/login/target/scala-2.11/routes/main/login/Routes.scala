
// @GENERATOR:play-routes-compiler
// @SOURCE:/opt/sbtworkspace/doomcart/modules/login/conf/login.routes
// @DATE:Fri Sep 30 21:51:40 VET 2016

package login

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:1
  LoginController_0: controllers.login.LoginController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:1
    LoginController_0: controllers.login.LoginController
  ) = this(errorHandler, LoginController_0, "/")

  import ReverseRouteContext.empty

  def withPrefix(prefix: String): Routes = {
    login.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, LoginController_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.login.LoginController.loginPage"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:1
  private[this] lazy val controllers_login_LoginController_loginPage0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_login_LoginController_loginPage0_invoker = createInvoker(
    LoginController_0.loginPage,
    HandlerDef(this.getClass.getClassLoader,
      "login",
      "controllers.login.LoginController",
      "loginPage",
      Nil,
      "GET",
      """""",
      this.prefix + """"""
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:1
    case controllers_login_LoginController_loginPage0_route(params) =>
      call { 
        controllers_login_LoginController_loginPage0_invoker.call(LoginController_0.loginPage)
      }
  }
}
